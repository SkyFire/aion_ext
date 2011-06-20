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
package gameserver.model.gameobjects.stats;

import gameserver.configs.administration.AdminConfig;
import gameserver.model.alliance.PlayerAllianceEvent;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.state.CreatureState;
import gameserver.model.group.GroupEvent;
import gameserver.network.aion.serverpackets.SM_ATTACK_STATUS;
import gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import gameserver.network.aion.serverpackets.SM_FLY_TIME;
import gameserver.network.aion.serverpackets.SM_STATUPDATE_HP;
import gameserver.network.aion.serverpackets.SM_STATUPDATE_MP;
import gameserver.services.AllianceService;
import gameserver.services.LifeStatsRestoreService;
import gameserver.taskmanager.tasks.PacketBroadcaster.BroadcastMode;
import gameserver.utils.PacketSendUtility;
import gameserver.world.zone.ZoneName;

import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author ATracer, sphinx
 */
public class PlayerLifeStats extends CreatureLifeStats<Player> {
    protected int currentFp;
    private final ReentrantLock fpLock = new ReentrantLock();

    private Future<?> flyRestoreTask;
    private Future<?> flyReduceTask;

    public PlayerLifeStats(Player owner, int currentHp, int currentMp, int currentFp) {
        super(owner, currentHp, currentMp);
        this.currentFp = currentFp;
    }

    public PlayerLifeStats(Player owner) {
        super(owner, owner.getGameStats().getCurrentStat(StatEnum.MAXHP), owner.getGameStats().getCurrentStat(
                StatEnum.MAXMP));
        this.currentFp = owner.getGameStats().getCurrentStat(StatEnum.FLY_TIME);
    }

    @Override
    protected void onReduceHp() {
        sendHpPacketUpdate();
        triggerRestoreTask();
        sendGroupPacketUpdate();
    }

    @Override
    protected void onReduceMp() {
        sendMpPacketUpdate();
        triggerRestoreTask();
        sendGroupPacketUpdate();
    }

    @Override
    protected void onIncreaseMp(TYPE type, int value) {
        sendMpPacketUpdate();
        sendAttackStatusPacketUpdate(type, value);
        sendGroupPacketUpdate();
    }

    @Override
    protected void onIncreaseHp(TYPE type, int value) {
        if (this.isFullyRestoredHp()) {
            // FIXME: Temp Fix: Reset aggro list when hp is full.
            this.owner.getAggroList().clear();
        }
        sendHpPacketUpdate();
        sendAttackStatusPacketUpdate(type, value);
        sendGroupPacketUpdate();
    }

    private void sendGroupPacketUpdate() {
        Player owner = getOwner();
        if (owner.isInGroup())
            owner.getPlayerGroup().updateGroupUIToEvent(owner, GroupEvent.MOVEMENT);
        if (owner.isInAlliance())
            AllianceService.getInstance().updateAllianceUIToEvent(owner, PlayerAllianceEvent.MOVEMENT);
    }

    @Override
    public Player getOwner() {
        return (Player) super.getOwner();
    }

    @Override
    public void restoreHp() {
        int currentRegenHp = getOwner().getGameStats().getCurrentStat(StatEnum.REGEN_HP);
        if (getOwner().isInState(CreatureState.RESTING))
            currentRegenHp *= 8;
        increaseHp(TYPE.REGULAR, currentRegenHp);
    }

    @Override
    public void restoreMp() {
        int currentRegenMp = getOwner().getGameStats().getCurrentStat(StatEnum.REGEN_MP);
        if (getOwner().isInState(CreatureState.RESTING))
            currentRegenMp *= 8;
        increaseMp(TYPE.NATURAL_MP, currentRegenMp);
    }

    @Override
    public void synchronizeWithMaxStats() {
        if (isAlreadyDead())
            return;

        super.synchronizeWithMaxStats();
        int maxFp = getMaxFp();
        if (currentFp != maxFp)
            currentFp = maxFp;
    }

    @Override
    public void updateCurrentStats() {
        super.updateCurrentStats();

        if (getMaxFp() < currentFp)
            currentFp = getMaxFp();

        if (!owner.isInState(CreatureState.FLYING))
            triggerFpRestore();
    }

    public void sendHpPacketUpdate() {
        owner.addPacketBroadcastMask(BroadcastMode.UPDATE_PLAYER_HP_STAT);
    }

    public void sendHpPacketUpdateImpl() {
        if (owner == null)
            return;

        PacketSendUtility.sendPacket((Player) owner, new SM_STATUPDATE_HP(currentHp, getMaxHp()));
    }

    public void sendMpPacketUpdate() {
        owner.addPacketBroadcastMask(BroadcastMode.UPDATE_PLAYER_MP_STAT);
    }

