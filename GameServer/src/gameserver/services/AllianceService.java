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

import gameserver.configs.main.GroupConfig;
import gameserver.model.alliance.PlayerAlliance;
import gameserver.model.alliance.PlayerAllianceEvent;
import gameserver.model.alliance.PlayerAllianceMember;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Monster;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.RequestResponseHandler;
import gameserver.model.gameobjects.player.RewardType;
import gameserver.model.group.PlayerGroup;
import gameserver.network.aion.serverpackets.*;
import gameserver.questEngine.QuestEngine;
import gameserver.questEngine.model.QuestCookie;
import gameserver.restrictions.RestrictionsManager;
import gameserver.utils.MathUtil;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.utils.idfactory.IDFactory;
import gameserver.utils.stats.StatFunctions;
import gameserver.world.WorldType;
import javolution.util.FastMap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Sarynth
 */
public class AllianceService {
    /**
     * @return alliance service
     */
    public static final AllianceService getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * Caching remove group member schedule
     */
    private FastMap<Integer, ScheduledFuture<?>> playerAllianceRemovalTasks;

    /**
     * Caching alliance members
     */
    private final FastMap<Integer, PlayerAlliance> allianceMembers;

    public AllianceService() {
        allianceMembers = new FastMap<Integer, PlayerAlliance>();
        playerAllianceRemovalTasks = new FastMap<Integer, ScheduledFuture<?>>();
    }

    /**
     * This method will add a member to the group member cache
     *
     * @param player
     */
    private void addAllianceMemberToCache(Player player) {
        if (!allianceMembers.containsKey(player.getObjectId()))
            allianceMembers.put(player.getObjectId(), player.getPlayerAlliance());
    }

    /**
     * @param playerObjId
     */
    private void removeAllianceMemberFromCache(int playerObjId) {
        if (allianceMembers.containsKey(playerObjId))
            allianceMembers.remove(playerObjId);
    }


    /**
     * @param playerObjId
     * @return returns true if player is in the cache
     */
    public boolean isAllianceMember(int playerObjId) {
        return allianceMembers.containsKey(playerObjId);
    }

    /**
     * Returns the player's alliance -- Required when Relogging
     *
     * @param playerObjId
     * @return PlayerAlliance
     */
    public PlayerAlliance getPlayerAlliance(int playerObjId) {
        return allianceMembers.get(playerObjId);
    }

    /**
     * @param playerObjectId
     * @param task
     */
    private void addAllianceRemovalTask(int playerObjectId, ScheduledFuture<?> task) {
        if (!playerAllianceRemovalTasks.containsKey(playerObjectId))
            playerAllianceRemovalTasks.put(playerObjectId, task);
    }

    /**
     * @param playerObjectId
     */
    private void cancelRemovalTask(int playerObjectId) {
        if (playerAllianceRemovalTasks.containsKey(playerObjectId)) {
            playerAllianceRemovalTasks.get(playerObjectId).cancel(true);
            playerAllianceRemovalTasks.remove(playerObjectId);
        }
    }

