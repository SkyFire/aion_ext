namespace Jamie.ParserBase
{
	using System;
	using System.Collections.Generic;
	using System.Diagnostics;
	using System.Xml.Schema;
	using System.Xml.Serialization;

	[Serializable]
    [XmlType(AnonymousType = true)]
    [XmlRoot(ElementName = "client_titles", Namespace = "", IsNullable = false)]
    public partial class ClientTitlesFile
    {
        [XmlElement("client_title", Form = XmlSchemaForm.Unqualified)]
        public List<Title> Titles;

        Dictionary<string, int> nameToId = null;

        internal void CreateIndex() {
            if (this.Titles == null)
                return;

            nameToId = new Dictionary<string, int>(StringComparer.InvariantCultureIgnoreCase);

            foreach (Title title in this.Titles) {
                if (!nameToId.ContainsKey(title.name)) {
                    nameToId.Add(title.name, title.id);
                } else {
                    Debug.Print("String with the name {0} already exists; id = {1}", title.name, title.id);
                }
            }
        }

        public int this[string stringId] {
            get {
                if (nameToId == null || String.IsNullOrEmpty(stringId) || !nameToId.ContainsKey(stringId))
                    return -1;
                return nameToId[stringId];
            }
        }
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class Title
    {
        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int id;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string desc;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string name;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string title_desc;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int title_race;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string title_location;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int title_priority;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int title_type;

        [XmlArrayAttribute(Form = XmlSchemaForm.Unqualified)]
        [XmlArrayItemAttribute("data", Form = XmlSchemaForm.Unqualified, IsNullable = false)]
        public TitleData[] bonus_attrs;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class TitleData
    {
        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string bonus_attr;
    }
}
