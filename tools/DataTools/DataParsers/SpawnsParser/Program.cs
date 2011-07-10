namespace Jamie.Npcs
{
    using System;
    using System.Windows.Forms;
    using System.Diagnostics;
    using System.Xml.Serialization;
    using System.Xml;
    using System.IO;
    using System.Collections.Generic;
    using Jamie.ParserBase;
    using System.Text;
    using System.Linq;
    using System.Globalization;
    using System.ComponentModel;

    class Program
    {
        [STAThread]
        static void Main() {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            Application.Run(new MainForm());
        }
    }

    public partial class MainForm
    {
        static string root = AppDomain.CurrentDomain.SetupInformation.ApplicationBase;

        void DoParse(string spawnPath, string[] missionFiles, SpawnParseType spawnType,
                     bool fixCoords, bool exportMissing) {
            Utility.LoadStrings(root);

            if (spawnType == SpawnParseType.Gather)
                Utility.LoadClientGatherables(root);

            Utility.LoadClientNpcs(root);

            Dictionary<string, MissionObjects> objects = new Dictionary<string, MissionObjects>();

            foreach (string file in missionFiles) {
                DirectoryInfo levelDir = new DirectoryInfo(file);
                Utility.LoadMissionFile(file);
                string folderName = levelDir.Parent.Name;
                string[] customNameParts = folderName.Split('-');
                objects.Add(customNameParts[0], Utility.MissionFile.Objects[0]);
            }

            string outputPath = Path.Combine(root, @"output");
            if (!Directory.Exists(outputPath))
                Directory.CreateDirectory(outputPath);

            var settings = new XmlWriterSettings()
            {
                CheckCharacters = false,
                CloseOutput = false,
                Indent = true,
                IndentChars = "\t",
                NewLineChars = "\n",
                Encoding = new UTF8Encoding(false)
            };

            CultureInfo ci = new CultureInfo("");

            Func<ObjectTypes, bool> checkFunc = (ObjectTypes type) =>
            {
                if (spawnType == SpawnParseType.Gather)
                    return type == ObjectTypes.HSP;
                else
                    return type == ObjectTypes.SP || type == ObjectTypes.Entity;
            };

            foreach (var pair in objects) {
                IEnumerable<ISpawnData> spawnObjects = Enumerable.Empty<ISpawnData>();

                if (pair.Value.Object != null)
                    spawnObjects = spawnObjects.Union(pair.Value.Object.Where(o => checkFunc.Invoke(o.Type) &&
                                                                                   !String.IsNullOrEmpty(o.npc))
                                                                       .Select(o => (ISpawnData)o));
                if (pair.Value.Entity != null)
                    spawnObjects = spawnObjects.Union(pair.Value.Entity.Where(o => checkFunc.Invoke(o.Type) &&
                                                                                   !String.IsNullOrEmpty(o.Name))
                                                                       .Select(o => (ISpawnData)o));
                if (!spawnObjects.Any())
                    continue;

                var groupedSpawns = spawnObjects.GroupBy(o => o.Name);

                SpawnsFile outputFile = new SpawnsFile();
                List<SpawnGroup> spawnGroups = new List<SpawnGroup>();

                foreach (var gr in groupedSpawns) {
                    string npcName = gr.Key;

                    GatherSource source = null;
                    Npc npc = null;
                    int npcid = 0;

                    if (spawnType == SpawnParseType.Gather) {
                        source = Utility.GatherSrcFile[npcName];
                        if (source == null) {
                            Debug.Print("Missing data for NPC: '{0}'", npcName);
                            continue;
                        }
                        npcid = source.id;
                    } else {
                        npcid = Utility.ClientNpcIndex[npcName];
                        if (npcid == -1) {
                            npcName = GetAlternateNpcName(ClientLevelMap.mapToId[pair.Key], npcName);
                            npcid = Utility.ClientNpcIndex[npcName];
                            if (npcid == -1) {
                                if (npcName.IndexOf("StoryBook", StringComparison.CurrentCultureIgnoreCase) != -1)
                                    npcid = fakeBookId++;
                                else if (!npcName.StartsWith("ParticleEffect") &&
                                    !npcName.StartsWith("Billboard") &&
                                    !npcName.StartsWith("Chair") &&
                                    !npcName.StartsWith("env_") &&
                                    !npcName.StartsWith("Milestone") &&
                                    !npcName.StartsWith("sys_event") &&
                                    !npcName.StartsWith("sys_proc") &&
                                    !npcName.StartsWith("Boids_"))
                                Debug.Print("Missing data for NPC: '{0}'", npcName);
                                if (npcid < 0)
                                    continue;
                            }
                        }
                        npc = Utility.ClientNpcIndex[npcid];
                        if (npc != null) {
                            bool isMonster = npc.ui_type == "monster" ||
                                             npc.npc_type == Jamie.ParserBase.NpcType.Monster ||
                                             npc.dir != null && npc.dir.StartsWith("Monster", StringComparison.CurrentCultureIgnoreCase);
                            if (spawnType == SpawnParseType.Monsters) {
                                if (!isMonster)
                                    continue;
                            } else if (isMonster)
                                continue;
                        }
                    }

                    var spGr = new SpawnGroup();
                    spGr.npcid = npcid;
                    int count = gr.Count();
                    spGr.@object = new SpawnGroupObject[count];
                    for (int i = 0; i < count; i++) {
                        var obj = new SpawnGroupObject();
                        ISpawnData mo = gr.ElementAt(i);
                        string[] coords = mo.Pos.Split(',');
                        obj.x = Decimal.Parse(coords[0], ci);
                        obj.y = Decimal.Parse(coords[1], ci);
                        obj.z = Decimal.Parse(coords[2], ci);
                        obj.staticid = mo.EntityId;
                        int dir = mo.dir;
                        if (!String.IsNullOrEmpty(mo.Angles)) {
                            string[] angles = mo.Angles.Split(',');
                            int c = (int)Math.Ceiling(Single.Parse(angles[2], ci)) / 3;
                            if (c < 0)
                                c = 120 + c;
                            c -= 30;
                            //if (mo.use_dir == 0)
                            //    c -= dir / 3;
                            if (c < 0)
                                c += 120;
                            obj.h = (sbyte)c;
                        }
                        spGr.@object[i] = obj;
                    }
                    spGr.@object = spGr.@object.OrderBy(o => o.x).ThenBy(o => o.y).ThenBy(o => o.z).ToArray();
                    spGr.interval = spawnType == SpawnParseType.Gather ? 230 : 295;
                    if (spawnType == SpawnParseType.Gather) {
                        spGr.name = Utility.StringIndex.GetString(source.desc);
                        spGr.type = source.source_type;
                        spGr.upper = source.source_upper;
                        spGr.level = source.skill_level;
                    } else {
                        if (npc != null)
                            spGr.name = Utility.StringIndex.GetString(npc.desc);
                    }
                    spawnGroups.Add(spGr);
                }

                if (spawnGroups.Count == 0) {
                    string existing = Path.Combine(outputPath, String.Format("{0}.xml", ClientLevelMap.mapToId[pair.Key]));
                    if (File.Exists(existing))
                        File.Delete(existing);
                    continue;
                }

                outputFile.Spawns = new SpawnGroup[spawnGroups.Count];
                int idx = 0;
                foreach (var sg in spawnGroups.OrderBy(sp => sp.npcid)) {
                    sg.map = ClientLevelMap.mapToId[pair.Key];
                    outputFile.Spawns[idx++] = sg;
                }

                string[] p = Directory.GetFiles(Path.Combine(spawnPath, spawnType.ToString()),
                                                String.Format("{0}.xml", ClientLevelMap.mapToId[pair.Key]));
                if (p.Length == 0) {
                    Utility.SpawnOrigFile = new SpawnsFile();
                    Utility.SpawnOrigFile.Spawns = new SpawnGroup[0];
                } else {
                    Utility.LoadOurSpawns(p[0], spawnType == SpawnParseType.Gather);
                }

                if (spawnType != SpawnParseType.Gather) {
                    SpawnParseType otherType = spawnType == SpawnParseType.Monsters ? SpawnParseType.Npcs :
                        SpawnParseType.Monsters;
                    p = Directory.GetFiles(Path.Combine(spawnPath, otherType.ToString()),
                                                        String.Format("{0}.xml", ClientLevelMap.mapToId[pair.Key]));

                    var firstFile = Utility.SpawnOrigFile.Spawns.ToList();
                    if (p.Length != 0) {
                        Utility.LoadOurSpawns(p[0], false);
                        firstFile.AddRange(Utility.SpawnOrigFile.Spawns);
                    }

                    p = Directory.GetFiles(Path.Combine(spawnPath, "Instances"),
                                                        String.Format("{0}.xml", ClientLevelMap.mapToId[pair.Key]));
                    if (p.Length != 0) {
                        Utility.LoadOurSpawns(p[0], false);
                        firstFile.AddRange(Utility.SpawnOrigFile.Spawns);
                    }

                    // remove not needed
                    for (int i = firstFile.Count - 1; i >= 0; i--) {
                        Npc npc = Utility.ClientNpcIndex[firstFile[i].npcid];
                        if (npc == null) {
                            Debug.Print("Removing invalid NPC: {0} ({1})", firstFile[i].npcid,
                                        ClientLevelMap.mapToId[pair.Key]);
                            firstFile.RemoveAt(i);
                            continue;
                        }
                        bool isMonster = npc.ui_type == "monster" ||
                                         npc.npc_type == Jamie.ParserBase.NpcType.Monster ||
                                         npc.dir != null && npc.dir.StartsWith("Monster", StringComparison.CurrentCultureIgnoreCase);
                        if (spawnType == SpawnParseType.Monsters && !isMonster ||
                            spawnType == SpawnParseType.Npcs && isMonster)
                            firstFile.RemoveAt(i);
                    }
                    Utility.SpawnOrigFile.Spawns = firstFile.ToArray();
                }

                HashSet<SpawnGroup> changedId = new HashSet<SpawnGroup>();

                foreach (var os in outputFile.Spawns) {
                    var test = Utility.SpawnOrigFile.Spawns.Where(s => s.npcid == os.npcid);
                    if (test.Any()) {
                        os.name = "";
                    }

                    var spgr = Utility.SpawnOrigFile.Spawns.Where(s => s.name == os.name);
                    var samesp = spgr.SelectMany(s => s.@object);
                    
                    if (spgr.Any()) {
                        SpawnGroup first = spgr.First();
                        os.interval = first.interval;
                        if (first.rw != os.rw)
                            os.rw = first.rw;
                        if (first.time != os.time)
                            os.time = first.time;
                    }

                    if (spawnType != SpawnParseType.Gather)
                        os.pool = os.@object.Length;

                    int total = 0;
                    decimal delta = 0;
                    List<SpawnGroupObject> skipped = new List<SpawnGroupObject>();
 
                    foreach (var same in os.@object) {
                        var obj = FindClosestSpawn(samesp, same.x, same.y, same.z);
                        if (obj == null) {
                            skipped.Add(same);
                            continue;
                        }

                        var originalGroup = spgr.Where(s => s.@object.Contains(obj)).First();
                        if (os.npcid != originalGroup.npcid)
                            changedId.Add(os);
                        if (same.staticid != 0) {
                            originalGroup = spgr.Where(s => s.@object.Where(o => o.staticid == same.staticid).Any())
                                                .FirstOrDefault();
                            if (originalGroup == null)
                                changedId.Add(os);
                        }

                        bool canFix = false;
                        if (spawnType != SpawnParseType.Gather) {
                            Npc npc = Utility.ClientNpcIndex[os.npcid];
                            if (npc == null) {
                                // don't export
                                os.name = String.Empty;
                                continue;
                            }
                            // don't fix, chairs need to be adjusted too
                            canFix = fixCoords && !String.IsNullOrEmpty(npc.idle_animation) &&
                                !npc.idle_animation.StartsWith("idle_chair", StringComparison.InvariantCultureIgnoreCase);
                        }
                        if (!fixCoords || !canFix) {
                            same.x = obj.x;
                            same.y = obj.y;
                            same.h = obj.h;
                        }

                        delta += same.z - obj.z;
                        same.z = obj.z;
                        same.w = obj.w;
                        total++;
                    }
                    if (total > 0) {
                        delta /= total;

                        Debug.Print("[{0} ({1},{2})] Z-delta: {3}; upper: {4}; type: {5}",
                            os.name, os.npcid, os.map, Math.Round(delta, 5), os.upper, os.type);
                        foreach (var skip in skipped) {
                            skip.z -= delta;
                            skip.z = Math.Round(skip.z, 5);
                        }
                    }
                }

                var namesSaved = outputFile.Spawns.Select(s => s.name).Distinct();
                var namesOrig = Utility.SpawnOrigFile.Spawns.Select(s => s.name).Distinct();

                List<SpawnGroup> toAdd = new List<SpawnGroup>();

                if (exportMissing) {
                    var addNew = namesSaved.Except(namesOrig);
                    if (addNew.Any()) {
                        foreach (var so in outputFile.Spawns) {
                            if (addNew.Contains(so.name))
                                toAdd.Add(so);
                        }
                    }
                    toAdd.AddRange(changedId);
                } else {
                    var addOur = namesOrig.Except(namesSaved);
                    if (addOur.Any()) {
                        foreach (var so in Utility.SpawnOrigFile.Spawns) {
                            if (addOur.Contains(so.name)) {
                                so.@object = so.@object.OrderBy(o => o.x).ThenBy(o => o.y).ThenBy(o => o.z).ToArray();
                                toAdd.Add(so);
                            }
                        }
                    }
                    toAdd.AddRange(outputFile.Spawns);
                }

                if (spawnType == SpawnParseType.Gather)
                    outputFile.Spawns = toAdd.OrderBy(s => s.npcid).ToArray();
                else
                    outputFile.Spawns = toAdd.Where(s => s.name != "").OrderBy(s => s.name).ToArray();

                if (outputFile.Spawns.Length == 0)
                    continue;

                try {
                    using (var fs = new FileStream(Path.Combine(outputPath,
                                                   String.Format("{0}.xml",
                                                   ClientLevelMap.mapToId[pair.Key])),
                                                   FileMode.Create, FileAccess.Write))
                    using (var writer = XmlWriter.Create(fs, settings)) {
                        XmlSerializer ser = new XmlSerializer(typeof(SpawnsFile));
                        ser.Serialize(writer, outputFile);
                    }
                } catch (Exception ex) {
                    Debug.Print(ex.ToString());
                }
            }
        }

        SpawnGroupObject FindClosestSpawn(IEnumerable<SpawnGroupObject> sp, decimal x, decimal y, decimal z) {
            var objects = sp.Where(o => Math.Abs(o.x - x) < 35 && Math.Abs(o.y - y) < 35);
            double distance = 100;
            SpawnGroupObject result = null;

            foreach (var obj in objects) {
                double dist = Math.Sqrt(Math.Pow((double)(obj.x - x), 2) +
                                        Math.Pow((double)(obj.y - y), 2) +
                                        Math.Pow((double)(obj.z - z), 2));
                if (distance > dist) {
                    result = obj;
                    distance = dist;
                }
            }
            return result;
        }

        static int fakeBookId = 1000000;

        public string GetAlternateNpcName(int mapId, string name) {
            if (name.StartsWith("surkana", StringComparison.InvariantCultureIgnoreCase)) {
                if (mapId == 300210000) {
                    name = "IDDreadgion_02_" + name;
                } else if (mapId == 300110000) {
                    name = "IDAb1_Dreadgion_" + name;
                }
            } else {
                int idx = name.IndexOf("_StoryBook_", StringComparison.InvariantCultureIgnoreCase);
                if (idx != -1) {
                    name = name.Replace("1.9v_", String.Empty);
                    return name;
                }
                idx = name.IndexOf("book_drawn", StringComparison.InvariantCultureIgnoreCase);
                if (idx != -1) {
                    string prefix = String.Empty;
                    if (mapId == 110010000)
                        prefix = "LC1_StoryBook_";
                    else if (mapId == 120010000)
                        prefix = "DC1_StoryBook_";
                    else
                        prefix = "StoryBook_";
                    name = prefix + name.Remove(0, idx + 10);
                    return name;
                }
            }

            return name;
        }
    }
}
