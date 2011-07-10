namespace Jamie.Skills
{
	using System;
	using System.ComponentModel;
	using System.Xml.Schema;
	using System.Xml.Serialization;

	[XmlInclude(typeof(TargetRelationProperty))]
	[XmlInclude(typeof(TargetRangeProperty))]
	[XmlInclude(typeof(FirstTargetRangeProperty))]
	[XmlInclude(typeof(FirstTargetProperty))]
	[XmlInclude(typeof(AddWeaponRangeProperty))]
    [XmlInclude(typeof(TargetStatusProperty))]
	[Serializable]
	public abstract partial class Property
	{
	}

	[Serializable]
	public partial class TargetRelationProperty : Property
	{
		public TargetRelationProperty() { }

		public TargetRelationProperty(TargetRelationAttribute value) {
			this.value = value;
		}

		[XmlAttribute]
		public TargetRelationAttribute value;
	}

	[Serializable]
	public partial class TargetSpeciesProperty : Property
	{
		public TargetSpeciesProperty() { }

		public TargetSpeciesProperty(TargetAttribute value) {
			this.value = value;
		}

		[XmlAttribute]
		public TargetAttribute value;
	}

	[Serializable]
	public enum TargetRelationAttribute
	{
		NONE = 0,
		ENEMY = 1,
		MYPARTY = 2,
		ALL = 3,
		FRIEND = 4,
	}

	[Serializable]
	public partial class TargetRangeProperty : Property
	{
		public TargetRangeProperty() { }

		public TargetRangeProperty(int maxCount, int distance, TargetRangeAttribute value) {
			this.maxcount = maxCount;
			this.distance = distance;
			this.value = value;
		}

		[XmlAttribute]
		public TargetRangeAttribute value;

		[XmlAttribute]
		[DefaultValue(0)]
		public int distance;

		[XmlAttribute]
		[DefaultValue(0)]
		public int maxcount;
	}

	[Serializable]
	public enum TargetRangeAttribute
	{
		NONE = 0,
		ONLYONE = 1,
		PARTY = 2,
		AREA = 3,
		PARTY_WITHPET = 4,
		POINT = 5,
	}

	[Serializable]
	public partial class FirstTargetRangeProperty : Property
	{
		public FirstTargetRangeProperty() { }

		public FirstTargetRangeProperty(int value) {
			this.value = value;
		}

		[XmlAttribute]
        [DefaultValue(0)]
		public int value;
	}

	[Serializable]
	public partial class FirstTargetProperty : Property
	{
		public FirstTargetProperty() { }

		public FirstTargetProperty(FirstTargetAttribute value) {
			this.value = value;
		}

		[XmlAttribute]
		public FirstTargetAttribute value;
	}

	[Serializable]
	public enum FirstTargetAttribute
	{
		NONE = 0,
		TARGETORME = 1,
		ME = 2,
		MYPET = 3,
		TARGET = 4,
		PASSIVE = 5,
		TARGET_MYPARTY_NONVISIBLE = 6,
		POINT = 7,
	}

	[Serializable]
	public class AddWeaponRangeProperty : Property
	{
	}

    [Serializable]
    public class TargetStatusProperty : Property
    {
		public TargetStatusProperty() { }

        public TargetStatusProperty(TargetState value) {
			this.value = value;
		}

        [XmlAttribute]
        public TargetState value;
    }

	[Serializable]
	public class Properties
	{
		[XmlElement("addweaponrange", Form = XmlSchemaForm.Unqualified)]
		public AddWeaponRangeProperty addweaponrange;

		[XmlElement("firsttarget", Form = XmlSchemaForm.Unqualified)]
		public FirstTargetProperty firsttarget;

		[XmlElement("firsttargetrange", Form = XmlSchemaForm.Unqualified)]
		public FirstTargetRangeProperty firsttargetrange;

		[XmlElement("targetrange", Form = XmlSchemaForm.Unqualified)]
		public TargetRangeProperty targetrange;

		[XmlElement("targetspecies", Form = XmlSchemaForm.Unqualified)]
		public TargetSpeciesProperty targetspecies;

		[XmlElement("targetrelation", Form = XmlSchemaForm.Unqualified)]
		public TargetRelationProperty targetrelation;

        [XmlElement("targetstatus", Form = XmlSchemaForm.Unqualified)]
        public TargetStatusProperty targetstatus;
	}
}
