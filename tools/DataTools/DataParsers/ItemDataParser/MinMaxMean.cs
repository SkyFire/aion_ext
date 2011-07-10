using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Jamie.ParserBase;

namespace Jamie.Items
{
    public class MinMaxMean
    {
        public long Min;
        public long Max;
        public long Mean;

        int count;

        public Item MinItem;

        Item maxItem;
        public Item MaxItem {
            get {
                if (maxItem == null)
                    return MinItem;
                return maxItem;
            }
        }

        public void AddValue(long value, Item item) {
            count++;
            if (count == 1)
                Min = Int64.MaxValue;

            if (Min > value) {
                Min = value;
                MinItem = item;
            }
            if (Max < value) {
                Max = value;
                maxItem = item;
            }

            Mean = (Mean * (count - 1) + value) / count;
        }
    }
}
