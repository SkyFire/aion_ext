namespace Jamie.Skills
{
	using System;
	using System.Collections.Generic;
	using System.ComponentModel;
	using System.IO;
	using System.Linq;
	using System.Text;
	using System.Xml;
	using System.Xml.Schema;
	using System.Xml.Serialization;

	[XmlRoot(ElementName = "skillMap")]
	[XmlType(Namespace = "")]
	public class SkillMap
	{
		[XmlElement("skill")]
		public List<SkillMapEntry> skills;

		[XmlIgnore]
		public Dictionary<string, SkillMapEntry> AllMappings =
			new Dictionary<string, SkillMapEntry>(StringComparer.InvariantCultureIgnoreCase);

		public SkillMap() {
			AllMappings.Add("hit_accuracy", new SkillMapEntry()
			{
				from = "hit_accuracy",
				to = "PHYSICAL_ACCURACY",
				modifier = "AddModifier",
				ModifierType = typeof(AddModifier)
			});
			AllMappings.Add("magical_hit_accuracy", new SkillMapEntry()
			{
				from = "magical_hit_accuracy",
				to = "MAGICAL_ACCURACY",
				modifier = "AddModifier",
				ModifierType = typeof(AddModifier)
			});
			AllMappings.Add("parry", new SkillMapEntry()
			{
				from = "parry",
				to = "PARRY",
				modifier = "AddModifier",
				ModifierType = typeof(AddModifier)
			});
			AllMappings.Add("dodge", new SkillMapEntry()
			{
				from = "dodge",
				to = "EVASION",
				modifier = "AddModifier",
				ModifierType = typeof(AddModifier)
			});
			AllMappings.Add("critical", new SkillMapEntry()
			{
				from = "critical",
				to = "PHYSICAL_CRITICAL",
				modifier = "AddModifier",
				ModifierType = typeof(AddModifier)
			});
			AllMappings.Add("min_damage", new SkillMapEntry()
			{
				from = "min_damage",
				to = "MIN_DAMAGES",
				modifier = "AddModifier",
				ModifierType = typeof(AddModifier)
			});
			AllMappings.Add("max_damage", new SkillMapEntry()
			{
				from = "max_damage",
				to = "MAX_DAMAGES",
				modifier = "AddModifier",
				ModifierType = typeof(AddModifier)
			});
			AllMappings.Add("attack_delay", new SkillMapEntry()
			{
				from = "attack_delay",
				to = "ATTACK_SPEED",
				modifier = "SetModifier",
				ModifierType = typeof(SetModifier)
			});
			AllMappings.Add("attack_range", new SkillMapEntry()
			{
				from = "attack_range",
				to = "ATTACK_RANGE",
				modifier = "SetModifier",
				ModifierType = typeof(SetModifier)
			});
			AllMappings.Add("hit_count", new SkillMapEntry()
			{
				from = "hit_count",
				to = "HIT_COUNT",
				modifier = "SetModifier",
				ModifierType = typeof(SetModifier)
			});
			AllMappings.Add("physical_defend", new SkillMapEntry()
			{
				from = "hit_count",
				to = "PHYSICAL_DEFENSE",
				modifier = "AddModifier",
				ModifierType = typeof(AddModifier)
			});
			AllMappings.Add("magical_resist", new SkillMapEntry()
			{
				from = "magical_resist",
				to = "MAGICAL_RESIST",
				modifier = "AddModifier",
				ModifierType = typeof(AddModifier)
			});
		}

		public void SaveToFile(string root) {
			skills = AllMappings.Select(pair => pair.Value).ToList();
			var saveSettings = new XmlWriterSettings()
			{
				CheckCharacters = false,
				CloseOutput = false,
				Encoding = new UTF8Encoding(false),
				Indent = true,
				IndentChars = "\t",
				NewLineChars = "\n",
			};
			using (FileStream stream = new FileStream(Path.Combine(root, @".\data\modifier_map.xml"),
													  FileMode.Create, FileAccess.Write)) {
				using (XmlWriter wr = XmlWriter.Create(stream, saveSettings)) {
					XmlSerializer ser = new XmlSerializer(typeof(SkillMap));
					ser.Serialize(wr, this);
				}
			}
		}

		public void LoadFromFile(string root) {
			skills = AllMappings.Select(pair => pair.Value).ToList();
			using (FileStream stream = new FileStream(Path.Combine(root, @".\data\modifier_map.xml"),
													  FileMode.Open, FileAccess.Read)) {
				using (XmlReader xr = XmlReader.Create(stream)) {
					XmlSerializer ser = new XmlSerializer(typeof(SkillMap));
					SkillMap map = (SkillMap)ser.Deserialize(xr);
					this.skills = map.skills;
					foreach (var skill in this.skills) {
						if (this.AllMappings.ContainsKey(skill.from))
							continue;
						this.AllMappings.Add(skill.from, skill);
					}
				}
			}
		}
	}

	[XmlType(AnonymousType = true)]
	public class SkillMapEntry
	{
		[XmlAttribute(Form = XmlSchemaForm.Unqualified)]
		public string from;

		[XmlAttribute(Form = XmlSchemaForm.Unqualified)]
		public string to;

		[XmlAttribute(AttributeName = "negative", Form = XmlSchemaForm.Unqualified)]
		[DefaultValue(false)]
		public bool IsNegative { get; set; }

		[XmlIgnore]
		public Type ModifierType { get; set; }

		[XmlAttribute(Form = XmlSchemaForm.Unqualified)]
		public string modifier {
			get { return ModifierType.Name; }
			set {
				ModifierType = Type.GetType(String.Format("{0}.{1}", this.GetType().Namespace, value));
			}
		}
	}
}
