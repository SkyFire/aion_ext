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
package gameserver.utils.stats;

import com.aionemu.commons.utils.Rnd;
import gameserver.configs.main.FallDamageConfig;
import gameserver.controllers.attack.AttackStatus;
import gameserver.model.SkillElement;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.Summon;
import gameserver.model.gameobjects.player.Equipment;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.state.CreatureState;
import gameserver.model.gameobjects.stats.CreatureGameStats;
import gameserver.model.gameobjects.stats.StatEnum;
import gameserver.model.templates.item.WeaponType;
import gameserver.model.templates.stats.NpcRank;
import gameserver.network.aion.serverpackets.SM_ATTACK_STATUS;
import gameserver.utils.PacketSendUtility;
import org.apache.log4j.Logger;

/**
 * @author ATracer
 * @author alexa026
 */
public class StatFunctions {
    private static Logger log = Logger.getLogger(StatFunctions.class);

    /**
     * @param player
     * @param target
     * @return XP reward from target
     */
    public static long calculateSoloExperienceReward(Player player, Creature target) {
        int playerLevel = player.getCommonData().getLevel();
        int targetLevel = target.getLevel();

        int baseXP = ((Npc) target).getObjectTemplate().getStatsTemplate().getMaxXp();
        int xpPercentage = XPRewardEnum.xpRewardFrom(targetLevel - playerLevel);

        long rewardXP = (long) Math.floor(baseXP * xpPercentage / 100);
        return (rewardXP < 0) ? Long.MAX_VALUE : rewardXP;
    }

    /**
     * @param player
     * @param target
     * @return
     */
    public static long calculateGroupExperienceReward(int maxLevelInRange, Creature target) {
        int targetLevel = target.getLevel();

        int baseXP = ((Npc) target).getObjectTemplate().getStatsTemplate().getMaxXp();
        int xpPercentage = XPRewardEnum.xpRewardFrom(targetLevel - maxLevelInRange);

        long rewardXP = (long) Math.floor(baseXP * xpPercentage / 100);
        return (rewardXP < 0) ? Long.MAX_VALUE : rewardXP;
    }

    /**
     * ref: http://www.aionsource.com/forum/mechanic-analysis/42597-character-stats-xp-dp-origin-gerbator-team-july-2009-a.html
     *
     * @param player
     * @param target
     * @return DP reward from target
     */
    public static int calculateSoloDPReward(Player player, Creature target) {
        int playerLevel = player.getCommonData().getLevel();
        int targetLevel = target.getLevel();
        NpcRank npcRank = ((Npc) target).getObjectTemplate().getRank();

        //TODO: fix to see monster Rank level, NORMAL lvl 1, 2 | ELITE lvl 1, 2 etc..
        //look at: http://www.aionsource.com/forum/mechanic-analysis/42597-character-stats-xp-dp-origin-gerbator-team-july-2009-a.html
        int baseDP = targetLevel * calculateRankMultipler(npcRank);

        int xpPercentage = XPRewardEnum.xpRewardFrom(targetLevel - playerLevel);
        int rewardDP = (int) Math.floor(baseDP * xpPercentage * player.getRates().getDpRate() / 100);
        return (rewardDP < 0) ? Integer.MAX_VALUE : rewardDP;
    }

    public static int calculatePvpSoloDPReward(Player defeated, Player winner) {
        int playerLevel = winner.getCommonData().getLevel();
        int targetLevel = defeated.getLevel();
        AbyssRankEnum playerRank = defeated.getAbyssRank().getRank();
        int baseDP = targetLevel * calculateRankPVPMultipler(playerRank);

        int xpPercentage = XPRewardEnum.xpRewardFrom(targetLevel - playerLevel);

        int rewardDP = (int) Math.floor(baseDP * xpPercentage * winner.getRates().getPvpDpRate() / 100);
        return (rewardDP < 0) ? Integer.MAX_VALUE : rewardDP;
    }

