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

import gameserver.controllers.SummonController.UnsummonType;
import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Summon;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.network.aion.SystemMessageId;
import gameserver.skillengine.model.SkillTargetSlot;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.zone.ZoneName;
import gameserver.world.World;

import java.util.*;

/**
 * @author HellBoy, ggadv2
 */
public class ArenaService {

    private Map<Integer, Player> playersInColiseum = Collections.synchronizedMap(new HashMap<Integer, Player>());
    private Map<Integer, Player> playersInTriniel = Collections.synchronizedMap(new HashMap<Integer, Player>());

    public void onDie(final Player defeated, Creature lastAttacker) {
        defeated.getController().cancelCurrentSkill();

        Summon summon = defeated.getSummon();
        if (summon != null)
            summon.getController().release(UnsummonType.UNSPECIFIED);

        PacketSendUtility.broadcastPacket(defeated, new SM_EMOTION(defeated, EmotionType.DIE, 0, lastAttacker == null ? 0 : lastAttacker.getObjectId()), true);

        PacketSendUtility.sendPacket(defeated, SM_SYSTEM_MESSAGE.DIE);
        defeated.getObserveController().notifyDeath(defeated);

        Player winner = null;
        if (lastAttacker instanceof Player)
            winner = (Player) lastAttacker;
        else if (lastAttacker instanceof Summon)
            winner = ((Summon) lastAttacker).getMaster();
            
        final Player defeater = winner;

        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (defeater == null || defeated == null)
                    return;

                // Broadcast arena info
                broadcastArenaInfo(ArenaInfo.ARENA_KICKED, defeater, defeated);

                defeated.getReviveController().skillRevive();
                if (isInTrinielArena(defeated))
                    TeleportService.teleportTo(defeated, 120010000, 1, 1005.1f, 1528.9f, 222.1f, 0);
                if (isInColiseum(defeated))
                    TeleportService.teleportTo(defeated, 110010000, 1, 1470.3f, 1343.5f, 563.7f, 21);
                unregister(defeated);

                defeated.getEffectController().removeAbnormalEffectsByTargetSlot(SkillTargetSlot.DEBUFF);
                defeated.getEffectController().removeEffect(8291);

                // Fully heal player
                defeated.getLifeStats().increaseHp(TYPE.HP, defeated.getLifeStats().getMaxHp() + 1);
                defeated.getLifeStats().increaseMp(TYPE.MP, defeated.getLifeStats().getMaxMp() + 1);
            }
        }, 5000);

        if (defeater != null)
            broadcastArenaInfo(ArenaInfo.ARENA_DEFEATED, defeater, defeated);
    }

    public void broadcastArenaInfo(ArenaInfo info, Player playerToUpdate1, Player playerToUpdate2) {
        List<Player> playersInArena = new ArrayList<Player>();

        if (isInTrinielArena(playerToUpdate1))
            playersInArena.addAll(playersInTriniel.values());
        else if (isInColiseum(playerToUpdate1))
            playersInArena.addAll(playersInColiseum.values());

        for (Player player : playersInArena) {
            switch (info) {
                case ARENA_DEFEATED:
                    PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_KILLMSG(playerToUpdate1.getName(), playerToUpdate2.getName()));
                    break;
                case ARENA_KICKED:
                    PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_PvPZONE_OUT_MESSAGE(playerToUpdate2.getName()));
                    break;
                default:
                    break;
            }
        }
    }

    public enum ArenaInfo {
        ARENA_DEFEATED,
        ARENA_KICKED
    }

    public void registerPlayerForTriniel(Player player) {
        playersInTriniel.put(player.getObjectId(), player);
    }

    public void registerPlayerForColiseum(Player player) {
        playersInColiseum.put(player.getObjectId(), player);
    }

    public void unregister(Player player) {
        playersInTriniel.remove(player.getObjectId());
        playersInColiseum.remove(player.getObjectId());
    }

    public boolean isInArena(Player player) {
        if (isInTrinielArena(player) || isInColiseum(player))
            return true;
        return false;
    }

    private boolean isInTrinielArena(Player player) {
        if (playersInTriniel.containsKey(player.getObjectId()))
            return true;
        return false;
    }

    private boolean isInColiseum(Player player) {
        if (playersInColiseum.containsKey(player.getObjectId()))
            return true;
        return false;
    }

    public boolean isInArenaZone(Player player) {
        if (isInTrinielArenaZone(player) || isInColiseumZone(player))
            return true;
        return false;
    }

    private boolean isInTrinielArenaZone(Player player) {
        int world = player.getWorldId();
        if (world == 120010000 && ZoneService.getInstance().isInsideZone(player, ZoneName.TRINIEL_PVP_ZONE))
            return true;
        return false;
    }

    private boolean isInColiseumZone(Player player) {
        int world = player.getWorldId();
        if (world == 110010000 && ZoneService.getInstance().isInsideZone(player, ZoneName.COLISEUM))
            return true;
        return false;
    }

    public boolean isInSameGroup(Player player1, Player player2) {
        if (player1.isInAlliance() && player2.isInAlliance()) {
            if (player1.getPlayerAlliance().getCaptainObjectId() == player2.getPlayerAlliance().getCaptainObjectId())
                return true;
        }
        else if (player1.isInGroup() && player2.isInGroup()) {
            if (player1.getPlayerGroup().getGroupId() == player2.getPlayerGroup().getGroupId())
                return true;
        }
        return false;
    }

    public boolean isEnemyPlayer(Player player1, Player player2) {
        if (isInArena(player1) && isInArena(player2)) {
            if (!isInSameGroup(player1, player2))
                return true;
        }
        return false;
    }

    public static final ArenaService getInstance() {
        return SingletonHolder.instance;
    }

    @SuppressWarnings("synthetic-access")
    public static class SingletonHolder {
        protected static final ArenaService instance = new ArenaService();
    }
}
