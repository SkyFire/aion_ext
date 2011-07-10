namespace Jamie.Skills
{
	using System;
	using System.Collections.Generic;
	using System.Linq;
	using System.Text;
	using Jamie.ParserBase;

	public class ChangeList : List<Change>
	{
		public ChangeList() { }

		public ChangeList(int capacity) : base(capacity) { }

		public ChangeList(IEnumerable<Change> collection) : base(collection) { }

		public ChangeList AddReserved(Type effecType, string[] reserved) {
			if (reserved == null)
				throw new ArgumentNullException("reserved");

			modifiersenum statValue = Utility.GetStat(effecType, reserved[12]);

			if (effecType.Name.ToLower().Contains("boost")) {
				if (reserved[1] != null) {
					var change = new Change()
					{
						stat = statValue,
						func = StatFunc.ADD,
						value = Int32.Parse(reserved[1])
					};
					if (reserved[0] != null)
						change.delta = Int32.Parse(reserved[0]);
					if (change.value > 0)
						this.Add(change);
				}

				if (reserved[3] != null) {
					var change = new Change()
					{
						stat = statValue,
						func = StatFunc.PERCENT,
						value = Int32.Parse(reserved[3])
					};
					if (change.value > 0)
						this.Add(change);
				}
			} else {
				if (reserved[1] != null) {
					var change = new Change()
					{
						stat = statValue,
						func = StatFunc.PERCENT,
						value = Int32.Parse(reserved[1])
					};
					if (change.value > 0)
						this.Add(change);
				}

				if (reserved[3] != null) {
					var change = new Change()
					{
						stat = statValue,
						func = StatFunc.ADD,
						value = Int32.Parse(reserved[3])
					};
					if (reserved[0] != null)
						change.delta = Int32.Parse(reserved[0]);
					if (change.value > 0)
						this.Add(change);
				}
			}

			return this;
		}
	}
}
