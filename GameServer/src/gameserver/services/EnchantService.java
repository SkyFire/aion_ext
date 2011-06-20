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

import com.aionemu.commons.utils.Rnd;
import gameserver.configs.main.EnchantsConfig;
import gameserver.model.DescriptionId;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.PersistentState;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.Storage;
import gameserver.model.gameobjects.stats.StatEnum;
import gameserver.model.gameobjects.stats.id.EnchantStatEffectId;
import gameserver.model.gameobjects.stats.listeners.ItemEquipmentListener;
import gameserver.model.gameobjects.stats.modifiers.AddModifier;
import gameserver.model.gameobjects.stats.modifiers.RateModifier;
import gameserver.model.gameobjects.stats.modifiers.StatModifier;
import gameserver.model.items.FusionStone;
import gameserver.model.items.ManaStone;
import gameserver.model.templates.item.ArmorType;
import gameserver.model.templates.item.ItemQuality;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.model.templates.item.WeaponType;
import gameserver.network.aion.serverpackets.SM_DELETE_ITEM;
import gameserver.network.aion.serverpackets.SM_STATS_INFO;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.network.aion.serverpackets.SM_UPDATE_ITEM;
import gameserver.utils.PacketSendUtility;
import org.apache.log4j.Logger;

import java.util.Set;
import java.util.TreeSet;

/**
 * @author ATracer, ZeroSignal
 */
public class EnchantService {
    private static final Logger log = Logger.getLogger(EnchantService.class);

    /**
     * @param player
     * @param targetItem
     * @param parentItem
     */
    public static boolean breakItem(Player player, Item targetItem, Item parentItem) {
        Storage inventory = player.getInventory();

        if (!inventory.isItemByObjId(targetItem.getObjectId()))
            return false;
        if (!inventory.isItemByObjId(parentItem.getObjectId()))
            return false;

        ItemTemplate itemTemplate = targetItem.getItemTemplate();
        ItemQuality quality = itemTemplate.getItemQuality();

        int number = 0;
        int level = 0;
        switch (quality) {
            case COMMON:
            case JUNK:
                number = Rnd.get(1, 2);
                level = Rnd.get(-4, 5);
                break;
            case RARE:
                number = Rnd.get(1, 4);
                level = Rnd.get(-4, 10);
                break;
            case LEGEND:
            case MYTHIC:
                number = Rnd.get(1, 6);
                level = Rnd.get(-4, 20);
                break;
            case EPIC:
                number = Rnd.get(1, 8);
                level = Rnd.get(-4, 40);
                break;
            case UNIQUE:
                number = Rnd.get(1, 8);
                level = Rnd.get(-4, 40);
                break;
        }

        int enchantItemLevel = targetItem.getItemTemplate().getLevel() + level;
        int enchantItemId = 166000000 + enchantItemLevel;

        inventory.removeFromBag(targetItem, true);
        PacketSendUtility.sendPacket(player, new SM_DELETE_ITEM(targetItem.getObjectId()));

        inventory.removeFromBagByObjectId(parentItem.getObjectId(), 1);

        ItemService.addItem(player, enchantItemId, number);
        return true;
    }

