using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Diagnostics;
using System.IO;
using System.Xml;
using System.Xml.Serialization;
using System.Configuration;

namespace QuestScriptsUpdater
{
    using Scripts = QuestScripts;

    class Utility
    {
        public static Strings StringIndex = null;
        public static QuestsFile QData = null;

        public static void LoadStrings(string rootPath) {
            try {
                using (var fs = new FileStream(Path.Combine(rootPath, @".\data\client_strings.xml"),
                                               FileMode.Open, FileAccess.Read))
                using (var reader = XmlReader.Create(fs)) {
                    XmlSerializer ser = new XmlSerializer(typeof(Strings));
                    StringIndex = (Strings)ser.Deserialize(reader);
                    StringIndex.CreateIndex();
                }
            } catch (Exception ex) {
                Debug.Print(ex.ToString());
            }
        }

        public static void LoadQData(string rootPath) {
            try {
                var readSettings = new XmlReaderSettings()
                {
                    ProhibitDtd = false,
                    CheckCharacters = false,
                    CloseInput = false
                };
                using (var fs = new FileStream(Path.Combine(rootPath, @".\data\quest.xml"),
                                               FileMode.Open, FileAccess.Read))
                using (var reader = XmlReader.Create(fs, readSettings)) {
                    XmlSerializer ser = new XmlSerializer(typeof(QuestsFile));
                    QData = (QuestsFile)ser.Deserialize(reader);
                }
            } catch (Exception ex) {
                Debug.Print(ex.ToString());
            }
        }

        public static QuestScripts GetScripts(string filePath) {
            try {
                using (var fs = new FileStream(filePath, FileMode.Open, FileAccess.Read))
                using (var reader = XmlReader.Create(fs)) {
                    XmlSerializer ser = new XmlSerializer(typeof(QuestScripts));
                    return (QuestScripts)ser.Deserialize(reader);
                }
            } catch (Exception ex) {
                Debug.Print(ex.ToString());
            }
            return null;
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

        internal static void UpdateScripts(Dictionary<string, Scripts> scriptsByLocation,
                                           Dictionary<string, List<int>> qIdsByLocation) {
            // MoveAscension(qIdsByLocation);

            string rootPath = AppDomain.CurrentDomain.SetupInformation.ApplicationBase;
            string scriptPath = Path.Combine(rootPath, ConfigurationManager.AppSettings["scriptPath"]);

            foreach (var q in QData.QList) {
                if (q.Description != null && q.Description.body == "Temporary")
                    continue;
                string location = null;
                if (q.category2 == null && q.category1 == "task")
                    location = "work_order";
                else
                    location = Utility.StringIndex.GetStringDescription(q.category2).body.ToLower();

                if (!scriptsByLocation.ContainsKey(location)) {
                    var scripts = new Scripts();
                    scripts.SetDefaultComments(scriptsByLocation.First().Value.GetDefaultComments());
                    scriptsByLocation.Add(location, scripts);
                }

                var scriptData = scriptsByLocation[location];

                if (qIdsByLocation.ContainsKey(location) && qIdsByLocation[location].Contains(q.id)) {
                    var matched = scriptData.scripts.Where(s => s.id == q.id);
                    if (matched.Count() == 0) {
                        var before = scriptData.scripts.Where(s => s.id > q.id).FirstOrDefault();
                        scriptData.InsertComment(before, String.Format("{0}: {1} (lvl {2}) handled by script", q.id,
                                                                       q.Description.body, q.minlevel_permitted));
                    } else {
                        foreach (var match in matched)
                            scriptData.SetComment(match, q.Description.body);
                    }
                } else {
                    var before = scriptData.scripts.Where(s => s.id > q.id).FirstOrDefault();
                    scriptData.InsertComment(before, String.Format("{0}: TODO: {1} (lvl {2})",
                        q.id, q.Description.body, q.minlevel_permitted));
                    if (!qIdsByLocation.ContainsKey(location)) {
                        qIdsByLocation.Add(location, new List<int>());
                    }
                    qIdsByLocation[location].Add(q.id);
                }
            }

            var settings = new XmlWriterSettings()
            {
                CloseOutput = false,
                Encoding = new UTF8Encoding(false),
                Indent = true,
                IndentChars = "\t",
                NewLineChars = "\n"
            };

            foreach (string key in qIdsByLocation.Keys) {
                if (key.StartsWith("test"))
                    continue;
                string fileName = key.Replace(' ', '_').Replace("'", String.Empty);
                string output = Path.Combine(rootPath, "output");

                if (!Directory.Exists(output))
                    Directory.CreateDirectory(output);

                string filePath = Path.Combine(output, fileName + ".xml");
                var scriptData = scriptsByLocation[key];

                using (var fs = new FileStream(filePath, FileMode.Create, FileAccess.Write))
                using (var xw = XmlWriter.Create(fs, settings)) {
                    var ser = new XmlSerializer(typeof(Scripts));
                    ser.Serialize(xw, scriptData);
                }
            }
        }
    }
}
