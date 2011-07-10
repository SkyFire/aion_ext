using System.Xml.Serialization;
using System.Xml.Schema;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Drawing;
using System.Text;
using System.Xml;
using System.IO;

namespace AionQuests
{
    [Serializable]
    [XmlType(AnonymousType = true)]
    [XmlRoot(ElementName = "client_items", Namespace = "", IsNullable = false)]
    public class ItemsFile
    {
        [XmlElement("client_item", Form = XmlSchemaForm.Unqualified)]
        public List<Item> ItemList;

        Dictionary<string, Item> keyToItem = null;
        Dictionary<string, Item> redeems = null;
        Dictionary<string, Item> matterOptions = null;
        Dictionary<string, Item> magicals = null;
        Dictionary<string, Item> recipes = null;
        Dictionary<string, Item> medicines = null;
        Dictionary<string, Item> food = null;
        Dictionary<string, Item> medals = null;
        Dictionary<string, Item> coins = null;
        Dictionary<string, Item> materials = null;
        Dictionary<string, Item> masterReceipts = null;

        public static Dictionary<ItemQualities, Color> QualityColors = new Dictionary<ItemQualities, Color>()
        {
            { ItemQualities.common, Color.White },
            { ItemQualities.legend, Color.DeepSkyBlue },
            { ItemQualities.epic, Color.Orange },
            { ItemQualities.unique, Color.Gold },
            { ItemQualities.rare, Color.LightGreen },
            { ItemQualities.junk, Color.Gray },
            { ItemQualities.mythic, Color.MediumOrchid }
        };

        public void CreateIndex() {
            if (this.ItemList == null)
                return;
            keyToItem = this.ItemList.ToDictionary(i => i.name, i => i, StringComparer.InvariantCultureIgnoreCase);

            var bonuses = this.ItemList.Where(i => i.BonusApply == Bonuses.inventory);

            food = bonuses.Where(i => i.name.StartsWith("food_") && !i.name.Contains("_material_") &&
                                      !i.name.Contains("_dr_"))
                          .ToDictionary(i => i.name, i => i, StringComparer.InvariantCultureIgnoreCase);

            matterOptions = bonuses.Where(i => i.name.StartsWith("matter_option_r_") &&
                                    !i.name.StartsWith("matter_option_magical_r_"))
                                   .ToDictionary(i => i.name, i => i, StringComparer.InvariantCultureIgnoreCase);

            medicines = bonuses.Where(i => i.name.StartsWith("potion_") || i.name.StartsWith("remedy_"))
                               .ToDictionary(i => i.name, i => i, StringComparer.InvariantCultureIgnoreCase);

            medals = bonuses.Where(i => i.name.StartsWith("medal_"))
                            .ToDictionary(i => i.name, i => i, StringComparer.InvariantCultureIgnoreCase);

            coins = bonuses.Where(i => i.name.StartsWith("coin_"))
                           .ToDictionary(i => i.name, i => i, StringComparer.InvariantCultureIgnoreCase);

            recipes = bonuses.Where(i => i.name.Contains("rec_") && !i.name.Contains("_dr_"))
                             .ToDictionary(i => i.name, i => i, StringComparer.InvariantCultureIgnoreCase);

            magicals = bonuses.Where(i => i.name.Contains("magical_"))
                              .ToDictionary(i => i.name, i => i, StringComparer.InvariantCultureIgnoreCase);

            redeems = bonuses.Where(i => i.name.Contains("_redeem_"))
                             .ToDictionary(i => i.name, i => i, StringComparer.InvariantCultureIgnoreCase);

            materials = bonuses.Where(i => i.name.Contains("_material_"))
                               .ToDictionary(i => i.name, i => i, StringComparer.InvariantCultureIgnoreCase);
        }

        public string this[string itemKey] {
            get {
                itemKey = itemKey.ToLower();
                if (keyToItem == null || !keyToItem.ContainsKey(itemKey))
                    return itemKey;
                return keyToItem[itemKey].Description;
            }
        }

