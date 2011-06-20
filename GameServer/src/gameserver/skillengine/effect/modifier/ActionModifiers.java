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
package gameserver.skillengine.effect.modifier;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ActionModifiers")
public class ActionModifiers {

    @XmlElements({
            @XmlElement(name = "stumbledamage", type = StumbleDamageModifier.class),
            @XmlElement(name = "frontdamage", type = FrontDamageModifier.class),
            @XmlElement(name = "backdamage", type = BackDamageModifier.class),
            @XmlElement(name = "stundamage", type = StunDamageModifier.class),
            @XmlElement(name = "poisondamage", type = PoisonDamageModifier.class),
            @XmlElement(name = "targetrace", type = TargetRaceDamageModifier.class)
    })
    protected List<ActionModifier> actionModifiers;

    /**
     * Gets the value of the actionModifiers property.
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link StumbleDamageModifier }
     * {@link FrontDamageModifier }
     * {@link BackDamageModifier }
     * {@link StunDamageModifier }
     * {@link PoisonDamageModifier }
     * {@link TargetRaceDamageModifier }
     */
    public List<ActionModifier> getActionModifiers() {
        if (actionModifiers == null) {
            actionModifiers = new ArrayList<ActionModifier>();
        }
        return this.actionModifiers;
    }
}
