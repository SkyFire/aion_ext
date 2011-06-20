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
package gameserver.services;

import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.RequestResponseHandler;
import gameserver.model.templates.CubeExpandTemplate;
import gameserver.network.aion.serverpackets.SM_CUBE_UPDATE;
import gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.utils.PacketSendUtility;
import org.apache.log4j.Logger;

/**
 * @author ATracer
 * @author Simple
 */
public class CubeExpandService {
    private static final Logger log = Logger.getLogger(CubeExpandService.class);

    private static final int MIN_EXPAND = 0;
    private static final int MAX_EXPAND = 9;

    /**
     * Shows Question window and expands on positive response
     *
     * @param player
     * @param npc
     */
    public static void expandCube(final Player player, Npc npc) {
        final CubeExpandTemplate expandTemplate = DataManager.CUBEEXPANDER_DATA.getCubeExpandListTemplate(npc.getNpcId());

        if (expandTemplate == null) {
            log.error("Cube Expand Template could not be found for Npc ID: " + npc.getObjectId());
            return;
        }

        if (npcCanExpandLevel(expandTemplate, player.getCubeSize() + 1)
                && validateNewSize(player.getCubeSize() + 1)) {
            /**
             * Check if our player can pay the cubic expand price
             */
            final int price = getPriceByLevel(expandTemplate, player.getCubeSize() + 1);

            RequestResponseHandler responseHandler = new RequestResponseHandler(npc) {
                @Override
                public void acceptRequest(Creature requester, Player responder) {
                    if (price > player.getInventory().getKinahItem().getItemCount()) {
                        PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.CUBEEXPAND_NOT_ENOUGH_KINAH);
                        return;
                    }
                    expand(responder);
                    player.getInventory().decreaseKinah(price);
                }

                @Override
                public void denyRequest(Creature requester, Player responder) {
                    // nothing to do
                }
            };

            boolean result = player.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_WAREHOUSE_EXPAND_WARNING,
                    responseHandler);
            if (result) {
                PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(
                        SM_QUESTION_WINDOW.STR_WAREHOUSE_EXPAND_WARNING, 0, String.valueOf(price)));
            }
        } else
            PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300430));
    }

    /**
     * Expands the cubes
     *
     * @param player
     */
    public static void expand(Player player) {
        if (!validateNewSize(player.getCubeSize() + 1))
            return;
        PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300431, "9")); // 9 Slots added
        player.setCubesize(player.getCubeSize() + 1);
        PacketSendUtility.sendPacket(player, new SM_CUBE_UPDATE(player, 0));
    }

    /**
     * Checks if new player cube is not max
     *
     * @param level
     * @return true or false
     */
    private static boolean validateNewSize(int level) {
        // check min and max level
        if (level < MIN_EXPAND || level > MAX_EXPAND)
            return false;
        return true;
    }

    /**
     * Checks if npc can expand level
     *
     * @param clist
     * @param level
     * @return true or false
     */
    private static boolean npcCanExpandLevel(CubeExpandTemplate clist, int level) {
        // check if level exists in template
        if (!clist.contains(level))
            return false;
        return true;
    }

    /**
     * The guy who created cube template should blame himself :) One day I will rewrite them
     *
     * @param clist
     * @param level
     * @return
     */
    private static int getPriceByLevel(CubeExpandTemplate clist, int level) {
        return clist.get(level).getPrice();
    }
}
