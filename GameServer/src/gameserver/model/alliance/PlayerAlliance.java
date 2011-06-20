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
package gameserver.model.alliance;

import gameserver.model.gameobjects.AionObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_ALLIANCE_MEMBER_INFO;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.services.AllianceService;
import gameserver.utils.PacketSendUtility;
import javolution.util.FastMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Sarynth
 */
public class PlayerAlliance extends AionObject {
    private int captainObjectId;

    private List<Integer> viceCaptainObjectIds = new ArrayList<Integer>();

    private FastMap<Integer, PlayerAllianceMember> allianceMembers = new FastMap<Integer, PlayerAllianceMember>().shared();
    private FastMap<Integer, PlayerAllianceGroup> allianceGroupForMember = new FastMap<Integer, PlayerAllianceGroup>().shared();

    private FastMap<Integer, PlayerAllianceGroup> allianceGroups = new FastMap<Integer, PlayerAllianceGroup>().shared();


    public PlayerAlliance(int objectId, int leaderObjectId) {
        super(objectId);
        setLeader(leaderObjectId);
    }

    public void addMember(Player member) {
        PlayerAllianceGroup group = getOpenAllianceGroup();
        PlayerAllianceMember allianceMember = new PlayerAllianceMember(member);
        group.addMember(allianceMember);

        allianceMembers.put(member.getObjectId(), allianceMember);
        allianceGroupForMember.put(member.getObjectId(), group);

        member.setPlayerAlliance(this);
    }

    /**
     * @return OpenAllianceGroup
     */
    private PlayerAllianceGroup getOpenAllianceGroup() {
        for (int i = 1000; i <= 1004; i++) {
            PlayerAllianceGroup group = allianceGroups.get(i);

            if (group == null) {
                group = new PlayerAllianceGroup(this);
                group.setAllianceId(i);
                allianceGroups.put(i, group);
                return group;
            }

            if (group.getMembers().size() < 6)
                return group;
        }
        throw new RuntimeException("All Alliance Groups Full.");
    }

    /**
     * @param member
     */
    public void removeMember(int memberObjectId) {
        allianceGroupForMember.get(memberObjectId).removeMember(memberObjectId);
        allianceGroupForMember.remove(memberObjectId);
        allianceMembers.remove(memberObjectId);

        // Check if Member was a Vice Captain
        if (viceCaptainObjectIds.contains(memberObjectId)) {
            viceCaptainObjectIds.remove(viceCaptainObjectIds.indexOf(memberObjectId));
        }

        // Check if Member was Captain
        if (memberObjectId == this.captainObjectId) {
            // Check Vice Captain for replacement...
            if (viceCaptainObjectIds.size() > 0) {
                int newCaptain = viceCaptainObjectIds.get(0);
                viceCaptainObjectIds.remove(viceCaptainObjectIds.indexOf(newCaptain));
                this.captainObjectId = newCaptain;
            } else if (allianceMembers.size() != 0) {
                // Pick first player in map
                PlayerAllianceMember newCaptain = allianceMembers.values().iterator().next();
                this.captainObjectId = newCaptain.getObjectId();
            }
        }

        AllianceService.getInstance().broadcastAllianceInfo(this, PlayerAllianceEvent.UPDATE);
    }


    /**
     * @param leader
     */
    public void setLeader(int newLeaderObjectId) {
        if (viceCaptainObjectIds.contains(newLeaderObjectId)) {
            // If new leader is Vice, set old leader to Vice.
            viceCaptainObjectIds.remove(viceCaptainObjectIds.indexOf(newLeaderObjectId));
            viceCaptainObjectIds.add(this.captainObjectId);
        }
        this.captainObjectId = newLeaderObjectId;
    }

    /**
     * @param viceLeader
     */
    public void promoteViceLeader(int viceLeaderObjectId) {
        viceCaptainObjectIds.add(viceLeaderObjectId);
    }

    /**
     * @param viceLeader
     */
    public void demoteViceLeader(int viceLeaderObjectId) {
        viceCaptainObjectIds.remove(viceCaptainObjectIds.indexOf(viceLeaderObjectId));
    }

    /**
     * @return
     */
    public PlayerAllianceMember getCaptain() {
        return getPlayer(getCaptainObjectId());
    }

