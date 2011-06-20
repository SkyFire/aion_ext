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

import gameserver.configs.main.CustomConfig;
import gameserver.dataholders.DataManager;
import gameserver.dataholders.PlayerInitialData.LocationData;
import gameserver.model.EmotionType;
import gameserver.model.Race;
import gameserver.model.gameobjects.Kisk;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.Storage;
import gameserver.model.gameobjects.state.CreatureState;
import gameserver.model.templates.BindPointTemplate;
import gameserver.model.templates.portal.ExitPoint;
import gameserver.model.templates.portal.PortalTemplate;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.model.templates.teleport.TelelocationTemplate;
import gameserver.model.templates.teleport.TeleportLocation;
import gameserver.model.templates.teleport.TeleporterTemplate;
import gameserver.network.aion.serverpackets.*;
import gameserver.network.aion.serverpacketseq.SEQ_SM_WINDSTREAM_ANNOUNCE;
import gameserver.services.ZoneService.ZoneUpdateMode;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.World;
import gameserver.world.WorldMapType;
import org.apache.log4j.Logger;

/**
 * @author ATracer , orz, Simple
 */
public class TeleportService
{
    private static final Logger	log						= Logger.getLogger(TeleportService.class);

    private static final int	TELEPORT_DEFAULT_DELAY	= 2200;

    private static final World	world					= World.getInstance();

    /**
     * Schedules teleport animation
     * 
     * @param activePlayer
     * @param mapid
     * @param x
     * @param y
     * @param z
     */
    public static void scheduleTeleportTask(final Player activePlayer, final int mapid, final float x, final float y,
        final float z)
    {
        teleportTo(activePlayer, mapid, x, y, z, TELEPORT_DEFAULT_DELAY);
    }

