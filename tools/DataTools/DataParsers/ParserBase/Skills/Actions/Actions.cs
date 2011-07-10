namespace Jamie.Skills
{
	using System;
	using System.Collections.Generic;
	using System.ComponentModel;
	using System.Xml.Schema;
	using System.Xml.Serialization;

	[XmlInclude(typeof(DpUseAction))]
	[XmlInclude(typeof(HpUseAction))]
	[XmlInclude(typeof(MpUseAction))]
	[XmlInclude(typeof(ItemUseAction))]
	[Serializable]
	public abstract partial class Action
	{
	}

	[Serializable]
	public partial class DpUseAction : Action
	{
		public DpUseAction() { }

		public DpUseAction(int value, int delta) {
			this.value = value;
			this.delta = delta;
		}

		[XmlAttribute]
		public int value;

		[XmlAttribute]
		[DefaultValue(0)]
		public int delta;
	}

	[Serializable]
	public partial class HpUseAction : Action
	{
		public HpUseAction() { }

		public HpUseAction(int value, int delta) {
			this.value = value;
			this.delta = delta;
		}

		[XmlAttribute]
		public int value;

        [XmlAttribute]
        [DefaultValue(0)]
        public int delta;

        [XmlAttribute]
        [DefaultValue(false)]
        public bool percent;
    }

	[Serializable]
	public partial class MpUseAction : Action
	{
		public MpUseAction() { }

		public MpUseAction(int value, int delta) {
			this.value = value;
			this.delta = delta;
		}

		[XmlAttribute]
		public int value;

        [XmlAttribute]
        [DefaultValue(0)]
        public int delta;

        [XmlAttribute]
        [DefaultValue(false)]
        public bool percent;
    }

	[Serializable]
	public partial class ItemUseAction : Action
	{
		public ItemUseAction() { }

		public ItemUseAction(int itemid, int count) {
			this.itemid = itemid;
			this.count = count;
		}

		[XmlAttribute]
		public int itemid;

		[XmlAttribute]
		public int count;
	}

	[Serializable]
	public partial class Actions
	{
		[XmlElement("itemuse", Form = XmlSchemaForm.Unqualified, Type = typeof(ItemUseAction))]
		[XmlElement("mpuse", Form = XmlSchemaForm.Unqualified, Type = typeof(MpUseAction))]
		[XmlElement("hpuse", Form = XmlSchemaForm.Unqualified, Type = typeof(HpUseAction))]
		[XmlElement("dpuse", Form = XmlSchemaForm.Unqualified, Type = typeof(DpUseAction))]
		public List<Action> ActionList;
	}
}
