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

package gameserver.questEngine.model;

import gameserver.model.gameobjects.Gatherable;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.StaticObject;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;

/**
 * @author MrPoke, modified Rolandas
 */
public class QuestCookie {
    private VisibleObject visibleObject;
    private Player player;
    private Integer questId;
    private Integer dialogId;
    private int questVars;
    private int workVar;
    private int targetId;

    /**
     * @param creature
     * @param player
     * @param questId
     */
    public QuestCookie(VisibleObject visibleObject, Player player, Integer questId, Integer dialogId) {
        super();
        this.visibleObject = visibleObject;
        this.player = player;
        this.player.setQuestCookie(this);
        this.questId = questId;
        this.dialogId = dialogId;

        if (visibleObject == null) {
            this.targetId = 0;
        } else if (visibleObject instanceof Npc) {
            this.targetId = ((Npc) visibleObject).getNpcId();
        } else if (visibleObject instanceof Gatherable) {
            this.targetId = ((Gatherable) visibleObject).getObjectTemplate().getTemplateId();
        } else if (visibleObject instanceof StaticObject) {
            this.targetId = ((StaticObject) visibleObject).getObjectTemplate().getTemplateId();
        }

        if (player.getQuestCookie().questId == this.questId) {
            this.questVars = player.getQuestCookie().questVars;
            this.workVar = player.getQuestCookie().workVar;
        } else if (this.questId == 0) {
            this.questVars = 0;
            this.workVar = 0;
        }
    }

    /**
     * @return the visibleObject
     */
    public VisibleObject getVisibleObject() {
        return visibleObject;
    }

    /**
     * @param visibleObject the visibleObject to set
     */
    public void setVisibleObject(VisibleObject visibleObject) {
        this.visibleObject = visibleObject;
    }

    /**
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @param player the player to set
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * @return the questId
     */
    public Integer getQuestId() {
        return questId;
    }

    /**
     * @param questId the questId to set
     */
    public void setQuestId(Integer questId) {
        this.questId = questId;
    }

    /**
     * @return the dialogId
     */
    public Integer getDialogId() {
        return dialogId;
    }

    /**
     * @param dialogId the dialogId to set
     */
    public void setDialogId(Integer dialogId) {
        this.dialogId = dialogId;
    }

    /**
     * @return the questVars
     */
    public int getQuestVars() {
        return questVars;
    }

    /**
     * @param questVars the questVars to set
     */
    public void setQuestVars(int questVars) {
        this.questVars = questVars;
    }

    /**
     * @return the workVar which is the number of active var
     */
    public int getQuestVarNum() {
        return workVar;
    }

    /**
     * @param questVarNum the workVar to set
     */
    public void setQuestWorkVar(int questVarNum) {
        this.workVar = questVarNum;
    }

    /**
     * @return the targetId
     */
    public int getTargetId() {
        return targetId;
    }

    /**
     * @param targetId the targetId to set
     */
    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

}
