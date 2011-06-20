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

package gameserver.network.aion.clientpackets;

import gameserver.model.gameobjects.AionObject;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.questEngine.QuestEngine;
import gameserver.questEngine.model.QuestCookie;
import gameserver.services.ClassChangeService;
import gameserver.world.World;
import org.apache.log4j.Logger;

/**
 * @author KKnD , orz, avol
 */
public class CM_DIALOG_SELECT extends AionClientPacket {
    /**
     * Target object id that client wants to TALK WITH or 0 if wants to unselect
     */
    private int targetObjectId;
    private int dialogId;
    private int selectableReward;
    @SuppressWarnings("unused")
    private int lastPage;
    private int questId;

    @SuppressWarnings("unused")
    private static final Logger log = Logger.getLogger(CM_DIALOG_SELECT.class);

    /**
     * Constructs new instance of <tt>CM_CM_REQUEST_DIALOG </tt> packet
     *
     * @param opcode
     */
    public CM_DIALOG_SELECT(int opcode) {
        super(opcode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        targetObjectId = readD();// empty
        dialogId = readH(); // total no of choice
        selectableReward = readH(); // selectable reward number in case of last reward for multiple time quests
        lastPage = readH();
        questId = readD();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        final Player player = getConnection().getActivePlayer();
        if (player == null)
            return;

        if (targetObjectId == 0) {
            if (QuestEngine.getInstance().onDialog(new QuestCookie(null, player, questId, dialogId)))
                return;
            // FIXME client sends unk1=1, targetObjectId=0, dialogId=2 (trader) => we miss some packet to close window
            ClassChangeService.changeClassToSelection(player, dialogId);
            return;
        }

        AionObject object = World.getInstance().findAionObject(targetObjectId);

        if (object instanceof Creature) {
            Creature creature = (Creature) object;
            switch (selectableReward) {
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 16:
                case 17:
                    dialogId = selectableReward;
                    break;
            }
            creature.getController().onDialogSelect(dialogId, player, questId);
        }
        //log.info("id: "+targetObjectId+" dialogId: " + dialogId +" unk1: " + unk1 + " questId: "+questId);
    }
}