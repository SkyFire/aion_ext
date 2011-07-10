using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Jamie.ParserBase;
using System.Diagnostics;
using System.Xml;
using System.IO;
using System.Xml.Serialization;

namespace Jamie.Npcs
{
    class Program
    {
        static string root = AppDomain.CurrentDomain.SetupInformation.ApplicationBase;

        static void Main(string[] args) {
            Utility.WriteExeDetails();
            Console.WriteLine("Loading strings...");
            Utility.LoadStrings(root);

            Console.WriteLine("Loading NPCs...");
            Utility.LoadClientNpcs(root);

            Console.WriteLine("Loading NPC templates...");
            Utility.LoadNpcsTemplate(root);

            var gossips = Utility.StringIndex.StringList.Where(s => s.name.StartsWith("STR_CHAT_") &&
                                                                    s.id > 1500000 ||
                                                                    s.id >= 340000 && s.id < 350000 ||
                                                                    s.id >= 390000 && s.id < 392000);
            string outputPath = Path.Combine(root, @"output");
            if (!Directory.Exists(outputPath))
                Directory.CreateDirectory(outputPath);

            NpcShouts outputFile = new NpcShouts();

            var settings = new XmlWriterSettings()
            {
                CheckCharacters = false,
                CloseOutput = false,
                Indent = true,
                IndentChars = "\t",
                NewLineChars = "\n",
                Encoding = new UTF8Encoding(false)
            };

            foreach (var gossip in gossips) {
                string npcName = gossip.name.Remove(0, 9);
                if (npcName.StartsWith("IDAbRe_Core") || npcName.StartsWith("IDDreadgion") ||
                    npcName.StartsWith("IDTemple") || npcName.StartsWith("IDCatacombs") ||
                    npcName.StartsWith("BLF4_Henchman") || npcName.StartsWith("NLehpar") ||
                    npcName.StartsWith("Lycan_") || npcName.StartsWith("Brownie_") ||
                    npcName.StartsWith("Krall_") || npcName.StartsWith("NKrall_") ||
                    npcName.StartsWith("NBrownie_") || npcName.StartsWith("ND2_")) {
                    if (!npcName.EndsWith("_s") && !npcName.EndsWith("_S") &&
                        !npcName.EndsWith("_b"))
                        npcName += "_s";
                }
                string[] nameParts = npcName.Split('_');
                if (nameParts.Length < 3)
                    continue;
                string type = nameParts[nameParts.Length - 1].ToLower();
                string eventType = nameParts[nameParts.Length - 2].ToLower();
                string number = nameParts[nameParts.Length - 3];

                int seq = -1;
                Gossip g = new Gossip();
                g.stringId = gossip.id;
                g.NpcList = new List<int>();

                if (type == "waypoint" || nameParts[0] == "IDElim" || nameParts[0] == "CKrall") {
                    eventType = type;
                    type = "s";
                    Array.Resize(ref nameParts, nameParts.Length + 1);
                    nameParts[nameParts.Length - 1] = type;
                    if (nameParts[0] == "IDElim") {
                        if (Int32.TryParse(eventType, out seq)) {
                            eventType = nameParts[nameParts.Length - 3].ToLower();
                            nameParts[nameParts.Length - 2] = eventType;
                            nameParts[nameParts.Length - 3] = seq.ToString();
                            number = nameParts[nameParts.Length - 3];
                        } else
                            number = "-1";
                    } else
                        number = nameParts[nameParts.Length - 3];
                }

                if (type != "s" && type != "b" && type != "h") {
                    if (Int32.TryParse(type, out seq)) {
                        IEnumerable<StringDescription> descriptions = null;
                        if (eventType == "orcwarrior") {
                            descriptions = Utility.StringIndex.StringList.Where(n => n.name.StartsWith("STR_ORCWARRIOR"));
                        }
                        if (descriptions != null && descriptions.Any()) {
                            foreach (var descr in descriptions) {
                                int npcId = Utility.ClientNpcIndex[descr.name];
                                if (npcId != -1) {
                                    g.NpcList.Add(npcId);
                                }
                            }
                            if (g.NpcList.Count > 0) {
                                AddGossipToOutput(outputFile, g);
                                continue;
                            }
                        }
                    }
                    Debug.Print("Unknown name: {0}", npcName);
                    continue;
                }

                ShoutEventType gType = ShoutEventType.NONE;
                if (eventType.StartsWith("idle"))
                    gType = ShoutEventType.IDLE;
                else if (eventType.StartsWith("batk") || eventType.StartsWith("katk") ||
                         eventType.StartsWith("atk") || eventType.StartsWith("eatk") || eventType == "attack")
                    gType = ShoutEventType.ATK;
                else if (eventType.StartsWith("kcast") || eventType.StartsWith("bcast") ||
                         eventType.StartsWith("kcsat"))
                    gType = ShoutEventType.CAST;
                else if (eventType.StartsWith("die"))
                    gType = ShoutEventType.DIE;
                else if (eventType.StartsWith("start"))
                    gType = ShoutEventType.START;
                else if (eventType.StartsWith("waypoint"))
                    gType = ShoutEventType.WAYPOINT;
                else if (eventType.StartsWith("skill"))
                    gType = ShoutEventType.SKILL;
                else if (eventType.StartsWith("help"))
                    gType = ShoutEventType.HELP;
                else if (eventType.Contains("fail"))
                    gType = ShoutEventType.FAIL;
                else if (eventType.StartsWith("seeuser"))
                    gType = ShoutEventType.SEEUSER;
                else if (eventType.StartsWith("wakeup"))
                    gType = ShoutEventType.WAKEUP;
                else if (eventType.StartsWith("win"))
                    gType = ShoutEventType.WIN;
                else if (eventType.StartsWith("despawn"))
                    gType = ShoutEventType.DESPAWN;
                else if (eventType.StartsWith("wounded"))
                    gType = ShoutEventType.WOUNDED;
                else if (eventType.StartsWith("flee"))
                    gType = ShoutEventType.FLEE;
                else if (eventType.StartsWith("yell"))
                    gType = ShoutEventType.YELL;
                else if (eventType.StartsWith("leave"))
                    gType = ShoutEventType.LEAVE;
                else if (eventType.StartsWith("sleep"))
                    gType = ShoutEventType.SLEEP;
                else if (eventType.StartsWith("resethate"))
                    gType = ShoutEventType.RESETHATE;
                else if (eventType.StartsWith("swichtarget"))
                    gType = ShoutEventType.SWICHTARGET;
                else if (eventType == "direction")
                    gType = ShoutEventType.DIRECTION;
                else if (eventType == "statup")
                    gType = ShoutEventType.STATUP;
                else {
                    Debug.Print("Unknown event: {0}", eventType);
                    continue;
                }

                if (Int32.TryParse(number, out seq)) {
                    npcName = String.Join("_", nameParts, 0, nameParts.Length - 3);
                } else {
                    npcName = String.Join("_", nameParts, 0, nameParts.Length - 2);
                }

                g.Event = gType;

                var npc = Utility.ClientNpcIndex[npcName];
                if (npc != -1) {
                    g.NpcList.Add(npc);
                } else if (seq == -1 || nameParts.Length > 3) {
                    string woPrefix = seq == -1 ? String.Join("_", nameParts, 1, nameParts.Length - 3)
                                                : String.Join("_", nameParts, 1, nameParts.Length - 4);
                    npc = Utility.ClientNpcIndex[woPrefix];
                    if (npc != -1) {
                        g.NpcList.Add(npc);
                    } else if (npcName == "E3") { // There are NPCs with names NPC_E3_
                    } else if (npcName == "AbPRo") {
                    } else if (npcName == "AbPRoD2") {
                    } else if (npcName == "GuardianAssassin") {
                        var all = Utility.OriginalNpcTemplate.GetNpcsFromTitleKey("assassin");
                        foreach (int id in all) {
                            string title = Utility.OriginalNpcTemplate.GetTitle(id);
                            var match = Utility.ClientNpcIndex[id];
                        }
                    } else if (npcName == "LF1Shugo") {
                    } else if (npcName == "LF1Guard") {
                        var all = Utility.OriginalNpcTemplate.GetNpcsFromTitleKey("guard");
                        foreach (int id in all) {
                            string title = Utility.OriginalNpcTemplate.GetTitle(id);
                            var match = Utility.ClientNpcIndex[id];
                        }
                    } else if (npcName == "LF4_Guard") {
                    } else if (npcName == "LF4_ShugoManager") {
                    } else if (npcName == "LF4_ShugoMaker") {
                    } else if (npcName == "LF4_ShugoWriter") {
                    } else if (npcName == "LF4_ShugoSupplyer") {
                    } else if (npcName == "DF4_Guard") {
                    } else if (npcName == "LF1aGuard") {
                    } else if (npcName == "LF1NPC") {
                    } else if (npcName == "LF1aNPC") {
                    } else if (nameParts[2] == "Wonsikuts") {
                        string[] stNameParts = npcName.Split('_');
                        stNameParts[2] = "LehparKNWonsikuts";
                        string name = String.Join("_", stNameParts);
                        var npcs = Utility.ClientNpcIndex.NpcList.Where(n => n.name.StartsWith(name));
                        foreach (var cn in npcs) {
                            g.NpcList.Add(cn.id);
                        }
                    } else if (npcName.StartsWith("BD3_Lizard_") || npcName.StartsWith("BD3_Naga_")) {
                        string[] stNameParts = woPrefix.Split('_');
                        List<string> names = new List<string>();
                        if (stNameParts[1] == "BeastKA") {
                            string startingName = String.Concat(nameParts[0], "_", stNameParts[0], "_",
                                                                "BeastK", "_", stNameParts[2][0]);
                            names.Add(startingName);
                            startingName = String.Concat(nameParts[0], "_", stNameParts[0], "_",
                                                                "BeastA", "_", stNameParts[2][0]);
                            names.Add(startingName);
                        } else {
                            string startingName = String.Concat(nameParts[0], "_", stNameParts[0], "_",
                                                                stNameParts[1], "_", stNameParts[2][0]);
                            if (stNameParts[1][0] == 'R' || stNameParts[1][0] == 'W')
                                startingName = startingName.Substring(0, startingName.Length - 2);
                            names.Add(startingName);
                        }
                        foreach (string name in names) {
                            var npcs = Utility.ClientNpcIndex.NpcList.Where(n => n.name.StartsWith(name));
                            foreach (var cn in npcs) {
                                g.NpcList.Add(cn.id);
                            }
                        }
                    } else if (npcName.StartsWith("LF1_Lehpar")) {
                        var npcs = Utility.ClientNpcIndex.NpcList.Where(n => n.ai_name != null &&
                                                                             n.ai_name.StartsWith(woPrefix));
                        foreach (var cn in npcs) {
                            g.NpcList.Add(cn.id);
                        }
                    } else if (nameParts[0] == "PLehpar") {
                        string aiName = nameParts[0] + "_" + nameParts[1];
                        var npcs = Utility.ClientNpcIndex.NpcList.Where(n => n.ai_name != null &&
                                                                             n.ai_name.StartsWith(aiName));
                        foreach (var cn in npcs) {
                            g.NpcList.Add(cn.id);
                        }
                    } else if (npcName == "Ratman_FNR") {
                        var npcs = Utility.ClientNpcIndex.NpcList.Where(n => n.ai_name != null &&
                                                                             n.ai_name == "Ratman_FnR");
                        foreach (var cn in npcs) {
                            g.NpcList.Add(cn.id);
                        }
                    } else if (nameParts[0] == "Brownie") {
                        char[] chars = woPrefix.ToCharArray();
                        chars[1] = Char.ToLower(chars[1]);
                        string aiName = nameParts[0] + "_" + new String(chars);
                        var npcs = Utility.ClientNpcIndex.NpcList.Where(n => n.ai_name != null &&
                                                                             n.ai_name == aiName);
                        foreach (var cn in npcs) {
                            g.NpcList.Add(cn.id);
                        }
                    } else if (nameParts[0] == "Nlehpar" || nameParts[0] == "NLehpar" ||
                               nameParts[0] == "Krall" || nameParts[0] == "NKrall" ||
                               nameParts[0] == "CKrall" || nameParts[0] == "ND2") {
                        char[] chars = nameParts[0].ToCharArray();
                        if (nameParts[0][0] != 'K')
                            chars[1] = Char.ToUpper(chars[1]);
                        string aiName = new String(chars) + "_";
                        IEnumerable<Npc> npcs = null;
                        if (woPrefix.Length == 1) {
                            aiName += woPrefix;
                            npcs = Utility.ClientNpcIndex.NpcList.Where(n => n.ai_name != null &&
                                                                             n.ai_name.StartsWith(aiName));
                        } else {
                            int test;
                            if (Int32.TryParse(nameParts[2], out test))
                                woPrefix = nameParts[1];
                            if (woPrefix == "BAS") {
                                npcs = Utility.ClientNpcIndex.NpcList.Where(n => n.ai_name != null &&
                                                                                 (n.ai_name.StartsWith(aiName + "B") ||
                                                                                  n.ai_name.StartsWith(aiName + "A") ||
                                                                                  n.ai_name.StartsWith(aiName + "S")));

                            } else {
                                chars = woPrefix.ToCharArray();
                                chars[1] = Char.ToLower(chars[1]);
                                aiName += new String(chars);
                                npcs = Utility.ClientNpcIndex.NpcList.Where(n => n.ai_name != null &&
                                                                                 n.ai_name == aiName);
                                if (!npcs.Any())
                                    npcs = Utility.ClientNpcIndex.NpcList.Where(n => n.ai_name != null &&
                                                                                     n.ai_name.StartsWith(aiName));
                            }
                        }
                        if (!npcs.Any()) {
                            Debug.Print("Unknown name: {0}", npcName);
                        }
                        foreach (var cn in npcs) {
                            g.NpcList.Add(cn.id);
                        }
                    } else if (npcName == "Ratman_FNR_su") {
                        var npcs = Utility.ClientNpcIndex.NpcList.Where(n => n.ai_name != null &&
                                                                             n.ai_name.StartsWith("Ratman_FnR_LWaSu"));
                        foreach (var cn in npcs) {
                            g.NpcList.Add(cn.id);
                        }
                    } else if (nameParts[0] == "Lycan") {
                        char[] chars = woPrefix.ToCharArray();
                        chars[1] = Char.ToLower(chars[1]);
                        string aiName = nameParts[0] + "_" + new String(chars);
                        var npcs = Utility.ClientNpcIndex.NpcList.Where(n => n.ai_name != null &&
                                                                             n.ai_name == aiName);
                        foreach (var cn in npcs) {
                            g.NpcList.Add(cn.id);
                        }
                    } else if (npcName == "LF1Farmer") {
                        var npcs = Utility.ClientNpcIndex.NpcList.Where(n => n.tribe == "farmer_Hkerubim_lf1");
                        foreach (var cn in npcs) {
                            g.NpcList.Add(cn.id);
                        }
                    } else {
                        if (woPrefix == "DF1_Jailer") {
                            var npcs = Utility.ClientNpcIndex.NpcList.Where(n => n.name.StartsWith(woPrefix));
                            foreach (var cn in npcs) {
                                g.NpcList.Add(cn.id);
                            }
                        } else if (npcName.Contains("BroadCaster")) {
                            if (npcName.StartsWith("LF1a"))
                                g.NpcList.Add(203121);
                            else if (npcName.StartsWith("LC1"))
                                g.NpcList.Add(203737);
                            else if (npcName.StartsWith("DF1a"))
                                g.NpcList.Add(203658);
                            else if (npcName.StartsWith("LF2"))
                                g.NpcList.Add(203924);
                            else if (npcName.StartsWith("DF2")) {
                                if (npcName.EndsWith("D")) {
                                    // g.NpcList.Add(203737);
                                } else
                                    g.NpcList.Add(204323);
                            } else if (npcName.StartsWith("Ab1")) {
                                if (npcName.EndsWith("L")) {
                                    // g.NpcList.Add(203737);
                                }
                            }
                        } else if (npcName.StartsWith("Q")) {
                            if (npcName == "Q2240") {
                                g.Event = ShoutEventType.QUEST;
                                g.param = "2240";
                                g.NpcList.Add(203640);
                            }
                        } else if (npcName.StartsWith("LF3_Little")) {
                            npcName = npcName.Remove(0, 10);
                            int no = Int32.Parse(npcName);
                            npcName = String.Format("Tree_Move_Little_{0}", no);
                            var match = Utility.ClientNpcIndex.NpcList.Where(n => n.name == npcName).First();
                            g.NpcList.Add(match.id);
                        } else if (npcName == "IDDF3_lp_Stein") {
                            g.NpcList.Add(204814);
                        } else {
                            var npcs = Utility.ClientNpcIndex.NpcList.Where(n => n.name.StartsWith(npcName));
                            foreach (var cn in npcs) {
                                g.NpcList.Add(cn.id);
                            }
                            if (g.NpcList.Count == 0) {
                                if (npcName.StartsWith("IDCa")) {
                                    npcName = "IDCatacombsH" + npcName.Substring(11);
                                    npcs = Utility.ClientNpcIndex.NpcList.Where(n => n.name.StartsWith(npcName));
                                    foreach (var cn in npcs) {
                                        g.NpcList.Add(cn.id);
                                    }
                                    if (g.NpcList.Count == 0) {
                                        Debug.Print("Unknown name: {0}", npcName);
                                    }
                                } else if (npcName == "SYSTEM_TEST") {
                                    continue;
                                } else
                                    Debug.Print("Unknown name: {0}", npcName);
                            }
                        }
                    }
                } else {
                    if (npcName.StartsWith("Q")) {
                        // Q2258 - 204190
                        if (npcName == "Q2258") {
                            g.Event = ShoutEventType.QUEST;
                            g.param = "2258";
                            g.NpcList.Add(204190);
                        } else
                            Debug.Print("Unknown name: {0}", npcName);
                    } else
                        Debug.Print("Unknown name: {0}", npcName);
                }

                if (g.param == null)
                    g.param = String.Empty;

                if (gossip.body.Contains("[%")) {
                    var vars = Utility.GetVarStrings(gossip.body);
                    if (vars.Count() > 1) {
                        Debug.Print("More than one var in '{0}'", gossip.body);
                    }
                    g.param = String.Format("{0}", vars.First());
                }
                AddGossipToOutput(outputFile, g);
            }

            outputFile.NpcList = outputFile.NpcList.OrderBy(n => n.npcid).ToList();

            try {
                using (var fs = new FileStream(Path.Combine(outputPath, "npc_shouts.xml"),
                                               FileMode.Create, FileAccess.Write))
                using (var writer = XmlWriter.Create(fs, settings)) {
                    XmlSerializer ser = new XmlSerializer(typeof(NpcShouts));
                    ser.Serialize(writer, outputFile);
                }
            } catch (Exception ex) {
                Debug.Print(ex.ToString());
            }
        }

        static void AddGossipToOutput(NpcShouts output, Gossip gossip) {
            if (gossip.NpcList.Count == 0) {
                var matchedEntry = output.NpcList.Where(g => g.npcid == 0).FirstOrDefault();
                if (matchedEntry == null)
                    matchedEntry = new NpcShoutData();
                matchedEntry.ShoutList.Add(new Shout()
                {
                    messageid = gossip.stringId,
                    @event = gossip.Event,
                    param = gossip.param
                });
                output.NpcList.Remove(matchedEntry);
                output.NpcList.Add(matchedEntry);
                return;
            }
            foreach (var npc in gossip.NpcList) {
                var matchedEntry = output.NpcList.Where(g => g.npcid == npc).FirstOrDefault();
                if (matchedEntry == null) {
                    matchedEntry = new NpcShoutData();
                    matchedEntry.npcid = npc;
                }
                matchedEntry.ShoutList.Add(new Shout()
                {
                    messageid = gossip.stringId,
                    @event = gossip.Event,
                    param = gossip.param
                });
                output.NpcList.Remove(matchedEntry);
                output.NpcList.Add(matchedEntry);
            }
        }
    }
}
