namespace Jamie.ParserBase
{
    using System.Xml.Serialization;
    using System;
    using System.Linq;
    using System.Xml.Schema;
    using System.ComponentModel;
    using System.Collections.Generic;

    [Serializable]
    [XmlType(AnonymousType = true)]
    [XmlRoot(ElementName = "client_cosmeticiteminfos", Namespace = "", IsNullable = false)]
    public partial class ClientCosmetics
    {
        [XmlElement("client_cosmeticiteminfo", Form = XmlSchemaForm.Unqualified)]
        public List<CosmeticInfo> Cosmetics;

        Dictionary<string, CosmeticInfo> nameToInfo = new Dictionary<string, CosmeticInfo>(StringComparer.InvariantCultureIgnoreCase);

        internal void CreateIndex() {
            if (Cosmetics == null)
                return;
            nameToInfo = Cosmetics.ToDictionary(s => s.name.ToLower(), s => s);
        }

        public CosmeticInfo this[string stringId] {
            get {
                stringId = stringId.Trim().ToLower();
                if (nameToInfo.ContainsKey(stringId))
                    return nameToInfo[stringId];
                return null;
            }
        }
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class CosmeticInfo
    {
        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int id;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string name;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string dev_name;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string preset_name;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string lip_color;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string eye_color;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string face_color;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string hair_color;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue(0)]
        public int hair_type;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue(0)]
        public int face_type;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue(0)]
        public int tattoo_type;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue(0)]
        public int makeup_type;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue(0)]
        public int voice_type;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public GenderPermitted gender_permitted;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string class_permitted;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public RacePermitted race_permitted;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public enum GenderPermitted
    {
        all,
        male,
        female
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public enum RacePermitted
    {
        pc_light,
        pc_dark
    }
}
