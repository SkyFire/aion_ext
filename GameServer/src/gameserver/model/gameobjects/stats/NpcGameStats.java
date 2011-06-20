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

import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.stats.modifiers.StatModifier;
import gameserver.model.items.ItemSlot;
import gameserver.model.items.NpcEquippedGear;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.model.templates.stats.NpcStatsTemplate;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.utils.PacketSendUtility;

import java.util.TreeSet;

/**
 * @author xavier
 */
public class NpcGameStats extends CreatureGameStats<Npc> {
    int currentRunSpeed = 0;

    public NpcGameStats(Npc owner) {
        super(owner);
        // TODO set other stats
        NpcStatsTemplate nst = owner.getObjectTemplate().getStatsTemplate();

        initStat(StatEnum.MAXHP, nst.getMaxHp()
                + Math.round((owner.getObjectTemplate().getHpGauge() * 1.5f) * owner.getLevel()));
        initStat(StatEnum.MAXMP, nst.getMaxMp());
        // TODO: Npc Attack Speed
        // initStat(StatEnum.ATTACK_SPEED, Math.round(nst.getAttackSpeed() * 1000));
        initStat(StatEnum.ATTACK_SPEED, 2000);
        initStat(StatEnum.PHYSICAL_DEFENSE, Math.round(((nst.getPdef() / owner.getLevel()) - 1) * nst.getPdef()
                + 10 * owner.getLevel()));
        initStat(StatEnum.EVASION, Math.round(nst.getEvasion() * 2.3f + owner.getLevel() * 10));
        initStat(StatEnum.MAGICAL_RESIST, Math.round(nst.getMdef()));
        initStat(StatEnum.MAIN_HAND_POWER, nst.getPower());
        initStat(StatEnum.MAIN_HAND_ACCURACY, Math.round(nst.getAccuracy() * 2.3f + owner.getLevel() * 10));
        initStat(StatEnum.MAIN_HAND_CRITICAL, Math.round(nst.getCrit()));
        initStat(StatEnum.SPEED, Math.round(nst.getRunSpeedFight() * 1000));
        initStat(StatEnum.WALK, Math.round(nst.getWalkSpeed() * 1000));
        initStat(StatEnum.MAGICAL_ACCURACY, 1500);
        initStat(StatEnum.BOOST_MAGICAL_SKILL, 1000);

        initStatsFromEquipment(owner);
    }

    /**
     * I hope one day we will have all stats from equip applied automatically
     *
     * @param owner
     */
    private void initStatsFromEquipment(Npc owner) {
        NpcEquippedGear equipment = owner.getObjectTemplate().getEquipment();
        if (equipment != null) {
            equipment.init();

            ItemTemplate itemTemplate = equipment.getItem(ItemSlot.MAIN_HAND);
            if (itemTemplate != null) {
                TreeSet<StatModifier> modifiers = itemTemplate.getModifiers();
                if (modifiers != null) {
                    for (StatModifier modifier : modifiers) {
                        if (modifier.getStat() == StatEnum.ATTACK_RANGE)
                            initStat(StatEnum.ATTACK_RANGE, modifier.apply(0, 0));
                    }
                }
            }
        }

        /**
         * ATTACK_RANGE set to 2000 if no weapon & no arange value.
         */
        int newArange = Math.round((owner.getObjectTemplate().getAttackRange() * 1000));
        if (newArange == 0) {
            newArange = 2000;
        }
        if (getCurrentStat(StatEnum.ATTACK_RANGE) == 0 || getCurrentStat(StatEnum.ATTACK_RANGE) < newArange)
            initStat(StatEnum.ATTACK_RANGE, newArange);
    }

    public void recomputeStats() {
        super.recomputeStats();

        int newRunSpeed = getCurrentStat(StatEnum.SPEED);

        if (newRunSpeed != currentRunSpeed) {
            owner.getMoveController().setSpeed(newRunSpeed / 1000f);
            PacketSendUtility.broadcastPacket(owner, new SM_EMOTION(owner, EmotionType.START_EMOTE2, 0, 0));
        }
        currentRunSpeed = newRunSpeed;
	}
}