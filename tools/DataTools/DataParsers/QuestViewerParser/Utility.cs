using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Diagnostics;
using System.IO;
using System.Xml;
using System.Xml.Serialization;
using JustAgile.Html.Linq;

namespace AionQuests
{
    class Utility
    {
        public static Strings StringIndex = null;
        static DialogStrings DialogStringIndex = null;
        public static ItemsFile ItemIndex { get; set; }
        static NpcFile npcData = null;

        public static bool TryLoadQuestHtml(string filePath, out QuestFile questFile) {
            string html = String.Empty;
            using (var stream = new StreamReader(filePath, Encoding.UTF8)) {
                var doc = HDocument.Load(stream);
                html = doc.ToString();
            }
            
            questFile = null;

            using (var ms = new MemoryStream(Encoding.UTF8.GetBytes(html.ToString().ToCharArray()))) {
                try {
                    XmlSerializer ser = new XmlSerializer(typeof(QuestFile));
                    questFile = (QuestFile)ser.Deserialize(ms);
                    questFile.fileName = Path.GetFileName(filePath);
                } catch (Exception ex) {
                    Debug.Print(ex.ToString());
                }
            }
            return questFile != null;
        }

        public static void LoadStrings(string rootPath) {
            try {
                using (var fs = new FileStream(Path.Combine(rootPath, @".\strings\client_strings.xml"),
                                               FileMode.Open, FileAccess.Read))
                using (var reader = XmlReader.Create(fs)) {
                    XmlSerializer ser = new XmlSerializer(typeof(Strings));
                    StringIndex = (Strings)ser.Deserialize(reader);
                    StringIndex.CreateIndex();
                }
            } catch (Exception ex) {
                Debug.Print(ex.ToString());
            }

            try {
                using (var fs = new FileStream(Path.Combine(rootPath, @".\strings\stringtable_dialog.xml"),
                                               FileMode.Open, FileAccess.Read))
                using (var reader = XmlReader.Create(fs)) {
                    XmlSerializer ser = new XmlSerializer(typeof(DialogStrings));
                    DialogStringIndex = (DialogStrings)ser.Deserialize(reader);
                    DialogStringIndex.CreateIndex();
                }
            } catch (Exception ex) {
                Debug.Print(ex.ToString());
            }
        }

        public static void LoadNpcs(string rootPath) {
            try {
                using (var fs = new FileStream(Path.Combine(rootPath, @".\npcs\client_npcs.xml"),
                                               FileMode.Open, FileAccess.Read))
                using (var reader = XmlReader.Create(fs)) {
                    XmlSerializer ser = new XmlSerializer(typeof(NpcFile));
                    npcData = (NpcFile)ser.Deserialize(reader);
                }
            } catch (Exception ex) {
                Debug.Print(ex.ToString());
            }
        }

        public static IEnumerable<string> GetNpcIdsFromDescription(string original) {
            IEnumerable<string> vars = GetVarStrings(original);
            List<string> ids = new List<string>();
            foreach (string var in vars) {
                if (!var.StartsWith("dic:"))
                    continue;
                string name = GetParsedString(String.Format("[%{0}]", var), true);
                if (!String.IsNullOrEmpty(name)) {
                    var descr = StringIndex.StringList.Where(s => s.body == name).FirstOrDefault();
                    if (descr != null) {
                        var possibleNpcs = npcData.NpcList.Where(n => n.desc == descr.name);
                        foreach (var npc in possibleNpcs) {
                            ids.Add(npc.id.ToString());
                        }
                    }
                }
            }
            return ids;
        }

        static int recurse = 0;

        public static IEnumerable<string> GetVarStrings(string original) {
            IList<string> vars = new List<string>();
            GetParsedString(original, ref vars, true);
            return vars;
        }

        public static string GetParsedString(string original, bool shortDescr) {
            IList<string> vars = new List<string>();
            return GetParsedString(original, ref vars, shortDescr);
        }

        static string GetParsedString(string original, ref IList<string> vars, bool shortDescr) {
            if (String.IsNullOrEmpty(original))
                return String.Empty;

            if (recurse > 15) {
                Debug.Print("Recursion too deep. Aborting parse");
                return original;
            }

            recurse++;

            StringBuilder sb = new StringBuilder();
            string varName = String.Empty;
            bool varStart = false;

            for (int i = 0; i < original.Length; i++) {
                if (shortDescr && original[i] == ';') {
                    recurse--;
                    return sb.ToString();
                }
                if (!varStart && original[i] == '[' && 
                    i < original.Length - 2 && original[i + 1] == '%') {
                    varStart = true;
                    i++;
                } else if (varStart && original[i] == ']') {
                    vars.Add(varName);
                    if (varName.StartsWith("dic:")) {
                        varName = varName.Remove(0, 4);
                        int id = StringIndex[varName];
                        if (id != -1) {
                            var descr = StringIndex.GetStringDescription(id);
                            if (vars.Contains(varName)) {
                                varName = String.Empty;
                                sb.Append(GetParsedString(descr.body, ref vars, true));
                                i++; // skip
                            } else {
                                sb.Append(GetParsedString(descr.body, ref vars, shortDescr));
                            }
                        } else {
                            Debug.Print("String not found: {0}", varName);
                        }
                        if (i < original.Length - 2 && original[i + 1] == '%') {
                            i++; // skip percent after the var
                        }
                    } else {
                        sb.Append(String.Format("[%{0}]", varName));
                    }
                    varName = String.Empty;
                    varStart = false;
                } else if (varStart) {
                    varName += original[i];
                } else {
                    sb.Append(original[i]);
                }
            }

            recurse--;
            return sb.ToString();
        }

        internal static void LoadItems(string root) {
            try {
                using (var fs = new FileStream(Path.Combine(root, @".\items\client_items.xml"),
                                               FileMode.Open, FileAccess.Read))
                using (var reader = XmlReader.Create(fs)) {
                    XmlSerializer ser = new XmlSerializer(typeof(ItemsFile));
                    ItemIndex = (ItemsFile)ser.Deserialize(reader);
                    ItemIndex.CreateIndex();
                }
            } catch (Exception ex) {
                Debug.Print(ex.ToString());
            }
        }
    }
}
