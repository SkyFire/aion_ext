using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace QuestDataParser
{
	class QuestCond
	{
		public int QuestId { get; private set; }
		public int Step { get; private set; }

		public QuestCond(int questId, int step) {
			this.QuestId = questId;
			this.Step = step;
		}

		public static bool operator >(QuestCond lhs, QuestCond rhs) {
			return lhs.QuestId == rhs.QuestId && lhs.Step > rhs.Step ||
				   lhs.QuestId > rhs.QuestId;
		}

		public static bool operator <(QuestCond lhs, QuestCond rhs) {
			return lhs.QuestId == rhs.QuestId && lhs.Step < rhs.Step ||
				   lhs.QuestId < rhs.QuestId;
		}

		public static bool operator >=(QuestCond lhs, QuestCond rhs) {
			return lhs.QuestId == rhs.QuestId && lhs.Step >= rhs.Step ||
				   lhs.QuestId > rhs.QuestId;
		}

		public static bool operator <=(QuestCond lhs, QuestCond rhs) {
			return lhs.QuestId == rhs.QuestId && lhs.Step <= rhs.Step ||
				   lhs.QuestId < rhs.QuestId;
		}

		public static bool operator ==(QuestCond lhs, QuestCond rhs) {
			return lhs.QuestId == rhs.QuestId && lhs.Step == rhs.Step;
		}

		public static bool operator !=(QuestCond lhs, QuestCond rhs) {
			return lhs.QuestId != rhs.QuestId || lhs.Step != rhs.Step;
		}

		public static int operator -(QuestCond lhs, QuestCond rhs) {
			if (lhs.QuestId != rhs.QuestId)
				return 0;
			return lhs.Step - rhs.Step;
		}

        public static explicit operator QuestStartCondition(QuestCond obj) {
            return new QuestStartCondition() { questId = obj.QuestId, step = obj.Step };
        }

		public override string ToString() {
			if (this.Step == 0)
				return this.QuestId.ToString();
			return String.Format("{0}:{1}", this.QuestId, this.Step);
		}

        public override int GetHashCode() {
            int hash = 1000000007 * this.QuestId;
            hash += 1000000009 * this.Step;
            return hash;
        }

        public override bool Equals(object obj) {
            return base.Equals(obj);
        }
	}
}
