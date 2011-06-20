/**
 * This file is part of alpha team <alpha-team.com>.
 *
 * alpha team is pryvate software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * alpha team is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with alpha team.  If not, see <http://www.gnu.org/licenses/>.
 */
package gameserver.controllers;

import java.util.List;

import gameserver.configs.administration.AdminConfig;
import gameserver.configs.main.CustomConfig;
import gameserver.controllers.movement.StartMovingListener;
import gameserver.dataholders.DataManager;
import gameserver.model.EmotionType;
import gameserver.model.TaskId;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.group.PlayerGroup;
import gameserver.model.siege.InstancePortal;
import gameserver.model.templates.portal.EntryPoint;
import gameserver.model.templates.portal.ExitPoint;
import gameserver.model.templates.portal.PortalTemplate;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import gameserver.services.InstanceService;
import gameserver.services.TeleportService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.World;
import gameserver.world.WorldMap;
import gameserver.world.WorldMapInstance;
import org.apache.log4j.Logger;


/**
 * @author ATracer
 */
public class PortalController extends NpcController {
    private static final Logger log = Logger.getLogger(PortalController.class);

    PortalTemplate portalTemplate;

    @Override
    public void setOwner(Creature owner) {
        super.setOwner(owner);
        portalTemplate = DataManager.PORTAL_DATA.getPortalTemplate(owner.getObjectTemplate().getTemplateId());
    }

