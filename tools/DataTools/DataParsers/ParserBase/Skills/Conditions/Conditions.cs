namespace Jamie.Skills
{
	using System;
	using System.Collections.Generic;
	using System.Xml.Schema;
	using System.Xml.Serialization;
	using System.ComponentModel;

	[XmlInclude(typeof(ArrowCheckCondition))]
	[XmlInclude(typeof(TargetCondition))]
	[XmlInclude(typeof(PlayerMovedCondition))]
	[XmlInclude(typeof(DpCondition))]
	[XmlInclude(typeof(HpCondition))]
	[XmlInclude(typeof(MpCondition))]
	[Serializable]
	public abstract partial class Condition
	{
	}

	[Serializable]
	public partial class ArrowCheckCondition : Condition
	{
	}

	[Serializable]
	public partial class TargetCondition : Condition
	{
		public TargetCondition() { }

		public TargetCondition(FlyRestriction value) {
			this.restriction = value;
		}

		[XmlAttribute]
		[DefaultValue(FlyRestriction.NONE)]
		public FlyRestriction restriction;
	}

	[Serializable]
	public partial class SelfCondition : Condition
	{
		public SelfCondition() { }

		public SelfCondition(FlyRestriction value) {
			this.restriction = value;
		}

		[XmlAttribute]
		[DefaultValue(FlyRestriction.NONE)]
		public FlyRestriction restriction;
	}

	[Serializable]
	public enum TargetAttribute
	{
		NONE = 0,
		SELF = 1,
		NPC = 2,
		PC = 3,
		ALL = 4,
	}

	[Serializable]
	public partial class PlayerMovedCondition : Condition
	{
		public PlayerMovedCondition() { }

		public PlayerMovedCondition(bool allow) {
			this.allow = allow;
		}

		[XmlAttribute]
		public bool allow;
	}

	[Serializable]
	public partial class DpCondition : Condition
	{
		public DpCondition() { }

		public DpCondition(int value) {
			this.value = value;
		}

		[XmlAttribute]
		public int value;
	}

	[Serializable]
	public partial class HpCondition : Condition
	{
		public HpCondition() { }

		public HpCondition(int value, int delta) {
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
	public partial class MpCondition : Condition
	{
		public MpCondition() { }

		public MpCondition(int value, int delta) {
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
	public partial class Conditions
	{
		[XmlElement("arrowcheck", Form = XmlSchemaForm.Unqualified, Type = typeof(ArrowCheckCondition))]
		[XmlElement("target", Form = XmlSchemaForm.Unqualified, Type = typeof(TargetCondition))]
		[XmlElement("mp", Form = XmlSchemaForm.Unqualified, Type = typeof(MpCondition))]
		[XmlElement("hp", Form = XmlSchemaForm.Unqualified, Type = typeof(HpCondition))]
		[XmlElement("dp", Form = XmlSchemaForm.Unqualified, Type = typeof(DpCondition))]
		[XmlElement("playermove", Form = XmlSchemaForm.Unqualified, Type = typeof(PlayerMovedCondition))]
		[XmlElement("self", Form = XmlSchemaForm.Unqualified, Type = typeof(SelfCondition))]
		public List<Condition> ConditionList;
	}
}
