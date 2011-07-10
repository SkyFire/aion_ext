namespace Jamie.Items
{
	using System.Collections.Generic;
	using Jamie.ParserBase;

	class AggregatedTaskItem
	{
		public int SkillId { get; private set; }
		public int SkillPoints { get; private set; }
		public int ItemId { get; private set; }
		public ItemRace Race { get; private set; }
		public bool IsRecipe { get; private set; }

		public AggregatedTaskItem(RecipeItem rcItem, ItemRace race, string skill, int skillPoints) {
			this.ItemId = rcItem.id;
			this.SkillId = (Utility.GetSkillIdFromName("_" + skill + "_") & 0xF);
			this.SkillPoints = skillPoints;
			this.Race = race;
			this.IsRecipe = rcItem.isRecipe;
		}

		public static IEnumerable<AggregatedTaskItem> CreateList(IEnumerable<RecipeItem> rcItems, ItemRace race, string skill, int skillPoints) {
			foreach (var rcItem in rcItems)
				yield return new AggregatedTaskItem(rcItem, race, skill, skillPoints);
		}
	}
}
