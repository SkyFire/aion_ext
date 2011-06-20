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

import gameserver.controllers.ArtifactController;
import gameserver.model.gameobjects.Npc;
import gameserver.model.templates.VisibleObjectTemplate;
import gameserver.model.templates.siege.ArtifactTemplate;
import gameserver.model.templates.spawn.SpawnTemplate;
import javolution.util.FastList;

/**
 * @author Sarynth
 */
public class Artifact extends Npc {

    private int artifactId;
    private ArtifactProtector protector;

    private FastList<Integer> relatedSpawnedObjectsIds;
    private ArtifactTemplate template;

    public Artifact(int objId, ArtifactController controller, SpawnTemplate spawn, VisibleObjectTemplate objectTemplate, int artifactId) {
        super(objId, controller, spawn, objectTemplate);
        this.artifactId = artifactId;
        this.relatedSpawnedObjectsIds = new FastList<Integer>();
    }

    public void setProtector(ArtifactProtector protector) {
        this.protector = protector;
        protector.setArtifact(this);
    }

    public int getLocationId() {
        return artifactId;
    }

    public ArtifactProtector getProtector() {
        return protector;
    }

    public void registerRelatedSpawn(int objectId) {
        if (!relatedSpawnedObjectsIds.contains(objectId))
            relatedSpawnedObjectsIds.add(objectId);
    }

    public FastList<Integer> getRelatedSpawnIds() {
        return relatedSpawnedObjectsIds;
    }

    /**
     * @return the template
     */
    public ArtifactTemplate getTemplate() {
        return template;
    }

    /**
     * @param template the template to set
     */
    public void setTemplate(ArtifactTemplate template) {
        this.template = template;
    }

}
