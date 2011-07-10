namespace Jamie.Npcs
{
    using System.Collections.Generic;
    using Jamie.Npcs;

    class Gossip
	{
		public List<int> NpcList = new List<int>();

        public ShoutEventType Event { get; set; }

        public int stringId;

        public string param;
	}
}
