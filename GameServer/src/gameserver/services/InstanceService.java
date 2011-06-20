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

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.utils.Rnd;
import gameserver.dao.InstanceTimeDAO;
import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.AionObject;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.group.PlayerGroup;
import gameserver.model.templates.WorldMapTemplate;
import gameserver.model.templates.portal.EntryPoint;
import gameserver.model.templates.portal.PortalTemplate;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.spawnengine.SpawnEngine;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.Executor;
import gameserver.world.World;
import gameserver.world.WorldMap;
import gameserver.world.WorldMapInstance;
import org.apache.log4j.Logger;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ATracer
 * @author Arkshadow
 */
public class InstanceService {
    private static Logger log = Logger.getLogger(InstanceService.class);

    /**
     * @param worldId
     * @param destroyTime
     * @return
     */
    public synchronized static WorldMapInstance getNextAvailableInstance(int worldId) {
        WorldMap map = World.getInstance().getWorldMap(worldId);

        if (!map.isInstanceType())
            throw new UnsupportedOperationException("Invalid call for next available instance  of " + worldId);

        int nextInstanceId = map.getNextInstanceId();

        log.info("Creating new instance: " + worldId + " " + nextInstanceId);

        WorldMapInstance worldMapInstance = new WorldMapInstance(map, nextInstanceId);
        startInstanceChecker(worldMapInstance);
        map.addInstance(nextInstanceId, worldMapInstance);
        SpawnEngine.getInstance().spawnInstance(worldId, worldMapInstance.getInstanceId());

        return worldMapInstance;
    }

    /**
     * Instance will be destroyed All players moved to bind location All objects - deleted
     */
    private static void destroyInstance(WorldMapInstance instance) {
        instance.getEmptyInstanceTask().cancel(false);

        final int worldId = instance.getMapId();
        int instanceId = instance.getInstanceId();

        WorldMap map = World.getInstance().getWorldMap(worldId);
        map.removeWorldMapInstance(instanceId);

        log.info("Destroying instance:" + worldId + " " + instanceId);

        instance.doOnAllObjects(new Executor<AionObject>() {
            @Override
            public boolean run(AionObject obj) {
                if (obj instanceof Player) {
                    Player player = (Player) obj;
                    PortalTemplate portal = DataManager.PORTAL_DATA.getInstancePortalTemplate(worldId, player.getCommonData().getRace());
                    moveToEntryPoint((Player) obj, portal, true);
                } else if (obj instanceof VisibleObject) {
                    ((VisibleObject) obj).getController().delete();
                }
                return true;
            }
        }, true);
    }

    /**
     * @param instance
     * @param player
     */
    public static void registerPlayerWithInstance(WorldMapInstance instance, Player player) {
        instance.register(player.getObjectId());
    }

    /**
     * @param instance
     * @param group
     */
    public static void registerGroupWithInstance(WorldMapInstance instance, PlayerGroup group) {
        group.setInstanceStartTimeNow();
        group.setGroupInstancePoints(0);
        instance.registerGroup(group);
    }

    /**
     * @param worldId
     * @param objectId
     * @return instance or null
     */
    public static WorldMapInstance getRegisteredInstance(int worldId, int objectId) {
        for (WorldMapInstance instance : World.getInstance().getWorldMap(worldId).getInstances()) {
            if (instance.isRegistered(objectId))
                return instance;
        }
        return null;
    }

    public static Map<Integer, Integer> getTimeInfo(Player player) {
        Map<Integer, Integer> result = new HashMap<Integer, Integer>();
        Map<Integer, Long> infos = DAOManager.getDAO(InstanceTimeDAO.class).getTimes(player);
        long actualTimestamp;
        int remainingTime;

        for (int i : infos.keySet()) {
            actualTimestamp = Calendar.getInstance().getTimeInMillis();
            remainingTime = (int) ((infos.get(i) - actualTimestamp) / 1000);
            if (remainingTime < 0)
                remainingTime = 0;
            log.debug("instance " + i + " time " + remainingTime + " player " + player.getName());
            result.put(i, remainingTime);
        }

        return result;
    }

