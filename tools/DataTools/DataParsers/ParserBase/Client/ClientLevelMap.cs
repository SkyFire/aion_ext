using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Jamie.Npcs
{
    public static class ClientLevelMap
    {
        public static Dictionary<string, int> mapToId = new Dictionary<string, int>()
        {
            { "LC1", 110010000 }, { "LC2", 110020000 }, { "dc1", 120010000 },
            { "dc2", 120020000 }, { "lf1", 210010000 }, { "LF2", 210020000 },
            { "LF1A", 210030000 }, { "LF3", 210040000 }, { "LF4", 210050000 },
            { "LF2A", 210060000 }, { "df1", 220010000 }, { "df1a", 220030000 },
            { "df2", 220020000 }, { "df3", 220040000 }, { "df2a", 220050000 }, 
            { "df4", 220070000 }, { "IdAbPro", 300010000 }, { "idtest_dungeon", 300020000 },
            { "IDAb1_MiniCastle", 300030000 }, { "IDLF1", 300040000 },
            { "IDAbRe_Up_Asteria", 300050000 }, { "IDAbRe_Low_Divine", 300060000 },
            { "IDAbRe_Up_Rhoo", 300070000 }, { "IDAbRe_Low_Wciel", 300080000 },
            { "IDAbRe_Low_Eciel", 300090000 }, { "IDShulackShip", 300100000 },
            { "IDAb1_Dreadgion", 300110000 }, { "IDAbRe_Up3_Dkisas", 300120000 },
            { "IDAbRe_Up3_Lamiren", 300130000 }, { "IDAbRe_Up3_Crotan", 300140000 },
            { "IDTemple_Up", 300150000 }, { "IDTemple_Low", 300160000 },
            { "IDCatacombs", 300170000 }, { "IDElim", 300190000 }, { "idnovice", 300200000 },
            { "iddreadgion_02", 300210000 }, { "IDAbRe-Core", 300220000 },
            { "IDCromede", 300230000 }, { "IDAbProL1", 310010000 }, { "IDAbProL2", 310020000 },
            { "IdAbGateL1", 310030000 }, { "IdAbGateL2", 310040000 }, { "IDLF3Lp", 310050000 }, 
            { "IDLF1B", 310060000 }, { "IDLF1B_Stigma", 310070000 },
            { "IDLC1_Arena", 310080000 }, { "IDLF3_Castle_Indratoo", 310090000 },
            { "IDLF3_Castle_Lehpar", 310100000 }, { "IDLF2A_Lab", 310110000 },
            { "IDAbProL3", 310120000 }, { "IDAbProD1", 320010000 }, { "IDAbProD2", 320020000 },
            { "IdAbGateD1", 320030000 }, { "IdAbGateD2", 320040000 }, 
            { "IDDF2Flying", 320050000 }, { "IDDF1B", 320060000 }, { "IDSpace", 320070000 },
            { "IDDF3_Dragon", 320080000 }, { "IDDC1_Arena", 320090000 }, 
            { "IDDF2_Dflame", 320100000 }, { "IDDF3_lp", 320110000 }, 
            { "IDDC1_Arena_3F", 320120000 }, { "IDDF2A_Adma", 320130000 },
            { "IDAbProD3", 320140000 }, { "ab1", 400010000 }, { "lf_prison", 510010000 },
            { "df_prison", 520010000 }, { "Underpass", 600010000 }, { "test_basic", 900020000 },
            { "test_server", 900030000 }, { "test_giantmonster", 900100000 },

            // IDElemental_1, IDElemental_2, IDYun, Test_MRT_IDZone, IDRaksha
            // { "arena_l_lobby", 210070000 }, { "arena_d_lobby", 220080000 },
            { "arena_l_lobby", 110070000 }, { "arena_d_lobby", 120080000 },
            { "idf4re_drana", 300250000 }, { "idarena", 300300000 },
        };
    }
}
