namespace Jamie.ParserBase
{
	using System;
	using System.Collections.Generic;
	using System.Diagnostics;
	using System.IO;
	using System.Linq;
	using System.Text;
	using System.Xml;
	using System.Xml.Serialization;
	using Jamie.Quests;
	using Jamie.Skills;
	using Jamie.Trade;
	using JustAgile.Html.Linq;
	using System.Runtime.Serialization.Formatters.Binary;
	using System.Reflection;
	using Jamie.Items;
    using Jamie.ParserBase.Skills;
	using Jamie.Npcs;
	using Jamie.Drops;

	public class Utility
	{
		public static ClientStringsFile StringIndex = null;
		public static ClientSkillFile SkillIndex = null;
		public static ClientItemsFile ItemIndex = null;
		public static ClientNpcsFile ClientNpcIndex = null;
		public static ClientUltraSkillFile UltraSkillIndex = null;
		public static QuestsFile QuestIndex = null;
		public static DialogDictionary DialogFiles = null;
		public static ClientGoodlistsFile GoodListIndex = null;
		public static TradeListFile OriginalTradeList = null;
		public static ClientRecipesFile RecipeIndex = null;
		public static ClientTitlesFile TitleIndex = null;
		public static ItemTemplates OriginalItemTemplate = null;
        public static SkillsLearns SkillLearnIndex = null;
		public static NpcTemplates OriginalNpcTemplate = null;
        public static NpcTemplates ExternalNpcTemplate = null;
        public static Droplist DropListTemplate = null;
        public static ClientMissionFile MissionFile = null;
        public static ClientGatherSrc GatherSrcFile = null;
        public static SpawnsFile SpawnOrigFile = null;
        public static ClientCosmetics CosmeticsIndex = null;
        public static ClientPresetFile Presets = null;
		public static ClientToypetFeed PetFeed = null;

		public static void WriteExeDetails() {
			var asm = Assembly.GetEntryAssembly();
			var descr = asm.GetCustomAttributes(typeof(AssemblyTitleAttribute), false)[0]
				as AssemblyTitleAttribute;
			var copy = asm.GetCustomAttributes(typeof(AssemblyCopyrightAttribute), false)[0]
				as AssemblyCopyrightAttribute;

			string description = String.Empty;
			string copyright = String.Empty;
			string versionNo = asm.GetName().Version.ToString();

			if (descr != null) {
				description = descr.Title;
			}

			if (copy != null) {
				copyright = copy.Copyright.Replace("©", "(c)");
			}

			Console.WriteLine(String.Format("{0}. {1}.", description, copyright));
			Console.WriteLine(String.Format("Version: {0}", versionNo));
			Console.WriteLine();
		}

		public static void LoadStrings(string rootPath) {
			try {
				using (var fs = new FileStream(Path.Combine(rootPath, @".\data\client_strings.xml"),
											   FileMode.Open, FileAccess.Read))
				using (var reader = XmlReader.Create(fs)) {
					XmlSerializer ser = new XmlSerializer(typeof(ClientStringsFile));
					StringIndex = (ClientStringsFile)ser.Deserialize(reader);
					StringIndex.CreateIndex();
				}
			} catch (Exception ex) {
				Debug.Print(ex.ToString());
			}
		}

		public static void LoadTradeListTemplates(string root) {
			try {
				using (var fs = new FileStream(Path.Combine(root, @".\data\npc_trade_list.xml"),
											   FileMode.Open, FileAccess.Read))
				using (var reader = XmlReader.Create(fs)) {
					XmlSerializer ser = new XmlSerializer(typeof(TradeListFile));
					OriginalTradeList = (TradeListFile)ser.Deserialize(reader);
				}
			} catch (Exception ex) {
				Debug.Print(ex.ToString());
			}
		}

		public static void LoadClientNpcs(string root) {
			try {
				using (var fs = new FileStream(Path.Combine(root, @".\data\client_npcs.xml"),
											   FileMode.Open, FileAccess.Read))
				using (var reader = XmlReader.Create(fs)) {
					XmlSerializer ser = new XmlSerializer(typeof(ClientNpcsFile));
					ser.UnknownElement += new XmlElementEventHandler(delegate(object sender, XmlElementEventArgs e)
					{
						Debug.Print("Unknown element: '{0}' (line: {1})", e.Element.Name, e.LineNumber);
					});
					ClientNpcIndex = (ClientNpcsFile)ser.Deserialize(reader);
					ClientNpcIndex.CreateIndex();
				}
			} catch (Exception ex) {
				Debug.Print(ex.ToString());
			}
		}

