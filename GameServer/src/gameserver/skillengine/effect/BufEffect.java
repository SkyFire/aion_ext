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
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BufEffect")
public abstract class BufEffect extends EffectTemplate {
    private static final Logger log = Logger.getLogger(BufEffect.class);

    @Override
    public void applyEffect(final Effect effect) {
        if (isOnFly()) {
            ActionObserver observer = new ActionObserver(ObserverType.STATECHANGE) {
                @Override
                public void stateChanged(CreatureState state, boolean isSet) {
                    if (state == CreatureState.FLYING) {
                        if (isSet) {
                            effect.addToEffectedController();
                        } else {
                            effect.endEffect();
                        }
                    }
                }
            };
            effect.getEffected().getObserveController().addObserver(observer);
            effect.setActionObserver(observer, position);
        } else {
            effect.addToEffectedController();
        }
    }


    @Override
    public void calculate(Effect effect) {
        effect.addSucessEffect(this);
    }

    /**
     * Will be called from effect controller when effect ends
     */
    @Override
    public void endEffect(Effect effect) {
        Creature effected = effect.getEffected();
        int skillId = effect.getSkillId();
        effected.getGameStats().endEffect(SkillEffectId.getInstance(skillId, effectid, position));
        ActionObserver observer = effect.getActionObserver(position);
        if (observer != null)
            effect.getEffected().getObserveController().removeObserver(observer);
    }

    /**
     * Will be called from effect controller when effect starts
     */
    @Override
    public void startEffect(Effect effect) {
        if (change == null)
            return;

    	TreeSet<StatModifier> modifiers = getModifiers(effect);
        startEffect(effect, modifiers);
    }
    
    public void startEffect(Effect effect, TreeSet<StatModifier> modifiers) {
        if (change == null)
            return;

        Creature effected = effect.getEffected();
        CreatureGameStats<? extends Creature> cgs = effected.getGameStats();

        SkillEffectId skillEffectId = getSkillEffectId(effect);

        if (modifiers.size() > 0) {
            // Hack for Assassin Skill: Aethertwisting
            for (StatModifier mod : modifiers) {
                if (mod instanceof SimpleModifier) {
                    SimpleModifier sf = (SimpleModifier) mod;
                    if (sf.getValue() < 0 && (effect.getEffector().getEffectController().hasAbnormalEffect(920) ||
                            effect.getEffector().getEffectController().hasAbnormalEffect(926) ||
                            effect.getEffector().getEffectController().hasAbnormalEffect(2118))) {
                        modifiers.remove(mod);
                    }
                }
            }
            cgs.addModifiers(skillEffectId, modifiers);

            if (effect.getEffected() instanceof Player) {
                Player player = (Player) effect.getEffected();
                PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
            }
        }
    }

    /**
     * @param effect
     * @return
     */
    protected SkillEffectId getSkillEffectId(Effect effect) {
        int skillId = effect.getSkillId();
        return SkillEffectId.getInstance(skillId, effectid, position);
    }

    /**
     * @param effect
     * @return
     */
    protected TreeSet<StatModifier> getModifiers(Effect effect) {
        int skillId = effect.getSkillId();
        int skillLvl = effect.getSkillLevel();

        TreeSet<StatModifier> modifiers = new TreeSet<StatModifier>();

        for (Change changeItem : change) {
            if (changeItem.getStat() == null) {
                log.warn("Skill stat has wrong name for skillid: " + skillId);
                continue;
            }

            int valueWithDelta = changeItem.getValue() + changeItem.getDelta() * skillLvl;

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

    @Override
    public void onPeriodicAction(Effect effect) {
        // TODO Auto-generated method stub

    }
}
