/*
 * This file is part of Aion X EMU <aionxemu.com>.
 *
 * This is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.eltnen;

import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.QuestService;
import gameserver.utils.PacketSendUtility;

/**
 * @author Rhys2002
 *         edited by xaerolt
 */
public class _1042KeeperoftheKaidanKey extends QuestHandler {
    private final static int questId = 1042;
    private final static int[] npc_ids = {203989, 203901};
    private final static int[] mob_ids = {212029, 212033}; //Kaidan Blocker Boss, Crack Kaidan Captain              //

    public _1042KeeperoftheKaidanKey() {
        super(questId);
    }

    @Override
    public void register() {
        qe.addQuestLvlUp(questId);
        for (int npc_id : npc_ids)
            qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);
        qe.setQuestItemIds(182201018).add(questId);                         ////////////// fix of q item id	 ///////////////////
        for (int mob_id : mob_ids)                                            //
            qe.setNpcQuestData(mob_id).addOnKillEvent(questId);            //
    }

    @Override
    public boolean onLvlUpEvent(QuestCookie env) {
        return defaultQuestOnLvlUpEvent(env, 1040);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null)
            return false;
        int var = qs.getQuestVarById(0);
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();

        if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 203901) {
                if (env.getDialogId() == -1)
                    return sendQuestDialog(env, 10002);
                else if (env.getDialogId() == 1009)
                    return sendQuestDialog(env, 5);
                else return defaultQuestEndDialog(env);
            }
            return false;
        } else if (qs.getStatus() != QuestStatus.START) {
            return false;
        }
        if (targetId == 203989) {
            switch (env.getDialogId()) {
                case 25:
                    if (var == 0)
                        return sendQuestDialog(env, 1011);
                case 1012:
                    PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 185));
                    break;
                case 10000:
                    if (var == 0) {
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                    }
            }
        } else if (targetId == 203901) {
            switch (env.getDialogId()) {
                case 25:
                    if (var == 2)
                        return sendQuestDialog(env, 1352);
                case 33:
                    if (QuestService.collectItemCheck(env, true)) {
                        player.getInventory().removeFromBagByItemId(182201018, 1);           ///// nobody needs useless junk ///
                        qs.setStatus(QuestStatus.REWARD);
                        updateQuestStatus(env);
                        return sendQuestDialog(env, 5);
                    } else
                        return sendQuestDialog(env, 1438);
            }
        }
        return false;
    }

    @Override
    public boolean onItemUseEvent(QuestCookie env, Item item)           // quest can be completed by using the needed key if it was gained before this fix
    {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        final int id = item.getItemTemplate().getTemplateId();
        final int itemObjId = item.getObjectId();
        if (id != 182201018)                                        //// for quest state change didn't activate without the required item ////
            return false;
        PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 20, 1, 0), true);
        qs.setQuestVarById(0, 2);
        updateQuestStatus(env);
        return true;
    }

    @Override
    public boolean onKillEvent(QuestCookie env)       // implements(at least partly) finding who has the key from monsters /////////////////
    {
        if (defaultQuestOnKillEvent(env, mob_ids, 1, 2))
            return true;
        else
            return false;
    }
}