		public static void LoadNpcsTemplate(string root) {
			try {
				using (var fs = new FileStream(Path.Combine(root, @".\data\npc_templates.xml"),
											   FileMode.Open, FileAccess.Read))
				using (var reader = XmlReader.Create(fs)) {
					XmlSerializer ser = new XmlSerializer(typeof(NpcTemplates));
					OriginalNpcTemplate = (NpcTemplates)ser.Deserialize(reader);
                    OriginalNpcTemplate.CreateIndex();
				}
			} catch (Exception ex) {
				Debug.Print(ex.ToString());
			}
		}

        public static void LoadExternalNpcsTemplate(string root) {
            try {
                using (var fs = new FileStream(Path.Combine(root, @".\data\npc_templates_ext.xml"),
                                               FileMode.Open, FileAccess.Read))
                using (var reader = XmlReader.Create(fs)) {
                    XmlSerializer ser = new XmlSerializer(typeof(NpcTemplates));
                    ExternalNpcTemplate = (NpcTemplates)ser.Deserialize(reader);
                    ExternalNpcTemplate.CreateIndex();
                }
            } catch (Exception ex) {
                Debug.Print(ex.ToString());
            }
        }

        public static void LoadClientGatherables(string root) {
            try {
                using (var fs = new FileStream(Path.Combine(root, @".\data\gather_src.xml"),
                                               FileMode.Open, FileAccess.Read))
                using (var reader = XmlReader.Create(fs)) {
                    XmlSerializer ser = new XmlSerializer(typeof(ClientGatherSrc));
					ser.UnknownElement += new XmlElementEventHandler(delegate(object sender, XmlElementEventArgs e)
					{
						Debug.Print("Unknown element: '{0}' (line: {1})", e.Element.Name, e.LineNumber);
					});
                    GatherSrcFile = (ClientGatherSrc)ser.Deserialize(reader);
                    GatherSrcFile.CreateIndex();
                }
            } catch (Exception ex) {
                Debug.Print(ex.ToString());
            }
        }

        public static void LoadOurSpawns(string file, bool gatherables) {
            try {
                using (var fs = new FileStream(file, FileMode.Open, FileAccess.Read))
                using (var reader = XmlReader.Create(fs)) {
                    XmlSerializer ser = new XmlSerializer(typeof(SpawnsFile));
                    SpawnOrigFile = (SpawnsFile)ser.Deserialize(reader);
                    if (gatherables && GatherSrcFile != null) {
                        foreach (var spawnGroup in SpawnOrigFile.Spawns) {
                            var existing = GatherSrcFile.Items.Where(i => i.id == spawnGroup.npcid)
                                                              .FirstOrDefault();
                            if (existing == null)
                                continue;
                            spawnGroup.name = Utility.StringIndex.GetString(existing.desc);
                            spawnGroup.level = existing.skill_level;
                        }
                    } else if (!gatherables && ClientNpcIndex != null) {
                        foreach (var spawnGroup in SpawnOrigFile.Spawns) {
                            var existing = ClientNpcIndex[spawnGroup.npcid];
                            if (existing == null)
                                continue;
                            spawnGroup.name = Utility.StringIndex.GetString(existing.desc);
                        }
                    }
                }
            } catch (Exception ex) {
                Debug.Print(ex.ToString());
            }
        }

        public static void LoadClientCosmetics(string root) {
            try {
                using (var fs = new FileStream(Path.Combine(root, @".\data\client_cosmetic_item_info.xml"),
                                               FileMode.Open, FileAccess.Read))
                using (var reader = XmlReader.Create(fs)) {
                    XmlSerializer ser = new XmlSerializer(typeof(ClientCosmetics));
					ser.UnknownElement += new XmlElementEventHandler(delegate(object sender, XmlElementEventArgs e)
					{
						Debug.Print("Unknown element: '{0}' (line: {1})", e.Element.Name, e.LineNumber);
					});
                    CosmeticsIndex = (ClientCosmetics)ser.Deserialize(reader);
                    CosmeticsIndex.CreateIndex();
                }
            } catch (Exception ex) {
                Debug.Print(ex.ToString());
            }
        }

