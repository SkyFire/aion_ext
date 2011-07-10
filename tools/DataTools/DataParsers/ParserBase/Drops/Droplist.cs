namespace Jamie.Drops
{
    using System;
    using System.Collections.Generic;
    using System.Xml.Schema;
    using System.Xml.Serialization;
    using System.ComponentModel;

    [Serializable]
    public class DropItem
    {
        [XmlAttribute]
        public int id;

        [XmlAttribute]
        public int min;

        [XmlAttribute]
        public int max;

        [XmlAttribute]
        public decimal chance;
    }

    [Serializable]
    public class NpcDrop
    {
        [XmlAttribute]
        public int npcid;

        [XmlAttribute]
        [DefaultValue(0)]
        public decimal nodrop;

        [XmlAttribute]
        [DefaultValue(DropAi.NONE)]
        public DropAi ai;

        [XmlElement("dropitem", Form = XmlSchemaForm.Unqualified)]
        public List<DropItem> DropItems;
    }

    [Serializable]
    public enum DropAi
    {
        NONE = 0,
        DROP,
        NORMAL,
        DESPAWN,
        DICE,
        DICE_UP3,
        DICE_UP3_BIG,
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    [XmlRoot(ElementName = "droplist", Namespace = "", IsNullable = false)]
    public class Droplist
    {
        [XmlElement("npcdrop", Form = XmlSchemaForm.Unqualified)]
        public List<NpcDrop> Drops;
    }
}
