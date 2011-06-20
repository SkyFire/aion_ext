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
package gameserver.questEngine.handlers;

import gameserver.dataholders.DataManager;
import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.QuestTemplate;
import gameserver.model.templates.bonus.AbstractInventoryBonus;
import gameserver.model.templates.quest.QuestItems;
import gameserver.network.aion.serverpackets.*;
import gameserver.questEngine.HandlerResult;
import gameserver.questEngine.QuestEngine;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.services.QuestService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.zone.ZoneName;

import java.util.Collections;

/**
 * @author MrPoke
 */
public class QuestHandler {
    private final Integer questId;

    protected QuestEngine qe;
    private Object syncObject = new Object();
    private boolean busy = false;

    /**
     * @param questId
     */
    protected QuestHandler(Integer questId) {
        this.questId = questId;
        this.qe = QuestEngine.getInstance();
    }

    public synchronized void updateQuestStatus(QuestCookie env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        PacketSendUtility.sendPacket(player, new SM_QUEST_ACCEPTED(questId, qs.getStatus(), qs.getQuestVars().getQuestVars()));
        if (qs.getStatus() == QuestStatus.START || qs.getStatus() == QuestStatus.REWARD || qs.getStatus() == QuestStatus.COMPLETE)
            player.getController().updateNearbyQuests();
        env.setQuestVars(qs.getQuestVars().getQuestVars());
    }