    /**
     * @param player
     * @param target
     * @return AP reward
     */
    public static int calculateSoloAPReward(Player player, Creature target) {
        int playerLevel = player.getCommonData().getLevel();
        int targetLevel = target.getLevel();
        int percentage = XPRewardEnum.xpRewardFrom(targetLevel - playerLevel);

        int rewardAP = (int) Math.floor(10 * percentage * player.getRates().getApNpcRate() / 100);
        return (rewardAP < 0) ? Integer.MAX_VALUE : rewardAP;
    }

    /**
     * @param maxLevelInRange
     * @param target
     * @return
     */
    public static int calculateGroupAPReward(int maxLevelInRange, Creature target) {
        int targetLevel = target.getLevel();
        NpcRank npcRank = ((Npc) target).getObjectTemplate().getRank();

        //TODO: fix to see monster Rank level, NORMAL lvl 1, 2 | ELITE lvl 1, 2 etc..
        int baseAP = 10 + calculateRankMultipler(npcRank) - 1;

        int apPercentage = XPRewardEnum.xpRewardFrom(targetLevel - maxLevelInRange);
        int rewardAP = (int) Math.floor(baseAP * apPercentage / 100);
        return (rewardAP < 0) ? Integer.MAX_VALUE : rewardAP;
    }

    /**
     * @param defeated
     * @param winner
     * @return Points Lost in PvP Death
     */
    public static int calculatePvPApLost(Player defeated, Player winner) {
        int pointsLost = Math.round(defeated.getAbyssRank().getRank().getPointsLost() * defeated.getRates().getApLostPlayerRate());

        // Level penalty calculation
        int difference = winner.getLevel() - defeated.getLevel();

        if (difference > 4) {
            pointsLost = Math.round(pointsLost * 0.1f);
        } else {
            switch (difference) {
                case 3:
                    pointsLost = Math.round(pointsLost * 0.85f);
                    break;
                case 4:
                    pointsLost = Math.round(pointsLost * 0.65f);
                    break;
            }
        }
        return pointsLost;
    }

    /**
     * @param defeated
     * @param winner
     * @return Points Gained in PvP Kill
     */
    public static int calculatePvpApGained(Player defeated, int maxRank, int maxLevel) {
        int pointsGained = Math.round(defeated.getAbyssRank().getRank().getPointsGained());

        // Level penalty calculation
        int difference = maxLevel - defeated.getLevel();

        if (difference > 4) {
            pointsGained = Math.round(pointsGained * 0.1f);
        } else if (difference < -3) {
            pointsGained = Math.round(pointsGained * 1.3f);
        } else {
            switch (difference) {
                case 3:
                    pointsGained = Math.round(pointsGained * 0.85f);
                    break;
                case 4:
                    pointsGained = Math.round(pointsGained * 0.65f);
                    break;
                case -2:
                    pointsGained = Math.round(pointsGained * 1.1f);
                    break;
                case -3:
                    pointsGained = Math.round(pointsGained * 1.2f);
                    break;
            }
        }

        // Abyss rank penalty calculation
        int winnerAbyssRank = maxRank;
        int defeatedAbyssRank = defeated.getAbyssRank().getRank().getId();
        int abyssRankDifference = winnerAbyssRank - defeatedAbyssRank;

        if (winnerAbyssRank <= 7 && abyssRankDifference > 0) {
            float penaltyPercent = abyssRankDifference * 0.05f;

            pointsGained -= Math.round(pointsGained * penaltyPercent);
        }

        return pointsGained;
    }

    /**
     * @param player
     * @param target
     * @return DP reward
     */
    public static int calculateGroupDPReward(Player player, Creature target) {
        int playerLevel = player.getCommonData().getLevel();
        int targetLevel = target.getLevel();
        NpcRank npcRank = ((Npc) target).getObjectTemplate().getRank();

        //TODO: fix to see monster Rank level, NORMAL lvl 1, 2 | ELITE lvl 1, 2 etc..
        int baseDP = targetLevel * calculateRankMultipler(npcRank);

        int xpPercentage = XPRewardEnum.xpRewardFrom(targetLevel - playerLevel);

        return (int) Math.floor(baseDP * xpPercentage * player.getRates().getGroupDpRate() / 100);
    }

