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
package gameserver.model.templates.item;

import gameserver.dataholders.DataManager;
import gameserver.itemengine.actions.AbstractItemAction;
import gameserver.itemengine.actions.ItemActions;
import gameserver.itemengine.actions.QuestStartAction;
import gameserver.model.PlayerClass;
import gameserver.model.gameobjects.stats.modifiers.StatModifier;
import gameserver.model.items.ItemId;
import gameserver.model.items.ItemMask;
import gameserver.model.templates.VisibleObjectTemplate;
import gameserver.model.templates.itemset.ItemSetTemplate;
import gameserver.model.templates.stats.ModifiersTemplate;
import org.apache.commons.lang.StringUtils;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.TreeSet;

/**
 * @author Luno modified by ATracer
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "item_templates")
public class ItemTemplate extends VisibleObjectTemplate {
    @XmlAttribute(name = "id", required = true)
    @XmlID
    private String id;

    @XmlElement(name = "modifiers", required = false)
    protected ModifiersTemplate modifiers;

    @XmlElement(name = "actions", required = false)
    protected ItemActions actions;

    @XmlElement(name = "bonus", required = false)
    protected ItemBonus bonusInfo;

    @XmlAttribute(name = "mask")
    private int mask;

    @XmlAttribute(name = "slot")
    private int itemSlot;

    @XmlAttribute(name = "usedelayid")
    private int useDelayId;

    @XmlAttribute(name = "usedelay")
    private int useDelay;

    @XmlAttribute(name = "equipment_type")
    private EquipType equipmentType = EquipType.NONE;

    @XmlAttribute(name = "cash_item")
    private int cashItem;

    @XmlAttribute(name = "dmg_decal")
    private int dmgDecal;

    @XmlAttribute(name = "weapon_boost")
    private int weaponBoost;

    @XmlAttribute(name = "extra_currency_item")
    private int extraCurrencyItem;

    @XmlAttribute(name = "extra_currency_item_count")
    private int extraCurrencyItemCount;

    @XmlAttribute(name = "price")
    private int price;

    @XmlAttribute(name = "ap")
    private int abyssPoints;

    @XmlAttribute(name = "ai")
    private int abyssItem;

    @XmlAttribute(name = "aic")
    private int abyssItemCount;

    @XmlAttribute(name = "max_stack_count")
    private int maxStackCount = 1;

    @XmlAttribute(name = "level")
    private int level;

    @XmlAttribute(name = "quality")
    private ItemQuality itemQuality;

    @XmlAttribute(name = "item_type")
    private ItemType itemType;

    @XmlAttribute(name = "item_category")
    private ItemCategory itemCategory;

    @XmlAttribute(name = "weapon_type")
    private WeaponType weaponType;

    @XmlAttribute(name = "armor_type")
    private ArmorType armorType;

    @XmlAttribute(name = "attack_type")
    private String attackType;                // TODO enum

    @XmlAttribute(name = "attack_gap")
    private float attackGap;                    // TODO enum

    @XmlAttribute(name = "desc")
    private String description;                // TODO string or int

    @XmlAttribute(name = "gender")
    private String genderPermitted;            // enum

    @XmlAttribute(name = "option_slot_bonus")
    private int optionSlotBonus;

    @XmlAttribute(name = "bonus_apply")
    private String bonusApply;                // enum

    @XmlAttribute(name = "race")
    private ItemRace race = ItemRace.ALL;

    @XmlAttribute(name = "origRace")
    private ItemRace origRace = ItemRace.ALL;

    private int itemId;

    @XmlAttribute(name = "return_world")
    private int returnWorldId;

    @XmlAttribute(name = "return_alias")
    private String returnAlias;

    @XmlElement(name = "godstone")
    private GodstoneInfo godstoneInfo;

    @XmlElement(name = "stigma")
    private Stigma stigma;

    @XmlAttribute(name = "name")
    private String name;

    @XmlAttribute(name = "restrict")
    private String restrict;

    @XmlTransient
    private int[] restricts;

    @XmlAttribute(name = "quest", required = false)
    private int itemQuestId;

    @XmlAttribute(name = "expire_mins", required = false)
    private int expireMins;

    @XmlAttribute(name = "cash_minute", required = false)
    private int cashAvailableMinute;

    @XmlAttribute(name = "exchange_mins", required = false)
    private int temporaryExchangeMins;

    /**
     * @return the mask
     */
    public int getMask() {
        return mask;
    }

    public int getItemSlot() {
        return itemSlot;
    }

    /**
     * @param playerClass
     * @return
     */
    public boolean isClassSpecific(PlayerClass playerClass) {
        boolean related = restricts[playerClass.ordinal()] > 0;
        if (!related && !playerClass.isStartingClass()) {
            related = restricts[PlayerClass.getStartingClassFor(playerClass).ordinal()] > 0;
        }
        return related;
    }

    /**
     * @param playerClass
     * @param level
     * @return
     */
    public boolean isAllowedFor(PlayerClass playerClass, int level) {
        ItemCategory category = getItemCategory();
        if ((category == ItemCategory.CASH_POLYMORPH_CANDY ||
                category == ItemCategory.CASH_CARD_TITLE ||
                category == ItemCategory.XPBOOST) && restricts[playerClass.ordinal()] != 0) {
            return restricts[playerClass.ordinal()] >= level;
        }
        return restricts[playerClass.ordinal()] <= level && restricts[playerClass.ordinal()] != 0;
    }

    /**
     * @return the modifiers
     */
    public TreeSet<StatModifier> getModifiers() {
        if (modifiers != null) {
            return modifiers.getModifiers();
        } else {
            return null;
        }
    }

    /**
     * @return the actions
     */
    public ItemActions getActions() {
        return actions;
    }

    /**
     * @return the actions
     */
    public void setActions(ItemActions itemActions) {
        this.actions = itemActions;
    }

    /**
     * @return the equipmentType
     */
    public EquipType getEquipmentType() {
        return equipmentType;
    }

    /**
     * @return the extraCurrencyItem
     */
    public int getExtraCurrencyItem() {
    	return extraCurrencyItem;
    }

    /**
     * @return the extraCurrencyItemCount
     */
    public int getExtraCurrencyItemCount() {
    	return extraCurrencyItemCount;
    }

    /**
     * @return the price
     */
    public int getPrice() {
        return price;
    }

    /**
     * @return the abyssPoints
     */
    public int getAbyssPoints() {
        return abyssPoints;
    }

    /**
     * @return the abyssItem
     */
    public int getAbyssItem() {
        return abyssItem;
    }

    /**
     * @return the abyssItemCount
     */
    public int getAbyssItemCount() {
        return abyssItemCount;
    }

    /**
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    /**
     * @return the quality
     */
    public ItemQuality getItemQuality() {
        return itemQuality;
    }

    /**
     * @return the itemCategory
     */
    public ItemCategory getItemCategory() {
        return itemCategory;
    }

    /**
     * @return the itemType
     */
    public ItemType getItemType() {
        return itemType;
    }

    /**
     * @return the weaponType
     */
    public WeaponType getWeaponType() {
        return weaponType;
    }

    /**
     * @return the armorType
     */
    public ArmorType getArmorType() {
        return armorType;
    }

    /**
     * @return the description
     */
    @Override
    public int getNameId() {
        try {
            int val = Integer.parseInt(description);
            return val;
        }
        catch (NumberFormatException nfe) {
            return 0;
        }
    }

    /**
     * @return the cashItem
     */
    public int getCashItem() {
        return cashItem;
    }

    /**
     * @return the dmgDecal
     */
    public int getDmgDecal() {
        return dmgDecal;
    }

    /**
     * @return the maxStackCount
     */
    public int getMaxStackCount() {
        return maxStackCount;
    }

    /**
     * @return the attackType
     */
    public String getAttackType() {
        return attackType;
    }

    /**
     * @return the attackGap
     */
    public float getAttackGap() {
        return attackGap;
    }

    /**
     * @return the genderPermitted
     */
    public String getGenderPermitted() {
        return genderPermitted;
    }

    /**
     * @return the optionSlotBonus
     */
    public int getOptionSlotBonus() {
        return optionSlotBonus;
    }

    /**
     * @return the bonusApply
     */
    public String getBonusApply() {
        return bonusApply;
    }

    /**
     * @return the bonusInfo
     */
    public ItemBonus getBonusInfo() {
        return bonusInfo;
    }


    /**
     * @return the race
     */
    public ItemRace getRace() {
        return race;
    }

    /**
     * @return the race to which the item belongs
     */
    public ItemRace getOriginRace() {
        return origRace;
    }

    /**
     * @return the weaponBoost
     */
    public int getWeaponBoost() {
        return weaponBoost;
    }

    /**
     * @return true or false
     */
    public boolean isWeapon() {
        return equipmentType == EquipType.WEAPON;
    }

    /**
     * @return true or false
     */
    public boolean isArmor() {
        return equipmentType == EquipType.ARMOR;
    }

    public boolean isKinah() {
        return itemId == ItemId.KINAH.value();
    }

    public boolean isStigma() {
        return itemId > 140000000 && itemId < 140001000;
    }

    void afterUnmarshal(Unmarshaller u, Object parent) {
        setItemId(Integer.parseInt(id));
        String[] parts = restrict.split(",");
        restricts = new int[12];
        for (int i = 0; i < parts.length; i++) {
            restricts[i] = Integer.parseInt(parts[i]);
        }
        if (itemQuestId != 0 && actions != null) {
            for (AbstractItemAction action : actions.getItemActions()) {
                if (action instanceof QuestStartAction) {
                    QuestStartAction qa = (QuestStartAction) action;
                    if (qa.getQuestId() == 0)
                        qa.setQuestId(itemQuestId);
                    break;
                }
            }
        }
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    /*
      * @return id of the associated ItemSetTemplate or null if none
      */

    public ItemSetTemplate getItemSet() {
        return DataManager.ITEM_SET_DATA.getItemSetTemplateByItemId(itemId);
    }

    /*
      * Checks if the ItemTemplate belongs to an item set
      */

    public boolean isItemSet() {
        return getItemSet() != null;
    }

    /**
     * @return the godstoneInfo
     */
    public GodstoneInfo getGodstoneInfo() {
        return godstoneInfo;
    }

    @Override
    public String getName() {
        return name != null ? name : StringUtils.EMPTY;
    }

    @Override
    public int getTemplateId() {
        return itemId;
    }

    /**
     * @return the returnWorldId
     */
    public int getReturnWorldId() {
        return returnWorldId;
    }

    /**
     * @return the returnAlias
     */
    public String getReturnAlias() {
        return returnAlias;
    }

    /**
     * @return the delay for item.
     */
    public int getDelayTime() {
        return useDelay;
    }

    /**
     * @return item delay id
     */
    public int getDelayId() {
        return useDelayId;
    }

    /**
     * @return the stigma
     */
    public Stigma getStigma() {
        return stigma;
    }

    /**
     * Itemmask
     *
     * @return
     */
    public boolean isLimitOne() {
        return (getMask() & ItemMask.LIMIT_ONE) == ItemMask.LIMIT_ONE;
    }

    /**
     * @return
     */
    public boolean isTradeable() {
        return (getMask() & ItemMask.TRADEABLE) == ItemMask.TRADEABLE;
    }

    /**
     * @return
     */
    public boolean isSellable() {
        return (getMask() & ItemMask.SELLABLE) == ItemMask.SELLABLE;
    }

    /**
     * @return
     */
    public boolean isStorableinCharWarehouse() {
        return (getMask() & ItemMask.STORABLE_IN_WH) == ItemMask.STORABLE_IN_WH;
    }

    /**
     * @return
     */
    public boolean isStorableinAccWarehouse() {
        return (getMask() & ItemMask.STORABLE_IN_AWH) == ItemMask.STORABLE_IN_AWH;
    }

    /**
     * @return
     */
    public boolean isStorableinLegionWarehouse() {
        return (getMask() & ItemMask.STORABLE_IN_LWH) == ItemMask.STORABLE_IN_LWH;
    }

    /**
     * @return TODO: figure out what its exactly
     */
    public boolean isBreakable() {
        return (getMask() & ItemMask.BREAKABLE) == ItemMask.BREAKABLE;
    }

    /**
     * @return
     */
    public boolean isSoulBound() {
        return (getMask() & ItemMask.SOUL_BOUND) == ItemMask.SOUL_BOUND;
    }

    /**
     * @return
     */
    public boolean isRemoveWhenLogout() {
        return (getMask() & ItemMask.REMOVE_LOGOUT) == ItemMask.REMOVE_LOGOUT;
    }

    /**
     * @return
     */
    public boolean isNoEnchant() {
        return (getMask() & ItemMask.NO_ENCHANT) == ItemMask.NO_ENCHANT;
    }

    /**
     * @return TODO: figure out what its
     */
    public boolean isCanProcEnchant() {
        return (getMask() & ItemMask.CAN_PROC_ENCHANT) == ItemMask.CAN_PROC_ENCHANT;
    }

    /**
     * @return
     */
    public boolean isCanFuse() {
        return (getMask() & ItemMask.CAN_COMPOSITE_WEAPON) == ItemMask.CAN_COMPOSITE_WEAPON;
    }

    /**
     * @return
     */
    public boolean isChangeSkinPermitted() {
        return (getMask() & ItemMask.REMODELABLE) == ItemMask.REMODELABLE;
    }

    /**
     * @return
     */
    public boolean isCanSplit() {
        return (getMask() & ItemMask.CAN_SPLIT) == ItemMask.CAN_SPLIT;
    }

    /**
     * @return
     */
    public boolean isItemDropPermitted() {
        return (getMask() & ItemMask.DELETABLE) == ItemMask.DELETABLE;
    }

    /**
     * @return the dyePermitted
     */
    public boolean isItemDyePermitted() {
        return (getMask() & ItemMask.DYEABLE) == ItemMask.DYEABLE;
    }

    /**
     * @return the itemQuestId
     */
    public int getItemQuestId() {
        return itemQuestId;
    }

    /**
     * @return the expireMins
     */
    public int getExpireMinutes() {
        return expireMins;
    }

    /**
     * @return the cashAvailableMinute
     */
    public int getCashAvailableMinute() {
        return cashAvailableMinute;
    }

    /**
     * @return the temporaryExchangeMins
     */
    public int getTempExchangeMinutes()
    {
        return temporaryExchangeMins;
    }
}
