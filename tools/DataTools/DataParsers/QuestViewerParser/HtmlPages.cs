using System.Xml.Serialization;
using System;
using System.Xml.Schema;
using System.Collections.Generic;
using System.Linq;

namespace AionQuests
{
    [Serializable]
    [XmlType(AnonymousType = true)]
    [XmlRoot(ElementName = "htmlpages", Namespace = "", IsNullable = false)]
    public partial class HtmlPageIndex
    {
        [XmlElement("htmlpage", Form = XmlSchemaForm.Unqualified)]
        public List<HtmlPageDescription> PageList;

        Dictionary<string, int> pageNameToId = null;
        Dictionary<string, int> nameToId = null;

        public void CreateIndex() {
            if (this.PageList == null)
                return;
            var namedPages = this.PageList.Where(p => !String.IsNullOrEmpty(p.htmlpagename));
            pageNameToId = namedPages.ToDictionary(p => p.htmlpagename, p => p.id);
            nameToId = this.PageList.ToDictionary(p => p.name, p => p.id);
        }

        public int this[string pageName] {
            get {
                if (pageNameToId == null || !pageNameToId.ContainsKey(pageName))
                    return -1;
                return pageNameToId[pageName];
            }
        }
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class HtmlPageDescription
    {
        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int id;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string name;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string htmlpagename;
    }
}

