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
package gameserver.network.aion;

import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.stats.modifiers.SimpleModifier;
import gameserver.model.gameobjects.stats.modifiers.StatModifier;
import gameserver.model.items.FusionStone;
import gameserver.model.items.ItemSlot;
import gameserver.model.items.ItemStone;
import gameserver.model.items.ManaStone;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.services.RentalService;

import java.nio.ByteBuffer;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * @author ATracer, ZeroSignal
 */
public abstract class InventoryPacket extends AionServerPacket {
    private static Logger log = Logger.getLogger(InventoryPacket.class);

    /**
     * The header of every item block
     *
     * @param buf
     * @param item
     */
    protected void writeGeneralInfo(ByteBuffer buf, Item item) {
        writeD(buf, item.getObjectId());
        ItemTemplate itemTemplate = item.getItemTemplate();
        writeD(buf, itemTemplate.getTemplateId());
        writeH(buf, 0x24);
        writeD(buf, itemTemplate.getNameId());
        writeH(buf, 0);
    }

    protected void writeMailGeneralInfo(ByteBuffer buf, Item item) {
        writeD(buf, item.getObjectId());
        ItemTemplate itemTemplate = item.getItemTemplate();
        writeD(buf, itemTemplate.getTemplateId());
        writeD(buf, 1);
        writeD(buf, 0);
        writeH(buf, 0x24);
        writeD(buf, itemTemplate.getNameId());
        writeH(buf, 0);
    }

    /**
     * All misc items
     *
     * @param buf
     * @param item
     */
    protected void writeGeneralItemInfo(ByteBuffer buf, Item item, boolean privateStore, boolean mail) {
        //length of details
        if (privateStore)
            writeH(buf, 0x16);
        else
            writeH(buf, 0x1A);

        writeC(buf, 0);
        writeH(buf, item.getItemMask());
        writeQ(buf, item.getItemCount());
        writeD(buf, 0); //Disappears time
        writeD(buf, 0);
        if (!privateStore) {
            writeD(buf, 0);
            writeH(buf, 0);
        }
        writeC(buf, 0);
        if (!mail)
            writeH(buf, item.getEquipmentSlot()); // not equipable items
    }

