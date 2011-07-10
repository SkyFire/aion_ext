namespace Jamie.Skills
{
	using System;
	using System.Collections.Generic;
	using System.ComponentModel;
	using System.Xml;
	using System.Xml.Schema;
	using System.Xml.Serialization;

	[Serializable]
	public partial class Modifiers
	{
		[XmlElement("add", Form = XmlSchemaForm.Unqualified, Type = typeof(AddModifier))]
		[XmlElement("sub", Form = XmlSchemaForm.Unqualified, Type = typeof(SubModifier))]
		[XmlElement("rate", Form = XmlSchemaForm.Unqualified, Type = typeof(RateModifier))]
		[XmlElement("set", Form = XmlSchemaForm.Unqualified, Type = typeof(SetModifier))]
		[XmlElement("mean", Form = XmlSchemaForm.Unqualified, Type = typeof(MeanModifier))]
		public List<Modifier> modifierList;
	}

	[XmlInclude(typeof(MeanModifier))]
	[XmlInclude(typeof(SetModifier))]
	[XmlInclude(typeof(RateModifier))]
	[XmlInclude(typeof(SubModifier))]
	[XmlInclude(typeof(AddModifier))]
	[Serializable]
	public class Modifier
	{
		[XmlAttribute]
		public modifiersenum name;

		[XmlIgnore]
		public bool nameSpecified;

		[XmlAttribute]
		[DefaultValue(false)]
		public bool bonus;

		public Modifier() {
			this.bonus = false;
		}
	}

	[Serializable]
	public partial class MeanModifier : Modifier, IXmlSerializable
	{
		public MeanModifier() { }
		public MeanModifier(int min, int max) {
			this.min = min;
			if (min > 0)
				this.minSpecified = true;
			this.max = max;
			if (max > 0)
				this.maxSpecified = true;
			this.name = modifiersenum.POWER;
			this.nameSpecified = true;
		}

		[XmlAttribute]
		public int min;

		[XmlIgnore]
		public bool minSpecified;

		[XmlAttribute]
		public int max;

		[XmlIgnore]
		public bool maxSpecified;

		#region IXmlSerializable Members

		public XmlSchema GetSchema() {
			return null;
		}

		public void ReadXml(XmlReader reader) {
			if (reader.MoveToContent() == XmlNodeType.Element && reader.LocalName == "mean") {
				if (!String.IsNullOrEmpty(reader["max"])) {
					max = XmlConvert.ToInt32(reader["max"]);
					maxSpecified = true;
				}
				if (!String.IsNullOrEmpty(reader["min"])) {
					min = XmlConvert.ToInt32(reader["min"]);
					minSpecified = true;
				}
				if (!String.IsNullOrEmpty(reader["name"])) {
					name = (modifiersenum)Enum.Parse(typeof(modifiersenum), reader["name"]);
					nameSpecified = true;
				}
				if (!String.IsNullOrEmpty(reader["bonus"]))
					bonus = XmlConvert.ToBoolean(reader["bonus"]);
				reader.Read();
			}
		}

		public void WriteXml(XmlWriter writer) {
			if (maxSpecified)
				writer.WriteAttributeString("max", XmlConvert.ToString(max));
			if (minSpecified)
				writer.WriteAttributeString("min", XmlConvert.ToString(min));
			if (bonus)
				writer.WriteAttributeString("bonus", XmlConvert.ToString(bonus));
			writer.WriteAttributeString("name", name.ToString());
		}

		#endregion
	}

	[Serializable]
	public partial class SetModifier : Modifier, IXmlSerializable
	{
		[XmlAttribute]
		public int value;

		[XmlIgnore]
		public bool valueSpecified;

		#region IXmlSerializable Members

		public XmlSchema GetSchema() {
			return null;
		}

		public void ReadXml(XmlReader reader) {
			if (reader.MoveToContent() == XmlNodeType.Element && reader.LocalName == "set") {
				value = XmlConvert.ToInt32(reader["value"]);
				if (!String.IsNullOrEmpty(reader["name"])) {
					name = (modifiersenum)Enum.Parse(typeof(modifiersenum), reader["name"]);
					nameSpecified = true;
				}
				if (!String.IsNullOrEmpty(reader["bonus"]))
					bonus = XmlConvert.ToBoolean(reader["bonus"]);
				reader.Read();
			}
		}

		public void WriteXml(XmlWriter writer) {
			writer.WriteAttributeString("value", value.ToString());
			if (bonus)
				writer.WriteAttributeString("bonus", XmlConvert.ToString(bonus));
			writer.WriteAttributeString("name", name.ToString());
		}

		#endregion
	}

	[Serializable]
	public partial class RateModifier : Modifier, IXmlSerializable
	{
		[XmlAttribute]
		public int value;

		[XmlIgnore]
		public bool valueSpecified;

		#region IXmlSerializable Members

		public XmlSchema GetSchema() {
			return null;
		}

		public void ReadXml(XmlReader reader) {
			if (reader.MoveToContent() == XmlNodeType.Element && reader.LocalName == "rate") {
				value = XmlConvert.ToInt32(reader["value"]);
				if (!String.IsNullOrEmpty(reader["name"])) {
					name = (modifiersenum)Enum.Parse(typeof(modifiersenum), reader["name"]);
					nameSpecified = true;
				}
				if (!String.IsNullOrEmpty(reader["bonus"]))
					bonus = XmlConvert.ToBoolean(reader["bonus"]);
				reader.Read();
			}
		}

		public void WriteXml(XmlWriter writer) {
			writer.WriteAttributeString("value", value.ToString());
			if (bonus)
				writer.WriteAttributeString("bonus", XmlConvert.ToString(bonus));
			writer.WriteAttributeString("name", name.ToString());
		}

		#endregion
	}

	[Serializable]
	public partial class SubModifier : Modifier, IXmlSerializable
	{
		[XmlAttribute]
		public int value;

		[XmlIgnore]
		public bool valueSpecified;

		#region IXmlSerializable Members

		public XmlSchema GetSchema() {
			return null;
		}

		public void ReadXml(XmlReader reader) {
			if (reader.MoveToContent() == XmlNodeType.Element && reader.LocalName == "sub") {
				value = XmlConvert.ToInt32(reader["value"]);
				if (!String.IsNullOrEmpty(reader["name"])) {
					name = (modifiersenum)Enum.Parse(typeof(modifiersenum), reader["name"]);
					nameSpecified = true;
				}
				if (!String.IsNullOrEmpty(reader["bonus"]))
					bonus = XmlConvert.ToBoolean(reader["bonus"]);
				reader.Read();
			}
		}

		public void WriteXml(XmlWriter writer) {
			writer.WriteAttributeString("value", value.ToString());
			if (bonus)
				writer.WriteAttributeString("bonus", XmlConvert.ToString(bonus));
			writer.WriteAttributeString("name", name.ToString());
		}

		#endregion
	}

	[Serializable]
	public partial class AddModifier : Modifier, IXmlSerializable
	{
		[XmlAttribute]
		public int value;

		[XmlIgnore]
		public bool valueSpecified;

		#region IXmlSerializable Members

		public XmlSchema GetSchema() {
			return null;
		}

		public void ReadXml(XmlReader reader) {
			if (reader.MoveToContent() == XmlNodeType.Element && reader.LocalName == "add") {
				value = XmlConvert.ToInt32(reader["value"]);
				if (!String.IsNullOrEmpty(reader["name"])) {
					name = (modifiersenum)Enum.Parse(typeof(modifiersenum), reader["name"]);
					nameSpecified = true;
				}
				if (!String.IsNullOrEmpty(reader["bonus"]))
					bonus = XmlConvert.ToBoolean(reader["bonus"]);
				reader.Read();
			}
		}

		public void WriteXml(XmlWriter writer) {
			writer.WriteAttributeString("value", value.ToString());
			if (bonus)
				writer.WriteAttributeString("bonus", XmlConvert.ToString(bonus));
			writer.WriteAttributeString("name", name.ToString());
		}

		#endregion
	}
}
