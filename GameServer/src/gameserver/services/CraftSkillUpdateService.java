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
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.RequestResponseHandler;
import gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import gameserver.network.aion.serverpackets.SM_SKILL_LIST;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.utils.PacketSendUtility;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * @author MrPoke, sphinx
 */

public class CraftSkillUpdateService {
    private static final Logger log = Logger.getLogger(CraftSkillUpdateService.class);

    private static final Map<Integer, LearnTemplate> npcBySkill = new HashMap<Integer, LearnTemplate>();
    private static final Map<Integer, Integer> cost = new HashMap<Integer, Integer>();
    private static final List<Integer> craftingSkillIds = new ArrayList<Integer>();

    public static final CraftSkillUpdateService getInstance() {
        return SingletonHolder.instance;
    }

    private CraftSkillUpdateService() {
        // Asmodian
        npcBySkill.put(204096, new LearnTemplate(30002, false, "Extract Vitality"));
        npcBySkill.put(204257, new LearnTemplate(30003, false, "Extract Aether"));

        npcBySkill.put(204100, new LearnTemplate(40001, true, "Cooking"));
        npcBySkill.put(204104, new LearnTemplate(40002, true, "Weaponsmithing"));
        npcBySkill.put(204106, new LearnTemplate(40003, true, "Armorsmithing"));
        npcBySkill.put(204110, new LearnTemplate(40004, true, "Tailoring"));
        npcBySkill.put(204102, new LearnTemplate(40007, true, "Alchemy"));
        npcBySkill.put(204108, new LearnTemplate(40008, true, "Handicrafting"));

        // Elyos
        npcBySkill.put(203780, new LearnTemplate(30002, false, "Extract Vitality"));
        npcBySkill.put(203782, new LearnTemplate(30003, false, "Extract Aether"));

        npcBySkill.put(203784, new LearnTemplate(40001, true, "Cooking"));
        npcBySkill.put(203788, new LearnTemplate(40002, true, "Weaponsmithing"));
        npcBySkill.put(203790, new LearnTemplate(40003, true, "Armorsmithing"));
        npcBySkill.put(203793, new LearnTemplate(40004, true, "Tailoring"));
        npcBySkill.put(203786, new LearnTemplate(40007, true, "Alchemy"));
        npcBySkill.put(203792, new LearnTemplate(40008, true, "Handicrafting"));

        cost.put(0, 3500);
        cost.put(99, 17000);
        cost.put(199, 115000);
        cost.put(299, 460000);
        cost.put(399, 1500000);
		cost.put(449, 3400000);
        cost.put(499, 3500000);

        craftingSkillIds.add(40001);
        craftingSkillIds.add(40002);
        craftingSkillIds.add(40003);
        craftingSkillIds.add(40004);
        craftingSkillIds.add(40007);
        craftingSkillIds.add(40008);

        log.info("CraftSkillUpdateService: Initialized.");
    }

    class LearnTemplate {
        private int skillId;
        private boolean isCraftSkill;

        /**
         * @return the isCraftSkill
         */
        public boolean isCraftSkill() {
            return isCraftSkill;
        }

        LearnTemplate(int skillId, boolean isCraftSkill, String skillName) {
            this.skillId = skillId;
            this.isCraftSkill = isCraftSkill;
        }

        /**
         * @return the skillId
         */
        public int getSkillId() {
            return skillId;
        }
    }

    public void learnSkill(Player player, Npc npc) {
        if (player.getLevel() < 10) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CRAFT_INFO_MAXPOINT_UP);
            return;
        }
        final LearnTemplate template = npcBySkill.get(npc.getNpcId());
        if (template == null)
            return;
        final int skillId = template.getSkillId();
        if (skillId == 0)
            return;

        int skillLvl = 0;
        if (player.getSkillList().isSkillPresent(skillId))
            skillLvl = player.getSkillList().getSkillLevel(skillId);

        if (!cost.containsKey(skillLvl))
            return;

        // max mastered crafting skill == 3
        if (isCraftingSkill(skillId)
                && (!canLearnMoreMasterCraftingSkill(player) && skillLvl == 499)) {
            PacketSendUtility.sendMessage(player, "You can only master 3 crafting skills.");
            return;
        }

        final int price = cost.get(skillLvl);
        final Item kinahItem = player.getInventory().getKinahItem();
        if (price > kinahItem.getItemCount()) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_NOT_ENOUGH_MONEY);
            return;
        }
        final int skillLevel = skillLvl;
        RequestResponseHandler responseHandler = new RequestResponseHandler(npc) {
            @Override
            public void acceptRequest(Creature requester, Player responder) {
                if (responder.getInventory().decreaseKinah(price)) {
                    responder.getSkillList().addSkill(responder, skillId, skillLevel + 1, true);
                    responder.getRecipeList().autoLearnRecipe(responder, skillId, skillLevel + 1);
                    PacketSendUtility.sendPacket(responder, new SM_SKILL_LIST(responder.getSkillList().getSkillEntry(skillId), 1330064));
                }
            }

            @Override
            public void denyRequest(Creature requester, Player responder) {
                //nothing to do
            }
        };

        boolean result = player.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_CRAFT_ADDSKILL_CONFIRM, responseHandler);
        if (result) {
            PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_CRAFT_ADDSKILL_CONFIRM, 0, new DescriptionId(DataManager.SKILL_DATA.getSkillTemplate(skillId).getNameId()), String.valueOf(price)));
        }
    }

    /**
     * check if skillId is crafting skill or not
     *
     * @param skillId
     * @return true or false
     */
    private static boolean isCraftingSkill(int skillId) {
        Iterator<Integer> it = craftingSkillIds.iterator();
        while (it.hasNext()) {
            if (it.next() == skillId)
                return true;
        }
        return false;
    }

    /**
     * Get total mastered crafting skills
     *
     * @return total number of mastered crafting skills
     */
    private static int getTotalMasterCraftingSkills(Player player) {
        int mastered = 0;

        Iterator<Integer> it = craftingSkillIds.iterator();
        while (it.hasNext()) {
            int skillId = it.next();
            int skillLvl = 0;
            if (player.getSkillList().isSkillPresent(skillId)) {
                skillLvl = player.getSkillList().getSkillLevel(skillId);
                if (skillLvl > 499)
                    mastered++;
            }
        }

        return mastered;
    }

    /**
     * Check if player can learn more master crafting skill or not (max is 2 including master-tier skills)
     *
     * @return true or false
     */
    private static boolean canLearnMoreMasterCraftingSkill(Player player) {
        if (getTotalMasterCraftingSkills(player) < 3)
            return true;
        else
            return false;
    }

    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder {
        protected static final CraftSkillUpdateService instance = new CraftSkillUpdateService();
    }
}