    /**
     * Hate based on BOOST_HATE stat
     * Now used only from skills, probably need to use for regular attack
     *
     * @param creature
     * @param value
     * @return
     */
    public static int calculateHate(Creature creature, int value) {
        return Math.round(value * creature.getGameStats().getCurrentStat(StatEnum.BOOST_HATE) / 100f);
    }

    /**
     * @param player
     * @param target
     * @return Damage made to target (-hp value)
     */
    public static int calculateBaseDamageToTarget(Creature attacker, Creature target) {
        return calculatePhysicDamageToTarget(attacker, target, 0);
    }

    /**
     * @param player
     * @param target
     * @param skillDamages
     * @return Damage made to target (-hp value)
     */
    public static int calculatePhysicDamageToTarget(Creature attacker, Creature target, int skillDamages) {
        CreatureGameStats<?> ags = attacker.getGameStats();
        CreatureGameStats<?> tgs = target.getGameStats();

        int resultDamage = 0;

        if (attacker instanceof Player) {
            int totalMin = ags.getCurrentStat(StatEnum.MIN_DAMAGES);
            int totalMax = ags.getCurrentStat(StatEnum.MAX_DAMAGES);
            int average = Math.round((totalMin + totalMax) / 2);
            int mainHandAttack = ags.getBaseStat(StatEnum.MAIN_HAND_POWER);

            Equipment equipment = ((Player) attacker).getEquipment();

            WeaponType weaponType = equipment.getMainHandWeaponType();

            if (weaponType != null) {
                if (average < 1) {
                    average = 1;
                    log.warn("Weapon stat MIN_MAX_DAMAGE resulted average zero in main-hand calculation");
                    log.warn("Weapon ID: " + String.valueOf(equipment.getMainHandWeapon().getItemTemplate().getTemplateId()));
                    log.warn("MIN_DAMAGE = " + String.valueOf(totalMin));
                    log.warn("MAX_DAMAGE = " + String.valueOf(totalMax));
                }

                //TODO move to controller
                if (weaponType == WeaponType.BOW)
                    equipment.useArrow();

                int min = Math.round((((mainHandAttack * 100) / average) * totalMin) / 100);
                int max = Math.round((((mainHandAttack * 100) / average) * totalMax) / 100);

                int base = Rnd.get(min, max);


                resultDamage = Math.round((base * (ags.getCurrentStat(StatEnum.POWER) * 0.01f + (ags.getBaseStat(StatEnum.MAIN_HAND_POWER) * 0.2f) * 0.01f))
                        + ags.getStatBonus(StatEnum.MAIN_HAND_POWER) + skillDamages);

            } else   //if hand attack
            {
                int base = Rnd.get(16, 20);
                resultDamage = Math.round(base * (ags.getCurrentStat(StatEnum.POWER) * 0.01f));
            }

            //adjusting baseDamages according to attacker and target level
            //
            resultDamage = adjustDamages(attacker, target, resultDamage);
            if (attacker.isInState(CreatureState.POWERSHARD)) {
                Item mainHandPowerShard = equipment.getMainHandPowerShard();
                if (mainHandPowerShard != null) {
                    resultDamage += mainHandPowerShard.getItemTemplate().getWeaponBoost();

                    equipment.usePowerShard(mainHandPowerShard, 1);
                }
            }
        } else if (attacker instanceof Summon) {
            int baseDamage = ags.getCurrentStat(StatEnum.MAIN_HAND_POWER);
            int max = ((baseDamage * attacker.getLevel()) / 10);
            resultDamage += Rnd.get(baseDamage, max);
            resultDamage = adjustDamages(attacker, target, resultDamage);
        } else {
            NpcRank npcRank = ((Npc) attacker).getObjectTemplate().getRank();
            double multipler = calculateRankMultipler(npcRank);
            double hpGaugeMod = 1 + (((Npc) attacker).getObjectTemplate().getHpGauge() / 10);
            int baseDamage = ags.getCurrentStat(StatEnum.MAIN_HAND_POWER);
            int max = (int) ((baseDamage * multipler * hpGaugeMod) + ((baseDamage * attacker.getLevel()) / 10));
            int min = max - ags.getCurrentStat(StatEnum.MAIN_HAND_POWER);
            resultDamage += Rnd.get(min, max);
        }

        resultDamage -= Math.round(tgs.getCurrentStat(StatEnum.PHYSICAL_DEFENSE) * 0.10f);

        if (resultDamage <= 0)
            resultDamage = 1;

        return resultDamage;
    }

