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
import gameserver.configs.main.SiegeConfig;
import gameserver.dao.SiegeDAO;
import gameserver.dataholders.DataManager;
import gameserver.model.DescriptionId;
import gameserver.model.Race;
import gameserver.model.alliance.PlayerAllianceGroup;
import gameserver.model.alliance.PlayerAllianceMember;
import gameserver.model.gameobjects.*;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.group.PlayerGroup;
import gameserver.model.legion.Legion;
import gameserver.model.siege.*;
import gameserver.model.templates.siege.*;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.network.aion.serverpackets.*;
import gameserver.spawnengine.SpawnEngine;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.Executor;
import gameserver.world.World;
import gameserver.utils.scheduler.Scheduler;
import gameserver.utils.ThreadPoolManager;
import javolution.util.FastList;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sarynth, zdead
 */
public class SiegeService {
    private static Logger log = Logger.getLogger(SiegeService.class);

    private Map<Integer, SiegeLocation> locations;
    private Map<Integer, SiegeSpawnList> spawnLists = new HashMap<Integer, SiegeSpawnList>();

    private List<String> errorReportItems = new ArrayList<String>();

    private Map<Integer, List<Integer>> fortressRelatedObjectIds = new HashMap<Integer, List<Integer>>();

    private long lastCalculationMillis;

