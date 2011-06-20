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
package gameserver.questEngine.handlers.models;

import gameserver.questEngine.QuestEngine;
import gameserver.questEngine.handlers.models.xmlQuest.events.OnKillEvent;
import gameserver.questEngine.handlers.models.xmlQuest.events.OnTalkEvent;
import gameserver.questEngine.handlers.template.XmlQuest;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mr. Poke
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "XmlQuest", propOrder = {"onTalkEvent", "onKillEvent"})
public class XmlQuestData extends QuestScriptData {
    @XmlElement(name = "on_talk_event")
    protected List<OnTalkEvent> onTalkEvent;
    @XmlElement(name = "on_kill_event")
    protected List<OnKillEvent> onKillEvent;
    @XmlAttribute(name = "start_npc_id")
    protected Integer startNpcId;
    @XmlAttribute(name = "end_npc_id")
    protected Integer endNpcId;

    /**
     * Gets the value of the onTalkEvent property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the onTalkEvent property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <p/>
     * <pre>
     * getOnTalkEvent().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list {@link OnTalkEvent }
     */
    public List<OnTalkEvent> getOnTalkEvent() {
        if (onTalkEvent == null) {
            onTalkEvent = new ArrayList<OnTalkEvent>();
        }
        return this.onTalkEvent;
    }

    /**
     * Gets the value of the onKillEvent property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the onKillEvent property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <p/>
     * <pre>
     * getOnKillEvent().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list {@link OnKillEvent }
     */
    public List<OnKillEvent> getOnKillEvent() {
        if (onKillEvent == null) {
            onKillEvent = new ArrayList<OnKillEvent>();
        }
        return this.onKillEvent;
    }

    /**
     * Gets the value of the startNpcId property.
     *
     * @return possible object is {@link Integer }
     */
    public Integer getStartNpcId() {
        return startNpcId;
    }

    /**
     * Gets the value of the endNpcId property.
     *
     * @return possible object is {@link Integer }
     */
    public Integer getEndNpcId() {
        return endNpcId;
    }

    /*
      * (non-Javadoc)
      * @see
      * com.aionemu.gameserver.questEngine.handlers.models.QuestScriptData#register(com.aionemu.gameserver.questEngine
      * .QuestEngine)
      */

    @Override
    public void register(QuestEngine questEngine) {
        questEngine.addQuestHandler(new XmlQuest(this));
    }
}
