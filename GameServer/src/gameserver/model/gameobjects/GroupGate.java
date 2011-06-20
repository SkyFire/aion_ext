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
/*
 * This file is part of the requirements for the Illusion Gate Skill.
 * Code References from ATracer's Trap.java of Aion-Unique
 */
package gameserver.model.gameobjects;

import gameserver.controllers.NpcController;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.VisibleObjectTemplate;
import gameserver.model.templates.spawn.SpawnTemplate;

/**
 * @author LokiReborn
 */
public class GroupGate extends Npc {

    /**
     * Creator of this GroupGate.
     */
    private Creature creator;

    /**
     * @param objId
     * @param controller
     * @param spawnTemplate
     * @param objectTemplate
     */
    public GroupGate(int objId, NpcController controller, SpawnTemplate spawnTemplate, VisibleObjectTemplate objectTemplate) {
        super(objId, controller, spawnTemplate, objectTemplate);
    }

    /**
     * @return the creator
     */
    public Creature getCreator() {
        return creator;
    }

    /**
     * @param creator the creator to set
     */
    public void setCreator(Creature creator) {
        this.creator = creator;
    }

    @Override
    public byte getLevel() {
        return (1);
    }

    @Override
    protected boolean isEnemyNpc(Npc visibleObject) {
        return this.creator.isEnemyNpc(visibleObject);
    }

    @Override
    protected boolean isEnemyPlayer(Player visibleObject) {
        return this.creator.isEnemyPlayer(visibleObject);
    }

    /**
     * @return NpcObjectType.GROUPGATE
     */
    @Override
    public NpcObjectType getNpcObjectType() {
        return NpcObjectType.GROUPGATE;
    }

    @Override
    public Creature getActingCreature() {
        return this.creator;
    }

    @Override
    public Creature getMaster() {
        return this.creator;
	}
}
