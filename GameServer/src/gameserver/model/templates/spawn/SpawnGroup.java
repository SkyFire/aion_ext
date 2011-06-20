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
package gameserver.model.templates.spawn;

import com.aionemu.commons.utils.Rnd;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.siege.SiegeRace;
import gameserver.spawnengine.SpawnHandlerType;
import org.apache.log4j.Logger;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "spawn")
public class SpawnGroup {
    /**
     * XML attributes
     * Order should be reversed to XML attributes order
     */
    @XmlAttribute(name = "time")
    private SpawnTime spawnTime;
    @XmlAttribute(name = "anchor")
    private String anchor;
    @XmlAttribute(name = "handler")
    private SpawnHandlerType handler;
    @XmlAttribute(name = "interval")
    private int interval;
    @XmlAttribute(name = "pool")
    private int pool;
    @XmlAttribute(name = "npcid")
    private int npcid;
    @XmlAttribute(name = "map")
    private int mapid;
    @XmlAttribute(name = "rw")
    private int randomWalk;
    @XmlAttribute(name = "npcid_dr")
    private int siegeNpcId_balaur;
    @XmlAttribute(name = "npcid_da")
    private int siegeNpcId_asmodians;
    @XmlAttribute(name = "npcid_li")
    private int siegeNpcId_elyos;
    @XmlAttribute(name = "boss")
    private boolean boss;

    @XmlElement(name = "object")
    private List<SpawnTemplate> objects;


    /**
     * Real-time properties
     */
    @XmlTransient
    private Map<Integer, Integer> lastSpawnedTemplate = new HashMap<Integer, Integer>();

    /**
     * Constructor used by unmarshaller
     */
    public SpawnGroup() {
    }

    /**
     * Constructor used to create new spawns not defined in xml
     *
     * @param mapid
     * @param npcid
     * @param interval
     * @param pool
     */
    public SpawnGroup(int mapid, int npcid, int interval, int pool) {
        super();
        this.mapid = mapid;
        this.npcid = npcid;
        this.interval = interval;
        this.pool = pool;
    }

    void afterUnmarshal(Unmarshaller u, Object parent) {
        if (objects != null && pool > objects.size()) {
            Logger.getLogger(SpawnGroup.class).warn(
                    "Incorrect pool value for spawn group. MapId:" + mapid + " Npc: " + npcid);
            this.pool = objects.size();
        }

        if (randomWalk != 0) {
            for (SpawnTemplate spawn : objects) {
                spawn.setRandomWalkNr(randomWalk);
            }
        }
    }

    /**
     * @return the mapid
     */
    public int getMapid() {
        return mapid;
    }


    /**
     * @return the npcid
     */
    public int getNpcid() {
        return npcid;
    }

    /**
     * @return the interval
     */
    public int getInterval() {
        return interval;
    }

    /**
     * @return the pool
     */
    public int getPool() {
        return pool;
    }

    /**
     * @return the objects
     */
    public List<SpawnTemplate> getObjects() {
        if (this.objects == null)
            this.objects = new ArrayList<SpawnTemplate>();

        return this.objects;
    }

    /**
     * @return the handler
     */
    public SpawnHandlerType getHandler() {
        return handler;
    }

    /**
     * @return the anchor
     */
    public String getAnchor() {
        return anchor;
    }

    /**
     * @return the dayTime
     */
    public SpawnTime getSpawnTime() {
        return spawnTime;
    }

    /**
     * Returns next template to spawn
     *
     * @param instance
     * @return SpawnTemplate
     */
    public SpawnTemplate getNextAvailableTemplate(int instance) {
        for (int i = 0; i < getObjects().size(); i++) {
            Integer lastSpawnCounter = lastSpawnedTemplate.get(instance);
            int nextSpawnCounter = lastSpawnCounter == null ? 0 : lastSpawnCounter + 1;

            if (nextSpawnCounter >= objects.size())
                nextSpawnCounter = 0;

            SpawnTemplate nextSpawn = objects.get(nextSpawnCounter);
            if (nextSpawn.isSpawned(instance))
                continue;

            lastSpawnedTemplate.put(instance, nextSpawnCounter);
            return nextSpawn;
        }
        return null;
    }

    public int size() {
        return getObjects().size();
    }

    /**
     *
     */
    public SpawnTemplate getNextRandomTemplate() {
        return objects.get(Rnd.get(0, size() - 1));
    }

    /**
     * Last spawn counter will be reseted during instance respawn
     *
     * @param instanceIndex
     */
    public void resetLastSpawnCounter(int instanceIndex) {
        if (lastSpawnedTemplate.containsKey(instanceIndex))
            lastSpawnedTemplate.remove(instanceIndex);
    }

    /**
     * Check whether pool size is equal to number of defined objects
     * For such pools no exchange template should be done
     *
     * @return
     */
    public boolean isFullPool() {
        return pool == objects.size();
    }

    /**
     * @param visibleObject
     */
    public synchronized void exchangeSpawn(VisibleObject visibleObject) {
        if (isFullPool())
            return;

        int instanceId = visibleObject.getInstanceId();
        SpawnTemplate nextSpawn = getNextAvailableTemplate(instanceId);
        if (nextSpawn != null)
            visibleObject.setSpawn(nextSpawn);
    }

    public int getSiegeNpcIdByRace(SiegeRace race) {
        switch (race) {
            case ASMODIANS:
                return siegeNpcId_asmodians;
            case BALAUR:
                return siegeNpcId_balaur;
            case ELYOS:
                return siegeNpcId_elyos;
            default:
                return 0;
        }
    }

    public boolean isBoss() {
        return boss;
    }
}
