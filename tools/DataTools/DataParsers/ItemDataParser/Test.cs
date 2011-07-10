namespace Jamie.Items
{
	using System;
	using System.Collections.Generic;
	using System.Diagnostics;
	using System.Linq;
	using Jamie.ParserBase;

	class TestUtility
	{
		static Dictionary<int, ItemTemplate> items = new Dictionary<int, ItemTemplate>();
		static Dictionary<BonusType, Dictionary<int, List<int>>> itemsByBonus =
			new Dictionary<BonusType, Dictionary<int, List<int>>>();

		public static void TestWorkOrders(ItemTemplates templates) {
			CreateItemData(templates);
			SampleWorkOrdersBonuses();
		}

		#region Data creation functions

		static void CreateItemData(ItemTemplates templates) {
			foreach (var it in templates.TemplateList) {
				items.Add(it.id, it);
				if (it.bonus != null) {
					BonusType bonusType = it.bonus[0].type;

					Dictionary<int, List<int>> map = null;
					if (!itemsByBonus.ContainsKey(bonusType)) {
						map = new Dictionary<int, List<int>>();
						itemsByBonus.Add(bonusType, map);
					} else {
						map = itemsByBonus[bonusType];
					}

					string[] bonusLevels = it.bonus[0].bonusLevel.Split(',');
					for (int i = 0; i < bonusLevels.Length; i++) {
						int level = Int32.Parse(bonusLevels[i]);
						List<int> list = null;
						if (map.ContainsKey(level))
							list = map[level];
						else {
							list = new List<int>();
							map.Add(level, list);
						}
						list.Add(it.id);
					}
				}
			}
		}

		#endregion

		#region Test functions

		static void SampleWorkOrdersBonuses() {
			String[] skills = { "_al_", "_as_", "_co_", "_ha_", "_ta_", "_ws_" };
				foreach (string skill in skills) {
					Debug.Print("------------------------");
					Debug.Print("Testing skill: " + skill);
					Debug.Print("------------------------");
					int skillId = Utility.GetSkillIdFromName(skill) & 0xF;
					for (int playerSkillPoints = 0; playerSkillPoints <= 500; playerSkillPoints += 49) {
						Debug.Print("Player skillpoints: " + playerSkillPoints);
						Debug.Print("------------------------");
						int startLevel = (skillId << 10) | playerSkillPoints;
						int endLevel = (skillId << 10) | (playerSkillPoints + 49);
						var bonuses = GetBonusItems(BonusType.TASK, startLevel, endLevel);
						foreach (int itemId in bonuses) {
							Item item = Utility.ItemIndex.ItemList.Where(i => i.id == itemId).First();
							Debug.Print("{0} ({1}) - {2}", item.id, item.Description, items[itemId].origRace);
						}
					}
			}
		}

		#endregion

		#region Sampling functions

		static List<int> GetBonusItems(BonusType type, int startLevel, int endLevel) {
			List<int> list = new List<int>();
			lock (itemsByBonus) {
				if (!itemsByBonus.ContainsKey(type))
					return list;
				var map = itemsByBonus[type];
				var submap = map.Where(entry => entry.Key >= startLevel && entry.Key < endLevel)
								.Select(entry => entry.Value);
				if (!submap.Any())
					return list;
				foreach (List<int> itemsList in submap)
					list.AddRange(itemsList);
			}
			return list;
		}

		#endregion
	}
}