    /**
     * Performs flight teleportation
     * 
     * @param template
     * @param locId
     * @param player
     */
    public static void flightTeleport(TeleporterTemplate template, int locId, Player player)
    {
        if(template.getTeleLocIdData() == null)
        {
            log.info(String.format("Missing locId for this teleporter at teleporter_templates.xml with locId: %d",
                locId));
            PacketSendUtility.sendMessage(player,
                "Missing locId for this teleporter at teleporter_templates.xml with locId: " + locId);
            return;
        }

        TeleportLocation location = template.getTeleLocIdData().getTeleportLocation(locId);
        if(location == null)
        {
            log.info(String.format("Missing locId for this teleporter at teleporter_templates.xml with locId: %d",
                locId));
            PacketSendUtility.sendMessage(player,
                "Missing locId for this teleporter at teleporter_templates.xml with locId: " + locId);
            return;
        }

        TelelocationTemplate locationTemplate = DataManager.TELELOCATION_DATA.getTelelocationTemplate(locId);
        if(locationTemplate == null)
        {
            log.info(String.format("Missing info at teleport_location.xml with locId: %d", locId));
            PacketSendUtility.sendMessage(player, "Missing info at teleport_location.xml with locId: " + locId);
            return;
        }

        if(!checkKinahForTransportation(location, player))
            return;

        if(player.getToyPet() != null)
            ToyPetService.getInstance().dismissPet(player, player.getToyPet().getPetId());

        player.setState(CreatureState.FLIGHT_TELEPORT);
        player.unsetState(CreatureState.ACTIVE);
        player.setFlightTeleportId(location.getTeleportId());
        PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_FLYTELEPORT, location
            .getTeleportId(), 0), true);
    }

    /**
     * Performs regular teleportation
     * 
     * @param template
     * @param locId
     * @param player
     */
    public static void regularTeleport(TeleporterTemplate template, int locId, Player player)
    {
        if(template.getTeleLocIdData() == null)
        {
            log.info(String.format("Missing locId for this teleporter at teleporter_templates.xml with locId: %d",
                locId));
            PacketSendUtility.sendMessage(player,
                "Missing locId for this teleporter at teleporter_templates.xml with locId: " + locId);
            return;
        }

        TeleportLocation location = template.getTeleLocIdData().getTeleportLocation(locId);
        if(location == null)
        {
            log.info(String.format("Missing locId for this teleporter at teleporter_templates.xml with locId: %d",
                locId));
            PacketSendUtility.sendMessage(player,
                "Missing locId for this teleporter at teleporter_templates.xml with locId: " + locId);
            return;
        }

        TelelocationTemplate locationTemplate = DataManager.TELELOCATION_DATA.getTelelocationTemplate(locId);
        if(locationTemplate == null)
        {
            log.info(String.format("Missing info at teleport_location.xml with locId: %d", locId));
            PacketSendUtility.sendMessage(player, "Missing info at teleport_location.xml with locId: " + locId);
            return;
        }

        if(!checkKinahForTransportation(location, player))
            return;

        if(player.getToyPet() != null)
            ToyPetService.getInstance().dismissPet(player, player.getToyPet().getPetId());

        PacketSendUtility.sendPacket(player, new SM_TELEPORT_LOC(locationTemplate.getMapId(), locationTemplate.getX(),
            locationTemplate.getY(), locationTemplate.getZ()));
        scheduleTeleportTask(player, locationTemplate.getMapId(), locationTemplate.getX(), locationTemplate.getY(),
            locationTemplate.getZ());
    }

    /**
     * Check kinah in inventory for teleportation
     * 
     * @param location
     * @param player
     * @return
     */
    private static boolean checkKinahForTransportation(TeleportLocation location, Player player)
    {
        Storage inventory = player.getInventory();

        int basePrice = (int) (location.getPrice() * CustomConfig.TRANSPORT_COST_MULTIPLIER);
        long transportationPrice = player.getPrices().getPriceForService(basePrice, player.getCommonData().getRace());

        if(!inventory.decreaseKinah(transportationPrice))
        {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.NOT_ENOUGH_KINAH(transportationPrice));
            return false;
        }
        return true;
    }

    /**
     * @param player
     * @param targetObjectId
     */
    public static void showMap(Player player, int targetObjectId, int npcId)
    {
        if(player.isInState(CreatureState.FLYING))
        {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_USE_AIRPORT_WHEN_FLYING);
            return;
        }

        Npc object = (Npc) world.findAionObject(targetObjectId);
        Race npcRace = object.getObjectTemplate().getRace();
        if(npcRace != null && npcRace != player.getCommonData().getRace())
        {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_MOVE_TO_AIRPORT_WRONG_NPC);// TODO retail
            // message
            return;
        }

        PacketSendUtility.sendPacket(player, new SM_TELEPORT_MAP(player, targetObjectId, getTeleporterTemplate(npcId)));
    }

    /**
     * Teleport Creature to the location using current heading and instanceId
     * 
     * @param worldId
     * @param x
     * @param y
     * @param z
     * @param delay
     * @return true or false
     */
    public static boolean teleportTo(Player player, int worldId, float x, float y, float z, int delay)
    {
        int instanceId = 1;
        if(player.getWorldId() == worldId)
        {
            instanceId = player.getInstanceId();
        }
        return teleportTo(player, worldId, instanceId, x, y, z, delay);
    }

    /**
     * @param worldId
     * @param instanceId
     * @param x
     * @param y
     * @param z
     * @param delay
     * @return true or false
     */
    public static boolean teleportTo(Player player, int worldId, int instanceId, float x, float y, float z, int delay)
    {
        return teleportTo(player, worldId, instanceId, x, y, z, player.getHeading(), delay);
    }

    /**
     * @param player
     * @param worldId
     * @param instanceId
     * @param x
     * @param y
     * @param z
     * @param heading
     * @param delay
     * @return
     */
    public static boolean teleportTo(final Player player, final int worldId, final int instanceId, final float x,
        final float y, final float z, final byte heading, final int delay)
    {
        boolean dead = player.getLifeStats().isAlreadyDead();
        if(dead || !player.isSpawned())
            return false;

        if(DuelService.getInstance().isDueling(player.getObjectId()))
            DuelService.getInstance().loseDuel(player);

        if(player.getToyPet() != null)
            ToyPetService.getInstance().dismissPet(player, player.getToyPet().getPetId());

        if(delay == 0)
        {
            changePosition(player, worldId, instanceId, x, y, z, heading);
            return true;
        }

        PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0, 0, delay, 0, 0));
        ThreadPoolManager.getInstance().schedule(new Runnable(){
            @Override
            public void run()
            {
                if(player.getLifeStats().isAlreadyDead() || !player.isSpawned())
                    return;

                PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(0, 0, 0, 0, 1, 0));
                changePosition(player, worldId, instanceId, x, y, z, heading);
            }
        }, delay);

        return true;
    }

    /**
     * @param worldId
     * @param instanceId
     * @param x
     * @param y
     * @param z
     * @param heading
     */
    private static void changePosition(Player player, int worldId, int instanceId, float x, float y, float z,
        byte heading)
    {
        player.getFlyController().endFly();

        world.despawn(player);

        int currentWorldId = player.getWorldId();
        world.setPosition(player, worldId, instanceId, x, y, z, heading);

        /**
         * instant teleport when map is the same
         */
        if(currentWorldId == worldId)
        {
            player.getKnownList().clear();
            PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
            PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, false));
            world.spawn(player);
            player.getEffectController().updatePlayerEffectIcons();
            player.getController().addZoneUpdateMask(ZoneUpdateMode.ZONE_REFRESH);
        }
        /**
         * teleport with full map reloading
         */
        else
        {
            player.getController().startProtectionActiveTask();
            PacketSendUtility.sendPacket(player, new SM_CHANNEL_INFO(player.getPosition()));
            PacketSendUtility.sendPacket(player, new SM_PLAYER_SPAWN(player));

            SEQ_SM_WINDSTREAM_ANNOUNCE seq_sm_windstream_announce = new SEQ_SM_WINDSTREAM_ANNOUNCE(worldId);
            if(seq_sm_windstream_announce.getStatus())
                PacketSendUtility.sendPacketSeq(player, seq_sm_windstream_announce);
        }
        player.unsetState(CreatureState.WALKING);
        player.getController().startProtectionActiveTask();

    }

    /**
     * @return the teleporterData
     */
    public static TeleporterTemplate getTeleporterTemplate(int npcId)
    {
        return DataManager.TELEPORTER_DATA.getTeleporterTemplate(npcId);
    }

    /**
     * @return the bindPointData
     */
    public static BindPointTemplate getBindPointTemplate2(int bindPointId)
    {
        return DataManager.BIND_POINT_DATA.getBindPointTemplate2(bindPointId);
    }

    /**
     * @param channel
     */
    public static void changeChannel(Player player, int channel)
    {
        if(player.getToyPet() != null)
            ToyPetService.getInstance().dismissPet(player, player.getToyPet().getPetId());
        world.despawn(player);
        world.setPosition(player, player.getWorldId(), channel + 1, player.getX(), player.getY(), player.getZ(), player
            .getHeading());
        player.getController().startProtectionActiveTask();
        PacketSendUtility.sendPacket(player, new SM_CHANNEL_INFO(player.getPosition()));
        PacketSendUtility.sendPacket(player, new SM_PLAYER_SPAWN(player));
    }

    /**
     * This method will move a player to their bind location with 0 delay
     * 
     * @param player
     * @param useTeleport
     */
    public static void moveToBindLocation(Player player, boolean useTeleport)
    {
        if(!player.isInDredgion())
            moveToBindLocation(player, useTeleport, 0);
        else
        {
            if(player.getCommonData().getRace() == Race.ASMODIANS)
                TeleportService.teleportTo(player, player.getWorldId(), player.getInstanceId(), 414, 193, 431, 0);
            else
                TeleportService.teleportTo(player, player.getWorldId(), player.getInstanceId(), 558, 190, 432, 0);
        }

    }

    /**
     * This method will move a player to their bind location
     * 
     * @param player
     * @param useTeleport
     * @param delay
     */
    public static void moveToBindLocation(Player player, boolean useTeleport, int delay)
    {
        float x, y, z;
        int worldId;

        int bindPointId = player.getCommonData().getBindPoint();

        if(bindPointId != 0)
        {
            BindPointTemplate bplist = getBindPointTemplate2(bindPointId);
            worldId = bplist.getZoneId();
            x = bplist.getX();
            y = bplist.getY();
            z = bplist.getZ();
        }
        else
        {
            LocationData locationData = DataManager.PLAYER_INITIAL_DATA.getSpawnLocation(player.getCommonData()
                .getRace());
            worldId = locationData.getMapId();
            x = locationData.getX();
            y = locationData.getY();
            z = locationData.getZ();
        }

        if(useTeleport)
        {
            teleportTo(player, worldId, x, y, z, delay);
        }
        else
        {
            if(player.getToyPet() != null)
                ToyPetService.getInstance().dismissPet(player, player.getToyPet().getPetId());
            world.setPosition(player, worldId, 1, x, y, z, player.getHeading());
        }
    }

    /**
     * This method will send the set bind point packet
     * 
     * @param player
     */
    public static void sendSetBindPoint(Player player)
    {
        int worldId;
        float x, y, z;
        if(player.getCommonData().getBindPoint() != 0)
        {
            BindPointTemplate bplist = DataManager.BIND_POINT_DATA.getBindPointTemplate2(player.getCommonData()
                .getBindPoint());
            if (bplist == null)
                return;

            worldId = bplist.getZoneId();
            x = bplist.getX();
            y = bplist.getY();
            z = bplist.getZ();
        }
        else
        {
            LocationData locationData = DataManager.PLAYER_INITIAL_DATA.getSpawnLocation(player.getCommonData()
                .getRace());
            if (locationData == null)
                return;

            worldId = locationData.getMapId();
            x = locationData.getX();
            y = locationData.getY();
            z = locationData.getZ();
        }
        PacketSendUtility.sendPacket(player, new SM_SET_BIND_POINT(worldId, x, y, z, player));
    }

    /**
     * @param portalName
     */
    public static void teleportToPortalExit(Player player, String portalName, int worldId, int delay)
    {
        PortalTemplate template = DataManager.PORTAL_DATA.getTemplateByNameAndWorld(worldId, portalName);
        if(template == null)
        {
            log.warn("No portal template found for : " + portalName + " " + worldId);
            return;
        }

        ExitPoint exitPoint = null;
        for(ExitPoint point : template.getExitPoint())
        {
            if(point.getRace() == null || point.getRace().equals(player.getCommonData().getRace()))
                exitPoint = point;
        }
        teleportTo(player, worldId, exitPoint.getX(), exitPoint.getY(), exitPoint.getZ(), delay);
    }

    public static void teleportToNpc(Player player, int npcId)
    {
        int delay = 0;
        SpawnTemplate template = DataManager.SPAWNS_DATA.getFirstSpawnByNpcId(npcId);

        if(template == null)
        {
            log.warn("No npc template found for : " + npcId);
            return;
        }

        teleportTo(player, template.getWorldId(), template.getX(), template.getY(), template.getZ(), delay);
    }

    /**
     * @param player
     * @param b
     */
    public static void moveToKiskLocation(Player player)
    {
        Kisk kisk = player.getKisk();

        int worldId = kisk.getWorldId();
        float x = kisk.getX();
        float y = kisk.getY();
        float z = kisk.getZ();
        byte heading = kisk.getHeading();

        int instanceId = 1;
        if(player.getWorldId() == worldId)
        {
            instanceId = player.getInstanceId();
        }

        teleportTo(player, worldId, instanceId, x, y, z, heading, 0);
    }

    public static void teleportToPrison(Player player)
    {
        switch(player.getCommonData().getRace())
        {
            case ELYOS:
                teleportTo(player, WorldMapType.LF_PRISON.getId(), 256, 256, 49, 0);
                break;
            case ASMODIANS:
                teleportTo(player, WorldMapType.DF_PRISON.getId(), 256, 256, 49, 0);
                break;
        }
    }
}
