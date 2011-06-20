/*
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
package gameserver.ai.events.handler;

import gameserver.ai.AI;
import gameserver.ai.events.Event;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.templates.spawn.SpawnGroup;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.model.templates.spawn.SpawnTime;
import gameserver.services.RespawnService;
import gameserver.utils.gametime.DayTime;
import gameserver.utils.gametime.GameTimeManager;

/**
 * @author ATracer
 */
public class DayTimeChangeEventHandler implements EventHandler {
    @Override
    public Event getEvent() {
        return Event.DAYTIME_CHANGE;
    }

    @Override
    public void handleEvent(Event event, AI<?> ai) {
        Npc owner = (Npc) ai.getOwner();
        SpawnTemplate spawn = owner.getSpawn();

        SpawnGroup group = spawn.getSpawnGroup();
        SpawnTime spawnTime = group.getSpawnTime();
        if (spawnTime == null)
            return;

        int instanceId = owner.getInstanceId();

        DayTime dayTime = GameTimeManager.getGameTime().getDayTime();
        if (spawnTime.isAllowedDuring(dayTime)) {
            RespawnService.addDayTimeSpawn((VisibleObject) owner);
            RespawnService.RespawnDelayedDayTimeSpawns(dayTime);
        }
        else {
            owner.getController().onDespawn(true);
            owner.getController().scheduleRespawn();
        }
    }
}
