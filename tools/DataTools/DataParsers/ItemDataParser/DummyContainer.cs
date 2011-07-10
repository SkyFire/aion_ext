using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Reflection;
using Jamie.ParserBase;

namespace Jamie.Items
{
	class CheckItem : IDynamicImport<Quest>
	{
		public List<AbstractInventoryBonus> bonuses;

		#region IDynamicImport<Quest> Members

		public void Import(Quest importObject, IEnumerable<System.Reflection.FieldInfo> getters) {
			if (bonuses == null)
				bonuses = new List<AbstractInventoryBonus>();
			Utility<Quest>.Instance.Export(importObject, getters, bonuses);
		}

		#endregion
	}

	class DummyContainer
	{
		public List<CheckItem> checkItems;
	}
}
