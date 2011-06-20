/**
 * This file is part of alpha team <alpha-team.com>.
 *
 * alpha team is pryvate software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * alpha team is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with alpha team.  If not, see <http://www.gnu.org/licenses/>.
 */
package gameserver.dataholders;

import gameserver.questEngine.handlers.models.*;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author MrPoke
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "quest_scripts")
public class QuestScriptsData {
    @XmlElements({@XmlElement(name = "report_to", type = ReportToData.class),
            @XmlElement(name = "monster_hunt", type = MonsterHuntData.class),
            @XmlElement(name = "xml_quest", type = XmlQuestData.class),
            @XmlElement(name = "item_collecting", type = ItemCollectingData.class),
            @XmlElement(name = "work_order", type = WorkOrdersData.class)
    })
    protected List<QuestScriptData> data;

    /**
     * @return the data
     */
    public List<QuestScriptData> getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(List<QuestScriptData> data) {
        this.data = data;
    }
}
