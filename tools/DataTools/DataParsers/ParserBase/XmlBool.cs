namespace Jamie.ParserBase
{
	using System;
	using System.Diagnostics;
	using System.Xml.Serialization;

	[Serializable]
	[XmlType(AnonymousType = true)]
	[DebuggerDisplay("{System.Boolean.Parse(Value.ToString().ToLower())}")]
	public class XmlBool : IEquatable<XmlBool>, IEquatable<bool>, IConvertible
	{
		public XmlBool() { }

		public XmlBool(bool value) {
			Value = value ? XmlBoolTypes.TRUE : XmlBoolTypes.FALSE;
		}

		[XmlText]
		public XmlBoolTypes Value = XmlBoolTypes.FALSE;

		public static explicit operator bool(XmlBool rValue) {
			if (rValue == null)
				return false;
			return Boolean.Parse(rValue.Value.ToString());
		}

		#region IEquatable<bool> Members

		bool IEquatable<bool>.Equals(bool other) {
			return Boolean.Equals(this, other);
		}

		#endregion

		#region IEquatable<XmlBool> Members

		bool IEquatable<XmlBool>.Equals(XmlBool other) {
			if (other == null)
				return false;
			return Enum.Equals(this.Value, other.Value);
		}

		#endregion

		public override int GetHashCode() {
			return (int)Value;
		}

		public override bool Equals(object obj) {
			XmlBool other = obj as XmlBool;
			if (other == null)
				return false;
			return other.Value == this.Value;
		}

		public static bool operator ==(XmlBool a, XmlBool b) {
			return Object.Equals(a, b);
		}

		public static bool operator !=(XmlBool a, XmlBool b) {
			return !Object.Equals(a, b);
		}

		#region IConvertible Members

		public TypeCode GetTypeCode() {
			return TypeCode.Boolean;
		}

		public bool ToBoolean(IFormatProvider provider) {
			return Boolean.Parse(Value.ToString());
		}

		public byte ToByte(IFormatProvider provider) {
			return (byte)Value;
		}

		public char ToChar(IFormatProvider provider) {
			throw new NotImplementedException();
		}

		public DateTime ToDateTime(IFormatProvider provider) {
			throw new NotImplementedException();
		}

		public decimal ToDecimal(IFormatProvider provider) {
			throw new NotImplementedException();
		}

		public double ToDouble(IFormatProvider provider) {
			throw new NotImplementedException();
		}

		public short ToInt16(IFormatProvider provider) {
			return (short)Value;
		}

		public int ToInt32(IFormatProvider provider) {
			return (int)Value;
		}

		public long ToInt64(IFormatProvider provider) {
			return (long)Value;
		}

		public sbyte ToSByte(IFormatProvider provider) {
			return (sbyte)Value;
		}

		public float ToSingle(IFormatProvider provider) {
			return (float)Value;
		}

		public string ToString(IFormatProvider provider) {
			return Boolean.Parse(Value.ToString()).ToString();
		}

		public object ToType(Type conversionType, IFormatProvider provider) {
			throw new NotImplementedException();
		}

		public ushort ToUInt16(IFormatProvider provider) {
			return (ushort)Value;
		}

		public uint ToUInt32(IFormatProvider provider) {
			return (uint)Value;
		}

		public ulong ToUInt64(IFormatProvider provider) {
			return (ulong)Value;
		}

		#endregion
	}

	[Serializable]
	[XmlType(AnonymousType = true)]
	public enum XmlBoolTypes
	{
		FALSE = 0,
		False = 0,
		TRUE = 1,
		True = 1,
	}
}
