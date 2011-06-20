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

import gameserver.skillengine.model.DispelType;
import gameserver.skillengine.model.Effect;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author ATracer
 */
public class DispelEffect extends EffectTemplate {
    @XmlElement(type = Integer.class)
    protected List<Integer> effectids;
    @XmlElement
    protected List<String> effecttype;
    @XmlAttribute
    protected DispelType dispeltype;
    @XmlAttribute
    protected Integer value;

    @Override
    public void applyEffect(Effect effect) {
        if (effect.getEffected() == null || effect.getEffected().getEffectController() == null)
            return;

        if (dispeltype == null)
            return;

        if (dispeltype == DispelType.EFFECTID && effectids == null)
            return;

        if (dispeltype == DispelType.EFFECTTYPE && effecttype == null)
            return;

        switch (dispeltype) {
            case EFFECTID:
                if (effectids == null)
                    return;

                for (Integer effectId : effectids) {
                    effect.getEffected().getEffectController().removeEffectByEffectId(effectId);
                }
                break;
            case EFFECTTYPE:
                if (effecttype == null)
                    return;

                for (String type : effecttype) {
                    EffectId abnormalType = EffectId.getEffectIdByName(type);
                    if (abnormalType != null && effect.getEffected().getEffectController().isAbnormalSet(abnormalType)) {
                        effect.getEffected().getEffectController().unsetAbnormal(abnormalType.getEffectId());
                    }
                }
                break;
        }
    }

    @Override
    public void calculate(Effect effect) {
        effect.addSucessEffect(this);
    }
}
