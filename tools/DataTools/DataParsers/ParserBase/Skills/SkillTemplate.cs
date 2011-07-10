namespace Jamie.Skills
{
	using System;
	using System.Collections.Generic;
	using System.Xml.Serialization;
	using System.ComponentModel;
	using System.Xml.Schema;
	using System.Reflection;
	using System.Diagnostics;
	using System.Linq;

	[Serializable]
	[XmlType(AnonymousType = true)]
	[XmlRoot("skill_data", Namespace = "", IsNullable = false)]
	public partial class SkillData
	{
		[XmlElement("skill_template", Form = XmlSchemaForm.Unqualified)]
		public List<SkillTemplate> SkillList = new List<SkillTemplate>();
	}

	[Serializable]
	[XmlType(AnonymousType = true)]
	public partial class SkillTemplate
	{
		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public Properties initproperties;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public Conditions startconditions;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public Properties setproperties;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public Conditions useconditions;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public Effects effects;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public Actions actions;

		[XmlAttribute]
		public int skill_id;

		[XmlAttribute]
		public string name;

		[XmlAttribute]
		[DefaultValue(0)]
		public int nameId;

		[XmlAttribute]
		public string stack;

		[XmlAttribute]
		public int lvl;

		[XmlAttribute]
		public skillType skilltype;

		[XmlAttribute]
		public skillSubType skillsubtype;

		[XmlAttribute]
		public TargetSlot tslot;

        [XmlAttribute]
        [DefaultValue(0)]
        public int tslot_level;

		[XmlAttribute]
		public activationAttribute activation;

		[XmlAttribute]
		public int cooldown;

		[XmlAttribute]
		public int duration;

		[XmlAttribute]
		[DefaultValue(0)]
		public int penalty_skill_id;

		[XmlAttribute]
		[DefaultValue(DispelCategory.NONE)]
		public DispelCategory dispel_category;

		[XmlAttribute]
		[DefaultValue(0)]
		public int dispel_level;

		[XmlAttribute]
		[DefaultValue(0)]
		public int pvp_damage;

		[XmlAttribute]
		[DefaultValue(0)]
		public int pvp_duration;

		[XmlAttribute]
		[DefaultValue(0)]
		public int cancel_rate;

		[XmlAttribute]
		[DefaultValue(0)]
		public int chain_skill_prob;

		[XmlAttribute]
		[DefaultValue(false)]
		public bool stance;

        [XmlAttribute]
        [DefaultValue(0)]
        public int delay_id;

		[XmlAttribute]
		[DefaultValue(0)]
		public int skillset_exception;

		public SkillTemplate() {
			this.cooldown = 0;
			this.chain_skill_prob = 0;
			this.cancel_rate = 0;
			this.stance = false;
			this.skillset_exception = 0;
		}

		public SkillTemplate(int skillId, int skillLevel) {
			this.skill_id = skillId;
			this.lvl = skillLevel;
		}
	}

	[Serializable]
	public enum skillSubType
	{
		NONE = 0,
		ATTACK = 1,
		CHANT = 2,
		HEAL = 3,
		BUFF = 4,
		DEBUFF = 5,
		SUMMON = 6,
		SUMMONHOMING = 7,
		SUMMONTRAP = 8,
	}

	[Serializable]
	public enum FlyRestriction
	{
		NONE = 0,
		ALL = 1,
		FLY = 2,
		GROUND = 3
	}

	[Serializable]
	public enum TargetSlot
	{
		NONE = 0,
		BUFF = 1,
		DEBUFF = 2,
		SPEC = 3,
		SPEC2 = 4,
		BOOST = 5,
		NOSHOW = 6,
		CHANT = 7,
	}

	[Serializable]
	public enum activationAttribute
	{
		NONE = 0,
		ACTIVE = 1,
		PROVOKED = 2,
		MAINTAIN = 3,
		TOGGLE = 4,
		PASSIVE = 5,
	}

	[Serializable]
	public enum DispelCategory
	{
		NONE = 0,
		ALL = 1,
		BUFF = 2,
		DEBUFF_MENTAL = 3,
		DEBUFF_PHYSICAL = 4,
		EXTRA = 5,
		NPC_BUFF = 6,
		NPC_DEBUFF_PHYSICAL = 7,
		STUN = 8,
	}
}
