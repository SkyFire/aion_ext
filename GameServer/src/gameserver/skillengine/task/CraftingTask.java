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
package gameserver.skillengine.task;

import gameserver.model.DescriptionId;
import gameserver.model.gameobjects.StaticObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.model.templates.recipe.RecipeTemplate;
import gameserver.network.aion.serverpackets.SM_CRAFT_ANIMATION;
import gameserver.network.aion.serverpackets.SM_CRAFT_UPDATE;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.services.CraftService;
import gameserver.services.ItemService;
import gameserver.utils.PacketSendUtility;

/**
 * @author Mr. Poke
 */
public class CraftingTask extends AbstractCraftTask {
    private RecipeTemplate recipeTemplate;
    private ItemTemplate itemTemplate;
    private int originalTemplate;
    private int comboStep;
    private int nextComboStep;

    /**
     * @param requestor
     * @param responder
     * @param successValue
     * @param failureValue
     */

    public CraftingTask(Player requestor, StaticObject responder,
                        RecipeTemplate recipeTemplate, ItemTemplate itemTemplate,
                        int originalTemplate, int skillLvlDiff, int comboStep) {
        super(requestor, responder, 100, 100, skillLvlDiff);
        this.recipeTemplate = recipeTemplate;
        this.itemTemplate = itemTemplate;
        this.originalTemplate = originalTemplate;
        this.comboStep = comboStep;
        this.nextComboStep = comboStep+1;
    }

    /* (non-Javadoc)
      * @see com.aionemu.gameserver.skillengine.task.AbstractCraftTask#onFailureFinish()
      */

    @Override
    protected void onFailureFinish() {
        PacketSendUtility.sendPacket(requestor, new SM_CRAFT_UPDATE(recipeTemplate.getSkillid(), itemTemplate, currentSuccessValue, currentFailureValue, 6));
        PacketSendUtility.broadcastPacket(requestor, new SM_CRAFT_ANIMATION(requestor.getObjectId(), responder.getObjectId(), 0, 3), true);
        PacketSendUtility.sendPacket(requestor, SM_SYSTEM_MESSAGE.STR_COMBINE_FAIL(new DescriptionId(ItemService.getItemTemplate(recipeTemplate.getProductid()).getNameId())));
    }

    /* (non-Javadoc)
      * @see com.aionemu.gameserver.skillengine.task.AbstractCraftTask#onSuccessFinish()
      */

    @Override
    protected void onSuccessFinish() {
    	if (critical && recipeTemplate.getComboProduct(nextComboStep) != null && recipeTemplate.getSkillid() != 40009)
    	{
    		CraftService.startComboCrafting(requestor, originalTemplate, responder.getObjectId(), nextComboStep);
    	} else {
            PacketSendUtility.sendPacket(requestor, new SM_CRAFT_UPDATE(recipeTemplate.getSkillid(), itemTemplate, currentSuccessValue, currentFailureValue, 5));
            PacketSendUtility.broadcastPacket(requestor, new SM_CRAFT_ANIMATION(requestor.getObjectId(), responder.getObjectId(), 0, 2), true);
            CraftService.finishCrafting(requestor, recipeTemplate, comboStep);
            PacketSendUtility.sendPacket(requestor, SM_SYSTEM_MESSAGE.STR_CRAFT_SUCCESS_GETEXP);
    	}
    }

    /* (non-Javadoc)
      * @see com.aionemu.gameserver.skillengine.task.AbstractCraftTask#sendInteractionUpdate()
      */

    @Override
    protected void sendInteractionUpdate() {
        PacketSendUtility.sendPacket(requestor, new SM_CRAFT_UPDATE(recipeTemplate.getSkillid(), itemTemplate, currentSuccessValue, currentFailureValue, setCritical ? 2 : 1));
    }

    /* (non-Javadoc)
      * @see com.aionemu.gameserver.skillengine.task.AbstractInteractionTask#onInteractionAbort()
      */

    @Override
    protected void onInteractionAbort() {
        PacketSendUtility.sendPacket(requestor, new SM_CRAFT_UPDATE(recipeTemplate.getSkillid(), itemTemplate, 0, 0, 4));
        PacketSendUtility.broadcastPacket(requestor, new SM_CRAFT_ANIMATION(requestor.getObjectId(), responder.getObjectId(), 0, 2), true);
    }

    /* (non-Javadoc)
      * @see com.aionemu.gameserver.skillengine.task.AbstractInteractionTask#onInteractionFinish()
      */

    @Override
    protected void onInteractionFinish() {
    }

    /* (non-Javadoc)
      * @see com.aionemu.gameserver.skillengine.task.AbstractInteractionTask#onInteractionStart()
      */

    @Override
    protected void onInteractionStart() {
        PacketSendUtility.sendPacket(requestor, new SM_CRAFT_UPDATE(recipeTemplate.getSkillid(), itemTemplate, 100, 100, 0));
        PacketSendUtility.sendPacket(requestor, new SM_CRAFT_UPDATE(recipeTemplate.getSkillid(), itemTemplate, 0, 0, 1));
        PacketSendUtility.broadcastPacket(requestor, new SM_CRAFT_ANIMATION(requestor.getObjectId(), responder.getObjectId(), recipeTemplate.getSkillid(), 0), true);
        PacketSendUtility.broadcastPacket(requestor, new SM_CRAFT_ANIMATION(requestor.getObjectId(), responder.getObjectId(), recipeTemplate.getSkillid(), 1), true);
    }

    @Override
    protected void onComboStart() {
        PacketSendUtility.sendPacket(requestor, new SM_CRAFT_UPDATE(recipeTemplate.getSkillid(), itemTemplate, 75, 100, 3));
        PacketSendUtility.broadcastPacket(requestor, new SM_CRAFT_ANIMATION(requestor.getObjectId(), responder.getObjectId(), recipeTemplate.getSkillid(), 5), true);
    }
}