		static int recurse = 0;

		public static IEnumerable<string> GetVarStrings(string original) {
			IList<string> vars = new List<string>();
			GetParsedString(original, ref vars, true);
			return vars;
		}

		public static string GetParsedString(string original, bool shortDescr) {
			IList<string> vars = new List<string>();
			return GetParsedString(original, ref vars, shortDescr);
		}

		static string GetParsedString(string original, ref IList<string> vars, bool shortDescr) {
			if (String.IsNullOrEmpty(original))
				return String.Empty;

			if (recurse > 15) {
				Debug.Print("Recursion too deep. Aborting parse");
				return original;
			}

			recurse++;

			StringBuilder sb = new StringBuilder();
			string varName = String.Empty;
			bool varStart = false;

			for (int i = 0; i < original.Length; i++) {
				if (shortDescr && original[i] == ';') {
					recurse--;
					return sb.ToString();
				}
				if (!varStart && original[i] == '[' &&
					i < original.Length - 2 && original[i + 1] == '%') {
					varStart = true;
					i++;
				} else if (varStart && original[i] == ']') {
					vars.Add(varName);
					if (varName.StartsWith("dic:")) {
						varName = varName.Remove(0, 4);
						int id = StringIndex[varName];
						if (id != -1) {
							var descr = StringIndex.GetStringDescription(id);
							if (vars.Contains(varName)) {
								varName = String.Empty;
								sb.Append(GetParsedString(descr.body, ref vars, true));
								i++; // skip
							} else {
								sb.Append(GetParsedString(descr.body, ref vars, shortDescr));
							}
						} else {
							Debug.Print("String not found: {0}", varName);
						}
						if (i < original.Length - 2 && original[i + 1] == '%') {
							i++; // skip percent after the var
						}
					} else {
						sb.Append(String.Format("[%{0}]", varName));
					}
					varName = String.Empty;
					varStart = false;
				} else if (varStart) {
					varName += original[i];
				} else {
					sb.Append(original[i]);
				}
			}

			recurse--;
			return sb.ToString();
		}

		/// <summary>
		/// Returns -1 if couldn't be parsed
		/// </summary>
		public static int GetSkillLevelFromName(string name, out string nameWithoutLevel) {
			string fullName = name.ToLower();
			if (fullName.StartsWith("str_"))
				fullName = fullName.Remove(0, 4);
			string[] nameParts = fullName.Split('_', '.');
			string levelPart = nameParts.Where(s => s.Length > 1 && s[0] == 'g' && Char.IsDigit(s[1]))
										.FirstOrDefault();
			int level = -1;
			nameWithoutLevel = null;
			if (levelPart == null) {
				nameWithoutLevel = fullName;
				return level;
			}

			int lvlIndex = Array.IndexOf(nameParts, levelPart);
			string[] partsWithoutLevel = new string[nameParts.Length - 1];
			Array.Copy(nameParts, partsWithoutLevel, lvlIndex);
			Array.Copy(nameParts, lvlIndex + 1, partsWithoutLevel, lvlIndex,
					   partsWithoutLevel.Length - lvlIndex);
			nameWithoutLevel = String.Join("_", partsWithoutLevel);

			levelPart = levelPart.Remove(0, 1);
			Int32.TryParse(levelPart, out level);
			return level;
		}

