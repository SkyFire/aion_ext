namespace Jamie.Skills
{
	using System;
	using System.Xml.Serialization;
	using System.Reflection;
	using System.Collections.Generic;

	[Serializable]
	public abstract class AlwaysEffect : Effect
	{
		[XmlAttribute("value")]
		public int count;

		[XmlIgnore]
		public int unknown1;

		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);

			this.count = Int32.Parse(importObject.reserved[8].Trim());
			this.unknown1 = Int32.Parse(importObject.reserved[12].Trim());
		}
	}

	[Serializable]
	public sealed class AlwaysDodgeEffect : AlwaysEffect
	{
	}

	[Serializable]
	public sealed class AlwaysParryEffect : AlwaysEffect
	{
	}

	[Serializable]
	public sealed class AlwaysBlockEffect : AlwaysEffect
	{
	}

	[Serializable]
	public sealed class AlwaysResistEffect : AlwaysEffect
	{
	}
}
