namespace Jamie.ParserBase
{
	using System;
	using System.Collections.Generic;
	using System.Diagnostics;
	using System.Xml.Schema;
	using System.Xml.Serialization;

    [Serializable]
    [XmlType(AnonymousType = true)]
    [XmlRoot(ElementName = "npc_clients", Namespace = "", IsNullable = false)]
    public partial class ClientNpcsFile
    {
        [XmlElement("npc_client", typeof(Npc), Form = XmlSchemaForm.Unqualified)]
        public List<Npc> NpcList;

        Dictionary<string, int> nameToId = null;
        Dictionary<int, Npc> idToObject = null;

        internal void CreateIndex() {
            if (this.NpcList == null)
                return;

            nameToId = new Dictionary<string, int>(StringComparer.InvariantCultureIgnoreCase);
            idToObject = new Dictionary<int, Npc>();

            foreach (Npc npc in this.NpcList) {
                if (!nameToId.ContainsKey(npc.name)) {
                    nameToId.Add(npc.name, npc.id);
                } else {
                    Debug.Print("String with the name {0} already exists; id = {1}", npc.name, npc.id);
                }
                if (!idToObject.ContainsKey(npc.id)) {
                    idToObject.Add(npc.id, npc);
                }
            }
        }

        public int this[string stringId] {
            get {
                if (nameToId == null || String.IsNullOrEmpty(stringId) || !nameToId.ContainsKey(stringId))
                    return -1;
                return nameToId[stringId];
            }
        }

        public Npc this[int id] {
            get {
                if (idToObject == null || !idToObject.ContainsKey(id))
                    return null;
                return idToObject[id];
            }
        }
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class Npc
    {
        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int id;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string desc;

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
        public CursorType cursor_type;

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
		public int attack_delay;

		public NpcTribe Tribe {
			get {
				if (tribe == null)
					return NpcTribe.None;
				return (NpcTribe)Enum.Parse(typeof(NpcTribe), tribe, true);
			}
		}

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string pet_ai_name;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public decimal sensory_range;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string race_type;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public NpcType npc_type;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public decimal talking_distance;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string dmg_decal_texture;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public DiskType disk_type;

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
        public AbyssNpcType abyss_npc_type;

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
        public int match_maker;

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
        public decimal[] attack_range;

        [XmlElement("attack_rate", Form = XmlSchemaForm.Unqualified)]
        public decimal[] attack_rate;

        [XmlElement("visible_equipments", Form = XmlSchemaForm.Unqualified)]
        public VisibleEquipment VisibleEquipment;

        [XmlElement("appearance", Form = XmlSchemaForm.Unqualified)]
        public Appearance Appearance;

        [XmlElement("trade_info", Form = XmlSchemaForm.Unqualified)]
        public TradeInfo TradeInfo;

        [XmlElement("abyss_trade_info", Form = XmlSchemaForm.Unqualified)]
        public TradeInfo AbyssTradeInfo;

		#region Added in 1.9 - 2.0

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public int instance_entry;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public int compound_weapon;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public int func_pet_manage;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public string furniture_category;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public string arrange_place;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public string arrange_rotation_side;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public string arrange_rotation_updown;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public string arrange_midair;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public string animation_marker;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public int visible_range;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public string hide_pc_fx_shader;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public string sanctuary_animation;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public string str_type;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public NpcFaction npcfaction_name;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public string guide_func;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public int package_permitted;

		[XmlElement("extra_currency_trade_info", Form = XmlSchemaForm.Unqualified)]
		public TradeInfo ExtraCurrencyTradeInfo;

		[XmlElement("coupon_trade_info", Form = XmlSchemaForm.Unqualified)]
		public TradeInfo CouponTradeInfo;

		[XmlElement("data", Form = XmlSchemaForm.Unqualified)]
		public List<TabData> Data;

		#endregion

		#region Added in 2.5

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public NpcFunction npc_function_type;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public int func_itemcharge;

		#endregion
	}

	[Serializable]
	public enum NpcFunction
	{
		None = 0,
		Bindstone,
		bindstone = Bindstone,
		Merchant,
		merchant = Merchant,
		Postbox,
		Teleport,
		teleport = Teleport,
		TELEPORT = Teleport,
		Vendor,
		vendor = Vendor,
		Warehouse,
		warehouse = Warehouse
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
    	public string etab;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string atab;

    	[XmlElement(Form = XmlSchemaForm.Unqualified)]
    	public string ctab;
    
    	[XmlElement(Form = XmlSchemaForm.Unqualified)]
    	public int buy_price_rate;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int sell_price_rate;
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

