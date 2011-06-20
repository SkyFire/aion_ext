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
package gameserver.model.items;

import com.aionemu.commons.utils.Rnd;
import gameserver.controllers.movement.ActionObserver;
import gameserver.controllers.movement.ActionObserver.ObserverType;
import gameserver.model.DescriptionId;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.PersistentState;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.item.GodstoneInfo;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.services.ItemService;
import gameserver.skillengine.SkillEngine;
import gameserver.skillengine.model.Skill;
import gameserver.utils.PacketSendUtility;
import org.apache.log4j.Logger;

import java.util.Collection;

/**
 * @author ATracer, ggadv2
 */
public class GodStone extends ItemStone {
    private static final Logger log = Logger.getLogger(GodStone.class);

    private final GodstoneInfo godstoneInfo;
    private ActionObserver actionListener;

    public GodStone(int itemObjId, int itemId, PersistentState persistentState) {
        super(itemObjId, itemId, 0, ItemStoneType.GODSTONE, persistentState);
        ItemTemplate itemTemplate = ItemService.getItemTemplate(itemId);
        godstoneInfo = itemTemplate.getGodstoneInfo();

        if (godstoneInfo == null)
            log.warn("CHECKPOINT: Godstone info missing for item : " + itemId);
    }

    /**
     * @param player
     */
    public void onEquip(final Player player, final Item item) {
        if (godstoneInfo == null || item == null)
            return;

        actionListener = new ActionObserver(ObserverType.ATTACK) {
            @Override
            public void attack(Creature creature) {
                if (godstoneInfo != item.getGodStone().getGodstoneInfo()) {
                    onUnEquip(player);
                    return;
                }

                // Check probability for this godstone, also check if the godstone effect actually comes from main or sub weapon
                int probability = 0;
                if (item.getEquipmentSlot() == ItemSlot.MAIN_HAND.getSlotIdMask())
                    probability = godstoneInfo.getProbability();
                else if (item.getEquipmentSlot() == ItemSlot.SUB_HAND.getSlotIdMask())
                    probability = godstoneInfo.getProbabilityleft();
                else {
                    onUnEquip(player);
                    return;
                }

                Skill skill = SkillEngine.getInstance().getSkill(player, godstoneInfo.getSkillid(), godstoneInfo.getSkilllvl(), player.getTarget());

                attachGodstoneEffect(player, probability, skill);
                player.getObserveController().notifyGodstoneObservers((Creature) player);
            }
        };

        player.getObserveController().addObserver(actionListener);
    }

    /**
     * Attach effect
     */
    public void attachGodstoneEffect(final Player player, final int probability, final Skill skill) {
        player.getObserveController().attach(
            new ActionObserver(ObserverType.GODSTONE) {
                @Override
                public void onGodstone(Creature creature) {
                    if (Rnd.get(0, probability) > Rnd.get(0, 1000)) {
                        PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1301062, new DescriptionId(skill.getSkillTemplate().getNameId())));
                        skill.useSkill();
                    }
                }
            }
        );
    }

    /**
     * Return godstoneInfo
     */
    public GodstoneInfo getGodstoneInfo() {
        return godstoneInfo;
    }

    /**
     * @param player
     */
    public void onUnEquip(Player player) {
        if (actionListener != null)
            player.getObserveController().removeObserver(actionListener);
    }
}
