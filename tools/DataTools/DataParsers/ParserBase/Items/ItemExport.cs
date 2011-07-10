namespace Jamie.Items
{
	using System;
	using System.Collections.Generic;
	using System.ComponentModel;
	using System.Linq;
	using System.Xml.Serialization;
	using Jamie.ParserBase;
	using Jamie.Skills;

	[Serializable]
	[XmlRoot(ElementName = "items")]
	public class ItemsExportFile
	{
		[XmlElement(ElementName = "item")]
		public List<ItemExport> items = new List<ItemExport>();

		[XmlElement(ElementName = "group")]
		public List<ItemGroups> grouped = new List<ItemGroups>();

		[XmlIgnore]
		public Dictionary<string, ItemExport> allNames =
			new Dictionary<string, ItemExport>(StringComparer.InvariantCultureIgnoreCase);

		[XmlIgnore]
		public IEnumerable<ItemExport> Manastones {
			get {
				if (grouped == null)
					return Enumerable.Empty<ItemExport>();
				var suffixes = grouped.Where(g => g.group == "manastone_reward").Select(g => g.items);
				return suffixes.SelectMany(s => s).SelectMany(s => s.items);
			}
		}

		[XmlIgnore]
		public IEnumerable<ItemExport> CoinRewards {
			get {
				if (grouped == null)
					return Enumerable.Empty<ItemExport>();
				var suffixes = grouped.Where(g => g.group == "coin_rewards").Select(g => g.items);
				return suffixes.SelectMany(s => s).SelectMany(s => s.items);
			}
		}

		[XmlIgnore]
		public IEnumerable<ItemExport> FoodRewards {
			get {
				if (grouped == null)
					return Enumerable.Empty<ItemExport>();
				var suffixes = grouped.Where(g => g.group == "food").Select(g => g.items);
				return suffixes.SelectMany(s => s).SelectMany(s => s.items);
			}
		}

		[XmlIgnore]
		public IEnumerable<ItemExport> EnchantRewards {
			get {
				if (grouped == null)
					return Enumerable.Empty<ItemExport>();
				var suffixes = grouped.Where(g => g.group == "enchant").Select(g => g.items);
				return suffixes.SelectMany(s => s).SelectMany(s => s.items);
			}
		}

		[XmlIgnore]
		public IEnumerable<ItemExport> MedicineRewards {
			get {
				if (grouped == null)
					return Enumerable.Empty<ItemExport>();
				var suffixes = grouped.Where(g => g.group == "medicine").Select(g => g.items);
				return suffixes.SelectMany(s => s).SelectMany(s => s.items);
			}
		}

		[XmlIgnore]
		public IEnumerable<ItemExport> BossRewards {
			get {
				if (grouped == null)
					return Enumerable.Empty<ItemExport>();
				var suffixes = grouped.Where(g => g.group == "boss").Select(g => g.items);
				return suffixes.SelectMany(s => s).SelectMany(s => s.items);
			}
		}

		[XmlIgnore]
		public IEnumerable<ItemExport> Documents {
			get {
				if (grouped == null)
					return Enumerable.Empty<ItemExport>();
				var suffixes = grouped.Where(g => g.group == "docs").Select(g => g.items);
				return suffixes.SelectMany(s => s).SelectMany(s => s.items);
			}
		}

        [XmlIgnore]
        public IEnumerable<ItemExport> QuestItems {
            get {
                if (grouped == null)
                    return Enumerable.Empty<ItemExport>();
                var suffixes = grouped.Where(g => g.group == "quest_items").Select(g => g.items);
                return suffixes.SelectMany(s => s).SelectMany(s => s.items);
            }
        }

        [XmlIgnore]
        public IEnumerable<ItemExport> WorkOrderItems {
            get {
                if (grouped == null)
                    return Enumerable.Empty<ItemExport>();
                var suffixes = grouped.Where(g => g.group == "craft_tasks").Select(g => g.items);
                return suffixes.SelectMany(s => s).SelectMany(s => s.items);
            }
        }

        [XmlIgnore]
        public IEnumerable<ItemExport> MasterRecipes {
            get {
                if (grouped == null)
                    return Enumerable.Empty<ItemExport>();
                var suffixes = grouped.Where(g => g.group == "hearts").Select(g => g.items);
                return suffixes.SelectMany(s => s).SelectMany(s => s.items);
            }
        }
	}

	[Serializable]
	public class ItemExport
	{
		[XmlAttribute(AttributeName = "id")]
		public int itemId;

		[XmlAttribute(AttributeName = "name")]
		public string desc;

		public ItemRace race;

		[XmlElement]
		[DefaultValue(ItemRace.ALL)]
		public ItemRace raceInternal;

		[XmlAttribute]
		[DefaultValue(0)]
		public int suffix;

		[XmlAttribute]
		[DefaultValue(0)]
		public int rentDays;

		[XmlElement(ElementName = "name")]
		public string itemName;

		[XmlIgnore]
		public ItemCategories category;

		[XmlIgnore]
		[DefaultValue(ArmorTypes.none)]
		public ArmorTypes armorType;

		[XmlIgnore]
		[DefaultValue(WeaponTypes.None)]
		public WeaponTypes weaponType;

		[XmlIgnore]
		[DefaultValue(EquipmentSlots.none)]
		public EquipmentSlots slot;

		[XmlIgnore]
		public ItemTypes itemType;

		[XmlIgnore]
		public Qualities quality;

		[XmlAttribute("group")]
		public string itemGroup;

		[XmlAttribute("master")]
		[DefaultValue(false)]
		public bool isMaster;

		[XmlIgnore]
		public int mask;

		[XmlIgnore]
		public int level;

		[XmlIgnore]
		[DefaultValue(false)]
		public bool canFuse;

		[XmlIgnore]
		public string itemIcon;

		[XmlAttribute]
		[DefaultValue(0)]
		public int expire_time;

		[XmlIgnore]
		public int cash_item;

		[XmlIgnore]
		public int cash_minute;

		[XmlIgnore]
		public int exchange_time;

		[XmlIgnore]
		public string restricts;

		[XmlIgnore]
		public string restricts_max;

		[XmlIgnore]
		public SkillTemplate skill_use;

		[XmlIgnore]
		public List<Modifier> modifiers;

		[XmlIgnore]
		public Item originalItem;

        [XmlIgnore]
        public int minLevel;

        [XmlIgnore]
        public ItemTag tag;
	}

	[Serializable]
	[XmlType(AnonymousType = true)]
	public enum Qualities
	{
		Common = 0,
		Rare,
		Unique,
        Legendary,
		Mythic,
        Epic,
		Junk
	}
}
