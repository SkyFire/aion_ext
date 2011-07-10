using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml.Serialization;
using System.Xml.Schema;

namespace Jamie.Quests
{
    [XmlInclude(typeof(airline_service))]
    [XmlInclude(typeof(restore_xp))]
    [XmlInclude(typeof(trade_buy))]
    [XmlInclude(typeof(trade_sell))]
    [XmlInclude(typeof(deposit_char_warehouse))]
    [XmlInclude(typeof(open_guild_warehouse))]
    [XmlInclude(typeof(exchange_coin))]
    [XmlInclude(typeof(open_vendor))]
    [XmlInclude(typeof(match_maker))]
    [XmlInclude(typeof(stigma_open))]
    [XmlInclude(typeof(remove_item_option))]
    [XmlInclude(typeof(combine_skill_levelup))]
    [XmlInclude(typeof(combine_task))]
    [XmlInclude(typeof(extend_inventory))]
    [XmlInclude(typeof(enter_pvp))]
    [XmlInclude(typeof(leave_pvp))]
    [XmlInclude(typeof(edit_char_all))]
    [XmlInclude(typeof(create_pcguild))]
    [XmlInclude(typeof(delete_pcguild))]
    [XmlInclude(typeof(recreate_pcguild))]
    [XmlInclude(typeof(guild_levelup))]
    [XmlInclude(typeof(change_item_skin))]
    [XmlInclude(typeof(edit_char_gender))]
    [XmlInclude(typeof(gather_skill_levelup))]
    [XmlInclude(typeof(compound_weapon))]
    [XmlInclude(typeof(decompound_weapon))]
    [XmlInclude(typeof(give_item_proc))]
    [XmlInclude(typeof(Pet_adopt))]
    [XmlInclude(typeof(Pet_abandon))]
    [XmlInclude(typeof(faction_join))]
    [XmlInclude(typeof(faction_separate))]
    [XmlInclude(typeof(instance_entry))]
    [XmlInclude(typeof(guild_change_emblem))]
	[Serializable]
    public abstract class NpcDialogFunc
    {
        [XmlText]
        public string Function;
    }

    [Serializable]
    public class airline_service : NpcDialogFunc
    {
    }

    [Serializable]
    public class restore_xp : NpcDialogFunc
    {
    }

    [Serializable]
    public class trade_buy : NpcDialogFunc
    {
    }

    [Serializable]
    public class trade_sell : NpcDialogFunc
    {
    }

    [Serializable]
    public class deposit_char_warehouse : NpcDialogFunc
    {
    }

    [Serializable]
    public class open_guild_warehouse : NpcDialogFunc
    {
    }

    [Serializable]
    public class exchange_coin : NpcDialogFunc
    {
    }

    [Serializable]
    public class open_vendor : NpcDialogFunc
    {
    }

    [Serializable]
    public class match_maker : NpcDialogFunc
    {
    }

    [Serializable]
    public class stigma_open : NpcDialogFunc
    {
    }

    [Serializable]
    public class remove_item_option : NpcDialogFunc
    {
    }

    [Serializable]
    public class combine_skill_levelup : NpcDialogFunc
    {
    }

    [Serializable]
    public class combine_task : NpcDialogFunc
    {
    }

    [Serializable]
    public class extend_inventory : NpcDialogFunc
    {
    }

    [Serializable]
    public class enter_pvp : NpcDialogFunc
    {
    }

    [Serializable]
    public class leave_pvp : NpcDialogFunc
    {
    }

    [Serializable]
    public class edit_char_all : NpcDialogFunc
    {
    }

    [Serializable]
    public class create_pcguild : NpcDialogFunc
    {
    }

    [Serializable]
    public class delete_pcguild : NpcDialogFunc
    {
    }

    [Serializable]
    public class recreate_pcguild : NpcDialogFunc
    {
    }

    [Serializable]
    public class guild_levelup : NpcDialogFunc
    {
    }

    [Serializable]
    public class change_item_skin : NpcDialogFunc
    {
    }

    [Serializable]
    public class edit_char_gender : NpcDialogFunc
    {
    }

    [Serializable]
    public class gather_skill_levelup : NpcDialogFunc
    {
    }

    [Serializable]
    public class compound_weapon : NpcDialogFunc
    {
    }

    [Serializable]
    public class decompound_weapon : NpcDialogFunc
    {
    }

    [Serializable]
    public class give_item_proc : NpcDialogFunc
    {
    }

    [Serializable]
    public class Pet_adopt : NpcDialogFunc
    {
    }

    [Serializable]
    public class Pet_abandon : NpcDialogFunc
    {
    }

    [Serializable]
    public class faction_join : NpcDialogFunc
    {
    }

    [Serializable]
    public class faction_separate : NpcDialogFunc
    {
    }

