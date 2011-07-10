namespace Jamie.Items
{
	using System;
	using System.Collections.Generic;
	using System.Linq;
	using System.Text;
	using System.Xml.Serialization;
	using System.Xml.Schema;
	using System.ComponentModel;

	[Serializable]
	[XmlType(AnonymousType = true)]
	[XmlRoot(ElementName = "wrapped_items", Namespace = "", IsNullable = false)]
	public partial class WrappedItemsFile
	{
		[XmlElement("wrapper_item", Form = XmlSchemaForm.Unqualified)]
		public WrapperItem[] wrapper_item;
	}

	[Serializable]
	public partial class WrapperItem
	{
		[XmlElement("item", Form = XmlSchemaForm.Unqualified)]
		public WrappedItem[] item;

		[XmlElement("wrapper_item", Form = XmlSchemaForm.Unqualified)]
		public WrapperItem[] wrapper_item;

		[XmlAttribute]
		[DefaultValue(0)]
		public int id;

		[XmlAttribute]
		[DefaultValue(0)]
		public int count;

		[XmlAttribute]
		[DefaultValue("")]
		public string description;

		public WrapperItem() {
			this.count = 0;
			this.description = String.Empty;
		}
	}

	[Serializable]
	public partial class WrappedItem
	{
		[XmlAttribute]
		[DefaultValue(0)]
		public int id;

		[XmlAttribute]
		[DefaultValue(BonusType.NONE)]
		public BonusType type;

		[XmlAttribute]
		public int level;

		[XmlIgnore]
		public bool levelSpecified;

        [XmlAttribute]
        public int min;

		[XmlAttribute]
		public int max;

		[XmlAttribute]
		[DefaultValue("")]
		public string description;

		public WrappedItem() {
			this.type = BonusType.NONE;
			this.description = String.Empty;
		}
	}
}