    @Override
    public void onDialogRequest(final Player player) {
        if (portalTemplate == null)
            return;

        if (!CustomConfig.ENABLE_INSTANCES)
            return;

        if (getOwner() instanceof InstancePortal) {
            if (((InstancePortal) getOwner()).getRace().getRaceId() != player.getCommonData().getRace().getRaceId())
                return;
        }

        final int defaultUseTime = 3000;
        PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), getOwner().getObjectId(),
                defaultUseTime, 1));
        PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_QUESTLOOT, 0, getOwner().getObjectId()), true);

        player.getController().cancelTask(TaskId.ITEM_USE);
        player.getObserveController().attach(new StartMovingListener() {

            @Override
            public void moved() {
                player.getController().cancelTask(TaskId.ITEM_USE);
                PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), getOwner().getObjectId(), defaultUseTime, 0));
                PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.END_QUESTLOOT, 0, getOwner().getObjectId()), true);
            }
        });
        player.getController().addNewTask(TaskId.ITEM_USE,
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), getOwner().getObjectId(),
                        defaultUseTime, 0));
                PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.END_QUESTLOOT, 0, getOwner().getObjectId()), true);

                analyzePortation(player);
            }

            /**
             * @param player
             */
            private void analyzePortation(final Player player) {
                if (portalTemplate.getIdTitle() != 0 && player.getCommonData().getTitleId() != portalTemplate.getIdTitle() && CustomConfig.PORTAL_REQUIREMENT_TITLE )
                    return;

                for (EntryPoint point : portalTemplate.getEntryPoint()) {
                    if (point.getRace() != null && !point.getRace().equals(player.getCommonData().getRace()))
                    {
                        PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MOVE_PORTAL_ERROR_INVALID_RACE);
                        return;
                    }
                }

                if (portalTemplate.getRace() != null && !portalTemplate.getRace().equals(player.getCommonData().getRace()) && CustomConfig.PORTAL_REQUIREMENT_RACE) {
                    PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MOVE_PORTAL_ERROR_INVALID_RACE);
                    return;
                }

                if ((portalTemplate.getMaxLevel() != 0 && player.getLevel() > portalTemplate.getMaxLevel() && CustomConfig.PORTAL_REQUIREMENT_LEVEL)
                        || player.getLevel() < portalTemplate.getMinLevel() && CustomConfig.PORTAL_REQUIREMENT_LEVEL) {
                    PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_LEVEL);
                    return;
                }

                PlayerGroup group = player.getPlayerGroup();
                if(player.getAccessLevel() < AdminConfig.INSTANCE_NO_GROUP) {
                    if (portalTemplate.isGroup() && group == null) {
                        PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ENTER_ONLY_PARTY_DON);
                        return;
                    }
                }

                if (portalTemplate.isGroup()) {
                    int worldId = 0;
                    for (ExitPoint point : portalTemplate.getExitPoint()) {
                        if (point.getRace() == null || point.getRace().equals(group.getGroupLeader().getCommonData().getRace()))
                            worldId = point.getMapId();
                    }

                    WorldMapInstance instance;
                    if(group != null) {
                        instance = InstanceService.getRegisteredInstance(worldId, group.getGroupId());
                    }
                    else {
                        instance = InstanceService.getRegisteredInstance(worldId, player.getObjectId());
                        if(instance != null)
                            transfer(player, instance);
                        else
                            port(player);
                        return;
                    }

                    // register if not yet created
                    if (instance == null && group != null) {
                        instance = registerGroup(group);
                    }

                    transfer(player, instance);
                } else if (!portalTemplate.isGroup()) {
                    int worldId = 0;
                    for (ExitPoint point : portalTemplate.getExitPoint()) {
                        if (point.getRace() == null || point.getRace().equals(player.getCommonData().getRace()))
                            worldId = point.getMapId();
                    }
                    WorldMapInstance instance = InstanceService.getRegisteredInstance(worldId, player.getObjectId());
                    // if already registered - just teleport
                    if (instance != null) {
                        transfer(player, instance);
                        return;
                    }
                    port(player);
                }
            }
        }, defaultUseTime));

    }

    /**
     * @param player
     */
    private void port(Player requester) {
        WorldMapInstance instance = null;
        int worldId = 0;
        for (ExitPoint point : portalTemplate.getExitPoint()) {
            if (point.getRace() == null || point.getRace().equals(requester.getCommonData().getRace()))
                worldId = point.getMapId();
        }

        if (portalTemplate.isInstance()) {
            instance = InstanceService.getNextAvailableInstance(worldId);
            InstanceService.registerPlayerWithInstance(instance, requester);

        } else {
            WorldMap worldMap = World.getInstance().getWorldMap(worldId);
            if (worldMap == null) {
                log.warn("There is no registered map with id " + worldId);
                return;
            }
            instance = worldMap.getWorldMapInstance();
        }

        transfer(requester, instance);
    }

    /**
     * @param player
     */
    private WorldMapInstance registerGroup(PlayerGroup group) {
        int worldId = 0;
        for (ExitPoint point : portalTemplate.getExitPoint()) {
            if (point.getRace() == null || point.getRace().equals(group.getGroupLeader().getCommonData().getRace()))
                worldId = point.getMapId();
        }
        WorldMapInstance instance = InstanceService.getNextAvailableInstance(worldId);
        InstanceService.registerGroupWithInstance(instance, group);
        return instance;
    }

    /**
     * @param players
     */
    private void transfer(Player player, WorldMapInstance instance) {
        int instanceId = DataManager.WORLD_MAPS_DATA.getTemplate(instance.getMapId()).getInstanceId();
        int cd = DataManager.WORLD_MAPS_DATA.getTemplate(instance.getMapId()).getCooldown();

        if (InstanceService.onRegisterRequest(player, instanceId, cd) || player.getWorldId() == instance.getMapId() || !CustomConfig.INSTANCE_COOLDOWN) {
            ExitPoint exitPoint = null;
            for (ExitPoint point : portalTemplate.getExitPoint()) {
                if (point.getRace() == null || point.getRace().equals(player.getCommonData().getRace()))
                    exitPoint = point;
            }

            TeleportService.teleportTo(player, exitPoint.getMapId(), instance.getInstanceId(),
                    exitPoint.getX(), exitPoint.getY(), exitPoint.getZ(), 0);
        } else {
            PacketSendUtility.sendMessage(player, "You cannot do that");
        }
    }
}
