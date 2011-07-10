using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml.Serialization;
using System.Xml.Schema;

namespace AionQuests
{
    [Serializable]
    [XmlType(AnonymousType = true)]
    [XmlRootAttribute(ElementName = "bonuses", Namespace = "", IsNullable = false)]
    public class InvBonuses
    {
        [XmlElement("bonus_info", Form = XmlSchemaForm.Unqualified)]
        public List<BonusItem> BonusItems;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public class BonusItem
    {
        [XmlAttribute]
        public int questId;

        [XmlElement("bonus", Form = XmlSchemaForm.Unqualified)]
        public List<BonusInfo> BonusInfos;

        [XmlElement]
        public WrapItem wrap;

        [XmlIgnore]
        public bool wrapSpecified;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public class BonusInfo
    {
        [XmlText]
        public string Value;

        [XmlAttribute]
        public int checkItem;

        [XmlIgnore]
        public bool checkItemSpecified;

        [XmlAttribute]
        public int count;

        [XmlIgnore]
        public bool countSpecified;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public class WrapItem
    {
        [XmlAttribute]
        public int itemId;

        [XmlAttribute]
        public int maxCount;

        [XmlAttribute]
        public BonusType type;

        [XmlAttribute]
        public int level;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public enum BonusType
    {
        MANASTONE,
        ENCHANT
    }
}
