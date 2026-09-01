package com.lota.lotaenchantment.registry;

import com.lota.lotaenchantment.LotaEnchantment;
import com.lota.lotaenchantment.enchantment.CustomEnchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS =
            DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, LotaEnchantment.MODID);

    public static final RegistryObject<Enchantment> POISON_PROTECTION = ENCHANTMENTS.register("poison_protection",
            () -> new CustomEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.ARMOR, 1, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET));

    public static final RegistryObject<Enchantment> MAGIC_PROTECTION = ENCHANTMENTS.register("magic_protection",
            () -> new CustomEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.ARMOR, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET));

    public static final RegistryObject<Enchantment> LIGHTNESS = ENCHANTMENTS.register("lightness",
            () -> new CustomEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.ARMOR, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET));

    public static final RegistryObject<Enchantment> ROGUE = ENCHANTMENTS.register("rogue",
            () -> new CustomEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.ARMOR, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET));

    public static final RegistryObject<Enchantment> LIFE_ARMOR = ENCHANTMENTS.register("life_armor",
            () -> new CustomEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.ARMOR, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET));

    public static final RegistryObject<Enchantment> MAGIC_DAMAGE = ENCHANTMENTS.register("magic_damage",
            () -> new CustomEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND));

    public static final RegistryObject<Enchantment> ARMOR_PENETRATION = ENCHANTMENTS.register("armor_penetration",
            () -> new CustomEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND));

    public static final RegistryObject<Enchantment> SWEEP = ENCHANTMENTS.register("sweep",
            () -> new CustomEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND));

    public static final RegistryObject<Enchantment> DAMAGE = ENCHANTMENTS.register("damage",
            () -> new CustomEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND));

    public static void register(IEventBus eventBus) {
        ENCHANTMENTS.register(eventBus);
    }
}
