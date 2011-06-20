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
package gameserver.skillengine.effect;

import gameserver.controllers.movement.ActionObserver;
import gameserver.controllers.movement.ActionObserver.ObserverType;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.state.CreatureState;
import gameserver.model.gameobjects.stats.CreatureGameStats;
import gameserver.model.gameobjects.stats.id.SkillEffectId;
import gameserver.model.gameobjects.stats.modifiers.*;
import gameserver.network.aion.serverpackets.SM_STATS_INFO;
import gameserver.skillengine.change.Change;
import gameserver.skillengine.model.Effect;
import gameserver.utils.PacketSendUtility;

import org.apache.log4j.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import java.util.TreeSet;

/**
 * @author ZeroSignal
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StatswitchEffect")
public class StatswitchEffect extends BufEffect {
    private static final Logger log = Logger.getLogger(StatswitchEffect.class);

    @Override
    public void calculate(Effect effect) {
        if (calculateEffectResistRate(effect, null))
            effect.addSucessEffect(this);
    }

    /**
     * Will be called from effect controller when effect starts
     */
    @Override
    public void startEffect(Effect effect) {
        if (change == null)
            return;

        Creature effector = effect.getEffector();
        Creature effected = effect.getEffected();
        
        CreatureGameStats<? extends Creature> effectorCgs = effector.getGameStats();
        CreatureGameStats<? extends Creature> effectedCgs = effected.getGameStats();

        TreeSet<StatModifier> effectorModifiers = getModifiers(effect, false);
        TreeSet<StatModifier> effectedModifiers = getModifiers(effect, true);

        SkillEffectId skillEffectId = getSkillEffectId(effect);

        if (effectorModifiers.size() > 0) {
            effectorCgs.addModifiers(skillEffectId, effectorModifiers);
            if (effector instanceof Player) {
                Player player = (Player) effect.getEffector();
                PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
            }
        }

        if (effectedModifiers.size() > 0) {
            effectedCgs.addModifiers(skillEffectId, effectedModifiers);
            if (effected instanceof Player) {
                Player player = (Player) effect.getEffected();
                PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
            }
        }
    }

    /**
     * @param effect
     * @return
     */
    protected TreeSet<StatModifier> getModifiers(Effect effect, boolean isEffected) {
        int skillId = effect.getSkillId();
        int skillLvl = effect.getSkillLevel();

        TreeSet<StatModifier> modifiers = new TreeSet<StatModifier>();

        for (Change changeItem : change) {
            if (changeItem.getStat() == null) {
                log.warn("Skill stat has wrong name for skillid: " + skillId);
                continue;
            }

            int valueWithDelta = changeItem.getValue() + changeItem.getDelta() * skillLvl;
            if (isEffected)
                valueWithDelta = -valueWithDelta;

            switch (changeItem.getFunc()) {
                case ADD:
                    if (changeItem.isUnchecked()) {
                        modifiers.add(UncheckedAdd.newInstance(changeItem.getStat(), valueWithDelta, true));
                    } else {
                        modifiers.add(AddModifier.newInstance(changeItem.getStat(), valueWithDelta, true));
                    }
                    break;
                case PERCENT:
                    modifiers.add(RateModifier.newInstance(changeItem.getStat(), valueWithDelta, true));
                    break;
                case REPLACE:
                    modifiers.add(SetModifier.newInstance(changeItem.getStat(), valueWithDelta, true));
                    break;
            }
        }
        return modifiers;
    }

}
