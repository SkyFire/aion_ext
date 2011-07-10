namespace Jamie.Pets
{
    using System;
    using System.Linq;
    using System.Xml.Serialization;
    using System.ComponentModel;
    using System.Collections.Generic;
    using System.Xml.Schema;
    using Jamie.ParserBase;
    using System.Reflection;
    using System.Diagnostics;
    using Jamie.Items;

    [Serializable]
    public enum FoodType
    {
        ALL = -1,
        NONE = 0,
        ARMOR = 5,
        BALAUR = 8,
        BISCUIT = 5000,
        BONE = 7,
        CRYSTAL = 5002,
        FLUID = 4,
        GEM = 5003,
        HEALTHY1 = 10,
        HEALTHY2 = 11,
        POWDER = 5001,
        SOUL = 9,
        THORN = 6,
        MISC = 3,
        DOPING = 10000,
        MISC1 = 1,
        MISC2 = 2,
        CASH1 = 101,
        CASH2 = 102,
        CASH3 = 103,
        CASH4 = 104,
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    [XmlRoot(ElementName = "pet_feed", Namespace = "", IsNullable = false)]
    public class PetFeed
    {
        [XmlArray("groups")]
        [XmlArrayItem("group", Form = XmlSchemaForm.Unqualified)]
        public ItemGroup[] ItemGroups;

        [XmlElement("flavour", Form = XmlSchemaForm.Unqualified)]
        public List<PetFlavour> Flavours = new List<PetFlavour>();
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public class ItemGroup
    {
        [XmlAttribute]
        public FoodType type;

        [XmlText]
        public string itemids;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public class PetFlavour
    {
        [XmlElement("food", Form = XmlSchemaForm.Unqualified)]
        public List<PetFood> food;

        [XmlAttribute]
        public int id;

        [XmlAttribute]
        public int count;

        [XmlAttribute]
        [DefaultValue(0)]
        public int love_count;

        [XmlAttribute]
        public int cd;

        [XmlAttribute]
        [DefaultValue("")]
        public string name;

        [XmlAttribute]
        [DefaultValue("")]
        public string desc;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public class PetFood : IDynamicImport<ToypetFeed>
    {
        [XmlAttribute]
        public FoodType type;

		[XmlElement("result", Form = XmlSchemaForm.Unqualified)]
		public List<PetReward> rewards = new List<PetReward>();

        [XmlAttribute]
        [DefaultValue(false)]
        public bool loved;

        [XmlAttribute]
        [DefaultValue("")]
        public string desc;

        public PetFood() {
            type = FoodType.ALL;
        }

        #region IDynamicImport<ToypetFeed> Members

        public void Import(ToypetFeed importObject, IEnumerable<FieldInfo> getters) {
            var fd = getters.First();
            if (fd.Name.StartsWith("love_flavor_id_") || fd.Name == "favorite_flavor_id") {
                int id = (int)fd.GetValue(importObject);
                string value = Utility.StringIndex.GetString("STR_FLAVOR_ID_" + id.ToString());
                type = (FoodType)id;
            }
            if (fd.Name.StartsWith("love_flavor_id_")) {
                loved = true;
            } else if (fd.Name.StartsWith("love_flavor_desc_") || fd.Name == "favorite_flavor_desc") {
                string key = (string)fd.GetValue(importObject);
                string descr = Utility.StringIndex.GetString(key);
                if (String.IsNullOrEmpty(descr))
                    return;

                ItemRace itemRace = ItemRace.ALL;
                string addPrefix = null;
                int[] appendItems = null;
                string flavourName = importObject.name;

                if (flavourName.StartsWith("event_")) {
                    string name = "wrap_event_box" + importObject.name.Remove(0, 13).Replace("_u_", "_");
                    Item item = Utility.ItemIndex.GetItem(name);
                    appendItems = new int[] { item.id };
                    flavourName = flavourName.Remove(0, 6);
                }

                if (flavourName.StartsWith("feeding_")) {
                    if (importObject.name.IndexOf("l_u_", 8) != -1) {
                        itemRace = ItemRace.ELYOS;
                        addPrefix = "Premium ";
                    } else if (importObject.name.IndexOf("d_u_", 8) != -1) {
                        itemRace = ItemRace.ASMODIANS;
                        addPrefix = "Premium ";
                    }
                }
				
                int start = descr.IndexOf("receive a", StringComparison.InvariantCultureIgnoreCase);
                string feedItem = String.Empty;
				if (descr.IndexOf("(ID 10") != -1) {
					feedItem = "CashItemFeeding_Reward_10" + descr[descr.Length - 2];
				} else if (start == -1) {
                    start = descr.IndexOf("obtain a", StringComparison.InvariantCultureIgnoreCase);
                    if (start != -1) {
                        int end = descr.IndexOf(" by ", start + 4, StringComparison.InvariantCultureIgnoreCase);
                        if (end == -1) {
                            end = descr.IndexOf(" with ", start + 8, StringComparison.InvariantCultureIgnoreCase);
                        }
                        feedItem = descr.Substring(start + 9, end - start - 9).Trim(new char[] { ' ' });
                    }

                } else {
                    feedItem = descr.Substring(start + 10).Trim(new char[] { ' ', '.' });
                }
                if (key == "STR_FLAVOR_RESULT_L_OD04_02A" && feedItem == "Golden Drakie Gift") {
                    feedItem = "Golden Radama Gift"; // fix bug in description
                } else if (key.IndexOf("_OD05_") != -1) {
                    if (loved) {
                        if (key.EndsWith("01A"))
							rewards.Add(new PetReward(188051353));
                        else
							rewards.Add(new PetReward(188051354));
                    } else {
						rewards.Add(new PetReward(188051355));
						rewards.Add(new PetReward(188051356));
						rewards.Add(new PetReward(188051357));
						rewards.Add(new PetReward(188051358));
						rewards.Add(new PetReward(188051359));
                    }
                    return;
                } else if (feedItem.StartsWith("Sharp Claw")) { // fix it too
                    feedItem = feedItem.Replace("Sharp Claw", "Spurred");
                } else if (flavourName == "cash_feeding_u_Griffopink_01" && feedItem.StartsWith("Aqua")) {
                    feedItem = feedItem.Replace("Aqua", "Pink");
                }

				// include other set if description is "Produces something"
				if (importObject.id == 10 || importObject.id == 24) {
				}
				string matchName = null;
				if (importObject.desc.IndexOf("HEAD") != -1)
					matchName = "HEAD";
				else if (importObject.desc.IndexOf("ENCHANT") != -1)
					matchName = "ENCHANT";

				desc = feedItem;

				// add a prefix, so it doesn't pick up a single item
                if (addPrefix != null)
                    feedItem = addPrefix + feedItem;

                var sameDescr = Utility.StringIndex.StringList.Where(d => d.body == feedItem);

                if (!sameDescr.Any() && type == FoodType.NONE) {
                    Debug.Print("Unknown pet feed exchange: {0}", (string)fd.GetValue(importObject));
                    return;
                }

                foreach (var test in sameDescr) {
                    var items = Utility.ItemIndex.GetItemsByDescription(test.name);
                    foreach (var testItem in items) {
                        if (testItem.race == itemRace)
							AddItems(test, feedItem, itemRace, matchName);
                    }
                }
				if (appendItems != null) {
					foreach (int id in appendItems)
						rewards.Add(new PetReward(id));
				}
            }
        }

        void AddItems(StringDescription strDescr, string feedItem, ItemRace itemRace, string matchName) {
            var item = Utility.ItemIndex.GetItemsByDescription(strDescr.name);
            string commonName = feedItem.Substring(feedItem.IndexOf(' ') + 1);
            var common = Utility.StringIndex.StringList.Where(d => d.body == commonName).FirstOrDefault();
            if (common != null) {
                // check race
                var commonItem = Utility.ItemIndex.GetItemsByDescription(common.name).Where(i => i.race == itemRace);
                if (!commonItem.Any())
                    common = null;
            }

            if (common == null) {
                string prefix = strDescr.name.Substring(0, strDescr.name.Length - 3).ToUpper();
                string ending = strDescr.name[strDescr.name.Length - 1].ToString();
                var bundles = Utility.StringIndex.StringList.Where(d => d.name.ToUpper().StartsWith(prefix) &&
                                                                        d.name.EndsWith(ending) &&
                                                                        !d.name.EndsWith("_DESC"));
                foreach (var bundle in bundles) {
                    item = Utility.ItemIndex.GetItemsByDescription(bundle.name);
                    if (item.Any()) {
                        if (item.First().race != itemRace)
                            continue;
						if (matchName != null) {
							bool matched = item.First().name.IndexOf(matchName, StringComparison.InvariantCultureIgnoreCase) != -1;
							if (!matched)
								continue;
						}
                        if (item.Count() > 1)
                            Debug.Print("More than one item!!!");
						rewards.Add(new PetReward(item.First().id));
                    } else {
                        Debug.Print("Missing item {0}", bundle.name);
                    }
                }
            } else {
                if (item.Any()) {
                    if (item.Count() > 1)
                        Debug.Print("More than one item!!!");
					rewards.Add(new PetReward(item.First().id));
                } else {
                    Debug.Print("Unknown pet feed item: {0}", strDescr.name);
                }
            }
        }

        #endregion
    }

	[Serializable]
	[XmlType(AnonymousType = true)]
	public class PetReward
	{
		public PetReward() { }

		public PetReward(int itemId) {
			item = itemId;
			name = Utility.ItemIndex.GetItem(itemId).Description;
		}

		[XmlAttribute]
		public int item;

		[XmlAttribute]
		public long price;

		[XmlAttribute]
		public decimal chance;

		[XmlAttribute]
		public string name;
	}
}
