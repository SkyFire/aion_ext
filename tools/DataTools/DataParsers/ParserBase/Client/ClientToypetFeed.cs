namespace Jamie.ParserBase
{
    using System;
    using System.Xml.Serialization;
    using System.Xml.Schema;
    using System.Collections.Generic;

    [Serializable]
    [XmlType(AnonymousType = true)]
    [XmlRoot(ElementName = "client_toypet_feeds", Namespace = "", IsNullable = false)]
    public class ClientToypetFeed
    {
        [XmlElement("client_toypet_feed", Form = XmlSchemaForm.Unqualified)]
        public List<ToypetFeed> Items;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public class ToypetFeed
    {
        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int id;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string name;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string desc;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int limit_love_count;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int love_flavor_id_1;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string love_flavor_desc_1;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int love_flavor_id_2;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string love_flavor_desc_2;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int love_flavor_id_3;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string love_flavor_desc_3;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int love_flavor_id_4;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string love_flavor_desc_4;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int feeding_count;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int favorite_flavor_id;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string favorite_flavor_desc;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int feeding_cooltime;
    }
}
