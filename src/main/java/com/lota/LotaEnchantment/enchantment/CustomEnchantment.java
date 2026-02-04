package com.lota.lotaenchantment.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class CustomEnchantment extends Enchantment {
    private final int maxLevel;

    public CustomEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        this(pRarity, pCategory, 5, pApplicableSlots);
    }

    public CustomEnchantment(Rarity pRarity, EnchantmentCategory pCategory, int maxLevel, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
        this.maxLevel = maxLevel;
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
