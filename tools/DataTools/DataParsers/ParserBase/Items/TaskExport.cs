namespace Jamie.Items
{
	using System;
	using System.Collections.Generic;
	using System.Linq;
	using System.Text;
	using System.Xml.Serialization;
	using System.ComponentModel;

	[Serializable]
	[XmlType(AnonymousType = true)]
	[XmlRoot(ElementName = "task_items", Namespace = "", IsNullable = false)]
	public class TaskExport
	{
		[XmlElement("race")]
		public List<RaceTask> RaceTasks = new List<RaceTask>() { new RaceTask() { Race = ItemRace.ELYOS }, 
																 new RaceTask() { Race = ItemRace.ASMODIANS } };
	}

	[Serializable]
	[XmlType(AnonymousType = true)]
	public class RaceTask
	{
		[XmlAttribute("name")]
		public ItemRace Race;

		[XmlElement("task")]
		public List<Task> Tasks = new List<Task>();

		public Task this[int skillPoints, string skill] {
			get {
				Task task = Tasks.Where(t => t.skillpoints == skillPoints && t.skill == skill).FirstOrDefault();
				if (task == null) {
					task = new Task() { skillpoints = skillPoints, skill = skill, 
										Items = new List<RecipeItem>(), race = Race };
					Tasks.Add(task);
				}
				return task;
			}
		}
	}

	[Serializable]
	[XmlType(AnonymousType = true)]
	public class Task
	{
		[XmlAttribute]
		public int skillpoints;

		[XmlAttribute]
		public string skill;

		[XmlElement("item")]
		public List<RecipeItem> Items;

		public bool ContainsItem(int itemId) {
			return Items.Where(i => i.id == itemId).Any();
		}

		[XmlIgnore]
		public ItemRace race;

		[XmlIgnore]
		public string SortString {
			get { return skill + skillpoints.ToString().PadLeft(3, '0'); }
		}
	}

	[Serializable]
	[XmlType(AnonymousType = true)]
	public class RecipeItem
	{
		[XmlAttribute]
		public int id;

		[XmlAttribute]
		public string name;

		[XmlAttribute]
		public long price;

		[XmlAttribute]
		public int level;

		[XmlAttribute]
		public string desc;

		[XmlIgnore]
		public ItemRace race;

		[XmlAttribute("recipe")]
		[DefaultValue(false)]
		public bool isRecipe;
	}
}
