namespace Jamie.Items
{
    using System;
    using System.ComponentModel;
    using System.Xml;
    using System.Xml.Schema;
    using System.Xml.Serialization;
    using Jamie.Skills;
    using Jamie.ParserBase;
	using Jamie.Pets;

    [Serializable]
    public class ItemBonus
    {
        public ItemBonus() { }

        public ItemBonus(ItemExport exported, BonusType type) {
            if (type != BonusType.NONE) {
                this.type = type;
                this.typeSpecified = true;
            }
            bonusLevel = exported.suffix.ToString();
        }

        [XmlAttribute]
        public BonusType type;

        [XmlIgnore]
        public bool typeSpecified;

        [XmlAttribute]
        public string bonusLevel;
    }

	[Serializable]
	public class ItemFeed
	{
		[XmlAttribute]
		public FoodType type;

		[XmlAttribute]
		[DefaultValue(0)]
		public int level;
	}

    [Serializable]
    public class RequireSkill
    {
        [XmlElement("skillId", Form = XmlSchemaForm.Unqualified)]
        public int[] skillId;

        [XmlAttribute]
        public int skilllvl;

        [XmlIgnore]
        public bool skilllvlSpecified;
    }

    [Serializable]
    public class Stigma
    {
        [XmlElement("require_skill", Form = XmlSchemaForm.Unqualified)]
        public RequireSkill[] require_skill;

        [XmlAttribute]
        public int shard;

        [XmlIgnore]
        public bool shardSpecified;

        [XmlAttribute]
        public int skilllvl;

        [XmlIgnore]
        public bool skilllvlSpecified;

        [XmlAttribute]
        public int skillid;

        [XmlIgnore]
        public bool skillidSpecified;
    }

    [Serializable]
    public class Godstone
    {
        [XmlAttribute]
        public int probability;

        [XmlIgnore]
        public bool probabilitySpecified;

        [XmlAttribute]
        [DefaultValue(0)]
        public int probabilityleft;

        [XmlAttribute]
        public int skilllvl;

        [XmlIgnore]
        public bool skilllvlSpecified;

        [XmlAttribute]
        public int skillid;

        [XmlIgnore]
        public bool skillidSpecified;
    }

    [XmlInclude(typeof(ToyPetSpawnAction))]
    [XmlInclude(typeof(CraftLearnAction))]
    [XmlInclude(typeof(QuestStartAction))]
    [XmlInclude(typeof(DyeAction))]
    [XmlInclude(typeof(ExtractAction))]
    [XmlInclude(typeof(EnchantItemAction))]
    [XmlInclude(typeof(SkillUseAction))]
    [XmlInclude(typeof(SkillLearnAction))]
    [XmlInclude(typeof(SplitAction))]
    [XmlInclude(typeof(ReadAction))]
    [XmlInclude(typeof(EmotionAction))]
    [XmlInclude(typeof(TitleAction))]
    [XmlInclude(typeof(TicketAction))]
    [XmlInclude(typeof(CosmeticAction))]
    [Serializable]
    public abstract partial class AbstractItemAction
    {
    }

    [Serializable]
    public class ToyPetSpawnAction : AbstractItemAction
    {
        [XmlAttribute]
        public int npcid;
        [XmlIgnore]
        public bool npcidSpecified;
    }

    [Serializable]
    public class CraftLearnAction : AbstractItemAction
    {
        [XmlAttribute]
        public int recipeid;

        [XmlIgnore]
        public bool recipeidSpecified;
    }

    [Serializable]
    public class QuestStartAction : AbstractItemAction
    {
        [XmlAttribute]
        public int questid;

        [XmlIgnore]
        public bool questidSpecified;
    }

    [Serializable]
    public class DyeAction : AbstractItemAction
    {
        [XmlAttribute]
        public string color;
    }

    [Serializable]
    public class ExtractAction : AbstractItemAction
    {
    }

    [Serializable]
    public class EnchantItemAction : AbstractItemAction
    {
        [XmlAttribute]
        public int count;
    }

    [Serializable]
    public class SkillUseAction : AbstractItemAction
    {
        public SkillUseAction() { }

        public SkillUseAction(int skillId, int level) {
            this.level = level;
            this.skillid = skillId;
        }

        [XmlAttribute]
        [DefaultValue(0)]
        public int level;

        [XmlAttribute]
        [DefaultValue(0)]
        public int skillid;
    }

    [Serializable]
    public class SkillLearnAction : AbstractItemAction
    {
        [XmlAttribute]
        public int skillid;

        [XmlIgnore]
        public bool skillidSpecified;

        [XmlAttribute]
        public skillPlayerClass @class;

        [XmlIgnore]
        public bool classSpecified;

        [XmlAttribute]
        public int level;

        [XmlIgnore]
        public bool levelSpecified;

        [XmlAttribute]
        public skillRace race;

        [XmlIgnore]
        public bool raceSpecified;
    }

    [Serializable]
    public class SplitAction : AbstractItemAction
    {
    }

    [Serializable]
    public class ReadAction : AbstractItemAction
    {
    }

