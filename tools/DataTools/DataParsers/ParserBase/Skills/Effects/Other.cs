namespace Jamie.Skills
{
	using System;
	using System.Collections.Generic;
	using System.ComponentModel;
	using System.Diagnostics;
	using System.Reflection;
	using System.Xml.Serialization;
	using Jamie.ParserBase;

	[Serializable]
	public sealed class OneTimeBoostHealEffect : Effect
	{
		[XmlAttribute]
		public float percent;

		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);

			this.percent = Int32.Parse(importObject.reserved[1]);
		}
	}

	[Serializable]
	public sealed class DeboostHealEffect : Effect
	{
		[XmlAttribute]
		public float percent;

		[XmlIgnore]
		public bool disable;

		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);

			if (importObject.reserved[0] != null) {
				int value = Int32.Parse(importObject.reserved[0].Trim());
				if (value == 99999) {
					this.disable = true; // healing is disabled for 60 secs
					percent = value;
					return;
				}
			}
			this.percent = Int32.Parse(importObject.reserved[1].Trim());
		}
	}

	[Serializable]
	public sealed class MagicCounterAtkEffect : Effect
	{
		[XmlAttribute]
		public int maxdmg;

		[XmlAttribute]
		public int percent;

		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);

			this.maxdmg = Int32.Parse(importObject.reserved[4].Trim());
			this.percent = Int32.Parse(importObject.reserved[1].Trim());
		}
	}

	[Serializable]
	public sealed class ChangeMpConsumptionEffect : Effect
	{
		[XmlAttribute]
		public int value;

		[XmlAttribute]
		public bool percent;

		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);

			this.value = Int32.Parse(importObject.reserved[1].Trim());
			this.percent = true;
		}
	}

	[Serializable]
	public sealed class RebirthEffect : Effect
	{
		[XmlAttribute]
		public int resurrect_percent;

		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);

			if (importObject.reserved[1] != null)
				resurrect_percent = Int32.Parse(importObject.reserved[1].Trim());
		}
	}

	[Serializable]
	public sealed class PulledEffect : Effect
	{
		// NO DATA
	}

	[Serializable]
	public sealed class OneTimeBoostSkillAttackEffect : Effect
	{
		[XmlAttribute]
		[DefaultValue(skillType.NONE)]
		public skillType type;

		[XmlAttribute]
		public int value;

		[XmlAttribute]
		public int count;

		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);

			// res11 and res12 - 0 500 ???
			this.type = (skillType)Enum.Parse(typeof(skillType), importObject.reserved[4], true);
			this.value = Int32.Parse(importObject.reserved[1].Trim());
			this.count = Int32.Parse(importObject.reserved[6].Trim());
		}
	}

	[Serializable]
	public enum skillType
	{
		NONE = 0,
		PHYSICAL = 1,
		MAGICAL = 2,
	}

	[Serializable]
	public sealed class PetOrderUseUltraSkillEffect : Effect
	{
		[XmlAttribute]
		public int ultra_skill;

		[XmlAttribute]
		[DefaultValue(false)]
		public bool disappear;

		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);

			this.ultra_skill = Int32.Parse(importObject.reserved[0].Trim());
			if (importObject.reserved[1] != null) {
				int additional = Int32.Parse(importObject.reserved[1].Trim());
				if (additional == 1) {
					disappear = true;
				} else {
					Debug.Print("Unknown value for PetOrderUseUltraSkillEffect: {0}", additional);
				}
			}
		}
	}

	[Serializable]
	public sealed class ReturnPointEffect : Effect
	{
		// NO DATA
	}

	[Serializable]
	public class ResurrectEffect : Effect
	{
		[XmlAttribute]
		[DefaultValue(0)]
		public int skill_id;

		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);

			ClientSkill skill = Utility.SkillIndex[importObject.reserved[5].Trim()];
			if (skill != null)
				this.skill_id = skill.id;
		}
	}

	[Serializable]
	public sealed class ResurrectPositionalEffect : ResurrectEffect
	{
	}

	[Serializable]
	public sealed class FearEffect : Effect
	{
		[XmlIgnore]
		public int value;

		[XmlIgnore]
		public int delta;

		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);

			this.value = Int32.Parse(importObject.reserved[1].Trim());
			if (importObject.reserved[0] != null)
				this.delta = Int32.Parse(importObject.reserved[0].Trim());
		}
	}

	[Serializable]
	public sealed class SkillLauncherEffect : Effect
	{
		[XmlAttribute]
		public int skill_id;

		[XmlAttribute]
		public int value;

		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);

			this.skill_id = Utility.SkillIndex[importObject.reserved[0].Trim()].id;
			this.value = Int32.Parse(importObject.reserved[1].Trim());
		}
	}

	[Serializable]
	public sealed class AuraEffect : Effect
	{
		[XmlAttribute]
		public int skill_id;

		[XmlAttribute]
		public int distance;

		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);

			//  E1 Reserved: CH_Chant_ImprovedMagic_G1_Effect null 20 10 Party Friend 1 0

			this.skill_id = Utility.SkillIndex[importObject.reserved[0].Trim()].id;
			// Party distance in %
			this.distance = Int32.Parse(importObject.reserved[2].Trim());
			// Friend distane in absolute values?
			// this.distance = Int32.Parse(importObject.reserved[3].Trim());
		}
	}

	[Serializable]
	public sealed class ConfuseEffect : Effect
	{
		[XmlAttribute]
		[DefaultValue(0)]
		public int value;

		[XmlAttribute]
		[DefaultValue(0)]
		public int delta;

		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);
			this.delta = importObject.remain[0].Value;
		}
	}

	[Serializable]
	public sealed class ParalyzeEffect : Effect
	{
		[XmlAttribute]
		[DefaultValue(0)]
		public int value;

		[XmlAttribute]
		[DefaultValue(0)]
		public int delta;

		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);
			if (importObject.remain[0] != null)
				this.delta = importObject.remain[0].Value;
			else
				this.delta = this.duration;
		}
	}

	[Serializable]
	public sealed class HostileUpEffect : Effect
	{
		[XmlAttribute]
		public int value;

		[XmlAttribute]
		public int delta;

		[XmlIgnore]
		[DefaultValue(EnmityType.NONE)]
		public EnmityType type;

		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);

			this.value = Int32.Parse(importObject.reserved[1].Trim());
			this.delta = Int32.Parse(importObject.reserved[0].Trim());
			this.type = (EnmityType)importObject.Skill.hostile_type;
		}
	}

	[Serializable]
	public enum EnmityType
	{
		NONE = 0,
		DIRECT = 1,
		INDIRECT = 2
	}

	[Serializable]
	public sealed class DiseaseEffect : Effect
	{
		// NO DATA
	}

	[Serializable]
	public sealed class BlindEffect : Effect
	{
		[XmlAttribute]
		[DefaultValue(0)]
		public int value;

		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);
			if (importObject.reserved[1] != null)
				this.value = Int32.Parse(importObject.reserved[1].Trim());
			else if (importObject.reserved[0] != null)
				this.value = Int32.Parse(importObject.reserved[0].Trim());
		}
	}

	[Serializable]
	public sealed class CurseEffect : Effect
	{
		[XmlAttribute]
		[DefaultValue(0)]
		public int value;

		[XmlAttribute]
		[DefaultValue(0)]
		public int delta;

		[XmlIgnore]
		public bool percent;

		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);

			this.percent = importObject.reserved[5].Trim() == "1";
			if (this.percent) {
				if (importObject.reserved[1] != null)
					this.value = Int32.Parse(importObject.reserved[1].Trim());
				if (importObject.reserved[0] != null)
					this.delta = Int32.Parse(importObject.reserved[0].Trim());
			} else {
				base.Import(importObject, typeof(CurseEffect), 1, 0, 1, 0, 5, Stat.MaxHP);
				base.Import(importObject, typeof(CurseEffect), 1, 0, 1, 0, 5, Stat.MaxMP);
			}
		}
	}

	[Serializable]
	public sealed class SilenceEffect : Effect
	{
		// res8 = 4 and res9 = 186 - special attack for water spirit
	}

    [Serializable]
    public sealed class BuffSilenceEffect : Effect
    {
    }

	[Serializable]
	public sealed class ReturnEffect : Effect
	{
		// ReturnHome effect
		// NO DATA
	}

	[Serializable]
	public sealed class SignetEffect : Effect
	{
		// res5 and res6 - stack level switches?
	}

	[Serializable]
	public class AbstractTransformEffect : Effect
	{
		[XmlAttribute]
		public int model;

		[XmlAttribute]
		[DefaultValue(TransformType.NONE)]
		public TransformType type;

		public AbstractTransformEffect() {
			this.type = TransformType.NONE;
		}

		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);

			if (importObject.reserved[7] != null)
				this.type = (TransformType)Enum.Parse(typeof(TransformType), importObject.reserved[7].Trim(), true);

			this.model = Utility.ClientNpcIndex[importObject.reserved[8].Trim()];
		}
	}

	[Serializable]
	public sealed class DeformEffect : AbstractTransformEffect
	{
	}

	[Serializable]
	public sealed class ShapeChangeEffect : AbstractTransformEffect
	{
	}

	[Serializable]
	public sealed class PolymorphEffect : AbstractTransformEffect
	{
	}

	[Serializable]
	public enum TransformType
	{
		NONE = 0,
		AVATAR,
		PC,
	}

	[Serializable]
	public sealed class SwitchHpMpEffect : Effect
	{
		// 2 skills with E1 Reserved: 0 100 0 100
	}

	[Serializable]
	public sealed class CloseAerialEffect : Effect
	{
		// NO DATA
	}

	[Serializable]
	public sealed class OpenAerialEffect : Effect
	{
		// NO DATA
		// res15 and res16 - summon data?
	}

	[Serializable]
	public sealed class BindEffect : Effect
	{
		// NO DATA
	}

    [Serializable]
    public sealed class BuffBindEffect : Effect
    {
        // NO DATA
    }

	[Serializable]
	public sealed class SpinEffect : Effect
	{
		// res16 values - 1 or 2
	}

	[Serializable]
	public sealed class StaggerEffect : Effect
	{
		// res16 values - 1 or 2
	}

	[Serializable]
	public sealed class StumbleEffect : Effect
	{
		// res16 values - 1 or 2
	}

	[Serializable]
	public sealed class SlowEffect : Effect
	{
		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);
			if (importObject.ChangeStat == Stat.None)
				importObject.ChangeStat = Stat.AttackDelay;
			base.Import(importObject, typeof(SlowEffect), 1, 0, 1, 0, 5, Stat.None);
		}
	}

	[Serializable]
	public sealed class SnareEffect : Effect
	{
		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);
			base.Import(importObject, typeof(SnareEffect), 1, 0, 1, 0, 5, Stat.Speed);
			base.Import(importObject, typeof(SnareEffect), 1, 0, 1, 0, 5, Stat.FlySpeed);

			foreach (var change in this.change)
				change.value = -change.value;
		}
	}

	[Serializable]
	public class SleepEffect : Effect
	{
		// NO DATA
		// res1 and res2 contains 0, summon order skills ?
	}

	[Serializable]
	public sealed class BuffSleepEffect : SleepEffect
	{
	}

	[Serializable]
	public sealed class StunEffect : Effect
	{

	}

    [Serializable]
    public sealed class BuffStunEffect : Effect
    {

    }

	[Serializable]
	public sealed class RootEffect : Effect
	{
		[XmlAttribute]
		public int resistchance;

		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);

			if (importObject.reserved[1] != null)
				this.resistchance = Int32.Parse(importObject.reserved[1].Trim());
		}
	}

    [Serializable]
    public sealed class SimpleRootEffect : Effect
    {
    }

	[Serializable]
	public sealed class SearchEffect : Effect
	{
		[XmlAttribute]
		public int value;

		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);
			this.value = Int16.Parse(importObject.reserved[6]);
		}
	}

	[Serializable]
	public sealed class HideEffect : Effect
	{
		[XmlAttribute]
		public int value;

		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);
			base.change = new ChangeList();
			var change = new Change();
			change.func = StatFunc.PERCENT;
			change.stat = modifiersenum.SPEED;
			change.value = Int16.Parse(importObject.reserved[1]) - 100;
			base.change.Add(change);
			this.value = Int16.Parse(importObject.reserved[6]);
		}
	}

	[Serializable]
	public sealed class SwitchHostileEffect : Effect
	{
		// NO DATA
	}

	[Serializable]
	public sealed class RandomMoveLocEffect : Effect
	{
		[XmlAttribute]
		public int value;

		[XmlAttribute]
		public string direction;

		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);

			this.value = Int32.Parse(importObject.reserved[1].Trim());
			int dir = Int32.Parse(importObject.reserved[2].Trim());
			if (dir == 1)
				direction = "FRONT";
			else if (dir == 2)
				direction = "BACK";
			else {
				Debug.Print("Unknown direction for RandomMoveLocEffect: {0}", importObject.Skill.id);
			}
		}
	}

	[Serializable]
	public sealed class BoostSkillCastingTimeEffect : Effect
	{
		[XmlAttribute]
		[DefaultValue(skillSubType.NONE)]
		public skillSubType type;

		[XmlAttribute]
		public int percent;

		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);

			this.percent = Int32.Parse(importObject.reserved[1].Trim());
			type = (skillSubType)Enum.Parse(typeof(skillSubType), importObject.reserved[2].Trim(), true);
		}
	}

	[Serializable]
	public sealed class NoFlyEffect : Effect
	{
		// NO DATA
	}

    [Serializable]
    public sealed class XPBoostEffect : Effect
    {
        [XmlAttribute]
        public int percent;

        public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
            base.Import(importObject, getters);

            this.percent = Int32.Parse(importObject.reserved[1].Trim());
        }
    }

    [Serializable]
    public sealed class ExtendAuraRangeEffect : Effect
    {
        [XmlAttribute]
        public int value;

        public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
            base.Import(importObject, getters);

            this.value = Int32.Parse(importObject.reserved[1].Trim());
        }
    }

	[Serializable]
	public sealed class RecallInstantEffect : Effect
	{

	}

	[Serializable]
	public sealed class InvulnerableWingEffect : Effect
	{

	}

    [Serializable]
    public sealed class WeaponDualEffect : Effect
    {
		[XmlAttribute]
		public int value;

		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);

			this.value = Int32.Parse(importObject.reserved[5].Trim());
		}
    }
}
