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

package quest.beluslan;

import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.state.CreatureState;
import gameserver.model.templates.quest.QuestItems;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import gameserver.network.aion.serverpackets.SM_TARGET_IMMOBILIZE;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.services.ZoneService;
import gameserver.skillengine.effect.EffectId;
import gameserver.utils.MathUtil;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.Executor;
import gameserver.world.MapRegion;
import gameserver.world.zone.ZoneName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 *
 */
public class _2057GlacionttheHardy extends QuestHandler {
    private final static int maxRadius = 200;
    private final static int questId = 2057;
    private final static int[] npc_ids = {204787, 204784};
    private final static int[] mob_ids = {213730, 213788, 213789, 213790, 213791};

    public _2057GlacionttheHardy() {
        super(questId);
    }

    @Override
    public void register() {
        qe.addQuestLvlUp(questId);
        qe.setQuestEnterZone(ZoneName.Q2057).add(questId);
        qe.setNpcQuestData(204787).addOnQuestStart(questId);
        qe.setQuestItemIds(182204316).add(questId);//Fire Bomb
        for (int mob_id : mob_ids)
            qe.setNpcQuestData(mob_id).addOnKillEvent(questId);
        for (int npc_id : npc_ids)
            qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);
    }

    @Override
    public boolean onLvlUpEvent(QuestCookie env) {
        return defaultQuestOnLvlUpEvent(env, 2056);
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
            if (targetId == 204787) {
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

        if (targetId == 204787) {
            switch (env.getDialogId()) {
                case 25:
                    if (var == 0)
                        return sendQuestDialog(env, 1011);
                case 10000:
                    if (var == 0) {
                        qs.setQuestVarById(0, ++var);
                        updateQuestStatus(env);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                    }
                case 1012:
                    PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 246));
                    return sendQuestDialog(env, 1012);
            }
        } else if (targetId == 204784) {
            switch (env.getDialogId()) {
                case 25:
                    if (var == 1)
                        return sendQuestDialog(env, 1352);
                case 10001:
                    if (var == 1) {
                        qs.setQuestVarById(0, ++var);
                        updateQuestStatus(env);
                        ItemService.addItems(player, Collections.singletonList(new QuestItems(182204316, 1)));
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                    }
                case 1354:
                    PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 247));
                    return sendQuestDialog(env, 1354);
            }
        }
        return false;
    }

    @Override
    public boolean onKillEvent(QuestCookie env) {
        QuestState qs = env.getPlayer().getQuestStateList().getQuestState(questId);

        if (qs == null || qs.getStatus() != QuestStatus.START)
            return false;

        int var = qs.getQuestVarById(0);
        int targetId = env.getTargetId();

        if ((targetId == 213730 || targetId == 213788 ||
                targetId == 213789 || targetId == 213790 ||
                targetId == 213791) && var == 3 && qs.getStatus() == QuestStatus.START) {
            int var1 = qs.getQuestVarById(1);
            int var2 = qs.getQuestVarById(2);
            int var3 = qs.getQuestVarById(3);
            int var4 = qs.getQuestVarById(4);
            int var5 = qs.getQuestVarById(5);
            boolean killed = false;

            if (targetId == 213730 && var1 == 0)//Glaciont the Hardy
            {
                qs.setQuestVarById(1, 1);
                updateQuestStatus(env);
                killed = true;
            } else if (targetId == 213788 && var2 == 0)//Frostfist
            {
                qs.setQuestVarById(2, 1);
                updateQuestStatus(env);
                killed = true;
            } else if (targetId == 213789 && var3 == 0)//Iceback
            {
                qs.setQuestVarById(3, 1);
                updateQuestStatus(env);
                killed = true;
            } else if (targetId == 213790 && var4 == 0)//Chillblow
            {
                qs.setQuestVarById(4, 1);
                updateQuestStatus(env);
                killed = true;
            } else if (targetId == 213791 && var5 == 0)//Snowfury
            {
                qs.setQuestVarById(5, 1);
                updateQuestStatus(env);
                killed = true;
            }

            if (killed && (var1 + var2 + var3 + var4 + var5) == 4) {
                qs.setQuestVarById(0, ++var);
                qs.setStatus(QuestStatus.REWARD);
                updateQuestStatus(env);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onItemUseEvent(QuestCookie env, Item item) {
        final Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        final int id = item.getItemTemplate().getTemplateId();
        final int itemObjId = item.getObjectId();

        if (qs != null && qs.getQuestVarById(0) == 3 && id == 182204316 && ZoneService.getInstance().isInsideZone(player, ZoneName.FROST_SPIRIT_VALLEY_220040000)) {
            PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 2000, 0, 0), true);
            ThreadPoolManager.getInstance().schedule(new Runnable() {
                @Override
                public void run() {
                    PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
                    player.getInventory().removeFromBagByItemId(182204316, 1);
                }
            }, 2000);
            DoMobEffect(player);
            return true;
        } else return false;
    }

    private void DoMobEffect(final Player player) {
        final List<Npc> effectedMobs = new ArrayList<Npc>();

        for (MapRegion r : player.getActiveRegion().getNeighbours()) {
            r.doOnAllNpcs(new Executor<Npc>() {
                @Override
                public boolean run(Npc npc) {
                    if (npc == null || !npc.isSpawned())
                        return true;

                    if (npc.isInState(CreatureState.DEAD))
                        return true;

                    if (MathUtil.getDistance(player.getX(), player.getY(), player.getZ(), npc.getX(), npc.getY(), npc.getZ()) > maxRadius)
                        return true;

                    int targetId = npc.getNpcId();
                    if (targetId == 213730 || targetId == 213788 || targetId == 213789 ||
                            targetId == 213790 || targetId == 213791)
                        effectedMobs.add(npc);

                    return true;
                }
            }, true);
        }

        if (effectedMobs.size() > 0) {
            for (Npc npc : effectedMobs) {
                npc.getController().cancelCurrentSkill();
                npc.getEffectController().setAbnormal(EffectId.STUN.getEffectId());
                PacketSendUtility.broadcastPacketAndReceive(npc, new SM_TARGET_IMMOBILIZE(npc));
                npc.getEffectController().sendEffectIconsTo(player);
            }
            ThreadPoolManager.getInstance().schedule(new Runnable() {
                @Override
                public void run() {
                    for (Npc npc : effectedMobs) {
                        npc.getEffectController().unsetAbnormal(EffectId.STUN.getEffectId());
                        PacketSendUtility.broadcastPacketAndReceive(npc, new SM_TARGET_IMMOBILIZE(npc));
                    }
                }
            }, 5000);
        }
    }

    @Override
    public boolean onEnterZoneEvent(QuestCookie env, ZoneName zoneName) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (zoneName != ZoneName.Q2057)
            return false;
        if (qs == null || qs.getStatus() != QuestStatus.START)
            return false;
        if (qs.getQuestVarById(0) == 2) {
            qs.setQuestVarById(0, 3);
            updateQuestStatus(env);
            PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 248));
            return true;
        }
        return false;
    }
}