    [Serializable]
    public class EmotionAction : AbstractItemAction
    {
        [XmlAttribute]
        public int emotionid;

        [XmlAttribute]
        public int expire;
    }

    [Serializable]
    public class TitleAction : AbstractItemAction
    {
        [XmlAttribute]
        public int titleid;

        [XmlAttribute]
        public int expire;
    }

    [Serializable]
    public class TicketAction : AbstractItemAction
    {
        [XmlAttribute]
        public ticketFunction function;

        [XmlAttribute]
        public int param;

        public TicketAction() {
            param = 1;
        }
    }

    [Serializable]
    public class CosmeticAction : AbstractItemAction
    {
        [XmlAttribute]
        [DefaultValue("")]
        public string lips;

        [XmlAttribute]
        [DefaultValue("")]
        public string eyes;

        [XmlAttribute]
        [DefaultValue("")]
        public string face;

        [XmlAttribute]
        [DefaultValue("")]
        public string hair;

        [XmlAttribute]
        [DefaultValue(0)]
        public int hairType;

        [XmlAttribute]
        [DefaultValue(0)]
        public int faceType;

        [XmlAttribute]
        [DefaultValue(0)]
        public int tattooType;

        [XmlAttribute]
        [DefaultValue(0)]
        public int makeupType;

        [XmlAttribute]
        [DefaultValue(0)]
        public int voiceType;

        [XmlAttribute]
        [DefaultValue("")]
        public string preset;
    }

    [Serializable]
    public enum ticketFunction
    {
        none = 0,
        addCube = 1,
        addWharehouse = 2,
    }

    [Serializable]
    public enum skillRace
    {
        PC_LIGHT,
        PC_DARK,
        ALL,
    }

    [Serializable]
    public class ItemActions
    {
        [XmlElement("skilllearn", Form = XmlSchemaForm.Unqualified)]
        public SkillLearnAction[] skilllearn;

        [XmlElement("skilluse", Form = XmlSchemaForm.Unqualified)]
        public SkillUseAction[] skilluse;

        [XmlElement("enchant", Form = XmlSchemaForm.Unqualified)]
        public EnchantItemAction[] enchant;

        [XmlElement("queststart", Form = XmlSchemaForm.Unqualified)]
        public QuestStartAction[] queststart;

        [XmlElement("dye", Form = XmlSchemaForm.Unqualified)]
        public DyeAction[] dye;

        [XmlElement("craftlearn", Form = XmlSchemaForm.Unqualified)]
        public CraftLearnAction[] craftlearn;

        [XmlElement("extract", Form = XmlSchemaForm.Unqualified)]
        public ExtractAction[] extract;

        [XmlElement("toypetspawn", Form = XmlSchemaForm.Unqualified)]
        public ToyPetSpawnAction[] toypetspawn;

        [XmlElement("split", Form = XmlSchemaForm.Unqualified)]
        public SplitAction[] split;

        [XmlElement("read", Form = XmlSchemaForm.Unqualified)]
        public ReadAction[] read;

        [XmlElement("emotion", Form = XmlSchemaForm.Unqualified)]
        public EmotionAction[] emotion;

        [XmlElement("title", Form = XmlSchemaForm.Unqualified)]
        public TitleAction[] title;

        [XmlElement("ticket", Form = XmlSchemaForm.Unqualified)]
        public TicketAction[] ticket;

        [XmlElement("cosmetic", Form = XmlSchemaForm.Unqualified)]
        public CosmeticAction[] cosmetic;
    }

    [Serializable]
    public class ItemTemplate
    {
        [XmlElement("modifiers", Form = XmlSchemaForm.Unqualified)]
        public Modifiers[] modifiers;

        [XmlElement("actions", Form = XmlSchemaForm.Unqualified)]
        public ItemActions[] actions;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public Godstone godstone;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public Stigma stigma;

        [XmlElement("bonus", Form = XmlSchemaForm.Unqualified)]
        public ItemBonus[] bonus;

		[XmlElement("feed", Form = XmlSchemaForm.Unqualified)]
		public ItemFeed[] feed;

        [XmlAttribute]
        public int id;

        [XmlAttribute]
        public int level;

        [XmlIgnore]
        public bool levelSpecified;

        [XmlAttribute]
        public int mask;

        [XmlIgnore]
        public bool maskSpecified;

        [XmlAttribute]
        public weaponType weapon_type;

        [XmlIgnore]
        public bool weapon_typeSpecified;

        [XmlAttribute]
        public armorType armor_type;

        [XmlIgnore]
        public bool armor_typeSpecified;

        [XmlAttribute]
        public int max_stack_count;

        [XmlIgnore]
        public bool max_stack_countSpecified;

        [XmlAttribute]
        public string item_type;

        [XmlAttribute]
        [DefaultValue("")]
        public string item_category;

        [XmlAttribute]
        public ItemQuality quality;

        [XmlIgnore]
        public bool qualitySpecified;

        [XmlAttribute]
        public int price;

        [XmlIgnore]
        public bool priceSpecified;

