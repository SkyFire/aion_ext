namespace Jamie.Npcs
{
    using System.Xml.Serialization;
    using System.Xml.Schema;
    using System;
    using System.ComponentModel;
    using System.Collections.Generic;

    [Serializable]
    [XmlType(AnonymousType = true)]
    [XmlRoot(ElementName = "door_templates", Namespace = "", IsNullable = false)]
    public partial class DoorTemplates
    {
        [XmlElement("door", Form = XmlSchemaForm.Unqualified)]
        public List<Door> Doors;

        public DoorTemplates() {
            Doors = new List<Door>();
        }
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class Door
    {
        [XmlElement("doorKey", Form = XmlSchemaForm.Unqualified)]
        public DoorKey doorKey;

        [XmlAttribute]
        public int id;

        [XmlAttribute]
        public int worldId;

        [XmlAttribute]
        [DefaultValue(false)]
        public bool closeable;

        [XmlAttribute]
        [DefaultValue(0)]
        public decimal x;

        [XmlAttribute]
        [DefaultValue(0)]
        public decimal y;

        [XmlAttribute]
        [DefaultValue(0)]
        public decimal z;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class DoorKey
    {
        [XmlAttribute]
        public int itemId;

        [XmlAttribute]
        [DefaultValue(false)]
        public bool remove;

        [XmlAttribute]
        [DefaultValue("")]
        public string name;

        [XmlAttribute]
        [DefaultValue(0)]
        public int nameId;

        [XmlAttribute]
        [DefaultValue("")]
        public string desc;
    }

}
