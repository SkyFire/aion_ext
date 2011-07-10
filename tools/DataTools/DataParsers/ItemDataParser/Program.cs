namespace Jamie.Items
{
    using System;
    using System.Collections.Generic;
    using System.Collections.Specialized;
    using System.Diagnostics;
    using System.Drawing;
    using System.Globalization;
    using System.IO;
    using System.Linq;
    using System.Reflection;
    using System.Text;
    using System.Threading;
    using System.Xml;
    using System.Xml.Serialization;
    using Jamie.ParserBase;
    using Jamie.Skills;
    using Jamie.Drops;
    using Jamie.Pets;
    using System.Collections;

    class Program
    {
        static string[] armors = { "D3_LYCAN", "D3_KRALL", "D3_LIZARDMAN", "SKELETON", 
								   "A_GRAVEKNIGHT", "DEATHKNIGHT", "UNDEADLIZARD" };
        static string[] balaurscales = { "D2_DRAGON", "D3_NAGA" };
        static string[] thorns = { "ROTTENTREE", "A_ROTTENTREE", "MUDTHORN", "XIPETO", "FELLIAL", 
                                   "FUNGY_DR", "VAMPIRE_D", "MIMIC3" };
        static string[] fluids = { "FUNGY", "WORM", "SLIME", "MILLIPEDE", "WATERWORM", "SHELUK", "SALIVADO",
								   "FOAM", "MIMIC2", "A_WATERWORM" };
        static string[] bones = { "MERDION", "LEPISMA", "BRAX", "FRILLFAIMAM", "DIREBEAST", "LOBSTER",
								  "MANDURIB", "DRYNAC", "CRESTLICH", "BASILISK", "GRAVEKNIGHT", "ZOMBIE",
								  "BAKU", "BONEDRAKE", "DRITON", "TROLL", "TRICO", "TROLLKIN", "LICH",
								  "TESINONWATCHER", "A_DRAKE" };
        static string[] souls = { "UNDEADLIGHT", "A_SKELETON", "MAIDENGOLEM", "GOLEM", "A_MAIDENGOLEM",
								  "A_UNDEADLIGHT", "A_UNDEADDARK", "A_SKELETONMAGICIAN", "A_SHADOWSTALKER",
								  "MAMMOTHGHOST", "LYCANGHOST", "FLYINGRAY", "GHOSTELIM", "BOOKIE_CRK",
								  "NEPILIM", "UNDEADDRAKAN", "FLYINGWORM", "A_SOULEDSTONE", "SOULEDSTONE",
                                  "SOULEDSTONE02" };
        static int[] healthyfood = { 182006413, 182006414, 182006415, 182006416, 182006417, 182006418 };
        static int[] biscuits = { 182006375, 182006376, 182006377 };
        static int[] testfeed = { 182006363, 182006364 };

        static List<int> excludeFeeds = new List<int>() {
			// poroco junk rewards
			182006419, 182006420,
			// broken armor/weapon junk rewards
			182006378, 182006379, 182006380, 182006381, 182006382, 182006383, 182006384,
			182006385, 182006386, 182006387, 182006388, 182006389, 182006390, 182006391, 182006392
		};

        static Dictionary<int, List<ItemFeed>> feedItems = new Dictionary<int, List<ItemFeed>>();

        static readonly string root = AppDomain.CurrentDomain.SetupInformation.ApplicationBase;
        static ItemsExportFile itemGroups;
        static TaskExport taskExport;

        static void Main(string[] args) {
            Utility.WriteExeDetails();

            InvBonuses bonuses = new InvBonuses();
            bonuses.BonusItems = new List<BonusData>();
            var utility = Utility<Quest>.Instance;

            Console.WriteLine("Loading strings...");
            Utility.LoadStrings(root);
            Console.WriteLine("Loading items...");
            Utility.LoadItems(root);

            var test = Utility.ItemIndex.ItemList.Where(i => i.name.IndexOf("_head_") != -1 &&
                                                             i.Description != null /*&&
															 i.Description.IndexOf("hairpin", StringComparison.InvariantCultureIgnoreCase) != -1*/
                                                                                                                                                  );
            StringBuilder heads = new StringBuilder();

            foreach (var hh in test) {
                string trimmed = hh.name.Remove(hh.name.Length - 4, 4);
                if (trimmed.EndsWith("_a_r2") || trimmed.EndsWith("_a_l2"))
                    heads.AppendLine(String.Format("{0}\t{1}\t{2}", hh.name, hh.Description, hh.Quality));
            }

            CultureInfo ci = new CultureInfo("");
            int lvl = 0;
            var junks = Utility.ItemIndex.ItemList.Where(i => i.Quality == ItemQualities.junk && i.quest == 0);

            var lvlJunks = from it in junks
                           let under = it.desc.LastIndexOf('_')
                           let hasNumber = under == -1 ? false : Int32.TryParse(it.desc.Substring(under + 1), out lvl)
                           where hasNumber && it.desc.StartsWith("STR_JUNK_")
                           select new { Item = it, Level = lvl, Name = it.desc.Substring(9, under - 9) };

            Console.WriteLine("Loading pet feeds...");
            Utility.LoadClientPetFeed(root);

            var expFeed = Utility<ToypetFeed>.Instance;
            expFeed.AddGetter("favorite_flavor_id");
            expFeed.AddGetter("favorite_flavor_desc");

            var ourFeed = new PetFeed();
            string[] groupNames = Enum.GetNames(typeof(FoodType));
            ourFeed.ItemGroups = groupNames.Select(n => new ItemGroup()
            {
                type = ((FoodType)Enum.Parse(typeof(FoodType), n))
            }).Where(g => g.type != FoodType.ALL && g.type != FoodType.NONE && g.type != FoodType.DOPING)
              .ToArray();

            foreach (ItemGroup group in ourFeed.ItemGroups) {
                if (group.type == FoodType.ARMOR) {
                    var ncArmors = lvlJunks.Where(i => armors.Contains(i.Name));
                    group.itemids = String.Join(",", ncArmors.Select(i => i.Item.id.ToString()).ToArray());
                    foreach (var armor in ncArmors) {
                        List<ItemFeed> feeds;
                        var feedItem = new ItemFeed() { type = group.type, level = armor.Level };
                        if (feedItems.ContainsKey(armor.Item.id))
                            feeds = feedItems[armor.Item.id];
                        else {
                            feeds = new List<ItemFeed>();
                            feedItems.Add(armor.Item.id, feeds);
                        }
                        feeds.Add(feedItem);
                    }
                } else if (group.type == FoodType.BALAUR) {
                    var ncScales = lvlJunks.Where(i => balaurscales.Contains(i.Name));
                    group.itemids = String.Join(",", ncScales.Select(i => i.Item.id.ToString()).ToArray());
                    foreach (var scale in ncScales) {
                        List<ItemFeed> feeds;
                        var feedItem = new ItemFeed() { type = group.type, level = scale.Level };
                        if (feedItems.ContainsKey(scale.Item.id))
                            feeds = feedItems[scale.Item.id];
                        else {
                            feeds = new List<ItemFeed>();
                            feedItems.Add(scale.Item.id, feeds);
                        }
                        feeds.Add(feedItem);
                    }
                } else if (group.type == FoodType.BISCUIT) {
                    group.itemids = String.Join(",", biscuits.Select(i => i.ToString()).ToArray());
                    foreach (var bis in biscuits) {
                        List<ItemFeed> feeds;
                        var feedItem = new ItemFeed() { type = group.type };
                        if (feedItems.ContainsKey(bis))
                            feeds = feedItems[bis];
                        else {
                            feeds = new List<ItemFeed>();
                            feedItems.Add(bis, feeds);
                        }
                        feeds.Add(feedItem);
                    }
                } else if (group.type == FoodType.BONE) {
                    var ncBones = lvlJunks.Where(i => bones.Contains(i.Name));
                    group.itemids = String.Join(",", ncBones.Select(i => i.Item.id.ToString()).ToArray());
                    foreach (var bone in ncBones) {
                        List<ItemFeed> feeds;
                        var feedItem = new ItemFeed() { type = group.type, level = bone.Level };
                        if (feedItems.ContainsKey(bone.Item.id))
                            feeds = feedItems[bone.Item.id];
                        else {
                            feeds = new List<ItemFeed>();
                            feedItems.Add(bone.Item.id, feeds);
                        }
                        feeds.Add(feedItem);
                    }
                } else if (group.type == FoodType.CRYSTAL) {
                    group.itemids = "182006376";
                    List<ItemFeed> feeds;
                    var feedItem = new ItemFeed() { type = group.type };
                    if (feedItems.ContainsKey(182006376))
                        feeds = feedItems[182006376];
                    else {
                        feeds = new List<ItemFeed>();
                        feedItems.Add(182006376, feeds);
                    }
                    feeds.Add(feedItem);
                } else if (group.type == FoodType.FLUID) {
                    var ncFluids = lvlJunks.Where(i => fluids.Contains(i.Name));
                    group.itemids = String.Join(",", ncFluids.Select(i => i.Item.id.ToString()).ToArray());
                    foreach (var fluid in ncFluids) {
                        List<ItemFeed> feeds;
                        var feedItem = new ItemFeed() { type = group.type, level = fluid.Level };
                        if (feedItems.ContainsKey(fluid.Item.id))
                            feeds = feedItems[fluid.Item.id];
                        else {
                            feeds = new List<ItemFeed>();
                            feedItems.Add(fluid.Item.id, feeds);
                        }
                        feeds.Add(feedItem);
                    }
                } else if (group.type == FoodType.GEM) {
                    group.itemids = "182006377";
                    List<ItemFeed> feeds;
                    var feedItem = new ItemFeed() { type = group.type };
                    if (feedItems.ContainsKey(182006377))
                        feeds = feedItems[182006377];
                    else {
                        feeds = new List<ItemFeed>();
                        feedItems.Add(182006377, feeds);
                    }
                    feeds.Add(feedItem);
                } else if (group.type == FoodType.HEALTHY1 || group.type == FoodType.HEALTHY2) {
                    group.itemids = String.Join(",", healthyfood.Select(i => i.ToString()).ToArray());
                    foreach (var hf in healthyfood) {
                        List<ItemFeed> feeds;
                        var feedItem = new ItemFeed() { type = group.type };
                        if (feedItems.ContainsKey(hf))
                            feeds = feedItems[hf];
                        else {
                            feeds = new List<ItemFeed>();
                            feedItems.Add(hf, feeds);
                        }
                        feeds.Add(feedItem);
                    }
                } else if (group.type == FoodType.POWDER) {
                    group.itemids = "182006375";
                    List<ItemFeed> feeds;
                    var feedItem = new ItemFeed() { type = group.type };
                    if (feedItems.ContainsKey(182006375))
                        feeds = feedItems[182006375];
                    else {
                        feeds = new List<ItemFeed>();
                        feedItems.Add(182006375, feeds);
                    }
                    feeds.Add(feedItem);
                } else if (group.type == FoodType.SOUL) {
                    var ncSouls = lvlJunks.Where(i => souls.Contains(i.Name));
                    group.itemids = String.Join(",", ncSouls.Select(i => i.Item.id.ToString()).ToArray());
                    foreach (var soul in ncSouls) {
                        List<ItemFeed> feeds;
                        var feedItem = new ItemFeed() { type = group.type, level = soul.Level };
                        if (feedItems.ContainsKey(soul.Item.id))
                            feeds = feedItems[soul.Item.id];
                        else {
                            feeds = new List<ItemFeed>();
                            feedItems.Add(soul.Item.id, feeds);
                        }
                        feeds.Add(feedItem);
                    }
                } else if (group.type == FoodType.THORN) {
                    var ncThorns = lvlJunks.Where(i => souls.Contains(i.Name));
                    group.itemids = String.Join(",", ncThorns.Select(i => i.Item.id.ToString()).ToArray());
                    foreach (var thorn in ncThorns) {
                        List<ItemFeed> feeds;
                        var feedItem = new ItemFeed() { type = group.type, level = thorn.Level };
                        if (feedItems.ContainsKey(thorn.Item.id))
                            feeds = feedItems[thorn.Item.id];
                        else {
                            feeds = new List<ItemFeed>();
                            feedItems.Add(thorn.Item.id, feeds);
                        }
                        feeds.Add(feedItem);
                    }
                } else if (group.type == FoodType.MISC) {
                    group.itemids = "MISC";
                } else if (group.type == FoodType.MISC1) {
                    group.itemids = "182006362";
                    List<ItemFeed> feeds;
                    var feedItem = new ItemFeed() { type = group.type };
                    if (feedItems.ContainsKey(182006362))
                        feeds = feedItems[182006362];
                    else {
                        feeds = new List<ItemFeed>();
                        feedItems.Add(182006362, feeds);
                    }
                    feeds.Add(feedItem);
                } else if (group.type == FoodType.MISC2) {
                    group.itemids = String.Join(",", testfeed.Select(i => i.ToString()).ToArray());
                    foreach (var t in testfeed) {
                        List<ItemFeed> feeds;
                        var feedItem = new ItemFeed() { type = group.type };
                        if (feedItems.ContainsKey(t))
                            feeds = feedItems[t];
                        else {
                            feeds = new List<ItemFeed>();
                            feedItems.Add(t, feeds);
                        }
                        feeds.Add(feedItem);
                    }
                }
            }

            Dictionary<FoodType, OrderedDictionary> priceLevelStats = new Dictionary<FoodType, OrderedDictionary>();
            foreach (var gr in ourFeed.ItemGroups) {
                if (gr.itemids == null)
                    continue;
                OrderedDictionary levelStats = null;
                if (!priceLevelStats.ContainsKey(gr.type)) {
                    levelStats = new OrderedDictionary();
                    priceLevelStats.Add(gr.type, levelStats);
                } else {
                    levelStats = priceLevelStats[gr.type];
                }
                List<int> itemIds = new List<int>();
                if (gr.itemids == "MISC") {
                    itemIds.AddRange(Utility.ItemIndex.ItemList.Where(i => i.quest == 0 && i.Quality == ItemQualities.junk)
                                                               .Select(i => i.id));
                } else {
                    itemIds.AddRange(gr.itemids.Split(',').Select(i => Int32.Parse(i)));
                }
                foreach (int id in itemIds) {
                    Item item = Utility.ItemIndex.GetItem(id);
                    MinMaxMean stats = null;
                    if (!levelStats.Contains(item.level)) {
                        stats = new MinMaxMean();
                        levelStats.Add(item.level, stats);
                    } else {
                        stats = (MinMaxMean)levelStats[(object)item.level];
                    }
                    stats.AddValue(item.price, item);
                }
            }

            StringBuilder print = new StringBuilder();
            foreach (var pair in priceLevelStats) {
                print.AppendLine(String.Format("Food type {0}", pair.Key.ToString()));
                foreach (DictionaryEntry entry in pair.Value) {
                    MinMaxMean stat = (MinMaxMean)entry.Value;
                    print.AppendLine(String.Format("\tLevel {0}: Min={1} ({2}), Max={3} ({4}), Mean={5}", entry.Key,
                        stat.Min, stat.MinItem.id, stat.Max, stat.MaxItem.id, stat.Mean));
                }
                print.AppendLine();
            }

            foreach (ToypetFeed feed in Utility.PetFeed.Items) {
                PetFlavour flavour = new PetFlavour();
                flavour.id = feed.id;
                flavour.count = feed.feeding_count;
                flavour.cd = feed.feeding_cooltime;
                flavour.love_count = feed.limit_love_count;
                // flavour.name = Utility.StringIndex.GetString(feed.name);
                flavour.desc = Utility.StringIndex.GetString(feed.desc);
                List<PetFood> list = new List<PetFood>();
                expFeed.Export<PetFood>(feed, "favorite_flavor_id", list);
                expFeed.Export<PetFood>(feed, "favorite_flavor_desc", list);
                List<PetFood> lovedList = new List<PetFood>();
                expFeed.Export<PetFood>(feed, "love_flavor_id_", lovedList);
                expFeed.Export<PetFood>(feed, "love_flavor_desc_", lovedList);
                list.AddRange(lovedList);
                flavour.food = list.Where(f => f.type != FoodType.NONE && (f.type != FoodType.ALL || f.rewards.Count > 0))
                                   .ToList();
                ourFeed.Flavours.Add(flavour);
            }

            HashSet<int> exportedRwds = new HashSet<int>();

            foreach (var f in ourFeed.Flavours) {
                foreach (var i in f.food) {
                    int nonEventItems = 0;
                    foreach (var ri in i.rewards) {
                        Item item = Utility.ItemIndex.GetItem(ri.item);
                        if (!item.Description.StartsWith("[Event]"))
                            nonEventItems++;
                    }

                    bool usePercent = true;
                    int healthyStartItem = 182006413;
                    int count = i.loved ? f.love_count : f.count;

                    for (int x = 0; x < i.rewards.Count; x++) {
                        PetReward r = i.rewards[x];
                        int priceItem = 0;
                        switch (i.type) {
                            case FoodType.POWDER:
                                priceItem = 182006375;
                                break;
                            case FoodType.CRYSTAL:
                                priceItem = 182006376;
                                break;
                            case FoodType.GEM:
                                priceItem = 182006377;
                                break;
                            case FoodType.HEALTHY1:
                            case FoodType.HEALTHY2:
                                priceItem = healthyStartItem++;
                                break;
                            default:
                                usePercent = false;
                                priceItem = r.item;
                                count = 1;
                                break;
                        }
                        Item item = Utility.ItemIndex.GetItem(r.item);
                        if (!item.Description.StartsWith("[Event]")) {
                            decimal percent = 100;
                            if (usePercent)
                                percent -= 100M / nonEventItems * (nonEventItems - x - 1);
                            decimal finalPrice = Utility.ItemIndex.GetItem(priceItem).price * count * percent / 100;
                            r.price = (long)Math.Round(finalPrice, 0);
                        }
                        exportedRwds.Add(r.item);
                    }
                }
            }

            var prwd = Utility.ItemIndex.ItemList.Where(i => i.tag == ItemTag.pet && !exportedRwds.Contains(i.id));
            StringBuilder ps = new StringBuilder();
            foreach (var p in prwd)
                ps.AppendLine(String.Format("{0}: {1}\t{2}\t{3}", p.id, p.Description, p.name, p.Quality));

            var saveSettings = new XmlWriterSettings()
            {
                CheckCharacters = false,
                CloseOutput = false,
                Encoding = new UTF8Encoding(false),
                Indent = true,
                IndentChars = "\t",
                NewLineChars = "\n",
            };

            string outputPath = Path.Combine(root, @".\output\");
            if (!Directory.Exists(outputPath))
                Directory.CreateDirectory(outputPath);

            using (FileStream stream = new FileStream(Path.Combine(outputPath, "pet_feed.xml"),
                                                      FileMode.Create, FileAccess.Write)) {
                using (XmlWriter wr = XmlWriter.Create(stream, saveSettings)) {
                    XmlSerializer ser = new XmlSerializer(typeof(PetFeed));
                    ser.Serialize(wr, ourFeed);
                }
            }

            Console.WriteLine("Loading quests...");
            Utility.LoadQuestFile(root);
            Console.WriteLine("Loading cosmetics...");
            Utility.LoadClientCosmetics(root);
            Console.WriteLine("Loading recipes...");
            Utility.LoadCombinedRecipes(root);
            Console.WriteLine("Loading skills...");
            Utility.LoadSkills(root);
            Console.WriteLine("Loading skill learns...");
            Utility.LoadSkillLearns(root);
            Console.WriteLine("Loading NPCs...");
            Utility.LoadClientNpcs(root);
            Console.WriteLine("Loading droplist...");
            Utility.LoadDroplist(root);
            Console.WriteLine("Loading presets...");

            string presetsPath = Path.Combine(root, @"data\presets");
            string[] presetFiles = Directory.GetFiles(presetsPath, "preset_*.xml");

            ClientPresetFile combinedPresets = null;

            foreach (string presetPath in presetFiles) {
                Utility.LoadClientPreset(presetPath);
                if (combinedPresets == null) {
                    combinedPresets = Utility.Presets;
                } else {
                    ClientPresetFile newPreset = Utility.Presets;
                    combinedPresets.Presets.Add(newPreset.Presets.First());
                }
            }

            List<OurPreset> ourPresets = new List<OurPreset>();
            foreach (Preset preset in combinedPresets.Presets) {
                OurPreset ourPreset = new OurPreset()
                {
                    name = preset.name.ToUpper(),
                    @class = (Class)Enum.Parse(typeof(Class), preset.pc_class.ToString(), true),
                    detail = preset.detail.ToLower(),
                    gender = preset.pc_type == PcType.pc_dm || preset.pc_type == PcType.pc_lm ? Gender.MALE : Gender.FEMALE,
                    race = preset.pc_type == PcType.pc_df || preset.pc_type == PcType.pc_dm ? ItemRace.ASMODIANS : ItemRace.ELYOS,
                    hair_color = getHexRGB(preset.hair_color),
                    lip_color = getHexRGB(preset.lip_color),
                    skin_color = getHexRGB(preset.skin_color),
                    height = (float)preset.scale,
                    hair_type = preset.hair_type,
                    face_type = preset.face_type
                };
                ourPresets.Add(ourPreset);
            }

            Utility.Presets.Presets = null;
            Utility.Presets.OurPresets = ourPresets;

            Console.WriteLine("Saving presets...");
            using (FileStream stream = new FileStream(Path.Combine(outputPath, "presets.xml"),
                                                      FileMode.Create, FileAccess.Write)) {
                using (XmlWriter wr = XmlWriter.Create(stream, saveSettings)) {
                    XmlSerializer ser = new XmlSerializer(typeof(ClientPresetFile));
                    ser.Serialize(wr, Utility.Presets);
                }
            }

            Console.WriteLine("Saving item groups...");
            ExportItemGroups(saveSettings, outputPath);

            Console.WriteLine("Saving recipes...");
            ExportRecipes(saveSettings, outputPath);

            Console.WriteLine("Saving grouped task items... ");

            taskExport = new TaskExport();
            foreach (var recipe in Utility.RecipeIndex.RecipeList) {
                if (recipe.qualification_race == RecipeRace.all)
                    continue;

                Task task = null;
                string skill = recipe.name;
                if (skill.StartsWith("rd"))
                    skill = skill.Remove(0, 1);
                skill = skill.Remove(0, 2).Substring(0, 2);

                if (recipe.qualification_race == RecipeRace.pc_light)
                    task = taskExport.RaceTasks[0][recipe.required_skillpoint, skill];
                else
                    task = taskExport.RaceTasks[1][recipe.required_skillpoint, skill];

                List<string> searchSuffixes = new List<string>();
                if (skill == "ha") {
                    searchSuffixes.Add("jr");
                    searchSuffixes.Add("hw");
                }
                searchSuffixes.Add(skill);

                HashSet<string> items = new HashSet<string>();
                foreach (string suffix in searchSuffixes) {
                    // products only if used to produce other products
                    // This won't add items as "item_d_ha_q6302"
                    if (recipe.product.Contains("_" + suffix + "_") && !recipe.product.StartsWith("item_"))
                        items.Add(recipe.product);
                    if (recipe.component1.StartsWith(suffix + "_part") ||
                        recipe.component1.StartsWith("shopmaterial_" + suffix + "_"))
                        items.Add(recipe.component1);
                    if (recipe.component2 != null && (recipe.component2.StartsWith(suffix + "_part") ||
                        recipe.component2.StartsWith("shopmaterial_" + suffix + "_")))
                        items.Add(recipe.component2);
                    if (recipe.component3 != null && (recipe.component3.StartsWith(suffix + "_part") ||
                        recipe.component3.StartsWith("shopmaterial_" + suffix + "_")))
                        items.Add(recipe.component3);
                    if (recipe.component4 != null && (recipe.component4.StartsWith(suffix + "_part") ||
                        recipe.component4.StartsWith("shopmaterial_" + suffix + "_")))
                        items.Add(recipe.component4);
                    if (recipe.component5 != null && (recipe.component5.StartsWith(suffix + "_part") ||
                        recipe.component5.StartsWith("shopmaterial_" + suffix + "_")))
                        items.Add(recipe.component5);
                    if (recipe.component6 != null && (recipe.component6.StartsWith(suffix + "_part") ||
                        recipe.component6.StartsWith("shopmaterial_" + suffix + "_")))
                        items.Add(recipe.component6);
                    if (recipe.component7 != null && (recipe.component7.StartsWith(suffix + "_part") ||
                        recipe.component7.StartsWith("shopmaterial_" + suffix + "_")))
                        items.Add(recipe.component7);
                }

                items.Add("rec_" + recipe.name);

                // All shopmaterials fall into range: min = 169400010; max = 169405025
                foreach (string item in items) {
                    Item inItem = Utility.ItemIndex.GetItem(item);
                    if (inItem != null) {
                        if (task.ContainsItem(inItem.id))
                            continue;

                        // exclude recipes which are autolearned
                        if (inItem.craft_recipe_info != null &&
                            Utility.RecipeIndex[inItem.craft_recipe_info].auto_learn > 0)
                            continue;
                        RecipeItem rcItem = new RecipeItem();
                        rcItem.id = inItem.id;
                        rcItem.level = inItem.level;
                        rcItem.name = inItem.name;
                        rcItem.price = inItem.price;
                        rcItem.desc = inItem.Description;
                        rcItem.race = task.race;
                        rcItem.isRecipe = inItem.craft_recipe_info != null;
                        task.Items.Add(rcItem);
                    } else {
                        Debug.Print("Missing recipe item: '{0}'", item);
                    }
                }
            }

            taskExport.RaceTasks[0].Tasks = taskExport.RaceTasks[0].Tasks.OrderBy(t => t.SortString).ToList();
            taskExport.RaceTasks[1].Tasks = taskExport.RaceTasks[1].Tasks.OrderBy(t => t.SortString).ToList();

            using (FileStream stream = new FileStream(Path.Combine(root, @".\output\tasks.xml"),
                                                      FileMode.Create, FileAccess.Write)) {
                using (XmlWriter wr = XmlWriter.Create(stream, saveSettings)) {
                    XmlSerializer ser = new XmlSerializer(typeof(TaskExport));
                    ser.Serialize(wr, taskExport);
                }
            }

            Console.WriteLine("Modifying item templates...");
            InjectBonusesToItemTemplate(saveSettings, outputPath);

            Console.Write("Parsing quests... ");
            int top = Console.CursorTop;
            int left = Console.CursorLeft;

            foreach (var q in Utility.QuestIndex.QuestList) {

                Console.SetCursorPosition(left, top);
                Console.Write("Q" + q.id);

                //WrappedBonus wi = null;
                //if (q.reward_item_ext_1 != null && q.reward_item_ext_1.StartsWith("wrap_")) {
                //    wi = new WrappedBonus();
                //    string[] itemData = q.reward_item_ext_1.Split(' ');
                //    Item item = Utility.ItemIndex.GetItem(itemData[0]);
                //    if (item == null)
                //        wi.originalItemId = 0;
                //    else
                //        wi.originalItemId = item.id;

                //    wi.bonusLevel = Utility.GetLevelFromName(itemData[0]);
                //    if (itemData[0].Contains("_enchant_"))
                //        wi.type = BonusType.ENCHANT;
                //    else if (itemData[0].Contains("_matter_option_"))
                //        wi.type = BonusType.MANASTONE;
                //    else
                //        wi = null; // _matter_matter_ do not contain names in client_strings yet
                //}
                if (q.HasRandomRaward()) {
                    BonusData bi = new BonusData();
                    bi.questId = q.id;
                    bi.BonusInfos = new List<AbstractInventoryBonus>();

                    var container = new DummyContainer();
                    container.checkItems = new List<CheckItem>();
                    utility.Export(q, "reward_item", container.checkItems);
                    utility.Export(q, "check_item", container.checkItems);
                    bi.BonusInfos = container.checkItems.Where(item => item.bonuses.Count > 0)
                                                        .SelectMany(item => item.bonuses).ToList();
                    //if (wi != null) {
                    //    bi.BonusInfos.Add(wi);
                    //}
                    bonuses.BonusItems.Add(bi);
                } /*else if (wi != null) {
                    BonusData bi = new BonusData();
                    bi.questId = q.id;
                    bi.BonusInfos = new List<AbstractInventoryBonus>() { wi };
                    bonuses.BonusItems.Add(bi);
                }*/

                Thread.Sleep(1);
            }

            Console.Clear();
            Console.WriteLine("Saving bonuses template... ");

            using (FileStream stream = new FileStream(Path.Combine(outputPath, "bonuses.xml"),
                                                      FileMode.Create, FileAccess.Write)) {
                using (XmlWriter wr = XmlWriter.Create(stream, saveSettings)) {
                    XmlSerializer ser = new XmlSerializer(typeof(InvBonuses));
                    ser.Serialize(wr, bonuses);
                }
            }

            Console.WriteLine("Done. Press any key...");
            Console.Read();
        }
        private static void ExportRecipes(XmlWriterSettings saveSettings, string outputPath) {
            var recipeFile = new RecipeTemplates();
            recipeFile.RecipeList = new List<RecipeTemplate>();

            var utility = Utility<CombineRecipe>.Instance;

            foreach (var recipe in Utility.RecipeIndex.RecipeList) {
                var template = new RecipeTemplate();
                template.id = recipe.id;
                template.nameid = Utility.StringIndex["str_" + recipe.name] * 2 + 1;
                template.skillid = Utility.GetSkillIdFromName(recipe.combineskill.ToString());
                template.race = (skillRace)Enum.Parse(typeof(skillRace),
                                                      recipe.qualification_race.ToString().ToUpper());
                template.skillpoint = recipe.required_skillpoint;
                template.dp = recipe.require_dp;
                template.autolearn = recipe.auto_learn;
                Item item = Utility.ItemIndex.GetItem(recipe.product);
                if (item == null)
                    Debug.Print("Missing product for recipe {0}", recipe.id);
                else
                    template.productid = item.id;
                template.quantity = recipe.product_quantity;
                template.componentquantity = recipe.component_quantity;
                template.tasktype = recipe.task_type;
                template.maxcount = recipe.max_production_count;
                template.delayid = recipe.craft_delay_id;
                template.delaytime = recipe.craft_delay_time;

                List<string> components = new List<string>();
                List<int> quantities = new List<int>();
                List<string> comboproducts = new List<string>();
                utility.Export(recipe, "component", components);
                utility.Export(recipe, "compo", quantities);

                // remove "component_quantity" which is the last in the list
                quantities.RemoveAt(quantities.Count - 1);
                for (int i = 0; i < components.Count; i++) {
                    string nameId = components[i];
                    item = Utility.ItemIndex.GetItem(nameId);
                    if (item == null) {
                        Debug.Print("Missing component for recipe {0}", recipe.id);
                        continue;
                    }
                    if (template.components == null)
                        template.components = new List<Component>();
                    var comp = new Component();
                    template.components.Add(comp);
                    comp.itemid = item.id;
                    comp.quantity = quantities[i];
                }

                utility.Export(recipe, "combo", comboproducts);

                for (int i = 0; i < comboproducts.Count; i++) {
                    string nameId = comboproducts[i];
                    item = Utility.ItemIndex.GetItem(nameId);
                    if (item == null) {
                        Debug.Print("Missing combo product for recipe {0}", recipe.id);
                        continue;
                    }
                    if (template.comboproducts == null)
                        template.comboproducts = new List<ComboProduct>();
                    var combo = new ComboProduct();
                    template.comboproducts.Add(combo);
                    combo.itemid = item.id;
                }
                recipeFile.RecipeList.Add(template);
            }

            using (FileStream stream = new FileStream(Path.Combine(outputPath, "recipe_templates.xml"),
                                                      FileMode.Create, FileAccess.Write)) {
                using (XmlWriter wr = XmlWriter.Create(stream, saveSettings)) {
                    XmlSerializer ser = new XmlSerializer(typeof(RecipeTemplates));
                    ser.Serialize(wr, recipeFile);
                }
            }
        }

        private static void InjectBonusesToItemTemplate(XmlWriterSettings saveSettings, string outputPath) {
            try {
                using (var fs = new FileStream(Path.Combine(root, @".\data\item_templates.xml"),
                                               FileMode.Open, FileAccess.Read))
                using (var reader = XmlReader.Create(fs)) {
                    XmlSerializer ser = new XmlSerializer(typeof(ItemTemplates));
                    Utility.OriginalItemTemplate = (ItemTemplates)ser.Deserialize(reader);
                }
            } catch (Exception ex) {
                Debug.Print(ex.ToString());
                return;
            }

            // Utility.CreateSkillMap(root);

            var allItems = itemGroups.grouped.SelectMany(g => g.items).SelectMany(s => s.items)
                                .ToDictionary(i => i.itemId, i => i);

            List<int> splitItems = Utility.ItemIndex.ItemList
                .Where(i => (i.disassembly_item.HasValue && i.disassembly_item.Value) &&
                             i.Category == ItemCategories.harvest).Select(i => i.id).ToList();

            List<int> questStart = Utility.ItemIndex.ItemList
                .Where(i => i.ActivateTarget == ActivateTargets.standalone && i.name.StartsWith("quest_") &&
                       i.quest == 3 && i.motion_name == null && i.area_to_use == null).
                    Select(i => i.id).ToList();

            Dictionary<int, List<int>> itemSkillPoints;
            Dictionary<int, List<int>> taskLevels = taskExport.RaceTasks.SelectMany(r => r.Tasks)
                .SelectMany(t => AggregatedTaskItem.CreateList(t.Items, t.race, t.skill, t.skillpoints))
                .Aggregate(itemSkillPoints = new Dictionary<int, List<int>>(), (l, a) =>
                {
                    if (a.SkillId == 0 || a.SkillPoints > 399)
                        return l;
                    int bonusLevel = a.SkillId;
                    bonusLevel <<= 10;
                    bonusLevel |= a.SkillPoints;
                    if (l.ContainsKey(a.ItemId)) {
                        var skillPoints = l[a.ItemId];
                        skillPoints.Add(bonusLevel);
                    } else {
                        List<int> list = new List<int>();
                        list.Add(bonusLevel);
                        l.Add(a.IsRecipe ? -a.ItemId : a.ItemId, list);
                    }
                    return l;
                });

            var elyosCraftItems = taskExport.RaceTasks[0].Tasks.SelectMany(t => t.Items).Select(rc => rc.id);
            var asmoCraftItems = taskExport.RaceTasks[1].Tasks.SelectMany(t => t.Items).Select(rc => rc.id);
            var commonCraftItems = elyosCraftItems.Intersect(asmoCraftItems).ToList();
            elyosCraftItems = elyosCraftItems.Except(commonCraftItems);
            asmoCraftItems = asmoCraftItems.Except(commonCraftItems);

            questStart.Add(182200214); // Namus's Diary

            // Documents which start quests
            questStart.Add(182200558); // Odium Refining Method
            questStart.Add(182200559); // Bandit's Letter
            questStart.Add(182201728); // Parchment Map
            questStart.Add(182201744); // Mapping the Revolutionaries
            questStart.Add(182201765); // Old Letter
            questStart.Add(182201770); // Adventurer's Diary
            questStart.Add(182203107); // Rolled Scroll
            questStart.Add(182203130); // Old Scroll
            questStart.Add(182203263); // A Bill Found in a Box
            questStart.Add(182204169); // Pamphlet
            questStart.Add(182204232); // Sodden Scroll
            questStart.Add(182204501); // Lepharist Book
            questStart.Add(182206084); // Lifeform Remodeling Report
            questStart.Add(182206700); // Dusty Book
            questStart.Add(182206722); // Balaur's Quartz of Memory
            questStart.Add(182206724); // Balaur's Map
            questStart.Add(182207009); // Ornate Jewelry Box
            questStart.Add(182207127); // Lifeform Remodeling Report
            questStart.Add(182207865); // Half-folded Paper
            questStart.Add(182208034); // Bloodied Note
            questStart.Add(182208043); // Red Journal
            questStart.Add(182208053); // Research Center Document
            questStart.Add(182209024); // Wet Letter
            questStart.Add(182209037); // Zombie's Diary
            questStart.Add(182209824); // Worn Book - 2.0

            questStart.Remove(182206722); // Murmur Fluid
            questStart.Remove(182206724); // Seasoned Moonflower Vegetables

            questStart.Add(182201309); // Jewel Box
            questStart.Add(182201400); // Fire Temple Key
            questStart.Add(182206842); // Vorgaltem Secret Order
            questStart.Add(182206843); // Stanis's Secret Order
            questStart.Add(182206844); // Temenos's Secret Order
            questStart.Add(182206845); // Omega's Fragment
            questStart.Add(182206846); // Violet Orb
            questStart.Add(182206847); // Violet Orb
            questStart.Add(182206848); // Violet Orb
            questStart.Add(182207845); // Angrief Special Orders
            questStart.Add(182207846); // Fundin's Special Orders
            questStart.Add(182207847); // Kirhua's Special Orders
            questStart.Add(182207848); // Shining Scroll
            questStart.Add(182207920); // Berokin's Image Marble
            questStart.Add(182207923); // Sakti's Crystal


            ItemTemplates saveTemplate = new ItemTemplates();
            List<ItemTemplate> exportTemplates = new List<ItemTemplate>();
            saveTemplate.TemplateList = new ItemTemplate[allItems.Count];
            var enumerator = allItems.Values.GetEnumerator();
            StringBuilder sqlData = new StringBuilder();

            while (enumerator.MoveNext()) {
                ItemTemplate template = new ItemTemplate();
                exportTemplates.Add(template);
                ItemExport exportItem = enumerator.Current;
                Item originalItem = exportItem.originalItem;

                template.id = exportItem.itemId;
                template.race = originalItem.race;
                template.origRace = exportItem.raceInternal;
                if (!String.IsNullOrEmpty(originalItem.gender_permitted))
                    template.gender = (Gender)Enum.Parse(typeof(Gender), originalItem.gender_permitted, true);
                if (elyosCraftItems.Contains(template.id))
                    template.origRace = ItemRace.ELYOS;
                else if (asmoCraftItems.Contains(template.id))
                    template.origRace = ItemRace.ASMODIANS;

                if (template.race != template.origRace) {
                    if (template.race != ItemRace.ALL) {
                        Debug.Print("Race mismatch... id: {0}", template.id);
                    }
                }

                if (feedItems.ContainsKey(template.id)) {
                    template.feed = feedItems[template.id].ToArray();
                }

                if (originalItem.doping_pet_useable) {
                    var feedItem = new ItemFeed() { type = FoodType.DOPING };
                    if (template.feed == null)
                        template.feed = new ItemFeed[1] { feedItem };
                    else {
                        Array.Resize(ref template.feed, template.feed.Length + 1);
                        template.feed[template.feed.Length - 1] = feedItem;
                    }
                }

                if (originalItem.quest == 0 && originalItem.Quality == ItemQualities.junk &&
                    originalItem.name.IndexOf("_pet_reward") == -1 &&
                    originalItem.name.IndexOf("_petreward") == -1 && originalItem.tag == ItemTag.none) {
                    string descr = originalItem.Description;
                    if (descr != null && descr.IndexOf("stinking", StringComparison.CurrentCultureIgnoreCase) == -1) {
                        var feedItem = new ItemFeed() { type = FoodType.MISC };
                        if (template.feed == null)
                            template.feed = new ItemFeed[1] { feedItem };
                        else {
                            Array.Resize(ref template.feed, template.feed.Length + 1);
                            template.feed[template.feed.Length - 1] = feedItem;
                        }
                    }
                }
                Thread.Sleep(10);

                if (splitItems.Contains(template.id)) {
                    if (template.actions == null) {
                        template.actions = new ItemActions[1];
                        template.actions[0] = new ItemActions();
                    }
                    template.actions[0].split = new SplitAction[1] { new SplitAction() };
                }

                if (originalItem.cash_social > 0) {
                    if (template.actions == null) {
                        template.actions = new ItemActions[1];
                        template.actions[0] = new ItemActions();
                    }
                    var ema = new EmotionAction();
                    ema.emotionid = originalItem.cash_social;
                    if (originalItem.cash_available_minute > 0) {
                        ema.expire = originalItem.cash_available_minute;
                        if (ema.expire > 60)
                            ema.expire--;
                    }
                    template.actions[0].emotion = new EmotionAction[1] { ema };
                }

                if (originalItem.cash_title > 0) {
                    if (template.actions == null) {
                        template.actions = new ItemActions[1];
                        template.actions[0] = new ItemActions();
                    }
                    var tia = new TitleAction();
                    tia.titleid = originalItem.cash_title;
                    if (originalItem.cash_available_minute > 0) {
                        tia.expire = originalItem.cash_available_minute;
                        if (tia.expire > 60)
                            tia.expire--;
                    }
                    template.actions[0].title = new TitleAction[1] { tia };
                }

                ItemExport exp = itemGroups.Manastones.Where(i => i.itemId == template.id).FirstOrDefault();
                if (exp != null) {
                    ItemBonus bonus = new ItemBonus(exp, BonusType.MANASTONE);
                    template.bonus = new ItemBonus[1] { bonus };
                }

                exp = itemGroups.CoinRewards.Where(i => i.itemId == template.id).FirstOrDefault();
                if (exp != null && exp.raceInternal != ItemRace.ALL) {
                    ItemBonus bonus = new ItemBonus(exp, BonusType.COIN);
                    template.bonus = new ItemBonus[1] { bonus };
                }

                exp = itemGroups.FoodRewards.Where(i => i.itemId == template.id).FirstOrDefault();
                if (exp != null && exp.raceInternal != ItemRace.ALL) {
                    ItemBonus bonus = new ItemBonus(exp, BonusType.FOOD);
                    template.bonus = new ItemBonus[1] { bonus };
                }

                //exp = itemGroups.EnchantRewards.Where(i => i.itemId == template.id).FirstOrDefault();
                //if (exp != null) {
                //    ItemBonus bonus = new ItemBonus(exp, BonusType.ENCHANT);
                //    template.bonus = new ItemBonus[1] { bonus };
                //}

                exp = itemGroups.MedicineRewards.Where(i => i.itemId == template.id).FirstOrDefault();
                if (exp != null) {
                    ItemBonus bonus = new ItemBonus(exp, BonusType.MEDICINE);
                    template.bonus = new ItemBonus[1] { bonus };
                }

                exp = itemGroups.BossRewards.Where(i => i.itemId == template.id).FirstOrDefault();
                if (exp != null) {
                    ItemBonus bonus = new ItemBonus(exp, BonusType.BOSS);
                    template.bonus = new ItemBonus[1] { bonus };
                }

                exp = itemGroups.MasterRecipes.Where(i => i.itemId == template.id).FirstOrDefault();
                if (exp != null) {
                    ItemBonus bonus = new ItemBonus(exp, BonusType.MASTER_RECIPE);
                    template.bonus = new ItemBonus[1] { bonus };
                }

                exp = itemGroups.QuestItems.Where(i => i.itemId == template.id).FirstOrDefault();
                if (exp != null) {
                    int pos = exp.itemName.LastIndexOf('_');
                    template.quest = Int32.Parse(exp.itemName.Substring(pos + 1, exp.itemName.Length - 2 - pos));
                    template.questSpecified = true;
                }

                exp = itemGroups.Documents.Where(i => i.itemId == template.id).FirstOrDefault();
                if (exp != null) {
                    if (template.actions == null) {
                        template.actions = new ItemActions[1];
                        template.actions[0] = new ItemActions();
                    } else if (template.actions[0].queststart != null && !questStart.Contains(template.id)) {
                        // delete queststart action (it's not a quest start item)
                        template.quest = template.actions[0].queststart[0].questid;
                        template.questSpecified = true;
                        template.actions[0].queststart = null;
                    }

                    if (exp.itemId != 182207127) {
                        template.actions[0].read = new ReadAction[1] { new ReadAction() };
                        if (template.quest == 0) {
                            Item item = Utility.ItemIndex.ItemList.Where(i => i.id == template.id).FirstOrDefault();
                            int pos = item.name.LastIndexOf('_');
                            template.quest = Int32.Parse(item.name.Substring(pos + 1, item.name.Length - 2 - pos));
                            template.questSpecified = true;
                        }
                    }
                }

                exp = itemGroups.WorkOrderItems.Where(i => i.itemId == template.id).FirstOrDefault();
                if (exp != null) {
                    int qIdStart = exp.itemName.IndexOf("_q") + 2;
                    int qIdEnd = exp.itemName.IndexOf("_", qIdStart);
                    string qStr = exp.itemName.Substring(qIdStart, qIdEnd - qIdStart);
                    int qId = Int32.Parse(qStr);
                    template.quest = qId;
                    template.questSpecified = true;
                }

                if (template.actions != null && template.actions[0] != null && !template.HasActions()) {
                    template.actions = null; // clear
                }

                if (questStart.Contains(template.id)) {
                    if (template.actions == null) {
                        template.actions = new ItemActions[1];
                        template.actions[0] = new ItemActions();
                    }
                    if (template.quest == 0) {
                        Item item = Utility.ItemIndex.ItemList.Where(i => i.id == template.id).FirstOrDefault();
                        int pos = item.name.LastIndexOf('_');
                        template.quest = Int32.Parse(item.name.Substring(pos + 1, item.name.Length - 2 - pos));
                        template.questSpecified = true;
                    }
                    var qa = new QuestStartAction();
                    // qa.questid = template.quest;
                    // qa.questidSpecified = true;
                    template.actions[0].queststart = new QuestStartAction[1] { qa };
                }

                if (taskLevels.ContainsKey(template.id) || taskLevels.ContainsKey(-template.id)) {
                    ItemBonus bonus = new ItemBonus();
                    bonus.type = taskLevels.ContainsKey(template.id) ? BonusType.TASK : BonusType.RECIPE;
                    bonus.typeSpecified = true;
                    var levels = bonus.type == BonusType.RECIPE ? taskLevels[-template.id] : taskLevels[template.id];
                    bonus.bonusLevel = String.Join(",", levels.Select(l => l.ToString()).ToArray());
                    template.bonus = new ItemBonus[1] { bonus };
                }

                template.can_fuse = exportItem.canFuse;
                // template.can_fuseSpecified = true;

                template.item_category = GetItemCategory(exportItem.itemIcon);
                template.item_type = exportItem.itemType.ToString().ToUpper();
                template.mask = exportItem.mask;
                template.maskSpecified = true;
                template.expire_time = (ExpireDuration)exportItem.expire_time;
                template.cash_item = exportItem.cash_item;
                template.cash_minute = (ExpireDuration)exportItem.cash_minute;
                template.exchange_mins = (ExpireDuration)exportItem.exchange_time;
                template.world_drop = exportItem.itemGroup == "random_drop";

                if (template.world_drop || exportItem.desc != null && exportItem.desc.StartsWith("[Event")) {
                    if (exportItem.level >= 52)
                        template.origRace = ItemRace.ALL;
                    sqlData.AppendFormat("DELETE FROM `droplist` WHERE itemId = {0};\n", template.id);
                    var allDrops = Utility.DropListTemplate.Drops.SelectMany(d => d.DropItems);
                    var removed = allDrops.Where(d => d.id == template.id).ToList();
                    foreach (var dropEntry in Utility.DropListTemplate.Drops) {
                        dropEntry.DropItems.RemoveAll(i => removed.Contains(i));
                    }
                }

                string minLevel = String.Empty;
                for (int i = 0; i < 12; i++)
                    minLevel += exportItem.level + ",";
                minLevel = minLevel.TrimEnd(',');
                if (exportItem.restricts != minLevel)
                    template.restrict = exportItem.restricts;

                if (exportItem.restricts_max != "0,0,0,0,0,0,0,0,0,0,0,0")
                    template.restrict_max = exportItem.restricts_max;

                SkillTemplate skill = exportItem.skill_use;
                if (skill != null) {
                    if (template.actions == null)
                        template.actions = new ItemActions[1];
                    if (template.actions[0] == null)
                        template.actions[0] = new ItemActions();
                    template.actions[0].skilluse = new SkillUseAction[1] { 
                        new SkillUseAction(skill.skill_id, skill.lvl)
                    };
                } else if (template.actions != null) {
                    template.actions[0].skilluse = null; // clear it
                }

                if (exportItem.modifiers != null)
                    template.modifiers = new Modifiers[1] { 
                        new Modifiers() { 
                            modifierList = exportItem.modifiers/*.OrderBy(m => m.GetType().Name)
                                                                 .ToList()*/
                        }
                    };
                else if (template.modifiers != null)
                    template.modifiers = null;

                template.quality = (ItemQuality)Enum.Parse(typeof(ItemQuality), originalItem.Quality.ToString().ToUpper());
                template.qualitySpecified = true;

                template.price = (int)originalItem.price;
                template.priceSpecified = template.price > 0;

                template.option_slot_bonus = originalItem.option_slot_bonus;
                template.option_slot_bonusSpecified = template.option_slot_bonus > 0;

                if (originalItem.no_enchant != null) {
                    bool value = originalItem.no_enchant.ToBoolean(CultureInfo.InvariantCulture);
                    if (value) {
                        template.no_enchant = value;
                        // template.no_enchantSpecified = true;
                    }
                }

                if (originalItem.can_proc_enchant != null) {
                    bool value = originalItem.can_proc_enchant.ToBoolean(CultureInfo.InvariantCulture);
                    if (value) {
                        template.can_proc_enchant = true;
                        // template.can_proc_enchantSpecified = true;
                    }
                }

                template.max_stack_count = originalItem.max_stack_count;
                template.max_stack_countSpecified = template.max_stack_count > 1;

                template.level = originalItem.level;
                template.levelSpecified = true;

                template.slot = (int)originalItem.EquipmentSlots;
                if (template.slot > 0)
                    template.slotSpecified = true;

                if (originalItem.weapon_boost_value > 0) {
                    template.weapon_boost = originalItem.weapon_boost_value;
                    template.weapon_boostSpecified = true;
                }

                if (originalItem.EquipmentSlots != EquipmentSlots.none) {
                    if (originalItem.WeaponType != WeaponTypes.None &&
                        originalItem.WeaponType != WeaponTypes.NoWeapon) {
                        // DAGGER_1H,MACE_1H,SWORD_1H,TOOLHOE_1H,BOOK_2H,ORB_2H,
                        // POLEARM_2H,STAFF_2H,SWORD_2H,TOOLPICK_2H,TOOLROD_2H,BOW
                        template.equipment_type = EquipType.WEAPON;
                        template.equipment_typeSpecified = true;
                        template.weapon_type = (weaponType)originalItem.WeaponType;
                        template.weapon_typeSpecified = true;
                    }
                    if (originalItem.EquipmentSlots == EquipmentSlots.right_or_left_battery) {
                        template.equipment_type = EquipType.ARMOR;
                        template.equipment_typeSpecified = true;
                        template.armor_type = armorType.SHARD;
                        template.armor_typeSpecified = true;
                    } else if (template.item_category == "SHIELD" || template.item_category == "CASH_SHIELD") {
                        template.equipment_type = EquipType.ARMOR;
                        template.equipment_typeSpecified = true;
                        template.armor_type = armorType.SHIELD;
                        template.armor_typeSpecified = true;
                    } else if (template.item_category == "ARROW") {
                        template.equipment_type = EquipType.ARMOR;
                        template.equipment_typeSpecified = true;
                        template.armor_type = armorType.ARROW;
                        template.armor_typeSpecified = true;
                    } else if (originalItem.ArmorType != ArmorTypes.none &&
                        originalItem.ArmorType != ArmorTypes.no_armor) {
                        template.equipment_type = EquipType.ARMOR;
                        template.equipment_typeSpecified = true;
                        // + SHARD, ARROW, SHIELD
                        template.armor_type = (armorType)Enum.Parse(typeof(armorType),
                                        originalItem.ArmorType.ToString().ToUpper());
                        template.armor_typeSpecified = true;
                    } else if (template.equipment_type == EquipType.NONE) {
                        template.equipment_type = EquipType.ARMOR;
                        template.equipment_typeSpecified = true;
                    }
                }

                template.dmg_decal = originalItem.dmg_decal;
                template.dmg_decalSpecified = template.dmg_decal > 0;

                if (!String.IsNullOrEmpty(originalItem.desc)) {
                    int desc = Utility.StringIndex[originalItem.desc];
                    if (desc != -1) {
                        template.desc = desc * 2 + 1;
                        template.descSpecified = true;
                    } else {
                        Debug.Print("Missing description for {0}", originalItem.id);
                    }
                }

                template.attack_gap = originalItem.attack_gap;
                template.attack_gapSpecified = template.attack_gap > 0;

                if (originalItem.item_drop_permitted != null) {
                    bool value = originalItem.item_drop_permitted.ToBoolean(CultureInfo.InvariantCulture);
                    if (value) {
                        template.drop = true;
                        // template.dropSpecified = true;
                    }
                }

                template.usedelayid = originalItem.use_delay_type_id;
                if (template.usedelayid > 0) {
                    template.usedelayidSpecified = true;
                    template.usedelay = originalItem.use_delay;
                    template.usedelaySpecified = template.usedelay > 0;
                }

                if (originalItem.require_shard > 0) {
                    template.stigma = new Stigma();
                    template.stigma.shard = originalItem.require_shard;
                    template.stigma.shardSpecified = true;
                    ClientSkill stigmaSkill = Utility.SkillIndex[originalItem.gain_skill1];
                    if (stigmaSkill != null) {
                        // gain_skill2 and gain_level2 used only in the test item
                        template.stigma.skillid = stigmaSkill.id;
                        template.stigma.skillidSpecified = true;
                        template.stigma.skilllvl = originalItem.gain_level1;
                        template.stigma.skilllvlSpecified = true;

                        var utility = Utility<Item>.Instance;
                        List<string> requiredSkills = new List<string>();
                        utility.Export<String>(originalItem, "require_skill", requiredSkills);
                        if (requiredSkills.Count > 0) {
                            List<RequireSkill> skills = new List<RequireSkill>();
                            for (int n = 1; n <= requiredSkills.Count; n++) {
                                FieldInfo fld = typeof(Item).GetField(String.Format("require_skill{0}_lv", n),
                                     BindingFlags.Instance | BindingFlags.Public);
                                int skillLvl = (int)fld.GetValue(originalItem);
                                string[] skillIds = requiredSkills[n - 1].Split(new string[] { " ", "," },
                                                                    StringSplitOptions.RemoveEmptyEntries);
                                List<int> ids = new List<int>();
                                foreach (string skillId in skillIds) {
                                    stigmaSkill = Utility.SkillIndex[skillId];
                                    if (stigmaSkill != null && stigmaSkill.id > 0)
                                        ids.Add(stigmaSkill.id);
                                }
                                if (ids.Count > 0)
                                    skills.Add(new RequireSkill()
                                    {
                                        skillId = ids.ToArray(),
                                        skilllvl = skillLvl,
                                        skilllvlSpecified = true
                                    });
                            }
                            if (skills.Count > 0)
                                template.stigma.require_skill = skills.ToArray();
                        }
                    } else {
                        Debug.Print("Missing stigma for {0}", originalItem.id);
                    }
                }

                if (originalItem.can_dye) {
                    template.dye = true;
                    // template.dyeSpecified = true;
                }

                if (originalItem.craft_recipe_info != null) {
                    var recipe = Utility.RecipeIndex[originalItem.craft_recipe_info];
                    if (recipe != null) {
                        if (template.actions == null)
                            template.actions = new ItemActions[1];
                        if (template.actions[0] == null)
                            template.actions[0] = new ItemActions();
                        var action = new CraftLearnAction();
                        action.recipeid = recipe.id;
                        action.recipeidSpecified = true;
                        template.actions[0].craftlearn = new CraftLearnAction[1] { action };
                    } else {
                        Debug.Print("Missing recipe for {0}", originalItem.id);
                    }
                }

                if (!String.IsNullOrEmpty(originalItem.dyeing_color) || originalItem.name == "dye_remover") {
                    if (template.actions == null)
                        template.actions = new ItemActions[1];
                    if (template.actions[0] == null)
                        template.actions[0] = new ItemActions();
                    var action = new DyeAction();
                    if (originalItem.name == "dye_remover")
                        action.color = "no";
                    else
                        action.color = action.color = getHexRGB(originalItem.dyeing_color);
                    template.actions[0].dye = new DyeAction[1] { action };
                }

                if (!String.IsNullOrEmpty(originalItem.cosmetic_name)) {
                    CosmeticInfo info = Utility.CosmeticsIndex[originalItem.cosmetic_name.ToLower()];
                    if (info == null) {
                        Debug.Print("Cosmetics not found: {0}", originalItem.cosmetic_name);
                    } else {
                        if (template.actions == null)
                            template.actions = new ItemActions[1];
                        if (template.actions[0] == null)
                            template.actions[0] = new ItemActions();
                        var action = new CosmeticAction();
                        if (info.hair_type > 0) {
                            action.hairType = info.hair_type;
                        }
                        if (info.face_type > 0) {
                            action.faceType = info.face_type;
                        }
                        if (info.makeup_type > 0) {
                            action.makeupType = info.makeup_type;
                        }
                        if (info.tattoo_type > 0) {
                            action.tattooType = info.tattoo_type;
                        }
                        if (info.voice_type > 0) {
                            action.voiceType = info.voice_type;
                        }
                        if (!String.IsNullOrEmpty(info.face_color)) {
                            action.face = getHexRGB(info.face_color);
                        }
                        if (!String.IsNullOrEmpty(info.hair_color)) {
                            action.hair = getHexRGB(info.hair_color);
                        }
                        if (!String.IsNullOrEmpty(info.lip_color)) {
                            action.lips = getHexRGB(info.lip_color);
                        }
                        if (!String.IsNullOrEmpty(info.eye_color)) {
                            action.eyes = getHexRGB(info.eye_color);
                        }
                        if (!String.IsNullOrEmpty(info.preset_name)) {
                            action.preset = info.preset_name.ToUpper();
                        }
                        template.actions[0].cosmetic = new CosmeticAction[1] { action };
                    }
                }

                if (originalItem.inven_warehouse_max_extendlevel != 0 &&
                    (template.item_category == "CUBE" || template.item_category == "CASH_CARD")) {
                    if (template.actions == null)
                        template.actions = new ItemActions[1];
                    if (template.actions[0] == null)
                        template.actions[0] = new ItemActions();
                    var action = new TicketAction();
                    action.function = template.item_category == "CUBE" ? ticketFunction.addCube : ticketFunction.addWharehouse;
                    template.actions[0].ticket = new TicketAction[1] { action };
                }

                if (!String.IsNullOrEmpty(originalItem.proc_enchant_skill)) {
                    ClientSkill enchantSkill = Utility.SkillIndex[originalItem.proc_enchant_skill];
                    if (enchantSkill != null) {
                        template.godstone = new Godstone();
                        template.godstone.probability = originalItem.proc_enchant_effect_occur_prob;
                        if (template.godstone.probability > 0)
                            template.godstone.probabilitySpecified = true;
                        template.godstone.probabilityleft = originalItem.proc_enchant_effect_occur_left_prob;
                        template.godstone.skillid = enchantSkill.id;
                        template.godstone.skillidSpecified = true;
                        template.godstone.skilllvl = originalItem.proc_enchant_skill_level;
                        template.godstone.skilllvlSpecified = true;
                    } else {
                        Debug.Print("Missing godstone for {0}", originalItem.id);
                    }
                }

                if (!String.IsNullOrEmpty(originalItem.skill_to_learn)) {
                    ClientSkill learnSkill = Utility.SkillIndex[originalItem.skill_to_learn];
                    if (learnSkill != null) {
                        if (template.actions == null)
                            template.actions = new ItemActions[1];
                        if (template.actions[0] == null)
                            template.actions[0] = new ItemActions();

                        var learns = Utility.SkillLearnIndex[originalItem.skill_to_learn];
                        if (learns.Count() > 0) {
                            var validActions = new List<SkillLearnAction>();
                            for (int i = 0; i < learns.Count(); i++) {
                                LearnSkill learn = learns.ElementAt(i);
                                if (learn.skill_level > 1)
                                    continue;
                                var action = new SkillLearnAction();
                                validActions.Add(action);
                                action.skillid = learnSkill.id;
                                action.skillidSpecified = true;
                                action.@class = (skillPlayerClass)Enum.Parse(typeof(skillPlayerClass), learn.@class);
                                action.classSpecified = true;
                                action.level = learn.pc_level;
                                action.levelSpecified = true;
                                action.race = (skillRace)Enum.Parse(typeof(skillRace), learn.race.ToUpper());
                                action.raceSpecified = true;
                            }
                            template.actions[0].skilllearn = validActions.ToArray();
                        }
                    } else {
                        Debug.Print("Missing learn skill for {0}", originalItem.id);
                    }
                }

                if (!String.IsNullOrEmpty(originalItem.toy_pet_name)) {
                    int npcId = Utility.ClientNpcIndex[originalItem.toy_pet_name];
                    if (npcId == -1)
                        Debug.Print("Missing pet NPC for {0}", originalItem.id);
                    else {
                        if (template.actions == null)
                            template.actions = new ItemActions[1];
                        if (template.actions[0] == null)
                            template.actions[0] = new ItemActions();
                        var action = new ToyPetSpawnAction();
                        action.npcid = npcId;
                        action.npcidSpecified = true;
                        template.actions[0].toypetspawn = new ToyPetSpawnAction[1] { action };
                    }
                }

                if (originalItem.return_worldid > 0) {
                    template.return_world = originalItem.return_worldid;
                    template.return_worldSpecified = true;
                    if (!String.IsNullOrEmpty(originalItem.return_alias))
                        template.return_alias = originalItem.return_alias.ToUpper();
                }

                template.cash_item = originalItem.cash_item;

                if (template.item_category == "PINCER") {
                    if (template.actions == null)
                        template.actions = new ItemActions[1];
                    if (template.actions[0] == null)
                        template.actions[0] = new ItemActions();
                    var action = new ExtractAction();
                    template.actions[0].extract = new ExtractAction[1] { action };
                }

                if (originalItem.sub_enchant_material_many > 0) {
                    if (template.actions == null)
                        template.actions = new ItemActions[1];
                    if (template.actions[0] == null)
                        template.actions[0] = new ItemActions();
                    var action = new EnchantItemAction();
                    action.count = originalItem.sub_enchant_material_many;
                    template.actions[0].enchant = new EnchantItemAction[1] { action };
                }

                if (originalItem.AttackType != AttackTypes.none) {
                    template.attack_type = originalItem.AttackType.ToString().ToUpper();
                }

                if (originalItem.abyss_point > 0) {
                    template.ap = (int)originalItem.abyss_point;
                }

                if (originalItem.abyss_item_count > 0) {
                    template.aic = (int)originalItem.abyss_item_count;
                }

                if (!String.IsNullOrEmpty(originalItem.abyss_item)) {
                    Item abyssItem = Utility.ItemIndex.GetItem(originalItem.abyss_item);
                    if (abyssItem == null)
                        Debug.Print("Missing abyss item for {0}", originalItem.id);
                    else {
                        template.ai = abyssItem.id;
                    }
                }

                if (originalItem.extra_currency_item_count > 0) {
                    template.eic = (int)originalItem.extra_currency_item_count;
                }

                if (!String.IsNullOrEmpty(originalItem.extra_currency_item)) {
                    Item extraItem = Utility.ItemIndex.GetItem(originalItem.extra_currency_item);
                    if (extraItem == null)
                        Debug.Print("Missing extra item for {0}", originalItem.id);
                    else {
                        template.ei = extraItem.id;
                    }
                }

                if (originalItem.coupon_item_count > 0) {
                    template.cic = (int)originalItem.coupon_item_count;
                }

                if (!String.IsNullOrEmpty(originalItem.coupon_item)) {
                    Item extraItem = Utility.ItemIndex.GetItem(originalItem.coupon_item);
                    if (extraItem == null)
                        Debug.Print("Missing extra item for {0}", originalItem.id);
                    else {
                        template.ci = extraItem.id;
                    }
                }

                if (originalItem.BonusApply != Bonuses.none) {
                    template.bonus_apply = (BonusApplyType)Enum.Parse(typeof(BonusApplyType),
                                                originalItem.BonusApply.ToString().ToUpper());
                    // template.bonus_applySpecified = true;
                }

                // template.name
            }

            saveTemplate.TemplateList = exportTemplates.OrderBy(i => i.id).ToArray();

            //var droplistDrops = Utility.DropListTemplate.Drops.SelectMany(d => d.DropItems);
            //var existingItems = exportTemplates.Select(t => t.id);
            //var nonexistant = droplistDrops.Where(d => !existingItems.Contains(d.id)).ToList();
            //foreach (var dropEntry in Utility.DropListTemplate.Drops) {
            //    dropEntry.DropItems.RemoveAll(i => nonexistant.Contains(i));
            //}

            using (FileStream stream = new FileStream(Path.Combine(outputPath, "item_templates.xml"),
                                                      FileMode.Create, FileAccess.Write)) {
                using (XmlWriter wr = XmlWriter.Create(stream, saveSettings)) {
                    XmlSerializer ser = new XmlSerializer(typeof(ItemTemplates));
                    ser.Serialize(wr, saveTemplate);
                }
            }

            using (FileStream stream = new FileStream(Path.Combine(outputPath, "clean_world_drops.sql"),
                                                      FileMode.Create, FileAccess.Write)) {
                using (var wr = new StreamWriter(stream, Encoding.ASCII)) {
                    wr.Write(sqlData.ToString());
                }
            }

            var emptyDrops = Utility.DropListTemplate.Drops.Where(d => d.DropItems.Count == 0).ToList();
            Utility.DropListTemplate.Drops.RemoveAll(d => emptyDrops.Contains(d));

            using (FileStream stream = new FileStream(Path.Combine(outputPath, "droplist.xml"),
                                                      FileMode.Create, FileAccess.Write)) {
                using (XmlWriter wr = XmlWriter.Create(stream, saveSettings)) {
                    XmlSerializer ser = new XmlSerializer(typeof(Droplist));
                    ser.Serialize(wr, Utility.DropListTemplate);
                }
            }

            // Do some tests
            // TestUtility.TestWorkOrders(itemTemplates);
        }

        public static string GetItemCategory(string iconName) {
            if (iconName == null)
                return String.Empty;
            iconName = iconName.ToLower();
            int skip = 2;
            string prefix = String.Empty;
            if (iconName.StartsWith("icon_cash_item_lineage2_body"))
                return "CASH_LINEAGE2_BODY";
            else if (iconName.StartsWith("icon_cash_item_americandress01_body"))
                return "CASH_AMERICAN_DRESS_BODY";
            else if (iconName.StartsWith("icon_cash_item_russiandress01_body"))
                return "CASH_RUSSIAN_DRESS_BODY";
            else if (iconName.StartsWith("icon_cash_item_europeandress01_body"))
                return "CASH_EUROPEAN_DRESS_BODY";
            else if (iconName.StartsWith("icon_cash_item_")) {
                skip = 3;
                prefix = "CASH_";
            } else if (iconName.StartsWith("icon_shop_item_")) {
                skip = 3;
                prefix = "SHOP_";
            }
            string[] nameParts = iconName.Split('_');
            int shorten = nameParts[nameParts.Length - 1].Length == 1 ? 1 : 0;
            string[] newParts = new String[nameParts.Length - skip - shorten];
            Array.Copy(nameParts, skip, newParts, 0, newParts.Length);

            StringBuilder sb = new StringBuilder();
            sb.Append(prefix);
            char digit = '\0';
            if (Char.IsDigit(newParts[0][0]))
                digit = newParts[0][0];
            for (int i = 0; i < newParts.Length; i++) {
                string namePart = newParts[i];
                string newPart = string.Empty;
                bool failed = false;
                bool swap = false;

                foreach (char ch in namePart) {
                    if (Char.IsDigit(ch)) {
                        if (i == 0 && digit != '\0') {
                            swap = true;
                            continue;
                        }
                        failed = true;
                        break;
                    }
                    newPart += ch;
                    if (swap) {
                        newPart += digit;
                        swap = false;
                    }
                }
                if (failed) {
                    if (i == newParts.Length - 1 && sb.Length == 0)
                        sb.Append(newPart);
                } else {
                    sb.Append(newPart);
                }
                sb.Append('_');
            }
            return sb.ToString().TrimEnd('_').ToUpper();
        }

        private static void ExportItemGroups(XmlWriterSettings saveSettings, string outputPath) {
            itemGroups = new ItemsExportFile();
            var forwardItems = new HashSet<string>(StringComparer.InvariantCultureIgnoreCase);

            SkillMap skillMap = new SkillMap();
            skillMap.LoadFromFile(root);

            foreach (var item in Utility.ItemIndex.ItemList) {
                string[] nameParts = item.name.Split('_');
                var exportItem = new ItemExport();
                exportItem.itemId = item.id;
                exportItem.itemName = item.name;
                exportItem.armorType = item.ArmorType;
                exportItem.category = item.Category;
                exportItem.itemType = item.ItemType;
                exportItem.level = item.level;
                exportItem.quality = (Qualities)item.Quality;
                exportItem.raceInternal = item.race;
                exportItem.slot = item.EquipmentSlots;
                exportItem.weaponType = item.WeaponType;
                if (item.can_composite_weapon != null)
                    exportItem.canFuse = item.can_composite_weapon.ToBoolean(CultureInfo.CurrentCulture);
                exportItem.itemIcon = item.icon_name;
                exportItem.mask = item.GetMask();
                exportItem.expire_time = item.expire_time;
                exportItem.cash_item = item.cash_item;
                exportItem.cash_minute = item.cash_available_minute;
                exportItem.exchange_time = item.temporary_exchange_time;
                exportItem.race = item.race;
                exportItem.restricts = item.GetRestrictions();
                exportItem.restricts_max = item.GetMaxRestrictions();
                exportItem.modifiers = item.GetModifiers(skillMap);
                exportItem.originalItem = item;
                exportItem.tag = item.tag;

                if (!String.IsNullOrEmpty(item.activation_skill)) {
                    ClientSkill skill = Utility.SkillIndex[item.activation_skill];
                    if (skill == null) {
                        Debug.Print("Missing skill description: {0}", item.activation_skill);
                    } else {
                        // level can not be zero
                        exportItem.skill_use = new SkillTemplate(skill.id, item.activation_level);
                    }
                }

                Item it = Utility.ItemIndex.GetItem(item.name);
                if (it != null) {
                    exportItem.desc = it.Description;
                }
                //if (exportItem.desc != null) {
                //    if (exportItem.desc.StartsWith("Shadetouched ") ||
                //        exportItem.desc.StartsWith("Suntouched ")) {
                //        exportItem.raceInternal = Race.All; // don't export selectable rewards
                //        continue;
                //    }
                //}

                if (exportItem.raceInternal != ItemRace.ALL) {
                    // don't change
                } else {
                    if (forwardItems.Contains(exportItem.itemName, StringComparer.InvariantCultureIgnoreCase)) {
                        exportItem.raceInternal = ItemRace.ELYOS;
                        forwardItems.Remove(exportItem.itemName);
                    } else {
                        bool dFound = false;
                        bool startFound = false;
                        int dIndex = 0;
                        for (int i = 0; i < nameParts.Length; i++) {
                            string np = nameParts[i];
                            if (!startFound && np.Length == 1)
                                startFound = true;
                            if (!startFound && np.Length > 1)
                                continue;
                            if (!dFound && np == "d") {
                                dFound = true;
                                dIndex = i;
                                exportItem.raceInternal = ItemRace.ASMODIANS;
                                continue;
                            }
                            if (dFound && np == "d") {
                                if (item.ItemType != ItemTypes.draconic) {
                                    // NPC Balic items and one wrap box
                                }
                            }
                        }

                        if (exportItem.raceInternal == ItemRace.ASMODIANS) {
                            string nameL = String.Join("_", nameParts, 0, dIndex);
                            string nameR = String.Join("_", nameParts, dIndex + 1, nameParts.Length - dIndex - 1);
                            string lightName = String.Concat(nameL, "_", nameR).TrimStart('_');
                            Item lightItem = Utility.ItemIndex.GetItem(lightName);
                            if (lightItem != null) {
                                // Debug.Print("Asmodian: {0}, Elyos: {1}", exportItem.itemId, lightItem.id);
                                if (itemGroups.allNames.ContainsKey(lightName))
                                    itemGroups.allNames[lightName].raceInternal = ItemRace.ELYOS;
                                else
                                    forwardItems.Add(lightName.ToLower());
                            } else {
                                lightName = String.Concat(nameL, "_l_", nameR).TrimStart('_');
                                lightItem = Utility.ItemIndex.GetItem(lightName);
                                if (lightItem != null) {
                                    // Debug.Print("Asmodian: {0}, Elyos: {1}", exportItem.itemId, lightItem.id);
                                    if (itemGroups.allNames.ContainsKey(lightName))
                                        itemGroups.allNames[lightName].raceInternal = ItemRace.ELYOS;
                                    else
                                        forwardItems.Add(lightName.ToLower());
                                }
                            }
                        }
                    }
                }

                string suffix = nameParts.Last().ToLower();
                bool isTaskItem = nameParts[0] == "item" && nameParts[1] == "part"; // always ends with _a or _b

                if (suffix.EndsWith("day")) {
                    exportItem.rentDays = Int32.Parse(nameParts.Last().Replace("day", String.Empty));
                    Array.Resize(ref nameParts, nameParts.Length - 1);
                } else if (suffix.EndsWith("ae") || suffix.EndsWith("an") || suffix.EndsWith("ex")) {
                    Array.Resize(ref nameParts, nameParts.Length - 1);
                }

                if (item.id == 110500631) {
                }

                if (nameParts.Length > 2) {
                    exportItem.suffix = Utility.GetLevelFromName(nameParts.Last());
                    int lvl = exportItem.suffix >> 7;
                    if (exportItem.suffix != 0) {
                        string prior = nameParts[nameParts.Length - 2];
                        if (prior.Length < 3) {
                            if (prior == "p" && lvl > 30 && nameParts[0] == "rec" && nameParts[nameParts.Length - 4] == "d") {
                                int skillId = Utility.GetSkillIdFromName(item.name) & 0xF;
                                if (lvl > 50)
                                    lvl = 60;
                                exportItem.suffix = (skillId << 10) | (((int)exportItem.quality << 7) | lvl);
                                exportItem.itemGroup = "hearts";
                            } else {
                                char qPrefix = Char.ToLower(exportItem.quality.ToString()[0]);
                                // check if _c1_c_10b, then group is "c"
                                string before = nameParts[nameParts.Length - 3];
                                if (before[0] == qPrefix && (before.Length == 1 ||
                                                             before.Length == 2 && Char.IsDigit(before[1]))) {
                                    exportItem.itemGroup = prior;
                                }
                            }
                        } else if (prior.Length == 6 && prior.EndsWith("stats") && nameParts[0] == "rec" &&
                                   nameParts[4] == "dr" && lvl > 30) {
                            int skillId = Utility.GetSkillIdFromName(item.name) & 0xF;
                            exportItem.suffix = (skillId << 10) | (((int)exportItem.quality << 7) | lvl);
                            exportItem.itemGroup = "hearts";
                        } else if (prior == "dark" || prior == "light") {
                            // fix race, because it can contain _d_ for elyos
                            exportItem.raceInternal = prior[0] == 'd' ? ItemRace.ASMODIANS : ItemRace.ELYOS;
                            exportItem.itemGroup = prior;
                        }
                        if (nameParts[0] == "potion" || nameParts[0] == "remedy") {
                            exportItem.itemGroup = "medicine";
                        } else if (nameParts[0] == "shopmaterial") {
                            exportItem.itemGroup = "shopmaterial";
                        } else if (nameParts[0] == "junk") {
                            if (nameParts[1] == "q" || nameParts[2] == "q")
                                exportItem.itemGroup = "quest_junk";
                            else
                                exportItem.itemGroup = "junk";
                        } else if (nameParts[0] == "key") {
                            exportItem.itemGroup = "keys";
                        } else if (nameParts[0] == "test") {
                            exportItem.itemGroup = "tests";
                        } else if (nameParts[0] == "jewelry") {
                            exportItem.itemGroup = "ores";
                        } else if (nameParts[0] == "food" && exportItem.category != ItemCategories.harvest) {
                            /*if (nameParts[1] == "r" || nameParts[1] == "d" && nameParts[2] == "r")
                                exportItem.itemGroup = "food_tasty";
                            else */
                            if (nameParts[1] == "dr") {
                                if (nameParts[2] == "r" || nameParts[2] == "d" && nameParts[3] == "r")
                                    exportItem.itemGroup = "food_draconic_tasty";
                                else
                                    exportItem.itemGroup = "food_draconic";
                            } else
                                exportItem.itemGroup = "food";
                        } else if (nameParts[0] == "matter" && nameParts[1] == "option" && nameParts[2] == "r") {
                            exportItem.itemGroup = "manastone_reward";
                        } else if (nameParts[0] == "matter" && nameParts[1] == "enchant") {
                            exportItem.itemGroup = "enchant";
                        } else if (nameParts[0].ToLower() == "doc") {
                            exportItem.itemGroup = "docs";
                            exportItem.suffix = 0;
                        } else if (nameParts[0] == "harvest") {
                            exportItem.itemGroup = "harvest";
                            exportItem.suffix = 0;
                        } else if (nameParts[0] == "rmc") {
                            exportItem.itemGroup = "legion_rewards";
                        } else if (isTaskItem) {
                            exportItem.itemGroup = "craft_tasks";
                        } else if (nameParts[1] == "part") {
                            exportItem.itemGroup = "task_rewards";
                        } else if (nameParts[0] == "wrap") {
                            exportItem.itemGroup = "wrapped_rewards";
                        }
                    } else if (nameParts[nameParts.Length - 2] == "m") {
                        exportItem.isMaster = true;
                    } else if (nameParts[0] == "stigma") {
                        exportItem.itemGroup = "stigmas";
                        exportItem.suffix = 0;
                    } else if (nameParts[0] == "skillbook") {
                        exportItem.itemGroup = "skillbooks";
                        exportItem.suffix = 0;
                    }

                    if (exportItem.itemGroup == "c")
                        exportItem.itemGroup = "coin_rewards";

                    if (exportItem.itemGroup == "g")
                        exportItem.itemGroup = "boss";

                    if ((exportItem.itemGroup == "r" || exportItem.itemGroup == "R") &&
                        !exportItem.originalItem.Description.StartsWith("Level ")) {
                        if (exportItem.originalItem.level >= 52)
                            exportItem.raceInternal = ItemRace.ALL;
                        exportItem.itemGroup = "random_drop";
                    }
                } else {
                    if (nameParts[0].ToLower() == "quest") {
                        exportItem.itemGroup = "quest_items";
                        exportItem.suffix = 0;
                    } else if (nameParts[0] == "coin") {
                        exportItem.itemGroup = "coins";
                        exportItem.suffix = 0;
                    } else if (nameParts[0] == "medal") {
                        exportItem.itemGroup = "medals";
                        exportItem.suffix = 0;
                    }
                }

                if (exportItem.rentDays > 0) {
                    exportItem.itemGroup = "rent";
                }
                if (exportItem.tag != ItemTag.none) {
                    exportItem.itemGroup = exportItem.tag.ToString();
                }

                itemGroups.items.Add(exportItem);
                itemGroups.allNames.Add(exportItem.itemName, exportItem);
            }

            var lowerLevel = itemGroups.items.Where(i => i.level > i.minLevel);

            StringBuilder sb = new StringBuilder();
            foreach (var it in lowerLevel) {
                sb.AppendFormat("{0} - {1} ({2})\r\n", it.itemId, it.desc, it.category);
            }

            var groupedLookup = itemGroups.items.ToLookup(it => it.itemGroup, it => it);
            var abyssGroup = groupedLookup.Where(g => g.Key == "a");
            Dictionary<string, ItemRace> worldDrops = new Dictionary<string, ItemRace>()
            {
                { "Soul", ItemRace.ALL }, { "Sky", ItemRace.ALL }, { "Eldritch", ItemRace.ALL },
                { "Grounded", ItemRace.ALL }, { "Angry", ItemRace.ALL }, { "Battlefield", ItemRace.ALL },
                { "Zealous", ItemRace.ALL }, { "Wraith's", ItemRace.ALL }, { "Fascination", ItemRace.ALL },
                { "Eternity", ItemRace.ALL }, { "Blood Seeking", ItemRace.ALL }, { "Divisive", ItemRace.ALL },
                { "Celestial", ItemRace.ALL }, { "Everwatcher's", ItemRace.ALL }, { "Old Guard's", ItemRace.ALL },
                { "Invader's", ItemRace.ALL }, { "Ancient Daeva's", ItemRace.ALL }, { "Carved", ItemRace.ALL },
                { "Anonymous", ItemRace.ALL }, { "Reddened", ItemRace.ALL }, { "White", ItemRace.ALL },
                { "Red Dragon Legion", ItemRace.ALL }, { "Rain", ItemRace.ALL }, { "Immemorial", ItemRace.ALL },
                { "Departed Soul's", ItemRace.ALL }, { "Purehorn's", ItemRace.ALL }, { "Calamity", ItemRace.ALL },
                { "Sleeper's", ItemRace.ALL }, { "Buried", ItemRace.ALL }, { "Immortal", ItemRace.ALL },
                { "Fallen Legionary's", ItemRace.ALL }, { "Nebulous", ItemRace.ALL }, { "Reaper's", ItemRace.ALL },
                { "Odious", ItemRace.ALL }, { "Protector's", ItemRace.ALL }, { "Martyr's", ItemRace.ALL },
                { "Oblate", ItemRace.ALL }, { "Dimensional", ItemRace.ALL }, { "Giant Lord's", ItemRace.ALL },
                { "Ancient Giant's", ItemRace.ALL }, { "Restoration", ItemRace.ALL }, { "Miracle", ItemRace.ALL },
                { "Comet", ItemRace.ALL }, { "Meteor", ItemRace.ALL }, { "Ancient Hero's", ItemRace.ALL },
                { "Giant's", ItemRace.ALL }, { "Elder", ItemRace.ALL }, { "Catastrophic", ItemRace.ALL },
                { "Horrific", ItemRace.ALL }, { "Chaos", ItemRace.ALL }, { "Dolan", ItemRace.ALL }, { "Judgment", ItemRace.ALL }, 
                { "Demolition", ItemRace.ALL }, { "Gracefull", ItemRace.ALL }, { "Death", ItemRace.ALL },
                { "Life", ItemRace.ALL }, { "Destructive", ItemRace.ALL }, { "Storm", ItemRace.ALL }, { "Sage", ItemRace.ALL },
                { "Sunset", ItemRace.ALL }, { "Angel's", ItemRace.ALL }, { "Guarded", ItemRace.ALL }, { "Dispelling", ItemRace.ALL },
                { "Archbishop", ItemRace.ALL }, { "Restless", ItemRace.ALL }, { "Prayer", ItemRace.ALL }, { "Torment", ItemRace.ALL }
            };

            foreach (var abyssSuffix in abyssGroup) {
                foreach (string name in worldDrops.Keys) {
                    var abyssItems = abyssSuffix.Where(a => a.desc.StartsWith(name));
                    foreach (var abyssItem in abyssItems)
                        abyssItem.itemGroup = "random_drop";
                }
            }

            foreach (var entry in groupedLookup) {
                IEnumerable<ItemExport> group = groupedLookup[entry.Key];
                ItemGroups itGroup = new ItemGroups();
                if (entry.Key != null)
                    itGroup.group = entry.Key;
                var suffixLookup = group.ToLookup(it => it.suffix, it => it);
                foreach (var suffixEntry in suffixLookup) {
                    IEnumerable<ItemExport> suffixGroup = suffixLookup[suffixEntry.Key];
                    ItemSuffixes itSuffix = new ItemSuffixes();
                    itSuffix.suffix = suffixEntry.Key;
                    foreach (var item in suffixGroup) {
                        //item.itemGroup = null;
                        //item.suffix = 0;
                        itSuffix.items.Add(item);
                    }
                    itGroup.items.Add(itSuffix);
                }
                itGroup.items = itGroup.items.OrderBy(it => it.suffix).ToList();
                itemGroups.grouped.Add(itGroup);
            }

            itemGroups.items.Clear();
            itemGroups.allNames.Clear();

            using (FileStream stream = new FileStream(Path.Combine(outputPath, "items.xml"),
                                                      FileMode.Create, FileAccess.Write)) {
                using (XmlWriter wr = XmlWriter.Create(stream, saveSettings)) {
                    XmlSerializer ser = new XmlSerializer(typeof(ItemsExportFile));
                    ser.Serialize(wr, itemGroups);
                }
            }
        }

        static string getHexRGB(string clientValue) {
            string[] rgbValues = clientValue.Split(',');
            Color color = Color.FromArgb(Int32.Parse(rgbValues[0]), Int32.Parse(rgbValues[1]), Int32.Parse(rgbValues[2]));
            return color.R.ToString("x2") + color.G.ToString("x2") + color.B.ToString("x2");
        }
    }
}
