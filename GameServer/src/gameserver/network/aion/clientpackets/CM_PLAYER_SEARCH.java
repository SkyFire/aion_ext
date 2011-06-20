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

import gameserver.configs.administration.AdminConfig;
import gameserver.configs.main.CustomConfig;
import gameserver.model.gameobjects.player.FriendList.Status;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_PLAYER_SEARCH;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.utils.Util;
import gameserver.world.Executor;
import gameserver.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Received when a player searches using the social search panel
 *
 * @author Ben
 */
public class CM_PLAYER_SEARCH extends AionClientPacket {
    /**
     * The max number of players to return as results
     */
    public static final int MAX_RESULTS = 125;

    private String name;
    private int region;
    private int classMask;
    private int minLevel;
    private int maxLevel;
    private int lfgOnly;

    public CM_PLAYER_SEARCH(int opcode) {
        super(opcode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        if (!(name = readS()).isEmpty()) {
            name = Util.convertName(name);
            readB(44 - (name.length() * 2 + 2));
        } else {
            readB(42);
        }
        region = readD();
        classMask = readD();
        minLevel = readC();
        maxLevel = readC();
        lfgOnly = readC();
        readC(); // 0x00 in search pane 0x30 in /who?
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        final Player activePlayer = getConnection().getActivePlayer();

        final List<Player> matches = new ArrayList<Player>(MAX_RESULTS);

        if (activePlayer != null && activePlayer.getLevel() < CustomConfig.LEVEL_TO_SEARCH) {
            sendPacket(SM_SYSTEM_MESSAGE.LEVEL_NOT_ENOUGH_FOR_SEARCH(String.valueOf(CustomConfig.LEVEL_TO_SEARCH)));
            return;
        }

        World.getInstance().doOnAllPlayers(new Executor<Player>() {
            @Override
            public boolean run(Player player) {
                if (matches.size() >= MAX_RESULTS)
                    return false;

                if (!player.isSpawned())
                    return true;
                else if (CustomConfig.SEARCH_LIST_ALL && (activePlayer.getAccessLevel() >= AdminConfig.SEARCH_LIST_ALL)) {
                    matches.add(player);
                    return true;
                } else if (player.getFriendList().getStatus() == Status.OFFLINE)
                    return true;
                else if (lfgOnly == 1 && !player.isLookingForGroup())
                    return true;
                else if (!name.isEmpty() && !player.getName().toLowerCase().contains(name.toLowerCase()))
                    return true;
                else if (minLevel != 0xFF && player.getLevel() < minLevel)
                    return true;
                else if (maxLevel != 0xFF && player.getLevel() > maxLevel)
                    return true;
                else if (classMask > 0 && (player.getPlayerClass().getMask() & classMask) == 0)
                    return true;
                else if (region > 0 && player.getActiveRegion().getMapId() != region)
                    return true;
                else if ((player.getCommonData().getRace() != activePlayer.getCommonData().getRace()) && (CustomConfig.FACTIONS_SEARCH_MODE == false))
                    return true;
                else
                // This player matches criteria
                {
                    matches.add(player);
                }
                return true;
            }
        }, true);

        sendPacket(new SM_PLAYER_SEARCH(matches, region));
    }

}
