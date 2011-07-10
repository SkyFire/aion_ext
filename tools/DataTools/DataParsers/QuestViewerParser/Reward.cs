using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AionQuests
{
    public class Reward
    {
        public int Exp { get; set; }
        public int Gold { get; set; }

        public List<Item> BasicItems { get; set; }
        public List<Item> SelectItems { get; set; }

        public string Title { get; set; }

        public bool IsExt { get; set; }

        public bool IsStigma { get; set; }
        public int Inventory { get; set; }

        public int AbyssRank { get; set; }
        public int AbyssPoints { get; set; }

        public string Receipe { get; set; }

        public int SillPoint { get; set; }
        public string Skill { get; set; }
    }
}
