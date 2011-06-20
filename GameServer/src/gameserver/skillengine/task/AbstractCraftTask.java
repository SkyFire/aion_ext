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
package gameserver.skillengine.task;

import com.aionemu.commons.utils.Rnd;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.configs.main.CraftConfig;

/**
 * @author ATracer
 */
public abstract class AbstractCraftTask extends AbstractInteractionTask {

    protected int successValue;
    protected int failureValue;
    protected int currentSuccessValue;
    protected int currentFailureValue;
    protected int skillLvlDiff;
    protected boolean critical;
    protected boolean setCritical = false;

    /**
     * @param requestor
     * @param responder
     * @param successValue
     * @param failureValue
     */
    public AbstractCraftTask(Player requestor, VisibleObject responder, int successValue, int failureValue, int skillLvlDiff) {
        super(requestor, responder);
        this.successValue = successValue;
        this.failureValue = failureValue;
        this.skillLvlDiff = skillLvlDiff;
        this.critical = Rnd.get(100) <= CraftConfig.CRIT_CRAFT;
    }

    @Override
    protected boolean onInteraction() {
        if (currentSuccessValue == successValue) {
            onSuccessFinish();
            return true;
        }
        if (currentFailureValue == failureValue) {
            onFailureFinish();
            return true;
        }

        analyzeInteraction();

        sendInteractionUpdate();
        return false;
    }

    /**
     * Perform interaction calculation
     */
    private void analyzeInteraction() {
        //TODO better random
        //if(Rnd.nextBoolean())
        int multi = Math.max(0, 33 - skillLvlDiff * 5);
        if (skillLvlDiff == 99999) {
            currentSuccessValue = successValue;
        } else if (Rnd.get(100) > multi) {
            if (critical && Rnd.get(100) < 30)
                setCritical = true;
            currentSuccessValue += Rnd.get(successValue / (multi + 1) / 2, successValue);
        } else {
            currentFailureValue += Rnd.get(failureValue / (multi + 1) / 2, failureValue);
        }

        if (currentSuccessValue >= successValue) {
            if (critical)
                setCritical = true;
            currentSuccessValue = successValue;
        } else if (currentFailureValue >= failureValue) {
            currentFailureValue = failureValue;
        }
    }

    protected abstract void sendInteractionUpdate();

    protected abstract void onSuccessFinish();

    protected abstract void onFailureFinish();
}