    private SiegeService() {
        if (SiegeConfig.SIEGE_ENABLED) {
            log.info("Loading Siege Location Data...");
            // Load Siege Status from Database
            locations = DataManager.SIEGE_LOCATION_DATA.getSiegeLocations();
            DAOManager.getDAO(SiegeDAO.class).loadSiegeLocations(locations);
            log.info("Successfully loaded " + locations.size() + " siege locations");

            // Load Siege Spawns
            log.info("Loading siege spawn data ...");
            for (SiegeLocation loc : locations.values()) {
                // Initialize Fortress Spawn Cache
                if (fortressRelatedObjectIds.containsKey(loc.getLocationId()))
                    fortressRelatedObjectIds.remove(loc.getLocationId());
                fortressRelatedObjectIds.put(loc.getLocationId(), new ArrayList<Integer>());

                // Load Spawnlist
                SiegeSpawnList list = DataManager.SIEGE_SPAWN_DATA.getSpawnsForLocation(loc.getLocationId());
                if (list != null)
                    spawnLists.put(loc.getLocationId(), list);
            }

            log.info("Successfully loaded " + spawnLists.size() + " siege spawnlists");

            // Start spawn
            log.info("Starting spawn of siege locations ....");

            // 1. Spawn Fortresses
            for (SiegeSpawnList sl : spawnLists.values()) {
                if (locations.get(sl.getLocationId()).getSiegeType() == SiegeType.FORTRESS) {
                    if (calculateVulnerable()) {
                        locations.get(sl.getLocationId()).setVulnerable(true);
                        locations.get(sl.getLocationId()).setNextState(0);
                        spawnLocation(sl.getLocationId(), locations.get(sl.getLocationId()).getRace(), "SIEGE");
                    } else {
                        locations.get(sl.getLocationId()).setVulnerable(false);
                        if (calculateVulnerable())
                            locations.get(sl.getLocationId()).setNextState(1);
                        else
                            locations.get(sl.getLocationId()).setNextState(0);

                        spawnLocation(sl.getLocationId(), locations.get(sl.getLocationId()).getRace(), "PEACE");
                    }
                }
            }
            // Start Timers
            lastCalculationMillis = System.currentTimeMillis();
            ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    processVulnerabilityCalculation();
                }
            }, SiegeConfig.SIEGE_TIMER_INTERVAL * 1000, SiegeConfig.SIEGE_TIMER_INTERVAL * 1000);

            // 2. Spawn Artifacts
            for (final SiegeSpawnList sl : spawnLists.values()) {
                if (locations.get(sl.getLocationId()).getSiegeType() == SiegeType.ARTIFACT)
                    spawnLocation(sl.getLocationId(), locations.get(sl.getLocationId()).getRace(), "SIEGE");
            }

            log.info("Successfully completed siege spawning");
            writeLogAbyssStatus();
        } else {
            log.info("Siege Disabled by Config.");
            locations = new FastMap<Integer, SiegeLocation>();
        }
    }

    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder {
        protected static final SiegeService instance = new SiegeService();
    }

    public static final SiegeService getInstance() {
        return SingletonHolder.instance;
    }

    public Map<Integer, SiegeLocation> getSiegeLocations() {
        return locations;
    }

    /**
     * @return siege time
     */
    public int getSiegeTime() {
        long remainingMillis = (getLastCalculationMillis() + (SiegeConfig.SIEGE_TIMER_INTERVAL * 1000)) - System.currentTimeMillis();
        long remainingSeconds = remainingMillis / 1000;
        return (int) remainingSeconds;
    }

    public long getLastCalculationMillis() {
        return lastCalculationMillis;
    }

    private boolean calculateVulnerable() {
        return (Rnd.get(0, 100) < SiegeConfig.SIEGE_VULNERABLE_CHANCE);
    }

    public void processVulnerabilityCalculation() {
        lastCalculationMillis = System.currentTimeMillis();
        log.debug("Processing vulnerability: " + locations.size() + " locations");
        for (final SiegeLocation loc : locations.values()) {
            log.debug("Processing siege #" + loc.getLocationId());
            if (loc.getSiegeType() == SiegeType.FORTRESS) {
                if (loc.isVulnerable()) {
                    loc.setNextState(calculateVulnerable() ? 1 : 0);
                    loc.setVulnerable(false);
                    clearFortress(loc.getLocationId());

                    String legionName = "";

                    if (loc.getLegionId() > 0) {
                        Legion legion = LegionService.getInstance().getLegion(loc.getLegionId());

                        if (legion != null)
                            legionName = legion.getLegionName();
                        else {
                            switch (loc.getRace()) {
                                case ASMODIANS:
                                    legionName = "Asmodians";
                                    break;
                                case BALAUR:
                                    legionName = "Balaurs";
                                    break;
                                case ELYOS:
                                    legionName = "Elyos";
                                    break;
                            }
                        }
                    } else {
                        switch (loc.getRace()) {
                            case ASMODIANS:
                                legionName = "Asmodians";
                                break;
                            case BALAUR:
                                legionName = "Balaurs";
                                break;
                            case ELYOS:
                                legionName = "Elyos";
                                break;
                        }
                    }

                    DAOManager.getDAO(SiegeDAO.class).updateSiegeLocation(loc);

                    DAOManager.getDAO(SiegeDAO.class).insertSiegeLogEntry(legionName, "DEFEND", System.currentTimeMillis() / 1000, loc.getLocationId());

                    ThreadPoolManager.getInstance().schedule(new Runnable() {
                        @Override
                        public void run() {
                            spawnLocation(loc.getLocationId(), loc.getRace(), "PEACE");
                        }
                    }, 5000);
                    broadcastUpdate(loc);
                } else {
                    if (loc.getNextState() == 1) {
                        loc.setNextState(0);
                        loc.setVulnerable(true);
                        clearFortress(loc.getLocationId());
                        ThreadPoolManager.getInstance().schedule(new Runnable() {
                            @Override
                            public void run() {
                                spawnLocation(loc.getLocationId(), loc.getRace(), "SIEGE");
                            }
                        }, 5000);
                        broadcastUpdate(loc);
                    } else {
                        loc.setNextState(calculateVulnerable() ? 1 : 0);
                        loc.setVulnerable(false);
                        clearFortress(loc.getLocationId());
                        ThreadPoolManager.getInstance().schedule(new Runnable() {
                            @Override
                            public void run() {
                                spawnLocation(loc.getLocationId(), loc.getRace(), "PEACE");
                            }
                        }, 5000);
                        broadcastUpdate(loc);
                    }
                }
            }
        }
    }

    public void clearArtifact(Artifact artifact) {
        int artifactId = artifact.getLocationId();
        for (Integer creature : artifact.getRelatedSpawnIds()) {
            AionObject obj = World.getInstance().findAionObject(creature);
            if (obj instanceof Creature) {
                Creature c = (Creature) obj;
                if (c.isSpawned())
                    World.getInstance().despawn(c);
                DataManager.SPAWNS_DATA.removeSpawn(c.getSpawn());
                c.setAi(null);
                c.getController().delete();
            }
        }
        DataManager.SPAWNS_DATA.removeSpawn(artifact.getSpawn());
        artifact.getController().delete();
        log.info("Cleaned ARTIFACT " + artifactId);
    }

    public void clearFortress(int fortressId) {
        List<Integer> spawnedCreatures = fortressRelatedObjectIds.get(fortressId);
        if (spawnedCreatures != null) {
            for (Integer creature : spawnedCreatures) {
                AionObject obj = World.getInstance().findAionObject(creature);
                if (obj != null && obj instanceof Creature) {
                    Creature c = (Creature) obj;
                    if (c.isSpawned())
                        World.getInstance().despawn(c);
                    DataManager.SPAWNS_DATA.removeSpawn(c.getSpawn());
                    c.setAi(null);
                    c.getController().delete();
                }
            }
        }
        fortressRelatedObjectIds.get(fortressId).clear();
        log.info("Cleaned FORTRESS " + fortressId);
    }

    public SiegeLocation getSiegeLocation(int locationId) {
        return locations.get(locationId);
    }

    public void capture(int locationId, SiegeRace race) {
        this.capture(locationId, race, 0);
    }

    public void capture(int locationId, SiegeRace race, int legionId) {
        SiegeLocation sLoc = locations.get(locationId);
        sLoc.setRace(race);
        String legionName = "";
        if (legionId > 0) {
            Legion legion = LegionService.getInstance().getLegion(legionId);

            if (legion != null) {
                sLoc.setLegionId(legion.getLegionId());
                legionName = legion.getLegionName();
            } else {
                sLoc.setLegionId(0);
                if (race == SiegeRace.ASMODIANS)
                    legionName = "Asmodians";
                else if (race == SiegeRace.BALAUR)
                    legionName = "Balaurs";
                else
                    legionName = "Elyos";
            }
        } else {
            sLoc.setLegionId(0);
            if (race == SiegeRace.ASMODIANS)
                legionName = "Asmodians";
            else if (race == SiegeRace.BALAUR)
                legionName = "Balaurs";
            else
                legionName = "Elyos";
        }

        if (sLoc.getSiegeType() == SiegeType.FORTRESS)
            sLoc.setVulnerable(false);

        DAOManager.getDAO(SiegeDAO.class).updateSiegeLocation(sLoc);

        DAOManager.getDAO(SiegeDAO.class).insertSiegeLogEntry(legionName, "CAPTURE", System.currentTimeMillis() / 1000, locationId);

        broadcastUpdate(sLoc);
        Influence.getInstance().recalculateInfluence();
    }

    public void broadcastUpdate(SiegeLocation loc) {
        SM_SIEGE_LOCATION_INFO pkt = new SM_SIEGE_LOCATION_INFO(loc);
        broadcast(pkt);
    }

    public void broadcastUpdate() {
        SM_SIEGE_LOCATION_INFO pkt = new SM_SIEGE_LOCATION_INFO();
        broadcast(pkt);
    }

    private void broadcast(final SM_SIEGE_LOCATION_INFO pkt) {
        World.getInstance().doOnAllPlayers(new Executor<Player>() {
            @Override
            public boolean run(Player player) {
                PacketSendUtility.sendPacket(player, pkt);
                PacketSendUtility.sendPacket(player, new SM_INFLUENCE_RATIO());
                return true;
            }
        });
    }

    private boolean spawnLocation(int locationId, SiegeRace spawnRace, String spawnType) {
        /*
           * String spawnType: SIEGE|PEACE|REINFORCEMENT
           */

        SiegeSpawnList list = spawnLists.get(locationId);
        SiegeLocation location = locations.get(locationId);

        if (list == null || location == null)
            return false;

        List<SiegeGuardTemplate> guards;
        if (spawnType.equals("SIEGE"))
            guards = list.getGuards().getSiegeGuards();
        else if (spawnType.equals("PEACE"))
            guards = list.getGuards().getPeaceGuards();
        else if (spawnType.equals("REINFORCEMENT"))
            guards = list.getGuards().getReinforcementsGuards();
        else
            return false;

        if (guards == null) {
            log.error("Cannot spawn a siege location without any guard. Aborting location " + locationId);
            return false;
        }

        if (location.getSiegeType() == SiegeType.ARTIFACT) {
            ArtifactTemplate artifactTemplate = list.getArtifactTemplate();

            if (artifactTemplate == null) {
                log.error("Cannot spawn an ARTIFACT location without an artifact XML definition.");
                return false;
            }

            if (artifactTemplate.getBaseInfo() == null) {
                log.error("Cannot spawn an ARTIFACT with no baseInfo definition !");
                return false;
            }

            if (artifactTemplate.getEffectTemplate() == null) {
                log.error("Cannot spawn an ARTIFACT with no related effect !");
                return false;
            }

            if (artifactTemplate.getProtectorTemplate() == null) {
                log.error("Cannot spawn an ARTIFACT with no protector !");
                return false;
            }

            int spawnedCounter = 0;

            // Everything is fine, let's go ! :)
            // 1. Spawn Artifact + Protector
            Artifact artifact = SpawnEngine.getInstance().spawnArtifact(locationId, spawnRace, artifactTemplate);
            spawnedCounter += 2;

            // 2. Spawn Guards
            for (SiegeGuardTemplate guardTemplate : guards) {
                int npcId = guardTemplate.getNpcId(spawnRace);
                if (npcId == 0)
                    continue;
                for (SiegeSpawnLocationTemplate spawnLoc : guardTemplate.getSpawnLocations()) {
                    SpawnTemplate gST = SpawnEngine.getInstance().addNewSpawn(location.getLocationTemplate().getWorldId(), 1, npcId, spawnLoc.getX(), spawnLoc.getY(), spawnLoc.getZ(), (byte) spawnLoc.getH(), 0, 0, true, true);
                    VisibleObject result = SpawnEngine.getInstance().spawnObject(gST, 1);
                    if (result != null) {
                        artifact.registerRelatedSpawn(result.getObjectId());
                        spawnedCounter++;
                    }
                }
            }

            log.info("Spawned ARTIFACT " + locationId + ": " + spawnedCounter);
            return true;

        } else if (location.getSiegeType() == SiegeType.FORTRESS) {
            int spawnedCounter = 0;

            if (spawnType.equals("PEACE")) {
                // 1. Guards
                for (SiegeGuardTemplate guardTemplate : guards) {
                    int npcId = guardTemplate.getNpcId(spawnRace);
                    if (npcId == 0)
                        continue;
                    for (SiegeSpawnLocationTemplate spawnLoc : guardTemplate.getSpawnLocations()) {
                        SpawnTemplate gST = SpawnEngine.getInstance().addNewSpawn(location.getLocationTemplate().getWorldId(), 1, npcId, spawnLoc.getX(), spawnLoc.getY(), spawnLoc.getZ(), (byte) spawnLoc.getH(), 0, 0, true);
                        VisibleObject result = SpawnEngine.getInstance().spawnObject(gST, 1);
                        if (result != null) {
                            if (!fortressRelatedObjectIds.get(locationId).contains(result.getObjectId()))
                                fortressRelatedObjectIds.get(locationId).add(result.getObjectId());
                            spawnedCounter++;
                        }
                    }

                    if (list.getArtifactTemplate() != null) {
                        ArtifactTemplate artifactTemplate = list.getArtifactTemplate();
                        if (artifactTemplate.getBaseInfo() == null) {
                            log.error("Cannot spawn an ARTIFACT with no baseInfo definition !");
                            return false;
                        }

                        if (artifactTemplate.getEffectTemplate() == null) {
                            log.error("Cannot spawn an ARTIFACT with no related effect !");
                            return false;
                        }

                        Artifact fArtifact = SpawnEngine.getInstance().spawnArtifact(locationId, spawnRace, artifactTemplate);

                        if (fArtifact != null) {
                            fortressRelatedObjectIds.get(locationId).add(fArtifact.getObjectId());
                            spawnedCounter++;
                        }

                    }

                }

                // 2. Instance Portal
                // Spawned only if fortress is controlled by asmo or elyos, on invulnerable state.
                if (spawnRace.getRaceId() != 2) {
                    if (list.getInstancePortalTemplate() != null && list.getInstancePortalTemplate().getBaseInfo().getNpcId(spawnRace) != 0) {
                        InstancePortal portal = SpawnEngine.getInstance().spawnInstancePortal(locationId, spawnRace, list.getInstancePortalTemplate());
                        if (portal != null) {
                            if (!fortressRelatedObjectIds.get(locationId).contains(portal.getObjectId()))
                                fortressRelatedObjectIds.get(locationId).add(portal.getObjectId());
                            spawnedCounter++;
                        }
                    } else
                        log.error("No Instance portal defined for FORTRESS " + locationId);
                }
            } else if (spawnType.equals("REINFORCEMENT")) {
                // Not implemented yet
            } else if (spawnType.equals("SIEGE")) {
                // SIEGE !!
                // 1. Guards
                for (SiegeGuardTemplate guardTemplate : guards) {
                    int npcId = guardTemplate.getNpcId(spawnRace);
                    if (npcId == 0)
                        continue;
                    for (SiegeSpawnLocationTemplate spawnLoc : guardTemplate.getSpawnLocations()) {
                        SpawnTemplate gST = SpawnEngine.getInstance().addNewSpawn(location.getLocationTemplate().getWorldId(), 1, npcId, spawnLoc.getX(), spawnLoc.getY(), spawnLoc.getZ(), (byte) spawnLoc.getH(), 0, 0, false);
                        VisibleObject result = SpawnEngine.getInstance().spawnObject(gST, 1);
                        if (result != null) {
                            if (!fortressRelatedObjectIds.get(locationId).contains(result.getObjectId()))
                                fortressRelatedObjectIds.get(locationId).add(result.getObjectId());
                            spawnedCounter++;
                        }
                    }
                }

                // 2. Fortress Guardian Deity
                if (list.getFortressGeneralTemplate() == null) {
                    log.error("No Fortress General defined for FORTRESS " + locationId);
                    return false;
                }
                FortressGeneral general = SpawnEngine.getInstance().spawnFortressGeneral(locationId, spawnRace, list.getFortressGeneralTemplate());
                if (general != null) {
                    if (!fortressRelatedObjectIds.get(locationId).contains(general.getObjectId()))
                        fortressRelatedObjectIds.get(locationId).add(general.getObjectId());
                    spawnedCounter++;
                }

                // 3. Fortress Gates
                List<FortressGateTemplate> fortressGatesTemplates = list.getFortressGatesTemplates();
                if (fortressGatesTemplates != null && fortressGatesTemplates.size() > 0) {
                    for (FortressGateTemplate fgTemplate : fortressGatesTemplates) {
                        FortressGate gate = SpawnEngine.getInstance().spawnFortressGate(locationId, spawnRace, fgTemplate);
                        if (gate != null) {
                            spawnedCounter++;
                            fortressRelatedObjectIds.get(locationId).add(gate.getObjectId());
                            if(fgTemplate.getArtifact() != null) {
                            	FortressGateArtifactTemplate aTemplate = fgTemplate.getArtifact();
                            	FortressGateArtifact fga = SpawnEngine.getInstance().spawnFortressGateArtifact(locationId, spawnRace, aTemplate);
                            	if (fga != null) {
                            		fga.setRelatedGate(gate);
                            		spawnedCounter++;
                            		fortressRelatedObjectIds.get(locationId).add(fga.getObjectId());
                            	}
                            }
                        }
                    }
                }

                // 4. Fortress Artifact (if present)
                if (list.getArtifactTemplate() != null) {
                    ArtifactTemplate artifactTemplate = list.getArtifactTemplate();
                    if (artifactTemplate.getBaseInfo() == null) {
                        log.error("Cannot spawn an ARTIFACT with no baseInfo definition !");
                        return false;
                    }

                    if (artifactTemplate.getEffectTemplate() == null) {
                        log.error("Cannot spawn an ARTIFACT with no related effect !");
                        return false;
                    }

                    Artifact fArtifact = SpawnEngine.getInstance().spawnArtifact(locationId, spawnRace, artifactTemplate);

                    if (fArtifact != null) {
                        fortressRelatedObjectIds.get(locationId).add(fArtifact.getObjectId());
                        spawnedCounter++;
                    }

                }

                // 5. Aetheric Field
                if (list.getAethericFieldTemplate() != null) {
                    AethericFieldTemplate afTemplate = list.getAethericFieldTemplate();
                    if (afTemplate.getGeneratorTemplate() == null || afTemplate.getShieldTemplate() == null) {
                        log.error("Missing definition informations for Aetheric Field");
                    } else {
                        AethericField field = SpawnEngine.getInstance().spawnAethericField(locationId, spawnRace, afTemplate);
                        if (field.getGenerator() != null && field.getShield() != null) {
                            spawnedCounter += 2;
                            fortressRelatedObjectIds.get(locationId).add(field.getGenerator().getObjectId());
                            fortressRelatedObjectIds.get(locationId).add(field.getShield().getObjectId());
                        }
                    }
                }

                log.info("Spawned FORTRESS/SIEGE " + locationId + ": " + spawnedCounter);
                return true;

            } else {
                log.error("Unknown spawnType: " + spawnType);
                return false;
            }
        } else {
            log.error("Cannot spawn siege type " + location.getSiegeType().name() + ": no such handler");
            return false;
        }

        return true;

    }

    public void onArtifactCaptured(Artifact artifact) {
        final int artifactId = artifact.getLocationId();
        SiegeService.getInstance().capture(artifact.getLocationId(), SiegeRace.BALAUR);
        clearArtifact(artifact);
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                spawnLocation(artifactId, SiegeRace.BALAUR, "SIEGE");
            }
        }, 5000);
    }

    public void onArtifactCaptured(Artifact artifact, Player taker) {
        final int artifactId = artifact.getLocationId();
        SiegeRace newRace = null;
        if (taker.getCommonData().getRace() == Race.ASMODIANS)
            newRace = SiegeRace.ASMODIANS;
        else if (taker.getCommonData().getRace() == Race.ELYOS)
            newRace = SiegeRace.ELYOS;

        if (newRace == null)
            return;

        final SiegeRace nr = newRace;

        int legionId = 0;
        if (taker.getLegion() != null)
            legionId = taker.getLegion().getLegionId();

        SiegeService.getInstance().capture(artifact.getLocationId(), newRace, legionId);
        clearArtifact(artifact);

        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                spawnLocation(artifactId, nr, "SIEGE");
            }
        }, 5000);
    }

    public void onFortressCaptured(final FortressGeneral general, Creature lastAttacker, FastList<PlayerGroup> rewardGroups, FastList<PlayerAllianceGroup> rewardAlliances) {
        // Taken by players = ASMODIANS OR ELYOS
        if (lastAttacker instanceof Player || lastAttacker instanceof Summon || lastAttacker instanceof Trap) {
            Player la = null;
            ArrayList<Player> players = new ArrayList<Player>();
            final SiegeRace newRace;
            int legionId = 0;

            if (lastAttacker instanceof Player) {
                la = (Player) lastAttacker;
            } else if (lastAttacker instanceof Summon) {
                la = (Player) (lastAttacker.getMaster());
            } else if (lastAttacker instanceof Trap) {
                la = (Player) (((Trap) lastAttacker).getCreator());
            } else {
                // something wrong
                return;
            }

            if (la == null)
                return;

            if (la.getLegion() != null) {
                legionId = la.getLegion().getLegionId();
            }

            for (PlayerGroup gr : rewardGroups) {
                for (Player p : gr.getMembers()) {
                    players.add(p);
                }
            }
            for (PlayerAllianceGroup agr : rewardAlliances) {
                for (PlayerAllianceMember m : agr.getMembers()) {
                    players.add(m.getPlayer());
                }
            }

            if (!players.contains(la)) {
                players.add(la);
            }

            switch (la.getCommonData().getRace()) {
                case ASMODIANS:
                    newRace = SiegeRace.ASMODIANS;
                    break;
                case ELYOS:
                    newRace = SiegeRace.ELYOS;
                    break;
                default:
                    newRace = SiegeRace.BALAUR;
                    break;
            }

            // Do Reward
            SiegeRewardTemplate rewardTemplate = locations.get(general.getFortressId()).getLocationTemplate().getSiegeRewards().get(0);
            for (Player pl : players) {
                ItemService.addItem(pl, rewardTemplate.getItemId(), rewardTemplate.getItemCount());
                if ( general.getFortressId() == 1011 ) {
                    pl.getCommonData().addAp(SiegeConfig.SIEGE_AP_REWARD_DIVINE);
                } else {
                    pl.getCommonData().addAp(SiegeConfig.SIEGE_AP_REWARD_DEFAULT);
                }
            }

            capture(general.getFortressId(), newRace, legionId);

            clearFortress(general.getFortressId());

            ThreadPoolManager.getInstance().schedule(new Runnable() {
                @Override 
                public void run() {
                    spawnLocation(general.getFortressId(), newRace, "PEACE");
                }
            }, 3500);
        }
        // Taken by Balaur
        else {
            capture(general.getFortressId(), SiegeRace.BALAUR);
            clearFortress(general.getFortressId());

            ThreadPoolManager.getInstance().schedule(new Runnable() {
                @Override 
                public void run() {
                    spawnLocation(general.getFortressId(), SiegeRace.BALAUR, "PEACE");
                }
            }, 3500);
        }
    }

    /**
     * Alerts a player about currently vulnerable fortresses
     *
     * @param p
     */
    public void alertVulnerable(Player p) {
        for (SiegeLocation loc : locations.values()) {
            if (loc.getSiegeType() == SiegeType.FORTRESS && loc.isVulnerable()) {
                PacketSendUtility.sendPacket(p, new SM_SYSTEM_MESSAGE(1301040, getFortressName(loc.getLocationId())));
            }
        }
    }

    private DescriptionId getFortressName(int fortressId) {
        switch (fortressId) {
            case 1011:
                return new DescriptionId(400910 * 2 + 1);
            case 1131:
                return new DescriptionId(400911 * 2 + 1);
            case 1132:
                return new DescriptionId(400912 * 2 + 1);
            case 1141:
                return new DescriptionId(400913 * 2 + 1);
            case 1211:
                return new DescriptionId(400914 * 2 + 1);
            case 1221:
                return new DescriptionId(400915 * 2 + 1);
            case 1231:
                return new DescriptionId(400916 * 2 + 1);
            case 1241:
                return new DescriptionId(400917 * 2 + 1);
            case 1251:
                return new DescriptionId(400918 * 2 + 1);
            case 2011:
                return new DescriptionId(401288 * 2 + 1);
            case 2021:
                return new DescriptionId(401290 * 2 + 1);
            case 3011:
                return new DescriptionId(401292 * 2 + 1);
            case 3021:
                return new DescriptionId(401294 * 2 + 1);
        }
        return new DescriptionId(0);
    }

    private void portEnemiesToBindPoint(Creature lastAttacker, final ArrayList<Player> players) {
        final ArrayList<Player> toBePorted = new ArrayList<Player>();
        if (players.size() > 0) {
            for (Player pl1 : players) {
                if (pl1.getKnownList() != null) {
                    pl1.getKnownList().doOnAllPlayers(new Executor<Player>() {
                        @Override
                        public boolean run(Player pl2) {
                            if (pl2.isEnemyPlayer(players.get(0))) {
                                toBePorted.add(pl2);
                            }
                            return true;
                        }
                    }, true);
                }
            }
        } else {
            if (!(lastAttacker instanceof Player)) {
                lastAttacker.getKnownList().doOnAllPlayers(new Executor<Player>() {
                    @Override
                    public boolean run(Player p) {
                        if (p.getCommonData().getRace() == Race.ELYOS || p.getCommonData().getRace() == Race.ASMODIANS)
                            toBePorted.add(p);
                        return true;
                    }
                }, true);
            } else {
                final Player lAtkr = (Player) lastAttacker;
                lAtkr.getKnownList().doOnAllPlayers(new Executor<Player>() {
                    @Override
                    public boolean run(Player p) {
                        if (p.isEnemyPlayer(lAtkr))
                            toBePorted.add(p);
                        return true;
                    }
                }, true);
            }
        }

        for (Player pl : toBePorted) {
            TeleportService.moveToBindLocation(pl, true);
        }

    }

    public void onPlayerLogin(final Player player) {
        alertVulnerable(player);
        PacketSendUtility.sendPacket(player, new SM_ABYSS_ARTIFACT_INFO(locations.values()));
        PacketSendUtility.sendPacket(player, new SM_ABYSS_ARTIFACT_INFO2(locations.values()));
        PacketSendUtility.sendPacket(player, new SM_ABYSS_ARTIFACT_INFO3(locations.values()));
        PacketSendUtility.sendPacket(player, new SM_FORTRESS_STATUS());

        for (final SiegeLocation loc : locations.values()) {
            if (loc.getSiegeType() == SiegeType.FORTRESS) {
                PacketSendUtility.sendPacket(player, new SM_FORTRESS_INFO(loc.getLocationId(), 0));
                ThreadPoolManager.getInstance().schedule(new Runnable() {

                    @Override
                    public void run() {
                        PacketSendUtility.sendPacket(player, new SM_FORTRESS_INFO(loc.getLocationId(), 1));
                    }
                }, 1500);
            }
        }

    }

    private void writeLogAbyssStatus() {
        log.info("===== ABYSS STATUS ======");
        int counterVul = 0;
        int counterInvul = 0;
        for (SiegeLocation loc : locations.values()) {
            if (loc.isVulnerable())
                counterVul++;
            else
                counterInvul++;
        }
        log.info("Vulnerable Fortresses: " + counterVul);
        log.info("Invulnerable Fortresses: " + counterInvul);
    }

    public void registerError(String errorMessage) {
        errorReportItems.add(errorMessage);
    }

    private void flushErrorReport() {
        errorReportItems.clear();
    }

    public boolean writeReport() {
        try {
            FileWriter writer = new FileWriter("fortress-report.txt");
            BufferedWriter out = new BufferedWriter(writer);
            out.write("=FortressErrorReport[Items:" + errorReportItems.size() + "]=\n");
            for (String m : errorReportItems) {
                out.write(m + "\n");
            }
            out.close();
            flushErrorReport();
            return true;
        }
        catch (Exception e) {
            log.error("Cannot write fortress error report", e);
            return false;
        }
    }

    public static SiegeRace getSiegeRaceFromRace(Race race) {
        switch (race) {
            case ELYOS:
                return SiegeRace.ELYOS;
            case ASMODIANS:
                return SiegeRace.ASMODIANS;
        }
        return null;
    }
}
