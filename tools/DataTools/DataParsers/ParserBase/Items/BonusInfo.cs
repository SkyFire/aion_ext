namespace Jamie.Items
{
	using System;
	using System.Collections.Generic;
	using System.Xml.Schema;
	using System.Xml.Serialization;

	[Serializable]
	[XmlType(AnonymousType = true)]
	[XmlRootAttribute(ElementName = "bonuses", Namespace = "", IsNullable = false)]
	public class InvBonuses
	{
		[XmlElement("bonus_info", Form = XmlSchemaForm.Unqualified)]
		public List<BonusData> BonusItems;
	}

	[Serializable]
	[XmlType(AnonymousType = true)]
	public class BonusData
	{
		[XmlAttribute]
		public int questId;

		[XmlElement("boss", typeof(BossBonus))]
		[XmlElement("coin", typeof(CoinBonus))]
		[XmlElement("enchant", typeof(EnchantBonus))]
		[XmlElement("food", typeof(FoodBonus))]
		[XmlElement("fortress", typeof(FortressBonus))]
		[XmlElement("goods", typeof(GoodsBonus))]
		[XmlElement("island", typeof(IslandBonus))]
		[XmlElement("magical", typeof(MagicalBonus))]
		[XmlElement("manastone", typeof(ManastoneBonus))]
		[XmlElement("master_recipe", typeof(MasterRecipeBonus))]
		[XmlElement("material", typeof(MaterialBonus))]
		[XmlElement("medal", typeof(MedalBonus))]
		[XmlElement("medicine", typeof(MedicineBonus))]
		[XmlElement("movie", typeof(CutSceneBonus))]
		[XmlElement("recipe", typeof(RecipeBonus))]
		[XmlElement("redeem", typeof(RedeemBonus))]
		[XmlElement("task", typeof(WorkOrderBonus))]
		[XmlElement("wrap", typeof(WrappedBonus))]
		public List<AbstractInventoryBonus> BonusInfos;

	}

	[Serializable]
	[XmlType(AnonymousType = true)]
	public enum BonusType
	{
		NONE = 0,
		BOSS,			// %Quest_L_boss; siege related?
		COIN,			// %Quest_L_coin (mage)
		                //				 (warrior)
		                // %Quest_L_coin (mage)
		                //				 (warrior)
		ENCHANT,
		FOOD,			// %Quest_L_food
		FORTRESS,		// %Quest_L_fortress; sends promotion mails with medals?
		GODSTONE,
		GOODS,			// %Quest_L_Goods
		ISLAND,			// %Quest_L_3_island; siege related?
		MAGICAL,		// %Quest_L_magical
		MANASTONE,		// %Quest_L_matter_option
		MASTER_RECIPE,	// %Quest_ta_l_master_recipe
		MATERIAL,		// %Quest_L_material
		MEDAL,			// %Quest_L_medal
		MEDICINE,		// %Quest_L_medicine; potions and remedies
		MOVIE,			// %Quest_L_Christmas; cut scenes
		RECIPE,			// %Quest_L_Recipe
		REDEEM,			// %Quest_L_Rnd_Redeem and %Quest_L_redeem
		TASK,			// %Quest_L_task; craft related
	}
}
