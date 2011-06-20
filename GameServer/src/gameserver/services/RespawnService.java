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

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.templates.spawn.SpawnGroup;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.model.templates.spawn.SpawnTime;
import gameserver.utils.ThreadPoolManager;
import gameserver.utils.gametime.DayTime;
import gameserver.utils.gametime.GameTimeManager;
import gameserver.world.World;

import java.util.concurrent.Future;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author ATracer
 */
public class RespawnService {
    static List<VisibleObject> dayTimeSpawns = new CopyOnWriteArrayList<VisibleObject>();

    /**
     * @param npc
     * @return Future<?>
     */
    public static Future<?> scheduleDecayTask(final Npc npc) {
        int decayInterval = (npc.getSpawn().getSpawnGroup().getInterval() - 2);
        if (decayInterval > 240)
            decayInterval = 240;

        return ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                npc.getController().onDespawn(false);
            }
        }, decayInterval * 1000);
    }

    /**
     * @param visibleObject
     */
    public static Future<?> scheduleRespawnTask(final VisibleObject visibleObject) {
        final int interval = visibleObject.getSpawn().getSpawnGroup().getInterval();

        return ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                SpawnTime spawnTime = visibleObject.getSpawn().getSpawnGroup().getSpawnTime();
                if (spawnTime != null) {
                    DayTime dayTime = GameTimeManager.getGameTime().getDayTime();
                    if (!spawnTime.isAllowedDuring(dayTime)) {
                        addDayTimeSpawn(visibleObject);
                        return;
                    }
                }
                scheduleRespawnTaskOk(visibleObject);
            }

        }, interval * 1000);
    }

    public static void addDayTimeSpawn(VisibleObject visibleObject) {
        synchronized (dayTimeSpawns) {
            if (!dayTimeSpawns.contains(visibleObject))
                dayTimeSpawns.add(visibleObject);
        }    
    }

    public static boolean scheduleRespawnTaskOk(VisibleObject visibleObject) {
        int instanceId = visibleObject.getInstanceId();
        int worldId = visibleObject.getSpawn().getWorldId();
        boolean instanceExists = InstanceService.isInstanceExist(worldId, instanceId);

        if (visibleObject.getSpawn().isNoRespawn(instanceId) || !instanceExists) {
            visibleObject.getController().delete();
            return false;
        } else {
            World world = World.getInstance();
            visibleObject.getSpawn().getSpawnGroup().exchangeSpawn(visibleObject);
            world.setPosition(visibleObject, worldId, visibleObject.getSpawn().getX(), visibleObject.getSpawn().getY(), visibleObject.getSpawn().getZ(), visibleObject.getSpawn().getHeading());
            //call onRespawn before actual spawning
            visibleObject.getController().onRespawn();
            world.spawn(visibleObject);
            return true;
        }    
    }

    public static void RespawnDelayedDayTimeSpawns(DayTime dayTime) {
        synchronized (dayTimeSpawns) {
            for (VisibleObject visibleObject : dayTimeSpawns)
            {
                SpawnTemplate spawnTemplate = visibleObject.getSpawn();
                if (spawnTemplate == null) {
                    dayTimeSpawns.remove(visibleObject);
                    continue;
                }

                int instanceId = visibleObject.getInstanceId();
                if (spawnTemplate.isSpawned(instanceId)) {
                    dayTimeSpawns.remove(visibleObject);
                    continue;
                }

                SpawnGroup spawnGroup = spawnTemplate.getSpawnGroup();
                if (spawnGroup == null) {
                    dayTimeSpawns.remove(visibleObject);
                    continue;
                }

                SpawnTime spawnTime = spawnGroup.getSpawnTime();
                if (spawnTime == null) {
                    dayTimeSpawns.remove(visibleObject);
                    continue;
                }

                if (spawnTime.isAllowedDuring(dayTime)) {
                    scheduleRespawnTaskOk(visibleObject);
                }
            }
        }
    }
}
