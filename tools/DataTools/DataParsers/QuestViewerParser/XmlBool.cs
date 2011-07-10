using System;
using System.Diagnostics;
using System.Xml.Serialization;

namespace AionQuests
{
    [Serializable]
    [XmlType(AnonymousType = true)]
    [DebuggerDisplay("{System.Boolean.Parse(Value.ToString().ToLower())}")]
    public class XmlBool : IEquatable<XmlBool>, IEquatable<bool>
    {
        public XmlBool() { }

        public XmlBool(bool value) {
            Value = value ? XmlBoolTypes.TRUE : XmlBoolTypes.FALSE;
        }

        [XmlText]
        public XmlBoolTypes Value = XmlBoolTypes.FALSE;

        public static implicit operator bool(XmlBool rValue) {
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
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public enum XmlBoolTypes
    {
        FALSE = 0,
        TRUE = 1
    }
}
