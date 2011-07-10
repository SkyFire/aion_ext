namespace Jamie.Skills
{
	using System;
	using System.Collections.Generic;
	using System.ComponentModel;
	using System.Diagnostics;
	using System.Linq;
	using System.Reflection;
	using System.Xml.Schema;
	using System.Xml.Serialization;

	[Serializable]
	public partial class DispelEffect : Effect
	{
		[XmlElement("effectids", Form = XmlSchemaForm.Unqualified)]
		public int[] effectids;

		[XmlElement("effecttype", Form = XmlSchemaForm.Unqualified)]
		public string[] effecttype;

		[XmlAttribute]
		public DispelType dispeltype;

		[XmlAttribute]
		[DefaultValue(0)]
		public int value;

		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);

			string dispelType = importObject.reserved[0].Trim().ToLower();
			bool skipAdd = false;

			if (dispelType == "effect_type")
				dispeltype = DispelType.EFFECTTYPE;
			else if (dispelType == "effect_id")
				dispeltype = DispelType.EFFECTID;
			else if (dispelType == "effect_id_range") {
				dispeltype = DispelType.EFFECTID;
				int from = Int32.Parse(importObject.reserved[1]);
				int to = Int32.Parse(importObject.reserved[2]);
				List<int> ids = new List<int>();
				for (int i = from; i <= to; i++)
					ids.Add(i);
				effectids = ids.ToArray();
				skipAdd = true;
			} else {
				Debug.Print("Unknown DispelType: '{0}'", dispelType);
				return;
			}

			if (!skipAdd) {
				List<string> dispels = new List<string>();
				for (int i = 1; i < 6; i++) {
					string dispel = importObject.reserved[i];
					if (dispel == null)
						continue;
					dispel = dispel.Trim().ToLower();
					//if (dispel == "polymorph")
					//    dispel = "shapechange";
					dispels.Add(dispel.ToUpper());
				}

				if (dispeltype == DispelType.EFFECTTYPE)
					effecttype = dispels.ToArray();
				else
					effectids = dispels.Select(s => Int32.Parse(s)).ToArray();
			}

			if (importObject.reserved[13] != null)
				this.value = Int32.Parse(importObject.reserved[13].Trim());
			else if (importObject.reserved[12] != null)
				this.value = Int32.Parse(importObject.reserved[12].Trim());
		}
	}

	[Serializable]
	public enum DispelType
	{
		NONE = 0,
		EFFECTID,
		EFFECTTYPE,
	}

	public abstract class DispelBaseEffect : Effect
	{
		[XmlAttribute]
		public int count;

		[XmlAttribute]
		public int level;

		[XmlIgnore]
		public int unknown1;

		[XmlIgnore]
		public int unknown2;

		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);

			this.count = Int32.Parse(importObject.reserved[1].Trim());

			if (importObject.reserved[0] != null)
				this.unknown1 = Int32.Parse(importObject.reserved[0].Trim());

			if (importObject.reserved[15] != null)
				this.level = Int32.Parse(importObject.reserved[15].Trim());

			if (importObject.reserved[17] != null)
				this.unknown2 = Int32.Parse(importObject.reserved[17].Trim());
		}
	}

	[Serializable]
	public sealed class DispelBuffEffect : DispelBaseEffect
	{
	}

	[Serializable]
	public sealed class DispelDebuffEffect : DispelBaseEffect
	{
	}

	[Serializable]
	public sealed class DispelDebuffPhysicalEffect : DispelBaseEffect
	{
	}

	[Serializable]
	public sealed class DispelDebuffMentalEffect : DispelBaseEffect
	{
	}

	[Serializable]
	public sealed class DispelBuffCounterAtkEffect : DispelBaseEffect
	{
		[XmlAttribute]
		public int value;

		[XmlAttribute]
		[DefaultValue(0)]
		public int delta;

		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);

			this.value = Int32.Parse(importObject.reserved[8].Trim());
			if (importObject.reserved[7] != null)
				this.delta = Int32.Parse(importObject.reserved[7].Trim());
		}
	}

	[Serializable]
	public sealed class EvadeEffect : DispelEffect
	{
		public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
			base.Import(importObject, getters);
		}
	}
}