    protected void writeStigmaInfo(ByteBuffer buf, Item item) {
        writeH(buf, 325); //length of details 45 01
        writeC(buf, 0x6);
        if (item.isEquipped())
            writeD(buf, item.getEquipmentSlot());
        else
            writeD(buf, 0);
        writeC(buf, 0x7);
        writeH(buf, 702); //skill id
        writeD(buf, 0);
        writeH(buf, 0);
        writeD(buf, 0x3c);  //0x3c

        writeD(buf, 0);
        writeD(buf, 0);

        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);

        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 1);//1
        writeD(buf, 0);

        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);

        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeH(buf, 0);
        writeH(buf, 0x0b); //0b

        writeC(buf, 0);
        writeD(buf, item.getItemTemplate().getTemplateId());

        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeC(buf, 0);

        writeD(buf, 82750); //3E 43 01 00

        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeC(buf, 0);

        writeC(buf, 0x22); // 22
        writeH(buf, 0);
    }

    /**
     * @param buf
     * @param item
     */
    protected void writeKinah(ByteBuffer buf, Item item, boolean isInventory) {
        writeH(buf, 0x1A); //length of details
        writeC(buf, 0);
        writeH(buf, item.getItemMask());
        writeQ(buf, item.getItemCount());
        writeD(buf, 0);
        writeD(buf, 0);
        writeH(buf, 0);
        writeC(buf, 0);
        writeD(buf, 0);
        writeH(buf, 255); // FF FF equipment
        if (isInventory)
            writeC(buf, 0);
    }

    /**
     * Write weapon info for non weapon switch items
     *
     * @param buf
     * @param item
     * @param isInventory
     */
    protected void writeWeaponInfo(ByteBuffer buf, Item item, boolean isInventory) {
        this.writeWeaponInfo(buf, item, isInventory, false, false, false);
    }

    /**
     * For all weapon. Weapon is identified by weapon_type in xml
     *
     * @param buf
     * @param item
     */
    protected void writeWeaponInfo(ByteBuffer buf, Item item, boolean isInventory, boolean isWeaponSwitch, boolean privateStore, boolean mail) {
        int itemSlotId = item.getEquipmentSlot();
        int sizeLoc;

        sizeLoc = buf.position();
        writeH(buf, 5);

        if (!isWeaponSwitch && item.getItemTemplate().getWeaponType().getRequiredSlots() == 2) {
            writeC(buf, 0x0E);
            writeD(buf, item.hasFusionedItem() ? item.getFusionedItem() : 0);
            writeFusionStones(buf, item);
            writeC(buf, item.hasOptionalFusionSocket() ? item.getOptionalFusionSocket() : 0x00);
        }

        writeC(buf, 0x06);
        writeD(buf, item.isEquipped() ? itemSlotId : 0x00);

        if (isWeaponSwitch)
            return;

        writeC(buf, 0x01);
        writeD(buf, ItemSlot.getSlotsFor(item.getItemTemplate().getItemSlot()).get(0).getSlotIdMask());
        writeD(buf, item.hasFusionedItem() ? 0x00 : 0x02);
        writeC(buf, 0x0B); //? some details separator
        writeC(buf, item.isSoulBound() ? 1 : 0);
        writeC(buf, item.getEnchantLevel()); //enchant (1-15)
        writeD(buf, item.getItemSkinTemplate().getTemplateId());
        writeC(buf, item.hasOptionalSocket() ? item.getOptionalSocket() : 0x00);

        writeItemStones(buf, item);

        ItemStone god = item.getGodStone();
        writeD(buf, god == null ? 0 : god.getItemId());

        writeD(buf, 0);

        writeD(buf, 0);//unk 1.5.1.9

        /*
            * This is where item bonuses should be inserted.
            * The format is as follows:
            * writeH(buf, 2560); 0x000A
            * writeH(buf, bonusType); //ex. 0x12 is +HP
            * writeD(buf, bonusAmount); //ex. 0xC4 is 196
            */

        writeH(buf, 0x00); // seperator between item bonus and item mask

        writeH(buf, item.getItemMask());
        writeQ(buf, item.getItemCount());
        if (privateStore)
            writeH(buf, 0);
        else
            writeS(buf, item.getItemCreator()); // PlayerObjId of crafter
        writeC(buf, 0);
        writeD(buf, RentalService.getInstance().getRentalTimeLeft(item)); // For temp items: Remaining seconds
        writeC(buf, 0);
        writeD(buf, 0);
        if (!privateStore)
            writeH(buf, 0);
        writeC(buf, 0);

        int placeHolder, size;
        size = (privateStore) ? 103 : (buf.position() - sizeLoc - 2);
        placeHolder = buf.position();
        buf.position(sizeLoc);
        writeH(buf, size);
        buf.position(placeHolder);

        if (!mail) writeH(buf, item.isEquipped() ? 255 : item.getEquipmentSlot()); // FF FF equipment
        if (isInventory) writeC(buf, 0); // item.isEquipped() ? 1 : 0
    }

    /**
     * Writes manastones : 6C - statenum mask, 6H - value
     *
     * @param buf
     * @param item
     */
    private void writeItemStones(ByteBuffer buf, Item item) {
        int count = 0;
        //TODO implement new area to store the values for the manastones
        if (item.hasManaStones()) {
            Set<ManaStone> itemStones = item.getItemStones();

            for (ManaStone itemStone : itemStones) {
                if (count == 6)
                    break;

                StatModifier modifier = itemStone.getFirstModifier();
                if (modifier != null) {
                    count++;
                    writeC(buf, modifier.getStat().getItemStoneMask());
                }
            }
            writeB(buf, new byte[(6 - count)]);
            count = 0;
            for (ManaStone itemStone : itemStones) {
                if (count == 6)
                    break;

                StatModifier modifier = itemStone.getFirstModifier();
                if (modifier != null) {
                    count++;
                    writeH(buf, ((SimpleModifier) modifier).getValue());
                }
            }
            writeB(buf, new byte[(6 - count) * 2]);
        } else {
            writeB(buf, new byte[18]);
        }

        //for now max 6 stones - write some junk
    }

    private void writeFusionStones(ByteBuffer buf, Item item) {
        int count = 0;

        if (item.hasFusionStones()) {
            Set<FusionStone> itemStones = item.getFusionStones();

            for (FusionStone itemStone : itemStones) {
                if (count == 6)
                    break;

                StatModifier modifier = itemStone.getFirstModifier();
                if (modifier != null) {
                    count++;
                    writeC(buf, modifier.getStat().getItemStoneMask());
                    writeH(buf, ((SimpleModifier) modifier).getValue());
                }
            }
            writeB(buf, new byte[(6 - count) * 3]);
        } else {
            writeB(buf, new byte[18]);
        }

        //for now max 6 stones - write some junk
    }

    /**
     * For all armor. Armor is identified by armor_type in xml
     *
     * @param buf
     * @param item
     */
    protected void writeArmorInfo(ByteBuffer buf, Item item, boolean isInventory, boolean privateStore, boolean mail) {
        int itemSlotId = item.getEquipmentSlot();

        int sizeLoc = buf.position();
        writeH(buf, 83);

        writeC(buf, 0x06);
        writeD(buf, item.isEquipped() ? itemSlotId : 0);
        writeC(buf, 0x02);
        writeD(buf, ItemSlot.getSlotsFor(item.getItemTemplate().getItemSlot()).get(0).getSlotIdMask());
        writeD(buf, 0);
        writeD(buf, 0);
        writeC(buf, 0x0B); //? some details separator
        writeC(buf, item.isSoulBound() ? 1 : 0);
        writeC(buf, item.getEnchantLevel()); //enchant (1-15)
        writeD(buf, item.getItemSkinTemplate().getTemplateId());
        writeC(buf, item.hasOptionalSocket() ? item.getOptionalSocket() : 0x00);

        writeItemStones(buf, item);

        writeC(buf, 0);
        writeD(buf, item.getItemColor());

        writeD(buf, 0);
        writeD(buf, 0); //unk 1.5.1.9
        writeC(buf, 0);
        writeH(buf, item.getItemMask());
        writeQ(buf, item.getItemCount());
        if (privateStore)
            writeH(buf, 0);
        else
            writeS(buf, item.getItemCreator()); // PlayerObjId of crafter
        writeC(buf, 0);
        writeD(buf, RentalService.getInstance().getRentalTimeLeft(item)); // For temp items: Remaining seconds
        writeC(buf, 0);
        writeD(buf, 0);
        if (!privateStore)
            writeH(buf, 0);
        writeC(buf, 0);

        if (!privateStore) {
            int placeHolder, size;
            size = buf.position() - sizeLoc - 2;
            placeHolder = buf.position();
            buf.position(sizeLoc);
            writeH(buf, size);
            buf.position(placeHolder);
        }

        if (!mail)
            writeH(buf, item.isEquipped() ? 255 : item.getEquipmentSlot()); // FF FF equipment
        if (isInventory)
            writeC(buf, 1); //item.isEquipped() ? 1 : 0

    }
}
