namespace Jamie.Skills
{
	using System;
	using System.Collections.Generic;
	using System.ComponentModel;
	using System.Xml.Schema;
	using System.Xml.Serialization;

	[Serializable]
	[XmlType(AnonymousType = true)]
	[XmlRoot(ElementName = "pet_skill_templates", Namespace = "", IsNullable = false)]
	public sealed class PetSkillTemplates
	{
		[XmlElement("pet_skill", Form = XmlSchemaForm.Unqualified)]
		public List<PetSkill> SkillList;
	}

	[Serializable]
	public sealed class PetSkill
	{
		[XmlAttribute]
		public int skill_id;

		[XmlAttribute]
		[DefaultValue(0)]
		public int pet_id;

		[XmlAttribute]
		public string missing_pet_id;

		[XmlAttribute]
		[DefaultValue(0)]
		public int order_skill;

		[XmlAttribute]
		public string missing_order_skill;
	}
}