		public static int GetLevelFromName(string name) {
			if (String.IsNullOrEmpty(name))
				return 0;
			int result = 0;
			char[] chars = name.ToCharArray();
			char lastChar = Char.ToLower(chars.Last());
			if (Char.IsLetter(lastChar)) {
				if (lastChar < 'a' || lastChar > 'z') {
					Console.WriteLine("Hm... suffix '{0}' is not valid", name);
				} else if (chars.Length == 1) {
					result = (lastChar - '`');
				} else if (Char.IsDigit(chars[0])) {
					int start = name.Length - 4;
					if (start < 0)
						start = 0;
					else if (chars[start] == '_')
						start++;
					string lvl = name.Substring(start, name.Length - start - 1);
					if (!Int32.TryParse(lvl, out result))
						return 0;
					result <<= 7;
					result |= (lastChar - '`');
				} else {
					int pos = name.LastIndexOf('_');
					if (pos != -1) {
						string lvl = name.Substring(pos + 1, name.Length - pos - 2);
						if (!Int32.TryParse(lvl, out result))
							return 0;
						result <<= 7;
						result |= (lastChar - '`');
					}
				}
			} else {
				string lvl = String.Empty;
                if (name.Length < 2)
                    lvl = name;
                else {
                    int i = 0;
                    while (i < chars.Length && Char.IsLetter(chars[i]))
                        i++;
                    lvl = name.Substring(i, name.Length - i);
                }
				if (!Int32.TryParse(lvl, out result))
					return 0;
			}
			return result;
		}

        public static int GetSkillIdFromName(string name) {
            if (name.Contains("_al_") || name == "alchemy")
                return (int)CombineSkillType.alchemy;
            else if (name.Contains("_co_") || name == "cooking")
                return (int)CombineSkillType.cooking;
            else if (name.Contains("_ha_") || name == "handiwork")
                return (int)CombineSkillType.handiwork;
            else if (name.Contains("_ws_") || name == "weaponsmith")
                return (int)CombineSkillType.weaponsmith;
            else if (name.Contains("_as_") || name == "armorsmith")
                return (int)CombineSkillType.armorsmith;
            else if (name.Contains("_ta_") || name == "tailoring")
                return (int)CombineSkillType.tailoring;
            else if (name == "convert")
                return (int)CombineSkillType.convert;
            return 0;
        }

		public static void LoadSkills(string root) {
			try {
				using (var fs = new FileStream(Path.Combine(root, @".\data\client_skills.xml"),
											   FileMode.Open, FileAccess.Read))
				using (var reader = XmlReader.Create(fs)) {
					XmlSerializer ser = new XmlSerializer(typeof(ClientSkillFile));
                    ser.UnknownElement += new XmlElementEventHandler(delegate(object sender, XmlElementEventArgs e)
                    {
                        Debug.Print("Unknown element: '{0}' (line: {1})", e.Element.Name, e.LineNumber);
                    });
					SkillIndex = (ClientSkillFile)ser.Deserialize(reader);
					SkillIndex.CreateIndex();
				}
			} catch (Exception ex) {
				Debug.Print(ex.ToString());
			}
		}

		public static int GetAttributeValue(string attribute) {
			string[] parts = attribute.Split(new string[] { " " }, StringSplitOptions.RemoveEmptyEntries);
			return Int32.Parse(parts[1].TrimEnd('%'));
		}

		public static string GetAttributeName(string attribute) {
			string[] parts = attribute.Split(new string[] { " " }, StringSplitOptions.RemoveEmptyEntries);
			return parts[0];
		}

		public static void LoadItems(string root) {
			try {
				using (var fs = new FileStream(Path.Combine(root, @".\data\client_items.xml"),
											   FileMode.Open, FileAccess.Read))
				using (var reader = XmlReader.Create(fs)) {
					XmlSerializer ser = new XmlSerializer(typeof(ClientItemsFile));
                    ser.UnknownElement += new XmlElementEventHandler(delegate(object sender, XmlElementEventArgs e)
                    {
                        Debug.Print("Unknown element: '{0}' (line: {1})", e.Element.Name, e.LineNumber);
                    });
					ItemIndex = (ClientItemsFile)ser.Deserialize(reader);
					ItemIndex.CreateIndex();
				}
			} catch (Exception ex) {
				Debug.Print(ex.ToString());
			}
		}

		public static weaponType GetWeaponType(string reserved5) {
			reserved5 = reserved5.Trim().ToLower();
			if (reserved5 == "1h_sword")
				return weaponType.SWORD_1H;
			else if (reserved5 == "2h_sword")
				return weaponType.SWORD_2H;
			else if (reserved5 == "1h_dagger")
				return weaponType.DAGGER_1H;
			else if (reserved5 == "1h_mace")
				return weaponType.MACE_1H;
			else if (reserved5 == "2h_polearm")
				return weaponType.POLEARM_2H;
			else if (reserved5 == "bow")
				return weaponType.BOW;
			else if (reserved5 == "2h_staff")
				return weaponType.STAFF_2H;
			else if (reserved5 == "2h_book")
				return weaponType.BOOK_2H;
			else if (reserved5 == "2h_orb")
				return weaponType.ORB_2H;
			else
				return weaponType.NONE;
		}

