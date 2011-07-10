using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Jamie.ParserBase
{
    [Serializable]
    public enum Gender
    {
        ALL = 0,
        MALE,
        FEMALE,
    }

	[Serializable]
	public enum weaponType
	{
		NONE = 0,
		DAGGER_1H = 1,
		MACE_1H = 2,
		SWORD_1H = 3,
        TOOLHOE_1H = 4,
		BOOK_2H = 5,
		ORB_2H = 6,
		POLEARM_2H = 7,
		STAFF_2H = 8,
		SWORD_2H = 9,
        TOOLPICK_2H = 10,
        TOOLROD_2H = 11,
        BOW = 12,
    }

	[Serializable]
	public enum armorType
	{
		NONE = 0,
		CHAIN,
		CLOTHES,
		LEATHER,
		PLATE,
		ROBE,
		SHARD,
		ARROW,
		SHIELD,
	}
}