        public Item GetItem(string itemKey) {
            itemKey = itemKey.ToLower();
            if (keyToItem == null || !keyToItem.ContainsKey(itemKey)) {
                return null;
            }
            return keyToItem[itemKey];
        }

        public Item GetRewardBonusItem(int level, bool light, string questExtraCategory, string bonusName,
                                       out int minLevel) {
            minLevel = 0;

            if (String.IsNullOrEmpty(bonusName) || !bonusName.StartsWith("%Quest_"))
                return null;

            // save Search 
            var randomSettings = new RandomItemSettings()
            {
                BonusName = bonusName,
                ExtraCategory = questExtraCategory,
                IsLight = light,
                Level = level
            };

            var bonusMagicWords = bonusName.Split('_').Select(w => w.ToLower()).ToArray();
            int searchKeyStart = 2;
            bool isQuestL = false;
            string additionalFilter = String.Empty;

            if (bonusMagicWords[1].Length == 1)
                isQuestL = bonusMagicWords[1] == "l";
            else {
                if (bonusMagicWords[1].Length != 2) // ws etc.
                    return null;
                additionalFilter = String.Format("_{0}_", bonusMagicWords[1]);
                isQuestL = light;
            }

            int last = bonusMagicWords.Length - 1;
            if (last < 2)
                return null;

            string levelSuffix = bonusMagicWords[last];
            if (levelSuffix == "lf2a") { // only one such quest in Theobomos
                additionalFilter = levelSuffix;
                // %Quest_L_Recipe_20a_LF2A
                last--;
                levelSuffix = bonusMagicWords[last];
                Array.Resize(ref bonusMagicWords, last + 1);
            } else if (levelSuffix == "woman" || levelSuffix == "man") {
                if (bonusMagicWords[last - 1] == "christmas")
                    return new Item() { name = "X" };
            }

            if (Char.IsLetter(levelSuffix[levelSuffix.Length - 1]))
                levelSuffix = levelSuffix.Remove(levelSuffix.Length - 1, 1);
            int maxLevel = Int32.Parse(levelSuffix);

            // if the strict level, search items between that level and next
            minLevel = maxLevel;
            if (maxLevel % 10 != 0) {
                maxLevel -= maxLevel % 10;
            }

            if (level > minLevel)
                minLevel = level;

            if (level > 0)
                maxLevel += 10;
            else
                maxLevel = Int32.MaxValue; // search all

            level = minLevel;

            string[] wordsBetween = new String[bonusMagicWords.Length - searchKeyStart - 1];
            Array.Copy(bonusMagicWords, searchKeyStart, wordsBetween, 0, wordsBetween.Length);
            string searchKey = String.Join("_", wordsBetween);

            Dictionary<string, Item> searchDictionary;

            switch (questExtraCategory) {
                case "coin_quest":
                    // what does "_w_" and "_m_" mean ?
                    if (searchKey == "medal")
                        searchDictionary = medals;
                    else {
                        searchKey = searchKey.Remove(searchKey.Length - 2, 2);
                        if (searchKey != "coin")
                            return null;
                        searchDictionary = coins;
                    }
                    level = minLevel = 0;
                    break;
                case "draconic_recipe_quest":
                    if (searchKey != "master_recipe_quest")
                        return null;
                    searchDictionary = recipes;
                    break;
                case "gold_quest":
                    if (searchKey.StartsWith("rnd_"))
                        searchKey = searchKey.Remove(0, 4);
                    if (searchKey != "redeem")
                        return null;
                    searchDictionary = redeems;
                    level = minLevel = 0;
                    break;
                default:
                    if (searchKey == "fortress") {
                        // For fortress return medals;
                        searchDictionary = medals;
                        level = minLevel = 0;
                    } else if (searchKey == "island" || searchKey == "boss" || searchKey == "task") {
                        return null;
                    } else if (searchKey == "recipe") {
                        // %Quest_L_Recipe_20a_LF2A
                        searchDictionary = recipes;
                    } else if (searchKey == "matter_option") {
                        searchDictionary = matterOptions;
                    } else if (searchKey == "food") {
                        searchDictionary = food;
                    } else if (searchKey == "magical") {
                        searchDictionary = magicals;
                    } else if (searchKey == "medicine") {
                        searchDictionary = medicines;
                    } else if (searchKey == "rnd_redeem") {
                        searchDictionary = redeems;
                        level = minLevel = 0;
                    } else if (searchKey == "material") {
                        searchDictionary = materials;
                    } else if (searchKey == "master") {
                        searchDictionary = masterReceipts;
                    } else {
                        return null;
                    }
                    break;
            }

            if (!String.IsNullOrEmpty(additionalFilter)) {
                searchDictionary = searchDictionary.Where(p => p.Key.Contains(additionalFilter,
                                                               StringComparison.InvariantCultureIgnoreCase))
                                                   .ToDictionary(p => p.Key, p => p.Value);
            }

            string includeFilter, excludeFilter, raceFilter;
            if (isQuestL) {
                includeFilter = "_l_";
                excludeFilter = "_d_";
                raceFilter = "pc_light";
            } else {
                includeFilter = "_d_";
                if (searchKey == "rnd_redeem")
                    excludeFilter = "junk_redeem_jewelry";
                else
                    excludeFilter = "_l_";
                raceFilter = "pc_dark";
            }

            var include = searchDictionary.Where(p => p.Key.Contains(includeFilter,
                                                      StringComparison.InvariantCultureIgnoreCase));
            var theRest = searchDictionary.Except(include);
            var exclude = theRest.Where(p => !p.Key.Contains(excludeFilter,
                                             StringComparison.InvariantCultureIgnoreCase) &&
                                             p.Value.race_permitted.Contains(raceFilter));
            var joined = include.Concat(exclude);

            int readLevel = 0;
            var levelFiltered = from j in joined
                                let lastUnderscore = j.Key.LastIndexOf('_')
                                let strLast = lastUnderscore > 0 ?
                                              j.Key.Substring(lastUnderscore + 1) : String.Empty
                                let lastIsChar = strLast.Length < 2 ? false :
                                                 Char.IsLetter(strLast[strLast.Length - 1])
                                let strLevel = lastIsChar ? strLast.Remove(strLast.Length - 1, 1)
                                                          : strLast
                                let hadLevel = Int32.TryParse(strLevel, out readLevel)
                                let found = hadLevel && readLevel < maxLevel && readLevel >= level
                                where found
                                select j.Value;

            Item result = levelFiltered.FirstOrDefault();
            if (result != null) {
                result = result.Clone();
                result.ChooseItemsFrom = levelFiltered;
                randomSettings.Level = minLevel;
                result.RandomSettings = randomSettings;
            }
            return result;
        }

