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

import gameserver.configs.main.CustomConfig;
import gameserver.model.DuelResult;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.RequestResponseHandler;
import gameserver.network.aion.serverpackets.SM_DUEL;
import gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.skillengine.model.SkillTargetSlot;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.World;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

import java.util.concurrent.ScheduledFuture;

/**
 * @author Simple
 * @author Sphinx :)
 */
public class DuelService {
    private static Logger log = Logger.getLogger(DuelService.class);

    private FastMap<Integer, Integer> duels;
    private FastMap<Integer, ScheduledFuture<?>> tasks;

    public static final DuelService getInstance() {
        return SingletonHolder.instance;
    }


    /**
     * @param duels
     */
    private DuelService() {
        this.duels = new FastMap<Integer, Integer>();
        this.tasks = new FastMap<Integer, ScheduledFuture<?>>();
        log.info("DuelService started.");
    }


    /**
     * Send the duel request to the owner
     *
     * @param requester the player who requested the duel
     * @param responder the player who respond to duel request
     */
    public void onDuelRequest(Player requester, Player responder) {
        /**
         * Check if requester isn't already in a duel and responder is same race
         */
        if (ArenaService.getInstance().isInArena(requester) || ArenaService.getInstance().isInArena(responder)) {
            PacketSendUtility.sendPacket(requester, SM_SYSTEM_MESSAGE.DUEL_PARTNER_INVALID(responder.getName()));
            return;
        }
        if (requester.isEnemyPlayer(responder) || isDueling(requester.getObjectId()) || isDueling(responder.getObjectId()))
        {
            PacketSendUtility.sendPacket(requester, SM_SYSTEM_MESSAGE.DUEL_REJECTED_BY(responder.getName()));
            return;
        }

        RequestResponseHandler rrh = new RequestResponseHandler(requester) {
            @Override
            public void denyRequest(Creature requester, Player responder) {
                rejectDuelRequest((Player) requester, responder);
            }

            @Override
            public void acceptRequest(Creature requester, Player responder) {
                startDuel((Player) requester, responder);
            }
        };
        responder.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_DUEL_DO_YOU_ACCEPT_DUEL, rrh);
        PacketSendUtility.sendPacket(responder, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_DUEL_DO_YOU_ACCEPT_DUEL,
                0, requester.getName()));
        PacketSendUtility.sendPacket(responder, SM_SYSTEM_MESSAGE.DUEL_ASKED_BY(requester.getName()));
    }

    /**
     * Asks confirmation for the duel request
     *
     * @param requester the player whose the duel was requested
     * @param responder the player whose the duel was responded
     */
    public void confirmDuelWith(Player requester, Player responder) {
        /**
         * Check if requester isn't already in a duel and responder is same race
         */
        if (requester.isEnemyPlayer(responder))
            return;

        RequestResponseHandler rrh = new RequestResponseHandler(responder) {
            @Override
            public void denyRequest(Creature requester, Player responder) {
                log.debug("[Duel] Player " + responder.getName() + " confirmed his duel with " + requester.getName());
            }

            @Override
            public void acceptRequest(Creature requester, Player responder) {
                cancelDuelRequest(responder, (Player) requester);
            }
        };
        requester.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_DUEL_DO_YOU_CONFIRM_DUEL, rrh);
        PacketSendUtility.sendPacket(requester, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_DUEL_DO_YOU_CONFIRM_DUEL,
                0, responder.getName()));
        PacketSendUtility.sendPacket(requester, SM_SYSTEM_MESSAGE.DUEL_ASKED_TO(responder.getName()));
    }

    /**
     * Rejects the duel request
     *
     * @param requester the duel requester
     * @param responder the duel responder
     */
    private void rejectDuelRequest(Player requester, Player responder) {
        log.debug("[Duel] Player " + responder.getName() + " rejected duel request from " + requester.getName());
        PacketSendUtility.sendPacket(requester, SM_SYSTEM_MESSAGE.DUEL_REJECTED_BY(responder.getName()));
        PacketSendUtility.sendPacket(responder, SM_SYSTEM_MESSAGE.DUEL_REJECT_DUEL_OF(requester.getName()));
    }

    /**
     * Cancels the duel request
     *
     * @param target    the duel target
     * @param requester
     */
    private void cancelDuelRequest(Player owner, Player target) {
        log.debug("[Duel] Player " + owner.getName() + " cancelled his duel request with " + target.getName());
        PacketSendUtility.sendPacket(target, SM_SYSTEM_MESSAGE.DUEL_CANCEL_DUEL_BY(owner.getName()));
        PacketSendUtility.sendPacket(owner, SM_SYSTEM_MESSAGE.DUEL_CANCEL_DUEL_WITH(target.getName()));
    }

    /**
     * Starts the duel
     *
     * @param requester the player to start duel with
     * @param responder the other player
     */
    private void startDuel(final Player requester, final Player responder) {
        PacketSendUtility.sendPacket(requester, SM_DUEL.SM_DUEL_STARTED(responder.getObjectId()));
        PacketSendUtility.sendPacket(responder, SM_DUEL.SM_DUEL_STARTED(requester.getObjectId()));
        createDuel(requester.getObjectId(), responder.getObjectId());

        /**
         * draw task
         */
        ScheduledFuture<?> task = ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                drawDuel(requester, responder);
            }
        }, (CustomConfig.DUEL_LENGTH * 1000));
        tasks.put(requester.getObjectId(), task);
    }

    /**
     * This method will make the selected players draw the duel
     *
     */
    public void drawDuel(Player player1, Player player2) {
        if (!isDueling(player1.getObjectId()) || !isDueling(player2.getObjectId()))
            return;

        if (player1 == null || player2 == null)
            return;

        /**
         * all debuffs are removed from both Players
         * Stop casting or skill use
         */
        player1.getEffectController().removeAbnormalEffectsByTargetSlot(SkillTargetSlot.DEBUFF);
        player1.getController().cancelCurrentSkill();
        player2.getEffectController().removeAbnormalEffectsByTargetSlot(SkillTargetSlot.DEBUFF);
        player2.getController().cancelCurrentSkill();

        PacketSendUtility.sendPacket(player1, SM_DUEL.SM_DUEL_RESULT(DuelResult.DUEL_DRAW, player1.getName()));
        PacketSendUtility.sendPacket(player2, SM_DUEL.SM_DUEL_RESULT(DuelResult.DUEL_DRAW, player2.getName()));

        removeDuel(player1.getObjectId(), player2.getObjectId());
        tasks.remove(player1.getObjectId());
    }
    
    /**
     * This method will make the selected player lose the duel
     *
     * @param player
     */
    public void loseDuel(Player player) {
        if (!isDueling(player.getObjectId()))
            return;

        /**
         * all debuffs are removed from loser
         * Stop casting or skill use
         */
        player.getEffectController().removeAbnormalEffectsByTargetSlot(SkillTargetSlot.DEBUFF);
        player.getController().cancelCurrentSkill();

        int opponnentId = duels.get(player.getObjectId());
        Player opponent = World.getInstance().findPlayer(opponnentId);

        if (opponent != null) {
            /**
             * all debuffs are removed from winner, but buffs will remain
             * Stop casting or skill use
             */
            opponent.getEffectController().removeAbnormalEffectsByTargetSlot(SkillTargetSlot.DEBUFF);
            opponent.getController().cancelCurrentSkill();

            PacketSendUtility.sendPacket(opponent, SM_DUEL.SM_DUEL_RESULT(DuelResult.DUEL_WON, player.getName()));
            PacketSendUtility.sendPacket(player, SM_DUEL.SM_DUEL_RESULT(DuelResult.DUEL_LOST, opponent.getName()));
        } else {
            log.warn("CHECKPOINT : duel opponent is already out of world");
        }

        removeDuel(player.getObjectId(), opponnentId);

        /**
         * stop draw task
         */
        if (tasks.containsKey(player.getObjectId())) {
            tasks.get(player.getObjectId()).cancel(true);
            tasks.remove(player.getObjectId());
        }
        else if (tasks.containsKey(opponnentId)) {
            tasks.get(opponnentId).cancel(true);
            tasks.remove(opponnentId);
        }
    }

    /**
     * @param player
     * @param lastAttacker
     */
    public void onDie(Player player) {
        loseDuel(player);
        player.getLifeStats().setCurrentHp(1);
    }

    /**
     * @param playerObjId
     * @return true of player is dueling
     */
    public boolean isDueling(int playerObjId) {
        return (duels.containsKey(playerObjId) && duels.containsValue(playerObjId));
    }

    /**
     * @param playerObjId
     * @param targetObjId
     * @return true of player is dueling
     */
    public boolean isDueling(int playerObjId, int targetObjId) {
        return duels.containsKey(playerObjId) && duels.get(playerObjId) == targetObjId;
    }

    /**
     * @param requesterObjId
     * @param responderObjId
     */
    public void createDuel(int requesterObjId, int responderObjId) {
        duels.put(requesterObjId, responderObjId);
        duels.put(responderObjId, requesterObjId);
    }

    /**
     * @param requesterObjId
     * @param responderObjId
     */
    private void removeDuel(int requesterObjId, int responderObjId) {
        duels.remove(requesterObjId);
        duels.remove(responderObjId);
    }

    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder
	{
		protected static final DuelService instance = new DuelService();
	}
}
