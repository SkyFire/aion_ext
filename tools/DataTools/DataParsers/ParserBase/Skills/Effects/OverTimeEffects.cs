namespace Jamie.Skills
{
	using System;
	using System.Xml.Serialization;
	using System.ComponentModel;
	using System.Reflection;
	using System.Collections.Generic;

	[Serializable]
	public abstract class OverTimeEffect : Effect
	{
		[XmlAttribute]
		public int value;

		[XmlAttribute]
		[DefaultValue(0)]
		public int delta;

		[XmlAttribute]
		[DefaultValue(false)]
		public bool percent;

		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);

			if (importObject.reserved[8] != null)
				this.value = Int32.Parse(importObject.reserved[8].Trim());
			if (importObject.reserved[7] != null)
				this.delta = Int32.Parse(importObject.reserved[7].Trim());

			if (importObject.reserved[5] != null)
				this.percent = importObject.reserved[5].Trim() == "1";
		}
	}

	[Serializable]
	public sealed class PoisonEffect : OverTimeEffect
	{
	}

	[Serializable]
	public sealed class BleedEffect : OverTimeEffect
	{
	}

	[Serializable]
	public sealed class SpellAttackEffect : OverTimeEffect
	{
	}

	[Serializable]
	public sealed class HealOverTimeEffect : OverTimeEffect
	{
	}

	[Serializable]
	public sealed class MpHealEffect : OverTimeEffect
	{
	}

	[Serializable]
	public sealed class FpHealEffect : OverTimeEffect
	{
	}

	[Serializable]
	public sealed class DpHealEffect : OverTimeEffect
	{
	}

	[Serializable]
	public sealed class FpAttackEffect : OverTimeEffect
	{
		// NO DATA
	}

	[Serializable]
	public sealed class MpAttackEffect : OverTimeEffect
	{
		// NO DATA
	}

	[Serializable]
	public sealed class MpUseOverTimeEffect : OverTimeEffect
	{
		// ARTIFICIAL EFFECT
		[XmlAttribute]
		[DefaultValue(0)]
		public int cost_start;

		[XmlAttribute]
		[DefaultValue(0)]
		public int cost_end;
	}

	[Serializable]
	public sealed class HpUseOverTimeEffect : OverTimeEffect
	{
		// ARTIFICIAL EFFECT
		[XmlAttribute]
		[DefaultValue(0)]
		public int cost_start;

		[XmlAttribute]
		[DefaultValue(0)]
		public int cost_end;
	}
}