    /**
     * @return captainObjectId
     */
    public int getCaptainObjectId() {
        return this.captainObjectId;
    }

    /**
     * @return viceCaptainObjectIds
     */
    public List<Integer> getViceCaptainObjectIds() {
        return this.viceCaptainObjectIds;
    }

    /**
     * @param player
     * @return
     */
    public int getAllianceIdFor(int playerObjectId) {
        if (!allianceGroupForMember.containsKey(playerObjectId))
            return 0;
        else
            return allianceGroupForMember.get(playerObjectId).getAllianceId();
    }

    /**
     * @param playerObjectId
     * @return member
     */
    public PlayerAllianceMember getPlayer(int playerObjectId) {
        return allianceMembers.get(playerObjectId);
    }

    /**
     * @return alliance size
     */
    public int size() {
        return getMembers().size();
    }

    /**
     * @return
     */
    public boolean isFull() {
        return (size() >= 24);
    }

    /**
     * @return
     */
    public Collection<PlayerAllianceMember> getMembers() {
        return allianceMembers.values();
    }

    /**
     * @param objectId
     * @return
     */
    public boolean hasAuthority(int playerObjectId) {
        return (playerObjectId == captainObjectId || viceCaptainObjectIds.contains(playerObjectId));
    }


    @Override
    public String getName() {
        return "Player Alliance";
    }

    /**
     * @param playerObjectId
     * @param secondObjectId
     */
    public void swapPlayers(int playerObjectId1, int playerObjectId2) {
        PlayerAllianceGroup group1 = allianceGroupForMember.get(playerObjectId1);
        PlayerAllianceGroup group2 = allianceGroupForMember.get(playerObjectId2);

        PlayerAllianceMember player1 = group1.removeMember(playerObjectId1);
        PlayerAllianceMember player2 = group2.removeMember(playerObjectId2);

        group1.addMember(player2);
        group2.addMember(player1);

        allianceGroupForMember.put(playerObjectId1, group2);
        allianceGroupForMember.put(playerObjectId2, group1);
    }

    /**
     * Designed to be able to move members while off-line.
     *
     * @param memberObjectId
     * @param allianceGroupId
     */
    public void setAllianceGroupFor(int memberObjectId, int allianceGroupId) {
        PlayerAllianceGroup leavingGroup = allianceGroupForMember.get(memberObjectId);
        if (leavingGroup == null)
            return;
        PlayerAllianceMember member = leavingGroup.getMemberById(memberObjectId);
        leavingGroup.removeMember(memberObjectId);

        PlayerAllianceGroup group = allianceGroups.get(allianceGroupId);

        if (group == null) {
            group = new PlayerAllianceGroup(this);
            group.setAllianceId(allianceGroupId);
            allianceGroups.put(allianceGroupId, group);
        }

        group.addMember(member);
        allianceGroupForMember.put(memberObjectId, group);
    }

    /**
     * @param objectId
     * @return
     */
    public PlayerAllianceGroup getPlayerAllianceGroupForMember(int objectId) {
        return allianceGroupForMember.get(objectId);
    }

    /**
     * @param player
     */
    public void onPlayerLogin(Player player) {
        allianceMembers.get(player.getObjectId()).onLogin(player);
    }

    /**
     * @param player
     */
    public void onPlayerDisconnect(Player player) {
        PlayerAllianceMember allianceMember = allianceMembers.get(player.getObjectId());
        allianceMember.onDisconnect();

        for (PlayerAllianceMember member : allianceMembers.values()) {
            // Check offline
            if (member.isOnline()) {
                PacketSendUtility.sendPacket(member.getPlayer(), SM_SYSTEM_MESSAGE.STR_FORCE_HE_BECOME_OFFLINE(player.getName()));
                PacketSendUtility.sendPacket(member.getPlayer(), new SM_ALLIANCE_MEMBER_INFO(allianceMember, PlayerAllianceEvent.DISCONNECTED));
            }
        }
    }

    /**
     * @param objectId
     * @return
     */
    public Collection<PlayerAllianceMember> getMembersForGroup(int playerObjectId) {
        PlayerAllianceGroup group = allianceGroupForMember.get(playerObjectId);
        // TODO: This should not be null...
        if (group == null) return (new FastMap<Integer, PlayerAllianceMember>()).values();
        return group.getMembers();
	}

}
