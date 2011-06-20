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
package gameserver.model.templates.stats;

import gameserver.model.PlayerClass;
import gameserver.utils.stats.ClassStats;

/**
 * @author ATracer
 */
public class CalculatedPlayerStatsTemplate extends PlayerStatsTemplate {

    private PlayerClass playerClass;

    public CalculatedPlayerStatsTemplate(PlayerClass playerClass) {
        this.playerClass = playerClass;
    }

    @Override
    public int getAccuracy() {
        return ClassStats.getAccuracyFor(playerClass);
    }

    @Override
    public int getAgility() {
        return ClassStats.getAgilityFor(playerClass);
    }

    @Override
    public int getHealth() {
        return ClassStats.getHealthFor(playerClass);
    }

    @Override
    public int getKnowledge() {
        return ClassStats.getKnowledgeFor(playerClass);
    }

    @Override
    public int getPower() {
        return ClassStats.getPowerFor(playerClass);
    }

    @Override
    public int getWill() {
        return ClassStats.getWillFor(playerClass);
    }

    @Override
    public float getAttackSpeed() {
        return ClassStats.getAttackSpeedFor(playerClass) / 1000f;
    }

    @Override
    public int getBlock() {
        return ClassStats.getBlockFor(playerClass);
    }

    @Override
    public int getEvasion() {
        return ClassStats.getEvasionFor(playerClass);
    }

    @Override
    public float getFlySpeed() {
        // TODO Auto-generated method stub
        return ClassStats.getFlySpeedFor(playerClass);
    }

    @Override
    public int getMagicAccuracy() {
        return ClassStats.getMagicAccuracyFor(playerClass);
    }

    @Override
    public int getMainHandAccuracy() {
        return ClassStats.getMainHandAccuracyFor(playerClass);
    }

    @Override
    public int getMainHandAttack() {
        return ClassStats.getMainHandAttackFor(playerClass);
    }

    @Override
    public int getMainHandCritRate() {
        return ClassStats.getMainHandCritRateFor(playerClass);
    }

    @Override
    public int getMaxHp() {
        return ClassStats.getMaxHpFor(playerClass, 10); // level is hardcoded
    }

    @Override
    public int getMaxMp() {
        return 1000;
    }

    @Override
    public int getParry() {
        return ClassStats.getParryFor(playerClass);
    }

    @Override
    public float getRunSpeed() {
        return ClassStats.getSpeedFor(playerClass);
    }

    @Override
    public float getWalkSpeed() {
        return 1.5f;
    }

    @Override
    public int getBoostHeal() {
        return ClassStats.getBoostHealFor(playerClass);
    }

}
