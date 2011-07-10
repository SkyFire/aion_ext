using System.Xml.Serialization;
using System;
using System.Xml.Schema;
using System.Collections.Generic;
using System.Linq;

namespace AionQuests
{
    [Serializable]
    [XmlType(AnonymousType = true)]
    [XmlRootAttribute(ElementName = "hyperlinks", Namespace = "", IsNullable = false)]
    public partial class HyperLinkIndex
    {
        [XmlElement("hyperlink", Form = XmlSchemaForm.Unqualified)]
        public List<HyperLink> HyperLinks;

        Dictionary<string, int> nameToId = null;

        public void CreateIndex() {
            if (this.HyperLinks == null)
                return;
            nameToId = this.HyperLinks.ToDictionary(p => p.name, p => p.id);
        }

        public int this[string linkName] {
            get {
                if (nameToId == null || !nameToId.ContainsKey(linkName))
                    return -1;
                return nameToId[linkName];
            }
        }
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class HyperLink
    {
        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int id;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string name;
    }
}
