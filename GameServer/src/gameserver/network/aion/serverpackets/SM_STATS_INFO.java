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

package gameserver.network.aion.serverpackets;

import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.PlayerCommonData;
import gameserver.model.gameobjects.stats.PlayerGameStats;
import gameserver.model.gameobjects.stats.PlayerLifeStats;
import gameserver.model.gameobjects.stats.StatEnum;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.utils.gametime.GameTimeManager;

import java.nio.ByteBuffer;

/**
 * In this packet Server is sending User Info?
 *
 * @author -Nemesiss-, Luno
 */
public class SM_STATS_INFO extends AionServerPacket {
    /**
     * Player that stats info will be send
     */
    private Player player;
    private PlayerGameStats pgs;
    private PlayerLifeStats pls;
    private PlayerCommonData pcd;

    /**
     * Constructs new <tt>SM_UI</tt> packet
     *
     * @param player
     */
    public SM_STATS_INFO(Player player) {
        this.player = player;
        this.pcd = player.getCommonData();
        this.pgs = player.getGameStats();
        this.pls = player.getLifeStats();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeD(buf, player.getObjectId());
        writeD(buf, GameTimeManager.getGameTime().getTime()); // Minutes since 1/1/00 00:00:00

        writeH(buf, pgs.getCurrentStat(StatEnum.POWER));// [current power]
        writeH(buf, pgs.getCurrentStat(StatEnum.HEALTH));// [current health]
        writeH(buf, pgs.getCurrentStat(StatEnum.ACCURACY));// [current accuracy]
        writeH(buf, pgs.getCurrentStat(StatEnum.AGILITY));// [current agility]
        writeH(buf, pgs.getCurrentStat(StatEnum.KNOWLEDGE));// [current knowledge]
        writeH(buf, pgs.getCurrentStat(StatEnum.WILL));// [current will]

        writeH(buf, pgs.getCurrentStat(StatEnum.WATER_RESISTANCE));// [current water]
        writeH(buf, pgs.getCurrentStat(StatEnum.WIND_RESISTANCE));// [current wind]
        writeH(buf, pgs.getCurrentStat(StatEnum.EARTH_RESISTANCE));// [current earth]
        writeH(buf, pgs.getCurrentStat(StatEnum.FIRE_RESISTANCE));// [current fire]
        writeH(buf, pgs.getCurrentStat(StatEnum.ELEMENTAL_RESISTANCE_LIGHT)); // [current light]
        writeH(buf, pgs.getCurrentStat(StatEnum.ELEMENTAL_RESISTANCE_DARK)); // [current dark]

        writeH(buf, player.getLevel());// [level]

        // something like very dynamic
        writeH(buf, 0); // [unk]
        writeH(buf, 0);// [unk]
        writeH(buf, 0);// [unk]

        writeQ(buf, pcd.getExpNeed());// [xp till next lv]
        writeQ(buf, pcd.getExpRecoverable()); // [recoverable exp]
        writeQ(buf, pcd.getExpShown()); // [current xp]

        writeD(buf, 0); // [unk]
        writeD(buf, pgs.getCurrentStat(StatEnum.MAXHP)); // [max hp]
        writeD(buf, pls.getCurrentHp());// [current hp]

        writeD(buf, pgs.getCurrentStat(StatEnum.MAXMP));// [max mana]
        writeD(buf, pls.getCurrentMp());// [current mana]

        writeH(buf, pgs.getCurrentStat(StatEnum.MAXDP));// [max dp]
        writeH(buf, pcd.getDp());// [current dp]

        writeD(buf, pgs.getCurrentStat(StatEnum.FLY_TIME));// [max fly time]

        writeD(buf, pls.getCurrentFp());// [current fly time]

        writeC(buf, player.getFlyState());// [fly state]
        writeC(buf, 0);// [unk]

        writeH(buf, pgs.getCurrentStat(StatEnum.MAIN_HAND_POWER)); // [current main hand attack]

        writeH(buf, pgs.getCurrentStat(StatEnum.OFF_HAND_POWER)); // [off hand attack]

        writeH(buf, pgs.getCurrentStat(StatEnum.PHYSICAL_DEFENSE));// [current pdef]

        writeH(buf, pgs.getCurrentStat(StatEnum.MAGICAL_ATTACK));// [current magic attack ?]

        writeH(buf, pgs.getCurrentStat(StatEnum.MAGICAL_RESIST)); // [current mres]

        writeF(buf, pgs.getCurrentStat(StatEnum.ATTACK_RANGE) / 1000f);// attack range
        writeH(buf, pgs.getCurrentStat(StatEnum.ATTACK_SPEED));// attack speed
        writeH(buf, pgs.getCurrentStat(StatEnum.EVASION));// [current evasion]
        writeH(buf, pgs.getCurrentStat(StatEnum.PARRY));// [current parry]
        writeH(buf, pgs.getCurrentStat(StatEnum.BLOCK));// [current block]

        writeH(buf, pgs.getCurrentStat(StatEnum.MAIN_HAND_CRITICAL));// [current main hand crit rate]
        writeH(buf, pgs.getCurrentStat(StatEnum.OFF_HAND_CRITICAL));// [current off hand crit rate]

        writeH(buf, pgs.getCurrentStat(StatEnum.MAIN_HAND_ACCURACY));// [current main_hand_accuracy]
        writeH(buf, pgs.getCurrentStat(StatEnum.OFF_HAND_ACCURACY));// [current off_hand_accuracy]

        writeH(buf, 0);// [unk]
        writeH(buf, pgs.getCurrentStat(StatEnum.MAGICAL_ACCURACY));// [current magic accuracy]
        writeH(buf, pgs.getCurrentStat(StatEnum.MAGICAL_CRITICAL)); // [current crit spell]

        writeH(buf, 0);// [unk]

        writeF(buf, (pgs.getBaseStat(StatEnum.BOOST_CASTING_TIME) -
            pgs.getCurrentStat(StatEnum.BOOST_CASTING_TIME)) / 100f + 1); // [current boost casting time]

        writeH(buf, 40);// [unk] 1.9 version

        // FIXME: TempFix MBoost Cap 2600
        int totalBoostMagicalSkill = pgs.getCurrentStat(StatEnum.BOOST_MAGICAL_SKILL);
        if (totalBoostMagicalSkill > 2600)
            totalBoostMagicalSkill = 2600;
        writeH(buf, totalBoostMagicalSkill); // [current magic boost] 1.9 version

        writeH(buf, pgs.getCurrentStat(StatEnum.BOOST_HEAL)); // [current boost_heal]
        writeH(buf, pgs.getCurrentStat(StatEnum.PHYSICAL_CRITICAL_RESIST)); // [current strike resist]
        writeH(buf, pgs.getCurrentStat(StatEnum.MAGICAL_CRITICAL_RESIST));// [current spell resist]
        writeH(buf, pgs.getCurrentStat(StatEnum.PHYSICAL_CRITICAL_DAMAGE_REDUCE));// [current strike fortitude]
        writeH(buf, pgs.getCurrentStat(StatEnum.MAGICAL_CRITICAL_DAMAGE_REDUCE));// [current spell fortitude]
        writeH(buf, 20511);// [unk] 1.9 version

        writeD(buf, (27 + (player.getCubeSize() * 9)));// [unk]

        writeD(buf, player.getInventory().size());// [unk]
        writeD(buf, 0);// [unk]
        writeD(buf, 0);// [unk]
        writeD(buf, pcd.getPlayerClass().getClassId());// [Player Class id]

        writeQ(buf, 0);// [unk] 1.9 version
        writeQ(buf, 0);// Current energy of repose 1.9
        writeQ(buf, 251141);// Max energy of repose 1.9
        writeQ(buf, 0);// [unk] 1.9 version

        //writeQ(buf, 4020244);// [current energy of repose]
        //writeQ(buf, 4720968);// [max energy of repose]

        writeH(buf, pgs.getBaseStat(StatEnum.POWER));// [base power]
        writeH(buf, pgs.getBaseStat(StatEnum.HEALTH));// [base health]

        writeH(buf, pgs.getBaseStat(StatEnum.ACCURACY));// [base accuracy]
        writeH(buf, pgs.getBaseStat(StatEnum.AGILITY));// [base agility]

        writeH(buf, pgs.getBaseStat(StatEnum.KNOWLEDGE));// [base knowledge]
        writeH(buf, pgs.getBaseStat(StatEnum.WILL));// [base will]

        writeH(buf, pgs.getBaseStat(StatEnum.WATER_RESISTANCE));// [base water res]
        writeH(buf, pgs.getBaseStat(StatEnum.WIND_RESISTANCE));// [base wind res]

        writeH(buf, pgs.getBaseStat(StatEnum.EARTH_RESISTANCE));// [base earth resist]
        writeH(buf, pgs.getBaseStat(StatEnum.FIRE_RESISTANCE));// [base fire res]

        writeD(buf, 0);// [unk]

        writeD(buf, pgs.getBaseStat(StatEnum.MAXHP));// [base hp]

        writeD(buf, pgs.getBaseStat(StatEnum.MAXMP));// [base mana]

        writeD(buf, pgs.getBaseStat(StatEnum.MAXDP));// [base dp]
        writeD(buf, pgs.getBaseStat(StatEnum.FLY_TIME));// [fly time]

        writeH(buf, pgs.getBaseStat(StatEnum.MAIN_HAND_POWER));// [base main hand attack]
        writeH(buf, pgs.getBaseStat(StatEnum.OFF_HAND_POWER));// [base off hand attack]

        writeH(buf, pgs.getBaseStat(StatEnum.MAGICAL_ATTACK)); // [base magic attack ?]
        writeH(buf, pgs.getBaseStat(StatEnum.PHYSICAL_DEFENSE)); // [base pdef]

        writeH(buf, pgs.getBaseStat(StatEnum.MAGICAL_RESIST)); // [base magic res]

        writeH(buf, 0); // [unk]

        writeF(buf, pgs.getBaseStat(StatEnum.ATTACK_RANGE) / 1000f);// [current attack range]

        writeH(buf, pgs.getBaseStat(StatEnum.EVASION)); // [base evasion]

        writeH(buf, pgs.getBaseStat(StatEnum.PARRY)); // [base parry]

        writeH(buf, pgs.getBaseStat(StatEnum.BLOCK)); // [base block]

        writeH(buf, pgs.getBaseStat(StatEnum.MAIN_HAND_CRITICAL)); // [base main hand crit rate]
        writeH(buf, pgs.getBaseStat(StatEnum.OFF_HAND_CRITICAL)); // [base off hand crit rate]

        writeH(buf, pgs.getBaseStat(StatEnum.MAGICAL_CRITICAL)); // [base MAGICAL crit rate]
        writeH(buf, 0); // [unk] VERSION 1.9

        writeH(buf, pgs.getBaseStat(StatEnum.MAIN_HAND_ACCURACY)); // [base main hand accuracy]
        writeH(buf, pgs.getBaseStat(StatEnum.OFF_HAND_ACCURACY)); // [base off hand accuracy]

        writeH(buf, 0); // [base Casting speed] VERSION 1.9

        writeH(buf, pgs.getBaseStat(StatEnum.MAGICAL_ACCURACY));// [base magic accuracy]

        writeH(buf, 0); // [base concentration]
        writeH(buf, pgs.getBaseStat(StatEnum.MAGICAL_ATTACK) + pgs.getBaseStat(StatEnum.BOOST_MAGICAL_SKILL));// [base magic boost]

        writeH(buf, pgs.getBaseStat(StatEnum.BOOST_HEAL)); // [base boostheal]
        writeH(buf, pgs.getBaseStat(StatEnum.PHYSICAL_CRITICAL_RESIST)); // [base strike resist]
        writeH(buf, pgs.getBaseStat(StatEnum.MAGICAL_CRITICAL_RESIST));// [base spell resist]
        writeH(buf, pgs.getBaseStat(StatEnum.PHYSICAL_CRITICAL_DAMAGE_REDUCE)); // [base strike fortitude]
        writeH(buf, pgs.getBaseStat(StatEnum.MAGICAL_CRITICAL_DAMAGE_REDUCE)); // [base spell fortitude]
    }
}