    [Serializable]
    public class instance_entry : NpcDialogFunc
    {
    }

    [Serializable]
    public class guild_change_emblem : NpcDialogFunc
    {
    }

	public partial class DialogHtml
    {
        [XmlArray(Form = XmlSchemaForm.Unqualified)]
        [XmlArrayItem("airline_service", typeof(airline_service), Form = XmlSchemaForm.Unqualified)]
        [XmlArrayItem("restore_xp", typeof(restore_xp), Form = XmlSchemaForm.Unqualified)]
        [XmlArrayItem("trade_buy", typeof(trade_buy), Form = XmlSchemaForm.Unqualified)]
        [XmlArrayItem("trade_sell", typeof(trade_sell), Form = XmlSchemaForm.Unqualified)]
        [XmlArrayItem("deposit_char_warehouse", typeof(deposit_char_warehouse), Form = XmlSchemaForm.Unqualified)]
        [XmlArrayItem("open_guild_warehouse", typeof(open_guild_warehouse), Form = XmlSchemaForm.Unqualified)]
        [XmlArrayItem("exchange_coin", typeof(exchange_coin), Form = XmlSchemaForm.Unqualified)]
        [XmlArrayItem("open_vendor", typeof(open_vendor), Form = XmlSchemaForm.Unqualified)]
        [XmlArrayItem("match_maker", typeof(match_maker), Form = XmlSchemaForm.Unqualified)]
        [XmlArrayItem("stigma_open", typeof(stigma_open), Form = XmlSchemaForm.Unqualified)]
        [XmlArrayItem("remove_item_option", typeof(remove_item_option), Form = XmlSchemaForm.Unqualified)]
        [XmlArrayItem("combine_skill_levelup", typeof(combine_skill_levelup), Form = XmlSchemaForm.Unqualified)]
        [XmlArrayItem("combine_task", typeof(combine_task), Form = XmlSchemaForm.Unqualified)]
        [XmlArrayItem("extend_inventory", typeof(extend_inventory), Form = XmlSchemaForm.Unqualified)]
        [XmlArrayItem("enter_pvp", typeof(enter_pvp), Form = XmlSchemaForm.Unqualified)]
        [XmlArrayItem("leave_pvp", typeof(leave_pvp), Form = XmlSchemaForm.Unqualified)]
        [XmlArrayItem("edit_char_all", typeof(edit_char_all), Form = XmlSchemaForm.Unqualified)]
        [XmlArrayItem("create_pcguild", typeof(create_pcguild), Form = XmlSchemaForm.Unqualified)]
        [XmlArrayItem("delete_pcguild", typeof(delete_pcguild), Form = XmlSchemaForm.Unqualified)]
        [XmlArrayItem("recreate_pcguild", typeof(recreate_pcguild), Form = XmlSchemaForm.Unqualified)]
        [XmlArrayItem("guild_levelup", typeof(guild_levelup), Form = XmlSchemaForm.Unqualified)]
        [XmlArrayItem("change_item_skin", typeof(change_item_skin), Form = XmlSchemaForm.Unqualified)]
        [XmlArrayItem("edit_char_gender", typeof(edit_char_gender), Form = XmlSchemaForm.Unqualified)]
        [XmlArrayItem("gather_skill_levelup", typeof(gather_skill_levelup), Form = XmlSchemaForm.Unqualified)]
        [XmlArrayItem("compound_weapon", typeof(compound_weapon), Form = XmlSchemaForm.Unqualified)]
        [XmlArrayItem("decompound_weapon", typeof(decompound_weapon), Form = XmlSchemaForm.Unqualified)]
        [XmlArrayItem("give_item_proc", typeof(give_item_proc), Form = XmlSchemaForm.Unqualified)]
        [XmlArrayItem("Pet_adopt", typeof(Pet_adopt), Form = XmlSchemaForm.Unqualified)]
        [XmlArrayItem("Pet_abandon", typeof(Pet_abandon), Form = XmlSchemaForm.Unqualified)]
        [XmlArrayItem("faction_join", typeof(faction_join), Form = XmlSchemaForm.Unqualified)]
        [XmlArrayItem("faction_separate", typeof(faction_separate), Form = XmlSchemaForm.Unqualified)]
        [XmlArrayItem("instance_entry", typeof(instance_entry), Form = XmlSchemaForm.Unqualified)]
        [XmlArrayItem("guild_change_emblem", typeof(guild_change_emblem), Form = XmlSchemaForm.Unqualified)]
        public NpcDialogFunc[] npcfuncs;

        public CutScene CutScene;
        public Movie Movie;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public class CutScene
    {
        [XmlAttribute]
        public int id;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public class Movie
    {
        [XmlAttribute]
        public int id;
    }
}
