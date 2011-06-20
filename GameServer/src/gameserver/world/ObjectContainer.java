/**
 * This file is part of alpha team <alpha-team.com>.
 *
 * alpha team is private software: you can redistribute it and/or modify
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
package gameserver.world;

import gameserver.model.gameobjects.AionObject;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;

import org.apache.log4j.Logger;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;

/**
 * @author xavier
 */
public class ObjectContainer {
    private static Logger log = Logger.getLogger(ObjectContainer.class);

    protected Map<Integer, Player> allPlayersById;
    protected Map<String, Player> allPlayersByName;
    protected Map<Integer, AionObject> allObjects;
    protected Map<Integer, Npc> allNpcs;

    protected ObjectContainer() {
        allPlayersById = Collections.synchronizedMap(new HashMap<Integer, Player>());
        allPlayersByName = Collections.synchronizedMap(new HashMap<String, Player>());
        allObjects = Collections.synchronizedMap(new HashMap<Integer, AionObject>());
        allNpcs = Collections.synchronizedMap(new HashMap<Integer, Npc>());
    }

    public synchronized void storeObject(AionObject object) {
        if (allObjects.containsKey(object.getObjectId()))
            allObjects.remove(object.getObjectId());

        allObjects.put(object.getObjectId(), object);

        if (object instanceof Player) {
            allPlayersByName.put(object.getName(), (Player) object);
            allPlayersById.put(object.getObjectId(), (Player) object);
        }

        if (object instanceof Npc) {
            allNpcs.put(object.getObjectId(), (Npc) object);
        }
    }

    public synchronized void removeObject(AionObject object) {
        allObjects.remove(object.getObjectId());

        if (object instanceof Player) {
            allPlayersById.remove(object.getObjectId());
            allPlayersByName.remove(object.getName());
        }

        if (object instanceof Npc) {
            allNpcs.remove(object.getObjectId());
        }
    }

    public Player findPlayer(String name) {
        return allPlayersByName.get(name);
    }

    public Player findPlayer(int objectId) {
        return allPlayersById.get(objectId);
    }

    public AionObject findAionObject(int objectId) {
        if (objectId == 0)
            return null;

        AionObject ao = allObjects.get(objectId);
        if (ao == null)
            log.info("ObjectContainer.findAionObject(" + objectId + ") Not Found.");
        return ao;
    }

    public void doOnAllPlayers(Executor<Player> playerExecutor, boolean now) {
        Collection<Player> players = allPlayersById.values();
        final Collection<Player> _players;

        synchronized (allPlayersById) {
            _players = new ArrayList<Player>(allPlayersById.size());
            _players.addAll(players);
        }

        playerExecutor.execute(_players, now);
    }

    public void doOnAllPlayers(Executor<Player> playerExecutor) {
        doOnAllPlayers(playerExecutor, false);
    }

    public void doOnAllObjects(Executor<AionObject> objectExecutor, boolean now) {
        Collection<AionObject> objects = allObjects.values();
        final Collection<AionObject> _objects;

        synchronized (allObjects) {
            _objects = new ArrayList<AionObject>(allObjects.size());
            _objects.addAll(objects);
        }

        objectExecutor.execute(_objects, now);
    }

    public void doOnAllObjects(Executor<AionObject> objectExecutor) {
        doOnAllObjects(objectExecutor, false);
    }

    public void doOnAllNpcs(Executor<Npc> npcExecutor, boolean now) {
        Collection<Npc> npcs = allNpcs.values();
        final Collection<Npc> _npcs;

        synchronized (allNpcs) {
            _npcs = new ArrayList<Npc>(allNpcs.size());
            _npcs.addAll(npcs);
        }

        npcExecutor.execute(_npcs, now);
    }

    public void doOnAllNpcs(Executor<Npc> npcExecutor) {
        doOnAllNpcs(npcExecutor, false);
    }

    public int getPlayersCount() {
        return allPlayersById.size();
    }

    public Collection<Player> getPlayers() {
        return allPlayersById.values();
    }

    public int getNpcsCount() {
        return allNpcs.size();
    }

    public int getObjectsCount() {
        return allObjects.size();
    }
}
