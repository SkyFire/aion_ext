namespace Jamie.Npcs
{
    using System;
    using System.Xml.Serialization;
    using System.ComponentModel;
    using System.Xml.Schema;
    using System.Collections.Generic;


    [Serializable]
    [XmlType(AnonymousType = true)]
    [XmlRoot(ElementName = "npc_shouts", Namespace = "", IsNullable = false)]
    public partial class NpcShouts
    {
        [XmlElement("shout_npc", Form = XmlSchemaForm.Unqualified)]
        public List<NpcShoutData> NpcList = new List<NpcShoutData>();
    }


    [Serializable]
    public partial class NpcShoutData
    {
        [XmlAttributeAttribute]
        public int npcid;

        [XmlElement("shout", Form = XmlSchemaForm.Unqualified)]
        public List<Shout> ShoutList = new List<Shout>();
    }

    [Serializable]
    public partial class Shout
    {
        [XmlAttribute]
        public int messageid;

        [XmlAttribute]
        public ShoutEventType @event;

        [XmlAttribute]
        [DefaultValue("")]
        public string param;
    }

    public enum ShoutEventType
    {
        NONE = 0,
        START,
        DESPAWN,
        DIE,
        ATK,
        CAST,
        DIRECTION,
        IDLE,
        WIN,
        FAIL,
        FLEE,
        WAYPOINT,
        HELP,
        SKILL,
        SLEEP,
        RESETHATE,
        WAKEUP,
        WOUNDED,
        SEEUSER,
        SWICHTARGET,
        YELL,
        LEAVE,
        QUEST,
        STATUP
    }
}
