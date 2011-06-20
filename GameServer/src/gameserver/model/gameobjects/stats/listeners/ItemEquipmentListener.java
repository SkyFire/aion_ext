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
package gameserver.model.gameobjects.stats.listeners;

import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.CreatureGameStats;
import gameserver.model.gameobjects.stats.PlayerGameStats;
import gameserver.model.gameobjects.stats.StatEnum;
import gameserver.model.gameobjects.stats.id.FusionStatEffectId;
import gameserver.model.gameobjects.stats.id.ItemSetStatEffectId;
import gameserver.model.gameobjects.stats.id.ItemStatEffectId;
import gameserver.model.gameobjects.stats.id.StoneStatEffectId;
import gameserver.model.gameobjects.stats.modifiers.*;
import gameserver.model.items.FusionStone;
import gameserver.model.items.ManaStone;
import gameserver.model.templates.item.ArmorType;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.model.templates.item.WeaponType;
import gameserver.model.templates.itemset.ItemSetTemplate;
import gameserver.model.templates.itemset.PartBonus;
import gameserver.skillengine.model.Effect;
import gameserver.skillengine.model.SkillTemplate;
import gameserver.services.EnchantService;
import org.apache.log4j.Logger;

import java.util.Set;
import java.util.TreeSet;

/**
 * @author xavier
 */
public class ItemEquipmentListener {
    private static final Logger log = Logger.getLogger(ItemEquipmentListener.class);

    /**
     * @param itemTemplate
     * @param slot
     * @param cgs
     */
    private static void onItemEquipment(ItemTemplate itemTemplate, int slot, CreatureGameStats<?> cgs) {
        TreeSet<StatModifier> modifiers = itemTemplate.getModifiers();
        if (modifiers == null) {
            if (cgs instanceof PlayerGameStats) {
                log.debug("No effect was found for item " + itemTemplate.getTemplateId());
            }
        }

        cgs.addModifiers(ItemStatEffectId.getInstance(itemTemplate.getTemplateId(), slot), modifiers);
    }

    /**
     * @param fusionTemplate
     * @param itemTemplate
     * @param slot
     * @param cgs
     */
    private static void onItemEquipment(ItemTemplate fusionTemplate, ItemTemplate itemTemplate, int slot, CreatureGameStats<?> cgs) {
        TreeSet<StatModifier> fusionModifiers = fusionTemplate.getModifiers();
        TreeSet<StatModifier> itemModifiers = itemTemplate.getModifiers();
        TreeSet<StatModifier> filteredModifiers = new TreeSet<StatModifier>();
        int attackSpeed = 0;
        int castSpeed = 0;
        int pvpDamage = 0;

        filteredModifiers.addAll(itemModifiers);

        for (StatModifier modifier : itemModifiers) {
            if (modifier.isBonus()) {
                if (modifier.getStat() == StatEnum.ATTACK_SPEED) {
                    SimpleModifier mod = (SimpleModifier) modifier;
                    attackSpeed = mod.getValue();
                } else if (modifier.getStat() == StatEnum.BOOST_CASTING_TIME) {
                    SimpleModifier mod = (SimpleModifier) modifier;
                    castSpeed = mod.getValue();
                } else if (modifier.getStat() == StatEnum.PVP_ATTACK_RATIO) {
                    SimpleModifier mod = (SimpleModifier) modifier;
                    pvpDamage = mod.getValue();
                }
            }
        }

        for (StatModifier modifier : fusionModifiers) {
            if (!modifier.isBonus()) {
                if (modifier.getStat() == StatEnum.POWER) {
                    MeanModifier mod = (MeanModifier) modifier;
                    int temp = Math.round((mod.getMin() + mod.getMax()) / 20);
                    filteredModifiers.add(AddModifier.newInstance(StatEnum.PHYSICAL_ATTACK, temp, true));
                } else if (modifier.getStat() == StatEnum.BOOST_MAGICAL_SKILL) {
                    SimpleModifier mod = (SimpleModifier) modifier;
                    int temp = Math.round(mod.getValue() / 10);
                    filteredModifiers.add(AddModifier.newInstance(StatEnum.BOOST_MAGICAL_SKILL, temp, true));
                }
            } else {
                if (modifier.getStat() == StatEnum.ATTACK_SPEED || modifier.getStat() == StatEnum.BOOST_CASTING_TIME
                        || modifier.getStat() == StatEnum.PVP_ATTACK_RATIO) {
                    if (modifier.getStat() == StatEnum.ATTACK_SPEED) {
                        SimpleModifier mod = (SimpleModifier) modifier;
                        attackSpeed = mod.getValue() - attackSpeed;
                        if (attackSpeed < 0)
                            filteredModifiers.add(RateModifier.newInstance(StatEnum.ATTACK_SPEED, attackSpeed, true));
                    } else if (modifier.getStat() == StatEnum.BOOST_CASTING_TIME) {
                        SimpleModifier mod = (SimpleModifier) modifier;
                        castSpeed = mod.getValue() - castSpeed;
                        if (castSpeed > 0)
                            filteredModifiers.add(RateModifier.newInstance(StatEnum.BOOST_CASTING_TIME, castSpeed, true));
                    } else if (modifier.getStat() == StatEnum.PVP_ATTACK_RATIO) {
                        SimpleModifier mod = (SimpleModifier) modifier;
                        pvpDamage = mod.getValue() - pvpDamage;
                        if (pvpDamage > 0)
                            filteredModifiers.add(AddModifier.newInstance(StatEnum.PVP_ATTACK_RATIO, pvpDamage, true));
                    }
                } else {
                    SimpleModifier mod = (SimpleModifier) modifier;
                    filteredModifiers.add(AddModifier.newInstance(modifier.getStat(), mod.getValue(), true));
                }
            }
        }
        cgs.addModifiers(ItemStatEffectId.getInstance(itemTemplate.getTemplateId(), slot), filteredModifiers);
    }