    /**
     * @param player
     * @param parentItem
     * @param targetItem
     */
    public static boolean enchantItem(Player player, Item parentItem, Item targetItem, Item supplementItem) {
        // Check that parentItem is still in Inventory.
        if (!player.getInventory().isItemByObjId(parentItem.getObjectId()))
            return false;

        if (!player.getInventory().isItemByObjId(targetItem.getObjectId()) &&
            !player.getEquipment().isItemByObjId(targetItem.getObjectId()))
        {
            return false;
        }

        int enchantStoneLevel = parentItem.getItemTemplate().getLevel();
        int targetItemLevel = targetItem.getItemTemplate().getLevel();

        if (targetItem.getItemTemplate().isNoEnchant() || targetItemLevel > enchantStoneLevel)
            return false;

        int qualityCap = 0;

        ItemQuality quality = targetItem.getItemTemplate().getItemQuality();

        switch (quality) {
            case COMMON:
            case JUNK:
                qualityCap = 0;
                break;
            case RARE:
                qualityCap = 10;
                break;
            case LEGEND:
            case MYTHIC:
                qualityCap = 20;
                break;
            case EPIC:
            case UNIQUE:
                qualityCap = 25;
                break;
        }

        int success = 50;

        int levelDiff = enchantStoneLevel - targetItemLevel;

        int extraSuccess = levelDiff - qualityCap;
        if (extraSuccess > 0) {
            success += extraSuccess * 5;
        }

        if (supplementItem != null) {
            if (!player.getInventory().isItemByObjId(supplementItem.getObjectId()))
                return false;

            int supplementUseCount = 1;
            int addsuccessRate = 10;
            int supplementId = supplementItem.getItemTemplate().getTemplateId();
            int enchantstoneLevel = parentItem.getItemTemplate().getLevel();
            int enchantitemLevel = targetItem.getEnchantLevel() + 1;

            //lesser supplements
            if (supplementId == 166100000 || supplementId == 166100003 || supplementId == 166100006)
                addsuccessRate = EnchantsConfig.LSSUP;
            //supplements
            if (supplementId == 166100001 || supplementId == 166100004 || supplementId == 166100007)
                addsuccessRate = EnchantsConfig.RGSUP;
            //greater supplements
            if (supplementId == 166100002 || supplementId == 166100005 || supplementId == 166100008)
                addsuccessRate = EnchantsConfig.GRSUP;

            if (enchantstoneLevel > 30 && enchantstoneLevel < 41)
                supplementUseCount = 5;

            if (enchantstoneLevel > 40 && enchantstoneLevel < 51)
                supplementUseCount = 10;

            if (enchantstoneLevel > 50 && enchantstoneLevel < 61)
                supplementUseCount = 25;

            if (enchantstoneLevel > 60 && enchantstoneLevel < 71)
                supplementUseCount = 55;

            if (enchantstoneLevel > 70 && enchantstoneLevel < 81)
                supplementUseCount = 85;

            if (enchantstoneLevel > 80 && enchantstoneLevel < 91)
                supplementUseCount = 115;

            if (enchantstoneLevel > 90)
                supplementUseCount = 145;


            if (enchantitemLevel > 10)
                supplementUseCount = supplementUseCount * 2;

            player.getInventory().removeFromBagByItemId(supplementItem.getItemId(), supplementUseCount);

            //Add successRate
            success = success + addsuccessRate;
        }

        if (success >= 95)
            success = 95;

        boolean result = false;

        if (Rnd.get(0, 100) < success)
            result = true;

        int currentEnchant = targetItem.getEnchantLevel();

        if (!result) {
            // Retail: http://powerwiki.na.aiononline.com/aion/Patch+Notes:+1.9.0.1
            // When socketing fails at +11~+15, the value falls back to +10.
            if (currentEnchant > 10)
                currentEnchant = 10;
            else if (currentEnchant > 0)
                currentEnchant -= 1;
        } else {
            // Items that are Fabled or Eternal can get up to +15.
            ItemQuality targetQuality = targetItem.getItemTemplate().getItemQuality();
            if (targetQuality == ItemQuality.UNIQUE || targetQuality == ItemQuality.EPIC) {
                if (currentEnchant < 15)
                    currentEnchant += 1;
            } else {
                if (currentEnchant < 10)
                    currentEnchant += 1;
            }
        }

        if (targetItem.isEquipped())
            onItemUnequip(player, targetItem);

        targetItem.setEnchantLevel(currentEnchant);

        if (targetItem.isEquipped() && !targetItem.isWeaponSwapped(player))
            onItemEquip(player, targetItem);

        PacketSendUtility.sendPacket(player, new SM_UPDATE_ITEM(targetItem));

        if (targetItem.isEquipped())
            player.getEquipment().setPersistentState(PersistentState.UPDATE_REQUIRED);
        else
            player.getInventory().setPersistentState(PersistentState.UPDATE_REQUIRED);

        if (result) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ENCHANT_ITEM_SUCCEED(new DescriptionId(Integer
                    .parseInt(targetItem.getName()))));
        } else {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ENCHANT_ITEM_FAILED(new DescriptionId(Integer
                    .parseInt(targetItem.getName()))));
        }
        player.getInventory().removeFromBagByObjectId(parentItem.getObjectId(), 1);

        return result;
    }

    /**
     * @param player
     * @param parentItem
     * @param targetItem
     */
    public static boolean socketManastone(Player player, Item parentItem, Item targetItem, Item supplementItem, int targetWeapon) {
        // Check that parentItem is still in Inventory.
        if (!player.getInventory().isItemByObjId(parentItem.getObjectId()))
            return false;

        if (!player.getInventory().isItemByObjId(targetItem.getObjectId()) &&
            !player.getEquipment().isItemByObjId(targetItem.getObjectId()))
        {
            return false;
        }

        boolean result = false;
        int successRate = 76;

        int stoneCount;
        int checkCount;
        if (targetWeapon == 1) {
            stoneCount = targetItem.getItemStones().size();
            checkCount = targetItem.getMaxStoneSlots();
        } else {
            stoneCount = targetItem.getFusionStones().size();
            Item fusionedItem = ItemService.newItem(targetItem.getFusionedItem(), 1);
            checkCount = fusionedItem.getMaxStoneSlots();
        }

        if (stoneCount >= checkCount){
            log.info("[AUDIT] Possible use Manastone bug player  : " + player.getName());
            return false;
        }

        switch (stoneCount) {
            case 1:
                successRate = EnchantsConfig.MSPERCENT;
                break;
            case 2:
                successRate = EnchantsConfig.MSPERCENT1;
                break;
            case 3:
                successRate = EnchantsConfig.MSPERCENT2;
                break;
            case 4:
                successRate = EnchantsConfig.MSPERCENT3;
                break;
            case 5:
                successRate = EnchantsConfig.MSPERCENT4;
                break;
        }

        if (targetWeapon == 1) {
            if (stoneCount >= targetItem.getSockets(false))
                successRate = EnchantsConfig.MSPERCENT5;
        } else {
            if (stoneCount >= targetItem.getSockets(true))
                successRate = EnchantsConfig.MSPERCENT5;
        }

        if (supplementItem != null) {
            if (!player.getInventory().isItemByObjId(supplementItem.getObjectId()))
                return false;

            int supplementUseCount = 1;
            int addsuccessRate = 10;
            int supplementId = supplementItem.getItemTemplate().getTemplateId();
            int manastoneId = parentItem.getItemTemplate().getTemplateId();
            int manastoneLevel = parentItem.getItemTemplate().getLevel();
            int manastoneCount;
            if (targetWeapon == 1) {
                manastoneCount = targetItem.getItemStones().size() + 1;
            } else {
                manastoneCount = targetItem.getFusionStones().size() + 1;
            }
            //lesser supplements
            if (supplementId == 166100000 || supplementId == 166100003 || supplementId == 166100006)
                addsuccessRate = EnchantsConfig.LSSUP;
            //supplements
            if (supplementId == 166100001 || supplementId == 166100004 || supplementId == 166100007)
                addsuccessRate = EnchantsConfig.RGSUP;
            //greater supplements
            if (supplementId == 166100002 || supplementId == 166100005 || supplementId == 166100008)
                addsuccessRate = EnchantsConfig.GRSUP;

            //basic formula by manastone level
            if (manastoneLevel > 30)
                supplementUseCount = supplementUseCount + 1;

            if (manastoneLevel > 40)
                supplementUseCount = supplementUseCount + 1;

            if (manastoneLevel > 50)
                supplementUseCount = supplementUseCount + 1;

            //manastone attacks and crit strike use more supplements
            if (manastoneId == 167000230 || manastoneId == 167000235)
                supplementUseCount = 5;

            if (manastoneId == 167000294 || manastoneId == 167000267 || manastoneId == 167000299)
                supplementUseCount = 5;

            if (manastoneId == 167000331)
                supplementUseCount = 10;

            if (manastoneId == 167000358 || manastoneId == 167000363)
                supplementUseCount = 15;

            if (manastoneId == 167000550)
                supplementUseCount = 20;

            if (manastoneId == 167000454 || manastoneId == 167000427 || manastoneId == 167000459)
                supplementUseCount = 25;

            if (manastoneId == 167000491)
                supplementUseCount = 50;

            if (manastoneId == 167000518 || manastoneId == 167000522)
                supplementUseCount = 75;

            //supplementUseCount * manastoneCount
            if (stoneCount > 0)
                supplementUseCount = supplementUseCount * manastoneCount;

            player.getInventory().removeFromBagByItemId(supplementItem.getItemId(), supplementUseCount);

            //Add successRate
            successRate = successRate + addsuccessRate;
        }

        if (Rnd.get(0, 100) < successRate)
            result = true;
        if (player.getInventory().removeFromBagByObjectId(parentItem.getObjectId(), 1) && result) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_GIVE_ITEM_OPTION_SUCCEED(new DescriptionId(
                    Integer.parseInt(targetItem.getName()))));

            if (targetWeapon == 1) {
                ManaStone manaStone = ItemService.addManaStone(targetItem, parentItem.getItemTemplate().getTemplateId());
                if (targetItem.isEquipped()) {
                    ItemEquipmentListener.addStoneStats(manaStone, player.getGameStats());
                    PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
                }
            } else {
                FusionStone manaStone = ItemService.addFusionStone(targetItem, parentItem.getItemTemplate().getTemplateId());
                if (targetItem.isEquipped()) {
                    ItemEquipmentListener.addFusionStats(manaStone, player.getGameStats());
                    PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
                }
            }
        } else {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_GIVE_ITEM_OPTION_FAILED(new DescriptionId(
                    Integer.parseInt(targetItem.getName()))));
            if (targetWeapon == 1) {
                Set<ManaStone> manaStones = targetItem.getItemStones();
                if (targetItem.isEquipped()) {
                    ItemEquipmentListener.removeStoneStats(manaStones, player.getGameStats());
                    PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
                }
                ItemService.removeAllManastone(player, targetItem);
            } else {
                Set<FusionStone> manaStones = targetItem.getFusionStones();

                if (targetItem.isEquipped()) {
                    ItemEquipmentListener.removeFusionStats(manaStones, player.getGameStats());
                    PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
                }

                ItemService.removeAllFusionStone(player, targetItem);
            }
        }

        PacketSendUtility.sendPacket(player, new SM_UPDATE_ITEM(targetItem));

        if (targetItem.isEquipped())
            player.getEquipment().setPersistentState(PersistentState.UPDATE_REQUIRED);
        else
            player.getInventory().setPersistentState(PersistentState.UPDATE_REQUIRED);

        return result;
    }

    /**
     * @param player
     * @param item
     */
    public static void onItemEquip(Player player, Item item) {
        try {
            int enchantLevel = item.getEnchantLevel();

            if (enchantLevel == 0)
                return;

            boolean isWeapon = item.getItemTemplate().isWeapon();
            boolean isArmor = item.getItemTemplate().isArmor();
            if (isWeapon) {
                TreeSet<StatModifier> modifiers = getWeaponModifiers(player, item);

                if (modifiers == null)
                    return;

                EnchantStatEffectId statId = EnchantStatEffectId.getInstance(item.getObjectId(), item
                        .getEquipmentSlot());

                player.getGameStats().addModifiers(statId, modifiers);
                return;
            }

            if (isArmor) {
                TreeSet<StatModifier> modifiers = getArmorModifiers(player, item);

                if (modifiers == null)
                    return;

                EnchantStatEffectId statId = EnchantStatEffectId.getInstance(item.getObjectId(), item
                        .getEquipmentSlot());
                player.getGameStats().addModifiers(statId, modifiers);
            }
        }
        catch (Exception ex) {
            log.error(ex.getCause() != null ? ex.getCause().getMessage() : "Error on item equip.");
        }
    }

    /**
     * @param player
     * @param item
     */
    public static void onItemUnequip(Player player, Item item) {
        try {
            int enchantLevel = item.getEnchantLevel();

            if (enchantLevel == 0)
                return;

            EnchantStatEffectId statId = EnchantStatEffectId.getInstance(item.getObjectId(), item.getEquipmentSlot());

            if (player.getGameStats().effectAlreadyAdded(statId))
                player.getGameStats().endEffect(statId);

        }
        catch (Exception ex) {
            log.error(ex.getCause() != null ? ex.getCause().getMessage() : null);
        }
    }

    /**
     * @param item
     * @return
     */
    private static TreeSet<StatModifier> getArmorModifiers(Player player, Item item) {
        TreeSet<StatModifier> modifiers = null;

        ArmorType armorType = item.getItemTemplate().getArmorType();
        if (armorType == null)
            return null;

        switch (armorType) {
            case ROBE:
                switch (item.getEquipmentSlot()) {
                    case 1 << 3: // torso
                        modifiers = EnchantWeapon.DEF3.getModifiers(player, item);
                        break;
                    case 1 << 5: // boots
                        modifiers = EnchantWeapon.DEF1.getModifiers(player, item);
                        break;
                    case 1 << 11: // pauldrons
                        modifiers = EnchantWeapon.DEF1.getModifiers(player, item);
                        break;
                    case 1 << 12: // pants
                        modifiers = EnchantWeapon.DEF2.getModifiers(player, item);
                        break;
                    case 1 << 4: // gloves
                        modifiers = EnchantWeapon.DEF1.getModifiers(player, item);
                        break;
                }
                break;
            case LEATHER:
                switch (item.getEquipmentSlot()) {
                    case 1 << 3: // torso
                        modifiers = EnchantWeapon.DEF6.getModifiers(player, item);
                        break;
                    case 1 << 5: // boots
                        modifiers = EnchantWeapon.DEF4.getModifiers(player, item);
                        break;
                    case 1 << 11: // pauldrons
                        modifiers = EnchantWeapon.DEF4.getModifiers(player, item);
                        break;
                    case 1 << 12: // pants
                        modifiers = EnchantWeapon.DEF5.getModifiers(player, item);
                        break;
                    case 1 << 4: // gloves
                        modifiers = EnchantWeapon.DEF4.getModifiers(player, item);
                        break;
                }
                break;
            case CHAIN:
                switch (item.getEquipmentSlot()) {
                    case 1 << 3: // torso
                        modifiers = EnchantWeapon.DEF9.getModifiers(player, item);
                        break;
                    case 1 << 5: // boots
                        modifiers = EnchantWeapon.DEF7.getModifiers(player, item);
                        break;
                    case 1 << 11: // pauldrons
                        modifiers = EnchantWeapon.DEF7.getModifiers(player, item);
                        break;
                    case 1 << 12: // pants
                        modifiers = EnchantWeapon.DEF8.getModifiers(player, item);
                        break;
                    case 1 << 4: // gloves
                        modifiers = EnchantWeapon.DEF7.getModifiers(player, item);
                        break;
                }
                break;
            case PLATE:
                switch (item.getEquipmentSlot()) {
                    case 1 << 3: // torso
                        modifiers = EnchantWeapon.DEF12.getModifiers(player, item);
                        break;
                    case 1 << 5: // boots
                        modifiers = EnchantWeapon.DEF10.getModifiers(player, item);
                        break;
                    case 1 << 11: // pauldrons
                        modifiers = EnchantWeapon.DEF10.getModifiers(player, item);
                        break;
                    case 1 << 12: // pants
                        modifiers = EnchantWeapon.DEF11.getModifiers(player, item);
                        break;
                    case 1 << 4: // gloves
                        modifiers = EnchantWeapon.DEF10.getModifiers(player, item);
                        break;
                }
                break;
            case SHIELD:
                modifiers = EnchantWeapon.SHIELD.getModifiers(player, item);
                break;
        }
        return modifiers;
    }

    /**
     * @param item
     * @return
     */
    private static TreeSet<StatModifier> getWeaponModifiers(Player player, Item item) {
        WeaponType weaponType = item.getItemTemplate().getWeaponType();

        if (weaponType == null)
            return null;

        TreeSet<StatModifier> modifiers = null;
        switch (weaponType) {
            case DAGGER_1H:
                modifiers = EnchantWeapon.DAGGER.getModifiers(player, item);
                break;
            case SWORD_1H:
                modifiers = EnchantWeapon.SWORD.getModifiers(player, item);
                break;
            case MACE_1H:
                modifiers = EnchantWeapon.MACE.getModifiers(player, item);
                break;
            case BOOK_2H:
                modifiers = EnchantWeapon.SPELLBOOK.getModifiers(player, item);
                break;
            case BOW:
                modifiers = EnchantWeapon.BOW.getModifiers(player, item);
                break;
            case ORB_2H:
                modifiers = EnchantWeapon.ORB.getModifiers(player, item);
                break;
            case STAFF_2H:
                modifiers = EnchantWeapon.STAFF.getModifiers(player, item);
                break;
            case SWORD_2H:
                modifiers = EnchantWeapon.GREATSWORD.getModifiers(player, item);
                break;
            case POLEARM_2H:
                modifiers = EnchantWeapon.POLEARM.getModifiers(player, item);
                break;
        }
        return modifiers;
    }

    /**
     * @author ATracer
     */
    private enum EnchantWeapon {
        DAGGER() {
            public TreeSet<StatModifier> getModifiers(Player player, Item item) {
                TreeSet<StatModifier> mod = new TreeSet<StatModifier>();
                StatEnum stat = (player.getEquipment().getMainHandWeapon() == item) ?
                    StatEnum.MAIN_HAND_POWER : StatEnum.OFF_HAND_POWER;
                mod.add(AddModifier.newInstance(stat, 2 * item.getEnchantLevel(), true));
                return mod;
            }
        },

        SWORD() {
            @Override
            public TreeSet<StatModifier> getModifiers(Player player, Item item) {
                TreeSet<StatModifier> mod = new TreeSet<StatModifier>();
                StatEnum stat = (player.getEquipment().getMainHandWeapon() == item) ?
                    StatEnum.MAIN_HAND_POWER : StatEnum.OFF_HAND_POWER;
                mod.add(AddModifier.newInstance(stat, 2 * item.getEnchantLevel(), true));
                return mod;
            }
        },

        MACE() {
            @Override
            public TreeSet<StatModifier> getModifiers(Player player, Item item) {
                TreeSet<StatModifier> mod = new TreeSet<StatModifier>();
                StatEnum stat = (player.getEquipment().getMainHandWeapon() == item) ?
                    StatEnum.MAIN_HAND_POWER : StatEnum.OFF_HAND_POWER;
                mod.add(AddModifier.newInstance(stat, 3 * item.getEnchantLevel(), true));
                mod.add(AddModifier.newInstance(StatEnum.BOOST_MAGICAL_SKILL, 20 * item.getEnchantLevel(), true));
                return mod;
            }
        },

        GREATSWORD() {
            @Override
            public TreeSet<StatModifier> getModifiers(Player player, Item item) {
                TreeSet<StatModifier> mod = new TreeSet<StatModifier>();
                mod.add(AddModifier.newInstance(StatEnum.PHYSICAL_ATTACK, 4 * item.getEnchantLevel(), true));
                return mod;
            }
        },

        POLEARM() {
            @Override
            public TreeSet<StatModifier> getModifiers(Player player, Item item) {
                TreeSet<StatModifier> mod = new TreeSet<StatModifier>();
                mod.add(AddModifier.newInstance(StatEnum.PHYSICAL_ATTACK, 4 * item.getEnchantLevel(), true));
                return mod;
            }
        },

        BOW() {
            @Override
            public TreeSet<StatModifier> getModifiers(Player player, Item item) {
                TreeSet<StatModifier> mod = new TreeSet<StatModifier>();
                mod.add(AddModifier.newInstance(StatEnum.PHYSICAL_ATTACK, 4 * item.getEnchantLevel(), true));
                return mod;
            }
        },

        STAFF() {
            @Override
            public TreeSet<StatModifier> getModifiers(Player player, Item item) {
                TreeSet<StatModifier> mod = new TreeSet<StatModifier>();
                int level = item.getEnchantLevel();
                mod.add(AddModifier.newInstance(StatEnum.PHYSICAL_ATTACK, 3 * level, true));
                mod.add(AddModifier.newInstance(StatEnum.BOOST_MAGICAL_SKILL, 20 * level, true));
                return mod;
            }
        },

        SPELLBOOK() {
            @Override
            public TreeSet<StatModifier> getModifiers(Player player, Item item) {
                TreeSet<StatModifier> mod = new TreeSet<StatModifier>();
                int level = item.getEnchantLevel();
                mod.add(AddModifier.newInstance(StatEnum.PHYSICAL_ATTACK, 3 * level, true));
                mod.add(AddModifier.newInstance(StatEnum.BOOST_MAGICAL_SKILL, 20 * level, true));
                return mod;
            }
        },

        ORB() {
            @Override
            public TreeSet<StatModifier> getModifiers(Player player, Item item) {
                TreeSet<StatModifier> mod = new TreeSet<StatModifier>();
                int level = item.getEnchantLevel();
                mod.add(AddModifier.newInstance(StatEnum.PHYSICAL_ATTACK, 3 * level, true));
                mod.add(AddModifier.newInstance(StatEnum.BOOST_MAGICAL_SKILL, 20 * level, true));
                return mod;
            }
        },

        SHIELD() {
            @Override
            public TreeSet<StatModifier> getModifiers(Player player, Item item) {
                TreeSet<StatModifier> mod = new TreeSet<StatModifier>();
                int level = item.getEnchantLevel();
                if(level <= 10)
                    mod.add(AddModifier.newInstance(StatEnum.DAMAGE_REDUCE, 2 * level, true));
                else {
                    mod.add(AddModifier.newInstance(StatEnum.BLOCK, 5 * level, true));
                    mod.add(AddModifier.newInstance(StatEnum.DAMAGE_REDUCE, 20, true));
                }
                return mod;
            }
        },

        DEF1() {
            @Override
            public TreeSet<StatModifier> getModifiers(Player player, Item item) {
                TreeSet<StatModifier> mod = new TreeSet<StatModifier>();
                int level = item.getEnchantLevel();
                mod.add(AddModifier.newInstance(StatEnum.PHYSICAL_DEFENSE, 1 * level, true));
                mod.add(AddModifier.newInstance(StatEnum.MAXHP, 10 * level, true));
                mod.add(AddModifier.newInstance(StatEnum.PHYSICAL_CRITICAL_RESIST, 2 * level, true));
                return mod;
            }
        },

        DEF2() {
            @Override
            public TreeSet<StatModifier> getModifiers(Player player, Item item) {
                TreeSet<StatModifier> mod = new TreeSet<StatModifier>();
                int level = item.getEnchantLevel();
                mod.add(AddModifier.newInstance(StatEnum.PHYSICAL_DEFENSE, 2 * level, true));
                mod.add(AddModifier.newInstance(StatEnum.MAXHP, 12 * level, true));
                mod.add(AddModifier.newInstance(StatEnum.PHYSICAL_CRITICAL_RESIST, 3 * level, true));
                return mod;
            }
        },

        DEF3() {
            @Override
            public TreeSet<StatModifier> getModifiers(Player player, Item item) {
                TreeSet<StatModifier> mod = new TreeSet<StatModifier>();
                int level = item.getEnchantLevel();
                mod.add(AddModifier.newInstance(StatEnum.PHYSICAL_DEFENSE, 3 * level, true));
                mod.add(AddModifier.newInstance(StatEnum.MAXHP, 14 * level, true));
                mod.add(AddModifier.newInstance(StatEnum.PHYSICAL_CRITICAL_RESIST, 4 * level, true));
                return mod;
            }
        },

        DEF4() {
            @Override
            public TreeSet<StatModifier> getModifiers(Player player, Item item) {
                TreeSet<StatModifier> mod = new TreeSet<StatModifier>();
                int level = item.getEnchantLevel();
                mod.add(AddModifier.newInstance(StatEnum.PHYSICAL_DEFENSE, 3 * level, true));
                mod.add(AddModifier.newInstance(StatEnum.MAXHP, 8 * level, true));
                mod.add(AddModifier.newInstance(StatEnum.PHYSICAL_CRITICAL_RESIST, 2 * level, true));
                return mod;
            }
        },

        DEF5() {
            @Override
            public TreeSet<StatModifier> getModifiers(Player player, Item item) {
                TreeSet<StatModifier> mod = new TreeSet<StatModifier>();
                int level = item.getEnchantLevel();
                mod.add(AddModifier.newInstance(StatEnum.PHYSICAL_DEFENSE, 5 * level, true));
                mod.add(AddModifier.newInstance(StatEnum.MAXHP, 10 * level, true));
                mod.add(AddModifier.newInstance(StatEnum.PHYSICAL_CRITICAL_RESIST, 3 * level, true));
                return mod;
            }
        },

        DEF6() {
            @Override
            public TreeSet<StatModifier> getModifiers(Player player, Item item) {
                TreeSet<StatModifier> mod = new TreeSet<StatModifier>();
                int level = item.getEnchantLevel();
                mod.add(AddModifier.newInstance(StatEnum.PHYSICAL_DEFENSE, 4 * level, true));
                mod.add(AddModifier.newInstance(StatEnum.MAXHP, 12 * level, true));
                mod.add(AddModifier.newInstance(StatEnum.PHYSICAL_CRITICAL_RESIST, 4 * level, true));
                return mod;
            }
        },

        DEF7() {
            @Override
            public TreeSet<StatModifier> getModifiers(Player player, Item item) {
                TreeSet<StatModifier> mod = new TreeSet<StatModifier>();
                int level = item.getEnchantLevel();
                mod.add(AddModifier.newInstance(StatEnum.PHYSICAL_DEFENSE, 3 * level, true));
                mod.add(AddModifier.newInstance(StatEnum.MAXHP, 6 * level, true));
                mod.add(AddModifier.newInstance(StatEnum.PHYSICAL_CRITICAL_RESIST, 2 * level, true));
                return mod;
            }
        },

        DEF8() {
            @Override
            public TreeSet<StatModifier> getModifiers(Player player, Item item) {
                TreeSet<StatModifier> mod = new TreeSet<StatModifier>();
                int level = item.getEnchantLevel();
                mod.add(AddModifier.newInstance(StatEnum.PHYSICAL_DEFENSE, 4 * level, true));
                mod.add(AddModifier.newInstance(StatEnum.MAXHP, 8 * level, true));
                mod.add(AddModifier.newInstance(StatEnum.PHYSICAL_CRITICAL_RESIST, 3 * level, true));
                return mod;
            }
        },

        DEF9() {
            @Override
            public TreeSet<StatModifier> getModifiers(Player player, Item item) {
                TreeSet<StatModifier> mod = new TreeSet<StatModifier>();
                int level = item.getEnchantLevel();
                mod.add(AddModifier.newInstance(StatEnum.PHYSICAL_DEFENSE, 5 * level, true));
                mod.add(AddModifier.newInstance(StatEnum.MAXHP, 10 * level, true));
                mod.add(AddModifier.newInstance(StatEnum.PHYSICAL_CRITICAL_RESIST, 4 * level, true));
                return mod;
            }
        },

        DEF10() {
            @Override
            public TreeSet<StatModifier> getModifiers(Player player, Item item) {
                TreeSet<StatModifier> mod = new TreeSet<StatModifier>();
                int level = item.getEnchantLevel();
                mod.add(AddModifier.newInstance(StatEnum.PHYSICAL_DEFENSE, 4 * level, true));
                mod.add(AddModifier.newInstance(StatEnum.MAXHP, 4 * level, true));
                mod.add(AddModifier.newInstance(StatEnum.PHYSICAL_CRITICAL_RESIST, 2 * level, true));
                return mod;
            }
        },

        DEF11() {
            @Override
            public TreeSet<StatModifier> getModifiers(Player player, Item item) {
                TreeSet<StatModifier> mod = new TreeSet<StatModifier>();
                int level = item.getEnchantLevel();
                mod.add(AddModifier.newInstance(StatEnum.PHYSICAL_DEFENSE, 5 * level, true));
                mod.add(AddModifier.newInstance(StatEnum.MAXHP, 6 * level, true));
                mod.add(AddModifier.newInstance(StatEnum.PHYSICAL_CRITICAL_RESIST, 3 * level, true));
                return mod;
            }
        },

        DEF12() {
            @Override
            public TreeSet<StatModifier> getModifiers(Player player, Item item) {
                TreeSet<StatModifier> mod = new TreeSet<StatModifier>();
                int level = item.getEnchantLevel();
                mod.add(AddModifier.newInstance(StatEnum.PHYSICAL_DEFENSE, 6 * level, true));
                mod.add(AddModifier.newInstance(StatEnum.MAXHP, 8 * level, true));
                mod.add(AddModifier.newInstance(StatEnum.PHYSICAL_CRITICAL_RESIST, 4 * level, true));
                return mod;
            }
        };

        public abstract TreeSet<StatModifier> getModifiers(Player player, Item item);

    }
}