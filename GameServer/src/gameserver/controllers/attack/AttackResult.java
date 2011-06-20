/**
 * This file is part of alpha team <alpha-team.com>.
 *
 * alpha team is pryvate software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * alpha team is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with alpha team.  If not, see <http://www.gnu.org/licenses/>.
 */
package gameserver.controllers.attack;

/**
 * @author ATracer
 */
public class AttackResult {
    private int damage;

    private AttackStatus attackStatus;

    private int shieldType;

    public AttackResult(int damage, AttackStatus attackStatus) {
        this.damage = damage;
        this.attackStatus = attackStatus;
    }

    /**
     * @return the damage
     */
    public int getDamage() {
        return damage;
    }

    /**
     * @param damage the damage to set
     */
    public void setDamage(int damage) {
        this.damage = damage;
    }

    /**
     * @return the attackStatus
     */
    public AttackStatus getAttackStatus() {
        return attackStatus;
    }

    /**
     * @return the shieldType
     */
    public int getShieldType() {
        return shieldType;
    }

    /**
     * @param shieldType the shieldType to set
     */
    public void setShieldType(int shieldType) {
        this.shieldType = shieldType;
    }
}