    /**
     * @param attacker
     * @param target
     * @return
     */
    public static int calculateOffHandPhysicDamageToTarget(Creature attacker, Creature target) {
        CreatureGameStats<?> ags = attacker.getGameStats();
        CreatureGameStats<?> tgs = target.getGameStats();

        int totalMin = ags.getCurrentStat(StatEnum.MIN_DAMAGES);
        int totalMax = ags.getCurrentStat(StatEnum.MAX_DAMAGES);
        int average = Math.round((totalMin + totalMax) / 2);
        int offHandAttack = ags.getBaseStat(StatEnum.OFF_HAND_POWER);

        Equipment equipment = ((Player) attacker).getEquipment();

        if (average < 1) {
            average = 1;
            log.warn("Weapon stat MIN_MAX_DAMAGE resulted average zero in off-hand calculation");
            log.warn("Weapon ID: " + String.valueOf(equipment.getOffHandWeapon().getItemTemplate().getTemplateId()));
            log.warn("MIN_DAMAGE = " + String.valueOf(totalMin));
            log.warn("MAX_DAMAGE = " + String.valueOf(totalMax));
        }

        int Damage = 0;
        int min = Math.round((((offHandAttack * 100) / average) * totalMin) / 100);
        int max = Math.round((((offHandAttack * 100) / average) * totalMax) / 100);

        int base = Rnd.get(min, max);
        Damage = Math.round((base * (ags.getCurrentStat(StatEnum.POWER) * 0.01f + (ags.getBaseStat(StatEnum.OFF_HAND_POWER) * 0.2f) * 0.01f))
                + ags.getStatBonus(StatEnum.OFF_HAND_POWER));

        Damage = adjustDamages(attacker, target, Damage);

        if (attacker.isInState(CreatureState.POWERSHARD)) {
            Item offHandPowerShard = equipment.getOffHandPowerShard();
            if (offHandPowerShard != null) {
                Damage += offHandPowerShard.getItemTemplate().getWeaponBoost();
                equipment.usePowerShard(offHandPowerShard, 1);
            }
        }

        Damage -= Math.round(tgs.getCurrentStat(StatEnum.PHYSICAL_DEFENSE) * 0.10f);

        for (float i = 0.25f; i <= 1; i += 0.25f) {
            if (Rnd.get(0, 100) < 50) {
                Damage *= i;
                break;
            }
        }

        if (Damage <= 0)
            Damage = 1;

        return Damage;
    }


    /**
     * @param player
     * @param target
     * @param skillEffectTemplate
     * @return HP damage to target
     */
    public static int calculateMagicDamageToTarget(Creature speller, Creature target, int baseDamages, SkillElement element) {
        CreatureGameStats<?> sgs = speller.getGameStats();
        CreatureGameStats<?> tgs = target.getGameStats();

        int totalBoostMagicalSkill = 0;

        totalBoostMagicalSkill += sgs.getCurrentStat(StatEnum.BOOST_MAGICAL_SKILL);

        if (totalBoostMagicalSkill > 2600)
            totalBoostMagicalSkill = 2600;

        int damages = Math.round(baseDamages * ((sgs.getCurrentStat(StatEnum.KNOWLEDGE) / 100f) + (totalBoostMagicalSkill / 1000f)));

        //adjusting baseDamages according to attacker and target level
        //
        damages = adjustDamages(speller, target, damages);

        // element resist: fire, wind, water, eath
        //
        // 10 elemental resist ~ 1% reduce of magical baseDamages
        //
        damages = Math.round(damages * (1 - tgs.getMagicalDefenseFor(element) / 1000f));

        // IMPORTANT NOTES
        //
        // magicalResistance supposed to be counted to EVADE magic, not to reduce damage, only the elementaryDefense it's counted to reduce magic attack
        //
        //     so example if 200 magic resist vs 100 magic accuracy, 200 - 100 = 100/10 = 0.10 or 10% chance of EVADE
        //
        // damages -= Math.round((elementaryDefense+magicalResistance)*0.60f);

        if (damages <= 0) {
            damages = 1;
        }

        return damages;
    }

