namespace Jamie.Skills
{
	using System;
	using System.Collections.Generic;
	using System.ComponentModel;
	using System.Diagnostics;
	using System.Linq;
	using System.Reflection;
	using System.Xml.Serialization;
	using Jamie.ParserBase;

	[XmlInclude(typeof(ProcAtkInstantEffect))]
	[XmlInclude(typeof(DispelBuffCounterAtkEffect))]
	[XmlInclude(typeof(MoveBehindEffect))]
	[XmlInclude(typeof(SkillAtkDrainInstantEffect))]
	[XmlInclude(typeof(SpellAtkDrainInstantEffect))]
	[XmlInclude(typeof(DelayDamageEffect))]
	[XmlInclude(typeof(SignetBurstEffect))]
	[XmlInclude(typeof(CarveSignetEffect))]
	[XmlInclude(typeof(BackDashEffect))]
	[XmlInclude(typeof(DashEffect))]
	[XmlInclude(typeof(SpellAttackInstantEffect))]
	[XmlInclude(typeof(SkillAttackInstantEffect))]
	[XmlInclude(typeof(PoisonEffect))]
	[XmlInclude(typeof(BleedEffect))]
	[XmlInclude(typeof(SpellAttackEffect))]
	[Serializable]
	public abstract partial class DamageEffect : Effect
	{
		[XmlAttribute]
		public int value;

		[XmlAttribute]
		[DefaultValue(0)]
		public int delta;

		public void AddModifiers(string condition, ClientEffect importObject, int valuePos, int deltaPos) {
			string[] condParts = condition.Split(new string[] { ",", " " }, StringSplitOptions.RemoveEmptyEntries);
			this.modifiers = new ActionModifiers();

			foreach (string cond in condParts) {
				if (cond.StartsWith("_race_")) {
					string race = cond.Remove(0, 6);
					SkillTargetRace targetRace = SkillTargetRace.PC_ALL;
					if (race == "pc_light") {
						targetRace = SkillTargetRace.ELYOS;
					} else if (race == "pc_dark") {
						targetRace = SkillTargetRace.ASMODIANS;
					} else {
						targetRace = (SkillTargetRace)Enum.Parse(typeof(SkillTargetRace), race, true);
					}
					if (targetRace != SkillTargetRace.PC_ALL) {
						if (this.modifiers.targetrace == null)
							this.modifiers.targetrace = new List<TargetRaceDamageModifier>();
						var mod = new TargetRaceDamageModifier();
						mod.race = targetRace;
                        if (importObject.reserved[valuePos] != null)
						    mod.value = Int32.Parse(importObject.reserved[valuePos].Trim());
						if (importObject.reserved[deltaPos] != null)
							mod.delta = Int32.Parse(importObject.reserved[deltaPos].Trim());
						this.modifiers.targetrace.Add(mod);
					}
				} else {
					try {
						TargetState targetState = new NamedEnum<TargetState>(cond);
						if (targetState == TargetState.BACK) {
							this.modifiers.backdamage = new BackDamageModifier();
							this.modifiers.backdamage.value = Int32.Parse(importObject.reserved[valuePos].Trim());
							this.modifiers.backdamage.delta = Int32.Parse(importObject.reserved[deltaPos].Trim());
						} else if (targetState == TargetState.FRONT) {
							this.modifiers.frontdamage = new FrontDamageModifier();
							this.modifiers.frontdamage.value = Int32.Parse(importObject.reserved[valuePos].Trim());
							this.modifiers.frontdamage.delta = Int32.Parse(importObject.reserved[deltaPos].Trim());
						} else if (targetState == TargetState.FLYING) {
							this.modifiers.flyingdamage = new FlyingDamageModifier();
							if (importObject.reserved[valuePos] != null)
								this.modifiers.flyingdamage.value = Int32.Parse(importObject.reserved[valuePos].Trim());
							if (importObject.reserved[deltaPos] != null)
								this.modifiers.flyingdamage.delta = Int32.Parse(importObject.reserved[deltaPos].Trim());
						} else if (targetState == TargetState.NON_FLYING) {
							this.modifiers.nonflyingdamage = new NonFlyingDamageModifier();
							if (importObject.reserved[valuePos] != null)
								this.modifiers.nonflyingdamage.value = Int32.Parse(importObject.reserved[valuePos].Trim());
							if (importObject.reserved[deltaPos] != null)
								this.modifiers.nonflyingdamage.delta = Int32.Parse(importObject.reserved[deltaPos].Trim());
						} else {
							if (this.modifiers.abnormaldamage == null)
								this.modifiers.abnormaldamage = new List<AbnormalDamageModifier>();
							var abnormaldamage = new AbnormalDamageModifier();
							abnormaldamage.type = targetState;
							if (importObject.reserved[valuePos] != null)
								abnormaldamage.value = Int32.Parse(importObject.reserved[valuePos].Trim());
							if (importObject.reserved[deltaPos] != null)
								abnormaldamage.delta = Int32.Parse(importObject.reserved[deltaPos].Trim());
							this.modifiers.abnormaldamage.Add(abnormaldamage);
						}
						if (condParts.Length > 1) {
							Debug.Print("WARN: More than 1 modifier for skill: {0}", importObject.Skill.id);
						}
					} catch {
						Debug.Print("Modifier '{0}' not handled", cond);
					}
				}
			}

			if (!this.modifiers.Present)
				this.modifiers = null;
		}
	}

