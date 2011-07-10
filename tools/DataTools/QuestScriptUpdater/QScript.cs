namespace QuestScriptsUpdater
{
	using System;
	using System.Collections.Generic;
	using System.Linq;
	using System.Text;
	using System.Xml.Serialization;
	using System.Xml.Schema;
	using System.ComponentModel;
	using System.Xml;
	using System.IO;
	using System.Configuration;
	using ScriptData = QuestScriptData;
	using QScripts = QuestScripts;
	using System.Xml.XPath;

	[Serializable]
	[XmlRoot("import", Namespace = "", IsNullable = false)]
	public partial class import
	{
		[XmlAttribute]
		public string file;

		[XmlAttribute]
		[DefaultValue(false)]
		public bool skipRoot;

		[XmlAttribute]
		[DefaultValue(true)]
		public bool recursiveImport;
		public import() {
			this.skipRoot = false;
			this.recursiveImport = true;
		}
	}

	[Serializable]
	public partial class MonsterInfo
	{
		[XmlAttribute]
		public int var_id;

		[XmlAttribute]
		[DefaultValue(0)]
		public int min_var_value;

		[XmlAttribute]
		public int max_kill;

		[XmlAttribute]
		public int npc_id;
		public MonsterInfo() {
			this.min_var_value = 0;
		}
	}

	[Serializable]
	public partial class QuestDialog
	{
		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public QuestConditions conditions;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public QuestOperations operations;

		[XmlAttribute]
		public int id;
	}

	[Serializable]
	public partial class QuestConditions
	{
		[XmlElement("dialog_id", Form = XmlSchemaForm.Unqualified)]
		public DialogIdCondition[] dialog_id;

		[XmlElement("npc_id", Form = XmlSchemaForm.Unqualified)]
		public NpcIdCondition[] npc_id;

		[XmlElement("pc_inventory", Form = XmlSchemaForm.Unqualified)]
		public PcInventoryCondition[] pc_inventory;

		[XmlElement("quest_status", Form = XmlSchemaForm.Unqualified)]
		public QuestStatusCondition[] quest_status;

		[XmlElement("quest_var", Form = XmlSchemaForm.Unqualified)]
		public QuestVarCondition[] quest_var;

		[XmlAttribute]
		public ConditionUnionType operate;
	}

	[Serializable]
	public partial class DialogIdCondition : QuestCondition
	{
		[XmlAttribute]
		public int value;
	}

	[XmlInclude(typeof(QuestVarCondition))]
	[XmlInclude(typeof(QuestStatusCondition))]
	[XmlInclude(typeof(PcInventoryCondition))]
	[XmlInclude(typeof(NpcIdCondition))]
	[XmlInclude(typeof(DialogIdCondition))]
	[Serializable]
	public abstract partial class QuestCondition
	{
		[XmlAttribute]
		public ConditionOperation op;
	}

	[Serializable]
	public enum ConditionOperation
	{
		EQUAL,
		GREATER,
		GREATER_EQUAL,
		LESSER,
		LESSER_EQUAL,
		NOT_EQUAL,
		IN,
		NOT_IN
	}

	[Serializable]
	public partial class QuestVarCondition : QuestCondition
	{
		[XmlAttribute]
		public int value;

		[XmlAttribute]
		public int var_id;
	}

	[Serializable]
	public partial class QuestStatusCondition : QuestCondition
	{
		[XmlAttribute]
		public QuestStatus value;

		[XmlAttribute]
		public int quest_id;

		[XmlIgnore]
		public bool quest_idSpecified;
	}

	[Serializable]
	public enum QuestStatus
	{
		NONE,
		START,
		REWARD,
		COMPLETE,
		LOCKED
	}

	[Serializable]
	public partial class PcInventoryCondition : QuestCondition
	{
		[XmlAttribute]
		public int item_id;

		[XmlAttribute]
		public long count;
	}

	[Serializable]
	public partial class NpcIdCondition : QuestCondition
	{
		[XmlAttribute]
		public int values;
	}

	[Serializable]
	public enum ConditionUnionType
	{
		AND,
		OR
	}

	[Serializable]
	public partial class QuestOperations
	{
		[XmlElement("give_item", Form = XmlSchemaForm.Unqualified)]
		public GiveItemOperation[] give_item;

		[XmlElement("set_quest_var", Form = XmlSchemaForm.Unqualified)]
		public SetQuestVarOperation[] set_quest_var;

		[XmlElement("npc_dialog", Form = XmlSchemaForm.Unqualified)]
		public NpcDialogOperation[] npc_dialog;

		[XmlElement("set_quest_status", Form = XmlSchemaForm.Unqualified)]
		public SetQuestStatusOperation[] set_quest_status;

		[XmlElement("start_quest", Form = XmlSchemaForm.Unqualified)]
		public StartQuestOperation[] start_quest;

		[XmlElement("take_item", Form = XmlSchemaForm.Unqualified)]
		public TakeItemOperation[] take_item;

		[XmlElement("collect_items", Form = XmlSchemaForm.Unqualified)]
		public CollectItemQuestOperation[] collect_items;

		[XmlElement("npc_use", Form = XmlSchemaForm.Unqualified)]
		public ActionItemUseOperation[] npc_use;

		[XmlElement("kill", Form = XmlSchemaForm.Unqualified)]
		public KillOperation[] kill;

		[XmlAttribute]
		[DefaultValue(true)]
		public bool @override;
		public QuestOperations() {
			this.@override = true;
		}
	}

	[Serializable]
	public partial class GiveItemOperation : QuestOperation
	{
		[XmlAttribute]
		public int item_id;

		[XmlAttribute]
		public int count;
	}

	[XmlInclude(typeof(TakeItemOperation))]
	[XmlInclude(typeof(ActionItemUseOperation))]
	[XmlInclude(typeof(CollectItemQuestOperation))]
	[XmlInclude(typeof(StartQuestOperation))]
	[XmlInclude(typeof(KillOperation))]
	[XmlInclude(typeof(SetQuestStatusOperation))]
	[XmlInclude(typeof(NpcDialogOperation))]
	[XmlInclude(typeof(SetQuestVarOperation))]
	[XmlInclude(typeof(GiveItemOperation))]
	[Serializable]
	public abstract partial class QuestOperation
	{
	}

	[Serializable]
	public partial class TakeItemOperation : QuestOperation
	{
		[XmlAttribute]
		public int item_id;

		[XmlAttribute]
		public int count;
	}

	[Serializable]
	public partial class ActionItemUseOperation : QuestOperation
	{
		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public QuestOperations finish;
	}

	[Serializable]
	public partial class CollectItemQuestOperation : QuestOperation
	{
		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public QuestOperations @true;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public QuestOperations @false;

		[XmlAttribute]
		[DefaultValue(true)]
		public bool removeItems;
		public CollectItemQuestOperation() {
			this.removeItems = true;
		}
	}

	[Serializable]
	public partial class StartQuestOperation : QuestOperation
	{
		[XmlAttribute]
		public int id;
	}

	[Serializable]
	public partial class KillOperation : QuestOperation
	{
	}

	[Serializable]
	public partial class SetQuestStatusOperation : QuestOperation
	{
		[XmlAttribute]
		public QuestStatus status;
	}

	[Serializable]
	public partial class NpcDialogOperation : QuestOperation
	{
		[XmlAttribute]
		public int id;

		[XmlAttribute]
		public int quest_id;

		[XmlIgnore]
		public bool quest_idSpecified;
	}

	[Serializable]
	public partial class SetQuestVarOperation : QuestOperation
	{
		[XmlAttribute]
		public int var_id;

		[XmlAttribute]
		public int value;
	}

	[Serializable]
	public partial class QuestNpc
	{
		[XmlElement("dialog", Form = XmlSchemaForm.Unqualified)]
		public QuestDialog[] dialog;

		[XmlAttribute]
		public int id;
	}

	[Serializable]
	public partial class QuestVar
	{
		[XmlElement("npc", Form = XmlSchemaForm.Unqualified)]
		public QuestNpc[] npc;

		[XmlAttribute]
		public int value;
	}

	[XmlInclude(typeof(OnKillEvent))]
	[XmlInclude(typeof(OnTalkEvent))]
	[Serializable]
	public abstract partial class QuestEvent
	{
		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public QuestConditions conditions;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public QuestOperations operations;

		[XmlAttribute]
		public int[] ids;
	}

	[Serializable]
	public partial class OnKillEvent : QuestEvent
	{
		[XmlElement("monster_infos", Form = XmlSchemaForm.Unqualified)]
		public MonsterInfo[] monster_infos;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public QuestOperations complite;
	}

	[Serializable]
	public partial class OnTalkEvent : QuestEvent
	{
		[XmlElement("var", Form = XmlSchemaForm.Unqualified)]
		public QuestVar[] var;
	}

	[XmlInclude(typeof(WorkOrdersData))]
	[XmlInclude(typeof(MonsterHuntData))]
	[XmlInclude(typeof(ReportToData))]
	[XmlInclude(typeof(ItemCollectingData))]
	[XmlInclude(typeof(XmlQuestData))]
	[Serializable]
	public abstract partial class QuestScriptData
	{
		[XmlAttribute]
		public int id;
	}

	[Serializable]
	[XmlRoot("work_order", Namespace = "", IsNullable = false)]
	public partial class WorkOrdersData : QuestScriptData
	{
		[XmlElement("give_component", Form = XmlSchemaForm.Unqualified)]
		public QuestItemsOur[] give_component;

		[XmlAttribute]
		public int start_npc_id;

		[XmlAttribute]
		public int recipe_id;
	}

	[Serializable]
	public partial class QuestItemsOur
	{
		[XmlAttribute]
		public int item_id;

		[XmlIgnore]
		public bool item_idSpecified;

		[XmlAttribute]
		public int count;

		[XmlIgnore]
		public bool countSpecified;
	}

	[Serializable]
	[XmlRoot("monster_hunt", Namespace = "", IsNullable = false)]
	public partial class MonsterHuntData : QuestScriptData
	{
		[XmlElement("monster_infos", Form = XmlSchemaForm.Unqualified)]
		public MonsterInfo[] monster_infos;

		[XmlAttribute]
		public int start_npc_id;

		[XmlAttribute]
		[DefaultValue(0)]
		public int end_npc_id;
		public MonsterHuntData() {
			this.end_npc_id = 0;
		}
	}

	[Serializable]
	[XmlRoot("report_to", Namespace = "", IsNullable = false)]
	public partial class ReportToData : QuestScriptData
	{
		[XmlAttribute]
		public int start_npc_id;

		[XmlAttribute]
		public int end_npc_id;

		[XmlAttribute]
		[DefaultValue(0)]
		public int item_id;

        [XmlAttribute]
        [DefaultValue(0)]
        public int add_end_npc_id;

		[XmlAttribute]
		[DefaultValue(0)]
		public int readable_item_id;
		public ReportToData() {
			this.item_id = 0;
			this.readable_item_id = 0;
		}
	}

	[Serializable]
	[XmlRoot("item_collecting", Namespace = "", IsNullable = false)]
	public partial class ItemCollectingData : QuestScriptData
	{
		[XmlAttribute]
		public int start_npc_id;

		[XmlAttribute]
		[DefaultValue(0)]
		public int end_npc_id;

        [XmlAttribute]
        [DefaultValue(0)]
        public int action_item_id;

		[XmlAttribute]
		[DefaultValue(0)]
		public int readable_item_id;
		public ItemCollectingData() {
			this.action_item_id = 0;
			this.end_npc_id = 0;
			this.readable_item_id = 0;
		}
	}

	[Serializable]
	[XmlRoot("xml_quest", Namespace = "", IsNullable = false)]
	public partial class XmlQuestData : QuestScriptData
	{
		[XmlElement("on_talk_event", Form = XmlSchemaForm.Unqualified)]
		public OnTalkEvent[] on_talk_event;

		[XmlElement("on_kill_event", Form = XmlSchemaForm.Unqualified)]
		public OnKillEvent[] on_kill_event;

		[XmlAttribute]
		public int start_npc_id;

		[XmlIgnore]
		public bool start_npc_idSpecified;

		[XmlAttribute]
		public int end_npc_id;

		[XmlIgnore]
		public bool end_npc_idSpecified;
	}

	[Serializable]
	public partial class QuestStartCondition
	{
		[XmlAttribute]
		public int quest;

		[XmlIgnore]
		public bool questSpecified;

		[XmlAttribute]
		public int step;

		[XmlIgnore]
		public bool stepSpecified;
	}

	[Serializable]
	public partial class QuestDrop
	{
		[XmlAttribute]
		public int npc_id;

		[XmlIgnore]
		public bool npc_idSpecified;

		[XmlAttribute]
		public int item_id;

		[XmlIgnore]
		public bool item_idSpecified;

		[XmlAttribute]
		public int chance;

		[XmlIgnore]
		public bool chanceSpecified;

		[XmlAttribute]
		[DefaultValue(false)]
		public bool drop_each_member;
	}

	[Serializable]
	public partial class Rewards
	{
		[XmlElement("selectable_reward_item", Form = XmlSchemaForm.Unqualified)]
		public QuestItems[] selectable_reward_item;

		[XmlElement("reward_item", Form = XmlSchemaForm.Unqualified)]
		public QuestItems[] reward_item;

		[XmlAttribute]
		public int gold;

		[XmlIgnore]
		public bool goldSpecified;

		[XmlAttribute]
		public int exp;

		[XmlIgnore]
		public bool expSpecified;

		[XmlAttribute]
		public int reward_abyss_point;

		[XmlIgnore]
		public bool reward_abyss_pointSpecified;

		[XmlAttribute]
		public int title;

		[XmlIgnore]
		public bool titleSpecified;

		[XmlAttribute]
		public int extend_inventory;

		[XmlIgnore]
		public bool extend_inventorySpecified;

		[XmlAttribute]
		public int extend_stigma;

		[XmlIgnore]
		public bool extend_stigmaSpecified;
	}

	[Serializable]
	public partial class CollectItem
	{
		[XmlAttribute]
		public int item_id;

		[XmlIgnore]
		public bool item_idSpecified;

		[XmlAttribute]
		public int count;

		[XmlIgnore]
		public bool countSpecified;
	}

	[Serializable]
	public partial class QuestOur
	{
		[XmlArray(Form = XmlSchemaForm.Unqualified)]
		[XmlArrayItem("collect_item", Form = XmlSchemaForm.Unqualified, IsNullable = false)]
		public CollectItem[] collect_items;

		[XmlElement("rewards", Form = XmlSchemaForm.Unqualified)]
		public Rewards[] rewards;

		[XmlElement("quest_drop", Form = XmlSchemaForm.Unqualified)]
		public QuestDrop[] quest_drop;

		[XmlArray(Form = XmlSchemaForm.Unqualified)]
		[XmlArrayItem("condition", Form = XmlSchemaForm.Unqualified, IsNullable = false)]
		public QuestStartCondition[] finished_quest_conds;

		[XmlArray(Form = XmlSchemaForm.Unqualified)]
		[XmlArrayItem("condition", Form = XmlSchemaForm.Unqualified, IsNullable = false)]
		public QuestStartCondition[] unfinished_quest_conds;

		[XmlArray(Form = XmlSchemaForm.Unqualified)]
		[XmlArrayItem("condition", Form = XmlSchemaForm.Unqualified, IsNullable = false)]
		public QuestStartCondition[] acquired_quest_conds;

		[XmlArray(Form = XmlSchemaForm.Unqualified)]
		[XmlArrayItem("condition", Form = XmlSchemaForm.Unqualified, IsNullable = false)]
		public QuestStartCondition[] noacquired_quest_conds;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public string class_permitted;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public Gender gender_permitted;

		[XmlIgnore]
		public bool gender_permittedSpecified;

		[XmlArray(Form = XmlSchemaForm.Unqualified)]
		[XmlArrayItem("quest_work_item", Form = XmlSchemaForm.Unqualified, IsNullable = false)]
		public QuestItems[] quest_work_items;

		[XmlElement("fighter_selectable_reward", Form = XmlSchemaForm.Unqualified)]
		public QuestItems[] fighter_selectable_reward;

		[XmlElement("knight_selectable_reward", Form = XmlSchemaForm.Unqualified)]
		public QuestItems[] knight_selectable_reward;

		[XmlElement("ranger_selectable_reward", Form = XmlSchemaForm.Unqualified)]
		public QuestItems[] ranger_selectable_reward;

		[XmlElement("assassin_selectable_reward", Form = XmlSchemaForm.Unqualified)]
		public QuestItems[] assassin_selectable_reward;

		[XmlElement("wizard_selectable_reward", Form = XmlSchemaForm.Unqualified)]
		public QuestItems[] wizard_selectable_reward;

		[XmlElement("elementalist_selectable_reward", Form = XmlSchemaForm.Unqualified)]
		public QuestItems[] elementalist_selectable_reward;

		[XmlElement("priest_selectable_reward", Form = XmlSchemaForm.Unqualified)]
		public QuestItems[] priest_selectable_reward;

		[XmlElement("chanter_selectable_reward", Form = XmlSchemaForm.Unqualified)]
		public QuestItems[] chanter_selectable_reward;

		[XmlAttribute]
		public int id;

		[XmlAttribute]
		public string name;

		[XmlAttribute]
		public int nameId;

		[XmlIgnore]
		public bool nameIdSpecified;

		[XmlAttribute]
		public int minlevel_permitted;

		[XmlIgnore]
		public bool minlevel_permittedSpecified;

		[XmlAttribute]
		public int max_repeat_count;

		[XmlIgnore]
		public bool max_repeat_countSpecified;

		[XmlAttribute]
		[DefaultValue(false)]
		public bool cannot_share;

		[XmlAttribute]
		[DefaultValue(false)]
		public bool cannot_giveup;

		[XmlAttribute]
		[DefaultValue(false)]
		public bool use_class_reward;

		[XmlAttribute]
		public Race race_permitted;

		[XmlIgnore]
		public bool race_permittedSpecified;

		[XmlAttribute]
		public int combineskill;

		[XmlIgnore]
		public bool combineskillSpecified;

		[XmlAttribute]
		public int combine_skillpoint;

		[XmlIgnore]
		public bool combine_skillpointSpecified;

		[XmlAttribute]
		[DefaultValue(false)]
		public bool timer;

		public QuestOur() {
			this.cannot_share = false;
			this.cannot_giveup = false;
			this.use_class_reward = false;
			this.timer = false;
		}
	}

	[Serializable]
	public enum Gender
	{
		MALE,
		FEMALE
	}

	[Serializable]
	public enum Race
	{
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
		BARRIER,
		PC_LIGHT_CASTLE_DOOR,
		PC_DARK_CASTLE_DOOR,
		DRAGON_CASTLE_DOOR,
		GCHIEF_LIGHT,
		GCHIEF_DARK,
		DRAGON,
		OUTSIDER,
		RATMAN,
		DEMIHUMANOID,
		UNDEAD,
		BEAST,
		MAGICALMONSTER,
		ELEMENTAL,
		NONE,
		PC_ALL,
		GOBLIN,
		GENERAL,
		NPC
	}

	[Serializable]
	[XmlRoot("quests", Namespace = "", IsNullable = false)]
	public partial class Quests
	{
		[XmlElement("import")]
		public List<import> imports;

		[XmlElement("quest", Form = XmlSchemaForm.Unqualified)]
		public List<QuestOur> quests;
	}

	[Serializable]
	[XmlRoot("quest_scripts", Namespace = "", IsNullable = false)]
	public partial class QuestScripts : IXmlSerializable
	{
		const string XSD_FILE = "quest_script_data.xsd";

		[XmlElement("import")]
		public List<import> imports;

		[XmlElement("item_collecting", Form = XmlSchemaForm.Unqualified, Type = typeof(ItemCollectingData))]
		[XmlElement("report_to", Form = XmlSchemaForm.Unqualified, Type = typeof(ReportToData))]
		[XmlElement("monster_hunt", Form = XmlSchemaForm.Unqualified, Type = typeof(MonsterHuntData))]
		[XmlElement("work_order", Form = XmlSchemaForm.Unqualified, Type = typeof(WorkOrdersData))]
		[XmlElement("xml_quest", Form = XmlSchemaForm.Unqualified, Type = typeof(XmlQuestData))]
		public List<QuestScriptData> scripts = new List<QuestScriptData>();

		Type GetTypeFromName(string elementName) {
			switch (elementName) {
				case "item_collecting":
					return typeof(ItemCollectingData);
				case "report_to":
					return typeof(ReportToData);
				case "monster_hunt":
					return typeof(MonsterHuntData);
				case "work_order":
					return typeof(WorkOrdersData);
				case "xml_quest":
					return typeof(XmlQuestData);
				case "import":
					return typeof(import);
			}
			return null;
		}

		#region IXmlSerializable Members

		XmlSchema schema = null;

		public XmlSchema GetSchema() {
			if (schema != null)
				return schema;
			string rootPath = AppDomain.CurrentDomain.SetupInformation.ApplicationBase;
			string scriptPath = Path.Combine(rootPath, ConfigurationManager.AppSettings["scriptPath"]);
			string filePath = Path.Combine(scriptPath, XSD_FILE);
			using (var fs = new FileStream(filePath, FileMode.Open, FileAccess.Read))
				schema = XmlSchema.Read(fs, null);
			return schema;
		}

		List<String> comments = new List<String>();
		Dictionary<ScriptData, int> scriptComments = new Dictionary<ScriptData, int>();

		public void SetDefaultComments(string value) {
			if (comments.Count > 1 && comments[0] == String.Empty) {
				comments[1] = value;
			} else {
				comments.Clear();
				comments.Add(String.Empty);
				comments.Add(value);
			}
		}

		public string GetDefaultComments() {
			if (comments.Count > 1 && comments[0] == String.Empty)
				return comments[1];
			else
				return String.Empty;
		}

		public void ReadXml(XmlReader reader) {
			var settings = new XmlReaderSettings()
			{
				ConformanceLevel = ConformanceLevel.Auto,
				ValidationFlags = XmlSchemaValidationFlags.None,
				ValidationType = ValidationType.None,
				CloseInput = false,
				ProhibitDtd = true
			};

			while (reader.Read()) {
				switch (reader.NodeType) {
					case XmlNodeType.Comment:
						if (comments.Count == 0) {
							// read only the first one
							comments.Add(String.Empty);
							comments.Add(reader.Value);
						}
						break;
					case XmlNodeType.Element:
						Type type = GetTypeFromName(reader.Name);
						XmlSerializer ser = new XmlSerializer(type);
						if (type.Equals(typeof(import))) {
							if (imports == null)
								imports = new List<import>();
							XmlReader fragReader = XmlReader.Create(reader, settings);
							imports.Add((import)ser.Deserialize(fragReader));
						} else {
							XmlReader fragReader = XmlReader.Create(reader, settings);
							var scriptData = (ScriptData)ser.Deserialize(fragReader);
							if (scripts == null)
								scripts = new List<ScriptData>();
							scripts.Add(scriptData);
							scriptComments.Add(scriptData, 0); // no comment
						}
						break;
				}
			}
			scripts = scripts.OrderBy(s => s.id).ToList();
		}

		internal void SetComment(ScriptData script, string comment) {
			if (!scriptComments.ContainsKey(script))
				scriptComments.Add(script, 0);
			comments.Add(comment);
			scriptComments[script] = comments.Count - 1;
		}

		internal void InsertComment(ScriptData beforeScript, string comment) {
			if (beforeScript == null) {
				comments.Add(comment);
				return;
			}

			if (!scriptComments.ContainsKey(beforeScript)) {
				comments.Add(comment);
				return;
			}

			int idx = scriptComments[beforeScript];
			if (idx == 0) { // not assigned yet
				comments.Add(comment);
				return;
			}
			comments.Insert(idx, comment);
			foreach (var pair in scriptComments) {
				if (pair.Value >= idx)
					scriptComments[pair.Key] += 1;
			}
		}

		public void WriteXml(XmlWriter writer) {
			if (scriptComments.Count == 0) {
				for (int i = 0; i < comments.Count; i++) {
					if (String.IsNullOrEmpty(comments[i]))
						continue;
					if (i > 1) {
						writer.WriteWhitespace("\t");
						writer.WriteComment(" " + comments[i].Trim() + " ");
						writer.WriteWhitespace("\n");
					} else {
						writer.WriteComment("\n  " + comments[i].Trim() + "\n");
						writer.WriteWhitespace("\n\n");
					}
				}
				return;
			}

			scriptComments = scriptComments.OrderBy(p => p.Key.id)
										   .ToDictionary(p => p.Key, p => p.Value);

			int last = 0;
			writer.WriteWhitespace("\n");

			foreach (var pair in scriptComments) {
				int idx = pair.Value;
				for (int i = last; i <= idx; i++) {
					if (String.IsNullOrEmpty(comments[i]))
						continue;
					if (i > 1) {
						writer.WriteWhitespace("\t");
						writer.WriteComment(" " + comments[i].Trim() + " ");
						writer.WriteWhitespace("\n");
					} else {
						writer.WriteComment("\n  " + comments[i].Trim() + "\n");
						writer.WriteWhitespace("\n\n");
					}					
				}
				last = idx + 1;

				using (var ms = new MemoryStream()) {
					var ser = new XmlSerializer(pair.Key.GetType());

					var settings = writer.Settings.Clone();
					var xFragWriter = XmlWriter.Create(ms, settings);
					ser.Serialize(ms, pair.Key);
					ms.Position = 0;

					var fragment = new XPathDocument(ms);
					var navigator = fragment.CreateNavigator();
					navigator.MoveToFirst();
					string xml = navigator.InnerXml
						.Replace(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"",
								 String.Empty);
					string[] lines = xml.Split('\n');

					// stupid writer indents with spaces anyway; fix it
					for (int line = 0; line < lines.Length; line++) {
						lines[line] = lines[line].Replace("  ", "\t").Insert(0, "\t");
					}
					writer.WriteRaw(String.Join("\n", lines));
				}
				writer.WriteWhitespace("\n");

                if (scriptComments.Last().Equals(pair) && last < comments.Count) {
                    idx = comments.Count - 1;
                    for (int i = last; i <= idx; i++) {
                        if (String.IsNullOrEmpty(comments[i]))
                            continue;
                        if (i > 1) {
                            writer.WriteWhitespace("\t");
                            writer.WriteComment(" " + comments[i].Trim() + " ");
                            writer.WriteWhitespace("\n");
                        } else {
                            writer.WriteComment("\n  " + comments[i].Trim() + "\n");
                            writer.WriteWhitespace("\n\n");
                        }
                    }
                }
			}
		}

		#endregion
	}
}
