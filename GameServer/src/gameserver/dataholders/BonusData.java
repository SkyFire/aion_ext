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

package gameserver.dataholders;

import gameserver.model.templates.bonus.BonusTemplate;
import gnu.trove.TIntObjectHashMap;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author Rolandas
 */

@XmlRootElement(name = "bonuses")
@XmlAccessorType(XmlAccessType.FIELD)
public class BonusData {
    @XmlElement(required = true, name = "bonus_info")
    protected List<BonusTemplate> list;

    /**
     * A map containing all bonuses templates
     */
    private TIntObjectHashMap<BonusTemplate> bonusInfoListData;

    void afterUnmarshal(Unmarshaller u, Object parent) {
        bonusInfoListData = new TIntObjectHashMap<BonusTemplate>();
        for (BonusTemplate it : list) {
            bonusInfoListData.put(it.getQuestId(), it);
        }
        list = null;
    }

    public BonusTemplate getBonusInfoByQuestId(int id) {
        return bonusInfoListData.get(id);
    }

    /**
     * @return bonusInfoListData()
     */
    public int size() {
        return bonusInfoListData.size();
	}
}
