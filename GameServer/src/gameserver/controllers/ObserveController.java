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
package gameserver.controllers;

import gameserver.controllers.attack.AttackResult;
import gameserver.controllers.attack.AttackStatus;
import gameserver.controllers.movement.ActionObserver;
import gameserver.controllers.movement.AttackCalcObserver;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.state.CreatureState;
import gameserver.skillengine.model.Skill;
import org.apache.commons.lang.ArrayUtils;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author ATracer
 * @author Cura
 */
public class ObserveController {
    // TODO revisit here later
    protected Queue<ActionObserver> moveObservers = new ConcurrentLinkedQueue<ActionObserver>();
    protected Queue<ActionObserver> attackObservers = new ConcurrentLinkedQueue<ActionObserver>();
    protected Queue<ActionObserver> attackedObservers = new ConcurrentLinkedQueue<ActionObserver>();
    protected Queue<ActionObserver> skilluseObservers = new ConcurrentLinkedQueue<ActionObserver>();
    protected Queue<ActionObserver> stateChangeObservers = new ConcurrentLinkedQueue<ActionObserver>();
    protected Queue<ActionObserver> deathObservers = new ConcurrentLinkedQueue<ActionObserver>();
    protected Queue<ActionObserver> jumpObservers = new ConcurrentLinkedQueue<ActionObserver>();
    protected Queue<ActionObserver> dotObservers = new ConcurrentLinkedQueue<ActionObserver>();
    protected Queue<ActionObserver> godstoneObservers = new ConcurrentLinkedQueue<ActionObserver>();

    protected Queue<ActionObserver> observers = new ConcurrentLinkedQueue<ActionObserver>();
    
    protected ActionObserver[] equipObservers = new ActionObserver[0];
    protected AttackCalcObserver[] attackCalcObservers = new AttackCalcObserver[0];

    /**
     * @param observer
     */
    public void attach(ActionObserver observer) {
        switch (observer.getObserverType()) {
            case ATTACK:
                attackObservers.add(observer);
                break;
            case ATTACKED:
                attackedObservers.add(observer);
                break;
            case MOVE:
                moveObservers.add(observer);
                break;
            case SKILLUSE:
                skilluseObservers.add(observer);
                break;
            case STATECHANGE:
                stateChangeObservers.add(observer);
                break;
            case DEATH:
                deathObservers.add(observer);
                break;
            case JUMP:
                jumpObservers.add(observer);
                break;
            case DOT:
                dotObservers.add(observer);
                break;
            case GODSTONE:
                //check godstone exploit
                if (godstoneObservers.size() < 2)
                    godstoneObservers.add(observer);
                break;
        }
    }

    /**
     * notify that creature moved
     */
    protected void notifyMoveObservers() {
        synchronized (moveObservers) {
            while (!moveObservers.isEmpty()) {
                ActionObserver observer = moveObservers.poll();
                observer.moved();
            }
        }
        synchronized (observers) {
            for (ActionObserver observer : observers) {
                observer.moved();
            }
        }
    }

    /**
     * notify that creature attacking
     */
    public void notifyAttackObservers(Creature creature) {
        synchronized (attackObservers) {
            while (!attackObservers.isEmpty()) {
                ActionObserver observer = attackObservers.poll();
                observer.attack(creature);
            }
        }
        synchronized (observers) {
            for (ActionObserver observer : observers) {
                observer.attack(creature);
            }
        }
    }

    /**
     * notify that creature attacked
     */
    protected void notifyAttackedObservers(Creature creature) {
        synchronized (attackedObservers) {
            while (!attackedObservers.isEmpty()) {
                ActionObserver observer = attackedObservers.poll();
                observer.attacked(creature);
            }
        }
        synchronized (observers) {
            for (ActionObserver observer : observers) {
                observer.attacked(creature);
            }
        }
    }

    /**
     * notify that creature used a skill
     */
    public void notifySkilluseObservers(Skill skill) {
        synchronized (skilluseObservers) {
            while (!skilluseObservers.isEmpty()) {
                ActionObserver observer = skilluseObservers.poll();
                observer.skilluse(skill);
            }
        }
        synchronized (observers) {
            for (ActionObserver observer : observers) {
                observer.skilluse(skill);
            }
        }
    }

    /**
     * notify that creature changed state
     */
    public void notifyStateChangeObservers(CreatureState state, boolean isSet) {
        synchronized (stateChangeObservers) {
            while (!stateChangeObservers.isEmpty()) {
                ActionObserver observer = stateChangeObservers.poll();
                observer.stateChanged(state, isSet);
            }
        }
        synchronized (observers) {
            for (ActionObserver observer : observers) {
                observer.stateChanged(state, isSet);
            }
        }
    }

    /**
     * @param item
     * @param owner
     */
    public void notifyItemEquip(Item item, Player owner) {
        synchronized (equipObservers) {
            for (ActionObserver observer : equipObservers) {
                observer.equip(item, owner);
            }
        }
    }

