using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace AionQuests
{
    class StepProcessOption
    {
        public int StepMapTo { get; private set; }

        bool _usePrepage = true;

        public bool UsePrepage {
            get { return _usePrepage; }
        }

        public StepProcessOption(int stepMapTo, bool usePrepage) {
            this.StepMapTo = stepMapTo;
            _usePrepage = usePrepage;
        }

        public StepProcessOption(int stepMapTo)
            : this(stepMapTo, true) {
        }
    }

    class IniReader
    {
        protected Dictionary<int, Dictionary<int, StepProcessOption>>
            _questStepMaps;
        protected Dictionary<string, string> _translation;

        const int MAX_BUFFER = Int16.MaxValue / 2 - 1;

        static string _appName;
        static string _iniPath;

        static IniReader() {
            _appName = typeof(IniReader).Assembly.GetName().Name;
            _iniPath = Path.Combine(AppDomain.CurrentDomain.SetupInformation.ApplicationBase,
                                    _appName + ".ini");
        }

        public IniReader() {
            LoadQuestStepMap();
            LoadTranslation();
        }

        public void LoadQuestStepMap() {
            NativeMethods.WritePrivateProfileString(null, null, null, _appName);
            List<string> sections = GetSectionNames();
            _questStepMaps = new Dictionary<int, Dictionary<int, StepProcessOption>>(sections.Count);
            foreach (string section in sections) {
                int questId;
                if (!Int32.TryParse(section, out questId))
                    continue;

                if (_questStepMaps.ContainsKey(questId))
                    continue;
                var questMap = new Dictionary<int, StepProcessOption>();
                List<string> keys = GetKeyNames(section);
                foreach (string key in keys) {
                    string valueName = GetIniFileString(section, key, key);
                    if (String.IsNullOrEmpty(valueName))
                        continue;

                    int stepNo;
                    if (!Int32.TryParse(key, out stepNo))
                        continue;
                    if (questMap.ContainsKey(stepNo))
                        continue;

                    int stepMapTo;
                    bool usePrepage = true;
                    if (!Int32.TryParse(valueName, out stepMapTo)) {
                        if (!valueName.Contains(','))
                            continue;
                        string[] options = valueName.Split(',');
                        if (!Int32.TryParse(options[0], out stepMapTo))
                            continue;
                        if (!Boolean.TryParse(options[1], out usePrepage))
                            continue;
                    }
                    questMap.Add(stepNo, new StepProcessOption(stepMapTo, usePrepage));
                }
                _questStepMaps.Add(questId, questMap);
            }
        }

        void LoadTranslation() {
            List<string> keys = GetKeyNames("translation");
            _translation = new Dictionary<string, string>();

            foreach (string key in keys) {
                string valueName = GetIniFileString("translation", key, key);
                if (_translation.ContainsKey(valueName))
                    continue;
                _translation.Add(key, valueName);
            }
        }

        public StepProcessOption this[int questId, int stepNo] {
            get {
                if (!_questStepMaps.ContainsKey(questId))
                    return new StepProcessOption(stepNo);

                var dic = _questStepMaps[questId];
                if (!dic.ContainsKey(stepNo))
                    return new StepProcessOption(stepNo);
                return dic[stepNo];
            }
        }

        public string this[string key] {
            get {
                if (!_translation.ContainsKey(key))
                    return Properties.Resources.ResourceManager.GetString(key);
                return _translation[key];
            }
        }

        #region INI handling functions

        List<string> GetSectionNames() {
            string returnString = new string(' ', MAX_BUFFER);
            NativeMethods.GetPrivateProfileString(null, null, null, returnString, MAX_BUFFER, _iniPath);
            List<string> result = new List<string>(returnString.Split('\0'));
            result.RemoveRange(result.Count - 2, 2);
            return result;
        }

        List<string> GetKeyNames(string section) {
            string returnString = new string(' ', MAX_BUFFER);
            NativeMethods.GetPrivateProfileString(section, null, null, returnString, MAX_BUFFER, _iniPath);
            List<string> result = new List<string>(returnString.Split('\0'));
            result.RemoveRange(result.Count - 2, 2);
            return result;
        }

        string GetIniFileString(string category, string key, string defaultValue) {
            string returnString = new string(' ', 1024);
            NativeMethods.GetPrivateProfileString(category, key, defaultValue, returnString, 1024, _iniPath);
            return returnString.Split('\0')[0];
        }

        void WriteIniFileString(string category, string key, string value) {
            NativeMethods.WritePrivateProfileString(category, key, value, _iniPath);
        }

        #endregion
    }
}
