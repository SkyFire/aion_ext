using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Runtime.Serialization;

namespace Jamie.Quests
{
	[Serializable]
	public class DialogDictionary : Dictionary<string, Dialogs>
	{
		public DialogDictionary() : base() { }

		public DialogDictionary(SerializationInfo si, StreamingContext sc)
			: base(si, sc) {
		}
	}
}