    public boolean sendQuestDialog(QuestCookie env, int dialogId) {
        Player player = env.getPlayer();
        int targetObjId = env.getVisibleObject() == null ? 0 : env.getVisibleObject().getObjectId();
        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjId, dialogId, questId));
        return true;
    }

    public boolean sendQuestDialog(QuestCookie env, int dialogId, int giveItemId, int giveItemCount, int removeItemId, int removeItemCount) {
        if (giveItemId != 0 && giveItemCount != 0)
            if (!defaultQuestGiveItem(env, giveItemId, giveItemCount))
                return false;
        defaultQuestRemoveItem(env, removeItemId, removeItemCount);
        Player player = env.getPlayer();
        int targetObjId = env.getVisibleObject() == null ? 0 : env.getVisibleObject().getObjectId();
        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjId, dialogId, questId));
        return true;
    }

    public boolean defaultCloseDialog(QuestCookie env, int step, int nextstep) {
        return defaultCloseDialog(env, step, nextstep, false, false, 0, 0, 0, 0, 0);
    }

    public boolean defaultCloseDialog(QuestCookie env, int step, int nextstep, boolean reward, boolean sameNpc) {
        return defaultCloseDialog(env, step, nextstep, reward, sameNpc, 0, 0, 0, 0, 0);
    }

    public boolean defaultCloseDialog(QuestCookie env, int step, int nextstep, boolean reward, boolean sameNpc, int rewardId) {
        return defaultCloseDialog(env, step, nextstep, reward, sameNpc, rewardId, 0, 0, 0, 0);
    }

    public boolean defaultCloseDialog(QuestCookie env, int step, int nextstep, int giveItemId, int giveItemCount, int removeItemId, int removeItemCount) {
        return defaultCloseDialog(env, step, nextstep, false, false, 0, giveItemId, giveItemCount, removeItemId, removeItemCount);
    }

    public boolean defaultCloseDialog(QuestCookie env, int step, int nextstep, boolean reward, boolean sameNpc, int giveItemId, int giveItemCount, int removeItemId, int removeItemCount) {
        return defaultCloseDialog(env, step, nextstep, reward, sameNpc, 0, giveItemId, giveItemCount, removeItemId, removeItemCount);
    }

    public boolean defaultCloseDialog(QuestCookie env, int step, int nextstep, boolean reward, boolean sameNpc, int rewardId, int giveItemId, int giveItemCount, int removeItemId, int removeItemCount) {
        QuestState qs = env.getPlayer().getQuestStateList().getQuestState(questId);
        if (qs.getQuestVarById(env.getQuestVarNum()) == step) {
            if (giveItemId != 0 && giveItemCount != 0)
                if (!defaultQuestGiveItem(env, giveItemId, giveItemCount))
                    return false;
            defaultQuestRemoveItem(env, removeItemId, removeItemCount);
            Player player = env.getPlayer();
            if (nextstep != 0)
                qs.setQuestVar(nextstep);
            if (reward)
                qs.setStatus(QuestStatus.REWARD);
            updateQuestStatus(env);
            if (sameNpc)
                return defaultQuestEndDialog(env, rewardId);
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
            return true;
        }
        return false;
    }

    public boolean defaultQuestStartDialog(QuestCookie env) {
        switch (env.getDialogId()) {
            case 1007:
                return sendQuestDialog(env, 4);
            case 1002:
                if (QuestService.startQuest(env, QuestStatus.START))
                    return sendQuestDialog(env, 1003);
                else
                    return false;
            case 1003:
                return sendQuestDialog(env, 1004);
        }
        return false;
    }

    public boolean defaultQuestStartItem(QuestCookie env) {
        Player player = env.getPlayer();
        switch (env.getDialogId()) {
            case 1002:
                QuestService.startQuest(env, QuestStatus.START);
                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 0));
                return true;
            case 1003:
                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 0));
                return true;
        }
        return false;
    }

    public boolean defaultQuestStartItem(QuestCookie env, int giveItemId, int giveItemCount, int removeItemId, int removeItemCount) {
        Player player = env.getPlayer();
        switch (env.getDialogId()) {
            case 1002:
                if (giveItemId != 0 && giveItemCount != 0)
                    if (!defaultQuestGiveItem(env, giveItemId, giveItemCount))
                        return false;
                defaultQuestRemoveItem(env, removeItemId, removeItemCount);
                QuestService.startQuest(env, QuestStatus.START);
                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 0));
                return true;
            case 1003:
                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 0));
                return true;
        }
        return false;
    }

    public boolean defaultQuestEndDialog(QuestCookie env) {
        return defaultQuestEndDialog(env, 0);
    }

    public boolean defaultQuestEndDialog(QuestCookie env, int reward) {
        int targetObjId = env.getVisibleObject() == null ? 0 : env.getVisibleObject().getObjectId();
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        switch (env.getDialogId()) {
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
                if (QuestService.questFinish(env, reward)) {
                    PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjId, 0));
                    return true;
                }
                return false;
            case 1009:
            case -1:
                if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
                    return sendQuestDialog(env, 5 + reward);
                }
        }
        return false;
    }

    public boolean defaultQuestOnKillEvent(QuestCookie env, int npcId, int startVar, int endVar) //for single npc kills
    {
        int[] mobids = {npcId};
        if (defaultQuestOnKillEvent(env, mobids, startVar, endVar))
            return true;
        else
            return false;
    }

    public boolean defaultQuestOnKillEvent(QuestCookie env, int[] npcIds, int startVar, int endVar) //for multi npcs kills
    {
        if (defaultQuestOnKillEvent(env, npcIds, startVar, endVar, 0))
            return true;
        else
            return false;
    }

    public boolean defaultQuestOnKillEvent(QuestCookie env, int npcId, int startVar, int endVar, int varNum) //for multi npcs kills
    {
        int[] mobids = {npcId};
        if (defaultQuestOnKillEvent(env, mobids, startVar, endVar, varNum))
            return true;
        else
            return false;
    }

    public boolean defaultQuestOnKillEvent(QuestCookie env, int[] npcIds, int startVar, int endVar, int varNum) //for multi npcs kills
    {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null)
            return false;
        int var = qs.getQuestVarById(varNum);

        if (qs.getStatus() != QuestStatus.START)
            return false;
        for (int id : npcIds) {
            if (env.getTargetId() == id) {
                if (var >= startVar && var < endVar) {
                    qs.setQuestVarById(varNum, var + 1);
                    updateQuestStatus(env);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean defaultQuestOnKillEvent(QuestCookie env, int npcId, int startVar, boolean reward) //for single npc kills
    {
        int[] mobids = {npcId};
        if (defaultQuestOnKillEvent(env, mobids, startVar, reward, 0))
            return true;
        else
            return false;
    }

    public boolean defaultQuestOnKillEvent(QuestCookie env, int npcId, int startVar, boolean reward, int varNum) //for single npc kills
    {
        int[] mobids = {npcId};
        if (defaultQuestOnKillEvent(env, mobids, startVar, reward, varNum))
            return true;
        else
            return false;
    }

    public boolean defaultQuestOnKillEvent(QuestCookie env, int[] npcIds, int startVar, boolean reward) //for single npc kills
    {
        if (defaultQuestOnKillEvent(env, npcIds, startVar, reward, 0))
            return true;
        else
            return false;
    }

    public boolean defaultQuestOnKillEvent(QuestCookie env, int[] npcIds, int startVar, boolean reward, int varNum) //for multi npcs kills
    {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null)
            return false;
        int var = qs.getQuestVarById(varNum);

        if (qs.getStatus() != QuestStatus.START)
            return false;
        for (int id : npcIds) {
            if (env.getTargetId() == id) {
                if (var == startVar) {
                    if (reward)
                        qs.setStatus(QuestStatus.REWARD);
                    else
                        qs.setQuestVarById(varNum, var + 1);
                    updateQuestStatus(env);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean defaultQuestOnKillPlayerEvent(QuestCookie env, int enemyAbyssRank, int startVar, int endVar, boolean reward) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null)
            return false;
        int var = qs.getQuestVarById(0);
        if (qs.getStatus() != QuestStatus.START)
            return false;
        if (env.getVisibleObject() instanceof Player) {
            Player enemy = (Player) env.getVisibleObject();
            if (player.getCommonData().getRace() != enemy.getCommonData().getRace()) {
                if (enemy.getAbyssRank().getRank().getId() >= enemyAbyssRank) {
                    if (var >= startVar && var < endVar) {
                        if (reward)
                            qs.setStatus(QuestStatus.REWARD);
                        else
                            qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * @return false if quest can't be started or true if can
     */
    public boolean defaultQuestOnLvlUpEvent(QuestCookie env) {
        int[] quests = {0};
        return defaultQuestOnLvlUpEvent(env, quests);
    }

    public boolean defaultQuestOnLvlUpEvent(QuestCookie env, int quest) {
        int[] quests = {quest};
        return defaultQuestOnLvlUpEvent(env, quests);
    }

    public boolean defaultQuestOnLvlUpEvent(QuestCookie env, int[] quests) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        boolean lvlCheck = QuestService.checkLevelRequirement(env.getQuestId(), player.getCommonData().getLevel());
        if (qs == null || qs.getStatus() != QuestStatus.LOCKED || !lvlCheck)
            return false;
        for (int id : quests) {
            if (id != 0) {
                QuestState qs2 = player.getQuestStateList().getQuestState(id);
                if (qs2 == null || qs2.getStatus() != QuestStatus.COMPLETE)
                    return false;
            }
        }
        qs.setStatus(QuestStatus.START);
        updateQuestStatus(env);
        return true;
    }

    /**
     * @return false if qs == null or true if init success
     */
    public boolean defaultQuestOnDialogInitStart(QuestCookie env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null)
            return false;
        env.setQuestWorkVar(0);
        env.setQuestVars(qs.getQuestVars().getQuestVars());
        return true;
    }

    /**
     * @return false and show the movie
     */
    public boolean defaultQuestMovie(QuestCookie env, int MovieId) {
        Player player = env.getPlayer();
        PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, MovieId));
        return false;
    }

    /**
     * @return false if not enough items collected
     */
    public boolean defaultQuestItemCheck(QuestCookie env, int step, int nextstep, boolean reward, int checkOkId, int checkFailId) {
        return defaultQuestItemCheck(env, step, nextstep, reward, checkOkId, checkFailId, 0, 0);
    }

    public boolean defaultQuestItemCheck(QuestCookie env, int step, int nextstep, boolean reward, int checkOkId, int checkFailId, int giveItemId, int giveItemCount) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs.getQuestVarById(env.getQuestVarNum()) == step) {
            if (QuestService.collectItemCheck(env, true)) {
                if (giveItemId != 0 && giveItemCount != 0)
                    if (!defaultQuestGiveItem(env, giveItemId, giveItemCount))
                        return false;
                if (nextstep != 0)
                    qs.setQuestVar(nextstep);
                if (reward)
                    qs.setStatus(QuestStatus.REWARD);
                updateQuestStatus(env);
                return sendQuestDialog(env, checkOkId);
            } else
                return sendQuestDialog(env, checkFailId);
        }
        return false;
    }

    public boolean defaultQuestNoneDialog(QuestCookie env, int startNpcId) {
        QuestTemplate template = DataManager.QUEST_DATA.getQuestById(questId);
        return defaultQuestNoneDialog(env, template, startNpcId, 1011);
    }

    public boolean defaultQuestNoneDialog(QuestCookie env, int startNpcId, int dialogId) {
        QuestTemplate template = DataManager.QUEST_DATA.getQuestById(questId);
        return defaultQuestNoneDialog(env, template, startNpcId, dialogId);
    }

    public boolean defaultQuestNoneDialog(QuestCookie env, QuestTemplate template, int startNpcId) {
        return defaultQuestNoneDialog(env, template, startNpcId, 1011);
    }

    public boolean defaultQuestNoneDialog(QuestCookie env, QuestTemplate template, int startNpcId, int dialogId) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() == QuestStatus.NONE || (qs.getStatus() == QuestStatus.COMPLETE && (qs.getCompliteCount() <= template.getMaxRepeatCount()))) {
            if (env.getTargetId() == startNpcId) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, dialogId);
                else
                    return defaultQuestStartDialog(env);
            }
        }
        return false;
    }

    public boolean defaultQuestNoneDialog(QuestCookie env, int startNpcId, int dialogId, int itemId, int itemCout) {
        QuestTemplate template = DataManager.QUEST_DATA.getQuestById(questId);
        return defaultQuestNoneDialog(env, template, startNpcId, dialogId, itemId, itemCout);
    }

    public boolean defaultQuestNoneDialog(QuestCookie env, int startNpcId, int itemId, int itemCout) {
        QuestTemplate template = DataManager.QUEST_DATA.getQuestById(questId);
        return defaultQuestNoneDialog(env, template, startNpcId, 1011, itemId, itemCout);
    }

    public boolean defaultQuestNoneDialog(QuestCookie env, QuestTemplate template, int startNpcId, int itemId, int itemCout) {
        return defaultQuestNoneDialog(env, template, startNpcId, 1011, itemId, itemCout);
    }

    public boolean defaultQuestNoneDialog(QuestCookie env, QuestTemplate template, int startNpcId, int dialogId, int itemId, int itemCout) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() == QuestStatus.NONE || (qs.getStatus() == QuestStatus.COMPLETE && (qs.getCompliteCount() <= template.getMaxRepeatCount()))) {
            if (env.getTargetId() == startNpcId) {
                if (env.getDialogId() == 25)
                    return sendQuestDialog(env, dialogId);
                if (itemId != 0 && itemCout != 0) {
                    if (env.getDialogId() == 1002) {
                        if (defaultQuestGiveItem(env, itemId, itemCout))
                            return defaultQuestStartDialog(env);
                        else
                            return true;
                    } else
                        return defaultQuestStartDialog(env);
                } else
                    return defaultQuestStartDialog(env);
            }
        }
        return false;
    }

    public boolean defaultQuestRewardDialog(QuestCookie env, int rewardNpcId, int reportDialogId) {
        return defaultQuestRewardDialog(env, rewardNpcId, reportDialogId, 0);
    }

    public boolean defaultQuestRewardDialog(QuestCookie env, int rewardNpcId, int reportDialogId, int rewardId) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs.getStatus() == QuestStatus.REWARD) {
            if (env.getTargetId() == rewardNpcId) {
                if (env.getDialogId() == -1 && reportDialogId != 0)
                    return sendQuestDialog(env, reportDialogId);
                else
                    return defaultQuestEndDialog(env, rewardId);
            }
        }
        return false;
    }

    public boolean defaultQuestGiveItem(QuestCookie env, int giveItemId, int giveItemCount) {
        Player player = env.getPlayer();
        if (giveItemId != 0 && giveItemCount != 0)
            if (player.getInventory().getItemCountByItemId(giveItemId) == 0)
                if (ItemService.addItems(player, Collections.singletonList(new QuestItems(giveItemId, giveItemCount))))
                    return true;
        return false;
    }

    public boolean defaultQuestRemoveItem(QuestCookie env, int removeItemId, int removeItemCount) {
        Player player = env.getPlayer();
        if (removeItemId != 0 && removeItemCount != 0) {
            player.getInventory().removeFromBagByItemId(removeItemId, removeItemCount);
            return true;
        }
        return false;
    }

    public boolean defaultQuestUseNpc(final QuestCookie env, int startVar, int endVar, EmotionType Start, final EmotionType End, boolean npcDissaper) {
        final Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        int var = qs.getQuestVarById(env.getQuestVarNum());

        if (var >= startVar && var < endVar) {
            synchronized (syncObject) {
                if (busy)
                    return false;
                busy = true;
            }
            final int targetObjectId = env.getVisibleObject().getObjectId();
            PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 1));
            PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, Start, 0, targetObjectId), true);

            try {
                ThreadPoolManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        if (!player.isTargeting(targetObjectId))
                            return;
                        if (player.getTarget() == null || player.getTarget() instanceof Player) {
                            PacketSendUtility.sendMessage(player, "Invalid target selected.");
                            return;
                        }
                        PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 0));
                        PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, End, 0, targetObjectId), true);
                        PacketSendUtility.broadcastPacket(player.getTarget(), new SM_EMOTION((Creature) player.getTarget(), EmotionType.EMOTE, 128, 0));

                        QuestUseNpcInsideFunction(env);
                    }
                }, 3000);
            }
            finally {
                busy = false;
            }
            return npcDissaper;
        }
        return false;
    }

    /**
     * @return the questId
     */
    public Integer getQuestId() {
        return questId;
    }

    public boolean onDialogEvent(QuestCookie questEnv) {
        return false;
    }

    public boolean onEnterWorldEvent(QuestCookie questEnv) {
        return false;
    }

    public boolean onEnterZoneEvent(QuestCookie questEnv, ZoneName zoneName) {
        return false;
    }

    public boolean onItemUseEvent(QuestCookie questEnv, Item item) {
        return false;
    }

    public boolean onItemSellBuyEvent(QuestCookie questEnv, int itemId) {
        return false;
    }

    public boolean onKillEvent(QuestCookie questEnv) {
        return false;
    }

    public boolean onAttackEvent(QuestCookie questEnv) {
        return false;
    }

    public boolean onActionItemEvent(QuestCookie questEnv) {
        return false;
    }

    public boolean onLvlUpEvent(QuestCookie questEnv) {
        return false;
    }

    public boolean onDieEvent(QuestCookie questEnv) {
        return false;
    }

    public boolean onMovieEndEvent(QuestCookie questEnv, int movieId) {
        return false;
    }

    public boolean onQuestFinishEvent(QuestCookie questEnv) {
        return false;
    }

    public boolean onQuestAbortEvent(QuestCookie questEnv) {
        return false;
    }

    public boolean onQuestTimerEndEvent(QuestCookie questEnv) {
        return false;
    }

    public boolean onSkillUseEvent(QuestCookie questEnv, int skillId) {
        return false;
    }

    public HandlerResult onBonusApplyEvent(QuestCookie questEnv, int index, AbstractInventoryBonus bonus) {
        return HandlerResult.UNKNOWN;
    }

    public void register() {
    }

    public void QuestUseNpcInsideFunction(QuestCookie env) {
    }
}
