using System.Xml.Serialization;
using System;
using System.Xml.Schema;
using System.Collections.Generic;
using System.Linq;

namespace AionQuests
{
    [Serializable]
    [XmlType(AnonymousType = true)]
    [XmlRoot(ElementName = "data", Namespace = "", IsNullable = false)]
    public partial class QuestItems
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

        [XmlIgnore]
        public List<int> Levels;

        [XmlIgnore]
        public List<string> Classes;

        Dictionary<string, Quest> nameToQuest = null;

        public void CreateIndex() {
            nameToQuest = this.QuestList.ToDictionary(p => p.name, p => p);
            Levels = this.QuestList.Select(q => q.minlevel_permitted)
                                   .Distinct()
                                   .OrderBy(i => i).ToList();
            Classes = this.QuestList.Select(q => q.class_permitted.Split(new string[] { " " },
                                                                         StringSplitOptions.RemoveEmptyEntries))
                                    .SelectMany(arr => arr).Distinct()
                                    .ToList();
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
    public partial class Quest
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
        public List<HtmlPage> HtmlPages { get; set; }

        [XmlIgnore]
        public List<HtmlPage> ValidHtmlPages {
            get {
                if (this.HtmlPages == null)
                    return null;
                var valid = this.HtmlPages.Where(p => p.name != "quest_summary" &&
                                                      p.name != "select_acqusitive_quest_desc" &&
                                                      p.name != "select_progressive_quest_desc" &&
                                                      p.name != "quest_complete" || p.ForceInclude);
                var nonEmpty = valid.Where(p => p.Content != null && p.Content.html != null &&
                                                p.Content.html.body != null && p.Content.html.body.p != null &&
                                                p.Content.html.body.p.Length > 0)
                                     .Select(p => new { Page = p, Para = p.Content.html.body.p });
                var hasText = from a in nonEmpty
                              let firstWithText = a.Para.Where(para => !String.IsNullOrEmpty(para.Value))
                                                        .FirstOrDefault()
                              where firstWithText != null
                              select a.Page;
                return hasText.ToList();
            }
        }

        public Reward GetReward(int number, out int repeatCount) {
            if (reward_repeat_count == 0 && max_repeat_count > 0)
                repeatCount = 1;
            else
                repeatCount = reward_repeat_count;

            if (number < 1 || number > 4)
                return null;

            Reward reward = new Reward();
            List<Item> items = new List<Item>();

            switch (number) {
                case 1:
                    reward.Title = reward_title1;
                    if (!String.IsNullOrEmpty(reward_exp1)) {
                        int exp;
                        if (Int32.TryParse(reward_exp1.Trim(), out exp))
                            reward.Exp = exp;
                    }
                    reward.Gold = reward_gold1;
                    reward.Inventory = reward_extend_inventory1;
                    reward.IsStigma = reward_extend_stigma1;
                    reward.AbyssPoints = reward_abyss_point1;
                    AddRewardItem(ref items, reward_item1_1);
                    AddRewardItem(ref items, reward_item1_2);
                    AddRewardItem(ref items, reward_item1_3);
                    reward.BasicItems = items;
                    items = new List<Item>();
                    AddRewardItem(ref items, selectable_reward_item1_1);
                    AddRewardItem(ref items, selectable_reward_item1_2);
                    AddRewardItem(ref items, selectable_reward_item1_3);
                    AddRewardItem(ref items, selectable_reward_item1_4);
                    AddRewardItem(ref items, selectable_reward_item1_5);
                    AddRewardItem(ref items, selectable_reward_item1_6);
                    AddRewardItem(ref items, selectable_reward_item1_7);
                    AddRewardItem(ref items, selectable_reward_item1_8);
                    break;
                case 2:
                    reward.Title = reward_title2;
                    if (!String.IsNullOrEmpty(reward_exp2)) {
                        int exp;
                        if (Int32.TryParse(reward_exp2.Trim(), out exp))
                            reward.Exp = exp;
                    }
                    reward.Gold = reward_gold2;
                    reward.AbyssPoints = reward_abyss_point2;
                    AddRewardItem(ref items, reward_item2_1);
                    reward.BasicItems = items;
                    items = new List<Item>();
                    AddRewardItem(ref items, selectable_reward_item2_1);
                    AddRewardItem(ref items, selectable_reward_item2_2);
                    AddRewardItem(ref items, selectable_reward_item2_3);
                    AddRewardItem(ref items, selectable_reward_item2_4);
                    AddRewardItem(ref items, selectable_reward_item2_5);
                    AddRewardItem(ref items, selectable_reward_item2_6);
                    break;
                case 3:
                    if (!String.IsNullOrEmpty(reward_exp3)) {
                        int exp;
                        if (Int32.TryParse(reward_exp3.Trim(), out exp))
                            reward.Exp = exp;
                    }
                    reward.Gold = reward_gold3;
                    reward.AbyssPoints = reward_abyss_point3;
                    AddRewardItem(ref items, reward_item3_1);
                    reward.BasicItems = items;
                    items = new List<Item>();
                    AddRewardItem(ref items, selectable_reward_item3_1);
                    AddRewardItem(ref items, selectable_reward_item3_2);
                    AddRewardItem(ref items, selectable_reward_item3_3);
                    AddRewardItem(ref items, selectable_reward_item3_4);
                    AddRewardItem(ref items, selectable_reward_item3_5);
                    break;
                case 4:
                    if (!String.IsNullOrEmpty(reward_exp4)) {
                        int exp;
                        if (Int32.TryParse(reward_exp4.Trim(), out exp))
                            reward.Exp = exp;
                    }
                    reward.Gold = reward_gold4;
                    reward.AbyssPoints = reward_abyss_point4;
                    AddRewardItem(ref items, reward_item4_1);
                    reward.BasicItems = items;
                    items = new List<Item>();
                    AddRewardItem(ref items, selectable_reward_item4_1);
                    AddRewardItem(ref items, selectable_reward_item4_2);
                    AddRewardItem(ref items, selectable_reward_item4_3);
                    AddRewardItem(ref items, selectable_reward_item4_4);
                    AddRewardItem(ref items, selectable_reward_item4_5);
                    break;
                default:
                    return null;
            }

            reward.AbyssRank = abyss_rank;
            reward.Skill = combineskill;
            reward.SillPoint = combine_skillpoint;
            reward.Receipe = recipe_name;
            reward.SelectItems = items;

            return reward;
        }

        public bool HasRandomRaward() {
            return reward_item1_1 != null && reward_item1_1.StartsWith("%Quest_") ||
                   reward_item1_2 != null && reward_item1_2.StartsWith("%Quest_") ||
                   reward_item1_3 != null && reward_item1_3.StartsWith("%Quest_");
        }

        void AddRewardItem(ref List<Item> itemList, string itemData) {
            if (String.IsNullOrEmpty(itemData))
                return;
            string[] data = itemData.Split(' ');
            int count = 0;
            if (data.Length != 2 || !Int32.TryParse(data[1], out count))
                count = 1;

            Item item = null;
            if (data[0].StartsWith("%Quest_")) {
                int minLevel;
                bool isLight = this.race_permitted.Contains("pc_light");
                item = Utility.ItemIndex.GetRewardBonusItem(0, isLight, this.extra_category, data[0], 
                                                            out minLevel);
                if (item == null) { // not implemented
                    item = new Item() { name = "Not implemented" };
                    item.desc_long = "Decision made reward";
                } else {
                    if (item.name == "X") {
                        RandomItemSettings settings = new RandomItemSettings()
                        {
                            Level = 10,
                            BonusName = "Christmas",
                            IsLight = isLight
                        };
                        item.RandomSettings = settings;
                        item.name = "Christmas scene";
                        item.desc_long = "Christmas cut scene for boys and girls";
                        item.level = 10;
                        List<Item> movies = new List<Item>();
                        Item it = item.Clone();
                        it.id = 135;
                        it.name += "_10";
                        movies.Add(it);
                        it = item.Clone();
                        it.name += "_11";
                        it.id = 136;
                        movies.Add(it);
                        it = item.Clone();
                        it.name += "_12";
                        it.id = 103;
                        movies.Add(it);
                        it = item.Clone();
                        it.name += "_13";
                        it.id = 136;
                        movies.Add(it);
                        item.ChooseItemsFrom = movies;
                    } else {
                        item.name = "Random reward";
                        item.desc_long = "Decision made reward";
                    }
                }
                item.icon_name = "icon_quest_dummy01";
            } else {
                item = Utility.ItemIndex.GetItem(data[0]);
            }
            if (item != null) {
                Item addItem = item.Clone();
                addItem.Count = count;
                itemList.Add(addItem);
            }
        }

        [XmlIgnore]
        public HtmlPage[][] StepPages { get; set; }

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

        // Permissions

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string class_permitted;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string race_permitted;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public bool cannot_giveup;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public bool cannot_share;

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

        // Group drop (TRUE, FALSE)

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
        public bool use_class_reward;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int reward_repeat_count;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public bool reward_extend_stigma1;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int reward_extend_inventory1;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string target_type; // force

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string title; // Only 1: Q1322

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int abyss_rank;

        // Class rewards

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public QuestItems[] fighter_selectable_reward;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public QuestItems[] knight_selectable_reward;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public QuestItems[] ranger_selectable_reward;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public QuestItems[] assassin_selectable_reward;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public QuestItems[] wizard_selectable_reward;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public QuestItems[] elementalist_selectable_reward;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public QuestItems[] priest_selectable_reward;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
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

        // Extended reward

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string reward_item_ext_1;

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
        public string reward_gold_ext;

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
        public string combineskill;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int combine_skillpoint;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string recipe_name;
    }
}

