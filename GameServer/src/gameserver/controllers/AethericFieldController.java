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

import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.Creature;
import gameserver.model.siege.AethericField.AethericFieldGenerator;
import gameserver.model.siege.AethericField.AethericFieldShield;

/**
 * @author Xitanium
 */
public class AethericFieldController {

    public class AethericFieldGeneratorController extends NpcController {
        @Override
        public void onDie(Creature lastAttacker) {
            if (getOwner().getReferentField().getShield().isActive()) {
                // Send Message?
                DataManager.SPAWNS_DATA.removeSpawn(getOwner().getReferentField().getShield().getSpawn());
                getOwner().getReferentField().getShield().getController().delete();
                getOwner().getReferentField().getShield().disable();
            }
        }

        @Override
        public void onRespawn() {
            super.onRespawn();
        }

        @Override
        public AethericFieldGenerator getOwner() {
            return (AethericFieldGenerator) super.getOwner();
        }
    }

    public class AethericFieldShieldController extends NpcController {
        @Override
        public void onRespawn() {
            super.onRespawn();
        }

        @Override
        public AethericFieldShield getOwner() {
            return (AethericFieldShield) super.getOwner();
        }

        //TODO: kill any player who forces passage through the shield
    }

    public AethericFieldController() {

    }

}
