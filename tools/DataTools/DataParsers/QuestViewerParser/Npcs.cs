using System.Xml.Serialization;
using System;
using System.Xml.Schema;
using System.Collections.Generic;

namespace AionQuests
{
    [Serializable]
    [XmlType(AnonymousType = true)]
    [XmlRoot(ElementName = "npc_clients", Namespace = "", IsNullable = false)]
    public partial class NpcFile
    {
        [XmlElement("npc_client", typeof(Npc), Form = XmlSchemaForm.Unqualified)]
        public List<Npc> NpcList;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class Npc
    {
        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int id;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string desc;

        [XmlIgnore]
        public StringDescription Description {
            get {
                return Utility.StringIndex.GetStringDescription(desc);
            }
        }

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string name;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string dir;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string mesh;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string material;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string show_dmg_decal;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string ui_type;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string cursor_type;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public bool hide_path;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public bool erect;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int scale;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int weapon_scale;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public decimal altitude;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public decimal stare_angle;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public decimal stare_distance;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public decimal move_speed_normal_walk;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public decimal art_org_move_speed_normal_walk;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public decimal move_speed_normal_run;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public decimal move_speed_combat_run;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public decimal art_org_speed_combat_run;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public decimal in_time;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public decimal out_time;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public decimal neck_angle;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public decimal spine_angle;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string ammo_bone;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string ammo_fx;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string ammo_speed;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public decimal pushed_range;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int hpgauge_level;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public bool magical_skill_boost;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string ai_name;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string tribe;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string pet_ai_name;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public decimal sensory_range;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string race_type;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string npc_type;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public decimal talking_distance;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string dmg_decal_texture;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string disk_type;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string weapon_hit_fx;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string foot_mat;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public bool hide_shadow;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string ammo_hit_fx;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int float_corpse;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string game_lang;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string ui_race_type;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string undetectable;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string idle_animation;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string talk_animation;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string appearance_custom;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string quest_ai_name;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string ment;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public bool recovery;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int recovery_opt1;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int recovery_opt2;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string walk_animation;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string airlines_name;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public decimal title_offset;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int extendcharwarehouse_start;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int extendcharwarehouse_end;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int give_item_proc;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string remove_item_option;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public bool change_item_skin;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string gather_skill_levelup;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int extendinventory_start;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int extendinventory_end;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int extendaccountwarehouse_start;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int extendaccountwarehouse_end;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public bool pvpzone;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string pvpzone_world_name;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string pvpzone_location_alias;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public bool edit_character_gender;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public bool edit_character_all;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string fxc_type;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string dmg_texture;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string jobfaction_name;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int @static;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string object_type;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string abyss_npc_type;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int artifact_id;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string user_animation;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string spawn_animation;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string deadbody_name_id;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public bool use_script;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public bool match_maker;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public decimal huge_mob;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public decimal rotation_period;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int talk_delay_time;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string bindstone_type;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int bindstone_capacity;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int bindstone_usecount;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public bool check_can_see;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string html_bg;

        [XmlElement("bound_radius", Form = XmlSchemaForm.Unqualified)]
        public BoundRadius BoundRadius;

        [XmlElement("attack_range", Form = XmlSchemaForm.Unqualified)]
        public decimal attack_range;

        [XmlElement("attack_rate", Form = XmlSchemaForm.Unqualified)]
        public decimal attack_rate;

        [XmlElement("visible_equipments", Form = XmlSchemaForm.Unqualified)]
        public VisibleEquipment VisibleEquipment;

        [XmlElement("appearance", Form = XmlSchemaForm.Unqualified)]
        public Appearance Appearance;

        [XmlElement("trade_info", Form = XmlSchemaForm.Unqualified)]
        public TradeInfo TradeInfo;

        [XmlElement("abyss_trade_info", Form = XmlSchemaForm.Unqualified)]
        public TradeInfo abyss_trade_info;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class BoundRadius
    {
        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public decimal front;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public decimal side;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public decimal upper;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class VisibleEquipment
    {
        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string head;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string torso;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string leg;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string foot;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string shoulder;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string glove;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string main;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string sub;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class Appearance
    {
        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string pc_type;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string face_type;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string face_color;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string hair_type;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string hair_color;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    [XmlRoot(Namespace = "", IsNullable = false)]
    public partial class TabList
    {
        [XmlElement("data", Form = XmlSchemaForm.Unqualified)]
        public List<TabData> Data;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class TabData
    {
        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string tab;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string atab;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class TradeInfo
    {
        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int sell_price_rate;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int buy_price_rate;

        [XmlElement("tab_list", Form = XmlSchemaForm.Unqualified)]
        public List<TabList> TabList;
    }
}
