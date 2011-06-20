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
import gameserver.model.Race;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.PersistentState;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.SkillListEntry;
import gameserver.model.items.ItemSlot;
import gameserver.model.templates.item.RequireSkill;
import gameserver.model.templates.item.Stigma;
import gameserver.network.aion.serverpackets.SM_SKILL_LIST;
import gameserver.network.aion.serverpackets.SM_STIGMA_SKILL_REMOVE;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.utils.PacketSendUtility;
import gameserver.configs.main.CustomConfig;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * @author ATracer, 10ton, ZeroSignal
 */
public class StigmaService {
    private static final Logger log = Logger.getLogger(StigmaService.class);

    /**
     * @param resultItem
     */
    public static boolean notifyEquipAction(Player player, Item resultItem, int slot) {
        if (resultItem.getItemTemplate().isStigma()) {
            int currentStigmaCount = player.getEquipment().getEquippedItemsStigma().size();
            int currentAdvancedStigmaCount = player.getEquipment().getEquippedItemsAdvancedStigma().size();

            if (slot >= ItemSlot.STIGMA1.getSlotIdMask() && slot <= ItemSlot.STIGMA6.getSlotIdMask() &&
                player.getCommonData().getStigmaSlotSize() <= currentStigmaCount)
            {   
                log.info("[AUDIT]Possible client hack stigma count big player: " + player.getName());
                 return false;
            }
            if (slot >= ItemSlot.ADV_STIGMA1.getSlotIdMask() && slot <= ItemSlot.ADV_STIGMA5.getSlotIdMask() &&
                player.getCommonData().getAdvancedStigmaSlotSize() <= currentAdvancedStigmaCount)
            {
                log.info("[AUDIT]Possible client hack advanced stigma count big player: " + player.getName());
                 return false;
            }

            if (!resultItem.getItemTemplate().isClassSpecific(player.getCommonData().getPlayerClass())) {
                log.info("[AUDIT]Possible client hack not valid for class. player: " + player.getName());
                return false;
            }

            Stigma stigmaInfo = resultItem.getItemTemplate().getStigma();

            if (stigmaInfo == null) {
                log.warn("Stigma info missing for item: " + resultItem.getItemTemplate().getTemplateId());
                return false;
            }

            int skillId = stigmaInfo.getSkillid();
            int shardCount = stigmaInfo.getShard();
            if (player.getInventory().getItemCountByItemId(141000001) < shardCount) {
                log.info("[AUDIT]Possible client hack stigma shard count low player: " + player.getName());
                return false;
            }
            int needSkill = stigmaInfo.getRequireSkill().size();
            for (RequireSkill rs : stigmaInfo.getRequireSkill()) {
                for (int id : rs.getSkillId()) {
                    if (player.getSkillList().isSkillPresent(id))
                        needSkill--;
                    break;
                }
            }
            if (needSkill != 0) {
                log.info("[AUDIT]Possible client hack advanced stigma skill player: " + player.getName());
            }

            player.getInventory().removeFromBagByItemId(141000001, shardCount);
            SkillListEntry skill = new SkillListEntry(skillId, true, stigmaInfo.getSkilllvl(), PersistentState.NOACTION);
            player.getSkillList().addSkill(skill);
            PacketSendUtility.sendPacket(player, new SM_SKILL_LIST(skill, 1300401));
        }
        return true;
    }

    /**
     * @param resultItem
     */
    public static boolean notifyUnequipAction(Player player, Item resultItem) {
        if (resultItem.getItemTemplate().isStigma()) {

            Stigma stigmaInfo = resultItem.getItemTemplate().getStigma();
            int skillId = stigmaInfo.getSkillid();
            for (Item item : player.getEquipment().getEquippedItemsStigma()) {
                Stigma si = item.getItemTemplate().getStigma();
                if (resultItem == item || si == null)
                    continue;
                for (RequireSkill rs : si.getRequireSkill()) {
                    if (rs.getSkillId().contains(skillId)) {
                        PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300410, new DescriptionId(resultItem.getItemTemplate().getNameId()), new DescriptionId(item.getItemTemplate().getNameId())));
                        return false;
                    }
                }
            }

