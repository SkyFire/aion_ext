namespace Jamie.ParserBase
{
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Xml;
    using System.Xml.Schema;
    using System.Xml.Serialization;
    using Jamie.ParserBase;
    using System.Reflection;
    using Jamie.Items;
    using Jamie.ParserBase.Skills;
    using Jamie.Quests;

    [Serializable]
    [XmlType(AnonymousType = true)]
    [XmlRoot(ElementName = "data", Namespace = "", IsNullable = false)]
    public class QuestItems
    {
        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string fighter_selectable_item;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string knight_selectable_item;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string ranger_selectable_item;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string assassin_selectable_item;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string wizard_selectable_item;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string elementalist_selectable_item;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string priest_selectable_item;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string chanter_selectable_item;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    [XmlRoot(ElementName = "quests", Namespace = "", IsNullable = false)]
    public partial class QuestsFile
    {
        [XmlElement("quest", Form = XmlSchemaForm.Unqualified)]
        public List<Quest> QuestList;

        Dictionary<string, Quest> nameToQuest = null;

        internal void CreateIndex() {
            nameToQuest = this.QuestList.ToDictionary(p => p.name, p => p);
        }

        public Quest this[string stringName] {
            get {
                if (nameToQuest == null || !nameToQuest.ContainsKey(stringName))
                    return null;
                return nameToQuest[stringName];
            }
        }
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class Quest : IAdvice
    {
        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int id;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string dev_name;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string name;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string desc;

        [XmlIgnore]
        public StringDescription Description {
            get {
                return Utility.StringIndex.GetStringDescription(desc);
            }
        }

        [XmlIgnore]
        public QuestOur OurQuest { get; set; }

        [XmlAnyElement]
        public List<XmlNode> nodes;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string category1;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string category2;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string extra_category;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public bool f_mission;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string @__type_desc__;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int max_repeat_count;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int client_level;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int minlevel_permitted;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int maxlevel_permitted;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string gender_permitted;

        [XmlIgnore]
        public bool gender_permittedSpecified;

        // Permissions

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string class_permitted;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string race_permitted;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public bool cannot_giveup;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public bool cannot_share;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int package_permitted;

        // Finished quests

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string finished_quest_cond1; // Q2411:2 or Q2373,Q2374

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string finished_quest_cond2;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string finished_quest_cond3;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string finished_quest_cond4;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string finished_quest_cond5;

        // Unfinished quests

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string unfinished_quest_cond1;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string unfinished_quest_cond2;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string unfinished_quest_cond3;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string unfinished_quest_cond4;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string unfinished_quest_cond5;

        // Aquired quests

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string acquired_quest_cond1;

        // Not aquired quests

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string noacquired_quest_cond1;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string noacquired_quest_cond2;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string noacquired_quest_cond3;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string noacquired_quest_cond4;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string noacquired_quest_cond5;

        // Collect items

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int collect_progress;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string collect_item1;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string collect_item2;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string collect_item3;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string collect_item4;

        // Item checks

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string check_item1_1; // junk_master_recipe_quest_50a 3

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string check_item1_2;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string check_item1_3;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string check_item1_4;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string check_item2_1;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string check_item3_1;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string check_item4_1;

        // Monsters drops

        // Monsters

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string drop_monster_1;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string drop_monster_2;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string drop_monster_3;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string drop_monster_4;

        // Drops

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string drop_item_1;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string drop_item_2;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string drop_item_3;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string drop_item_4;

        // Drop probability (percents)

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int drop_prob_1;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int drop_prob_2;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int drop_prob_3;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int drop_prob_4;

        // Group drop (TRUE, FALSE, 0, 1, 2)

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string drop_each_member_1;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
		public string drop_each_member_2;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
		public string drop_each_member_3;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
		public string drop_each_member_4;

        // Experience

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string reward_exp1;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string reward_exp2;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string reward_exp3;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string reward_exp4;

        // Reward options

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int use_class_reward;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int reward_repeat_count;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int reward_extend_stigma1;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int reward_extend_inventory1;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string target_type; // force

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string title; // Only 1: Q1322

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int abyss_rank;

        // Class rewards

        [XmlArray(Form = XmlSchemaForm.Unqualified)]
        [XmlArrayItem(ElementName = "data")]
        public QuestItems[] fighter_selectable_reward;

        [XmlArray(Form = XmlSchemaForm.Unqualified)]
        [XmlArrayItem(ElementName = "data")]
        public QuestItems[] knight_selectable_reward;

        [XmlArray(Form = XmlSchemaForm.Unqualified)]
        [XmlArrayItem(ElementName = "data")]
        public QuestItems[] ranger_selectable_reward;

        [XmlArray(Form = XmlSchemaForm.Unqualified)]
        [XmlArrayItem(ElementName = "data")]
        public QuestItems[] assassin_selectable_reward;

        [XmlArray(Form = XmlSchemaForm.Unqualified)]
        [XmlArrayItem(ElementName = "data")]
        public QuestItems[] wizard_selectable_reward;

        [XmlArray(Form = XmlSchemaForm.Unqualified)]
        [XmlArrayItem(ElementName = "data")]
        public QuestItems[] elementalist_selectable_reward;

        [XmlArray(Form = XmlSchemaForm.Unqualified)]
        [XmlArrayItem(ElementName = "data")]
        public QuestItems[] priest_selectable_reward;

        [XmlArray(Form = XmlSchemaForm.Unqualified)]
        [XmlArrayItem(ElementName = "data")]
        public QuestItems[] chanter_selectable_reward;

        // Reward item 1

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string reward_item1_1;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string selectable_reward_item1_1;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string reward_item1_2;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string selectable_reward_item1_2;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string reward_item1_3;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string reward_item1_4;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string selectable_reward_item1_3;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string selectable_reward_item1_4;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string selectable_reward_item1_5;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string selectable_reward_item1_6;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string selectable_reward_item1_7;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string selectable_reward_item1_8;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int reward_gold1;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string reward_title1;

        // Reward item 2

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string reward_item2_1;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string selectable_reward_item2_1;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string selectable_reward_item2_2;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string selectable_reward_item2_3;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string selectable_reward_item2_4;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string selectable_reward_item2_5;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string selectable_reward_item2_6;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int reward_gold2;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string reward_title2;

        // Reward item 3

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string reward_item3_1;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string selectable_reward_item3_1;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string selectable_reward_item3_2;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string selectable_reward_item3_3;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string selectable_reward_item3_4;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string selectable_reward_item3_5;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int reward_gold3;

        // Reward item 4

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string reward_item4_1;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string selectable_reward_item4_3;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string selectable_reward_item4_4;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string selectable_reward_item4_5;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string selectable_reward_item4_1;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string selectable_reward_item4_2;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int reward_gold4;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int reward_abyss_point4;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string inventory_item_name1;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string inventory_item_name2;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string inventory_item_name3;

        // Extended reward

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string reward_item_ext_1;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string reward_item_ext_2;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string selectable_reward_item_ext_1;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string selectable_reward_item_ext_2;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string selectable_reward_item_ext_3;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string selectable_reward_item_ext_4;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string selectable_reward_item_ext_5;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string selectable_reward_item_ext_6;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string selectable_reward_item_ext_7;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string selectable_reward_item_ext_8;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int reward_gold_ext;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string reward_title_ext;

        // Abyss reward

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int reward_abyss_point1;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int reward_abyss_point2;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int reward_abyss_point3;

        // Work items

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string quest_work_item1;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string quest_work_item2;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string quest_work_item3;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string quest_work_item4;

        // Equiped items (item sets)

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string equiped_item_name1;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string equiped_item_name2;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string equiped_item_name3;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string equiped_item_name4;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string equiped_item_name5;

        // Recipe reward
        // category1 = task

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public CombineSkillType combineskill;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int combine_skillpoint;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string recipe_name;

        // Other
        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string npcfaction_name;

        // 2.1
        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public RepeatCycle quest_repeat_cycle;

        public bool HasRandomRaward() {
            return reward_item1_1 != null && reward_item1_1.StartsWith("%Quest_") ||
                   reward_item1_2 != null && reward_item1_2.StartsWith("%Quest_") ||
                   reward_item1_3 != null && reward_item1_3.StartsWith("%Quest_") ||
                   reward_item1_4 != null && reward_item1_4.StartsWith("%Quest_");
        }

        #region IAdvice Members

        public Type GetClassType(FieldInfo getter) {
            string fieldValue = getter.GetValue(this) as String;
            if (String.IsNullOrEmpty(fieldValue) || !fieldValue.StartsWith("%Quest_"))
                return null;
            string data = fieldValue.Remove(0, 7).ToLower();
            if (data.Contains("_boss_"))
                return typeof(BossBonus);
            if (data.Contains("_coin_"))
                return typeof(CoinBonus);
            if (data.Contains("_food_"))
                return typeof(FoodBonus);
            if (data.Contains("_fortress_"))
                return typeof(FortressBonus);
            if (data.Contains("_goods_"))
                return typeof(GoodsBonus);
            if (data.Contains("_island_"))
                return typeof(IslandBonus);
            if (data.Contains("_magical_"))
                return typeof(MagicalBonus);
            if (data.Contains("_master_recipe_"))
                return typeof(MasterRecipeBonus);
            if (data.Contains("_matter_option_"))
                return typeof(ManastoneBonus);
            if (data.Contains("_material_"))
                return typeof(MaterialBonus);
            if (data.Contains("_medal_"))
                return typeof(MedalBonus);
            if (data.Contains("_medicine_"))
                return typeof(MedicineBonus);
            if (data.Contains("_christmas_"))
                return typeof(CutSceneBonus);
            if (data.Contains("_recipe_"))
                return typeof(RecipeBonus);
            if (data.Contains("_redeem_"))
                return typeof(RedeemBonus);
            if (data.Contains("_task_"))
                return typeof(WorkOrderBonus);
            return null;
        }

        #endregion
    }

    public enum RepeatCycle
    {
        all = 0,
        mon,
        tue,
        wed,
        thu,
        fri,
        sat,
        sun
    }
}

