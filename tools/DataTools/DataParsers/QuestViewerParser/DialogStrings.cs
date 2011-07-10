using System.Xml.Serialization;
using System;
using System.Xml.Schema;
using System.Collections.Generic;
using System.Linq;

namespace AionQuests
{
    [Serializable]
    [XmlType(AnonymousType = true)]
    [XmlRoot(ElementName = "strings", Namespace = "", IsNullable = false)]
    public partial class DialogStrings
    {
        [XmlElement("string", Form = XmlSchemaForm.Unqualified)]
        public List<StringDefinition> StringList;


        Dictionary<string, int> nameToId = null;

        public void CreateIndex() {
            if (this.StringList == null)
                return;
            nameToId = this.StringList.ToDictionary(p => p.name, p => p.id);
        }

        public int this[string stringName] {
            get {
                if (nameToId == null || !nameToId.ContainsKey(stringName))
                    return -1;
                return nameToId[stringName];
            }
        }

        public StringDefinition GetStringDescription(int id) {
            if (StringList == null)
                return null;
            var matches = StringList.Where(s => s.id == id);
            if (!matches.Any())
                return null;
            return matches.First();
        }
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class StringDefinition
    {
        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int id;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string name;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string body;
    }
}


