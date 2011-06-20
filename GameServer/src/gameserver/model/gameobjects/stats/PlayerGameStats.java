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

import gameserver.configs.main.CustomConfig;
import gameserver.dataholders.PlayerStatsData;
import gameserver.model.EmotionType;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.modifiers.CheckWeapon;
import gameserver.model.templates.stats.PlayerStatsTemplate;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_STATS_INFO;
import gameserver.utils.PacketSendUtility;

/**
 * @author xavier
 */
public class PlayerGameStats extends CreatureGameStats<Player> {
    private int currentRunSpeed = 0;
    private int currentFlySpeed = 0;
    private int currentAttackSpeed = 0;

    /**
     * @param owner
     */
    public PlayerGameStats(Player owner) {
        super(owner);
    }

    /**
     * @param playerStatsData
     * @param owner
     */
    public PlayerGameStats(PlayerStatsData playerStatsData, final Player owner) {
        super(owner);
        PlayerStatsTemplate pst = playerStatsData.getTemplate(owner.getPlayerClass(), owner.getLevel());
        initStats(pst, owner.getLevel());
        log.debug("loading base game stats for player " + owner.getName() + " (id " + owner.getObjectId() + "): "
                + this);
    }


    public void recomputeStats() {
        super.recomputeStats();
        CheckWeapon.getInstance().WeaponCheck(owner);
        int newRunSpeed = getCurrentStat(StatEnum.SPEED);
        int newFlySpeed = getCurrentStat(StatEnum.FLY_SPEED);
        int newAttackSpeed = getCurrentStat(StatEnum.ATTACK_SPEED);

        if (newRunSpeed != currentRunSpeed || currentFlySpeed != newFlySpeed || newAttackSpeed != currentAttackSpeed) {
            PacketSendUtility.broadcastPacket(owner, new SM_EMOTION(owner, EmotionType.START_EMOTE2, 0, 0), true);
        }

        PacketSendUtility.sendPacket(owner, new SM_STATS_INFO(owner));

        this.currentRunSpeed = newRunSpeed;
        this.currentFlySpeed = newFlySpeed;
        this.currentAttackSpeed = newAttackSpeed;
    }

    /**
     * @param pst
     * @param level
     */
    private void initStats(PlayerStatsTemplate pst, int level) {
        this.initStats(pst.getMaxHp(), pst.getMaxMp(), pst.getPower(), pst.getHealth(), pst.getAgility(), pst
                .getAccuracy(), pst.getKnowledge(), pst.getWill(), pst.getMainHandAttack(), pst.getMainHandCritRate(), Math
                .round(pst.getAttackSpeed() * 1000), 1500, Math.round(pst.getRunSpeed() * 1000), Math.round(pst.getFlySpeed() * 1000), pst.getBoostHeal());
        setAttackCounter(1);
        initStat(StatEnum.PARRY, pst.getParry());
        initStat(StatEnum.BLOCK, pst.getBlock());
        initStat(StatEnum.EVASION, pst.getEvasion());
        initStat(StatEnum.MAGICAL_ACCURACY, pst.getMagicAccuracy());
        initStat(StatEnum.MAIN_HAND_ACCURACY, pst.getMainHandAccuracy());
        initStat(StatEnum.FLY_TIME, CustomConfig.BASE_FLYTIME);
        initStat(StatEnum.REGEN_HP, level + 3);
        initStat(StatEnum.REGEN_MP, level + 8);
        initStat(StatEnum.MAXDP, 4000);
        initStat(StatEnum.BOOST_HUNTING_XP_RATE, 100);
        initStat(StatEnum.BOOST_GROUP_HUNTING_XP_RATE, 100);
        initStat(StatEnum.BOOST_QUEST_XP_RATE, 100);
        initStat(StatEnum.BOOST_CRAFTING_XP_RATE, 100);
        initStat(StatEnum.BOOST_GATHERING_XP_RATE, 100);
    }

