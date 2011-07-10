namespace Jamie.ParserBase
{
	using System;
	using System.Diagnostics;
	using System.Reflection;

	public class NamedEnum<T> where T : IComparable, IFormattable, IConvertible
	{
		#region Private fields

		protected T _value;
		protected static readonly
			BindingFlags flags = BindingFlags.Static | BindingFlags.GetField | BindingFlags.Public;

		#endregion

		#region Constructors

		public NamedEnum() {
			FieldInfo[] fields = typeof(T).GetFields(flags);
			_value = (T)fields[0].GetValue(null);
		}

		public NamedEnum(string value) {
			this.Value = value;
		}

		public NamedEnum(T namedEnum) {
			_value = namedEnum;
		}

		#endregion

		#region Value property accessors
#line hidden

		[DebuggerBrowsable(DebuggerBrowsableState.Never)]
		public string Value {
			get {
				FieldInfo[] fields = typeof(T).GetFields(flags);
				Type dataType = null;

				try {
					dataType = Enum.GetUnderlyingType(typeof(T));
				} catch (ArgumentException) {
					return String.Empty;
				}

				foreach (FieldInfo field in fields) {
					object value = field.GetValue(null);
					value = Convert.ChangeType(value, dataType);
					if (Enum.Equals((T)value, _value)) {
						object[] strValues = field.GetCustomAttributes(true);
						if (strValues.Length > 0)
							return strValues[0].ToString();
						else
							return field.Name.ToLower();
					}
				}
				return String.Empty;
			}

			set {
				FieldInfo[] fields = typeof(T).GetFields(flags);

				foreach (FieldInfo field in fields) {
					object[] strValues = field.GetCustomAttributes(true);
					if (strValues.Length > 0) {
						if (String.Compare(strValues[0].ToString(), value, true) == 0) {
							_value = (T)field.GetValue(null);
							return;
						}
					} else if (String.IsNullOrEmpty(value)) {
						_value = (T)field.GetValue(null);
						return;
					}
				}
				throw new ArgumentOutOfRangeException(typeof(T).Name);
			}
		}

#line default
		#endregion

		#region Operators
		public static implicit operator NamedEnum<T>(T enumValue) {
			if (Enum.IsDefined(typeof(T), enumValue)) {
				NamedEnum<T> newValue = new NamedEnum<T>();
				newValue._value = enumValue;
				return newValue;
			} else {
				throw new ArgumentOutOfRangeException(typeof(T).Name);
			}
		}

		public static implicit operator T(NamedEnum<T> namedEnum) {
			return namedEnum._value;
		}

		public static bool operator ==(NamedEnum<T> rhs, NamedEnum<T> lhs) {
			return rhs.Equals(lhs);
		}

		public static bool operator !=(NamedEnum<T> rhs, NamedEnum<T> lhs) {
			return !rhs.Equals(lhs);
		}

		#endregion

		#region Overriden methods

		public override string ToString() {
			return Value;
		}

		public override int GetHashCode() {
			return _value.GetHashCode();
		}

		public override bool Equals(object obj) {
			if (!(obj is NamedEnum<T>)) return false;
			NamedEnum<T> myNamedEnum = (NamedEnum<T>)obj;
			return object.Equals(this._value, myNamedEnum._value);
		}

		#endregion
	}
}
