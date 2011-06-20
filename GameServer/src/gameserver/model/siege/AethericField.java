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
package gameserver.model.siege;

import gameserver.controllers.AethericFieldController.AethericFieldGeneratorController;
import gameserver.controllers.AethericFieldController.AethericFieldShieldController;
import gameserver.model.gameobjects.Npc;
import gameserver.model.templates.VisibleObjectTemplate;
import gameserver.model.templates.spawn.SpawnTemplate;

/**
 * @author zdead
 */
public class AethericField {

    public class AethericFieldGenerator extends Npc {
        private AethericField fieldRef;

        public AethericFieldGenerator(int objId, AethericFieldGeneratorController controller, SpawnTemplate spawn, VisibleObjectTemplate objectTemplate, AethericField fieldRef) {
            super(objId, controller, spawn, objectTemplate);
            this.fieldRef = fieldRef;
        }

        public AethericField getReferentField() {
            return this.fieldRef;
        }
    }

    public class AethericFieldShield extends Npc {
        private AethericField fieldRef;
        private boolean active;

        public AethericFieldShield(int objId, AethericFieldShieldController controller, SpawnTemplate spawn, VisibleObjectTemplate objectTemplate, AethericField fieldRef) {
            super(objId, controller, spawn, objectTemplate);
            this.fieldRef = fieldRef;
            this.active = true;
        }

        public AethericField getReferentField() {
            return this.fieldRef;
        }

        public void disable() {
            this.active = false;
        }

        public boolean isActive() {
            return active;
        }
    }

    private int fortressId;

    private AethericFieldGenerator generator;
    private AethericFieldShield shield;

    public AethericField(int fortressId) {
        this.fortressId = fortressId;
    }

    public int getFortressId() {
        return fortressId;
    }

    public void setGenerator(AethericFieldGenerator gen) {
        this.generator = gen;
    }

    public void setShield(AethericFieldShield shield) {
        this.shield = shield;
    }

    public AethericFieldGenerator getGenerator() {
        return generator;
    }

    public AethericFieldShield getShield() {
        return shield;
    }

}
