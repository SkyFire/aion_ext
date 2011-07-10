namespace Jamie.Skills
{
	using System;
	using System.Collections.Generic;
	using System.ComponentModel;
	using System.Xml.Schema;
	using System.Xml.Serialization;
	using Jamie.ParserBase;

	[XmlInclude(typeof(TargetRaceDamageModifier))]
	[XmlInclude(typeof(FlyingDamageModifier))]
	[XmlInclude(typeof(NonFlyingDamageModifier))]
	[XmlInclude(typeof(AbnormalDamageModifier))]
	[XmlInclude(typeof(FrontDamageModifier))]
	[XmlInclude(typeof(BackDamageModifier))]
	[Serializable]
	public abstract class ActionModifier
	{
	}

	[Serializable]
	public class TargetRaceDamageModifier : ActionModifier
	{
		[XmlAttribute]
		public int value;

		[XmlAttribute]
		public int delta;

		[XmlAttribute]
		public SkillTargetRace race;
	}

	[Serializable]
	public enum SkillTargetRace
	{
		PC_ALL = 0,
		ELYOS,
		ASMODIANS,
		LYCAN,
		CONSTRUCT,
		CARRIER,
		DRAKAN,
		LIZARDMAN,
		TELEPORTER,
		NAGA,
		BROWNIE,
		KRALL,
		SHULACK,
		PC_LIGHT_CASTLE_DOOR,
		PC_DARK_CASTLE_DOOR,
		DRAGON_CASTLE_DOOR,
		GCHIEF_LIGHT,
		GCHIEF_DARK,
		DRAGON,
		OUTSIDER,
		RATMAN,
		UNDEAD,
		BEAST,
		MAGICALMONSTER,
		ELEMENTAL,
        LIVINGWATER,
	}

	[Serializable]
	public partial class FrontDamageModifier : ActionModifier
	{
		[XmlAttribute]
		public int value;

		[XmlAttribute]
		[DefaultValue(0)]
		public int delta;
	}

	[Serializable]
	public partial class BackDamageModifier : ActionModifier
	{
		[XmlAttribute]
		public int value;

		[XmlAttribute]
		[DefaultValue(0)]
		public int delta;
	}

	[Serializable]
	public partial class FlyingDamageModifier : ActionModifier
	{
		[XmlAttribute]
		public int value;

		[XmlAttribute]
		[DefaultValue(0)]
		public int delta;
	}

	[Serializable]
	public partial class NonFlyingDamageModifier : ActionModifier
	{
		[XmlAttribute]
		public int value;

		[XmlAttribute]
		[DefaultValue(0)]
		public int delta;
	}

	[Serializable]
	public partial class AbnormalDamageModifier : ActionModifier
	{
		[XmlAttribute]
		public int value;

		[XmlAttribute]
		[DefaultValue(0)]
		public int delta;

		[XmlAttribute]
		public TargetState type;
	}

	[Serializable]
	public partial class ActionModifiers
	{
		[XmlElement("backdamage", Form = XmlSchemaForm.Unqualified)]
		public BackDamageModifier backdamage;

		[XmlElement("frontdamage", Form = XmlSchemaForm.Unqualified)]
		public FrontDamageModifier frontdamage;

		[XmlElement("flyingdamage", Form = XmlSchemaForm.Unqualified)]
		public FlyingDamageModifier flyingdamage;

		[XmlElement("nonflyingdamage", Form = XmlSchemaForm.Unqualified)]
		public NonFlyingDamageModifier nonflyingdamage;

		[XmlElement("abnormaldamage", Form = XmlSchemaForm.Unqualified)]
		public List<AbnormalDamageModifier> abnormaldamage;

		[XmlElement("targetrace", Form = XmlSchemaForm.Unqualified)]
		public List<TargetRaceDamageModifier> targetrace;

		public bool Present {
			get {
				return backdamage != null || frontdamage != null || flyingdamage != null ||
					   nonflyingdamage != null || abnormaldamage != null || targetrace != null;
			}
		}
	}

	[Serializable]
    [Flags]
	public enum TargetState
	{
        NONE = 0,
		[EntryValue("_back")]
		BACK = 1,
		[EntryValue("_flying")]
		FLYING = 2,
		[EntryValue("_front")]
		FRONT = 4,
		[EntryValue("_nflying")]
		NON_FLYING = 8,
		[EntryValue("bleed")]
		BLEED = 16,
		[EntryValue("blind")]
		BLIND = 32,
		[EntryValue("curse")]
		CURSE = 64,
		[EntryValue("deform")]
		DEFORM = 128,
		[EntryValue("fear")]
		FEAR = 256,
		[EntryValue("openaerial")]
		OPENAERIAL = 1024,
		[EntryValue("paralyze")]
		PARALYZE = 2048,
		[EntryValue("poison")]
		POISON = 4096,
		[EntryValue("snare")]
		SNARE = 8192,
		[EntryValue("stumble")]
        STUMBLE = 16384,
		[EntryValue("stun")]
        STUN = 32768,
        [EntryValue("bind")]
        BIND = 65536,
        [EntryValue("stagger")]
        STAGGER = 131072,
        [EntryValue("spin")]
        SPIN = 262144
	}
}
