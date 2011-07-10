namespace Jamie.Quests
{
	using System;
	using System.Collections.Generic;
	using System.ComponentModel;
	using System.Linq;
	using System.Reflection;
	using System.Xml.Schema;
	using System.Xml.Serialization;
	using Jamie.ParserBase;

	[Serializable]
	[XmlType(AnonymousType = true)]
	public partial class QuestDrop : IDynamicImport<Quest>, ICloneable
	{
		[XmlAttribute]
		public bool drop_each_member;

		[XmlAttribute]
		[DefaultValue(false)]
		public bool mentor;

		[XmlAttribute]
		public int chance;

		[XmlAttribute]
		public int item_id;

		[XmlAttribute]
		[DefaultValue(0)]
		public int npc_id;

		// [XmlAttribute]
		[XmlIgnore]
		public string npc_faction;

		[XmlIgnore]
		public List<string> npcIds;

		#region IDynamicImport<Quest> Members

		public void Import(Quest importObject, IEnumerable<FieldInfo> getters) {
			var getter = getters.First();
			int pos = getter.Name.LastIndexOf('_');
			string suffix = getter.Name.Substring(pos);
			List<string> listS = new List<string>();
			Utility<Quest>.Instance.Export(importObject, "drop_each_member" + suffix, listS);
			if (listS.Count > 0) {
				bool drop;
				int dm;
				if (Boolean.TryParse(listS[0].ToLower(), out drop))
					drop_each_member = drop;
				else if (Int32.TryParse(listS[0], out dm)) {
					if (dm > 0) {
						drop_each_member = true;
						if (dm > 1)
							mentor = true;
					}
				}
			}
			List<int> listI = new List<int>();
			Utility<Quest>.Instance.Export(importObject, "drop_prob" + suffix, listI);
			if (listI.Count > 0)
				chance = listI[0];
			listS = new List<string>();
			Utility<Quest>.Instance.Export(importObject, "drop_item" + suffix, listS);
			if (listS.Count > 0) {
				Item item = Utility.ItemIndex.GetItem(listS[0]);
				if (item != null)
					item_id = item.id;
			}
			listS = new List<string>();
			Utility<Quest>.Instance.Export(importObject, "drop_monster" + suffix, listS);
			if (listS.Count > 0) {
				int id = Utility.ClientNpcIndex[listS[0]];
				if (id == -1) {
					npcIds = new List<string>();
					npcIds.AddRange(listS[0].Split(new string[] { " " }, StringSplitOptions.RemoveEmptyEntries));
				} else {
					npc_id = id;
				}
			}
			if (importObject.npcfaction_name != null)
				npc_faction = importObject.npcfaction_name;
		}

		#endregion

		#region ICloneable Members

		public object Clone() {
			QuestDrop obj = (QuestDrop)this.MemberwiseClone();
			obj.npcIds = null;
			return obj;
		}

		#endregion
	}

	[Serializable]
	[XmlType(AnonymousType = true)]
	public partial class QuestItemsOur : IDynamicImport<Quest>
	{
		[XmlAttribute]
		public int count;

		[XmlAttribute]
		public int item_id;

		[XmlIgnore]
		public bool item_idSpecified;

		#region IDynamicImport<Quest> Members

		public void Import(Quest importObject, IEnumerable<FieldInfo> getters) {
			var getter = getters.First();
			string[] data = null;

			if (getter.Name.StartsWith("quest_work_item") ||
				getter.Name.StartsWith("reward_item") ||
				getter.Name.StartsWith("selectable_reward_item")) {
				List<string> listS = new List<string>();
				Utility<Quest>.Instance.Export(importObject, getter.Name, listS);
				if (listS.Count > 0) {
					data = listS[0].Split(new string[] { " " }, StringSplitOptions.RemoveEmptyEntries);
				}
			}
			if (data != null) {
				if (data.Length > 1) {
					count = Int32.Parse(data[1]);
				} else {
					count = 1;
				}
				Item item = Utility.ItemIndex.GetItem(data[0]);
				if (item == null) { // exception
					if (data[0].StartsWith("food_mpregen_10a") ||
						data[0].StartsWith("food_hpregen_10a"))
						item = Utility.ItemIndex.GetItem("shop_" + data[0]);
					if (item == null)
						return;
				}
				if (item != null) {
					item_id = item.id;
					item_idSpecified = true;
				}
			}
		}

		#endregion
	}

	[Serializable]
	[XmlType(AnonymousType = true)]
	public partial class Rewards : IDynamicImport<Quest>
	{
		[XmlElement("selectable_reward_item", Form = XmlSchemaForm.Unqualified)]
		public List<QuestItemsOur> SelectableRewards;

		[XmlElement("reward_item", Form = XmlSchemaForm.Unqualified)]
		public List<QuestItemsOur> BasicRewards;

		[XmlAttribute]
		public int extend_stigma;

		[XmlIgnore]
		public bool extend_stigmaSpecified;

		[XmlAttribute]
		public int extend_inventory;

		[XmlIgnore]
		public bool extend_inventorySpecified;

		[XmlAttribute]
        [DefaultValue(0)]
		public int title;

		[XmlAttribute]
		public int reward_abyss_point;

		[XmlIgnore]
		public bool reward_abyss_pointSpecified;

		[XmlAttribute]
		public int exp;

		[XmlIgnore]
		public bool expSpecified;

		[XmlAttribute]
		public int gold;

		[XmlIgnore]
		public bool goldSpecified;

		#region IDynamicImport<Quest> Members

		public void Import(Quest importObject, IEnumerable<FieldInfo> getters) {
			var getter = getters.First();
			if (getter.Name.StartsWith("reward_exp")) {
                List<string> listS = new List<string>();
				Utility<Quest>.Instance.Export(importObject, getter.Name, listS);
				if (listS.Count > 0) {
                    int expInt;
                    if (!Int32.TryParse(listS[0], out expInt))
                        exp = 0;
                    else
                        exp = expInt;
					if (exp > 0)
						expSpecified = true;
				}
			} else if (getter.Name.StartsWith("reward_gold") || getter.Name.StartsWith("reward_gold_ext")) {
				List<int> listI = new List<int>();
				Utility<Quest>.Instance.Export(importObject, getter.Name, listI);
				if (listI.Count > 0) {
					gold = listI[0];
					if (gold > 0)
						goldSpecified = true;
				}
			} else if (getter.Name.StartsWith("reward_abyss_point")) {
				List<int> listI = new List<int>();
				Utility<Quest>.Instance.Export(importObject, getter.Name, listI);
				if (listI.Count > 0) {
					reward_abyss_point = listI[0];
					if (reward_abyss_point > 0)
						reward_abyss_pointSpecified = true;
				}
			} else if (getter.Name.StartsWith("reward_title") || getter.Name.StartsWith("reward_title_ext")) {
				List<string> listS = new List<string>();
				Utility<Quest>.Instance.Export(importObject, getter.Name, listS);
				if (listS.Count > 0) {
					int titleIndex = Utility.TitleIndex[listS[0]];
					if (titleIndex > 0) {
						title = titleIndex;
					}
				}
			} else if (getter.Name.StartsWith("reward_extend_inventory")) {
				List<int> listI = new List<int>();
				Utility<Quest>.Instance.Export(importObject, getter.Name, listI);
				if (listI.Count > 0) {
					extend_inventory = listI[0];
					if (extend_inventory > 0)
						extend_inventorySpecified = true;
				}
			} else if (getter.Name.StartsWith("reward_extend_stigma")) {
				List<int> listI = new List<int>();
				Utility<Quest>.Instance.Export(importObject, getter.Name, listI);
				if (listI.Count > 0) {
					extend_stigma = listI[0];
					if (extend_stigma > 0)
						extend_stigmaSpecified = true;
				}
			} else if (getter.Name.StartsWith("reward_item") && getter.Name[11] != '_' ||
					   getter.Name.StartsWith("reward_item_ext_")) {
				var list = new List<QuestItemsOur>();
				Utility<Quest>.Instance.Export(importObject, getters, list);
				list = list.Where(i => i.item_id > 0 && i.count > 0).ToList();
				if (list.Count > 0)
					BasicRewards = list;
			} else if (getter.Name.StartsWith("selectable_reward_item") ||
					   getter.Name.StartsWith("selectable_reward_item_ext_")) {
				var list = new List<QuestItemsOur>();
				Utility<Quest>.Instance.Export(importObject, getters, list);
				list = list.Where(i => i.item_id > 0 && i.count > 0).ToList();
				if (list.Count > 0)
					SelectableRewards = list;
			}
		}

		#endregion

        [XmlIgnore]
        public List<QuestItemsOur> AllRewards {
            get {
                List<QuestItemsOur> list = new List<QuestItemsOur>();
                if (this.BasicRewards != null)
                    list.AddRange(this.BasicRewards);
                if (this.SelectableRewards != null)
                    list.AddRange(this.SelectableRewards);
                return list;
            }
        }
	}

	[Serializable]
	[XmlType(AnonymousType = true)]
	public partial class CollectItem : IDynamicImport<Quest>
	{
		[XmlAttribute]
		public int count;

		[XmlAttribute]
		public int item_id;

		#region IDynamicImport<Quest> Members

		public void Import(Quest importObject, IEnumerable<FieldInfo> getters) {
			var getter = getters.First();
			string[] data = null;

			if (getter.Name.StartsWith("collect_item")) {
				List<string> listS = new List<string>();
				Utility<Quest>.Instance.Export(importObject, getter.Name, listS);
				if (listS.Count > 0) {
					data = listS[0].Split(new string[] { " " }, StringSplitOptions.RemoveEmptyEntries);
				}
			}

			if (data != null) {
				if (data.Length > 1) {
					count = Int32.Parse(data[1]);
				} else {
					count = 1;
				}
				Item item = Utility.ItemIndex.GetItem(data[0]);
				if (item != null) {
					item_id = item.id;
				}
			}
		}

		#endregion
	}

	[Serializable]
	[XmlType(AnonymousType = true)]
	public partial class QuestOur
	{
		[XmlAttribute]
		public int combine_skillpoint;

		[XmlIgnore]
		public bool combine_skillpointSpecified;

		[XmlAttribute]
		public int combineskill;

		[XmlIgnore]
		public bool combineskillSpecified;

		[XmlAttribute]
		public Race race_permitted;

		[XmlIgnore]
		public bool race_permittedSpecified;

		[XmlAttribute]
		[DefaultValue(0)]
		public int use_class_reward;

		[XmlAttribute]
		[DefaultValue(false)]
		public bool cannot_giveup;

		[XmlAttribute]
		[DefaultValue(false)]
		public bool cannot_share;

		[XmlAttribute]
		public int max_repeat_count;

        [XmlAttribute]
        [DefaultValue(0)]
        public int repeat_day;

		[XmlAttribute]
		public int minlevel_permitted;

        [XmlAttribute]
        [DefaultValue(0)]
        public int maxlevel_permitted;

		[XmlAttribute]
		public int nameId;

		[XmlIgnore]
		public bool nameIdSpecified;

		[XmlAttribute]
		public string name;

		[XmlAttribute]
		public int id;

		[XmlArray(ElementName = "collect_items", Form = XmlSchemaForm.Unqualified)]
		[XmlArrayItem("collect_item", Form = XmlSchemaForm.Unqualified)]
		public List<CollectItem> CollectItems;

		[XmlIgnore]
		public bool CollectItemsSpecified;

		[XmlElement("rewards", Form = XmlSchemaForm.Unqualified)]
		public List<Rewards> Rewards;

		[XmlElement("extrewards", Form = XmlSchemaForm.Unqualified)]
		public List<Rewards> ExtRewards;

		[XmlElement("quest_drop", Form = XmlSchemaForm.Unqualified)]
		public List<QuestDrop> QuestDrops;

		[XmlArray(ElementName = "finished_quest_conds", Form = XmlSchemaForm.Unqualified)]
		[XmlArrayItem("condition", Form = XmlSchemaForm.Unqualified)]
		public List<QuestStartCondition> finished_quest_conds;

		[XmlArray(ElementName = "unfinished_quest_conds", Form = XmlSchemaForm.Unqualified)]
		[XmlArrayItem("condition", Form = XmlSchemaForm.Unqualified)]
		public List<QuestStartCondition> unfinished_quest_conds;

		[XmlArray(ElementName = "acquired_quest_conds", Form = XmlSchemaForm.Unqualified)]
		[XmlArrayItem("condition", Form = XmlSchemaForm.Unqualified)]
		public List<QuestStartCondition> acquired_quest_conds;

		[XmlArray(ElementName = "noacquired_quest_conds", Form = XmlSchemaForm.Unqualified)]
		[XmlArrayItem("condition", Form = XmlSchemaForm.Unqualified)]
		public List<QuestStartCondition> noacquired_quest_conds;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public string class_permitted;

		[XmlIgnore]
		public bool class_permittedSpecified;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		[DefaultValue(Gender.ALL)]
		public Gender gender_permitted;

		[XmlIgnore]
		public bool gender_permittedSpecified;

		[XmlArray(ElementName = "quest_work_items", Form = XmlSchemaForm.Unqualified)]
		[XmlArrayItem("quest_work_item", Form = XmlSchemaForm.Unqualified)]
		public List<QuestItemsOur> QuestWorkItems;

		[XmlIgnore]
		public bool QuestWorkItemsSpecified;

		[XmlElement("fighter_selectable_reward", Form = XmlSchemaForm.Unqualified)]
		public List<QuestItemsOur> FighterSelectableRewards;

		[XmlElement("knight_selectable_reward", Form = XmlSchemaForm.Unqualified)]
		public List<QuestItemsOur> KnightSelectableRewards;

		[XmlElement("ranger_selectable_reward", Form = XmlSchemaForm.Unqualified)]
		public List<QuestItemsOur> RangerSelectableRewards;

		[XmlElement("assassin_selectable_reward", Form = XmlSchemaForm.Unqualified)]
		public List<QuestItemsOur> AssassinSelectableRewards;

		[XmlElement("wizard_selectable_reward", Form = XmlSchemaForm.Unqualified)]
		public List<QuestItemsOur> WizardSelectableRewards;

		[XmlElement("elementalist_selectable_reward", Form = XmlSchemaForm.Unqualified)]
		public List<QuestItemsOur> ElementalistSelectableRewards;

		[XmlElement("priest_selectable_reward", Form = XmlSchemaForm.Unqualified)]
		public List<QuestItemsOur> PriestSelectableRewards;

		[XmlElement("chanter_selectable_reward", Form = XmlSchemaForm.Unqualified)]
		public List<QuestItemsOur> ChanterSelectableRewards;

        [XmlIgnore]
        public List<QuestItemsOur> AllRewards {
            get {
                List<QuestItemsOur> list = new List<QuestItemsOur>();
                if (this.FighterSelectableRewards != null)
                    list.AddRange(this.FighterSelectableRewards);
                if (this.KnightSelectableRewards != null)
                    list.AddRange(this.KnightSelectableRewards);
                if (this.RangerSelectableRewards != null)
                    list.AddRange(this.RangerSelectableRewards);
                if (this.AssassinSelectableRewards != null)
                    list.AddRange(this.AssassinSelectableRewards);
                if (this.WizardSelectableRewards != null)
                    list.AddRange(this.WizardSelectableRewards);
                if (this.ElementalistSelectableRewards != null)
                    list.AddRange(this.ElementalistSelectableRewards);
                if (this.PriestSelectableRewards != null)
                    list.AddRange(this.PriestSelectableRewards);
                if (this.ChanterSelectableRewards != null)
                    list.AddRange(this.ChanterSelectableRewards);
                foreach (var rew in this.Rewards) {
                    list.AddRange(rew.AllRewards);
                }
                return list;
            }
        }

		public void AddClassRewards(Quest quest) {
			if (quest.assassin_selectable_reward != null) {
				this.AssassinSelectableRewards = new List<QuestItemsOur>();
				foreach (var r in quest.assassin_selectable_reward) {
					QuestItemsOur our = GetRewardItem(r.assassin_selectable_item);
					if (our != null)
						this.AssassinSelectableRewards.Add(our);
				}
			}

			if (quest.chanter_selectable_reward != null) {
				this.ChanterSelectableRewards = new List<QuestItemsOur>();
				foreach (var r in quest.chanter_selectable_reward) {
					QuestItemsOur our = GetRewardItem(r.chanter_selectable_item);
					if (our != null)
						this.ChanterSelectableRewards.Add(our);
				}
			}

			if (quest.elementalist_selectable_reward != null) {
				this.ElementalistSelectableRewards = new List<QuestItemsOur>();
				foreach (var r in quest.elementalist_selectable_reward) {
					QuestItemsOur our = GetRewardItem(r.elementalist_selectable_item);
					if (our != null)
						this.ElementalistSelectableRewards.Add(our);
				}
			}

			if (quest.fighter_selectable_reward != null) {
				this.FighterSelectableRewards = new List<QuestItemsOur>();
				foreach (var r in quest.fighter_selectable_reward) {
					QuestItemsOur our = GetRewardItem(r.fighter_selectable_item);
					if (our != null)
						this.FighterSelectableRewards.Add(our);
				}
			}

			if (quest.knight_selectable_reward != null) {
				this.KnightSelectableRewards = new List<QuestItemsOur>();
				foreach (var r in quest.knight_selectable_reward) {
					QuestItemsOur our = GetRewardItem(r.knight_selectable_item);
					if (our != null)
						this.KnightSelectableRewards.Add(our);
				}
			}

			if (quest.priest_selectable_reward != null) {
				this.PriestSelectableRewards = new List<QuestItemsOur>();
				foreach (var r in quest.priest_selectable_reward) {
					QuestItemsOur our = GetRewardItem(r.priest_selectable_item);
					if (our != null)
						this.PriestSelectableRewards.Add(our);
				}
			}

			if (quest.ranger_selectable_reward != null) {
				this.RangerSelectableRewards = new List<QuestItemsOur>();
				foreach (var r in quest.ranger_selectable_reward) {
					QuestItemsOur our = GetRewardItem(r.ranger_selectable_item);
					if (our != null)
						this.RangerSelectableRewards.Add(our);
				}
			}

			if (quest.wizard_selectable_reward != null) {
				this.WizardSelectableRewards = new List<QuestItemsOur>();
				foreach (var r in quest.wizard_selectable_reward) {
					QuestItemsOur our = GetRewardItem(r.wizard_selectable_item);
					if (our != null)
						this.WizardSelectableRewards.Add(our);
				}
			}
        }

        static QuestItemsOur GetRewardItem(string data) {
            string[] parts = data.Split(new string[] { " " }, StringSplitOptions.RemoveEmptyEntries);
            int itemCount = 0;
            if (parts.Length > 1) {
                itemCount = Int32.Parse(parts[1]);
            } else {
                itemCount = 1;
            }
            int id = 0;
            Item item = Utility.ItemIndex.GetItem(parts[0]);
            if (item != null) {
                id = item.id;
            }
            if (id == 0)
                return null;
            return new QuestItemsOur() { count = itemCount, item_id = id, item_idSpecified = true };
        }

		public QuestOur() {
			this.cannot_share = false;
			this.cannot_giveup = false;
		}
	}

	[Serializable]
	public enum Race
	{
		NONE = 0,
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
		PC_ALL,
		GOBLIN,
		GENERAL,
		NPC
	}

	[Serializable]
	[XmlRoot("quests", Namespace = "", IsNullable = false)]
	public partial class quest_data
	{
		[XmlElement("import")]
		public List<TemplateImportPart> Imports;

		[XmlElement("quest", Form = XmlSchemaForm.Unqualified)]
		public List<QuestOur> Quests;
	}

    [Serializable]
    [XmlType(AnonymousType = true)]
    public class QuestStartCondition
    {
        [XmlElement("quest", Form = XmlSchemaForm.Unqualified)]
        public List<QuestStep> questSteps;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class QuestStep
    {
        public QuestStep() { }

        public QuestStep(int questId, int rewardNo) {
            this.questId = questId;
            this.rewardNo = rewardNo;
        }

        [XmlAttribute(AttributeName = "id")]
        public int questId;

        [XmlAttribute]
        [DefaultValue(0)]
        public int rewardNo;

        public static bool operator >(QuestStep lhs, QuestStep rhs) {
            return lhs.questId == rhs.questId && lhs.rewardNo > rhs.rewardNo ||
                   lhs.questId > rhs.questId;
        }

        public static bool operator <(QuestStep lhs, QuestStep rhs) {
            return lhs.questId == rhs.questId && lhs.rewardNo < rhs.rewardNo ||
                   lhs.questId < rhs.questId;
        }

        public static bool operator >=(QuestStep lhs, QuestStep rhs) {
            return lhs.questId == rhs.questId && lhs.rewardNo >= rhs.rewardNo ||
                   lhs.questId > rhs.questId;
        }

        public static bool operator <=(QuestStep lhs, QuestStep rhs) {
            return lhs.questId == rhs.questId && lhs.rewardNo <= rhs.rewardNo ||
                   lhs.questId < rhs.questId;
        }

        public static bool operator ==(QuestStep lhs, QuestStep rhs) {
            return lhs.questId == rhs.questId && lhs.rewardNo == rhs.rewardNo;
        }

        public static bool operator !=(QuestStep lhs, QuestStep rhs) {
            return lhs.questId != rhs.questId || lhs.rewardNo != rhs.rewardNo;
        }

        public static int operator -(QuestStep lhs, QuestStep rhs) {
            if (lhs.questId != rhs.questId)
                return 0;
            return lhs.rewardNo - rhs.rewardNo;
        }

        public override string ToString() {
            if (this.rewardNo == 0)
                return this.questId.ToString();
            return String.Format("{0}:{1}", this.questId, this.rewardNo);
        }

        public override int GetHashCode() {
            int hash = 1000000007 * this.questId;
            hash += 1000000009 * this.rewardNo;
            return hash;
        }

        public override bool Equals(object obj) {
            return base.Equals(obj);
        }
    }
}

