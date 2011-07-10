using System;
using System.IO;
using System.Linq;
using System.Text;
using System.Xml;
using System.Xml.Serialization;
using Jamie.ParserBase;
using System.Diagnostics;

namespace Jamie.Drops
{
    static class Program
    {
        static readonly string root = AppDomain.CurrentDomain.SetupInformation.ApplicationBase;

        static void Main(string[] args) {
            Utility.WriteExeDetails();

            Console.WriteLine("Loading NPCs...");
            Utility.LoadClientNpcs(root);
            Console.WriteLine("Loading droplist...");
            Utility.LoadDroplist(root);

            var saveSettings = new XmlWriterSettings()
            {
                CheckCharacters = false,
                CloseOutput = false,
                Encoding = new UTF8Encoding(false),
                Indent = true,
                IndentChars = "\t",
                NewLineChars = "\n",
            };

            string outputPath = Path.Combine(root, @"output");
            if (!Directory.Exists(outputPath))
                Directory.CreateDirectory(outputPath);

            // Sort and additional info
            foreach (var drop in Utility.DropListTemplate.Drops) {
                Npc npc = Utility.ClientNpcIndex[drop.npcid];
                if (npc == null) {
                    drop.DropItems.Clear();
                    continue;
                }
                drop.ai = GetAiName(npc.ai_name);
                drop.DropItems = drop.DropItems.OrderBy(d => d.id).ToList();
                decimal total = drop.DropItems.Where(i => i.chance != 100M).Sum(i => i.chance);
                if (total < 100)
                    drop.nodrop = 100 - total;
            }

            Utility.DropListTemplate.Drops = Utility.DropListTemplate.Drops.OrderBy(d => d.npcid).ToList();

            // Remove empty drops
            var emptyDrops = Utility.DropListTemplate.Drops.Where(d => d.DropItems.Count == 0).ToList();
            Utility.DropListTemplate.Drops.RemoveAll(d => emptyDrops.Contains(d));

            using (FileStream stream = new FileStream(Path.Combine(outputPath, "droplist.xml"),
                                                      FileMode.Create, FileAccess.Write)) {
                using (XmlWriter wr = XmlWriter.Create(stream, saveSettings)) {
                    XmlSerializer ser = new XmlSerializer(typeof(Droplist));
                    ser.Serialize(wr, Utility.DropListTemplate);
                }
            }
        }

        public static DropAi GetAiName(string clientName) {
            if (clientName.StartsWith("AI_IDREWARD_", StringComparison.InvariantCultureIgnoreCase) ||
                clientName.StartsWith("FOBJ_")) {
                string[] parts = clientName.Split('_');
                if (String.Compare(parts[1], "NormalDrop", true) == 0)
                    return DropAi.NORMAL;
                else if (String.Compare(parts[2], "FobjDropDice", true) == 0) {
                    if (parts.Length > 3) {
                        if (String.Compare(parts[3], "Up3", true) == 0)
                            return DropAi.DICE_UP3;
                        else if (String.Compare(parts[3], "Up3Big", true) == 0)
                            return DropAi.DICE_UP3_BIG;
                    } else {
                        return DropAi.DICE;
                    }
                } else if (parts.Length > 2 && String.Compare(parts[2], "FobjDrop", true) == 0) {
                    if (parts.Length > 3 && String.Compare(parts[3], "DespawnSound", true) == 0)
                        return DropAi.DESPAWN;
                    else
                        return DropAi.DROP;
                } else {
                    Debug.Print("Unknown drop AI: {0}", clientName);
                }
            }
            return DropAi.NONE;
        }
    }
}
