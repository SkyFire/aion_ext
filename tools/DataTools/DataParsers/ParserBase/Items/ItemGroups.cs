namespace Jamie.Items
{
	using System;
	using System.Collections.Generic;
	using System.Linq;
	using System.Text;
	using System.Xml.Serialization;

	[Serializable]
    public class ItemGroups
    {
        [XmlAttribute("id")]
        public string group;

        [XmlElement(ElementName = "suffix")]
        public List<ItemSuffixes> items = new List<ItemSuffixes>();

        public ItemGroups() {
            group = "none";
        }
    }

    [Serializable]
    public class ItemSuffixes
    {
        [XmlAttribute("id")]
        public int suffix;

        [XmlElement(ElementName = "item")]
        public List<ItemExport> items = new List<ItemExport>();
    }
}