		public static armorType GetArmorType(string reserved5) {
			reserved5 = reserved5.Trim();
			return (armorType)Enum.Parse(typeof(armorType), reserved5, true);
		}

		public static modifiersenum GetStat(Type effectType, string reservedValue) {
			if (effectType.Equals(typeof(WeaponMasteryEffect)))
				return modifiersenum.PHYSICAL_ATTACK;
			else if (effectType.Equals(typeof(ArmorMasteryEffect)))
				return modifiersenum.PHYSICAL_DEFENSE;
			else if (effectType.Equals(typeof(BoostHealEffect)))
				return modifiersenum.BOOST_HEAL;
			else if (effectType.Equals(typeof(BoostHateEffect)))
				return modifiersenum.BOOST_HATE;

			if (reservedValue == null)
				return modifiersenum.NONE;

			try {
				Stat stat = (Stat)Enum.Parse(typeof(Stat), reservedValue, true);
				return new NamedEnum<modifiersenum>(stat.ToString());
			} catch {
				return modifiersenum.NONE;
			}
		}

		public static void LoadUltraSkills(string root) {
			try {
				using (var fs = new FileStream(Path.Combine(root, @".\data\client_ultra_skills.xml"),
											   FileMode.Open, FileAccess.Read))
				using (var reader = XmlReader.Create(fs)) {
					XmlSerializer ser = new XmlSerializer(typeof(ClientUltraSkillFile));
					ser.UnknownElement += new XmlElementEventHandler(delegate(object sender, XmlElementEventArgs e)
					{
						Debug.Print("Unknown element: '{0}' (line: {1})", e.Element.Name, e.LineNumber);
					});
					UltraSkillIndex = (ClientUltraSkillFile)ser.Deserialize(reader);
				}
			} catch (Exception ex) {
				Debug.Print(ex.ToString());
			}
		}

       public static void LoadSkillLearns(string root) {
            try {
                using (var fs = new FileStream(Path.Combine(root, @".\data\client_skill_learns.xml"),
                                               FileMode.Open, FileAccess.Read))
                using (var reader = XmlReader.Create(fs)) {
                    XmlSerializer ser = new XmlSerializer(typeof(SkillsLearns));
					ser.UnknownElement += new XmlElementEventHandler(delegate(object sender, XmlElementEventArgs e)
					{
						Debug.Print("Unknown element: '{0}' (line: {1})", e.Element.Name, e.LineNumber);
					});
                    SkillLearnIndex = (SkillsLearns)ser.Deserialize(reader);
                    SkillLearnIndex.CreateIndex();
                }
            } catch (Exception ex) {
                Debug.Print(ex.ToString());
            }
        }

		public static void LoadQuestFile(string rootPath) {
			var readSettings = new XmlReaderSettings()
			{
				ProhibitDtd = false,
				CheckCharacters = false,
				CloseInput = false
			};
			try {
				using (var fs = new FileStream(Path.Combine(rootPath, @".\data\quest.xml"),
											   FileMode.Open, FileAccess.Read))
				using (var reader = XmlReader.Create(fs, readSettings)) {
					XmlSerializer ser = new XmlSerializer(typeof(QuestsFile));
					ser.UnknownElement += new XmlElementEventHandler(delegate(object sender, XmlElementEventArgs e)
					{
						Debug.Print("Unknown element: '{0}' (line: {1})", e.Element.Name, e.LineNumber);
					});
					QuestIndex = (QuestsFile)ser.Deserialize(reader);
				}
			} catch (Exception ex) {
				Debug.Print(ex.ToString());
			}
		}