    /**
     * @param npcRank
     * @return
     */
    public static int calculateRankMultipler(NpcRank npcRank) {
        //FIXME: to correct formula, have any reference?
        int multipler;
        switch (npcRank) {
            case JUNK:
                multipler = 2;
                break;
            case NORMAL:
                multipler = 2;
                break;
            case ELITE:
                multipler = 3;
                break;
            case HERO:
                multipler = 4;
                break;
            case LEGENDARY:
                multipler = 5;
                break;
            default:
                multipler = 1;
        }

        return multipler;
    }
    
    /**
	 * @param playerRank
	 * @return
	 */
	private static int calculateRankPVPMultipler(AbyssRankEnum playerRank)
	{
		 //FIXME: to correct formula, have any reference?
		 int PVPmultipler;
	        switch (playerRank) {
	            case GRADE9_SOLDIER:
	            	PVPmultipler = 1;
	                break;
	            case GRADE8_SOLDIER:
	            	PVPmultipler = 2;
	                break;
	            case GRADE7_SOLDIER:
	            	PVPmultipler = 3;
	                break;
	            case GRADE6_SOLDIER:
	            	PVPmultipler = 4;
	                break;
	            case GRADE5_SOLDIER:
	            	PVPmultipler = 5;
	                break;
	            case GRADE4_SOLDIER:
	            	PVPmultipler = 6;
	                break;
	            case GRADE3_SOLDIER:
	            	PVPmultipler = 7;
	                break;
	            case GRADE2_SOLDIER:
	            	PVPmultipler = 8;
	                break;
	            case GRADE1_SOLDIER:
	            	PVPmultipler = 9;
	                break;
	            case STAR1_OFFICER:
	            	PVPmultipler = 10;
	                break;
	            case STAR2_OFFICER:
	            	PVPmultipler = 11;
	                break;
	            case STAR3_OFFICER:
	            	PVPmultipler = 12;
	                break;
	            case STAR4_OFFICER:
	            	PVPmultipler = 13;
	                break;
	            case STAR5_OFFICER:
	            	PVPmultipler = 14;
	                break;
	            case GENERAL:
	            	PVPmultipler = 15;
	                break;
	            case GREAT_GENERAL:
	            	PVPmultipler = 16;
	                break;
	            case COMMANDER:
	            	PVPmultipler = 17;
	                break;
	            case SUPREME_COMMANDER:
	            	PVPmultipler = 18;
	                break;
	            default:
	            	PVPmultipler = 1;
	        }

	        return PVPmultipler;
	}


    /**
     * adjust baseDamages according to their level || is PVP?
     *
     * @param attacker    lvl
     * @param target      lvl
     * @param baseDamages
     * @ref:
     */
    public static int adjustDamages(Creature attacker, Creature target, int Damages) {

        int attackerLevel = attacker.getLevel();
        int targetLevel = target.getLevel();
        int baseDamages = Damages;

        //fix this for better monster target condition please
        if ((attacker instanceof Player) && !(target instanceof Player)) {

            if (targetLevel > attackerLevel) {

                float multipler = 0.0f;
                int differ = (targetLevel - attackerLevel);

                if (differ <= 2) {
                    return baseDamages;
                } else if (differ > 2 && differ < 10) {
                    multipler = (differ - 2f) / 10f;
                    baseDamages -= Math.round((baseDamages * multipler));
                } else {
                    baseDamages -= Math.round((baseDamages * 0.80f));
                }

                return baseDamages;
            }
        } //end of damage to monster

        //PVP damages is capped of 60% of the actual baseDamage
        else if (((attacker instanceof Summon) || (attacker instanceof Player)) && (target instanceof Player)) {
            baseDamages = Math.round(baseDamages * 0.60f);
            float pvpAttackBonus = attacker.getGameStats().getCurrentStat(StatEnum.PVP_ATTACK_RATIO) * 0.001f;
            float pvpDefenceBonus = target.getGameStats().getCurrentStat(StatEnum.PVP_DEFEND_RATIO) * 0.001f;
            baseDamages = Math.round(baseDamages + (baseDamages * pvpAttackBonus) - (baseDamages * pvpDefenceBonus));
            return baseDamages;
        }

        return baseDamages;

    }

