package com.lota.lotaenchantment.config;

import net.minecraftforge.common.ForgeConfigSpec;

public final class ModConfig {
    public static final ForgeConfigSpec SERVER_SPEC;
    public static final ForgeConfigSpec.DoubleValue DAMAGE_ENCHANTMENT_BONUS_PER_LEVEL;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("enchantments");
        DAMAGE_ENCHANTMENT_BONUS_PER_LEVEL = builder
                .comment("Attack damage bonus added by Damage enchantment per level")
                .defineInRange("damageBonusPerLevel", 5.0D, 0.0D, 1024.0D);
        builder.pop();
        SERVER_SPEC = builder.build();
    }

    private ModConfig() {
    }
}
