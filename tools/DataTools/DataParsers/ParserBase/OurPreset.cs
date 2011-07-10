namespace Jamie.ParserBase
{
    using System;
    using System.Xml.Schema;
    using System.Xml.Serialization;
    using Jamie.Items;
    using System.ComponentModel;

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class OurPreset
    {
        [XmlAttribute(Form = XmlSchemaForm.Unqualified)]
        public string name;

        [XmlAttribute(AttributeName = "class", Form = XmlSchemaForm.Unqualified)]
        public Class @class;

        [XmlAttribute(Form = XmlSchemaForm.Unqualified)]
        public Gender gender;

        [XmlAttribute(Form = XmlSchemaForm.Unqualified)]
        public ItemRace race;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue(0)]
        public float height;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue(0)]
        public int hair_type;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue(0)]
        public int face_type;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue(0)]
        public int feat_type1;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue(0)]
        public int feat_type2;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string hair_color;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string lip_color;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string eye_color;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string skin_color;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string detail;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue(0)]
        public int npc_head_type;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue(0)]
        public int npc_hair_type;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue(0)]
        public int npc_face_type;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue(0)]
        public int npc_feature_type1;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue(0)]
        public int npc_feature_type2;
    }
}
