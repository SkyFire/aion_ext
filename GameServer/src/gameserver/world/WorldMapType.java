/**
 * This file is part of Aion X Emu <aionxemu.com>
 *
 *  This is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This software is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser Public License
 *  along with this software.  If not, see <http://www.gnu.org/licenses/>.
 */

package gameserver.world;

public enum WorldMapType {
    // Asmodea
    PANDAEMONIUM(120010000),
    ISHALGEN(220010000),
    MORHEIM(220020000),
    ALTGARD(220030000),
    BELUSLAN(220040000),
    BRUSTHONIN(220050000),

    //Balaurea
    INGGISON(210050000),
    GELKMAROS(220070000),

    // Elysia
    SANCTUM(110010000),
    POETA(210010000),
    VERTERON(210030000),
    ELTNEN(210020000),
    HEIRON(210040000),
    THEOMOBOS(210060000),

    //Instances
    ID_AB_PRO(300010000),
    ID_TEST_DUNGEON(300020000),
    NOCHSANA_TRAINING_CAMP(300030000),
    DARK_POETA(300040000),
    ASTERIA_CHAMBER(300050000),
    SULFUR_TREE_NEST(300060000),
    CHAMBER_OF_ROAH(300070000),
    LEFT_WING_CHAMBER(300080000),
    RIGHT_WING_CHAMBER(300090000),
    STEEL_RAKE(300100000),
    DREDGION(300110000),
    KYSIS_CHAMBER(300120000),
    MIREN_CHAMBER(300130000),
    KROTAN_CHAMBER(300140000),
    UDAS_TEMPLE_UPPER(300150000),
    UDAS_TEMPLE_LOWER(300160000),
    BESHMUNDIR_TEMPLE(300170000),
    TALOCS_HOLLOW(300190000),
    IDNOVICE(300200000),
    CHANTRA_DREDGION(300210000),
    DEBRIS_OF_ABYSS(300220000),
    KROMEDES_TRIAL(300230000),
    AZOTURAN_FORTRESS(310010000),
    IDABPROL2(310020000),
    AERDINA(310030000),
    IDABGATEL2(310040000),
    AETHEROGENETICS_LAB(310050000),
    IDLF1B(310060000),
    IDLF1B_STIGMA(310070000),
    IDLC1_ARENA(310080000),
    INDRATU_FORTRESS(310090000),
    THEOBOMOS_LAB(310110000),
    IDABPROL3(310120000),
    IDABPROD1(320010000),
    IDABPROD2(320020000),
    IDABGATED1(320030000),
    IDABGATED2(320040000),
    SKY_TEMPLE_INTERIOR(320050000),
    IDDF1B(320060000),
    IDSPACE(320070000),
    DRAUPNIR_CAVE(320080000),
    IDDC1_ARENA(320090000),
    FIRE_TEMPLE(320100000),
    ALQUIMIA(320110000),
    IDDC1_ARENA_3F(320120000),
    ADMA_STRONGHOLD(320130000),
    IDABPROD3(320140000),
    
    // Abuss
    RESHANTA(400010000),
    // Prison
    LF_PRISON(510010000),
    DF_PRISON(520010000),
    
    SILENTERA_CANYON(600010000),
    TEST_BASIC(900020000),
    TEST_SERVER(900030000),
    TEST_GIANT_MONSTER(900100000),
    
    HOUSING_BARRACK(900110000);


    private final int worldId;

    WorldMapType(int worldId) {
        this.worldId = worldId;
    }

    public int getId() {
        return worldId;
    }

    /**
     * @param id of world
     * @return WorldMapType
     */
    public static WorldMapType getWorld(int id) {
        for (WorldMapType type : WorldMapType.values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        return null;
    }
}