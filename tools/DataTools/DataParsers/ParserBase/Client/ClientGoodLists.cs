namespace Jamie.ParserBase
{
	using System;
	using System.Collections.Generic;
	using System.ComponentModel;
	using System.Xml.Schema;
	using System.Xml.Serialization;

    [Serializable]
    [XmlType(AnonymousType = true)]
    [XmlRoot(ElementName = "client_npc_goodslists", Namespace = "", IsNullable = false)]
    public partial class ClientGoodlistsFile
    {
        [XmlElement("client_npc_goodslist", Form = XmlSchemaForm.Unqualified)]
        public List<ClientGoodList> GoodLists;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class ClientGoodList
    {
        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int id;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string name;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string desc;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue(SalestimeTable.none)]
        public SalestimeTable salestime_table_name;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string sale_explain_desc;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string advertise_msg;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string gossip_msg;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue(0)]
        public int sales_clear_turn;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue(0)]
        public int sales_clear_interval;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue(0)]
        public int guild_level;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue(0)]
        public int use_category;

        [XmlArray(ElementName = "goods_list", Form = XmlSchemaForm.Unqualified)]
        [XmlArrayItem("data", Form = XmlSchemaForm.Unqualified, IsNullable = false)]
        public List<GoodListData> data;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public enum SalestimeTable
    {
        none = 0,
        all_turn,
        every_10_12_4_8_10_12,
        every_10_12_2_6_10_12,
        Friday_worktime,
        Monday_worktime,
        Thursday_worktime,
        Tuesday_worktime,
        Wednesday_worktime,
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class GoodListData
    {
        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string item;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int sell_limit;

        [XmlIgnore]
        public bool sell_limitSpecified;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int buy_limit;

        [XmlIgnore]
        public bool buy_limitSpecified;
    }
}