    /**
     * @param item
     * @param cgs
     */
    public static void onItemEquipment(Item item, Player owner) {
        ItemTemplate itemTemplate = item.getItemTemplate();

        if (item.hasFusionedItem()) {
            ItemTemplate fusionTemplate = DataManager.ITEM_DATA.getItemTemplate(item.getFusionedItem());
            onItemEquipment(fusionTemplate, itemTemplate, item.getEquipmentSlot(), owner.getGameStats());
        } else
            onItemEquipment(itemTemplate, item.getEquipmentSlot(), owner.getGameStats());

        // Check if belongs to ItemSet
        if (itemTemplate.isItemSet())
            onItemSetPartEquipment(itemTemplate.getItemSet(), owner);

        if (item.hasManaStones())
            addStonesStats(item.getItemStones(), owner.getGameStats());

        if (item.hasFusionStones())
            addFusionStats(item.getFusionStones(), owner.getGameStats());

        addGodstoneEffect(owner, item);

        recalculateWeaponMastery(owner);

        if (item.getItemTemplate().isArmor())
            recalculateArmorMastery(owner);

        EnchantService.onItemEquip(owner, item);
        owner.getController().updatePassiveStats();
    }

    /**
     * @param itemSetTemplate
     * @param player
     */
    private static void onItemSetPartEquipment(ItemSetTemplate itemSetTemplate, Player player) {
        if (itemSetTemplate == null)
            return;

        // 1.- Check equipment for items already equip with this itemSetTemplate id
        int itemSetPartsEquipped = player.getEquipment().itemSetPartsEquipped(itemSetTemplate.getId());

        // 2.- Check Item Set Parts and add effects one by one if not done already
        for (PartBonus itempartbonus : itemSetTemplate.getPartbonus()) {
            ItemSetStatEffectId setEffectId = ItemSetStatEffectId.getInstance(itemSetTemplate.getId(), itempartbonus
                    .getCount());
            // If the partbonus was not applied before, do it now
            if (itempartbonus.getCount() <= itemSetPartsEquipped
                    && !player.getGameStats().effectAlreadyAdded(setEffectId)) {
                player.getGameStats().addModifiers(setEffectId, itempartbonus.getModifiers());
            }
        }

        // 3.- Finally check if all items are applied and set the full bonus if not already applied
        if (itemSetTemplate.getFullbonus() != null && itemSetPartsEquipped == itemSetTemplate.getFullbonus().getCount()) {
            ItemSetStatEffectId setEffectId = ItemSetStatEffectId.getInstance(itemSetTemplate.getId(),
                    itemSetPartsEquipped + 1);
            if (!player.getGameStats().effectAlreadyAdded(setEffectId)) {
                // Add the full bonus with index = total parts + 1 to avoid confusion with part bonus equal to number of
                // objects
                player.getGameStats().addModifiers(setEffectId, itemSetTemplate.getFullbonus().getModifiers());
            }
        }
    }

