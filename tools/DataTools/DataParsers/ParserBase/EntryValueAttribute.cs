using System;

namespace Jamie.ParserBase
{
	public sealed class EntryValueAttribute : Attribute
	{
		private readonly string stringValue;

		public EntryValueAttribute(string stringValue) {
			this.stringValue = stringValue;
		}

		public override string ToString() {
			return stringValue;
		}
	}
}