        [XmlAttribute]
        [DefaultValue(0)]
        public int ap;

        [XmlAttribute]
        [DefaultValue(0)]
        public int ai;

        [XmlAttribute]
        [DefaultValue(0)]
        public int aic;

        [XmlAttribute]
        [DefaultValue(0)]
        public int ei;

        [XmlAttribute]
        [DefaultValue(0)]
        public int eic;

        [XmlAttribute]
        [DefaultValue(0)]
        public int ci;

        [XmlAttribute]
        [DefaultValue(0)]
        public int cic;

        [XmlAttribute]
        [DefaultValue(ItemRace.ALL)]
        public ItemRace race;

        [XmlAttribute]
        [DefaultValue(ItemRace.ALL)]
        public ItemRace origRace;

        [XmlAttribute]
        public bool drop;

        [XmlIgnore]
        public bool dropSpecified;

        [XmlAttribute]
        public bool dye;

        [XmlIgnore]
        public bool dyeSpecified;

        [XmlAttribute]
        public bool can_proc_enchant;

        [XmlIgnore]
        public bool can_proc_enchantSpecified;

        [XmlAttribute]
        public bool no_enchant;

        [XmlIgnore]
        public bool no_enchantSpecified;

        [XmlAttribute]
        public int option_slot_bonus;

        [XmlIgnore]
        public bool option_slot_bonusSpecified;

        [XmlAttribute]
        public bool can_fuse;

        [XmlIgnore]
        public bool can_fuseSpecified;

        [XmlAttribute]
        public string restrict;

        [XmlAttribute]
        [DefaultValue("")]
        public string restrict_max;

        [XmlAttribute]
        public int desc;

        [XmlIgnore]
        public bool descSpecified;

        [XmlAttribute]
        public float attack_gap;

        [XmlIgnore]
        public bool attack_gapSpecified;

        [XmlAttribute]
        public string attack_type;

        [XmlAttribute]
        public int dmg_decal;

        [XmlIgnore]
        public bool dmg_decalSpecified;

        [XmlAttribute]
        [DefaultValue(0)]
        public int cash_item;

        [XmlAttribute]
        public int usedelay;

        [XmlIgnore]
        public bool usedelaySpecified;

        [XmlAttribute]
        public int usedelayid;

        [XmlIgnore]
        public bool usedelayidSpecified;

        [XmlAttribute]
        public int slot;

        [XmlIgnore]
        public bool slotSpecified;

        [XmlAttribute]
        public EquipType equipment_type;

        [XmlIgnore]
        public bool equipment_typeSpecified;

        [XmlAttribute]
        public string return_alias;

        [XmlAttribute]
        public int return_world;

        [XmlIgnore]
        public bool return_worldSpecified;

        [XmlAttribute]
        public int weapon_boost;

        [XmlIgnore]
        public bool weapon_boostSpecified;

        [XmlAttribute]
        [DefaultValue(Gender.ALL)]
        public Gender gender;

        [XmlAttribute]
        public string func_pet_name;

        [XmlAttribute]
        public BonusApplyType bonus_apply;

        [XmlIgnore]
        public bool bonus_applySpecified;

        [XmlAttribute]
        public string name;

        [XmlAttribute]
        public int quest;

        [XmlIgnore]
        public bool questSpecified;

        [XmlAttribute("expire_mins")]
        [DefaultValue(0)]
        public ExpireDuration expire_time;

        [XmlAttribute("cash_minute")]
        [DefaultValue(0)]
        public ExpireDuration cash_minute;

        [XmlAttribute("exchange_mins")]
        [DefaultValue(0)]
        public ExpireDuration exchange_mins;

		[XmlAttribute("world_drop")]
		[DefaultValue(false)]
		public bool world_drop;

        public bool HasActions() {
            return actions != null && (actions[0].craftlearn != null ||
                actions[0].dye != null || actions[0].enchant != null ||
                actions[0].extract != null || actions[0].queststart != null ||
                actions[0].read != null || actions[0].skilllearn != null ||
                actions[0].skilluse != null || actions[0].split != null ||
                actions[0].toypetspawn != null || actions[0].emotion != null ||
                actions[0].title != null);
        }
    }

    [Serializable]
    public enum EquipType
    {
        NONE = 0,
        ARMOR,
        WEAPON,
    }

    [Serializable]
    public enum BonusApplyType
    {
        EQUIP,
        INVENTORY,
    }

    [Serializable]
    [DefaultValue(ItemRace.ALL)]
    public enum ItemRace
    {
        ALL = 0,
        ELYOS,
        ASMODIANS,
    }

    [Serializable]
    public enum ItemQuality
    {
        COMMON,
        RARE,
        UNIQUE,
        LEGEND,
        MYTHIC,
        EPIC,
        JUNK,
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    [XmlRoot(ElementName = "item_templates", Namespace = "", IsNullable = false)]
    public class ItemTemplates
    {
        [XmlElement("item_template", Form = XmlSchemaForm.Unqualified)]
        public ItemTemplate[] TemplateList;
    }
}
