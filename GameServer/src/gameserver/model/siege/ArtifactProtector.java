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

import gameserver.ai.npcai.AggressiveAi;
import gameserver.ai.npcai.MonsterAi;
import gameserver.configs.main.CustomConfig;
import gameserver.controllers.ArtifactProtectorController;
import gameserver.model.gameobjects.Npc;
import gameserver.model.templates.VisibleObjectTemplate;
import gameserver.model.templates.spawn.SpawnTemplate;

/**
 * @author xitanium
 */
public class ArtifactProtector extends Npc {

    private Artifact artifact;

    public ArtifactProtector(int objId, ArtifactProtectorController controller, SpawnTemplate spawn, VisibleObjectTemplate objectTemplate) {
        super(objId, controller, spawn, objectTemplate);
    }

    @Override
    public ArtifactProtectorController getController() {
        return (ArtifactProtectorController) super.getController();
    }

    public void setArtifact(Artifact artifact) {
        this.artifact = artifact;
    }

    public Artifact getArtifact() {
        return artifact;
    }

    @Override
    public void initializeAi() {
        if (isAggressive() && !CustomConfig.DISABLE_MOB_AGGRO)
            this.ai = new AggressiveAi();
        else
            this.ai = new MonsterAi();

        ai.setOwner(this);
    }

}