    public void sendMpPacketUpdateImpl() {
        if (owner == null)
            return;

        PacketSendUtility.sendPacket((Player) owner, new SM_STATUPDATE_MP(currentMp, getMaxMp()));
    }

    /**
     * @return the currentFp
     */
    @Override
    public int getCurrentFp() {
        return this.currentFp;
    }

    /**
     * @return maxFp of creature according to stats
     */
    public int getMaxFp() {
        return owner.getGameStats().getCurrentStat(StatEnum.FLY_TIME);
    }

    /**
     * @return FP percentage 0 - 100
     */
    public int getFpPercentage() {
        return 100 * currentFp / getMaxFp();
    }

    /**
     * This method is called whenever caller wants to restore creatures's FP
     *
     * @param value
     * @return
     */
    @Override
    public int increaseFp(int value) {

        fpLock.lock();

        try {
            if (isAlreadyDead()) {
                return 0;
            }
            int newFp = this.currentFp + value;
            if (newFp > getMaxFp()) {
                newFp = getMaxFp();
            }
            if (currentFp != newFp) {
                this.currentFp = newFp;
            }
        }
        finally {
            fpLock.unlock();
        }

        onIncreaseFp();

        return currentFp;

    }

    /**
     * This method is called whenever caller wants to reduce creatures's MP
     *
     * @param value
     * @return
     */
    public int reduceFp(int value) {
        fpLock.lock();
        try {
            int newFp = this.currentFp - value;

            if (newFp < 0)
                newFp = 0;

            this.currentFp = newFp;
        }
        finally {
            fpLock.unlock();
        }

        onReduceFp();

        return currentFp;
    }

    public int setCurrentFp(int value) {
        fpLock.lock();
        try {
            int newFp = value;

            if (newFp < 0)
                newFp = 0;

            this.currentFp = newFp;
        }
        finally {
            fpLock.unlock();
        }

        onReduceFp();

        return currentFp;
    }

    protected void onIncreaseFp() {
        owner.addPacketBroadcastMask(BroadcastMode.UPDATE_PLAYER_FLY_TIME);
    }

    protected void onReduceFp() {
        owner.addPacketBroadcastMask(BroadcastMode.UPDATE_PLAYER_FLY_TIME);
    }

    public void sendFpPacketUpdateImpl() {
        if (owner == null)
            return;

        PacketSendUtility.sendPacket((Player) owner, new SM_FLY_TIME(currentFp, getMaxFp()));
    }

    /**
     * this method should be used only on FlyTimeRestoreService
     */
    public void restoreFp() {
        //how much fly time restoring per 2 second.
        increaseFp(1);
    }

    public void specialrestoreFp() {
        if (owner.getGameStats().getCurrentStat(StatEnum.REGEN_FP) != 0)
            increaseFp(owner.getGameStats().getCurrentStat(StatEnum.REGEN_FP) / 6);
    }

    public void triggerFpRestore() {
        cancelFpReduce();

        if (flyRestoreTask == null && !alreadyDead && !isFlyTimeFullyRestored()) {
            this.flyRestoreTask = LifeStatsRestoreService.getInstance().scheduleFpRestoreTask(this);
        }
    }

    public void cancelFpRestore() {
        if (flyRestoreTask != null && !flyRestoreTask.isCancelled()) {
            flyRestoreTask.cancel(false);
            this.flyRestoreTask = null;
        }
    }

    public void triggerFpReduce(ZoneName currentFlightZoneName) {
        cancelFpRestore();
        cancelFpReduce();

        if (flyReduceTask == null && !alreadyDead &&
                getOwner().getAccessLevel() < AdminConfig.GM_FLIGHT_UNLIMITED) {
            this.flyReduceTask = LifeStatsRestoreService.getInstance().scheduleFpReduceTask(this, currentFlightZoneName);
        }
    }

    public void cancelFpReduce() {
        if (flyReduceTask != null && !flyReduceTask.isCancelled()) {
            flyReduceTask.cancel(false);
            this.flyReduceTask = null;
        }
    }

    public boolean isFlyTimeFullyRestored() {
        return getMaxFp() == currentFp;
    }

    @Override
    public void cancelAllTasks() {
        super.cancelAllTasks();
        cancelFpReduce();
        cancelFpRestore();
    }

    @Override
    public void triggerRestoreOnRevive() {
        super.triggerRestoreOnRevive();
        triggerFpRestore();
    }

    @Override
    protected void sendAttackStatusPacketUpdate(TYPE type, int value) {
        if (owner == null) {
            return;
        }

        PacketSendUtility.sendPacket((Player) owner, new SM_ATTACK_STATUS((Player) owner, type, 0, value));
        PacketSendUtility.broadcastPacket(owner, new SM_ATTACK_STATUS(owner, 0));
    }

}
