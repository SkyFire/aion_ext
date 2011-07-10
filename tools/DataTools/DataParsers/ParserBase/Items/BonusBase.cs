namespace Jamie.Items
{
	using System;
	using System.Collections.Generic;
	using System.ComponentModel;
	using System.Linq;
	using System.Reflection;
	using System.Xml;
	using System.Xml.Serialization;
	using Jamie.ParserBase;
	using Jamie.Quests;

	[Serializable]
	[XmlType(AnonymousType = false)]
	[XmlInclude(typeof(BossBonus))]
	[XmlInclude(typeof(CoinBonus))]
	[XmlInclude(typeof(EnchantBonus))]
	[XmlInclude(typeof(FoodBonus))]
	[XmlInclude(typeof(FortressBonus))]
	[XmlInclude(typeof(GoodsBonus))]
	[XmlInclude(typeof(IslandBonus))]
	[XmlInclude(typeof(MagicalBonus))]
	[XmlInclude(typeof(ManastoneBonus))]
	[XmlInclude(typeof(MasterRecipeBonus))]
	[XmlInclude(typeof(MaterialBonus))]
	[XmlInclude(typeof(MedalBonus))]
	[XmlInclude(typeof(MedicineBonus))]
	[XmlInclude(typeof(CutSceneBonus))]
	[XmlInclude(typeof(RecipeBonus))]
	[XmlInclude(typeof(RedeemBonus))]
	[XmlInclude(typeof(WorkOrderBonus))]
	[XmlInclude(typeof(WrappedBonus))]
	public abstract class AbstractInventoryBonus
	{
		public abstract BonusType getType();

		[XmlAttribute]
		[DefaultValue(0)]
		public int bonusLevel;

		[XmlAttribute]
		[DefaultValue(0)]
		public int count;

		public static AbstractInventoryBonus Create(BonusType bonusType) {
			switch (bonusType) {
				case BonusType.BOSS:
					return new BossBonus();
				case BonusType.COIN:
					return new CoinBonus();
				case BonusType.ENCHANT:
					return new EnchantBonus();
				case BonusType.FOOD:
					return new FoodBonus();
				case BonusType.FORTRESS:
					return new FortressBonus();
				case BonusType.GOODS:
					return new GoodsBonus();
				case BonusType.ISLAND:
					return new IslandBonus();
				case BonusType.MAGICAL:
					return new MagicalBonus();
				case BonusType.MANASTONE:
					return new ManastoneBonus();
				case BonusType.MASTER_RECIPE:
					return new MasterRecipeBonus();
				case BonusType.MATERIAL:
					return new MaterialBonus();
				case BonusType.MEDAL:
					return new MedalBonus();
				case BonusType.MEDICINE:
					return new MedicineBonus();
				case BonusType.MOVIE:
					return new CutSceneBonus();
				case BonusType.RECIPE:
					return new RecipeBonus();
				case BonusType.REDEEM:
					return new RedeemBonus();
				case BonusType.TASK:
					return new WorkOrderBonus();
				default:
					throw new ArgumentException("bonusType");
			}
		}
	}

	[Serializable]
	[XmlType(AnonymousType = false)]
	public abstract class SimpleCheckItemBonus : AbstractInventoryBonus
	{
		[XmlAttribute]
		[DefaultValue(0)]
		public int checkItem;
	}

	[Serializable]
	[XmlType(AnonymousType = false)]
	public class BossBonus : AbstractInventoryBonus, IDynamicImport<Quest>
	{
		static readonly BonusType type = BonusType.BOSS;

		public override BonusType getType() {
			return type;
		}

		#region IDynamicImport<Quest> Members

		public void Import(Quest importObject, IEnumerable<FieldInfo> getters) {
			string data = (string)getters.First().GetValue(importObject);
			if (data == null)
				return;
			if (data.StartsWith("%Quest_")) {
				bonusLevel = Utility.GetLevelFromName(data);
			}
		}

		#endregion
	}

	[XmlType(AnonymousType = false)]
	public class CoinBonus : SimpleCheckItemBonus, IDynamicImport<Quest>
	{
		static readonly BonusType type = BonusType.COIN;

		public CoinBonus() { }

		#region IDynamicImport<Quest> Members

		public void Import(Quest importObject, IEnumerable<FieldInfo> getters) {
			string data = (string)getters.First().GetValue(importObject);
			if (data == null)
				return;
			if (data.StartsWith("%Quest_")) {
				bonusLevel = Utility.GetLevelFromName(data);
			} else {
				string[] itemData = data.Split(new char[] { ' ' }, StringSplitOptions.RemoveEmptyEntries);
				Item item = Utility.ItemIndex.GetItem(itemData[0]);
				if (item != null && item.id > 0) {
					checkItem = item.id;
					if (itemData.Length > 1)
						count = Int32.Parse(itemData[1]);
					else
						count = 1;
				}
			}
		}

		#endregion

		public override BonusType getType() {
			return type;
		}
	}

	[XmlType(AnonymousType = false)]
	public class CutSceneBonus : AbstractInventoryBonus, IDynamicImport<Quest>
	{
		static readonly BonusType type = BonusType.MOVIE;

		[XmlAttribute]
		[DefaultValue(Gender.ALL)]
		public Gender gender;

		[XmlAttribute]
		public int movieId;

		[XmlAttribute]
		[DefaultValue(0)]
		public int checkItem;

		public override BonusType getType() {
			return type;
		}

		#region IDynamicImport<Quest> Members

		public void Import(Quest importObject, IEnumerable<FieldInfo> getters) {
			string data = (string)getters.First().GetValue(importObject);
			if (data == null)
				return;
			if (data.StartsWith("%Quest_")) {
				bonusLevel = Utility.GetLevelFromName(data);
			} else {
				string[] itemData = data.Split(new char[] { ' ' }, StringSplitOptions.RemoveEmptyEntries);
				Item item = Utility.ItemIndex.GetItem(itemData[0]);
				if (item != null && item.id > 0) {
					checkItem = item.id;
					if (itemData.Length > 1)
						count = Int32.Parse(itemData[1]);
					else
						count = 1;
				}
			}
		}

		#endregion
	}

	[XmlType(AnonymousType = false)]
	public class EnchantBonus : SimpleCheckItemBonus, IDynamicImport<Quest>
	{
		static readonly BonusType type = BonusType.ENCHANT;

		public override BonusType getType() {
			return type;
		}

        #region IDynamicImport<Quest> Members

        public void Import(Quest importObject, IEnumerable<FieldInfo> getters) {
            string data = (string)getters.First().GetValue(importObject);
            if (data == null)
                return;
            if (data.StartsWith("%Quest_")) {
                if (Char.IsLetter(data[data.Length - 1]))
                    data = data.Remove(data.Length - 1, 1);
                int under = data.LastIndexOf('_');
                data = data.Substring(under + 1, data.Length - under - 1);
                bonusLevel = Int32.Parse(data);
            } else {
                string[] itemData = data.Split(new char[] { ' ' }, StringSplitOptions.RemoveEmptyEntries);
                Item item = Utility.ItemIndex.GetItem(itemData[0]);
                if (item != null && item.id > 0) {
                    checkItem = item.id;
                    if (itemData.Length > 1)
                        count = Int32.Parse(itemData[1]);
                    else
                        count = 1;
                }
            }
        }

        #endregion
    }

	[XmlType(AnonymousType = false)]
	public class FoodBonus : SimpleCheckItemBonus, IDynamicImport<Quest>
	{
		static readonly BonusType type = BonusType.FOOD;

		public override BonusType getType() {
			return type;
		}

		#region IDynamicImport<Quest> Members

		public void Import(Quest importObject, IEnumerable<FieldInfo> getters) {
			string data = (string)getters.First().GetValue(importObject);
			if (data == null)
				return;
			if (data.StartsWith("%Quest_")) {
				bonusLevel = Utility.GetLevelFromName(data);
			} else {
				string[] itemData = data.Split(new char[] { ' ' }, StringSplitOptions.RemoveEmptyEntries);
				Item item = Utility.ItemIndex.GetItem(itemData[0]);
				if (item != null && item.id > 0) {
					checkItem = item.id;
					if (itemData.Length > 1)
						count = Int32.Parse(itemData[1]);
					else
						count = 1;
				}
			}
		}

		#endregion
	}

	[XmlType(AnonymousType = false)]
	public class FortressBonus : AbstractInventoryBonus, IDynamicImport<Quest>
	{
		static readonly BonusType type = BonusType.FORTRESS;

		[XmlAttribute]
		[DefaultValue(0)]
		public int checkItem;

		public override BonusType getType() {
			return type;
		}

		#region IDynamicImport<Quest> Members

		public void Import(Quest importObject, IEnumerable<FieldInfo> getters) {
			string data = (string)getters.First().GetValue(importObject);
			if (data == null)
				return;
			if (data.StartsWith("%Quest_")) {
				bonusLevel = Utility.GetLevelFromName(data);
			} else {
				string[] itemData = data.Split(new char[] { ' ' }, StringSplitOptions.RemoveEmptyEntries);
				Item item = Utility.ItemIndex.GetItem(itemData[0]);
				if (item != null && item.id > 0) {
					checkItem = item.id;
					if (itemData.Length > 1)
						count = Int32.Parse(itemData[1]);
					else
						count = 1;
				}
			}
		}

		#endregion
	}

	[XmlType(AnonymousType = false)]
	public class GoodsBonus : AbstractInventoryBonus, IDynamicImport<Quest>
	{
		static readonly BonusType type = BonusType.GOODS;

		[XmlAttribute]
		[DefaultValue(0)]
		public int checkItem;

		public override BonusType getType() {
			return type;
		}

		#region IDynamicImport<Quest> Members

		public void Import(Quest importObject, IEnumerable<FieldInfo> getters) {
			string data = (string)getters.First().GetValue(importObject);
			if (data == null)
				return;
			if (data.StartsWith("%Quest_")) {
				bonusLevel = Utility.GetLevelFromName(data);
			} else {
				string[] itemData = data.Split(new char[] { ' ' }, StringSplitOptions.RemoveEmptyEntries);
				Item item = Utility.ItemIndex.GetItem(itemData[0]);
				if (item != null && item.id > 0) {
					checkItem = item.id;
					if (itemData.Length > 1)
						count = Int32.Parse(itemData[1]);
					else
						count = 1;
				}
			}
		}

		#endregion
	}

	[XmlType(AnonymousType = false)]
	public class IslandBonus : AbstractInventoryBonus, IDynamicImport<Quest>
	{
		static readonly BonusType type = BonusType.ISLAND;

		public override BonusType getType() {
			return type;
		}

		#region IDynamicImport<Quest> Members

		public void Import(Quest importObject, IEnumerable<FieldInfo> getters) {
			string data = (string)getters.First().GetValue(importObject);
			if (data == null)
				return;
			if (data.StartsWith("%Quest_")) {
				bonusLevel = Utility.GetLevelFromName(data);
			}
		}

		#endregion
	}

	[XmlType(AnonymousType = false)]
	public class MagicalBonus : SimpleCheckItemBonus, IDynamicImport<Quest>
	{
		static readonly BonusType type = BonusType.MAGICAL;

		public override BonusType getType() {
			return type;
		}

		#region IDynamicImport<Quest> Members

		public void Import(Quest importObject, IEnumerable<FieldInfo> getters) {
			string data = (string)getters.First().GetValue(importObject);
			if (data == null)
				return;
			if (data.StartsWith("%Quest_")) {
				bonusLevel = Utility.GetLevelFromName(data);
			} else {
				string[] itemData = data.Split(new char[] { ' ' }, StringSplitOptions.RemoveEmptyEntries);
				Item item = Utility.ItemIndex.GetItem(itemData[0]);
				if (item != null && item.id > 0) {
					checkItem = item.id;
					if (itemData.Length > 1)
						count = Int32.Parse(itemData[1]);
					else
						count = 1;
				}
			}
		}

		#endregion
	}

	[XmlType(AnonymousType = false)]
	public class ManastoneBonus : SimpleCheckItemBonus, IDynamicImport<Quest>
	{
		static readonly BonusType type = BonusType.MANASTONE;

		public override BonusType getType() {
			return type;
		}

		#region IDynamicImport<Quest> Members

		public void Import(Quest importObject, IEnumerable<FieldInfo> getters) {
			string data = (string)getters.First().GetValue(importObject);
			if (data == null)
				return;
			if (data.StartsWith("%Quest_")) {
                if (Char.IsLetter(data[data.Length - 1]))
                    data = data.Remove(data.Length - 1, 1);
                int under = data.LastIndexOf('_');
                data = data.Substring(under + 1, data.Length - under - 1);
                bonusLevel = Int32.Parse(data);
			} else {
				string[] itemData = data.Split(new char[] { ' ' }, StringSplitOptions.RemoveEmptyEntries);
				Item item = Utility.ItemIndex.GetItem(itemData[0]);
				if (item != null && item.id > 0) {
					checkItem = item.id;
					if (itemData.Length > 1)
						count = Int32.Parse(itemData[1]);
					else
						count = 1;
				}
			}
		}

		#endregion
	}

	[XmlType(AnonymousType = false)]
	public class MasterRecipeBonus : SimpleCheckItemBonus, IDynamicImport<Quest>
	{
		static readonly BonusType type = BonusType.MASTER_RECIPE;

		[XmlAttribute]
		public int skillId;

		public override BonusType getType() {
			return type;
		}

		#region IDynamicImport<Quest> Members

		public void Import(Quest importObject, IEnumerable<FieldInfo> getters) {
			string data = (string)getters.First().GetValue(importObject);
			if (data == null)
				return;
			if (data.StartsWith("%Quest_")) {
				skillId = Utility.GetSkillIdFromName(data);
			} else {
				string[] itemData = data.Split(new char[] { ' ' }, StringSplitOptions.RemoveEmptyEntries);
				Item item = Utility.ItemIndex.GetItem(itemData[0]);
				if (item != null && item.id > 0) {
					checkItem = item.id;
					if (itemData.Length > 1)
						count = Int32.Parse(itemData[1]);
					else
						count = 1;
				}
			}
		}

		#endregion
	}

	[XmlType(AnonymousType = false)]
	public class MaterialBonus : SimpleCheckItemBonus, IDynamicImport<Quest>
	{
		static readonly BonusType type = BonusType.MATERIAL;

		public override BonusType getType() {
			return type;
		}

		#region IDynamicImport<Quest> Members

		public void Import(Quest importObject, IEnumerable<FieldInfo> getters) {
			string data = (string)getters.First().GetValue(importObject);
			if (data == null)
				return;
			if (data.StartsWith("%Quest_")) {
				bonusLevel = Utility.GetLevelFromName(data);
			} else {
				string[] itemData = data.Split(new char[] { ' ' }, StringSplitOptions.RemoveEmptyEntries);
				Item item = Utility.ItemIndex.GetItem(itemData[0]);
				if (item != null && item.id > 0) {
					checkItem = item.id;
					if (itemData.Length > 1)
						count = Int32.Parse(itemData[1]);
					else
						count = 1;
				}
			}
		}

		#endregion
	}

	[XmlType(AnonymousType = false)]
	public class MedalBonus : SimpleCheckItemBonus, IDynamicImport<Quest>
	{
		static readonly BonusType type = BonusType.MEDAL;

		public override BonusType getType() {
			return type;
		}

		#region IDynamicImport<Quest> Members

		public void Import(Quest importObject, IEnumerable<FieldInfo> getters) {
			string data = (string)getters.First().GetValue(importObject);
			if (data == null)
				return;
			if (data.StartsWith("%Quest_")) {
				bonusLevel = Utility.GetLevelFromName(data);
			} else {
				string[] itemData = data.Split(new char[] { ' ' }, StringSplitOptions.RemoveEmptyEntries);
				Item item = Utility.ItemIndex.GetItem(itemData[0]);
				if (item != null && item.id > 0) {
					checkItem = item.id;
					if (itemData.Length > 1)
						count = Int32.Parse(itemData[1]);
					else
						count = 1;
				}
			}
		}

		#endregion
	}

	[XmlType(AnonymousType = false)]
	public class MedicineBonus : SimpleCheckItemBonus, IDynamicImport<Quest>
	{
		static readonly BonusType type = BonusType.MEDICINE;

		public override BonusType getType() {
			return type;
		}

		#region IDynamicImport<Quest> Members

		public void Import(Quest importObject, IEnumerable<FieldInfo> getters) {
			string data = (string)getters.First().GetValue(importObject);
			if (data == null)
				return;
			if (data.StartsWith("%Quest_")) {
				bonusLevel = Utility.GetLevelFromName(data);
			} else {
				string[] itemData = data.Split(new char[] { ' ' }, StringSplitOptions.RemoveEmptyEntries);
				Item item = Utility.ItemIndex.GetItem(itemData[0]);
				if (item != null && item.id > 0) {
					checkItem = item.id;
					if (itemData.Length > 1)
						count = Int32.Parse(itemData[1]);
					else
						count = 1;
				}
			}
		}

		#endregion
	}

	[XmlType(AnonymousType = false)]
	public class RecipeBonus : SimpleCheckItemBonus, IDynamicImport<Quest>
	{
		static readonly BonusType type = BonusType.RECIPE;

		public override BonusType getType() {
			return type;
		}

		#region IDynamicImport<Quest> Members

		public void Import(Quest importObject, IEnumerable<FieldInfo> getters) {
			string data = (string)getters.First().GetValue(importObject);
			if (data == null)
				return;
			if (data.StartsWith("%Quest_")) {
				bonusLevel = Utility.GetLevelFromName(data);
			} else {
				string[] itemData = data.Split(new char[] { ' ' }, StringSplitOptions.RemoveEmptyEntries);
				Item item = Utility.ItemIndex.GetItem(itemData[0]);
				if (item != null && item.id > 0) {
					checkItem = item.id;
					if (itemData.Length > 1)
						count = Int32.Parse(itemData[1]);
					else
						count = 1;
				}
			}
		}

		#endregion
	}

	[XmlType(AnonymousType = false)]
	public class RedeemBonus : AbstractInventoryBonus, IDynamicImport<Quest>
	{
		static readonly BonusType type = BonusType.REDEEM;

		[XmlAttribute]
		[DefaultValue(0)]
		public int checkItem;

		public override BonusType getType() {
			return type;
		}

		#region IDynamicImport<Quest> Members

		public void Import(Quest importObject, IEnumerable<FieldInfo> getters) {
			string data = (string)getters.First().GetValue(importObject);
			if (data == null)
				return;
			if (data.StartsWith("%Quest_")) {
				bonusLevel = Utility.GetLevelFromName(data);
			} else {
				string[] itemData = data.Split(new char[] { ' ' }, StringSplitOptions.RemoveEmptyEntries);
				Item item = Utility.ItemIndex.GetItem(itemData[0]);
				if (item != null && item.id > 0) {
					checkItem = item.id;
					if (itemData.Length > 1)
						count = Int32.Parse(itemData[1]);
					else
						count = 1;
				}
			}
		}

		#endregion
	}

	[XmlType(AnonymousType = false)]
	public class WorkOrderBonus : SimpleCheckItemBonus, IDynamicImport<Quest>
	{
		static readonly BonusType type = BonusType.TASK;

		[XmlAttribute]
		public int skillId;

		public override BonusType getType() {
			return type;
		}

		#region IDynamicImport<Quest> Members

		public void Import(Quest importObject, IEnumerable<FieldInfo> getters) {
			string data = (string)getters.First().GetValue(importObject);
			if (data == null)
				return;
			if (data.StartsWith("%Quest_")) {
				skillId = Utility.GetSkillIdFromName(data);
			} else {
				string[] itemData = data.Split(new char[] { ' ' }, StringSplitOptions.RemoveEmptyEntries);
				Item item = Utility.ItemIndex.GetItem(itemData[0]);
				if (item != null && item.id > 0) {
					checkItem = item.id;
					if (itemData.Length > 1)
						count = Int32.Parse(itemData[1]);
					else
						count = 1;
				}
			}
		}

		#endregion
	}

	[XmlType(AnonymousType = false)]
	public class WrappedBonus : AbstractInventoryBonus
	{
		public WrappedBonus() {
			maxCount = 5; // unknown; just set to reasonable value
		}

		public WrappedBonus(BonusType bonusType) {
			type = bonusType;
		}

		[XmlAttribute(AttributeName = "itemId")]
		[DefaultValue(0)]
		public int originalItemId;

		[XmlAttribute]
		[DefaultValue(0)]
		public int maxCount;

		[XmlAttribute]
		public BonusType type;

		public override BonusType getType() {
			return type;
		}
	}
}