    public static boolean onRegisterRequest(Player player, int instanceId, int cd) {
        if (!DAOManager.getDAO(InstanceTimeDAO.class).exists(player, instanceId)) {
            DAOManager.getDAO(InstanceTimeDAO.class).createEntry(instanceId, player);
        }
        Map<Integer, Integer> infos = getTimeInfo(player);
        if (infos.get(instanceId) != 0) {
            return false;
        }
        DAOManager.getDAO(InstanceTimeDAO.class).updateEntry(instanceId, player, cd);
        return true;
    }

    /**
     * @param player
     */
    public static void onPlayerLogin(Player player) {
        int worldId = player.getWorldId();

        WorldMapTemplate worldTemplate = DataManager.WORLD_MAPS_DATA.getTemplate(worldId);
        if (worldTemplate.isInstance()) {
            PortalTemplate portalTemplate = null;

            try {
                portalTemplate = DataManager.PORTAL_DATA.getInstancePortalTemplate(worldId, player.getCommonData().getRace());
            } catch (IllegalArgumentException e) {
                log.error("No portal template found for " + worldId);
                return;
            }

            if (portalTemplate == null) {
                log.error("No portal template found for " + worldId);
                return;
            }

            int lookupId = player.getObjectId();
            if (portalTemplate.isGroup() && player.getPlayerGroup() != null) {
                lookupId = player.getPlayerGroup().getGroupId();
            }

            WorldMapInstance registeredInstance = getRegisteredInstance(worldId, lookupId);
            if (registeredInstance != null) {
                World.getInstance().setPosition(player, worldId, registeredInstance.getInstanceId(), player.getX(), player.getY(),
                        player.getZ(), player.getHeading());
                return;
            }

            moveToEntryPoint(player, portalTemplate, false);
        }
    }

    /**
     * @param player
     * @param portalTemplates
     */
    private static void moveToEntryPoint(Player player, PortalTemplate portalTemplate, boolean useTeleport) {
        EntryPoint entryPoint = null;
        List<EntryPoint> entryPoints = portalTemplate.getEntryPoint();

        for (EntryPoint point : entryPoints) {
            if (point.getRace() == null || point.getRace().equals(player.getCommonData().getRace())) {
                entryPoint = point;
                break;
            }
        }

        if (entryPoint == null) {
            log.warn("Entry point not found for " + player.getCommonData().getRace() + " " + player.getWorldId());
            return;
        }

        if (useTeleport) {
            TeleportService.teleportTo(player, entryPoint.getMapId(), 1, entryPoint.getX(), entryPoint.getY(),
                    entryPoint.getZ(), 0);
        } else {
            World.getInstance().setPosition(player, entryPoint.getMapId(), 1, entryPoint.getX(), entryPoint.getY(),
                    entryPoint.getZ(), player.getHeading());
        }

    }

    /**
     * @param worldId
     * @param instanceId
     * @return
     */
    public static boolean isInstanceExist(int worldId, int instanceId) {
        return World.getInstance().getWorldMap(worldId).getWorldMapInstanceById(instanceId) != null;
    }

    /**
     * @param worldMapInstance
     */
    private static void startInstanceChecker(WorldMapInstance worldMapInstance) {
        int delay = 60000 + Rnd.get(-10, 10);
        worldMapInstance.setEmptyInstanceTask(ThreadPoolManager.getInstance().scheduleAtFixedRate(
                new EmptyInstanceCheckerTask(worldMapInstance), delay, delay));
    }

    private static class EmptyInstanceCheckerTask implements Runnable {
        private WorldMapInstance worldMapInstance;

        private EmptyInstanceCheckerTask(WorldMapInstance worldMapInstance) {
            this.worldMapInstance = worldMapInstance;
        }

        @Override
        public void run() {
            PlayerGroup registeredGroup = worldMapInstance.getRegisteredGroup();
            if (registeredGroup == null) {
                if (worldMapInstance.getPlayersCount() == 0) {
                    destroyInstance(worldMapInstance);
                    return;
                }
            } else if (registeredGroup.size() == 0) {
                destroyInstance(worldMapInstance);
			}
		}
	}
    
    public static VisibleObject addNewSpawn(int worldId, int instanceId, int templateId, float x, float y, float z, byte heading, boolean noRespawn) {
        SpawnTemplate spawn = SpawnEngine.getInstance().addNewSpawn(worldId, instanceId, templateId, x, y, z, heading, 0, 0, noRespawn);
        return SpawnEngine.getInstance().spawnObject(spawn, instanceId);
    }
}