    /**
     * @param owner
     */
    public static void recalculateWeaponMastery(Player owner) {
        //don't calculate for not initialized equipment
        if (owner.getEquipment() == null)
            return;

        //remove old mastery skills
        int currentMainWeaponMasterySkill = owner.getEffectController().getWeaponMastery();
        int currentSubWeaponMasterySkill = owner.getEffectController().getSubWeaponMastery();
        if (currentMainWeaponMasterySkill != 0)
            owner.getEffectController().removePassiveEffect(currentMainWeaponMasterySkill);
        if (currentSubWeaponMasterySkill != 0)
            owner.getEffectController().removePassiveEffect(currentSubWeaponMasterySkill);

        //apply new mastery skills
        WeaponType mainWeaponType = owner.getEquipment().getMainHandWeaponType();
        WeaponType subWeaponType = owner.getEquipment().getOffHandWeaponType();
        Integer mainSkillId = owner.getSkillList().getWeaponMasterySkill(mainWeaponType);
        Integer subSkillId = owner.getSkillList().getWeaponMasterySkill(subWeaponType);

        if (mainSkillId != null) {
            SkillTemplate mainSkillTemplate = DataManager.SKILL_DATA.getSkillTemplate(mainSkillId);
            if (mainSkillTemplate != null) {
                Effect effect = new Effect(owner, owner, mainSkillTemplate, 1, mainSkillTemplate.getDuration());
                effect.addAllEffectToSucess();
                effect.applyEffect();
            }
        }

        if (subSkillId != null && mainSkillId != subSkillId) {
            SkillTemplate subSkillTemplate = DataManager.SKILL_DATA.getSkillTemplate(subSkillId);
            if (subSkillTemplate != null) {
                Effect effect = new Effect(owner, owner, subSkillTemplate, 1, subSkillTemplate.getDuration());
                effect.addAllEffectToSucess();
                effect.applyEffect();
            }
        }
    }

    /**
     * @param owner
     */
    public static void recalculateArmorMastery(Player owner) {
        //don't calculate for not initialized equipment
        if (owner.getEquipment() == null)
            return;

        for (ArmorType armorType : ArmorType.values()) {
            boolean armorEquiped = owner.getEquipment().isArmorEquipped(armorType);
            Integer skillId = owner.getSkillList().getArmorMasterySkill(armorType);
            if (skillId == null)
                continue;
            boolean masterySet = owner.getEffectController().isArmorMasterySet(skillId);
            //remove effect if no armor is equiped

            if (masterySet && !armorEquiped) {
                owner.getEffectController().removePassiveEffect(skillId);
            }
            //add effect if armor is equiped
            if (!masterySet && armorEquiped) {
                owner.getController().useSkill(skillId);
            }
        }
    }

    /**
     * All modifiers of stones will be applied to character
     *
     * @param itemStones
     * @param cgs
     */
    private static void addStonesStats(Set<ManaStone> itemStones, CreatureGameStats<?> cgs) {
        if (itemStones == null || itemStones.size() == 0)
            return;

        for (ManaStone stone : itemStones) {
            addStoneStats(stone, cgs);
        }
    }

    /**
     * All modifiers of stones will be applied to character
     *
     * @param itemStones
     * @param cgs
     */
    private static void addFusionStats(Set<FusionStone> itemStones, CreatureGameStats<?> cgs) {
        if (itemStones == null || itemStones.size() == 0)
            return;

        for (FusionStone stone : itemStones) {
            addFusionStats(stone, cgs);
        }
    }

    /**
     * All modifiers of stones will be removed
     *
     * @param itemStones
     * @param cgs
     */
    public static void removeStoneStats(Set<ManaStone> itemStones, CreatureGameStats<?> cgs) {
        if (itemStones == null || itemStones.size() == 0)
            return;

        for (ManaStone stone : itemStones) {
            TreeSet<StatModifier> modifiers = stone.getModifiers();
            if (modifiers != null) {
                cgs.endEffect(StoneStatEffectId.getInstance(stone.getItemObjId(), stone.getSlot()));
            }
        }
    }

