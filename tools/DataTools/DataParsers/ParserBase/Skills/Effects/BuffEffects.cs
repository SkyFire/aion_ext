namespace Jamie.Skills
{
	using System;
	using System.Collections.Generic;
	using System.ComponentModel;
	using System.Reflection;
	using System.Xml.Serialization;
	using Jamie.ParserBase;

	[XmlInclude(typeof(OneTimeBoostSkillCriticalEffect))]
	[XmlInclude(typeof(ResurrectBaseEffect))]
	[XmlInclude(typeof(BoostHateEffect))]
	[XmlInclude(typeof(StatdownEffect))]
	[XmlInclude(typeof(ArmorMasteryEffect))]
	[XmlInclude(typeof(WeaponMasteryEffect))]
	[XmlInclude(typeof(BoostHealEffect))]
	[XmlInclude(typeof(BoostSkillCastingTimeEffect))]
	[XmlInclude(typeof(WeaponStatupEffect))]
	[XmlInclude(typeof(WeaponStatboostEffect))]
	[XmlInclude(typeof(StatboostEffect))]
	[XmlInclude(typeof(StatupEffect))]
	[Serializable]
	public abstract partial class BufEffect : Effect
	{
	}

	[Serializable]
	public sealed class OneTimeBoostSkillCriticalEffect : BufEffect
	{
		[XmlAttribute]
		public int count;

		[XmlIgnore]
		public int maxboost;

		// what is that? max percentage and max value? E1 Reserved: 0 70 null null;  0 0 0 1000;

		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);

			this.count = Int32.Parse(importObject.reserved[6].Trim());
			if (importObject.reserved[3] != null) {
				this.maxboost = Int32.Parse(importObject.reserved[3].Trim());
			}
		}
	}

	[Serializable]
	public sealed class ResurrectBaseEffect : BufEffect
	{
		// NO DATA
	}

	[Serializable]
	public sealed class BoostHateEffect : BufEffect
	{
		[XmlAttribute]
		[DefaultValue(0)]
		public int value;

		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);

			this.value = Int32.Parse(importObject.reserved[1].Trim());
			this.change = new ChangeList();
			var change = new Change();
			change.value = this.value;
			if (importObject.reserved[0] != null)
				change.delta = Int32.Parse(importObject.reserved[0].Trim());
			change.func = StatFunc.PERCENT;
			change.stat = modifiersenum.BOOST_HATE;
			this.change.Add(change);
		}
	}

	[Serializable]
	public sealed class StatdownEffect : BufEffect
	{
		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);

			Stat stat = Stat.None;
			base.Import(importObject, typeof(StatdownEffect), 1, 0, 1, 0, 5, stat);

			if (importObject.reserved[13] != null) {
				stat = (Stat)Enum.Parse(typeof(Stat), importObject.reserved[13].Trim(), true);
				base.Import(importObject, typeof(StatdownEffect), 3, 2, 3, 2, 6, stat);
			}
			if (importObject.reserved[17] != null) {
				stat = (Stat)Enum.Parse(typeof(Stat), importObject.reserved[17].Trim(), true);
				base.Import(importObject, typeof(StatdownEffect), 15, 14, 15, 14, 16, stat);
			}
			foreach (var change in this.change) {
				change.value = -change.value;
			}
		}
	}

	[Serializable]
	public sealed class ArmorMasteryEffect : BufEffect
	{
		[XmlAttribute]
		public armorType armor;

		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);

			this.armor = Utility.GetArmorType(importObject.reserved[4]);
			this.change = new ChangeList();
			var change = new Change();
			change.stat = modifiersenum.PHYSICAL_DEFENSE;
			change.value = Int32.Parse(importObject.reserved[1]);
			change.delta = Int32.Parse(importObject.reserved[0]);
			change.func = StatFunc.PERCENT;
			this.change.Add(change);
		}
	}

	[Serializable]
	public sealed class WeaponMasteryEffect : BufEffect
	{
		public WeaponMasteryEffect() { }

		public WeaponMasteryEffect(weaponType weapon) {
			this.weapon = weapon;
		}

		[XmlAttribute]
		public weaponType weapon;

		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);

			this.weapon = Utility.GetWeaponType(importObject.reserved[4]);
			this.change = new ChangeList();
			var change = new Change();
			change.stat = modifiersenum.PHYSICAL_ATTACK;
			change.value = Int32.Parse(importObject.reserved[1]);
			change.delta = Int32.Parse(importObject.reserved[0]);
			change.func = StatFunc.PERCENT;
			this.change.Add(change);
		}
	}

	[Serializable]
	public sealed class BoostHealEffect : BufEffect
	{
		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);

			this.change = new ChangeList();
			var change = new Change();
			change.stat = modifiersenum.BOOST_HEAL;
			change.func = StatFunc.ADD;
			change.value = Int32.Parse(importObject.reserved[1].Trim()) * 10;
			this.change.Add(change);
		}
	}

	[Serializable]
	public sealed class WeaponStatupEffect : BufEffect
	{
		[XmlAttribute]
		public weaponType weapon;

		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);
			base.Import(importObject, typeof(WeaponStatupEffect), 1, 0, 1, 0, 5, Stat.None);

			this.weapon = Utility.GetWeaponType(importObject.reserved[4]);
		}
	}

	[Serializable]
	public sealed class WeaponStatboostEffect : BufEffect
	{
		[XmlAttribute]
		public weaponType weapon;

		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);
			base.Import(importObject, typeof(WeaponStatboostEffect), 1, 0, 3, 2, 5, Stat.None);

			this.weapon = Utility.GetWeaponType(importObject.reserved[4]);
		}
	}

	[Serializable]
	public sealed class StatboostEffect : BufEffect
	{
		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);
			base.Import(importObject, typeof(StatboostEffect), 1, 0, 3, 2, 5, Stat.None);
		}
	}

	[Serializable]
	public sealed class StatupEffect : BufEffect
	{
		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);

			Stat stat = Stat.None;
			base.Import(importObject, typeof(StatupEffect), 1, 0, 1, 0, 5, stat);

			if (importObject.reserved[13] != null) {
				stat = (Stat)Enum.Parse(typeof(Stat), importObject.reserved[13].Trim(), true);
				base.Import(importObject, typeof(StatupEffect), 3, 2, 3, 2, 6, stat);
			}
			if (importObject.reserved[17] != null) {
				stat = (Stat)Enum.Parse(typeof(Stat), importObject.reserved[17].Trim(), true);
				base.Import(importObject, typeof(StatupEffect), 15, 14, 15, 14, 16, stat);
			}
		}
	}

    [Serializable]
    public sealed class ShieldMasteryEffect : BufEffect
    {
        public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
            base.Import(importObject, getters);

            this.change = new ChangeList();
            var change = new Change();
            change.stat = modifiersenum.BLOCK;
            change.func = StatFunc.PERCENT;
            change.value = Int32.Parse(importObject.reserved[1].Trim());
            this.change.Add(change);
        }
    }
}
