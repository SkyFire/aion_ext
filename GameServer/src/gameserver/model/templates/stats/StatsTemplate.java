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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class is only a container for Stats.
 * <p/>
 * Created on: 04.08.2009 14:59:10
 *
 * @author Aquanox
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "stats_template")
public abstract class StatsTemplate {
    @XmlAttribute(name = "maxHp")
    private int maxHp;
    @XmlAttribute(name = "maxMp")
    private int maxMp;

    @XmlAttribute(name = "walk_speed")
    private float walkSpeed;
    @XmlAttribute(name = "run_speed")
    private float runSpeed;
    @XmlAttribute(name = "fly_speed")
    private float flySpeed;

    @XmlAttribute(name = "attack_speed")
    private float attackSpeed;

    @XmlAttribute(name = "evasion")
    private int evasion;
    @XmlAttribute(name = "block")
    private int block;
    @XmlAttribute(name = "parry")
    private int parry;

    @XmlAttribute(name = "main_hand_attack")
    private int mainHandAttack;
    @XmlAttribute(name = "main_hand_accuracy")
    private int mainHandAccuracy;
    @XmlAttribute(name = "main_hand_crit_rate")
    private int mainHandCritRate;

    @XmlAttribute(name = "magic_accuracy")
    private int magicAccuracy;
    @XmlAttribute(name = "boost_heal")
    private int boostHeal;

    /* ======================================= */

    public int getMaxHp() {
        return maxHp;
    }

    public int getMaxMp() {
        return maxMp;
    }


    /* ======================================= */

    public float getWalkSpeed() {
        return walkSpeed;
    }

    public float getRunSpeed() {
        return runSpeed;
    }

    public float getFlySpeed() {
        return flySpeed;
    }

    public float getAttackSpeed() {
        return attackSpeed;
    }

    /* ======================================= */

    public int getEvasion() {
        return evasion;
    }

    public int getBlock() {
        return block;
    }

    public int getParry() {
        return parry;
    }

    /* ======================================= */

    public int getMainHandAttack() {
        return mainHandAttack;
    }

    public int getMainHandAccuracy() {
        return mainHandAccuracy;
    }

    public int getMainHandCritRate() {
        return mainHandCritRate;
    }

    /* ======================================= */

    public int getMagicAccuracy() {
        return magicAccuracy;
    }
    public int getBoostHeal() {
        return boostHeal;
    }
}