            // check the stigma depends on advanced stimga
            for (Item item : player.getEquipment().getEquippedItemsAdvancedStigma()) {
                Stigma si = item.getItemTemplate().getStigma();
                if (resultItem == item || si == null)
                    continue;
                for (RequireSkill rs : si.getRequireSkill()) {
                    if (rs.getSkillId().contains(skillId)) {
                        PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300410, new DescriptionId(resultItem.getItemTemplate().getNameId()), new DescriptionId(item.getItemTemplate().getNameId())));
                        return false;
                    }
                }
            }
            
            player.getSkillList().removeSkill(skillId);
            int nameId = DataManager.SKILL_DATA.getSkillTemplate(skillId).getNameId();
            PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300403, new DescriptionId(nameId)));
            PacketSendUtility.sendPacket(player, new SM_STIGMA_SKILL_REMOVE(skillId));
        }
        return true;
    }

    /**
     * @param player
     */
    public static void onPlayerLogin(Player player) {
        if (CustomConfig.STIGMA_AUTOLEARN)
            return;

        List<Item> stigmaItems = player.getEquipment().getEquippedItemsAllStigma();

        for (Item item : stigmaItems) {
            Stigma stigmaInfo = item.getItemTemplate().getStigma();
            if (stigmaInfo == null) {
                log.warn("Stigma info missing for item: " + item.getItemTemplate().getTemplateId());
                return;
            }
            int skillId = stigmaInfo.getSkillid();
            SkillListEntry skill = new SkillListEntry(skillId, true, stigmaInfo.getSkilllvl(), PersistentState.NOACTION);
            player.getSkillList().addSkill(skill);
        }

        for (Item item : stigmaItems) {
            if(!isPossibleEquippedStigma(player, item)) {
                log.info("[AUDIT]Possible client hack stigma count big :O player: " + player.getName());
                player.getEquipment().unEquipItem(item.getObjectId(), 0);
                continue;
            }

            Stigma stigmaInfo = item.getItemTemplate().getStigma();
            int needSkill = stigmaInfo.getRequireSkill().size();
            for (RequireSkill rs : stigmaInfo.getRequireSkill()) {
                for (int id : rs.getSkillId()) {
                    if (player.getSkillList().isSkillPresent(id)) {
                        needSkill--;
                        break;
                    }
                }
            }
            if (needSkill != 0) {
                log.info("[AUDIT]Possible client hack stigma skill player: " + player.getName());
                player.getEquipment().unEquipItem(item.getObjectId(), 0);
                continue;
            }
            if (!item.getItemTemplate().isClassSpecific(player.getCommonData().getPlayerClass())) {
                log.info("[AUDIT]Possible client hack not valid for class. player: " + player.getName());
                player.getEquipment().unEquipItem(item.getObjectId(), 0);
                continue;
            }
        }
    }

    private static boolean isPossibleEquippedStigma(Player player, Item item)
    {
        if(player == null || item == null || !item.getItemTemplate().isStigma())
            return false;

        int itemSlotToEquip = item.getEquipmentSlot();
        if(itemSlotToEquip >= ItemSlot.STIGMA1.getSlotIdMask() &&
           itemSlotToEquip <= ItemSlot.STIGMA6.getSlotIdMask())
        {
            int stigmaCount = player.getCommonData().getStigmaSlotSize();
            if(stigmaCount <= 0)
                return false;
            if(stigmaCount == 6)
                return true;

            int[] itemSlotStigma = {
                ItemSlot.STIGMA1.getSlotIdMask(),
                ItemSlot.STIGMA2.getSlotIdMask(),
                ItemSlot.STIGMA3.getSlotIdMask(),
                ItemSlot.STIGMA4.getSlotIdMask(),
                ItemSlot.STIGMA5.getSlotIdMask(),
            };         
            for(int i = 0; i < stigmaCount; ++i) {
                if (itemSlotToEquip == itemSlotStigma[i])
                    return true;
            }
        }
        else if(itemSlotToEquip >= ItemSlot.ADV_STIGMA1.getSlotIdMask() &&
            itemSlotToEquip <= ItemSlot.ADV_STIGMA5.getSlotIdMask())
        {
            int advStigmaCount = player.getCommonData().getAdvancedStigmaSlotSize();
            if(advStigmaCount <= 0)
                return false;
            if(advStigmaCount >= 5)
                return true;

            int[] itemSlotAdvStigma = {
                ItemSlot.ADV_STIGMA1.getSlotIdMask(),
                ItemSlot.ADV_STIGMA2.getSlotIdMask(),
                ItemSlot.ADV_STIGMA3.getSlotIdMask(),
                ItemSlot.ADV_STIGMA4.getSlotIdMask(),
            };
            for(int i = 0; i < advStigmaCount; ++i) {
                if (itemSlotToEquip == itemSlotAdvStigma[i])
                    return true;
            }
        }
        return false;
    }
}