		public static void LoadNpcGoodLists(string root) {
			try {
				using (var fs = new FileStream(Path.Combine(root, @".\data\client_npc_goodslist.xml"),
											   FileMode.Open, FileAccess.Read))
				using (var reader = XmlReader.Create(fs)) {
					XmlSerializer ser = new XmlSerializer(typeof(ClientGoodlistsFile));
					ser.UnknownElement += new XmlElementEventHandler(delegate(object sender, XmlElementEventArgs e)
					{
						Debug.Print("Unknown element: '{0}' (line: {1})", e.Element.Name, e.LineNumber);
					});
					GoodListIndex = (ClientGoodlistsFile)ser.Deserialize(reader);
				}
			} catch (Exception ex) {
				Debug.Print(ex.ToString());
			}
		}

		public static void LoadCombinedRecipes(string root) {
			try {
				using (var fs = new FileStream(Path.Combine(root, @".\data\client_combine_recipe.xml"),
											   FileMode.Open, FileAccess.Read))
				using (var reader = XmlReader.Create(fs)) {
					XmlSerializer ser = new XmlSerializer(typeof(ClientRecipesFile));
					ser.UnknownElement += new XmlElementEventHandler(delegate(object sender, XmlElementEventArgs e)
					{
						Debug.Print("Unknown element: '{0}' (line: {1})", e.Element.Name, e.LineNumber);
					});
					RecipeIndex = (ClientRecipesFile)ser.Deserialize(reader);
					RecipeIndex.CreateIndex();
				}
			} catch (Exception ex) {
				Debug.Print(ex.ToString());
			}
		}

		public static void LoadTitles(string root) {
			try {
				using (var fs = new FileStream(Path.Combine(root, @".\data\client_titles.xml"),
											   FileMode.Open, FileAccess.Read))
				using (var reader = XmlReader.Create(fs)) {
					XmlSerializer ser = new XmlSerializer(typeof(ClientTitlesFile));
					ser.UnknownElement += new XmlElementEventHandler(delegate(object sender, XmlElementEventArgs e)
					{
						Debug.Print("Unknown element: '{0}' (line: {1})", e.Element.Name, e.LineNumber);
					});
					TitleIndex = (ClientTitlesFile)ser.Deserialize(reader);
					TitleIndex.CreateIndex();
				}
			} catch (Exception ex) {
				Debug.Print(ex.ToString());
			}
		}

        public static void LoadDroplist(string rootPath) {
            try {
                using (var fs = new FileStream(Path.Combine(rootPath, @".\data\droplist.xml"),
                                               FileMode.Open, FileAccess.Read))
                using (var reader = XmlReader.Create(fs)) {
                    XmlSerializer ser = new XmlSerializer(typeof(Droplist));
                    DropListTemplate = (Droplist)ser.Deserialize(reader);
                }
            } catch (Exception ex) {
                Debug.Print(ex.ToString());
            }
        }

        public static void LoadClientPreset(string filePath) {
            try {
                using (var fs = new FileStream(filePath, FileMode.Open, FileAccess.Read))
                using (var reader = XmlReader.Create(fs)) {
                    XmlSerializer ser = new XmlSerializer(typeof(ClientPresetFile));
                    Presets = (ClientPresetFile)ser.Deserialize(reader);
                }
            } catch (Exception ex) {
                Debug.Print(ex.ToString());
            }
        }

		public static void LoadClientPetFeed(string rootPath) {
			try {
				using (var fs = new FileStream(Path.Combine(rootPath, @".\data\toypet_feed.xml"),
											   FileMode.Open, FileAccess.Read))
				using (var reader = XmlReader.Create(fs)) {
					XmlSerializer ser = new XmlSerializer(typeof(ClientToypetFeed));
					PetFeed = (ClientToypetFeed)ser.Deserialize(reader);
				}
			} catch (Exception ex) {
				Debug.Print(ex.ToString());
			}
		}

        public static void LoadMissionFile(string path) {
            try {
                using (var fs = new FileStream(path, FileMode.Open, FileAccess.Read))
                using (var reader = XmlReader.Create(fs)) {
                    XmlSerializer ser = new XmlSerializer(typeof(ClientMissionFile));
                    ser.UnknownAttribute += new XmlAttributeEventHandler(delegate(object sender, XmlAttributeEventArgs e)
                    {
                        Debug.Print("Unknown attribute: '{0}' (file: {1}; line: {2})", e.Attr.Name, path, e.LineNumber);
                    });
                    ser.UnknownElement += new XmlElementEventHandler(delegate(object sender, XmlElementEventArgs e)
                    {
                        //Debug.Print("Unknown element: '{0}' (file: {1}; line: {2})", e.Element.Name, path, e.LineNumber);
                    });
                    MissionFile = (ClientMissionFile)ser.Deserialize(reader);
                }
            } catch (Exception ex) {
                Debug.Print(ex.ToString());
            }
        }

