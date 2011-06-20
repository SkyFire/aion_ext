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
package gameserver.model.siege;

import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_INFLUENCE_RATIO;
import gameserver.services.SiegeService;
import gameserver.utils.PacketSendUtility;
import gameserver.world.Executor;
import gameserver.world.World;

/**
 * Calculates fortresses as 10 points and artifacts as 1 point each.
 * Need to find retail calculation. (Upper forts worth more...)
 *
 * @author Sarynth
 */
public class Influence {
    private float elyos = 0;
    private float asmos = 0;
    private float balaur = 0;

    private Influence() {
        calculateInfluence();
    }

    public static final Influence getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * Recalculates Influence and Broadcasts new values
     */
    public void recalculateInfluence() {
        calculateInfluence();

        broadcastInfluencePacket();
    }

    /**
     * calculate influence
     */
    private void calculateInfluence() {
        int total = 0;
        int asmos = 0;
        int elyos = 0;
        int balaur = 0;

        for (SiegeLocation sLoc : SiegeService.getInstance().getSiegeLocations().values()) {
            int bonus = 0;
            switch (sLoc.getSiegeType()) {
                case ARTIFACT:
                    bonus = 1;
                    break;
                case FORTRESS:
                    bonus = 10;
                    break;
                default:
                    break;
            }
            // TODO: Better formula...
            total += bonus;
            switch (sLoc.getRace()) {
                case BALAUR:
                    balaur += bonus;
                    break;
                case ASMODIANS:
                    asmos += bonus;
                    break;
                case ELYOS:
                    elyos += bonus;
                    break;
            }
        }

        this.balaur = (float) balaur / total;
        this.elyos = (float) elyos / total;
        this.asmos = (float) asmos / total;
    }

    /**
     * Broadcast packet with influence update to all players.
     * - Responsible for the message "The Divine Fortress is now vulnerable."
     */
    private void broadcastInfluencePacket() {
        final SM_INFLUENCE_RATIO pkt = new SM_INFLUENCE_RATIO();

        World.getInstance().doOnAllPlayers(new Executor<Player>() {
            @Override
            public boolean run(Player player) {
                PacketSendUtility.sendPacket(player, pkt);
                return true;
            }
        });
    }

    /**
     * @return elyos control
     */
    public float getElyos() {
        return this.elyos;
    }

    /**
     * @return asmos control
     */
    public float getAsmos() {
        return this.asmos;
    }

    /**
     * @return balaur control
     */
    public float getBalaur() {
        return this.balaur;
    }

    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder {
        protected static final Influence instance = new Influence();
    }

}
