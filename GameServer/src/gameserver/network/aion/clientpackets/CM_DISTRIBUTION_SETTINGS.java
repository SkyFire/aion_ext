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

package gameserver.network.aion.clientpackets;


import gameserver.model.gameobjects.player.Player;
import gameserver.model.group.LootDistribution;
import gameserver.model.group.LootGroupRules;
import gameserver.model.group.LootRuleType;
import gameserver.model.group.PlayerGroup;
import gameserver.network.aion.AionClientPacket;

/**
 * @author Lyahim, Simple
 */
public class CM_DISTRIBUTION_SETTINGS extends AionClientPacket {

    private LootRuleType lootrules; //0-free-for-all, 1-round-robin 2-leader
    private LootDistribution autodistribution;
    //rare item distribution
    //0-normal, 2-Roll-dice,3-bid
    private int common_item_above;
    private int superior_item_above;
    private int heroic_item_above;
    private int fabled_item_above;
    private int ethernal_item_above;
    private int over_ethernal;
    private int over_over_ethernal;

    public CM_DISTRIBUTION_SETTINGS(int opcode) {
        super(opcode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        @SuppressWarnings("unused")
        int unk1 = readD();

        int rules = readD();
        switch (rules) {
            case 0:
                this.lootrules = LootRuleType.FREEFORALL;
                break;
            case 1:
                this.lootrules = LootRuleType.ROUNDROBIN;
                break;
            case 2:
                this.lootrules = LootRuleType.LEADER;
                break;
            default:
                this.lootrules = LootRuleType.FREEFORALL;
                break;
        }

        int autoDist = readD();
        switch (autoDist) {
            case 0:
                this.autodistribution = LootDistribution.NORMAL;
                break;
            case 2:
                this.autodistribution = LootDistribution.ROLL_DICE;
                break;
            case 3:
                this.autodistribution = LootDistribution.BID;
                break;
            default: // It happens!
                this.autodistribution = LootDistribution.NORMAL;
                break;
        }

        this.common_item_above = readD();
        this.superior_item_above = readD();
        this.heroic_item_above = readD();
        this.fabled_item_above = readD();
        this.ethernal_item_above = readD();
        this.over_ethernal = readD();
        this.over_over_ethernal = readD();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        Player leader = getConnection().getActivePlayer();
        if (leader != null) {
            PlayerGroup pg = leader.getPlayerGroup();
            if (pg != null)
                pg.setLootGroupRules(new LootGroupRules(this.lootrules,
                        this.autodistribution, this.common_item_above,
                        this.superior_item_above, this.heroic_item_above,
                        this.fabled_item_above, this.ethernal_item_above,
                        this.over_ethernal, this.over_over_ethernal));
        }
    }
}
