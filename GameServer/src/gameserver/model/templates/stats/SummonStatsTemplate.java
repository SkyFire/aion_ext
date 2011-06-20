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
package gameserver.model.templates.stats;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "summon_stats_template")
public class SummonStatsTemplate extends StatsTemplate {
    @XmlAttribute(name = "pdefense")
    private int pdefense;
    @XmlAttribute(name = "mresist")
    private int mresist;
	@XmlAttribute(name = "mcrit")
	private int mcrit;

    /**
     * @return the pdefense
     */
    public int getPdefense() {
        return pdefense;
    }

    /**
     * @return the mresist
     */
    public int getMresist() {
        return mresist;
    }
	public int getMcrit() {
		return mcrit;
	}


}