    /**
     * Calculates DODGE chance
     *
     * @param attacker
     * @param attacked
     * @return int
     */
    public static int calculatePhysicalDodgeRate(Creature attacker, Creature attacked) {
        //check always dodge
        if (attacked.getObserveController().checkAttackStatus(AttackStatus.DODGE))
            return 100;

        int accuracy;

        if (attacker instanceof Player && ((Player) attacker).getEquipment().getOffHandWeaponType() != null)
            accuracy = Math.round((attacker.getGameStats().getCurrentStat(StatEnum.MAIN_HAND_ACCURACY) + attacker
                    .getGameStats().getCurrentStat(StatEnum.OFF_HAND_ACCURACY)) / 2);
        else
            accuracy = attacker.getGameStats().getCurrentStat(StatEnum.MAIN_HAND_ACCURACY);

        int dodgeRate = (attacked.getGameStats().getCurrentStat(StatEnum.EVASION) - accuracy) / 10;
        // maximal dodge rate
        if (dodgeRate > 30)
            dodgeRate = 30;

        if (dodgeRate <= 0)
            return 1;

        return dodgeRate;
    }

    /**
     * Calculates PARRY chance
     *
     * @param attacker
     * @param attacked
     * @return int
     */
    public static int calculatePhysicalParryRate(Creature attacker, Creature attacked) {
        //check always parry
        if (attacked.getObserveController().checkAttackStatus(AttackStatus.PARRY))
            return 100;

        int accuracy;

        if (attacker instanceof Player && ((Player) attacker).getEquipment().getOffHandWeaponType() != null)
            accuracy = Math.round((attacker.getGameStats().getCurrentStat(StatEnum.MAIN_HAND_ACCURACY) + attacker
                    .getGameStats().getCurrentStat(StatEnum.OFF_HAND_ACCURACY)) / 2);
        else
            accuracy = attacker.getGameStats().getCurrentStat(StatEnum.MAIN_HAND_ACCURACY);

        int parryRate = (attacked.getGameStats().getCurrentStat(StatEnum.PARRY) - accuracy) / 10;
        // maximal parry rate
        if (parryRate > 40)
            parryRate = 40;

        if (parryRate <= 0)
            return 1;

        return parryRate;
    }

    /**
     * Calculates BLOCK chance
     *
     * @param attacker
     * @param attacked
     * @return int
     */
    public static int calculatePhysicalBlockRate(Creature attacker, Creature attacked) {
        //check always block
        if (attacked.getObserveController().checkAttackStatus(AttackStatus.BLOCK))
            return 100;

        int accuracy;

        if (attacker instanceof Player && ((Player) attacker).getEquipment().getOffHandWeaponType() != null)
            accuracy = Math.round((attacker.getGameStats().getCurrentStat(StatEnum.MAIN_HAND_ACCURACY) + attacker
                    .getGameStats().getCurrentStat(StatEnum.OFF_HAND_ACCURACY)) / 2);
        else
            accuracy = attacker.getGameStats().getCurrentStat(StatEnum.MAIN_HAND_ACCURACY);

        int blockRate = (attacked.getGameStats().getCurrentStat(StatEnum.BLOCK) - accuracy) / 10;
        // maximal block rate
        if (blockRate > 50)
            blockRate = 50;

        if (blockRate <= 0)
            return 1;

        return blockRate;
    }

