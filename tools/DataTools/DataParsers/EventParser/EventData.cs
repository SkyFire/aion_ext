namespace Jamie.Quests
{
	using System;
	using System.Collections.Generic;
	using System.Linq;
	using System.Text;
	using System.Xml.Serialization;
	using System.ComponentModel;
	using Jamie.Items;

	[Serializable]
	[XmlType(AnonymousType = true)]
	[XmlRoot(ElementName = "events", Namespace = "", IsNullable = false)]
	public class EventsFile
	{
		[XmlElement("event")]
		public List<EventData> events;
	}

	[Serializable]
	[XmlType(Namespace = "", AnonymousType = true)]
	public class EventData
	{
		[XmlAttribute]
		public string name;

		[XmlElement("quest")]
		public List<EventQuest> quests;

		[XmlElement("wrap")]
		public List<EventItem> wrapItems;

		[XmlElement("reward")]
		public List<EventItem> rewardItems;

		[XmlElement("cash")]
		public List<EventItem> cash;
	}

	[Serializable]
	[XmlType(Namespace = "", AnonymousType = true)]
	public class EventQuest
	{
		[XmlAttribute]
		public int id;

		[XmlAttribute]
		public string name;

		[XmlElement("item")]
		public List<EventItem> items;
	}

	[Serializable]
	[XmlType(Namespace = "", AnonymousType = true)]
	public class EventItem
	{
		[XmlAttribute]
		public int item_id;

		[XmlAttribute]
		public string name;

		[XmlAttribute]
		[DefaultValue(0)]
		public int npc_id;

		[XmlAttribute]
		[DefaultValue(0)]
		public int chance;

		[XmlAttribute]
		[DefaultValue(0)]
		public ItemRace race;

		[XmlAttribute]
		[DefaultValue("")]
		public string comment;
	}
}