    /**
     * @param player
     */
    public void scheduleRemove(final Player player) {
        ScheduledFuture<?> future = ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                removeMemberFromAlliance(player.getPlayerAlliance(), player.getObjectId(), PlayerAllianceEvent.LEAVE_TIMEOUT);
            }
        }, GroupConfig.ALLIANCE_REMOVE_TIME * 1000);

        addAllianceRemovalTask(player.getObjectId(), future);
        player.getPlayerAlliance().onPlayerDisconnect(player);
    }

    /**
     * @param inviter
     * @param invited
     */
    public void invitePlayerToAlliance(final Player inviter, final Player invited) {
        if (RestrictionsManager.canInviteToAlliance(inviter, invited)) {
            RequestResponseHandler responseHandler = getResponseHandler(inviter, invited);

            boolean result = invited.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_REQUEST_ALLIANCE_INVITE, responseHandler);

            if (result) {
                if (invited.isInGroup()) {
                    PacketSendUtility.sendPacket(inviter, SM_SYSTEM_MESSAGE.STR_PARTY_ALLIANCE_INVITED_HIS_PARTY(invited.getName()));
                } else {
                    PacketSendUtility.sendPacket(inviter, SM_SYSTEM_MESSAGE.STR_FORCE_INVITED_HIM(invited.getName()));
                }

                PacketSendUtility.sendPacket(invited, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_REQUEST_ALLIANCE_INVITE, 0, inviter.getName()));
            }
        }
    }

    /**
     * @param inviter
     * @param invited
     * @return requestResponseHandler
     */
    private RequestResponseHandler getResponseHandler(final Player inviter, final Player invited) {
        RequestResponseHandler responseHandler = new RequestResponseHandler(inviter) {
            @Override
            public void acceptRequest(Creature requester, Player responder) {
                List<Player> playersToAdd = new ArrayList<Player>();
                PlayerAlliance alliance = inviter.getPlayerAlliance();

                if (alliance == null) {
                    alliance = new PlayerAlliance(IDFactory.getInstance().nextId(), inviter.getObjectId());

                    // Collect Inviter Group
                    if (inviter.isInGroup()) {
                        PlayerGroup group = inviter.getPlayerGroup();
                        playersToAdd.addAll(group.getMembers());
                        Iterator<Player> pIter = group.getMembers().iterator();
                        while (pIter.hasNext()) {
                            GroupService.getInstance().removePlayerFromGroup(pIter.next());
                        }
                    } else {
                        playersToAdd.add(inviter);
                    }
                } else if (alliance.size() == 24) {
                    PacketSendUtility.sendMessage(invited, "That alliance is already full.");
                    PacketSendUtility.sendMessage(inviter, "Your alliance is already full.");
                    return;
                } else if (invited.isInGroup() && invited.getPlayerGroup().size() + alliance.size() > 24) {
                    PacketSendUtility.sendMessage(invited, "That alliance is now too full for your group to join.");
                    PacketSendUtility.sendMessage(inviter, "Your alliance is now too full for that group to join.");
                    return;
                }

                // Collect Invited Group
                if (invited.isInGroup()) {
                    PlayerGroup group = invited.getPlayerGroup();
                    playersToAdd.addAll(group.getMembers());
                    Iterator<Player> pIter = group.getMembers().iterator();
                    while (pIter.hasNext()) {
                        GroupService.getInstance().removePlayerFromGroup(pIter.next());
                    }
                } else {
                    playersToAdd.add(invited);
                }

                // Finally, send packets and add players.
                for (Player member : playersToAdd) {
                    addMemberToAlliance(alliance, member);
                }
            }

            @Override
            public void denyRequest(Creature requester, Player responder) {
                PacketSendUtility.sendPacket(inviter, SM_SYSTEM_MESSAGE.STR_PARTY_ALLIANCE_HE_REJECT_INVITATION(responder.getName()));
            }
        };
        return responseHandler;
    }

    /**
     * @param alliance
     * @param newMember
     */
    protected void addMemberToAlliance(PlayerAlliance alliance, Player newMember) {
        alliance.addMember(newMember);
        addAllianceMemberToCache(newMember);

        PacketSendUtility.sendPacket(newMember, new SM_ALLIANCE_INFO(alliance));
        PacketSendUtility.sendPacket(newMember, new SM_SHOW_BRAND(0, 0));
        PacketSendUtility.sendPacket(newMember, SM_SYSTEM_MESSAGE.STR_FORCE_ENTERED_FORCE());

        broadcastAllianceMemberInfo(alliance, newMember.getObjectId(), PlayerAllianceEvent.ENTER);
        sendOtherMemberInfo(alliance, newMember);
    }


    /**
     * @param alliance
     * @param playerObjectId
     * @param allianceGroupId
     * @param secondObjectId
     */
    public void handleGroupChange(PlayerAlliance alliance, int playerObjectId, int allianceGroupId, int secondObjectId) {
        if (allianceGroupId == 0) {
            alliance.swapPlayers(playerObjectId, secondObjectId);

            broadcastAllianceMemberInfo(alliance, playerObjectId, PlayerAllianceEvent.MEMBER_GROUP_CHANGE);
            broadcastAllianceMemberInfo(alliance, secondObjectId, PlayerAllianceEvent.MEMBER_GROUP_CHANGE);
        } else {
            alliance.setAllianceGroupFor(playerObjectId, allianceGroupId);
            broadcastAllianceMemberInfo(alliance, playerObjectId, PlayerAllianceEvent.MEMBER_GROUP_CHANGE);
        }
    }

    /**
     * @param memberToUpdate
     * @param event
     * @param params
     */
    private void broadcastAllianceMemberInfo(PlayerAlliance alliance, int playerObjectId, PlayerAllianceEvent event, String... params) {
        PlayerAllianceMember memberToUpdate = alliance.getPlayer(playerObjectId);
        if (memberToUpdate != null)
            broadcastAllianceMemberInfo(alliance, memberToUpdate, event, params);
    }

    private void broadcastAllianceMemberInfo(PlayerAlliance alliance, PlayerAllianceMember memberToUpdate, PlayerAllianceEvent event, String... params) {
        for (PlayerAllianceMember allianceMember : alliance.getMembers()) {
            if (allianceMember.getPlayer() == null)
                continue;

            Player member = allianceMember.getPlayer();

            PacketSendUtility.sendPacket(member, new SM_ALLIANCE_MEMBER_INFO(memberToUpdate, event));
            PacketSendUtility.sendPacket(member, new SM_PLAYER_ID(memberToUpdate));
            switch (event) {
                case ENTER:
                    if (!member.equals(memberToUpdate.getPlayer()))
                        PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.STR_FORCE_HE_ENTERED_FORCE(memberToUpdate.getName()));
                    break;
                case LEAVE:
                    PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.STR_PARTY_ALLIANCE_HE_LEAVED_PARTY(memberToUpdate.getName()));
                    break;
                case LEAVE_TIMEOUT:
                    PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.STR_FORCE_HE_BECOME_OFFLINE_TIMEOUT(memberToUpdate.getName()));
                    break;
                case BANNED:
                    if (params.length == 0)
                        return;

                    if (member.equals(memberToUpdate.getPlayer()))
                        PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.STR_FORCE_BAN_ME(params[0]));
                    else
                        PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.STR_FORCE_BAN_HIM(params[0], memberToUpdate.getName()));
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * @param alliance
     * @param event
     * @param params
     */
    public void broadcastAllianceInfo(PlayerAlliance alliance, PlayerAllianceEvent event, String... params) {
        SM_ALLIANCE_INFO packet = new SM_ALLIANCE_INFO(alliance);
        for (PlayerAllianceMember allianceMember : alliance.getMembers()) {
            if (allianceMember.getPlayer() == null)
            	continue;
            
            Player member = allianceMember.getPlayer();
            PacketSendUtility.sendPacket(member, packet);
            switch (event) {
                case APPOINT_CAPTAIN:
                    PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.STR_FORCE_CHANGE_LEADER(params[0], alliance.getCaptain().getName()));
                    break;
                case APPOINT_VICE_CAPTAIN:
                    PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.STR_FORCE_PROMOTE_MANAGER(params[0]));
                    break;
                case DEMOTE_VICE_CAPTAIN:
                    PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.STR_FORCE_DEMOTE_MANAGER(params[0]));
                    break;
            }
        }
    }

    /**
     * @param alliance
     * @param member
     */
    private void sendOtherMemberInfo(PlayerAlliance alliance, Player memberToSend) {

        for (PlayerAllianceMember allianceMember : alliance.getMembers()) {
            if (memberToSend.getObjectId() == allianceMember.getObjectId())
                continue;

            PacketSendUtility.sendPacket(memberToSend, new SM_ALLIANCE_MEMBER_INFO(allianceMember, PlayerAllianceEvent.UPDATE));
            PacketSendUtility.sendPacket(memberToSend, new SM_PLAYER_ID(allianceMember));
        }
    }

    /**
     * @param actingMember
     * @param status
     * @param playerObjId
     */
    public void playerStatusInfo(Player actingMember, int status, int playerObjId) {
        PlayerAlliance alliance = actingMember.getPlayerAlliance();

        if (alliance == null) {
            PacketSendUtility.sendMessage(actingMember, "Your alliance is null...");
        }

        switch (status) {
            case 12: // Leave Alliance
                removeMemberFromAlliance(alliance, actingMember.getObjectId(), PlayerAllianceEvent.LEAVE);
                break;
            case 14: // Ban from Alliance
                removeMemberFromAlliance(alliance, playerObjId, PlayerAllianceEvent.BANNED, actingMember.getName());
                break;
            case 15: // Make Alliance Captain
                String oldLeader = alliance.getCaptain().getName();
                alliance.setLeader(playerObjId);
                broadcastAllianceInfo(alliance, PlayerAllianceEvent.APPOINT_CAPTAIN, oldLeader, alliance.getCaptain().getName());
                break;
            case 19: // Check Readiness State
                PacketSendUtility.sendMessage(actingMember, "Readiness check is not implmeneted yet. (ID: " + playerObjId + ")");
                break;
            case 23: // Appoint Alliance ViceCaptain
                alliance.promoteViceLeader(playerObjId);
                broadcastAllianceInfo(alliance, PlayerAllianceEvent.APPOINT_VICE_CAPTAIN, alliance.getPlayer(playerObjId).getName());
                break;
            case 24: // Demote Alliance ViceCaptain
                alliance.demoteViceLeader(playerObjId);
                broadcastAllianceInfo(alliance, PlayerAllianceEvent.DEMOTE_VICE_CAPTAIN, alliance.getPlayer(playerObjId).getName());
                break;
        }
    }

    /**
     * @param member
     * @param event
     * @param params
     */
    public void removeMemberFromAlliance(PlayerAlliance alliance, int memberObjectId, PlayerAllianceEvent event, String... params) {
    	// Player
        PlayerAllianceMember allianceMember = alliance.getPlayer(memberObjectId);

        // TODO: Why is this null sometimes (found when banning from alliance)
        if (allianceMember == null)
            return;

        Player allianceMemberPlayer = allianceMember.getPlayer();
        if (allianceMemberPlayer != null)
        {
            allianceMemberPlayer.setPlayerAlliance(null);
            PacketSendUtility.sendPacket(allianceMemberPlayer, new SM_LEAVE_GROUP_MEMBER());
        }

        // Alliance
        broadcastAllianceMemberInfo(alliance, allianceMember, event, params);
        alliance.removeMember(memberObjectId);
        removeAllianceMemberFromCache(memberObjectId);

        // Check Disband
        if (alliance.size() == 1) {
            // IDFactory.getInstance().releaseId(alliance.getObjectId());
            Player player = alliance.getCaptain().getPlayer();
            removeMemberFromAlliance(alliance, alliance.getCaptainObjectId(), event);
            if (player != null)
                PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_PARTY_ALLIANCE_DISPERSED());
        }
    }

    /**
     * Unused until login packet sequence can be determined
     *
     * @param player
     */
    public void setAlliance(Player player) {
        if (!isAllianceMember(player.getObjectId()))
            return;

        final PlayerAlliance alliance = getPlayerAlliance(player.getObjectId());

        // Alliance is empty.
        if (alliance.size() == 0) {
            removeAllianceMemberFromCache(player.getObjectId());
            return;
        }

        player.setPlayerAlliance(alliance);
        cancelRemovalTask(player.getObjectId());
    }

    /**
     * Unused until login packet sequence can be determined
     *
     * @param player
     */
    public void onLogin(Player player) {
        final PlayerAlliance alliance = player.getPlayerAlliance();

        alliance.onPlayerLogin(player);

        // Required for relogging to work. (?)
        PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, false));

        PacketSendUtility.sendPacket(player, new SM_ALLIANCE_INFO(alliance));
        PacketSendUtility.sendPacket(player, new SM_SHOW_BRAND(0, 0));

        broadcastAllianceMemberInfo(alliance, player.getObjectId(), PlayerAllianceEvent.RECONNECT);
        sendOtherMemberInfo(alliance, player);
    }

    /**
     * @param player
     */
    public void onLogout(Player player) {
        scheduleRemove(player);
    }

    /**
     * Currently sends movement packet to all alliance members.
     *
     * @param player
     * @param event
     */
    public void updateAllianceUIToEvent(Player player, PlayerAllianceEvent event) {
        PlayerAlliance alliance = player.getPlayerAlliance();

        switch (event) {
            case MOVEMENT:
            case UPDATE:
                PlayerAllianceMember member = alliance.getPlayer(player.getObjectId());
                if (member != null) {
                    SM_ALLIANCE_MEMBER_INFO packet = new SM_ALLIANCE_MEMBER_INFO(member, event);
                    for (PlayerAllianceMember allianceMember : alliance.getMembers()) {
                        if (allianceMember.isOnline() && !player.equals(allianceMember.getPlayer()))
                            PacketSendUtility.sendPacket(allianceMember.getPlayer(), packet);
                    }
                }
                break;

            default:
                // Unsupported
                break;
        }
    }

    /**
     * Sends brand information to each member.
     *
     * @param alliance
     * @param brandId
     * @param targetObjectId
     */
    public void showBrand(PlayerAlliance alliance, int brandId, int targetObjectId) {
        for (PlayerAllianceMember allianceMember : alliance.getMembers()) {
            if (!allianceMember.isOnline()) continue;
            PacketSendUtility.sendPacket(allianceMember.getPlayer(), new SM_SHOW_BRAND(brandId, targetObjectId));
        }
    }

    /**
     * @param winner
     * @param owner
     */
    public void doReward(PlayerAlliance alliance, Monster owner) {
        // TODO: Merge with group type do-reward. (Near identical to GroupService doReward code.)
        // Plus complete rewrite of drop system and exp system.
        // http://www.aionsource.com/topic/40542-character-stats-xp-dp-origin-gerbatorteam-july-2009/

        // Find Group Members and Determine Highest Level
        List<Player> players = new ArrayList<Player>();
        int partyLvlSum = 0;
        int highestLevel = 0;
        for (PlayerAllianceMember allianceMember : alliance.getMembers()) {
            if (!allianceMember.isOnline()) continue;
            Player member = allianceMember.getPlayer();
            if (MathUtil.isIn3dRange(member, owner, GroupConfig.GROUP_MAX_DISTANCE)) {
                if (member.getLifeStats().isAlreadyDead())
                    continue;
                players.add(member);
                partyLvlSum += member.getLevel();
                if (member.getLevel() > highestLevel)
                    highestLevel = member.getLevel();
            }
        }

        // All are dead or not nearby.
        if (players.size() == 0)
            return;

        //AP reward
        int apRewardPerMember = 0;
        WorldType worldType = owner.getWorldType();

        //WorldType worldType = sp.getWorld().getWorldMap(player.getWorldId()).getWorldType();
        if (worldType == WorldType.ABYSS) {
            // Split Evenly
            apRewardPerMember = Math.round(StatFunctions.calculateGroupAPReward(highestLevel, owner) / players.size());
        }

        // Exp reward
        long expReward = StatFunctions.calculateGroupExperienceReward(highestLevel, owner);

        // Exp Mod
        // TODO: Add logic to prevent power leveling. Players 10 levels below highest member should get 0 exp.
        double mod = 1;
        if (players.size() == 0)
            return;
        else if (players.size() > 1)
            mod = 1 + (((players.size() - 1) * 10) / 100);

        expReward *= mod;

        for (Player member : players) {
            // Exp reward
            long reward = (expReward * member.getLevel()) / partyLvlSum;
            member.getCommonData().addExp(reward, RewardType.GROUP_HUNTING);

            PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.EXP(Long.toString(reward)));

            // DP reward
            int currentDp = member.getCommonData().getDp();
            int dpReward = StatFunctions.calculateGroupDPReward(member, owner);
            member.getCommonData().setDp(dpReward + currentDp);

            // AP reward
            if (apRewardPerMember > 0)
                member.getCommonData().addAp(Math.round(apRewardPerMember * member.getRates().getApNpcRate()));

            QuestEngine.getInstance().onKill(new QuestCookie(owner, member, 0, 0));
        }

        // Give Drop
        Player leader = alliance.getCaptain().getPlayer();

        // TODO: Better Group/Alliance Drop methods.
        if (leader == null) return;

        DropService.getInstance().registerDrop(owner, leader, highestLevel, players);
    }

    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder {
        protected static final AllianceService instance = new AllianceService();
    }
}