    /**
     * Calculates CRITICAL chance
     *
     * @param attacker
     * @return double
     */
    public static double calculatePhysicalCriticalRate(Creature attacker, Creature attacked) {
        int critical;

        // check always critical
        if (attacker.getObserveController().checkAttackerStatus(AttackStatus.CRITICAL))
            return 100;

        if (attacker instanceof Player && ((Player) attacker).getEquipment().getOffHandWeaponType() != null)
            critical = Math.round(((attacker.getGameStats().getCurrentStat(StatEnum.MAIN_HAND_CRITICAL) + attacker
                    .getGameStats().getCurrentStat(StatEnum.OFF_HAND_CRITICAL)) / 2) - attacked.getGameStats().getCurrentStat(StatEnum.PHYSICAL_CRITICAL_RESIST));
        else
            critical = attacker.getGameStats().getCurrentStat(StatEnum.MAIN_HAND_CRITICAL) - attacked.getGameStats().getCurrentStat(StatEnum.PHYSICAL_CRITICAL_RESIST);


        double criticalRate;

        if (critical <= 440)
            criticalRate = critical * 0.1f;
        else if (critical <= 600)
            criticalRate = (440 * 0.1f) + ((critical - 440) * 0.05f);
        else
            criticalRate = (440 * 0.1f) + (160 * 0.05f) + ((critical - 600) * 0.02f);
        // minimal critical rate
        if (criticalRate < 1)
            criticalRate = 1;

        return criticalRate;
    }

    /**
     * Calculates MAGIC CRITICAL chance
     *
     * @param attacker
     * @return double
     */
    public static double calculateMagicCriticalRate(Creature attacker, Creature attacked) {
        int critical;

        // check always critical
        if (attacker.getObserveController().checkAttackerStatus(AttackStatus.CRITICAL))
            return 100;

        critical = attacker.getGameStats().getCurrentStat(StatEnum.MAGICAL_CRITICAL) - attacked.getGameStats().getCurrentStat(StatEnum.MAGICAL_CRITICAL_RESIST);

        double criticalRate;

        if (critical <= 440)
            criticalRate = critical * 0.1f;
        else if (critical <= 600)
            criticalRate = (440 * 0.1f) + ((critical - 440) * 0.05f);
        else
            criticalRate = (440 * 0.1f) + (160 * 0.05f) + ((critical - 600) * 0.02f);
        // minimal critical rate
        if (criticalRate < 1)
            criticalRate = 1;

        return criticalRate;
    }

    /**
     * Calculates RESIST chance
     *
     * @param attacker
     * @param attacked
     * @return int
     */
    public static int calculateMagicalResistRate(Creature attacker, Creature attacked) {
        if (attacked.getObserveController().checkAttackStatus(AttackStatus.RESIST))
            return 100;

        int stat_res = attacked.getGameStats().getCurrentStat(StatEnum.MAGICAL_RESIST);
        int stat_acc = attacker.getGameStats().getCurrentStat(StatEnum.MAGICAL_ACCURACY);

        int attackerLevel = attacker.getLevel();
        int targetLevel = attacked.getLevel();

        int resist = (stat_res - stat_acc) / 10;

        if ((targetLevel - attackerLevel) > 2)
            resist += (targetLevel - attackerLevel - 2) * 10;

        //TODO some skills have higher resist

        if (resist <= 0)//cant resist
            return 0;
        else if (resist > 95)//hardcap 95%
            return 95;
        else
            return resist;
    }

    /**
     * Calculates the fall damage
     *
     * @param player
     * @param distance
     * @return True if the player is forced to his bind location.
     */
    public static boolean calculateFallDamage(Player player, float distance) {
        if (player.isInvul()) {
            return false;
        }

        if (distance >= FallDamageConfig.MAXIMUM_DISTANCE_DAMAGE) {
            player.getController().onStopMove();
            player.getFlyController().onStopGliding();
            player.getController().onDie(player);

            player.getReviveController().bindRevive();
            return true;
        } else if (distance >= FallDamageConfig.MINIMUM_DISTANCE_DAMAGE) {
            float dmgPerMeter = player.getLifeStats().getMaxHp() * FallDamageConfig.FALL_DAMAGE_PERCENTAGE / 100f;
            int damage = (int) (distance * dmgPerMeter);

            player.getLifeStats().reduceHp(damage, player);
            PacketSendUtility.sendPacket(player, new SM_ATTACK_STATUS(player, SM_ATTACK_STATUS.TYPE.DAMAGE, 0, damage));
        }

        return false;
    }

}
