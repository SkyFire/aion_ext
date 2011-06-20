/*
 * This file is part of Aion X Emu <aionxemu.com>.
 *
 *  This is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This software is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this software.  If not, see <http://www.gnu.org/licenses/>.
 */
package gameserver.model.templates.pet;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import gameserver.model.templates.stats.PetStatsTemplate;

/**
 * @author IlBuono
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "pet")
public class PetTemplate
{
    @XmlAttribute(name = "id", required = true)
    private int id;
    @XmlAttribute(name = "name", required = true)
    private String name;
    @XmlAttribute(name = "nameid", required = true)
    private int nameId;
    @XmlElement(name = "petfunction")
    private List<PetFunction> petFunctions;
    @XmlElement(name = "petstats")
    private PetStatsTemplate petStats;

    public int getPetId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public int getNameId()
    {
        return nameId;
    }

    public List<PetFunction> getPetFunction()
    {
        return petFunctions;
    }

    public PetFunction getWarehouseFunction()
    {
        if (petFunctions == null)
            return null;

        for (PetFunction pf : petFunctions)
        {
            if (pf.getPetFunctionType() == PetFunctionType.WAREHOUSE)
                return pf;
        }
        return null;
    }

    public PetStatsTemplate getPetStats()
    {
        return petStats;
    }
}
