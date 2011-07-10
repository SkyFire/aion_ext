namespace Jamie.ParserBase
{
    using System;
    using System.Linq;
    using System.Xml.Serialization;
    using System.Xml.Schema;
    using System.ComponentModel;
    using System.Collections.Generic;

    [Serializable]
    [XmlType(AnonymousType = true)]
    [XmlRoot(ElementName = "gather_srcs", Namespace = "", IsNullable = false)]
    public partial class ClientGatherSrc
    {
        [XmlElement("gather_src", Form = XmlSchemaForm.Unqualified)]
        public List<GatherSource> Items;

        Dictionary<string, GatherSource> nameToObject = new Dictionary<string, GatherSource>();

        internal void CreateIndex() {
            nameToObject = Items.ToDictionary(s => s.name.ToLower(), s => s);
        }

        public GatherSource this[string gatherName] {
            get {
                gatherName = gatherName.Trim().ToLower();
                if (nameToObject.ContainsKey(gatherName))
                    return nameToObject[gatherName];
                return null;
            }
        }
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class GatherSource
    {
        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int id;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string name;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string desc;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public GatherCategory category;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public SourceType source_type;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string mesh;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string source_color;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public decimal source_upper;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string source_fx;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string motion_name;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string harvestskill;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int skill_level;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue(0)]
        public int char_level_limit;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int gather_delay_id;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int gather_delay;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string required_item;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int check_type;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue(false)]
        public bool erase_value;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string material1;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int normal_rate1;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue("")]
        public string material2;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue(0)]
        public int normal_rate2;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue("")]
        public string material3;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue(0)]
        public int normal_rate3;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue("")]
        public string material4;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue(0)]
        public int normal_rate4;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue("")]
        public string material5;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue(0)]
        public int normal_rate5;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue("")]
        public string material6;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue(0)]
        public int normal_rate6;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue("")]
        public string material7;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue(0)]
        public int normal_rate7;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue("")]
        public string material8;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue(0)]
        public int normal_rate8;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue("")]
        public string extra_material1;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue(0)]
        public int extra_normal_rate1;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue("")]
        public string extra_material2;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue(0)]
        public int extra_normal_rate2;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue("")]
        public string extra_material3;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue(0)]
        public int extra_normal_rate3;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue("")]
        public string extra_material4;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue(0)]
        public int extra_normal_rate4;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue("")]
        public string extra_material5;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue(0)]
        public int extra_normal_rate5;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue("")]
        public string extra_material6;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue(0)]
        public int extra_normal_rate6;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue("")]
        public string extra_material7;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue(0)]
        public int extra_normal_rate7;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue("")]
        public string extra_material8;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue(0)]
        public int extra_normal_rate8;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int harvest_count;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int success_adj;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int failure_adj;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int aerial_adj;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int captcha_rate;

        public GatherSource() {
            this.category = GatherCategory.harvest_source;
        }
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public enum GatherCategory
    {
        harvest_source
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public enum SourceType
    {
        none,
        berry,
        fish,
        herb,
        jewelry,
        metal,
        noblemetal,
        od,
        plant,
        shell,
        tree,
        vegetable
    }
}