        public Item GetRewardBonusForLevel(RandomItemSettings settings, IEnumerable<Item> chooseItemsFrom, int level) {
            Dictionary<string, Item> tempDic = chooseItemsFrom.ToDictionary(i => i.name, i => i);
            int readLevel = 0;

            var levelFiltered = from j in tempDic
                                let lastUnderscore = j.Key.LastIndexOf('_')
                                let strLast = lastUnderscore > 0 ?
                                              j.Key.Substring(lastUnderscore + 1) : String.Empty
                                let lastIsChar = strLast.Length < 2 ? false :
                                                 Char.IsLetter(strLast[strLast.Length - 1])
                                let strLevel = lastIsChar ? strLast.Remove(strLast.Length - 1, 1)
                                                          : strLast
                                let hadLevel = Int32.TryParse(strLevel, out readLevel)
                                let found = hadLevel && readLevel <= level + 10 && readLevel >= level
                                where found
                                select j.Value;

            Item result = levelFiltered.FirstOrDefault();
            if (result != null) {
                result = result.Clone();
                result.ChooseItemsFrom = levelFiltered;
            }
            return result;
        }
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public class Item
    {
        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int id;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string desc;

        [XmlIgnore]
        public string Description {
            get {
                var s = Utility.StringIndex.GetStringDescription(desc.ToUpper());
                if (s == null)
                    return desc;
                return s.body;
            }
        }

        [XmlIgnore]
        public IEnumerable<Item> ChooseItemsFrom { get; set; }

        [XmlIgnore]
        public RandomItemSettings RandomSettings { get; set; }

        [XmlIgnore]
        public int Count { get; set; }

        public Item Clone() {
            return (Item)this.MemberwiseClone();
        }

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string name;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string desc_long;

        [XmlElement(ElementName = "weapon_type", Form = XmlSchemaForm.Unqualified)]
        public string WeaponType;

        [XmlElement(ElementName = "item_type", Form = XmlSchemaForm.Unqualified)]
        public ItemTypes ItemType;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string mesh;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public bool mesh_change;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string material;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public short dmg_decal;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string item_fx;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string combat_item_fx;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string icon_name;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string blade_fx;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string trail_tex;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string equip_bone;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string combat_equip_bone;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public long price;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public long abyss_point;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int max_stack_count;

        [XmlElement(ElementName = "equipment_slots", Form = XmlSchemaForm.Unqualified)]
        public EquipmentSlots EquipmentSlots;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int min_damage;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int max_damage;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string str;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string agi;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string kno;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int hit_accuracy;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int critical;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int parry;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int magical_skill_boost;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int magical_hit_accuracy;

        [XmlElement(ElementName = "attack_type", Form = XmlSchemaForm.Unqualified)]
        public AttackTypes AttackType;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int attack_delay;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public short hit_count;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public float attack_gap;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public float attack_range;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public float basic_length;

        [XmlElement(ElementName = "quality", Form = XmlSchemaForm.Unqualified)]
        public ItemQualities Quality;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int level;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public XmlBool lore;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public XmlBool can_exchange;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public XmlBool can_sell_to_npc;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public XmlBool can_deposit_to_character_warehouse;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public XmlBool can_deposit_to_account_warehouse;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public XmlBool can_deposit_to_guild_warehouse;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public XmlBool breakable;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public XmlBool soul_bind;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public XmlBool remove_when_logout;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string gender_permitted;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int warrior;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int scout;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int mage;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int cleric;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int fighter;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int knight;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int assassin;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int ranger;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int wizard;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int elementalist;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int chanter;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int priest;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public short option_slot_bonus;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string bonus_attr12;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string bonus_attr1;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string bonus_attr2;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string bonus_attr3;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string bonus_attr4;

        [XmlElement(ElementName = "bonus_apply", Form = XmlSchemaForm.Unqualified)]
        public Bonuses BonusApply;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public XmlBool no_enchant;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public XmlBool can_proc_enchant;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public bool cannot_changeskin;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string ui_sound_type;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string cash_item;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public XmlBool can_split;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public XmlBool item_drop_permitted;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string race_permitted;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string abyss_item;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int abyss_item_count;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string bonus_attr5;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string bonus_attr6;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string bonus_attr7;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string bonus_attr8;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int cash_available_minute;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string bonus_attr9;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string bonus_attr10;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public XmlBool confirm_to_delete_cash_item;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string bonus_attr11;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string ammo_bone;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string ammo_fx;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int ammo_speed;

        [XmlElement(ElementName = "armor_type", Form = XmlSchemaForm.Unqualified)]
        public ArmorTypes ArmorType;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int dodge;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int magical_resist;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int physical_defend;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public bool can_dye;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string default_color_m;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string default_color_f;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string visual_slot;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int fx_mesh;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public short extract_skin_type;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int block;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int damage_reduce;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int reduce_max;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int gathering_point;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int require_shard;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string gain_skill1;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int gain_level1;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string require_skill1;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int require_skill1_lv;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string require_skill2;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int require_skill2_lv;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string stigma_type;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string gain_skill2;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int gain_level2;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string tool_type;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string motion_name;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string combineskill;

        [XmlElement(ElementName = "category", Form = XmlSchemaForm.Unqualified)]
        public ItemCategories Category;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string use_fx;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string use_fx_bone;

        [XmlElement(ElementName = "activation_mode", Form = XmlSchemaForm.Unqualified)]
        public ActivationModes ActivationMode;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int activation_count;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int breakdown;

        [XmlElement(ElementName = "activate_target", Form = XmlSchemaForm.Unqualified)]
        public ActivateTargets ActivateTarget;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int use_delay_type_id;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int use_delay;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string craft_recipe_info;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int quest;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string activation_skill;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public short activation_level;

        [XmlElement(Form = XmlSchemaForm.Unqualified, IsNullable = true)]
        public System.Nullable<bool> disassembly_item;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int warrior_max;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int scout_max;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int mage_max;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int cleric_max;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int fighter_max;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int knight_max;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int assassin_max;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int ranger_max;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int wizard_max;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int elementalist_max;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int chanter_max;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int priest_max;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int unit_sell_count;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int casting_delay;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string return_alias;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int return_worldid;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string area_to_use;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string stat_enchant_type;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int stat_enchant_value;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string proc_enchant_skill;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int proc_enchant_skill_level;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int proc_enchant_effect_occur_prob;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string desc_proc;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string proc_fx;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string proc_enchant_effect_occur_left_prob;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string weapon_boost_value;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string dyeing_color;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string skill_to_learn;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string cash_social;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string cash_title;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public short inven_warehouse_max_extendlevel;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string cosmetic_name;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string coupon_item;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int coupon_item_count;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string doc_bg;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string toy_pet_name;

        [XmlElement(Form = XmlSchemaForm.Unqualified, IsNullable = true)]
        public System.Nullable<int> equip_type;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string difficulty;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public float scale;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public enum ItemTypes
    {
        normal = 0,
        abyss,
        draconic,
        devanion,
    }

    [Serializable]
    [Flags]
    [XmlType(AnonymousType = true)]
    public enum EquipmentSlots
    {
        none = 0,
        main = 2,
        head = 4,
        sub = 8,
        main_or_sub = 16,
        torso = 32,
        neck = 64,
        leg = 128,
        foot = 256,
        shoulder = 512,
        waist = 1024,
        glove = 2048,
        right_or_left_ear = 4096,
        right_or_left_finger = 8192,
        right_or_left_battery = 16384,
        wing = 32768,
        Legend = 65536
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public enum AttackTypes
    {
        none = 0,
        physical,
        magical_fire,
        magical_water
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public enum ItemQualities
    {
        common = 0, // baltos spalvos
        legend, // zydros spalvos
        epic, // oranzines spalvos
        unique, // geltonos spalvos
        rare, // zalios spalvos
        junk, // pilkos spalvos
        mythic, // purpurines spalvos
        Rare = rare,
        Legend = legend,
        Unique = unique
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public enum Bonuses
    {
        none = 0,
        equip,
        inventory,
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public enum ArmorTypes
    {
        none = 0,
        robe,
        clothes,
        leather,
        plate,
        no_armor,
        chain
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public enum ItemCategories
    {
        none = 0,
        armor_craft,
        cooking,
        weapon_craft,
        handiwork,
        alchemy,
        carpentry,
        tailoring,
        leatherwork,
        harvest,
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public enum ActivateTargets
    {
        none = 0,
        standalone,
        target,
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public enum ActivationModes
    {
        None = 0,
        Combat,
        Both,
        both = Both
    }
}