    /**
     * @param item
     * @param owner
     */
    public void notifyItemUnEquip(Item item, Player owner) {
        synchronized (equipObservers) {
            for (ActionObserver observer : equipObservers) {
                observer.unequip(item, owner);
            }
        }
    }

    /**
     * @param observer
     */
    public void addObserver(ActionObserver observer) {
        synchronized (observers) {
            observers.add(observer);
        }
    }

    /**
     * @param observer
     */
    public void addEquipObserver(ActionObserver observer) {
        synchronized (equipObservers) {
            equipObservers = (ActionObserver[]) ArrayUtils.add(equipObservers, observer);
        }
    }

    /**
     * @param observer
     */
    public void addAttackCalcObserver(AttackCalcObserver observer) {
        synchronized (attackCalcObservers) {
            attackCalcObservers = (AttackCalcObserver[]) ArrayUtils.add(attackCalcObservers, observer);
        }
    }

    /**
     * @param observer
     */
    public void removeObserver(ActionObserver observer) {
        synchronized (observers) {
            if (observers.contains(observer))
                observers.remove(observer);
        }
    }

    public void removeDeathObserver(ActionObserver observer) {
        synchronized (deathObservers) {
            if (deathObservers.contains(observer))
                deathObservers.remove(observer);
        }
    }

    /**
     * @param observer
     */
    public void removeEquipObserver(ActionObserver observer) {
        synchronized (equipObservers) {
            equipObservers = (ActionObserver[]) ArrayUtils.removeElement(equipObservers, observer);
        }
    }

    /**
     * @param observer
     */
    public void removeAttackCalcObserver(AttackCalcObserver observer) {
        synchronized (attackCalcObservers) {
            attackCalcObservers = (AttackCalcObserver[]) ArrayUtils.removeElement(attackCalcObservers, observer);
        }
    }

    /**
     * @param status
     * @return true or false
     */
    public boolean checkAttackStatus(AttackStatus status) {
        synchronized (attackCalcObservers) {
            for (AttackCalcObserver observer : attackCalcObservers) {
                if (observer.checkStatus(status))
                    return true;
            }
        }
        return false;
    }

    /**
     * @param status
     * @return
     */
    public boolean checkAttackerStatus(AttackStatus status) {
        synchronized (attackCalcObservers) {
            for (AttackCalcObserver observer : attackCalcObservers) {
                if (observer.checkAttackerStatus(status))
                    return true;
            }
        }
        return false;
    }

    /**
     * @param attackList
     */
    public void checkShieldStatus(List<AttackResult> attackList) {
        synchronized (attackCalcObservers) {
            for (AttackCalcObserver observer : attackCalcObservers) {
                observer.checkShield(attackList);
            }
        }
    }

    public float getBasePhysicalDamageMultiplier() {
        float multiplier = 1;

        synchronized (attackCalcObservers) {
            for (AttackCalcObserver observer : attackCalcObservers) {
                multiplier *= observer.getBasePhysicalDamageMultiplier();
            }
        }

        return multiplier;
    }

    public float getBaseMagicalDamageMultiplier() {
        float multiplier = 1;

        synchronized (attackCalcObservers) {
            for (AttackCalcObserver observer : attackCalcObservers) {
                multiplier *= observer.getBaseMagicalDamageMultiplier();
            }
        }

        return multiplier;
    }

    public void notifyDeath(Creature creature) {
        synchronized (deathObservers) {
            while (!deathObservers.isEmpty()) {
                ActionObserver observer = deathObservers.poll();
                observer.died(creature);
            }
        }
    }

    /**
     * notify that player jumped
     */
    public void notifyJumpObservers() {
        synchronized (jumpObservers) {
            while (!jumpObservers.isEmpty()) {
                ActionObserver observer = jumpObservers.poll();
                observer.jump();
            }
        }
        synchronized (observers) {
            for (ActionObserver observer : observers) {
                observer.jump();
            }
        }
    }

    /**
     * notify that player received damage over time
     */
    public void notifyDotObservers(Creature creature) {
        synchronized (dotObservers) {
            while (!dotObservers.isEmpty()) {
                ActionObserver observer = dotObservers.poll();
                observer.onDot(creature);
            }
        }
        synchronized (observers) {
            for(ActionObserver observer : observers)
            {
                observer.onDot(creature);
            }
        }
    }

    /**
     * notify that player using godstone
     */
    public void notifyGodstoneObservers(Creature creature) {
        synchronized (godstoneObservers) {
            while (!godstoneObservers.isEmpty()) {
                ActionObserver observer = godstoneObservers.poll();
                observer.onGodstone(creature);
            }
        }
        synchronized (observers) {
            for (ActionObserver observer : observers) {
                observer.onGodstone(creature);
            }
        }
    }
}