	[Serializable]
	public enum NpcType
	{
		Abyss_Guard,
		Abyss_guard = Abyss_Guard,
		General,
		general = General,
		Guard,
		guard = Guard,
		Mercenary,
		Monster,
		monster = Monster,
		Summon_Pet
	}

	[Serializable]
	public enum NpcTribe
	{
		AbyssDrakanGate,
		Aggressive_Dark,
		Aggressive_Dark_HSpectre,
		Aggressive_Light,
		Aggressive_Light_HSpectre,
		Aggressive1_AAggressive2,
		Aggressive2_AAggressive1,
		AggressiveEscort,
		AggressiveSingleMonster,
		AggressiveSupportMonster,
		AggressiveSupportMonster2,
		AggressiveToIDElim,
		AggressiveToPCPet,
		AggressiveToShulack,
		AggressiveToShulack2,
		AirEl1,
		AirEl2,
		AirEl3,
		AirElboss,
		Anti_Crystal,
		APretor,
		ArcheryBasfelt_AtargetBasfelt_df1,
		ArcheryBasfelt_AtargetBasfelt_lf1,
		ArcheryBasfelt2_AtargetBasfelt2_df1,
		ArcheryBasfelt2_AtargetBasfelt2_lf1,
		Asist_D,
		Atauric,
		AtkDrakan,
		Bat_Family_Elite,
		BmDGuardian,
		BmLGuardian,
		Bomb_Lizardman,
		Brax,
		Brohum,
		Brownie,
		BrownieFeller_Hzaif_lf1,
		BrownieGuard,
		Calydon,
		Calydon_PolyMorph,
		CyclopsBoss,
		Consiade,
		Consiade_Sum,
		Crystal,
		Crystal_NmdD,
		Crystal_Sum,
		D1_Hkerubim_lf1,
		Dark_Lich,
		Dark_Mob,
		Dark_NPC,
		Dark_Sur_Mob,
		Daru,
		Daru_Hzaif,
		DRaggMob_ADRGuard1,
		Dragon,
		Dragon_Slave,
		DrakanDF3Boss,
		DrakanDF3Slave,
		DrakanDoor,
		DrakanPolymorph,
		DrakePurple_Master,
		DrakePurple_Slave,
		Draky_Bomb_Ex,
		Draky_Bomb_Master,
		Drama_EvE_nonPC_A,
		Drama_EvE_nonPC_B,
		Drama_Kimeia_DarkNPC,
		Drama_Kimeia_Mob,
		Drama_Kimeia_NPC,
		Dramata,
		DramataTimerA,
		DramataTimerB,
		Dummy,
		Dummy_DGuard,
		Dummy_LGuard,
		Dummy2,
		Dummy2_DGuard,
		Dummy2_LGuard,
		Elemental_Air,
		Elemental_Earth,
		Elemental_Fire,
		Elemental_Water,
		Escort,
		Ettin,
		F4Guard_Dark,
		F4Guard_Dragon,
		F4Guard_Light,
		F4Raid,
		Fanatic,
		Farmer_Hkerubim_lf1,
		Fethlot,
		Field_Object_All,
		Field_Object_All_Monster,
		Field_Object_Dark,
		Field_Object_Light,
		FireEl1,
		FireEl2,
		FireEl3,
		FireElboss,
		FireFungy,
		FireTemple_Mob,
		FriendlyToIDElim,
		FrillFaimamBaby,
		FrillFaimamMom,
		Fungy,
		Gargoyle,
		Gargoyle_Elite,
		General,
		General_ADaDr,
		General_Dark,
		General_Dragon,
		GeneralDa_ALiDr,
		GeneralDr_ALiDa,
		GhostLight,
		GHTimer,
		GMaster,
		Goblin,
		Golem_Switch,
		Griffo,
		Griffon,
		GSlave,
		Guard,
		Guard_Dark,
		Guard_Dark_AlycanAratman_df1,
		Guard_DarkMA,
		Guard_Dragon,
		Guard_DragonMA,
		Guard_FTargetBasfelt_df1,
		Guard_FTargetBasfelt_lf1,
		Guard_Light_Akerubim_lf1,
		Guard_LightMA,
		GuardDark_Alehpar,
		Guardian,
		Hippolizard,
		Holyservant,
		Holyservant_Debuffer,
		Holyservant_Despawn,
		Hostile_All,
		HostileOnlyMonster,
		IDCatacombs_Drake,
		IDCatacombs_Taros,
		IDElim,
		IDElim_Friend,
		IDLF1_Monster,
		IDTemple_Bugs,
		IDTemple_Stone,
		Kalnif_Aminx,
		Kalnif_Atog,
		Kerubim_Ad1_lf1,
		Kerubim_Afarmer_lf1,
		Krall,
		Krall_Training,
		KrallWizardCy,
		L_DRGuard_ADRaggMob1,
		Lasberg,
		Lehpar,
		Lehpar_AGuardDark,
		Lehpar_APretor,
		Lich_SouledStone,
		Light_Denlabis,
		Light_Lich,
		Light_Mob,
		Light_Sur_Mob,
		Lizardman,
		Lizardman_Bomb,
		Lycan,
		Lycan_Aguard_Dark_df1,
		Lycan_Hunter,
		Lycan_Mage,
		Lycan_Pet,
		Lycan_Pet_Training,
		Lycan_Sum,
		Lycan_Training,
		LycanDF2master,
		LycanDF2Slave1,
		LycanDF2Slave2,
		Lupyllini,
		Minx,
		Minx_HKalnif,
		Minx_HZaif,
		Monster,
		MosbearBaby,
		MosbearFather,
		Muta_Hoctaside,
		Neut,
		NeutBug,
		NeutQueen,
		NLizardman,
		NLizardman2,
		NLizardPet,
		NLizardPriest,
		NLizardraiser,
		NNaga,
		NNaga_Boss_Servant,
		NNaga_Elemental,
		NNaga_Elementalist,
		NNaga_Priest,
		NNaga_PriestBoss,
		NNaga_Servant,
		NoFight,
		None,
		Octaside_Amuta,
		OctasideBaby,
		Orc,
		PC,
		Pet,
		Pet_Dark,
		PolymorphFungy,
		Pretor_ALehpar,
		ProtectGuard_Dark,
		ProtectGuard_Light,
		Ranmark,
		Ratman,
		Ratman_Aguard_Dark_df1,
		RatmanDFworker,
		RatmanWorker,
		RobberAlder_Asprigg_df1,
		Sam_Elite,
		Samm,
		Seiren,
		Seiren_Master,
		Seiren_Snake,
		ShellizardBaby,
		ShellizardMom,
		Shulack,
		Shulack_Attacked,
		Shulack_Attacking,
		Shulack_Deck,
		Shulack_Deck_Killer,
		Shulack_Slave,
		Shulack_Support,
		SouledStone,
		SouledStone_Mini,
		Spaller,
		SpallerCtrl,
		Spectre_AALightDark,
		Sprigg_HrobberAlder_df1,
		SpriggRefuse_df1,
		Succubus_Elite,
		SwellFish,
		TargetBasfelt_df1,
		TargetBasfelt2_df1,
		Tauric,
		Test_Dark_ADragon,
		Test_Dark_AEtc,
		Test_Dark_ALight,
		Test_Dragon_ADark,
		Test_Dragon_AEtc,
		Test_Dragon_ALight,
		Test_Etc_ADark,
		Test_Etc_ADragon,
		Test_Etc_ALight,
		Test_Light_ADark,
		Test_Light_ADragon,
		Test_Light_AEtc,
		Tog,
		Tog_AKalnif,
		Towerman,
		Trico,
		Tricon,
		UndeadGradiator_df1,
		UseAll,
		UseAllNoneToMonster,
		XDrakan,
		XDrakan_Elementalist,
		XDrakan_Pet,
		XDrakan_Priest,
		XDrakan_Servant,
		Xipeto,
		XipetoBaby,
		Zaif,
		Zaif_AbrownieFeller_lf1,
		Zaif_Adaru,
		Zaif_AMinx,
	}

