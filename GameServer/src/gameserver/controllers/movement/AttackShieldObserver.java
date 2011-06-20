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
package gameserver.controllers.movement;

import gameserver.controllers.attack.AttackResult;
import gameserver.skillengine.model.Effect;

import java.util.List;


/**
 * @author ATracer
 */
public class AttackShieldObserver extends AttackCalcObserver {

    private int hit;
    private int totalHit;
    private Effect effect;
    private boolean percent;

    /**
     * @param percent
     * @param value
     * @param status
     */
    public AttackShieldObserver(int hit, int totalHit, boolean percent, Effect effect) {
        this.hit = hit;
        this.totalHit = totalHit;
        this.effect = effect;
        this.percent = percent;
    }

    @Override
    public void checkShield(List<AttackResult> attackList) {
        for (AttackResult attackResult : attackList) {
            int damage = attackResult.getDamage();

            int absorbedDamage = 0;
            if (percent)
                absorbedDamage = damage * hit / 100;
            else
                absorbedDamage = damage >= hit ? hit : damage;

            absorbedDamage = absorbedDamage >= totalHit ? totalHit : absorbedDamage;
            totalHit -= absorbedDamage;

            if (absorbedDamage > 0)
                attackResult.setShieldType(2);//TODO investigate other shield types
            attackResult.setDamage(damage - absorbedDamage);

            if (totalHit <= 0) {
                effect.endEffect();
                return;
            }
		}	
	}
}
