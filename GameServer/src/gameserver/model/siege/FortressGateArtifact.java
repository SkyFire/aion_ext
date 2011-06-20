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

import gameserver.controllers.FortressGateArtifactController;
import gameserver.model.gameobjects.Npc;
import gameserver.model.templates.VisibleObjectTemplate;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;

/**
 * @author Sarynth
 */
public class FortressGateArtifact extends Npc {

    private FortressGate relatedGate;
    private int healRatio;

    public FortressGateArtifact(int objId, FortressGateArtifactController controller, SpawnTemplate spawn, VisibleObjectTemplate objectTemplate, int healRatio) {
        super(objId, controller, spawn, objectTemplate);
        this.healRatio = healRatio;
    }

    public FortressGate getRelatedGate() {
        return relatedGate;
    }

    public void setRelatedGate(FortressGate gate) {
        relatedGate = gate;
    }

    public void healGate() {
        int hpToAdd = relatedGate.getLifeStats().getMaxHp() * (healRatio / 100);
        relatedGate.getLifeStats().increaseHp(TYPE.NATURAL_HP, hpToAdd);
    }

}
