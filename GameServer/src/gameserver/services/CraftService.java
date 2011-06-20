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
package gameserver.services;

import gameserver.dataholders.DataManager;
import gameserver.model.DescriptionId;
import gameserver.model.gameobjects.AionObject;
import gameserver.model.gameobjects.StaticObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.RewardType;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.model.templates.recipe.Component;
import gameserver.model.templates.recipe.RecipeTemplate;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.skillengine.task.CraftingTask;
import gameserver.utils.PacketSendUtility;
import gameserver.world.World;
import org.apache.log4j.Logger;

/**
 * @author MrPoke, sphinx, PZIKO333
 */
public class CraftService {
    private static final Logger log = Logger.getLogger(CraftService.class);

    /**
     * @param player
     * @param recipetemplate
     * @param critical
     */
    public static void finishCrafting(Player player, RecipeTemplate recipetemplate, int comboStep) {
        int productItemId = 0;

        if(recipetemplate.getComboProduct(comboStep) != null)
        {
        	productItemId = recipetemplate.getComboProduct(comboStep);
        } else {
        	productItemId = recipetemplate.getProductid();
        }


        if (recipetemplate.getSkillid() == 40009)

        {
            ItemService.addItem(player, productItemId, recipetemplate.getQuantity());
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_COMBINE_SUCCESS(new DescriptionId(ItemService.getItemTemplate(productItemId).getNameId())));
        } else if ((productItemId != 0) && (recipetemplate.getSkillid() != 40009)) {
            int xpReward = (int) ((0.008 * (recipetemplate.getSkillpoint() + 100) * (recipetemplate.getSkillpoint() + 100) + 60));
            ItemService.addItem(player, productItemId, recipetemplate.getQuantity(), player.getName());
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_COMBINE_SUCCESS(new DescriptionId(ItemService.getItemTemplate(productItemId).getNameId())));

            if (player.getSkillList().addSkillXp(player, recipetemplate.getSkillid(), (int)RewardType.CRAFTING.calcReward(player, xpReward)))
                player.getCommonData().addExp(xpReward, RewardType.CRAFTING);
            else
                PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.MSG_DONT_GET_PRODUCTION_EXP(new DescriptionId(recipetemplate.getSkillid())));
        }
        player.setCraftingTask(null);
    }

    /**
     * @param player
     * @param targetTemplateId
     * @param recipeId
     * @param targetObjId
     */
    public static void startComboCrafting(Player player, int recipeId, int targetObjId, int comboStep) {
        RecipeTemplate recipeTemplate = DataManager.RECIPE_DATA.getRecipeTemplateById(recipeId);

        if (recipeTemplate != null) {

            int skillId = recipeTemplate.getSkillid();
            AionObject target = World.getInstance().findAionObject(targetObjId);

            ItemTemplate itemTemplate = ItemService.getItemTemplate(recipeTemplate.getComboProduct(comboStep));
            if (itemTemplate == null)
                return;

            int skillLvlDiff = player.getSkillList().getSkillLevel(skillId) - recipeTemplate.getSkillpoint();

            player.setCraftingTask(new CraftingTask(player, (StaticObject) target, recipeTemplate, itemTemplate, recipeId, skillLvlDiff, comboStep));
            player.getCraftingTask().combo();
        }
    }

    /**
     * @param player
     * @param targetTemplateId
     * @param recipeId
     * @param targetObjId
     */
    public static void startCrafting(Player player, int targetTemplateId, int recipeId, int targetObjId) {
        if (player.getCraftingTask() != null && player.getCraftingTask().isInProgress())
            return;

        RecipeTemplate recipeTemplate = DataManager.RECIPE_DATA.getRecipeTemplateById(recipeId);

        if (recipeTemplate != null) {
            // check for pre-usage crafting -----------------------------------------------------
            int skillId = recipeTemplate.getSkillid();
            AionObject target = World.getInstance().findAionObject(targetObjId);

            //morphing dont need static object/npc to use
            if ((skillId != 40009) && (target == null || !(target instanceof StaticObject))) {
                log.info("[AUDIT] Player " + player.getName() + " tried to craft incorrect target.");
                return;
            }

            if (recipeTemplate.getDp() != null && (player.getCommonData().getDp() < recipeTemplate.getDp())) {
                log.info("[AUDIT] Player " + player.getName() + " modded her/his client.");
                return;
            }
            if (player.getInventory().isFull()) {
                if (player.getInventory().getItemCountByItemId(recipeTemplate.getProductid()) == 0) {
                    PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.COMBINE_INVENTORY_IS_FULL);
                    return;
                }
            }

            for (Component component : recipeTemplate.getComponent()) {
                if (player.getInventory().getItemCountByItemId(component.getItemid()) < component.getQuantity()) {
                    log.info("[AUDIT] Player " + player.getName() + " modded her/his client.");
                    return;
                }
            }
            // ---------------------------------------------------------------------------------

            //craft item template --------------------------------------------------------------
            ItemTemplate itemTemplate = ItemService.getItemTemplate(recipeTemplate.getProductid());
            if (itemTemplate == null)
                return;

            if (recipeTemplate.getDp() != null)
                player.getCommonData().addDp(-recipeTemplate.getDp());

            for (Component component : recipeTemplate.getComponent()) {
                player.getInventory().removeFromBagByItemId(component.getItemid(), component.getQuantity());
            }
            // ----------------------------------------------------------------------------------

            // start crafting
            int skillLvlDiff = player.getSkillList().getSkillLevel(skillId) - recipeTemplate.getSkillpoint();
            int morph = 99999;

            if (skillId == 40009) {
                player.setCraftingTask(new CraftingTask(player, (StaticObject) target, recipeTemplate, itemTemplate, recipeId, morph, -1));
            } else {
                player.setCraftingTask(new CraftingTask(player, (StaticObject) target, recipeTemplate, itemTemplate, recipeId, skillLvlDiff, -1));
            }
            player.getCraftingTask().start();
        }
    }
}