	[Serializable]
	public partial class ProcAtkInstantEffect : AbstractVdpEffect
	{
		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);
			// Stagger 1 (13, 14) ?? Skill Id = 8344
		}
	}

	[Serializable]
	public partial class MoveBehindEffect : DamageEffect
	{
		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);

			this.value = Int32.Parse(importObject.reserved[3].Trim());
			this.delta = Int32.Parse(importObject.reserved[2].Trim());
		}
	}

	[Serializable]
	public abstract class AbstractAtkDrainInstantEffect : DamageEffect
	{
		[XmlAttribute]
		[DefaultValue(0)]
		public int hp;

		[XmlAttribute]
		[DefaultValue(0)]
		public int mp;

		[XmlAttribute]
		[DefaultValue(false)]
		public bool percent;
	}


	[Serializable]
	public partial class SkillAtkDrainInstantEffect : AbstractAtkDrainInstantEffect
	{
		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);

			if (importObject.reserved[14] != null) {
				hp = Int32.Parse(importObject.reserved[14].Trim());
				percent = true;
			}

			if (importObject.reserved[16] != null) {
				mp = Int32.Parse(importObject.reserved[16].Trim());
				percent = true;
			}

			if (importObject.reserved[3] != null)
				this.value = Int32.Parse(importObject.reserved[3].Trim());
			if (importObject.reserved[2] != null)
				this.delta = Int32.Parse(importObject.reserved[2].Trim());

			if (this.value == 0 && this.delta == 0) {
				if (importObject.reserved[1] != null)
					this.value = Int32.Parse(importObject.reserved[1].Trim());
				if (importObject.reserved[0] != null)
					this.delta = Int32.Parse(importObject.reserved[0].Trim());
			}
		}
	}

	[Serializable]
	public enum HealType
	{
		NONE = 0,
		HP,
		MP,
		DP,
		FP,
	}

	[Serializable]
	public partial class SpellAtkDrainInstantEffect : AbstractAtkDrainInstantEffect
	{
		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);

			this.percent = importObject.reserved[5].Trim() == "1";
			this.value = Int32.Parse(importObject.reserved[1].Trim());
			if (importObject.reserved[0] != null)
				this.delta = Int32.Parse(importObject.reserved[0].Trim());

			if (importObject.reserved[14] != null)
				this.hp = Int32.Parse(importObject.reserved[14].Trim());

			if (importObject.reserved[16] != null)
				this.mp = Int32.Parse(importObject.reserved[16].Trim());

			if (importObject.reserved[6] != null) {
				string condition = importObject.reserved[6].Trim().ToLower();
				base.AddModifiers(condition, importObject, 3, 2);
			}
		}
	}

	[Serializable]
	public partial class DelayDamageEffect : DamageEffect
	{
		[XmlAttribute]
		public int delay;

		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);

			this.delay = Int32.Parse(importObject.reserved[8].Trim());
			// some value at res8 (time or final damage?)
			this.value = Int32.Parse(importObject.reserved[1].Trim());
			this.delta = Int32.Parse(importObject.reserved[0].Trim());
		}
	}

	[Serializable]
	public partial class SignetBurstEffect : DamageEffect
	{
		[XmlAttribute]
		public string signet;

		[XmlAttribute]
		public int signetlvl; // %e1.SignetBurst.SignetGrade

		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);

			this.signet = "SYSTEM_SKILL_SIGNET" + importObject.reserved[6].Trim();
			this.signetlvl = Int32.Parse(importObject.reserved[7]);

			this.value = Int32.Parse(importObject.reserved[1]);
			this.delta = Int32.Parse(importObject.reserved[0]);

			int[] exceptions = { 823, 824, 853, 11573 };
			bool isException = exceptions.Contains(importObject.Skill.id);

			if (isException) {
				this.subeffect = new SubEffect();
				string skillName = importObject.reserved[12].Trim();
				ClientSkill skill = Utility.SkillIndex[skillName];
				if (skill == null)
					skill = Utility.SkillIndex[String.Format("NormalAttack_{0}", skillName)];
				this.subeffect.skill_id = skill.id;
				return;
			}

			if (importObject.reserved[14] != null) {
				this.subeffect = new SubEffect();
				this.subeffect.skill_id = Utility.SkillIndex[importObject.reserved[14].Trim()].id;
			}

			if (importObject.reserved[12] != null) {
				base.AddModifiers(importObject.reserved[12].Trim().ToLower(), importObject, 5, 4);
			}
		}
	}

	[Serializable]
	public partial class CarveSignetEffect : DamageEffect
	{
		[XmlAttribute]
		public int signetlvl;

		[XmlAttribute]
		public int signetid;

		[XmlAttribute]
		public string signet;

		[XmlAttribute]
		public int probability;

		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);

			this.signet = "SYSTEM_SKILL_SIGNET" + importObject.reserved[12].Trim();
			this.signetlvl = Int32.Parse(importObject.reserved[13]);
			string signetName = String.Format("Signet1_{0}", this.signetlvl);
			ClientSkill skill = Utility.SkillIndex[signetName];
			if (skill == null) {
				Debug.Print("Missing signet skill '{0}' for skillId {1}", signetName, importObject.Skill.id);
			} else {
				signetid = skill.id;
			}

			if (importObject.reserved[3] != null) {
				this.value = Int32.Parse(importObject.reserved[3]);
				this.delta = Int32.Parse(importObject.reserved[2]);
			} else if (importObject.reserved[1] != null) {
				this.value = Int32.Parse(importObject.reserved[1]);
				this.delta = Int32.Parse(importObject.reserved[0]);
			}

			if (importObject.reserved[6] != null) {
				string subName = "NormalAttack_" + importObject.reserved[6].Trim();
				this.subeffect = new SubEffect();
				this.subeffect.skill_id = Utility.SkillIndex[subName].id;
			}

			this.probability = Int32.Parse(importObject.reserved[15]);
		}
	}

	[Serializable]
	public partial class BackDashEffect : DamageEffect
	{
		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);

			this.value = Int32.Parse(importObject.reserved[3].Trim());
			this.delta = Int32.Parse(importObject.reserved[2].Trim());
		}
	}

	[Serializable]
	public partial class DashEffect : DamageEffect
	{
		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);

			this.value = Int32.Parse(importObject.reserved[3].Trim());
			this.delta = Int32.Parse(importObject.reserved[2].Trim());
		}
	}

	[Serializable]
	public partial class SpellAttackInstantEffect : DamageEffect // AbstractVdpEffect
	{
		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);

			if (importObject.reserved[15] != null) {
				base.AddModifiers(importObject.reserved[15].Trim().ToLower(), importObject, 8, 7);
			}

			if (importObject.reserved[13] != null) {
				this.subeffect = new SubEffect();
				string skilLvl = importObject.reserved[14].Trim();
				string genericName = importObject.reserved[13].Trim();
				if (genericName.StartsWith("N"))
					genericName = genericName.Substring(1);
				if (Char.IsDigit(genericName[genericName.Length - 1])) {
					// override skill level
					skilLvl = genericName.Substring(genericName.Length - 1, 1);
					genericName = genericName.Substring(0, genericName.Length - 1);
				}
				string shortName = String.Format("NormalAttack_{0}", genericName);

				string skillName;
				if (skilLvl == null || skilLvl == "0")
					skillName = shortName;
				else
					skillName = String.Format("NormalAttack_{0}_{1}", genericName, skilLvl);
				ClientSkill skill = Utility.SkillIndex[skillName];
				if (skill == null) {
					skill = Utility.SkillIndex[shortName];
					if (skill == null)
						skill = Utility.SkillIndex[genericName];
				}
				if (skill == null) {
					Debug.Print("Subeffect skill not found: {0}", genericName);
				} else {
					this.subeffect.skill_id = skill.id;
				}
			}

			if (importObject.reserved[1] != null) {
				this.value = Int32.Parse(importObject.reserved[1].Trim());
				if (importObject.reserved[0] != null)
					this.delta = Int32.Parse(importObject.reserved[0].Trim());
			}
		}
	}

	[Serializable]
	public partial class SkillAttackInstantEffect : DamageEffect
	{
        [XmlAttribute]
        [DefaultValue(0)]
        public int value2;

        [XmlAttribute]
        [DefaultValue(0)]
        public int delta2;

		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);

			if (importObject.reserved[15] != null) {
				base.AddModifiers(importObject.reserved[15].Trim().ToLower(), importObject, 9, 8);
			}
			if (importObject.reserved[13] != null) {
				this.subeffect = new SubEffect();
				string skilLvl = importObject.reserved[14].Trim();
				string genericName = importObject.reserved[13].Trim();
				if (genericName.StartsWith("N"))
					genericName = genericName.Substring(1);
				if (Char.IsDigit(genericName[genericName.Length - 1])) {
					// override skill level
					skilLvl = genericName.Substring(genericName.Length - 1, 1);
					genericName = genericName.Substring(0, genericName.Length - 1);
				}
				string shortName = String.Format("NormalAttack_{0}", genericName);

				string skillName;
				if (skilLvl == null || skilLvl == "0")
					skillName = shortName;
				else
					skillName = String.Format("NormalAttack_{0}_{1}", genericName, skilLvl);
				ClientSkill skill = Utility.SkillIndex[skillName];
				if (skill == null) {
					skill = Utility.SkillIndex[shortName];
					if (skill == null)
						skill = Utility.SkillIndex[genericName];
				}
				if (skill == null) {
					Debug.Print("Subeffect skill not found: {0}", genericName);
				} else {
					this.subeffect.skill_id = skill.id;
				}
			}

			if (importObject.reserved[3] != null) {
				this.value = Int32.Parse(importObject.reserved[3].Trim());
				if (importObject.reserved[2] != null)
					this.delta = Int32.Parse(importObject.reserved[2].Trim());
			}
			if (importObject.reserved[1] != null) {
				this.value2 = Int32.Parse(importObject.reserved[1].Trim());
				if (importObject.reserved[0] != null)
					this.delta2 = Int32.Parse(importObject.reserved[0].Trim());
			}
		}
	}

	[Serializable]
	public partial class DelayedFPAttackInstantEffect : DamageEffect
	{
		[XmlAttribute]
		public int delay;

		[XmlAttribute]
		[DefaultValue(false)]
		public bool percent;

		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);

			this.percent = importObject.reserved[5].Trim() == "1";
			this.value = Int32.Parse(importObject.reserved[1].Trim());
			if (importObject.reserved[0] != null)
				this.delta = Int32.Parse(importObject.reserved[0].Trim());

			this.delay = Int32.Parse(importObject.reserved[8].Trim());
		}
	}

	[Serializable]
	public partial class MpAttackInstantEffect : DamageEffect
	{
		[XmlAttribute]
		[DefaultValue(false)]
		public bool percent;

		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);

			this.percent = importObject.reserved[5].Trim() == "1";
			this.value = Int32.Parse(importObject.reserved[1].Trim());
			if (importObject.reserved[0] != null)
				this.delta = Int32.Parse(importObject.reserved[0].Trim());
		}
	}

	public partial class FpAttackInstantEffect : DamageEffect
	{
		[XmlAttribute]
		[DefaultValue(false)]
		public bool percent;

		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);

			this.percent = importObject.reserved[5].Trim() == "1";
			this.value = Int32.Parse(importObject.reserved[1].Trim());
			if (importObject.reserved[0] != null)
				this.delta = Int32.Parse(importObject.reserved[0].Trim());
		}
	}


    [Serializable]
    public sealed class SpellAtkDrainEffect : AbstractAtkDrainInstantEffect
    {
        public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
            base.Import(importObject, getters);

            this.value = Int32.Parse(importObject.reserved[8].Trim());
            this.delta = Int32.Parse(importObject.reserved[7].Trim());

            this.hp = Int32.Parse(importObject.reserved[14].Trim());
        }
    }
}
