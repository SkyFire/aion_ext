namespace Jamie.ParserBase
{
	using System;
	using System.Collections.Generic;
	using System.Xml.Schema;
	using System.Xml.Serialization;

	[Serializable]
	[XmlType(AnonymousType = true)]
	[XmlRoot(ElementName = "client_ultra_skills", Namespace = "", IsNullable = false)]
	public sealed class ClientUltraSkillFile
	{
		[XmlElement("client_ultra_skill", Form = XmlSchemaForm.Unqualified)]
		public List<ClientUltraSkill> UltraSkillList;
	}

	[Serializable]
	[XmlType(AnonymousType = true)]
	public sealed class ClientUltraSkill
	{
		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public int id;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public string ultra_skill;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public string pet_name;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public string order_skill;
	}
}
