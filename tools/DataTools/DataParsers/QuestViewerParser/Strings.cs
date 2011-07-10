using System.Xml.Serialization;
using System;
using System.Xml.Schema;
using System.Collections.Generic;
using System.Linq;
using System.Diagnostics;

namespace AionQuests
{
    [Serializable]
    [XmlType(AnonymousType = true)]
    [XmlRoot(ElementName = "strings", Namespace = "", IsNullable = false)]
    public partial class Strings
    {
        [XmlElement("string", Form = XmlSchemaForm.Unqualified)]
        public List<StringDescription> StringList;

        Dictionary<string, int> nameToId = null;
        Dictionary<string, StringDescription> nameToObj = null;
        Dictionary<int, StringDescription> idToObj = null;

        public void CreateIndex() {
            if (this.StringList == null)
                return;

            nameToId = new Dictionary<string, int>(StringComparer.InvariantCultureIgnoreCase);
            nameToObj = new Dictionary<string, StringDescription>(StringComparer.InvariantCultureIgnoreCase);
            idToObj = new Dictionary<int, StringDescription>();

            foreach (var descr in this.StringList) {
                if (!nameToId.ContainsKey(descr.name.ToUpper())) {
                    nameToId.Add(descr.name, descr.id);
                    nameToObj.Add(descr.name, descr);
                    idToObj.Add(descr.id, descr);
                } else {
                    Debug.Print("String with the name {0} already exists; id = {1}", descr.name, descr.id);
                }
            }
        }

        public int this[string stringId] {
            get {
                stringId = stringId.ToUpper();
                if (nameToId == null || String.IsNullOrEmpty(stringId) || !nameToId.ContainsKey(stringId))
                    return -1;
                return nameToId[stringId];
            }
        }

        public StringDescription GetStringDescription(int id) {
            if (StringList == null)
                return null;
            if (!idToObj.ContainsKey(id))
                return null;
            return idToObj[id];
        }

        public StringDescription GetStringDescription(string stringId) {
            if (StringList == null)
                return null;

            stringId = stringId.ToUpper();
            if (!nameToObj.ContainsKey(stringId))
                return null;
            return nameToObj[stringId];
        }

        public string GetString(string stringId) {
            if (stringId == null)
                return String.Empty;
            int idx = this[stringId.ToUpper()];
            if (idx == -1)
                return stringId;
            return this.GetStringDescription(idx).body;
        }
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    [DebuggerDisplay("Name: {body}, Id = {id}")]
    public partial class StringDescription
    {
        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int id;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string name;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string body;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int display_type;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string message_type;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string ment;
    }
}