    /**
     * All modifiers of stones will be removed
     *
     * @param itemStones
     * @param cgs
     */
    public static void removeFusionStats(Set<FusionStone> itemStones, CreatureGameStats<?> cgs) {
        if (itemStones == null || itemStones.size() == 0)
            return;

        for (FusionStone stone : itemStones) {
            TreeSet<StatModifier> modifiers = stone.getModifiers();
            if (modifiers != null) {
                cgs.endEffect(FusionStatEffectId.getInstance(stone.getItemObjId(), stone.getSlot()));
            }
        }
    }

    /**
     * Used when socketing of equipped item
     *
     * @param stone
     * @param cgs
     */
    public static void addStoneStats(ManaStone stone, CreatureGameStats<?> cgs) {
        TreeSet<StatModifier> modifiers = stone.getModifiers();
        if (modifiers != null) {
            cgs.addModifiers(StoneStatEffectId.getInstance(stone.getItemObjId(), stone.getSlot()), modifiers);
        }
    }

    /**
     * Used when socketing of equipped item
     *
     * @param stone
     * @param cgs
     */
    public static void addFusionStats(FusionStone stone, CreatureGameStats<?> cgs) {
        TreeSet<StatModifier> modifiers = stone.getModifiers();
        if (modifiers != null) {
            cgs.addModifiers(FusionStatEffectId.getInstance(stone.getItemObjId(), stone.getSlot()), modifiers);
        }
    }

    /**
     * @param item
     * @param owner
     */
    public static void onItemUnequipment(Item item, Player owner) {
        // Check if belongs to an ItemSet
        if (item.getItemTemplate().isItemSet())
            onItemSetPartUnequipment(item.getItemTemplate().getItemSet(), owner);

        owner.getGameStats().endEffect(
                ItemStatEffectId.getInstance(item.getItemTemplate().getTemplateId(), item.getEquipmentSlot()));

        if (item.hasManaStones())
            removeStoneStats(item.getItemStones(), owner.getGameStats());

        if (item.hasFusionStones())
            removeFusionStats(item.getFusionStones(), owner.getGameStats());

        removeGodstoneEffect(owner, item);

        recalculateWeaponMastery(owner);

        if (item.getItemTemplate().isArmor())
            recalculateArmorMastery(owner);

        EnchantService.onItemUnequip(owner, item);

    }

    /**
     * @param itemSetTemplate
     * @param player
     */
    private static void onItemSetPartUnequipment(ItemSetTemplate itemSetTemplate, Player player) {
        if (itemSetTemplate == null)
            return;

        // 1.- Check number of item parts equipped before the removal (i.e. current + 1)
        int previousItemSetPartsEquipped = player.getEquipment().itemSetPartsEquipped(itemSetTemplate.getId()) + 1;

        // 2.- Check if removed one item from the full set and if so remove the full bonus
        if (itemSetTemplate.getFullbonus() != null
                && previousItemSetPartsEquipped == itemSetTemplate.getFullbonus().getCount()) {
            // Full bonus was added with index = total parts + 1 to avoid confusion with part bonus equal to total
            // number of item set parts
            player.getGameStats()
                    .endEffect(
                            ItemSetStatEffectId.getInstance(itemSetTemplate.getId(),
                                    itemSetTemplate.getFullbonus().getCount() + 1));
        }

        // 3.- Check Item Set Parts and remove appropriate effects
        for (PartBonus itempartbonus : itemSetTemplate.getPartbonus()) {
            // Remove modifier if not applicable anymore
            if (itempartbonus.getCount() == previousItemSetPartsEquipped) {
                player.getGameStats().endEffect(
                        ItemSetStatEffectId.getInstance(itemSetTemplate.getId(), itempartbonus.getCount()));
            }
        }
    }


    /**
     * @param item
     */
    private static void addGodstoneEffect(Player player, Item item) {
        if (item.getGodStone() != null) {
            item.getGodStone().onEquip(player, item);
        }
    }

    /**
     * @param item
     */
    private static void removeGodstoneEffect(Player player, Item item) {
		    if(item.getGodStone() != null) {
			      item.getGodStone().onUnEquip(player);
		    }
	  }
}