		public static void LoadHtmlDialogs(string root) {
			string serializedPath = Path.Combine(root, @".\dialogs\dialogs.dat");
			if (File.Exists(serializedPath)) {
				try {
					using (FileStream fs = new FileStream(serializedPath, FileMode.Open)) {
						BinaryFormatter bf = new BinaryFormatter();
						DialogFiles = (DialogDictionary)bf.Deserialize(fs);
					}
				} catch (Exception ex) {
					Debug.Print(ex.ToString());
				}
			}

			if (DialogFiles == null) {
				DialogFiles = new DialogDictionary();
			} else {
				return;
			}

			List<string> files = new List<string>();
			files.AddRange(Directory.GetFiles(Path.Combine(root, @"dialogs"), @"*.html",
											  SearchOption.AllDirectories));

			for (int i = 0; i < files.Count; i++) {
				string name = Path.GetFileName(files[i]);
				if (name.ToLower().StartsWith("quest_"))
					continue;
				string qId = Path.GetFileNameWithoutExtension(Path.GetFileName(files[i])).Trim();
				DirectoryInfo info = new DirectoryInfo(files[i]);
				while (info.Parent.Name.ToLower() != "dialogs") {
					qId += '|' + info.Parent.Name;
					info = info.Parent;
				}
				if (!DialogFiles.ContainsKey(qId)) {
					Dialogs dialogs;
					if (TryLoadQuestHtml(files[i], out dialogs))
						DialogFiles.Add(qId, dialogs);
				}
			}

			try {
				using (FileStream fs = new FileStream(serializedPath, FileMode.Create)) {
					BinaryFormatter bf = new BinaryFormatter();
					bf.Serialize(fs, DialogFiles);
				}
			} catch (Exception ex) {
				Debug.Print(ex.ToString());
			}
		}

		public static bool TryLoadQuestHtml(string filePath, out Dialogs dialogs) {
			string html = String.Empty;
			using (var stream = new StreamReader(filePath, Encoding.UTF8)) {
				var doc = HDocument.Load(stream);
				html = doc.ToString();
			}

			dialogs = null;
			using (var ms = new MemoryStream(Encoding.UTF8.GetBytes(html.ToString().ToCharArray()))) {
				try {
					XmlSerializer ser = new XmlSerializer(typeof(Dialogs));
					dialogs = (Dialogs)ser.Deserialize(ms);
					dialogs.fileName = Path.GetFileName(filePath);
				} catch (Exception ex) {
					Debug.Print(ex.ToString());
				}
			}
			return dialogs != null;
		}

		public static IEnumerable<string> GetNpcIdsFromDescription(string original) {
			IEnumerable<string> vars = GetVarStrings(original);
			List<string> ids = new List<string>();
			foreach (string var in vars) {
				if (!var.StartsWith("dic:"))
					continue;
				string name = GetParsedString(String.Format("[%{0}]", var), true);
				if (!String.IsNullOrEmpty(name)) {
					var descr = StringIndex.StringList.Where(s => s.body == name).FirstOrDefault();
					if (descr != null) {
						var possibleNpcs = ClientNpcIndex.NpcList.Where(n => n.desc == descr.name);
						foreach (var npc in possibleNpcs) {
							ids.Add(npc.id.ToString());
						}
					}
				}
			}
			return ids;
		}

