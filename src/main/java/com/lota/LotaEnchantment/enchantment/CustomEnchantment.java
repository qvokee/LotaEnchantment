package com.lota.lotaenchantment.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class CustomEnchantment extends Enchantment {
    private final int maxLevel;
    private final EnchantmentCategory category;

    public CustomEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        this(pRarity, pCategory, 5, pApplicableSlots);
    }

    public CustomEnchantment(Rarity pRarity, EnchantmentCategory pCategory, int maxLevel, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
        this.maxLevel = maxLevel;
        this.category = pCategory;
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        if (stack.isEmpty() || stack.getItem() instanceof EnchantedBookItem) {
            return !stack.isEmpty();
        }

        if (this.category == EnchantmentCategory.ARMOR) {
            return isArmor(stack);
        }

        if (this.category == EnchantmentCategory.WEAPON) {
            return isWeapon(stack);
        }

        return super.canEnchant(stack);
    }

    private static boolean isArmor(ItemStack stack) {
        EquipmentSlot slot = LivingEntity.getEquipmentSlotForItem(stack);
        return stack.getItem() instanceof ArmorItem || slot.getType() == EquipmentSlot.Type.ARMOR;
    }

    private static boolean isWeapon(ItemStack stack) {
        if (stack.getItem() instanceof SwordItem
                || stack.getItem() instanceof DiggerItem
                || stack.getItem() instanceof ProjectileWeaponItem
                || stack.getItem() instanceof TridentItem) {
            return true;
        }

        return stack.getAttributeModifiers(EquipmentSlot.MAINHAND).containsKey(Attributes.ATTACK_DAMAGE);
    }

    @Override
    public int getMinCost(int pLevel) {
        return 1 + 10 * (pLevel - 1);
    }

    @Override
    public int getMaxCost(int pLevel) {
        return super.getMinCost(pLevel) + 50;
    }

    @Override
    public int getMaxLevel() {
        return this.maxLevel;
    }
    
    @Override
    public boolean isTradeable() {
        return true;
    }

    @Override
    public boolean isDiscoverable() {
        return true;
    }
}
