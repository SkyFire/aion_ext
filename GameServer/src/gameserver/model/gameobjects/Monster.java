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
package gameserver.model.gameobjects;

import gameserver.ai.npcai.AggressiveAi;
import gameserver.ai.npcai.MonsterAi;
import gameserver.configs.main.CustomConfig;
import gameserver.controllers.MonsterController;
import gameserver.model.templates.VisibleObjectTemplate;
import gameserver.model.templates.spawn.SpawnTemplate;

public class Monster extends Npc {
    /**
     * @param objId
     * @param controller
     * @param spawn
     * @param objectTemplate
     */
    public Monster(int objId, MonsterController controller, SpawnTemplate spawn, VisibleObjectTemplate objectTemplate) {
        super(objId, controller, spawn, objectTemplate);
    }

    @Override
    public MonsterController getController() {
        return (MonsterController) super.getController();
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