    /**
     * @param maxHp
     * @param maxMp
     * @param power
     * @param health
     * @param agility
     * @param accuracy
     * @param knowledge
     * @param will
     * @param mainHandAttack
     * @param mainHandCritRate
     * @param attackSpeed
     * @param attackRange
     * @param runSpeed
     * @param flySpeed
     */
    protected void initStats(int maxHp, int maxMp, int power, int health, int agility, int accuracy, int knowledge, int will, int mainHandAttack, int mainHandCritRate, int attackSpeed, int attackRange, int runSpeed, int flySpeed, int boostHeal) {
        stats.clear();
        initStat(StatEnum.MAXHP, maxHp);
        initStat(StatEnum.MAXMP, maxMp);
        initStat(StatEnum.POWER, power);
        initStat(StatEnum.ACCURACY, accuracy);
        initStat(StatEnum.HEALTH, health);
        initStat(StatEnum.AGILITY, agility);
        initStat(StatEnum.KNOWLEDGE, knowledge);
        initStat(StatEnum.WILL, will);
        initStat(StatEnum.MAIN_HAND_POWER, Math.round(18 * (power * 0.01f)));
        initStat(StatEnum.MAIN_HAND_CRITICAL, mainHandCritRate);
        initStat(StatEnum.OFF_HAND_POWER, 0);
        initStat(StatEnum.OFF_HAND_CRITICAL, 0);
        initStat(StatEnum.ATTACK_SPEED, attackSpeed);
        initStat(StatEnum.MAIN_HAND_ATTACK_SPEED, attackSpeed);
        initStat(StatEnum.OFF_HAND_ATTACK_SPEED, 0);
        initStat(StatEnum.ATTACK_RANGE, attackRange);
        initStat(StatEnum.PHYSICAL_DEFENSE, 0);
        initStat(StatEnum.PARRY, Math.round(agility * 3.1f - 248.5f + 12.4f * owner.getLevel()));
        initStat(StatEnum.EVASION, Math.round(agility * 3.1f - 248.5f + 12.4f * owner.getLevel()));
        initStat(StatEnum.BLOCK, Math.round(agility * 3.1f - 248.5f + 12.4f * owner.getLevel()));
        initStat(StatEnum.DAMAGE_REDUCE, 0);
        initStat(StatEnum.MAIN_HAND_ACCURACY, Math.round((accuracy * 2 - 10) + 8 * owner.getLevel()));
        initStat(StatEnum.OFF_HAND_ACCURACY, Math.round((accuracy * 2 - 10) + 8 * owner.getLevel()));
        initStat(StatEnum.MAGICAL_RESIST, 0);
        initStat(StatEnum.WIND_RESISTANCE, 0);
        initStat(StatEnum.FIRE_RESISTANCE, 0);
        initStat(StatEnum.WATER_RESISTANCE, 0);
        initStat(StatEnum.EARTH_RESISTANCE, 0);
        initStat(StatEnum.MAGICAL_ACCURACY, Math.round(14.26f * owner.getLevel()));
        initStat(StatEnum.BOOST_MAGICAL_SKILL, 0);
        initStat(StatEnum.SPEED, runSpeed);
        initStat(StatEnum.FLY_SPEED, flySpeed);
        initStat(StatEnum.PVP_ATTACK_RATIO, 0);
        initStat(StatEnum.PVP_DEFEND_RATIO, 0);
        initStat(StatEnum.BOOST_CASTING_TIME, 100);
        initStat(StatEnum.BOOST_HATE, 100);
        initStat(StatEnum.BOOST_HEAL, boostHeal);
        initStat(StatEnum.MAGICAL_RESIST, 50); // 1.9 every class start with 50 points of spell resist.
        initStat(StatEnum.MAGICAL_CRITICAL, 50);
        initStat(StatEnum.MAGICAL_CRITICAL_RESIST, 0);
    }

    /**
     * @param playerStatsData
     * @param level
     */
    public void doLevelUpgrade() {
        initStats(owner.getPlayerStatsTemplate(), owner.getLevel());
        recomputeStats();
    }
}
