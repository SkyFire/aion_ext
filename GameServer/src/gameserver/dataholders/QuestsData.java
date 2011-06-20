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

import gameserver.model.templates.QuestTemplate;
import gnu.trove.TIntObjectHashMap;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author MrPoke
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "quests")
public class QuestsData {

    @XmlElement(name = "quest", required = true)
    protected List<QuestTemplate> questsData;
    private TIntObjectHashMap<QuestTemplate> questData = new TIntObjectHashMap<QuestTemplate>();

    void afterUnmarshal(Unmarshaller u, Object parent) {
        questData.clear();
        for (QuestTemplate quest : questsData) {
            questData.put(quest.getId(), quest);
        }
    }

    public QuestTemplate getQuestById(int id) {
        return questData.get(id);
    }

    public int size() {
        return questData.size();
    }

    /**
     * @return the questsData
     */
    public List<QuestTemplate> getQuestsData() {
        return questsData;
    }

    /**
     * @param questsData the questsData to set
     */
    public void setQuestsData(List<QuestTemplate> questsData) {
        this.questsData = questsData;
        afterUnmarshal(null, null);
    }

}
