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
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.id.SkillEffectId;
import gameserver.model.gameobjects.stats.modifiers.StatModifier;
import gameserver.model.templates.item.WeaponType;
import gameserver.skillengine.model.Effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.util.TreeSet;

/**
 * @author ATracer
 *         <p/>
 *         The code here is duplicated of WeaponStatboost
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WeaponStatupEffect")
public class WeaponStatupEffect extends BufEffect {
    @XmlAttribute(name = "weapon")
    private WeaponType weaponType;

    @Override
    public void startEffect(final Effect effect) {
        if (!(effect.getEffector() instanceof Player))
            return;


        final Player effected = (Player) effect.getEffected();

        final SkillEffectId skillEffectId = getSkillEffectId(effect);
        final TreeSet<StatModifier> stats = getModifiers(effect);

        if (effected.getEquipment().isWeaponEquipped(weaponType))
            effected.getGameStats().addModifiers(skillEffectId, stats);

        /**
         * Since weapon stat boost is only for BOW and SWORD_2H in templates - checking only
         * one weapon is enough for final result.
         */
        ActionObserver aObserver = new ActionObserver(ObserverType.EQUIP) {

            @Override
            public void equip(Item item, Player owner) {
                if (item.getItemTemplate().getWeaponType() == weaponType)
                    effected.getGameStats().addModifiers(skillEffectId, stats);
            }

            @Override
            public void unequip(Item item, Player owner) {
                if (item.getItemTemplate().getWeaponType() == weaponType)
                    effected.getGameStats().endEffect(skillEffectId);
            }

        };

        effected.getObserveController().addEquipObserver(aObserver);
        effect.setActionObserver(aObserver, position);
    }

    @Override
    public void endEffect(Effect effect) {
        ActionObserver observer = effect.getActionObserver(position);
        if (observer != null)
            effect.getEffected().getObserveController().removeEquipObserver(observer);

        final SkillEffectId skillEffectId = getSkillEffectId(effect);

        if (effect.getEffected().getGameStats().effectAlreadyAdded(skillEffectId))
            effect.getEffected().getGameStats().endEffect(skillEffectId);

    }

    @Override
    public void applyEffect(Effect effect) {
        effect.addToEffectedController();
    }

    @Override
    public void calculate(Effect effect) {
        effect.addSucessEffect(this);
    }

}
