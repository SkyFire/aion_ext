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
package gameserver.spawnengine;

import com.aionemu.commons.utils.Rnd;
import gameserver.controllers.RiftController;
import gameserver.controllers.effect.EffectController;
import gameserver.dataholders.DataManager;
import gameserver.model.Race;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.NpcTemplate;
import gameserver.model.templates.spawn.SpawnGroup;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.network.aion.serverpackets.SM_DELETE;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.utils.idfactory.IDFactory;
import gameserver.world.NpcKnownList;
import gameserver.world.World;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author ATracer
 */
public class RiftSpawnManager {

    private static final Logger log = Logger.getLogger(RiftSpawnManager.class);

    private static final ConcurrentLinkedQueue<Npc> rifts = new ConcurrentLinkedQueue<Npc>();


    private static final int RIFT_RESPAWN_DELAY = 100 * 60 * 1000;
    private static final int RIFT_LIFETIME = 26 * 60 * 1000;

    private static final Map<String, SpawnGroup> spawnGroups = new HashMap<String, SpawnGroup>();

    public static void addRiftSpawnGroup(SpawnGroup spawnGroup) {
        spawnGroups.put(spawnGroup.getAnchor(), spawnGroup);
    }

    /**
     *
     */
    public static void startRiftPool() {
        ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                RiftEnum rift1 = RiftEnum.values()[Rnd.get(0, 6)];
                RiftEnum rift2 = RiftEnum.values()[Rnd.get(7, 13)];
                RiftEnum rift3 = RiftEnum.values()[Rnd.get(14, 20)];
                RiftEnum rift4 = RiftEnum.values()[Rnd.get(21, 27)];

                spawnRift(rift1);
                spawnRift(rift2);
                spawnRift(rift3);
                spawnRift(rift4);
            }
        }, 10000, RIFT_RESPAWN_DELAY);
    }

    /**
     * @param rift1
     */
    private static void spawnRift(RiftEnum rift) {
        log.info("Spawning rift : " + rift.name());
        SpawnGroup masterGroup = spawnGroups.get(rift.getMaster());
        SpawnGroup slaveGroup = spawnGroups.get(rift.getSlave());

        if (masterGroup == null || slaveGroup == null)
            return;

        int instanceCount = World.getInstance().getWorldMap(masterGroup.getMapid()).getInstanceCount();

        SpawnTemplate masterTemplate = masterGroup.getNextRandomTemplate();
        SpawnTemplate slaveTemplate = slaveGroup.getNextRandomTemplate();

        for (int i = 1; i <= instanceCount; i++) {
            Npc slave = spawnInstance(i, masterGroup, slaveTemplate, new RiftController(null, rift));
            spawnInstance(i, masterGroup, masterTemplate, new RiftController(slave, rift));
        }
    }

    private static Npc spawnInstance(int instanceIndex, SpawnGroup spawnGroup, SpawnTemplate spawnTemplate, RiftController riftController) {
        NpcTemplate masterObjectTemplate = DataManager.NPC_DATA.getNpcTemplate(spawnGroup.getNpcid());
        Npc npc = new Npc(IDFactory.getInstance().nextId(), riftController,
                spawnTemplate, masterObjectTemplate);

        npc.setKnownlist(new NpcKnownList(npc));
        npc.setEffectController(new EffectController(npc));
        npc.getController().onRespawn();

        World world = World.getInstance();
        world.storeObject(npc);
        world.setPosition(npc, spawnTemplate.getWorldId(), instanceIndex,
                spawnTemplate.getX(), spawnTemplate.getY(), spawnTemplate.getZ(), spawnTemplate.getHeading());
        world.spawn(npc);
        rifts.add(npc);

        scheduleDespawn(npc);
        riftController.sendAnnounce();

        return npc;
    }

    /**
     * @param npc
     */
    private static void scheduleDespawn(final Npc npc) {
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (npc != null && !npc.isSpawned()) {
                    PacketSendUtility.broadcastPacket(npc, new SM_DELETE(npc, 15));
                    npc.getController().onDespawn(true);
                    World.getInstance().despawn(npc);
                }
                rifts.remove(npc);
            }
        }, RIFT_LIFETIME);
    }

    public enum RiftEnum {
        ELTNEN_AM("ELTNEN_AM", "MORHEIM_AS", 12, 28, Race.ASMODIANS),
        ELTNEN_BM("ELTNEN_BM", "MORHEIM_BS", 20, 32, Race.ASMODIANS),
        ELTNEN_CM("ELTNEN_CM", "MORHEIM_CS", 35, 36, Race.ASMODIANS),
        ELTNEN_DM("ELTNEN_DM", "MORHEIM_DS", 35, 37, Race.ASMODIANS),
        ELTNEN_EM("ELTNEN_EM", "MORHEIM_ES", 45, 40, Race.ASMODIANS),
        ELTNEN_FM("ELTNEN_FM", "MORHEIM_FS", 50, 40, Race.ASMODIANS),
        ELTNEN_GM("ELTNEN_GM", "MORHEIM_GS", 50, 45, Race.ASMODIANS),

        HEIRON_AM("HEIRON_AM", "BELUSLAN_AS", 24, 35, Race.ASMODIANS),
        HEIRON_BM("HEIRON_BM", "BELUSLAN_BS", 36, 35, Race.ASMODIANS),
        HEIRON_CM("HEIRON_CM", "BELUSLAN_CS", 48, 46, Race.ASMODIANS),
        HEIRON_DM("HEIRON_DM", "BELUSLAN_DS", 48, 40, Race.ASMODIANS),
        HEIRON_EM("HEIRON_EM", "BELUSLAN_ES", 60, 50, Race.ASMODIANS),
        HEIRON_FM("HEIRON_FM", "BELUSLAN_FS", 60, 55, Race.ASMODIANS),
        HEIRON_GM("HEIRON_GM", "BELUSLAN_GS", 72, 55, Race.ASMODIANS),

        MORHEIM_AM("MORHEIM_AM", "ELTNEN_AS", 12, 28, Race.ELYOS),
        MORHEIM_BM("MORHEIM_BM", "ELTNEN_BS", 20, 32, Race.ELYOS),
        MORHEIM_CM("MORHEIM_CM", "ELTNEN_CS", 35, 36, Race.ELYOS),
        MORHEIM_DM("MORHEIM_DM", "ELTNEN_DS", 35, 37, Race.ELYOS),
        MORHEIM_EM("MORHEIM_EM", "ELTNEN_ES", 45, 40, Race.ELYOS),
        MORHEIM_FM("MORHEIM_FM", "ELTNEN_FS", 50, 40, Race.ELYOS),
        MORHEIM_GM("MORHEIM_GM", "ELTNEN_GS", 50, 45, Race.ELYOS),

        BELUSLAN_AM("BELUSLAN_AM", "HEIRON_AS", 24, 35, Race.ELYOS),
        BELUSLAN_BM("BELUSLAN_BM", "HEIRON_BS", 36, 35, Race.ELYOS),
        BELUSLAN_CM("BELUSLAN_CM", "HEIRON_CS", 48, 46, Race.ELYOS),
        BELUSLAN_DM("BELUSLAN_DM", "HEIRON_DS", 48, 40, Race.ELYOS),
        BELUSLAN_EM("BELUSLAN_EM", "HEIRON_ES", 60, 50, Race.ELYOS),
        BELUSLAN_FM("BELUSLAN_FM", "HEIRON_FS", 60, 55, Race.ELYOS),
        BELUSLAN_GM("BELUSLAN_GM", "HEIRON_GS", 72, 55, Race.ELYOS);

        private String master;
        private String slave;
        private int entries;
        private int maxLevel;
        private Race destination;

        private RiftEnum(String master, String slave, int entries, int maxLevel, Race destination) {
            this.master = master;
            this.slave = slave;
            this.entries = entries;
            this.maxLevel = maxLevel;
            this.destination = destination;
        }

        /**
         * @return the master
         */
        public String getMaster() {
            return master;
        }

        /**
         * @return the slave
         */
        public String getSlave() {
            return slave;
        }

        /**
         * @return the entries
         */
        public int getEntries() {
            return entries;
        }

        /**
         * @return the maxLevel
         */
        public int getMaxLevel() {
            return maxLevel;
        }

        /**
         * @return the destination
         */
        public Race getDestination() {
            return destination;
        }
    }

    /**
     * @param activePlayer
     */
    public static void sendRiftStatus(Player activePlayer) {
        for (Npc rift : rifts) {
            if (rift.getWorldId() == activePlayer.getWorldId()) {
                ((RiftController) rift.getController()).sendMessage(activePlayer);
            }
        }
    }
}
