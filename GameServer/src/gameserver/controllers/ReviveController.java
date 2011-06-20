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

import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.Kisk;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.*;
import gameserver.services.TeleportService;
import gameserver.skillengine.SkillEngine;
import gameserver.skillengine.effect.EffectTemplate;
import gameserver.skillengine.effect.RebirthEffect;
import gameserver.skillengine.model.Effect;
import gameserver.skillengine.model.Skill;
import gameserver.utils.PacketSendUtility;

import java.util.List;

/**
 * @author Jego
 */
public class ReviveController {

    private Player player;
    private int rebirthResurrectPercent;
    private boolean toBeTeleported;
    private VisibleObject teleportTarget;

    public ReviveController(Player player) {
        this.player = player;
        this.rebirthResurrectPercent = 1;
    }

    /**
     *
     */
    public void skillRevive() {
        revive(10, 10);
        applySoulSickness();
        PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.RESURRECT), true);

        PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.REVIVE);
        PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));

        if (toBeTeleported && teleportTarget != null)
        	TeleportService.teleportTo(player, teleportTarget.getWorldId(), teleportTarget.getInstanceId(), teleportTarget.getX(), teleportTarget.getY(), teleportTarget.getZ(), teleportTarget.getHeading(), 0);
        
        if (player.isInPrison())
            TeleportService.teleportToPrison(player);
    }

    /**
     *
     */
    public void rebirthRevive() {
        if (rebirthResurrectPercent <= 0) {
            PacketSendUtility.sendMessage(player, "Error: Rebirth effect missing percent.");
            rebirthResurrectPercent = 5;
        }
        revive(rebirthResurrectPercent, rebirthResurrectPercent);
        applySoulSickness();
        PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.RESURRECT), true);

        PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.REVIVE);
        PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));

        if (player.isInPrison())
            TeleportService.teleportToPrison(player);
    }

    /**
     *
     */
    public void bindRevive() {
        revive(25, 25);
        applySoulSickness();
        PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.REVIVE);
        // TODO: It is not always necessary.
        // sendPacket(new SM_QUEST_LIST(activePlayer));
        PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
        PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, false));

        if (player.isInPrison())
            TeleportService.teleportToPrison(player);
        else
            TeleportService.moveToBindLocation(player, true);
    }

    public void kiskRevive() {
        Kisk kisk = player.getKisk();
        if (kisk == null) {
            bindRevive();
            return;
        }

        revive(25, 25);
        PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.REVIVE);

        PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
        PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, false));

        if (player.isInPrison())
            TeleportService.teleportToPrison(player);
        else {
            TeleportService.moveToKiskLocation(player);
            kisk.resurrectionUsed(player);
        }
    }

    private void revive(int hpPercent, int mpPercent) {
        player.getLifeStats().setCurrentHpPercent(hpPercent);
        player.getLifeStats().setCurrentMpPercent(mpPercent);
        player.getCommonData().setDp(0);
        player.getLifeStats().triggerRestoreOnRevive();

        player.getController().onRespawn();
    }

    /**
     *
     */
    public void itemSelfRevive() {
        Item item = getSelfRezStone(player);
        if (item == null) {
            // Fake Packet Spoof? Send SM_DIE again?
            PacketSendUtility.sendMessage(player, "Error: Couldn't find self-rez item.");
            return;
        }

        // Add Cooldown and use item
        int useDelay = item.getItemTemplate().getDelayTime();
        player.addItemCoolDown(item.getItemTemplate().getDelayId(), System.currentTimeMillis() + useDelay, useDelay / 1000);
        PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(),
                item.getObjectId(), item.getItemTemplate().getTemplateId()), true);
        player.getInventory().removeFromBagByObjectId(item.getObjectId(), 1);

        // Tombstone Self-Rez retail verified 15%
        revive(15, 15);
        applySoulSickness();
        PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.RESURRECT), true);

        PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.REVIVE);
        PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));

        if (player.isInPrison())
            TeleportService.teleportToPrison(player);
    }

    /**
     * Stone Use Order determined by highest inventory slot. :(
     * If player has two types, wrong one might be used.
     *
     * @param player
     * @return selfRezItem
     */
    private Item getSelfRezStone(Player player) {
        Item item = null;
        item = tryStone(161001001);
        if (item == null)
            item = tryStone(161000003);
        if (item == null)
            item = tryStone(161000004);
        if (item == null)
            item = tryStone(161000001);
        return item;
    }

    /**
     * @param stoneItemId
     * @return stoneItem or null
     */
    private Item tryStone(int stoneId) {
        Item item = player.getInventory().getFirstItemByItemId(stoneId);
        if (item != null && player.isItemUseDisabled(item.getItemTemplate().getDelayId()))
            item = null;
        return item;
    }

    /**
     * Need to find how an item is determined as able to self-rez.
     *
     * @param player
     * @return boolean can self rez with item
     */
    public boolean checkForSelfRezItem(Player player) {
        return (getSelfRezStone(player) != null);
    }

    /**
     * Rebirth Effect is id 160.
     *
     * @param player2
     * @return
     */
    public boolean checkForSelfRezEffect(Player player) {
        //Store the effect info.
        List<Effect> effects = player.getEffectController().getAbnormalEffects();
        for (Effect effect : effects) {
            for (EffectTemplate template : effect.getEffectTemplates()) {
                if (template.getEffectid() == 160 && template instanceof RebirthEffect) {
                    RebirthEffect rebirthEffect = (RebirthEffect) template;
                    rebirthResurrectPercent = rebirthEffect.getResurrectPercent();
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Apply Soul Sickness effect to the revived player
     */
    private void applySoulSickness() {
        Skill skill = SkillEngine.getInstance().getSkill(player,8291,1,player);
		skill.useSkill();
	}

	/**
	 * @param toBeTeleported the toBeTeleported to set
	 */
	public void setToBeTeleported(boolean toBeTeleported)
	{
		this.toBeTeleported = toBeTeleported;
	}

	/**
	 * @return the toBeTeleported
	 */
	public boolean isToBeTeleported()
	{
		return toBeTeleported;
	}

	/**
	 * @param teleportTarget the teleportTarget to set
	 */
	public void setTeleportTarget(VisibleObject teleportTarget)
	{
		this.teleportTarget = teleportTarget;
	}

	/**
	 * @return the teleportTarget
	 */
	public VisibleObject getTeleportTarget()
	{
		return teleportTarget;
	}
}
