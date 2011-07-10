namespace Jamie.ParserBase
{
	using System;
	using System.Xml.Serialization;

	[Serializable]
    [Flags]
    public enum Class
    {
        NONE = 0,
        WARRIOR = 1,
        SCOUT = 2,
        MAGE = 4,
        CLERIC = 8,
        GLADIATOR = 16,
        [XmlEnum("GLADIATOR")]
        FIGHTER = 16,
        TEMPLAR = 32,
        [XmlEnum("TEMPLAR")]
        KNIGHT = 32,
        ASSASSIN = 64,
        RANGER = 128,
        SORCERER = 256,
        [XmlEnum("SORCERER")]
        WIZARD = 256,
        SPIRIT_MASTER = 512,
        [XmlEnum("SPIRIT_MASTER")]
        ELEMENTALLIST = 512,
        CHANTER = 1024,
        PRIEST = 2048,
        ALL = WARRIOR | SCOUT | MAGE | CLERIC | FIGHTER | KNIGHT | ASSASSIN | RANGER | WIZARD | ELEMENTALLIST | CHANTER | PRIEST
    }

    [Serializable]
    [Flags]
    public enum ClassOur
    {
        NONE = 0,
        WARRIOR = 1,
        SCOUT = 2,
        MAGE = 4,
        PRIEST = 8,
        GLADIATOR = 16,
        TEMPLAR = 32,
        ASSASSIN = 64,
        RANGER = 128,
        SORCERER = 256,
        SPIRIT_MASTER = 512,
        CHANTER = 1024,
        CLERIC = 2048,
        ALL = WARRIOR | SCOUT | MAGE | CLERIC | GLADIATOR | TEMPLAR | ASSASSIN | RANGER | SORCERER | SPIRIT_MASTER | CHANTER | PRIEST
    }

    [Serializable]
    public enum skillPlayerClass
    {
        ALL = 0,
        WARRIOR,
        FIGHTER,
        KNIGHT,
        SCOUT,
        ASSASSIN,
        RANGER,
        MAGE,
        WIZARD,
        ELEMENTALLIST,
        PRIEST,
        CLERIC,
        CHANTER,
     }
}
