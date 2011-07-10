namespace Jamie.Skills
{
	using System;
	using System.Xml.Serialization;
	using System.ComponentModel;
	using System.Reflection;
	using System.Collections.Generic;

	[Serializable]
	public abstract partial class AbstractVdpEffect : Effect
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

			if (importObject.reserved[5] != null && importObject.reserved[5].Trim() == "1")
				this.percent = true;
			this.value = Int32.Parse(importObject.reserved[1].Trim());
			if (importObject.reserved[0] != null)
				this.delta = Int32.Parse(importObject.reserved[0].Trim());
		}
	}

	[Serializable]
	public sealed class HealInstantEffect : AbstractVdpEffect
	{
		// instant heal
	}

	[Serializable]
	public sealed class MpHealInstantEffect : AbstractVdpEffect
	{
		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			if (importObject.Skill.id == 19230) {
				this.value = Int32.Parse(importObject.reserved[10].Trim());
			} else {
				base.Import(importObject, getters);
			}
		}
	}

	[Serializable]
	public sealed class DpHealInstantEffect : AbstractVdpEffect
	{
	}

	[Serializable]
	public sealed class FpHealInstantEffect : AbstractVdpEffect
	{
	}

	[Serializable]
	public sealed class ItemHealFpEffect : AbstractVdpEffect
	{
	}

	[Serializable]
	public sealed class ItemHealDpEffect : AbstractVdpEffect
	{
	}

	[Serializable]
	public sealed class ItemHealMpEffect : AbstractVdpEffect
	{
	}

	[Serializable]
	public sealed class ItemHealEffect : AbstractVdpEffect
	{
	}

	[Serializable]
	public sealed class HealCastorOnAttackedEffect : AbstractVdpEffect
	{
		// distance is always 5 meters (res4)
	}
}