	[Serializable]
	public enum NpcFaction
	{
		Army_Da,
		Army_Li,
		BountyHunter_Da,
		BountyHunter_Li,
		GuardianOfDivine,
		GuardianOfTower,
		zeridith,
        // 2.1
        Mentee_Li,
        Mentee_Da,
        Mentor_Li,
        Mentor_Da,
	}

	[Serializable]
	public enum AbyssNpcType
	{
		artifact,
		artifact_effect_core,
		boss,
        BOSS = boss,
		defender,
		door,
		doorrepair,
		etc,
		guard,
		raid,
		shieldNPC_off,
		shieldNPC_on,
		Teleporter,
	}

    [Serializable]
    public enum DiskType
    {
        ancientclan,
        D1,
        D2,
        d2 = D2,
        D3,
        d3 = D3,
        D4,
        D5,
        D6,
        D7,
        drakan,
        e_air,
        e_earth,
        e_fire,
        e_water,
        etc,
        function,
        guard,
        inhabitant,
        merchant,
        polymorph_human,
        Polymorph_human_nocustom,
    }

    [Serializable]
    public enum CursorType
    {
        none = 0,
        None = none,
        action,
        attack,
        dark,
        general,
        light,
        talk,
        Talk = talk,
        trade,
        // 2.1
        monster,
        // 2.5
        coop,
    }
}

