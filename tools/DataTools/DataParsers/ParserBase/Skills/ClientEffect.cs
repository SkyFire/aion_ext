namespace Jamie.Skills
{
	using System;
	using System.Collections.Generic;
	using System.Linq;
	using System.Text;
	using System.Reflection;
	using System.Diagnostics;
	using Jamie.ParserBase;

	public class ClientEffect : IDynamicImport<ClientSkill>
	{
		public ClientEffect() {
			checkforchain = true;
		}

		public int?[] accuracy_modifiers;

		public int basiclv;

		public bool checkforchain;

		public int checktime;

		public bool cond_attack_dir;

		public int?[] cond_attack_dir_prob;

		public PreeffectNumber cond_preeffect;

		public int?[] cond_preeffect_prob;

		public string cond_race;

		public int?[] cond_race_prob;

		public string cond_status;

		public int?[] cond_status_prob;

		public int?[] critical_add_dmg_modifiers;

		public int?[] critical_prob_modifiers;

		public int effectid;

		public bool hidemsg;

		public int hop_a;
		public int hop_b;

		public HopType hop_type;

		public bool noresist;

		public int randomtime;

		public int?[] remain;

		public string reserved_cond1;

		public int?[] reserved_cond1_prob;

		public string reserved_cond2;

		public int?[] reserved_cond2_prob;

		public string[] reserved;

		public string target_type;

		public EffectType type;

		public int e;

		public Stat ChangeStat { get; set; }

		public ClientSkill Skill { get; set; }

		public SkillTemplate Template { get; set; }

		#region IDynamicImport<ClientSkill> Members

		public void Import(ClientSkill importObject, IEnumerable<FieldInfo> getters) {
			foreach (var fd in getters) {
				List<int> valueList = new List<int>();
				int fdNameStart = fd.Name.IndexOf('_');
				string effectName = fd.Name.Substring(0, fdNameStart);
				char n = effectName[effectName.Length - 1];
				e = Int32.Parse(n.ToString());
				string fdName = fd.Name.Substring(fdNameStart, fd.Name.Length - fdNameStart);
				if (fdName.StartsWith("_acc_mod")) {
					string ns = fdName.Remove(0, 8);
					int number = Int32.Parse(ns);
					if (accuracy_modifiers == null)
						accuracy_modifiers = new int?[number];
					else if (number > accuracy_modifiers.Length)
						Array.Resize(ref accuracy_modifiers, number);
					accuracy_modifiers[number - 1] = (int?)fd.GetValue(importObject);
				} else if (fdName == "_basiclv") {
					basiclv = (int)fd.GetValue(importObject);
				} else if (fdName == "_checktime") {
					checktime = (int)fd.GetValue(importObject);
				} else if (fdName.StartsWith("_critical_add_dmg_mod")) {
					string ns = fdName.Remove(0, 21);
					int number = Int32.Parse(ns);
					if (critical_add_dmg_modifiers == null)
						critical_add_dmg_modifiers = new int?[number];
					else if (number > critical_add_dmg_modifiers.Length)
						Array.Resize(ref critical_add_dmg_modifiers, number);
					critical_add_dmg_modifiers[number - 1] = (int?)fd.GetValue(importObject);
				} else if (fdName.StartsWith("_critical_prob_mod")) {
					string ns = fdName.Remove(0, 18);
					int number = Int32.Parse(ns);
					if (critical_prob_modifiers == null)
						critical_prob_modifiers = new int?[number];
					else if (number > critical_prob_modifiers.Length)
						Array.Resize(ref critical_prob_modifiers, number);
					critical_prob_modifiers[number - 1] = (int?)fd.GetValue(importObject);
				} else if (fdName == "_effectid") {
					effectid = (int)fd.GetValue(importObject);
				} else if (fdName == "_hidemsg") {
					hidemsg = (bool)fd.GetValue(importObject);
				} else if (fdName == "_hop_a") {
					hop_a = (int)fd.GetValue(importObject);
				} else if (fdName == "_hop_b") {
					hop_b = (int)fd.GetValue(importObject);
				} else if (fdName == "_hop_type") {
					string value = (string)fd.GetValue(importObject);
					if (value == null)
						hop_type = HopType.NONE;
					else
						hop_type = (HopType)Enum.Parse(typeof(HopType), value, true);
				} else if (fdName == "_noresist") {
					noresist = (bool)fd.GetValue(importObject);
				} else if (fdName == "_randomtime") {
					randomtime = (int)fd.GetValue(importObject);
				} else if (fdName.StartsWith("_remain")) {
					string ns = fdName.Remove(0, 7);
					int number = Int32.Parse(ns);
					if (remain == null)
						remain = new int?[number];
					else if (number > remain.Length)
						Array.Resize(ref remain, number);
					remain[number - 1] = (int?)fd.GetValue(importObject);
				} else if (fdName == "_reserved_cond1") {
					reserved_cond1 = (string)fd.GetValue(importObject);
				} else if (fdName == "_reserved_cond2") {
					reserved_cond2 = (string)fd.GetValue(importObject);
				} else if (fdName.StartsWith("_reserved_cond1_prob")) {
					string ns = fdName.Remove(0, 20);
					int number = Int32.Parse(ns);
					if (reserved_cond1_prob == null)
						reserved_cond1_prob = new int?[number];
					else if (number > reserved_cond1_prob.Length)
						Array.Resize(ref reserved_cond1_prob, number);
					reserved_cond1_prob[number - 1] = (int?)fd.GetValue(importObject);
				} else if (fdName.StartsWith("_reserved_cond2_prob")) {
					string ns = fdName.Remove(0, 20);
					int number = Int32.Parse(ns);
					if (reserved_cond2_prob == null)
						reserved_cond2_prob = new int?[number];
					else if (number > reserved_cond2_prob.Length)
						Array.Resize(ref reserved_cond2_prob, number);
					reserved_cond2_prob[number - 1] = (int?)fd.GetValue(importObject);
				} else if (fdName.StartsWith("_reserved")) {
					string ns = fdName.Remove(0, 9);
					int number = Int32.Parse(ns);
					if (reserved == null)
						reserved = new string[number];
					else if (number > reserved.Length)
						Array.Resize(ref reserved, number);
					string value = (string)fd.GetValue(importObject);
					int valueInt;
					if (value == null || number != 13 || Int32.TryParse(value, out valueInt))
						reserved[number - 1] = value;
					else {
						try {
							Stat stat = (Stat)Enum.Parse(typeof(Stat), value, true);
							ChangeStat = stat;
							reserved[number - 1] = stat.ToString();
						} catch {
							Debug.Print("Unknown reserved13: {0}", value);
						}
					}
				} else if (fdName == "_target_type") {
					target_type = (string)fd.GetValue(importObject);
				} else if (fdName == "_type") {
					string value = (string)fd.GetValue(importObject);
					if (value == null)
						type = EffectType.None;
					else {
						try {
							type = (EffectType)Enum.Parse(typeof(EffectType), value, true);
						} catch {
							Debug.Print("Unknown effect: {0}", value);
						}
					}
				} else if (fdName.StartsWith("_cond_race_prob")) {
					string ns = fdName.Remove(0, 15);
					int number = Int32.Parse(ns);
					if (cond_race_prob == null)
						cond_race_prob = new int?[number];
					else if (number > cond_race_prob.Length)
						Array.Resize(ref cond_race_prob, number);
					cond_race_prob[number - 1] = (int?)fd.GetValue(importObject);
				} else if (fdName == "_cond_race") {
					cond_race = (string)fd.GetValue(importObject);
				} else if (fdName.StartsWith("_cond_attack_dir_prob")) {
					string ns = fdName.Remove(0, 21);
					int number = Int32.Parse(ns);
					if (cond_attack_dir_prob == null)
						cond_attack_dir_prob = new int?[number];
					else if (number > cond_attack_dir_prob.Length)
						Array.Resize(ref cond_attack_dir_prob, number);
					cond_attack_dir_prob[number - 1] = (int?)fd.GetValue(importObject);
				} else if (fdName == "_cond_attack_dir") {
					string value = (string)fd.GetValue(importObject);
					if (value != null)
						cond_attack_dir = value.Trim() != "0";
				} else if (fdName.StartsWith("_cond_preeffect_prob")) {
					string ns = fdName.Remove(0, 20);
					int number = Int32.Parse(ns);
					if (cond_preeffect_prob == null)
						cond_preeffect_prob = new int?[number];
					else if (number > cond_preeffect_prob.Length)
						Array.Resize(ref cond_preeffect_prob, number);
					cond_preeffect_prob[number - 1] = (int?)fd.GetValue(importObject);
				} else if (fdName == "_cond_preeffect") {
					cond_preeffect = (PreeffectNumber)fd.GetValue(importObject);
				} else if (fdName == "_cond_status") {
					cond_status = (string)fd.GetValue(importObject);
				}
			}
		}

		#endregion
	}

	[Serializable]
	public enum HitType
	{
		All = 0,
		EveryHit = 1,
		PhHit = 2,
		MaHit = 3,
		NmlAttack = 4,
		NmlAtk = NmlAttack
	}

	[Serializable]
	public enum Stat
	{
		None = 0,
		ActiveDefend,
		Agi,
		AllPara,
		AllResist,
		AllSpeed,
		ArAll,
        arDeform,
		ArRoot,
		ArSleep,
		ArSnare,
		ArSpin,
		ArStagger,
		ArStumble,
		ArStun,
		ArStunLike,
		AttackDelay,
		AttackRange,
		Block,
		BoostCastingTime,
		Concentration,
		Critical,
		Dex,
		Dodge,
		ElementalDefendAir,
		ElementalDefendAll,
		ElementalDefendDark,
		ElementalDefendEarth,
		ElementalDefendFire,
		ElementalDefendLight,
		ElementalDefendWater,
		ErAir,
		ErEarth,
		ErFire,
		ErWater,
		FlySpeed,
		FPRegen,
		HealSkillBoost,
		HitAccuracy,
		HP,
		HPRegen,
		Kno,
		KnoWil,
		MagicalAttack,
		MagicalCritical,
		MagicalCriticalDamageReduce,
		MagicalCriticalReduceRate,
		MagicalDefend,
		MagicalHitAccuracy,
		MagicalResist,
		MagicalSkillBoost,
		MPRegen,
		MaxFP,
		MaxHP,
		MaxMP,
		MP,
		OpenAerial,
		Parry,
		PhyAttack,
		PhysicalCriticalDamageReduce,
		PhysicalCriticalReduceRate,
		PhysicalDefend,
		PMAttack,
		PMDefend,
		PVPAttackRatio,
		PVPDefendRatio,
		SkillLv,
		Speed,
		Stagger_Arp,
		Str,
		Stumble,
		Stumble_Arp,
		Stun_Arp,
		Vit,
		Wil,
	}
}
