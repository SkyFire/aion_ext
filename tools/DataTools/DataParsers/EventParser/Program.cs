namespace Jamie.Quests
{
    using System;
    using System.Diagnostics;
    using System.IO;
    using System.Linq;
    using System.Text;
    using System.Xml;
    using Jamie.ParserBase;
    using System.Collections.Generic;
    using System.Xml.Serialization;
    using Jamie.Items;
    using Jamie.Skills;

    class Program
    {
        static string root = AppDomain.CurrentDomain.SetupInformation.ApplicationBase;
        static Dictionary<string, List<Quest>> questsByCategory = new Dictionary<string, List<Quest>>();

        static void Main(string[] args) {
            Utility.WriteExeDetails();
            Console.WriteLine("Loading strings...");
            Utility.LoadStrings(root);

            Console.WriteLine("Loading items...");
            Utility.LoadItems(root);

            Console.WriteLine("Loading NPCs...");
            Utility.LoadClientNpcs(root);

            Console.WriteLine("Loading client trade lists...");
            Utility.LoadNpcGoodLists(root);

            Console.WriteLine("Loading quests...");
            Utility.LoadQuestFile(root);

            Console.WriteLine("Loading NPC dialogs...");
            Utility.LoadHtmlDialogs(root);

			string outputPath = Path.Combine(root, @".\output");
			if (!Directory.Exists(outputPath))
				Directory.CreateDirectory(outputPath);

			//var outputFile = new TradeListFile();

			var settings = new XmlWriterSettings()
			{
				CheckCharacters = false,
				CloseOutput = false,
				Indent = true,
				IndentChars = "\t",
				NewLineChars = "\n",
				Encoding = new UTF8Encoding(false)
			};

            string[] armorMat = { "pl", "ch", "lt", "rb" };
            string[] weapons = { 
                                 "orb", "dagger", "mace", "sword", "staff", "book", "2hsword", "polearm", "bow",
                                 "t_orb", "t_dagger", "t_mace", "t_sword", "t_staff", "t_book", "t_2hsword", "t_polearm", "bow"
                               };

			var disasmItems = Utility.ItemIndex.ItemList.Where(i => i.disassembly_item.HasValue && 
															   i.disassembly_item.Value &&
															   i.Category != ItemCategories.harvest);

			var metals = Utility.ItemIndex.ItemList.Where(i => i.Category == ItemCategories.harvest &&
															   i.Quality == ItemQualities.common &&
															   (i.name.StartsWith("metal") ||
															    i.name.StartsWith("noblemetal")));

			var jewels = Utility.ItemIndex.ItemList.Where(i => i.Category == ItemCategories.harvest &&
															   i.Quality == ItemQualities.rare &&
															   i.quest == 0 &&
															   i.name.StartsWith("jewelry"));

			var enchants = Utility.ItemIndex.ItemList.Where(i => i.package_permitted > 0 &&
															     i.name.StartsWith("matter_enchant"));

			var aethers = Utility.ItemIndex.ItemList.Where(i => i.Category == ItemCategories.harvest &&
																!i.disassembly_item.Value &&
																i.name.StartsWith("od_"));

			var balaurs = Utility.ItemIndex.ItemList.Where(i => i.Quality == ItemQualities.rare &&
																i.name.StartsWith("dr_material"));

            var petals = Utility.ItemIndex.ItemList.Where(i => i.Quality == ItemQualities.rare &&
															   i.name.StartsWith("harvest_dye"));

            var gatherables = Utility.ItemIndex.ItemList.Where(i => i.Category == ItemCategories.harvest &&
                                                                    i.quest == 0);
			// don't know actually
			var accessoir = Utility.ItemIndex.ItemList.Where(i => i.name.StartsWith("ac_head") /*&&
																  i.name.Contains("_p_")*/);
            SkillMap skillMap = new SkillMap();
            skillMap.LoadFromFile(root);

			WrappedItemsFile wrappedItemFile = new WrappedItemsFile();
			List<WrapperItem> wrappers = new List<WrapperItem>();
			foreach (var item in disasmItems) {
				if (item.desc != null) {
					var wrapper = new WrapperItem();
					wrapper.id = item.id;
					wrapper.description = item.Description;
					string[] nameParts = item.name.Split('_');
					string last = nameParts[nameParts.Length - 1];
					if (Char.IsDigit(last[0]) && last.EndsWith("day")) {
						string itemName = String.Join("_", nameParts, 1, nameParts.Length - 1);
						Item wrappedItem = Utility.ItemIndex.GetItem(itemName);
						if (wrappedItem == null) {
							var matched = Utility.ItemIndex.GetItemsByDescription("str_" + itemName);
							if (matched.Count() > 1 || matched.Count() == 0)
								Debug.Print("Wrapped item not found for: {0}", item.name);
							else
								wrappedItem = matched.First();
						}
						if (wrappedItem != null) {
							wrapper.item = new WrappedItem[1];
							var wi = new WrappedItem();
							wi.id = wrappedItem.id;
							wi.min = wi.max = 1;
							wi.description = wrappedItem.Description;
							wrapper.item[0] = wi;
						}
                    } else if (nameParts.Contains("copper")) {
                        wrapper.item = new WrappedItem[1];
                        var wi = new WrappedItem();
                        wi.id = nameParts.Contains("l") ? 186000002 : 186000007;
                        wi.min = wi.max = 1;
                        wrapper.item[0] = wi;
                    } else if (nameParts.Contains("silver")) {
                        wrapper.item = new WrappedItem[1];
                        var wi = new WrappedItem();
                        wi.id = nameParts.Contains("l") ? 186000003 : 186000008;
                        wi.min = wi.max = 1;
                        wrapper.item[0] = wi;
                    } else if (nameParts.Contains("gold")) {
                        wrapper.item = new WrappedItem[1];
                        var wi = new WrappedItem();
                        wi.id = nameParts.Contains("l") ? 186000004 : 186000009;
                        wi.min = wi.max = 1;
                        wrapper.item[0] = wi;
                    } else if (nameParts.Contains("platinum")) {
                        wrapper.item = new WrappedItem[1];
                        var wi = new WrappedItem();
                        wi.id = nameParts.Contains("l") ? 186000005 : 186000010;
                        wi.min = wi.max = 1;
                        wrapper.item[0] = wi;
					} else if (nameParts.Contains("mithril") || 
							   nameParts.Contains("medal") && nameParts.Contains("Quest")) {
                        wrapper.item = new WrappedItem[1];
                        var wi = new WrappedItem();
                        wi.id = nameParts.Contains("l", StringComparer.InvariantCultureIgnoreCase) ? 
								186000018 : 186000019;
                        wi.min = wi.max = 1;
                        wrapper.item[0] = wi;
                    } else if (nameParts.Contains("torso") || nameParts.Contains("pants")) {
                        string itemName = String.Join("_", nameParts, 1, nameParts.Length - 1);
                        List<WrappedItem> armor = new List<WrappedItem>();
                        for (int i = 0; i < 4; i++) {
							string name = armorMat[i] + "_" + itemName;
							Item wrappedItem = Utility.ItemIndex.GetItem(name);
                            if (wrappedItem == null) {
								name = "str_" + armorMat[i] + "_" + itemName;
								var matched = Utility.ItemIndex.GetItemsByDescription(name);
                                if (matched.Count() > 1 || matched.Count() == 0)
									Debug.Print("Wrapped item not found for: {0}", name);
                                else
                                    wrappedItem = matched.First();
                            }
                            if (wrappedItem != null) {
                                var wi = new WrappedItem();
                                wi.id = wrappedItem.id;
                                wi.min = 0;
                                wi.max = 1;
                                wi.description = wrappedItem.Description;
                                armor.Add(wi);
                            }
                        }
                        wrapper.count = 1;
                        wrapper.item = armor.ToArray();
                    } else if (nameParts[0] != "material" && nameParts.Contains("weapon")) {
                        string itemName = String.Join("_", nameParts, 2, nameParts.Length - 2);
                        List<WrappedItem> weaps = new List<WrappedItem>();
                        for (int i = 0; i < 18; i++) {
							string name = weapons[i] + "_" + itemName;
							Item wrappedItem = Utility.ItemIndex.GetItem(name);
                            if (wrappedItem == null) {
								name = "str_" + weapons[i] + "_" + itemName;
								var matched = Utility.ItemIndex.GetItemsByDescription(name);
                                if (matched.Count() > 1 || matched.Count() == 0)
									Debug.Print("Wrapped item not found for: {0}", name);
                                else
                                    wrappedItem = matched.First();
                            }
                            if (wrappedItem != null) {
                                var wi = new WrappedItem();
                                wi.id = wrappedItem.id;
                                wi.min = 0;
                                wi.max = 1;
                                wi.description = wrappedItem.Description;
                                weaps.Add(wi);
                            }
                        }
                        wrapper.item = weaps.ToArray();
                        wrapper.count = 1;
                    } else if (nameParts.Contains("metal")) {
						string itemName = String.Join("_", nameParts, 1, nameParts.Length - 1);
						string levelSuffix = nameParts.Last();
						levelSuffix = levelSuffix.Substring(0, levelSuffix.Length - 1);
						int level = Int32.Parse(levelSuffix) * 10;
						var rewards = metals.Where(m => m.level == level);
						List<WrappedItem> items = new List<WrappedItem>();
						foreach (var metal in rewards) {
							var wi = new WrappedItem();
							wi.id = metal.id;
							wi.min = 1;
							wi.max = 5;
							wi.description = metal.Description;
							items.Add(wi);
						}
						wrapper.count = 1;
						wrapper.item = items.ToArray();
					} else if (nameParts.Contains("jewelry")) {
						string itemName = String.Join("_", nameParts, 1, nameParts.Length - 1);
						string levelSuffix = nameParts.Last();
						levelSuffix = levelSuffix.Substring(0, levelSuffix.Length - 1);
						int level = Int32.Parse(levelSuffix) * 10;
						var rewards = jewels.Where(m => m.level == level);
						List<WrappedItem> items = new List<WrappedItem>();
						foreach (var jewel in rewards) {
							var wi = new WrappedItem();
							wi.id = jewel.id;
							wi.min = 1;
							wi.max = 5;
							wi.description = jewel.Description;
							items.Add(wi);
						}
						wrapper.count = 1;
						wrapper.item = items.ToArray();
					} else if (nameParts.Contains("od") || nameParts.Contains("od02") ||
							   nameParts.Contains("od03") || nameParts.Contains("od04")) {
						string itemName = String.Join("_", nameParts, 1, nameParts.Length - 1);
						string levelSuffix = nameParts.Last();
						levelSuffix = levelSuffix.Substring(0, levelSuffix.Length - 1);
						int level = Int32.Parse(levelSuffix) * 10;
						var rewards = aethers.Where(m => m.level == level);
						List<WrappedItem> items = new List<WrappedItem>();
						foreach (var aether in rewards) {
							var wi = new WrappedItem();
							wi.id = aether.id;
							wi.min = 1;
							wi.max = 5;
							wi.description = aether.Description;
							items.Add(wi);
						}
						wrapper.count = 1;
						wrapper.item = items.ToArray();
					} else if (nameParts.Contains("dr") && (nameParts.Contains("material") ||
							   nameParts.Contains("material02") || nameParts.Contains("material03") ||
							   nameParts.Contains("material04"))) {
						string itemName = String.Join("_", nameParts, 1, nameParts.Length - 1);
						string levelSuffix = nameParts.Last();
						levelSuffix = levelSuffix.Substring(0, levelSuffix.Length - 1);
						int level = Int32.Parse(levelSuffix) * 10 + 20;
						var rewards = balaurs.Where(m => m.level == level);
						List<WrappedItem> items = new List<WrappedItem>();
						foreach (var material in rewards) {
							var wi = new WrappedItem();
							wi.id = material.id;
							wi.min = 1;
							wi.max = 5;
							wi.description = material.Description;
							items.Add(wi);
						}
						wrapper.count = 1;
						wrapper.item = items.ToArray();
					} else if (nameParts.Contains("enchant") || nameParts.Contains("enchant02") ||
							   nameParts.Contains("enchant03") || nameParts.Contains("enchant04") ||
							   nameParts.Contains("enchant05")) {
						string itemName = String.Join("_", nameParts, 1, nameParts.Length - 1);
						string levelSuffix = nameParts.Last();
						if (Char.IsLetter(levelSuffix.ToCharArray().Last()))
							levelSuffix = levelSuffix.Substring(0, levelSuffix.Length - 1);
						int level = Int32.Parse(levelSuffix) - 1;
						var rewards = enchants.Skip(level * 10).Take(10);
						List<WrappedItem> items = new List<WrappedItem>();
						foreach (var enchant in rewards) {
							var wi = new WrappedItem();
							wi.id = enchant.id;
							wi.min = 1;
							wi.max = 5;
							wi.description = enchant.Description;
							items.Add(wi);
						}
						wrapper.count = 1;
						wrapper.item = items.ToArray();
					} else if (nameParts.Contains("dye")) {
						string itemName = String.Join("_", nameParts, 1, nameParts.Length - 1);
						string levelSuffix = nameParts.Last();
						if (Char.IsLetter(levelSuffix.ToCharArray().Last()))
							levelSuffix = levelSuffix.Substring(0, levelSuffix.Length - 1);
                        int level = Int32.Parse(levelSuffix);
						List<WrappedItem> items = new List<WrappedItem>();
                        foreach (var dye in petals) {
							var wi = new WrappedItem();
							wi.id = dye.id;
                            wi.min = level;
                            wi.max = 5;
							wi.description = dye.Description;
							items.Add(wi);
						}
						wrapper.count = 1;
						wrapper.item = items.ToArray();
					} else if (nameParts.Contains("head")) {
						string itemName = String.Join("_", nameParts, 1, nameParts.Length - 1);
						string levelSuffix = nameParts.Last();
						if (Char.IsLetter(levelSuffix.ToCharArray().Last()))
							levelSuffix = levelSuffix.Substring(0, levelSuffix.Length - 1);
						int level = Int32.Parse(levelSuffix) * 10 + 10;
						// don't know...
					} else if (nameParts.Contains("medal")) {
						int start = 1;
						if (nameParts.Contains("d"))
							start++;
						string itemName = String.Join("_", nameParts, start, nameParts.Length - 1 - start);
						Item wrappedItem = Utility.ItemIndex.GetItem(itemName);
						if (wrappedItem == null) {
							Debug.Print("Wrapped item not found for: {0}", item.name);
						} else {
							wrapper.item = new WrappedItem[1];
							var wi = new WrappedItem();
							wi.id = wrappedItem.id;
							wi.min = 1;
							wi.max = 5;
							wi.description = wrappedItem.Description;
							wrapper.item[0] = wi;
						}
                    } else if (nameParts[0] == "material") {
                        ItemQualities q = ItemQualities.junk;
                        try {
                            q = (ItemQualities)Enum.Parse(typeof(ItemQualities), nameParts[1], true);
                        } catch { 
                        }
                        if (q != ItemQualities.junk) {
                            ItemRace raceExlude = nameParts.Contains("d") ? ItemRace.ELYOS : ItemRace.ASMODIANS;
                            var g = gatherables.Where(i => i.Quality == q && i.quest == 0);
                            List<WrappedItem> items = new List<WrappedItem>();
                            foreach (var gatherable in g) {
                                if (gatherable.race == raceExlude || gatherable.name.StartsWith("od") ||
                                    gatherable.id >= 152002001 && gatherable.id <= 152002003)
                                    continue;
                                var wi = new WrappedItem();
                                wi.id = gatherable.id;
                                wi.min = 10;
                                wi.max = 10;
                                wi.description = gatherable.Description;
                                items.Add(wi);
                            }
                            wrapper.count = 10;
                            wrapper.item = items.ToArray();
                        } else if (nameParts[1] == "flower") {
                            List<WrappedItem> items = new List<WrappedItem>();
                            foreach (var dye in petals) {
                                var wi = new WrappedItem();
                                wi.id = dye.id;
                                wi.max = 1;
                                wi.description = dye.Description;
                                items.Add(wi);
                            }
                            wrapper.count = 1;
                            wrapper.item = items.ToArray();
                        } else if (nameParts[1] == "weapon") {
                            ItemRace raceExlude = nameParts.Contains("d") ? ItemRace.ELYOS : ItemRace.ASMODIANS;
                            var fabledW = Utility.ItemIndex.ItemList.Where(i => 
                                i.level >= 50 &&
                                i.WeaponType != WeaponTypes.None &&
                                i.WeaponType != WeaponTypes.NoWeapon &&
                                i.quest == 0);
                            var fabledWH = from w in fabledW
                                           let parts = w.name.Split('_')
                                           let suffix = parts[parts.Length - 2]
                                           where suffix == "h"
                                           select w;
                            List<WrappedItem> items = new List<WrappedItem>();
                            foreach (var weap in fabledWH) {
                                if (weap.race == raceExlude)
                                    continue;
                                var wi = new WrappedItem();
                                wi.id = weap.id;
                                wi.max = 1;
                                wi.description = weap.Description;
                                items.Add(wi);
                            }
                            wrapper.count = 1;
                            wrapper.item = items.ToArray();
                        }
                    }
					wrappers.Add(wrapper);
				}
			}
			wrappedItemFile.wrapper_item = wrappers.ToArray();

			try {
				using (var fs = new FileStream(Path.Combine(outputPath, "wrapped_items.xml"),
											   FileMode.Create, FileAccess.Write))
				using (var writer = XmlWriter.Create(fs, settings)) {
					XmlSerializer ser = new XmlSerializer(typeof(WrappedItemsFile));
					ser.Serialize(writer, wrappedItemFile);
				}
			} catch (Exception ex) {
				Debug.Print(ex.ToString());
			}

            // var eventQuests = Utility.QuestIndex.QuestList.Where(q => q.category1 == QuestCategory1.Event);
            var expUtil = Utility<Quest>.Instance;

            foreach (var q in Utility.QuestIndex.QuestList) {
                string eventName = String.Empty;
                if (q.dev_name.StartsWith("[이벤트]일본대만_1주년")) {
                    eventName = ItemTag.taiwan.ToString();
                } else if (q.dev_name.StartsWith("[이벤트]할로윈")) {
                    eventName = "halloween";
                } else if (q.dev_name.StartsWith("[이벤트]구정") || q.dev_name.StartsWith("[이벤트]신년")) {
                    eventName = "NewYear";
                } else if (q.dev_name.StartsWith("제로코크")) {
                    eventName = "china";
                }


                var rewards = new List<Rewards>();
                expUtil.Export(q, "reward_item", rewards);
                expUtil.Export(q, "selectable_reward_item", rewards);
                expUtil.Export(q, "reward_item_ext_", rewards);
                expUtil.Export(q, "selectable_reward_item_ext_", rewards);
                rewards = rewards.Where(r => r.BasicRewards != null || r.SelectableRewards != null).ToList();

                QuestOur qOur = new QuestOur();
                qOur.Rewards = rewards;
                qOur.AddClassRewards(q);
                List<QuestItemsOur> allRewards = qOur.AllRewards;
                q.OurQuest = qOur;

                if (!String.IsNullOrEmpty(eventName)) {
                    AddQuest(eventName, q);
                    continue;
                }

                string questName = q.Description.body;
                if (questName.StartsWith("[")) {
                    int pos = questName.IndexOf(']');
                    questName = questName.Substring(1, pos - 1);
                    if (questName.Contains("Daily")) {
                        AddQuest("daily", q);
                        continue;
                    }
                }

                if (allRewards.Count > 0) {
                    foreach (var br in allRewards) {
                        Item item = Utility.ItemIndex.ItemList.Where(i => i.id == br.item_id).First();
                        string[] nameParts = item.name.Split('_');
                        if (nameParts[0] == "world" && nameParts[1] == "wrap" && nameParts[2] == "event" ||
                            nameParts[0] == "world" && nameParts[1] == "event") {
                            eventName = nameParts[3];
                        }
                        break;
                    }
                } else {
                    if (q.category2 == "STR_FACTION_GuardianOfDivine" ||
                        q.category2 == "STR_FACTION_GuardianOfTower" ||
                        q.category2 == "STR_FACTION_Army_Li" ||
                        q.category2 == "STR_FACTION_BountyHunter_Li" ||
                        q.category2 == "STR_FACTION_Army_Da" ||
                        q.category2 == "STR_FACTION_BountyHunter_Da") {
                        eventName = "guild_order";
                    } else if (q.dev_name.StartsWith("7월 프로모션 퀘스트")) {
                        eventName = ItemTag.july_promotion.ToString();
                    } else if (q.dev_name.StartsWith("[이벤트]해외용 임시 등록")) {
                        eventName = "Temporary";
                    }
                }

                if (!String.IsNullOrEmpty(eventName)) {
                    AddQuest(eventName, q);
                    continue;
                } else {
                    Debug.Print("Unknown event category: {0}", q.category2);
                }
            }

            EventsFile outputFile = new EventsFile();
            outputFile.events = new List<EventData>();

            ILookup<string, Item> worldEventsByName;
            ILookup<string, Item> worldCashByName;
            ILookup<string, Item> worldGiftsByName;

            ILookup<string, Item> eventsByName;
            ILookup<string, Item> cashByName;
            ILookup<string, Item> giftsByName;

            var worldItems = Utility.ItemIndex.ItemList.Where(i => i.name.StartsWith("world_"));
            var worldEvents = worldItems.Where(i => i.name.StartsWith("world_event_"));
            worldEventsByName = (from it in worldEvents
                                 let names = it.name.Split(new char[] { '_' })
                                 let subset = names.Where(n => n.Length > 3)
                                 from name in subset
                                 select new { Name = name, Item = it })
                                .ToLookup(a => a.Name, a => a.Item, StringComparer.InvariantCultureIgnoreCase);
            var worldCash = worldItems.Where(i => i.name.StartsWith("world_cash_"));
            worldCashByName = (from it in worldCash
                               let names = it.name.Split(new char[] { '_' })
                               let subset = names.Where(n => n.Length > 3)
                               from name in subset
                               select new { Name = name, Item = it })
                               .ToLookup(a => a.Name, a => a.Item, StringComparer.InvariantCultureIgnoreCase);
            var worldGifts = worldItems.Where(i => i.name.StartsWith("world_wrap_"));
            worldGiftsByName = (from it in worldGifts
                                let names = it.name.Split(new char[] { '_' })
                                let subset = names.Where(n => n.Length > 3)
                                from name in subset
                                select new { Name = name, Item = it })
                                .ToLookup(a => a.Name, a => a.Item, StringComparer.InvariantCultureIgnoreCase);

            var westItems = Utility.ItemIndex.ItemList.Where(i => i.name.StartsWith("west_"));

            var chinaItems = Utility.ItemIndex.ItemList.Where(i => i.name.StartsWith("china_"));

            var eventItems = Utility.ItemIndex.ItemList.Where(i => i.name.StartsWith("event_"));
            eventsByName = (from it in eventItems
                            let names = it.name.Split(new char[] { '_' })
                            let subset = names.Where(n => n.Length > 3)
                            from name in subset
                            select new { Name = name, Item = it })
                            .ToLookup(a => a.Name, a => a.Item, StringComparer.InvariantCultureIgnoreCase);
            var eventCash = Utility.ItemIndex.ItemList.Where(i => i.name.StartsWith("cash_"));
            cashByName = (from it in eventCash
                          let names = it.name.Split(new char[] { '_' })
                          let subset = names.Where(n => n.Length > 3)
                          from name in subset
                          select new { Name = name, Item = it })
                          .ToLookup(a => a.Name, a => a.Item, StringComparer.InvariantCultureIgnoreCase);
            var eventGifts = Utility.ItemIndex.ItemList.Where(i => i.name.StartsWith("wrap_event_"));
            giftsByName = (from it in eventItems
                           let names = it.name.Split(new char[] { '_' })
                           let subset = names.Where(n => n.Length > 3)
                           from name in subset
                           select new { Name = name, Item = it })
                           .ToLookup(a => a.Name, a => a.Item, StringComparer.InvariantCultureIgnoreCase);

            var dailyGifts = Utility.ItemIndex.ItemList.Where(i => i.name.StartsWith("wrap_daily_") ||
                                                                   i.name.StartsWith("wrap_d_daily"));

            var combined = worldEvents.Union(worldCash).Union(worldGifts).Union(westItems).Union(chinaItems)
                                      .Union(eventItems).Union(eventCash).Union(eventGifts).Union(dailyGifts);
            var dailyItems = Utility.ItemIndex.ItemList.Where(i => i.tag == ItemTag.daily);
            var missing = dailyItems.Except(combined);

            questsByCategory.Add("deva", new List<Quest>());
            // questsByCategory.Add("devaday", new List<Quest>());
            questsByCategory.Add("foolsday", new List<Quest>());
            questsByCategory.Add("goods", new List<Quest>());
            questsByCategory.Add("july_pcbang", new List<Quest>());
            questsByCategory.Add("kaspa", new List<Quest>());
            questsByCategory.Add("petcard", new List<Quest>());
            questsByCategory.Add("santa", new List<Quest>());
            questsByCategory.Add("repeatquest", new List<Quest>());
            questsByCategory.Add("season1", new List<Quest>());
            questsByCategory.Add("v19promotion", new List<Quest>());
            questsByCategory.Add("worldcup", new List<Quest>());
            questsByCategory.Add("wondergirls", new List<Quest>());
            questsByCategory.Add("west", new List<Quest>());
            questsByCategory.Add("fanta", new List<Quest>());
            questsByCategory.Add("shapechange", new List<Quest>());
            questsByCategory.Add("social", new List<Quest>());
            questsByCategory.Add("changeskin", new List<Quest>());
            questsByCategory.Add("highdress", new List<Quest>());
            questsByCategory.Add("weddingdress", new List<Quest>());
			questsByCategory.Add("wing", new List<Quest>());

            foreach (string category in questsByCategory.Keys) {
                var quests = questsByCategory[category];
                var eventData = new EventData();
                eventData.name = category;
                eventData.quests = new List<EventQuest>();
                Dictionary<int, Item> categoryItems = null;
                if (category == "china") {
                    categoryItems = chinaItems.ToDictionary(i => i.id, i => i);
                } else if (category == "daily") {
                    categoryItems = dailyItems.ToDictionary(i => i.id, i => i);
                } else if (category == "fanta" || category == "shapechange" || category == "santa" ||
                           category == "social" || category == "changeskin" || category == "highdress" ||
                           category == "weddingdress" || category == "wing") {
                    categoryItems = new Dictionary<int, Item>();
                    if (eventsByName.Contains(category)) {
                        foreach (var item in eventsByName[category])
                            categoryItems.Add(item.id, item);
                    }
                    if (cashByName.Contains(category)) {
                        foreach (var item in cashByName[category]) {
                            if (!categoryItems.ContainsKey(item.id))
                                categoryItems.Add(item.id, item);
                        }
                    }
                    if (giftsByName.Contains(category)) {
                        foreach (var item in giftsByName[category]) {
                            if (!categoryItems.ContainsKey(item.id))
                                categoryItems.Add(item.id, item);
                        }
                    }
                    if (category == "fanta")
                        eventData.name = "fanta_japen";
                    else if (category == "santa")
                        eventData.name = "xmas";
                    else if (category == "social")
                        eventData.name = "performance";
                } else if (category == "west") {
                    categoryItems = westItems.ToDictionary(i => i.id, i => i);
                } else {
                    categoryItems = Utility.ItemIndex.ItemList.Where(i => i.tag.ToString() == category)
                                                              .ToDictionary(i => i.id, i => i);
                    if (worldEventsByName.Contains(category)) {
                        foreach (var item in worldEventsByName[category])
                            categoryItems.Add(item.id, item);
                    }
                    if (worldCashByName.Contains(category)) {
                        foreach (var item in worldCashByName[category])
                            categoryItems.Add(item.id, item);
                    }
                    if (worldGiftsByName.Contains(category)) {
                        foreach (var item in worldGiftsByName[category])
                            categoryItems.Add(item.id, item);
                    }
                }
                foreach (var q in quests) {
                    var eventQuest = new EventQuest();
                    eventQuest.id = q.id;
                    eventQuest.name = q.Description.body.Trim();
                    eventData.quests.Add(eventQuest);
                    var rewardIds = q.OurQuest.AllRewards.Select(r => r.item_id);
                    if (categoryItems != null) {
                        foreach (int rewardId in rewardIds)
                            categoryItems.Remove(rewardId);
                    }
                }
                if (categoryItems != null) {
                    eventData.wrapItems = new List<EventItem>();
                    eventData.rewardItems = new List<EventItem>();
                    eventData.cash = new List<EventItem>();
                    HashSet<int> cashIds = new HashSet<int>();

                    foreach (var pair in categoryItems) {
                        var eventItem = new EventItem();
                        Item origItem = Utility.ItemIndex.GetItem(pair.Key);
                        eventItem.name = Utility.StringIndex.GetString(origItem.desc).Trim();
                        eventItem.item_id = pair.Key;
                        eventItem.race = origItem.race;
                        if (origItem.extra_currency_item_count > 0) {
                            int cashId = Utility.ItemIndex.GetItem(origItem.extra_currency_item).id;
                            cashIds.Add(cashId);
                        }
                        if (origItem.cash_item == 1 || origItem.name.StartsWith("wrap_")) {
                            eventItem.comment = Utility.StringIndex.GetString(origItem.desc_long);
                            eventData.wrapItems.Add(eventItem);
                        } else {
                            eventData.rewardItems.Add(eventItem);
                        }
                    }
                    if (eventData.rewardItems.Count == 0)
                        eventData.rewardItems = null;
                    else if (cashIds.Count > 0) {
                        foreach (var ci in cashIds) {
                            var cashItem = eventData.rewardItems.Where(r => r.item_id == ci).FirstOrDefault();
                            if (cashItem != null) {
                                eventData.rewardItems.Remove(cashItem);
                            } else {
                                cashItem = new EventItem();
                                cashItem.item_id = ci;
                                var origItem = Utility.ItemIndex.ItemList.Where(i => i.id == ci).FirstOrDefault();
                                cashItem.name = Utility.StringIndex.GetString(origItem.desc).Trim();
                                cashItem.race = origItem.race;
                            }
                            eventData.cash.Add(cashItem);
                        }
                    }
                    if (eventData.wrapItems.Count == 0)
                        eventData.wrapItems = null;
                }
                outputFile.events.Add(eventData);
            }

            //var merchants = Utility.ClientNpcIndex.NpcList.Where(n => n.cursor_type == CursorType.trade ||
            //                                                          n.disk_type == DiskType.merchant ||
            //                                                          n.TradeInfo != null ||
            //                                                          n.AbyssTradeInfo != null ||
            //                                                          n.CouponTradeInfo != null ||
            //                                                          n.ExtraCurrencyTradeInfo != null);
            //foreach (var merchant in merchants) {
            //    var dialogs = Utility.DialogFiles.Where(p => p.Key.StartsWith(merchant.name.ToLower() + '|'));
            //    if (dialogs.Any()) {
            //        foreach (var dialog in dialogs) {
            //            var pg = dialog.Value.HtmlPages.Where(p => p.npcfuncs != null).FirstOrDefault();
            //            if (pg != null) {
            //                var sellbuy = pg.npcfuncs.Where(f => f.GetType().Equals(typeof(trade_buy)) ||
            //                                                     f.GetType().Equals(typeof(trade_sell)) ||
            //                                                     f.GetType().Equals(typeof(exchange_coin)) ||
            //                                                     f.GetType().Equals(typeof(Pet_adopt)));
            //                if (sellbuy.Any()) {
            //                    break;
            //                }
            //            }
            //        }
            //    }
            //}

            //Utility.OriginalTradeList.TradeLists =
            //    Utility.OriginalTradeList.TradeLists.OrderBy(t => t.npc_id).ToList();

            try {
                using (var fs = new FileStream(Path.Combine(outputPath, "events.xml"),
                                               FileMode.Create, FileAccess.Write))
                using (var writer = XmlWriter.Create(fs, settings)) {
                    XmlSerializer ser = new XmlSerializer(typeof(EventsFile));
                    ser.Serialize(writer, outputFile);
                }
            } catch (Exception ex) {
                Debug.Print(ex.ToString());
            }
        }

        static void AddQuest(string eventCategory, Quest quest) {
            if (!String.IsNullOrEmpty(eventCategory)) {
                if (questsByCategory.ContainsKey(eventCategory)) {
                    questsByCategory[eventCategory].Add(quest);
                } else {
                    questsByCategory.Add(eventCategory, new List<Quest>() { quest });
                }
            }
        }
    }
}
