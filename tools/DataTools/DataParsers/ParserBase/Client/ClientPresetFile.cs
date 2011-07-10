namespace Jamie.ParserBase
{
    using System;
    using System.Collections.Generic;
    using System.ComponentModel;
    using System.Xml.Schema;
    using System.Xml.Serialization;

    [Serializable]
    [XmlType(AnonymousType = true)]
    [XmlRoot(ElementName = "custom_presets", Namespace = "", IsNullable = false)]
    public partial class ClientPresetFile
    {
        [XmlElement("custom_preset", Form = XmlSchemaForm.Unqualified)]
        public List<Preset> Presets;

        [XmlElement("preset", Form = XmlSchemaForm.Unqualified)]
        public List<OurPreset> OurPresets;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class Preset
    {
        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string name;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string desc;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int version;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public PcType pc_type;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public PcClass pc_class;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public decimal scale;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int hair_type;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int face_type;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int feat_type1;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
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
        public int npc_head_type;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
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

    [Serializable]
    [XmlType(AnonymousType = true)]
    public enum PcType
    {
        pc_df,
        pc_dm,
        pc_lf,
        pc_lm,
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public enum PcClass
    {
        warrior,
        cleric,
        mage,
        scout,
    }
}
