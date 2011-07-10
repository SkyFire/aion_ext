namespace Jamie.ParserBase
{
	using System;
	using System.Collections.Generic;
	using System.ComponentModel;
	using System.Linq;
	using System.Xml.Schema;
	using System.Xml.Serialization;

	[Serializable]
	[XmlType(AnonymousType = true)]
	[XmlRoot(ElementName = "client_combine_recipes", Namespace = "", IsNullable = false)]
	public partial class ClientRecipesFile
	{
		[XmlElement("client_combine_recipe", Form = XmlSchemaForm.Unqualified)]
		public List<CombineRecipe> RecipeList;

		Dictionary<string, CombineRecipe> keyToRecipe = null;

		internal void CreateIndex() {
			if (this.RecipeList == null)
				return;
			keyToRecipe = this.RecipeList.ToDictionary(i => i.name, i => i, StringComparer.InvariantCultureIgnoreCase);
		}

		public CombineRecipe this[string recipeKey] {
			get {
				recipeKey = recipeKey.ToLower();
				if (keyToRecipe == null || !keyToRecipe.ContainsKey(recipeKey))
					return null;
				return keyToRecipe[recipeKey];
			}
		}
	}

	[Serializable]
	[XmlType(AnonymousType = true)]
	public partial class CombineRecipe
	{
		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public int id;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public string name;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public string desc;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public string desc_craftman;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public RecipeCombineSkills combineskill;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public RecipeRace qualification_race;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public int required_skillpoint;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public int auto_learn;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public string product;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public int product_quantity;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public int component_quantity;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public string component1;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public int compo1_quantity;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public string component2;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		[DefaultValue(0)]
		public int compo2_quantity;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public string component3;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		[DefaultValue(0)]
		public int compo3_quantity;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public string component4;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		[DefaultValue(0)]
		public int compo4_quantity;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public string component5;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		[DefaultValue(0)]
		public int compo5_quantity;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public string component6;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		[DefaultValue(0)]
		public int compo6_quantity;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public int max_production_count;

		[XmlIgnore]
		public bool max_production_countSpecified;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public string component7;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		[DefaultValue(0)]
		public int compo7_quantity;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public string combo1_product;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public string combo2_product;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		[DefaultValue(0)]
		public int require_dp;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		[DefaultValue(0)]
		public int craft_delay_id;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		[DefaultValue(0)]
		public int craft_delay_time;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		[DefaultValue(0)]
		public int task_type;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public string combo3_product;

		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public string combo4_product;

		public bool ContainsShopMaterials {
			get {
				return component1.StartsWith("shopmaterial_") ||
					   component2 != null && component2.StartsWith("shopmaterial_") ||
					   component3 != null && component3.StartsWith("shopmaterial_") ||
					   component4 != null && component4.StartsWith("shopmaterial_") ||
					   component5 != null && component5.StartsWith("shopmaterial_") ||
					   component6 != null && component6.StartsWith("shopmaterial_") ||
					   component7 != null && component7.StartsWith("shopmaterial_");
			}
		}

		public CombineRecipe() {
		}
	}

	[Serializable]
	[XmlType(AnonymousType = true)]
	public enum RecipeCombineSkills
	{
		alchemy,
		armorsmith,
		convert,
		cooking,
		handiwork,
		tailoring,
		weaponsmith
	}

	[Flags]
	[Serializable]
	[XmlType(AnonymousType = true)]
	public enum RecipeRace
	{
		pc_light = 1,
		pc_dark = 2,
		all = pc_dark | pc_light
	}
}
