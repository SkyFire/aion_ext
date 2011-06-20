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

import gameserver.model.siege.SiegeRace;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author xitanium
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SingleSpawnBaseInfo")
public class SiegeSingleSpawnBaseInfo {

    @XmlAttribute(name = "npcid_dr")
    protected int npcid_Drakan;
    @XmlAttribute(name = "npcid_da")
    protected int npcid_Asmodians;
    @XmlAttribute(name = "npcid_li")
    protected int npcid_Elyos;

    @XmlAttribute(name = "x")
    protected float x;
    @XmlAttribute(name = "y")
    protected float y;
    @XmlAttribute(name = "z")
    protected float z;
    @XmlAttribute(name = "h")
    protected int h;

    @XmlAttribute(name = "static_id")
    protected int staticId;

    /**
     * @return the x
     */
    public float getX() {
        return x;
    }

    /**
     * @return the y
     */
    public float getY() {
        return y;
    }

    /**
     * @return the z
     */
    public float getZ() {
        return z;
    }

    /**
     * @return the h
     */
    public int getH() {
        return h;
    }

    public int getNpcId(SiegeRace race) {
        switch (race) {
            case ASMODIANS:
                return npcid_Asmodians;
            case BALAUR:
                return npcid_Drakan;
            case ELYOS:
                return npcid_Elyos;
            default:
                return 0;
        }
    }

    public int getStaticId() {
        return staticId;
    }

}
