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
package gameserver.model.templates.siege;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author xitanium
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Artifact")
public class ArtifactTemplate {

    @XmlElement(name = "baseinfo")
    protected SiegeSingleSpawnBaseInfo baseInfo;
    @XmlElement(name = "artifact_effect")
    protected ArtifactEffectTemplate artifactEffect;
    @XmlElement(name = "artifact_protector")
    protected ArtifactProtectorTemplate artifactProtector;

    public SiegeSingleSpawnBaseInfo getBaseInfo() {
        return baseInfo;
    }

    public ArtifactEffectTemplate getEffectTemplate() {
        return artifactEffect;
    }

    public ArtifactProtectorTemplate getProtectorTemplate() {
        return artifactProtector;
    }

}