		public static void CreateSkillMap(string root) {
			Console.WriteLine("Creating mapping rules...");

			var utility = Utility<Item>.Instance;
			SkillMap map = new SkillMap();

			foreach (var our in Utility.OriginalItemTemplate.TemplateList) {
				var clientMatch = Utility.ItemIndex.ItemList.Where(i => i.id == our.id).FirstOrDefault();
				if (clientMatch == null || our.modifiers == null)
					continue;

				List<string> bonusAttrs = new List<string>();
				utility.Export(clientMatch, "bonus_attr", bonusAttrs);

				int modifierCount = 0;
				if (clientMatch.min_damage > 0 || clientMatch.max_damage > 0)
					modifierCount++;
				if (clientMatch.min_damage > 0)
					modifierCount++;
				if (clientMatch.max_damage > 0)
					modifierCount++;
				if (clientMatch.hit_accuracy > 0)
					modifierCount++;
				if (clientMatch.magical_hit_accuracy > 0)
					modifierCount++;
				if (clientMatch.parry > 0)
					modifierCount++;
				if (clientMatch.critical > 0)
					modifierCount++;
				if (clientMatch.attack_delay > 0)
					modifierCount++;
				if (clientMatch.attack_range > 0)
					modifierCount++;
				if (clientMatch.hit_count > 0)
					modifierCount++;
				if (our.modifiers[0].modifierList.Count != modifierCount + bonusAttrs.Count)
					continue;

				bool allMatched = true;
				var newMappings = new Dictionary<string, SkillMapEntry>();
				var bonuses = our.modifiers[0].modifierList.Where(m => m.bonus).ToList();
				var clientBonuses = from bonus in bonusAttrs
									let name = GetAttributeName(bonus)
									let value = GetAttributeValue(bonus)
									select new { Name = name, Value = value };

				foreach (var clientBonus in clientBonuses) {
					Type type = default(Type);
					int value = 0;
					Modifier ourModifier = null;
					if (map.AllMappings.ContainsKey(clientBonus.Name)) {
						ourModifier = bonuses.Where(b => b.name.ToString() == map.AllMappings[clientBonus.Name].to)
											 .FirstOrDefault();
						if (ourModifier == null) {
							continue;
						}
						if (ourModifier is AddModifier) {
							AddModifier addMod = (AddModifier)ourModifier;
							value = addMod.value;
							type = typeof(AddModifier);
						} else if (ourModifier is SubModifier) {
							SubModifier subMod = (SubModifier)ourModifier;
							value = subMod.value;
							type = typeof(SubModifier);
						} else if (ourModifier is RateModifier) {
							RateModifier rateMod = (RateModifier)ourModifier;
							value = rateMod.value;
							type = typeof(RateModifier);
						} else if (ourModifier is SetModifier) {
							SetModifier setMod = (SetModifier)ourModifier;
							value = setMod.value;
							type = typeof(SetModifier);
						} else {
							// not implemented
							allMatched = false;
							break;
						}
						if (Math.Abs(value) != Math.Abs(clientBonus.Value)) {
							allMatched = false;
							break;
						}
						bonuses.Remove(ourModifier); // exclude from search
					} else {
						foreach (Modifier mod in bonuses) {
							if (mod is AddModifier) {
								AddModifier addMod = (AddModifier)mod;
								value = addMod.value;
								type = typeof(AddModifier);
							} else if (mod is SubModifier) {
								SubModifier subMod = (SubModifier)mod;
								value = subMod.value;
								type = typeof(SubModifier);
							} else if (mod is RateModifier) {
								RateModifier rateMod = (RateModifier)mod;
								value = rateMod.value;
								type = typeof(RateModifier);
							} else if (mod is SetModifier) {
								SetModifier setMod = (SetModifier)mod;
								value = setMod.value;
								type = typeof(SetModifier);
							} else {
								// not implemented
								allMatched = false;
								break;
							}
							if (Math.Abs(value) == Math.Abs(clientBonus.Value)) {
								ourModifier = mod;
								break;
							}
						}
						if (ourModifier == null) { // not found the same value
							allMatched = false;
							break;
						}
						bonuses.Remove(ourModifier);
						newMappings.Add(clientBonus.Name, new SkillMapEntry()
						{
							from = clientBonus.Name,
							to = ourModifier.name.ToString(),
							ModifierType = type,
							IsNegative = value != clientBonus.Value,
						});
					}
				}

				if (allMatched) {
					foreach (var pair in newMappings)
						map.AllMappings.Add(pair.Key, pair.Value);
				}
			}
			map.SaveToFile(root);
		}
    }
}
