using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Jamie.ParserBase.Skills
{
    [Serializable]
    public enum CombineSkillType
    {
        any = 0,
        gathering_b = 30002,
        aerial_gathering = 30003,
        cooking = 40001,
        weaponsmith = 40002,
        armorsmith = 40003,
        tailoring = 40004,
        alchemy = 40007,
        handiwork = 40008,
        convert = 40009
    }
}
