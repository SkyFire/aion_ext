namespace Jamie.Items
{
    using System;
    using System.Collections.Generic;
    using System.ComponentModel;
    using System.Xml.Schema;
    using System.Xml.Serialization;
    using Jamie.ParserBase;

    [Serializable]
    public partial class ComboProduct
    {
        [XmlAttribute]
        public int itemid;
    }

    [Serializable]
    public partial class Component
    {
        [XmlAttribute]
        public int quantity;
        
        [XmlAttribute]
        public int itemid;
    }

    [Serializable]
    public partial class RecipeTemplate
    {
        [XmlElement("component", Form = XmlSchemaForm.Unqualified)]
        public List<Component> components;

        [XmlElement("comboproduct", Form = XmlSchemaForm.Unqualified)]
        public List<ComboProduct> comboproducts;

        [XmlAttribute]
        public int id;

        [XmlAttribute]
        public int nameid;

        [XmlAttribute]
        public int skillid;

        [XmlAttribute]
        public skillRace race;

        [XmlAttribute]
        public int skillpoint;

        [XmlAttribute]
        [DefaultValue(0)]
        public int dp;

        [XmlAttribute]
        [DefaultValue(0)]
        public int autolearn;

        [XmlAttribute]
        public int productid;

        [XmlAttribute]
        public int quantity;

        [XmlAttribute]
        public int componentquantity;

        [XmlAttribute]
        [DefaultValue(0)]
        public int maxcount;

        [XmlAttribute]
        [DefaultValue(0)]
        public int delayid;

        [XmlAttribute]
        [DefaultValue(0)]
        public int delaytime;

        [XmlAttribute]
        [DefaultValue(0)]
        public int tasktype;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    [XmlRoot(ElementName = "recipe_templates", Namespace = "", IsNullable = false)]
    public partial class RecipeTemplates
    {
        [XmlElement("import")]
        public List<TemplateImportPart> import;

        [XmlElement("recipe_template", Form = XmlSchemaForm.Unqualified)]
        public List<RecipeTemplate> RecipeList;
    }
}
