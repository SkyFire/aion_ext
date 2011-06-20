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

import gameserver.model.gameobjects.Npc;
import gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import gameserver.services.LifeStatsRestoreService;

/**
 * @author ATracer
 */
public class NpcLifeStats extends CreatureLifeStats<Npc> {
    /**
     * @param owner
     */
    public NpcLifeStats(Npc owner) {
        super(owner, owner.getGameStats().getCurrentStat(StatEnum.MAXHP), owner.getGameStats().getCurrentStat(
                StatEnum.MAXMP));
    }

    @Override
    protected void onIncreaseHp(TYPE type, int value) {
        sendAttackStatusPacketUpdate(type, value);
    }

    @Override
    protected void onIncreaseMp(TYPE type, int value) {
        // nothing todo
    }

    @Override
    protected void onReduceHp() {
        // nothing todo
    }

    @Override
    protected void onReduceMp() {
        // nothing todo
    }

    @Override
    protected void triggerRestoreTask() {
        if (lifeRestoreTask == null && !alreadyDead) {
            this.lifeRestoreTask = LifeStatsRestoreService.getInstance().scheduleHpRestoreTask(this);
        }
	}
}
