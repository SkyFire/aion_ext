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
package gameserver.controllers;

import com.aionemu.commons.utils.Rnd;
import gameserver.controllers.movement.StartMovingListener;
import gameserver.dataholders.DataManager;
import gameserver.model.DescriptionId;
import gameserver.model.gameobjects.Gatherable;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.RewardType;
import gameserver.model.templates.GatherableTemplate;
import gameserver.model.templates.gather.Material;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.network.aion.serverpackets.SM_DELETE;
import gameserver.network.aion.serverpackets.SM_GATHERABLE_INFO;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.services.ItemService;
import gameserver.services.RespawnService;
import gameserver.skillengine.task.GatheringTask;
import gameserver.utils.PacketSendUtility;
import gameserver.world.World;

import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author ATracer, sphinx (03/20/2010)
 */
public class GatherableController extends VisibleObjectController<Gatherable> {
    private int gatherCount;
    private int currentGatherer;
    private GatheringTask task;

    public enum GatherState {
        GATHERED,
        GATHERING,
        IDLE
    }

    private GatherState state = GatherState.IDLE;

    /**
     * Start gathering process
     *
     * @param player
     */
    public void onStartUse(final Player player) {
        //basic actions, need to improve here
        final GatherableTemplate template = this.getOwner().getObjectTemplate();

        if (!checkPlayerSkill(player, template))
            return;

        List<Material> materials = template.getMaterials().getMaterial();

        int index = 0;
        Material material = materials.get(index); //default is 0
        int count = materials.size();

        if (count < 1) {
            //error - theoretically if XML data is correct, this should never happen.
            return;
        } else if (count == 1) {
            //default is 0
        } else {
            // need space in inventory
            long storedMaterialCount = player.getInventory().getItemCountByItemId(material.getItemid());
            // player doesn't have material in inventory
            if (storedMaterialCount == 0) {
                if (player.getInventory().getNumberOfFreeSlots() == 0) {
                    PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.EXTRACT_GATHER_INVENTORY_IS_FULL());
                    return;
                }
            }
            // player already has material in inventory
            else {
                ItemTemplate materialItemTemplate = ItemService.getItemTemplate(material.getItemid());
                if (storedMaterialCount >= materialItemTemplate.getMaxStackCount() && player.getInventory().getNumberOfFreeSlots() == 0) {
                    PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.EXTRACT_GATHER_INVENTORY_IS_FULL());
                    return;
                }
            }

            int gatherRate = 1; // 1x rates (probably make config later, if fixed to non-linear statistic probability)
            float maxrate = 0;
            int rate = 0;
            int i = 0; //index counter

            //sort materials to ascending order
            SortedMap<Integer, Integer> hasMat = new TreeMap<Integer, Integer>();
            for (Material mat : materials) {
                maxrate += mat.getRate(); //get maxrate
                hasMat.put(mat.getRate(), i); //sort and save index of materials (key is rate and rate is unique on each gatherId)
                i++;
            }

            Iterator<Integer> it = hasMat.keySet().iterator();
            while (it.hasNext()) {
                rate = it.next();
                float percent = Rnd.get() * 100f;
                float chance = ((rate / maxrate) * 100f * gatherRate);

                // default index is to 0, look to up little bit on 'material'
                if (percent < chance) {
                    index = hasMat.get(rate); //return index
                    material = materials.get(index);
                    break;
                }
            }
        }

        final Material finalMaterial = material;

        if (state != GatherState.GATHERING) {
            state = GatherState.GATHERING;
            currentGatherer = player.getObjectId();
            player.getObserveController().attach(new StartMovingListener() {

                @Override
                public void moved() {
                    finishGathering(player);
                }
            });
            int skillLvlDiff = player.getSkillList().getSkillLevel(template.getHarvestSkill()) - template.getSkillLevel();
            task = new GatheringTask(player, getOwner(), finalMaterial, skillLvlDiff);
            task.start();
        }
    }

    /**
     * Checks whether player have needed skill for gathering and skill level is sufficient
     *
     * @param player
     * @param template
     * @return
     */
    private boolean checkPlayerSkill(final Player player, final GatherableTemplate template) {
        int harvestSkillId = template.getHarvestSkill();

        //check skill is available
        if (!player.getSkillList().isSkillPresent(harvestSkillId)) {
            //TODO send some message ?
            return false;
        }
        if (player.getSkillList().getSkillLevel(harvestSkillId) < template.getSkillLevel()) {
            //TODO send some message ?
            return false;
        }
        return true;
    }

    public void completeInteraction(Player player) {
        state = GatherState.IDLE;
        gatherCount++;
        if (gatherCount >= getOwner().getObjectTemplate().getHarvestCount()) {
            onDie();
            PacketSendUtility.sendPacket(player, new SM_DELETE(getOwner(), 1));
        }
    }

    public void rewardPlayer(Player player) {
        if (player != null) {
            int skillLvl = getOwner().getObjectTemplate().getSkillLevel();
            int xpReward = (int) ((0.008 * (skillLvl + 100) * (skillLvl + 100) + 60));

            if (player.getSkillList().addSkillXp(player, getOwner().getObjectTemplate().getHarvestSkill(), (int)RewardType.GATHERING.calcReward(player, xpReward))) {
                PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.EXTRACT_GATHER_SUCCESS_GETEXP());
                player.getCommonData().addExp(xpReward, RewardType.GATHERING);
            } else
                PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.MSG_DONT_GET_PRODUCTION_EXP(new DescriptionId(DataManager.SKILL_DATA.getSkillTemplate(getOwner().getObjectTemplate().getHarvestSkill()).getNameId())));

        }
    }

    /**
     * Called by client when some action is performed or on finish gathering
     * Called by move observer on player move
     *
     * @param player
     */
    public void finishGathering(Player player) {
        if (currentGatherer == player.getObjectId()) {
            if (state == GatherState.GATHERING) {
                task.abort();
            }
            currentGatherer = 0;
            state = GatherState.IDLE;
        }
    }

    private void onDie() {
        Gatherable owner = getOwner();
        RespawnService.scheduleRespawnTask(owner);
        World.getInstance().despawn(owner);
        owner.getController().delete();
    }

    @Override
    public void onRespawn() {
        PacketSendUtility.broadcastPacket(getOwner(), new SM_GATHERABLE_INFO(getOwner()));
        this.gatherCount = 0;
    }

    @Override
    public Gatherable getOwner()
	{
		return super.getOwner();
	}
}
