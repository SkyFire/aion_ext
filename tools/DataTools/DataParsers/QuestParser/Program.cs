using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading;
using System.Xml;
using System.Xml.Serialization;
using Jamie.ParserBase;
using Jamie.ParserBase.Skills;

namespace Jamie.Quests
{
	class Program
	{
		static readonly string root = AppDomain.CurrentDomain.SetupInformation.ApplicationBase;

		static void Main(string[] args) {

			Utility.WriteExeDetails();
			Console.WriteLine("Loading quests...");
			Utility.LoadQuestFile(root);

			var newStuff = Utility.QuestIndex.QuestList.Where(n => n.nodes.Count > 0)
													   .SelectMany(n => n.nodes);
			var distinctNew = newStuff.Select(n => n.Name).Distinct();

			if (distinctNew.Any()) {
				Console.WriteLine("New elements found in XML which were not coded in. Continue?");
				Console.WriteLine("Elements are:");
				foreach (var nodeName in distinctNew)
					Console.WriteLine("      {0}", nodeName);
				Console.Write("(Y/N): ");
				var input = Console.ReadKey(true);
				if (Char.ToLower(input.KeyChar) == 'n')
					return;
			}

			Console.WriteLine("Loading strings...");
			Utility.LoadStrings(root);
			Console.WriteLine("Loading NPCs...");
			Utility.LoadClientNpcs(root);
			Console.WriteLine("Loading items...");
			Utility.LoadItems(root);
			Console.WriteLine("Loading titles...");
			Utility.LoadTitles(root);

			Console.Write("Parsing... ");
			int top = Console.CursorTop;
			int left = Console.CursorLeft;

			var utility = Utility<Quest>.Instance;
			List<QuestOur> ourList = new List<QuestOur>();
			foreach (Quest quest in Utility.QuestIndex.QuestList) {
				// not enabled 임시 in korean
				if (quest.Description.body == "Temporary" || quest.Description.body == "임시")
					continue;

				if (quest.minlevel_permitted != 99 && quest.minlevel_permitted > 55 &&
					quest.minlevel_permitted != 65)
					continue;

				QuestOur q = new QuestOur();
				q.id = quest.id;

				Console.SetCursorPosition(left, top);
				Console.Write("Q" + q.id);

				q.max_repeat_count = quest.max_repeat_count;
				q.nameId = (quest.Description.id * 2 + 1);
				if (q.nameId > 0)
					q.nameIdSpecified = true;

				q.name = quest.Description.body.TrimEnd();
				q.minlevel_permitted = quest.minlevel_permitted;
                if (quest.minlevel_permitted < quest.maxlevel_permitted)
                    q.maxlevel_permitted = quest.maxlevel_permitted;
                q.repeat_day = (int)quest.quest_repeat_cycle;
				q.cannot_giveup = quest.cannot_giveup;
				q.cannot_share = quest.cannot_share;

				string[] classes = quest.class_permitted.ToUpper().Split(' ');
				string classesParse = String.Join(",", classes);
				Class classEnum = (Class)Enum.Parse(typeof(Class), classesParse);
				if ((classEnum & Class.ALL) != Class.ALL) {
					q.class_permitted = ((ClassOur)classEnum).ToString().Replace(",", String.Empty);
					q.class_permittedSpecified = true;
				}

				string race = quest.race_permitted.ToLower();
				if (race == "pc_light") {
					q.race_permitted = Race.ELYOS;
					q.race_permittedSpecified = true;
				} else if (race == "pc_dark") {
					q.race_permitted = Race.ASMODIANS;
					q.race_permittedSpecified = true;
				}

				string gender = quest.gender_permitted.ToLower();
				if (gender != "all") {
					q.gender_permittedSpecified = true;
					if (gender == "male")
						q.gender_permitted = Gender.MALE;
					else
						q.gender_permitted = Gender.FEMALE;
				}

				var drops = new List<QuestDrop>();
				utility.Export(quest, "drop_item_", drops);
				drops = drops.Where(d => d.item_id != 0 && d.chance > 0).ToList();
				if (drops.Count > 0) {
					// check if multiple NCS
					q.QuestDrops = new List<QuestDrop>();
					foreach (var drop in drops) {
						if (drop.npc_id > 0)
							q.QuestDrops.Add(drop);
						else {
							if (drop.npcIds == null /*&& drop.npc_faction != null*/) {
								// NOT defined in 1.9 but in 2.0 - skip them
								// q.QuestDrops.Add(drop);
								continue;
							}
							foreach (var npcName in drop.npcIds) {
								QuestDrop newDrop = (QuestDrop)drop.Clone();
								int id = Utility.ClientNpcIndex[npcName];
								if (id != -1) {
									newDrop.npc_id = id;
									q.QuestDrops.Add(newDrop);
								}
							}
						}
					}
				}

				q.QuestWorkItems = new List<QuestItemsOur>();
				utility.Export(quest, "quest_work_item", q.QuestWorkItems);
				q.QuestWorkItems = q.QuestWorkItems.Where(d => d.count > 0).ToList();
				if (q.QuestWorkItems.Count == 0)
					q.QuestWorkItems = null;
				else
					q.QuestWorkItemsSpecified = true;

				if (quest.assassin_selectable_reward != null) {
					q.AssassinSelectableRewards = new List<QuestItemsOur>();
					foreach (var r in quest.assassin_selectable_reward) {
						QuestItemsOur our = GetRewardItem(r.assassin_selectable_item);
						if (our != null)
							q.AssassinSelectableRewards.Add(our);
					}
				}

				if (quest.chanter_selectable_reward != null) {
					q.ChanterSelectableRewards = new List<QuestItemsOur>();
					foreach (var r in quest.chanter_selectable_reward) {
						QuestItemsOur our = GetRewardItem(r.chanter_selectable_item);
						if (our != null)
							q.ChanterSelectableRewards.Add(our);
					}
				}

				if (quest.elementalist_selectable_reward != null) {
					q.ElementalistSelectableRewards = new List<QuestItemsOur>();
					foreach (var r in quest.elementalist_selectable_reward) {
						QuestItemsOur our = GetRewardItem(r.elementalist_selectable_item);
						if (our != null)
							q.ElementalistSelectableRewards.Add(our);
					}
				}

				if (quest.fighter_selectable_reward != null) {
					q.FighterSelectableRewards = new List<QuestItemsOur>();
					foreach (var r in quest.fighter_selectable_reward) {
						QuestItemsOur our = GetRewardItem(r.fighter_selectable_item);
						if (our != null)
							q.FighterSelectableRewards.Add(our);
					}
				}

				if (quest.knight_selectable_reward != null) {
					q.KnightSelectableRewards = new List<QuestItemsOur>();
					foreach (var r in quest.knight_selectable_reward) {
						QuestItemsOur our = GetRewardItem(r.knight_selectable_item);
						if (our != null)
							q.KnightSelectableRewards.Add(our);
					}
				}

				if (quest.priest_selectable_reward != null) {
					q.PriestSelectableRewards = new List<QuestItemsOur>();
					foreach (var r in quest.priest_selectable_reward) {
						QuestItemsOur our = GetRewardItem(r.priest_selectable_item);
						if (our != null)
							q.PriestSelectableRewards.Add(our);
					}
				}

				if (quest.ranger_selectable_reward != null) {
					q.RangerSelectableRewards = new List<QuestItemsOur>();
					foreach (var r in quest.ranger_selectable_reward) {
						QuestItemsOur our = GetRewardItem(r.ranger_selectable_item);
						if (our != null)
							q.RangerSelectableRewards.Add(our);
					}
				}

				if (quest.wizard_selectable_reward != null) {
					q.WizardSelectableRewards = new List<QuestItemsOur>();
					foreach (var r in quest.wizard_selectable_reward) {
						QuestItemsOur our = GetRewardItem(r.wizard_selectable_item);
						if (our != null)
							q.WizardSelectableRewards.Add(our);
					}
				}

				q.CollectItems = new List<CollectItem>();
				utility.Export(quest, "collect_item", q.CollectItems);
				q.CollectItems = q.CollectItems.Where(d => d.count > 0).ToList();
				if (q.CollectItems.Count == 0)
					q.CollectItems = null;
				else
					q.CollectItemsSpecified = true;

				q.combine_skillpoint = quest.combine_skillpoint;
				if (q.combine_skillpoint > 0)
					q.combine_skillpointSpecified = true;

				if (quest.combineskill != CombineSkillType.any) {
					q.combineskill = (int)quest.combineskill;
					q.combineskillSpecified = true;
				}

				var questConds = GetConditions(quest, "finished_quest_cond");
				if (questConds.Count > 0)
					q.finished_quest_conds = questConds;

				questConds = GetConditions(quest, "unfinished_quest_cond");
				if (questConds.Count > 0)
					q.unfinished_quest_conds = questConds;

				questConds = GetConditions(quest, "acquired_quest_cond");
				if (questConds.Count > 0)
					q.acquired_quest_conds = questConds;

				questConds = GetConditions(quest, "noacquired_quest_cond");
				if (questConds.Count > 0)
					q.noacquired_quest_conds = questConds;

				q.use_class_reward = quest.use_class_reward;

				var rewards = new List<Rewards>();

				utility.Export(quest, "reward_exp", rewards);
				utility.Export(quest, "reward_gold", rewards);
				utility.Export(quest, "reward_abyss_point", rewards);
				utility.Export(quest, "reward_title", rewards);
				utility.Export(quest, "reward_extend_inventory", rewards);
				utility.Export(quest, "reward_extend_stigma", rewards);
				utility.Export(quest, "reward_item", rewards);
				utility.Export(quest, "selectable_reward_item", rewards);

				rewards = rewards.Where(r => r.BasicRewards != null || r.SelectableRewards != null ||
											 r.exp > 0 || r.gold > 0 || r.reward_abyss_point > 0 ||
											 r.title > 0).ToList();

				if (rewards.Count > 0)
					q.Rewards = rewards;

				Rewards extRewards = null;

				if (quest.reward_gold_ext != 0) {
					extRewards = new Rewards();
					extRewards.gold = quest.reward_gold_ext;
					extRewards.goldSpecified = true;
				}

				if (quest.reward_title_ext != null) {
					int titleId = Utility.TitleIndex[quest.reward_title_ext];
					if (titleId > 0) {
						if (extRewards == null)
							extRewards = new Rewards();
						extRewards.title = titleId;
					}
				}

				rewards = new List<Rewards>();
				utility.Export(quest, "reward_item_ext_", rewards);
				rewards = rewards.Where(r => r.BasicRewards != null).ToList();
				if (rewards.Count > 0) {
					var items = rewards.SelectMany(r => r.BasicRewards);
					if (extRewards == null)
						extRewards = new Rewards();
					extRewards.BasicRewards = new List<QuestItemsOur>();
					extRewards.BasicRewards.AddRange(items);
				}

				rewards = new List<Rewards>();
				utility.Export(quest, "selectable_reward_item_ext_", rewards);
				rewards = rewards.Where(r => r.SelectableRewards != null).ToList();
				if (rewards.Count > 0) {
					var items = rewards.SelectMany(r => r.SelectableRewards);
					if (extRewards == null)
						extRewards = new Rewards();
					extRewards.SelectableRewards = new List<QuestItemsOur>();
					extRewards.SelectableRewards.AddRange(items);
				}

				if (extRewards != null)
					q.ExtRewards = new List<Rewards>() { extRewards };

				ourList.Add(q);
				Thread.Sleep(1);
			}

			quest_data fileData = new quest_data();
			fileData.Quests = ourList;

			var settings = new XmlWriterSettings()
			{
				CheckCharacters = false,
				CloseOutput = false,
				Encoding = new UTF8Encoding(false),
				Indent = true,
				IndentChars = "\t",
			};

			string outputPath = Path.Combine(root, @".\output\");
			if (!Directory.Exists(outputPath))
				Directory.CreateDirectory(outputPath);

			try {
				using (var fs = new FileStream(Path.Combine(outputPath, "quest_data.xml"),
											   FileMode.Create, FileAccess.Write))
				using (var writer = XmlWriter.Create(fs, settings)) {
					XmlSerializer ser = new XmlSerializer(typeof(quest_data));
					ser.Serialize(writer, fileData);
				}
			} catch (Exception ex) {
				Debug.Print(ex.ToString());
			}

			Console.Clear();
			Console.WriteLine("Done.");
			Console.ReadKey();
			Environment.Exit(0);
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

		static List<QuestItemsOur> GetSelectableRewards(Quest quest, string name) {
			List<QuestItemsOur> list = new List<QuestItemsOur>(1);
			Utility<Quest>.Instance.Export(quest, name, list);
			list = list.Where(r => r.count > 0).ToList();
			if (list.Count == 0)
				list = null;
			return list;
		}

		static List<QuestStartCondition> GetConditions(Quest quest, string name) {
			List<String> conds = new List<string>();
			Utility<Quest>.Instance.Export(quest, name, conds);
			List<QuestStartCondition> startConditions = new List<QuestStartCondition>();
			if (conds.Count > 0) {
				foreach (string cond in conds) {
					string[] parseString = cond.Split(new string[] { " ", "," },
													  StringSplitOptions.RemoveEmptyEntries);
					var finalConds = new Dictionary<int, QuestStep>();
					var condition = new QuestStartCondition();
					condition.questSteps = new List<QuestStep>();
					foreach (string c in parseString) {
						string[] condData = c.Split('_');
						string questIdStep = (condData.Length > 1 ? condData[1] : condData[0]).TrimStart('q', 'Q');
						condData = questIdStep.Split(':');
						int questId = Int32.Parse(condData[0]);
						int rewardNo = 0;
						if (condData.Length > 1)
							rewardNo = Int32.Parse(condData[1]);
						QuestStep qs = new QuestStep(questId, rewardNo);
						if (finalConds.ContainsKey(questId)) {
							QuestStep qsOld = finalConds[questId];
						} else {
							finalConds.Add(questId, qs);
						}
					}
					condition.questSteps = finalConds.OrderBy(p => p.Key).Select(p => p.Value).ToList();
					startConditions.Add(condition);
				}
			}
			return startConditions;
		}
	}
}
