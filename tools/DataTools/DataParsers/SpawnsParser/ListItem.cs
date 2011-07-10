using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Jamie.Npcs
{
    class ListItem
    {
        public ListItem(int mapId) {
            MapId = mapId;
        }

        public string DisplayName { get; set; }
        public string FilePath { get; set; }
        public int MapId { get; private set; }

        public override string ToString() {
            return this.DisplayName;
        }
    }
}
