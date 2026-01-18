package com.lota.lotaenchantment.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {

    @Shadow public abstract String getDescriptionId();
    @Shadow public abstract boolean isCurse();
    @Shadow public abstract int getMaxLevel();

    @Inject(method = "getFullname", at = @At("HEAD"), cancellable = true)
    public void getFullname(int level, CallbackInfoReturnable<Component> cir) {
        MutableComponent mutablecomponent = Component.translatable(this.getDescriptionId());
        
        if (this.isCurse()) {
            mutablecomponent.withStyle(ChatFormatting.RED);
        } else {
            mutablecomponent.withStyle(ChatFormatting.GRAY);
        }

        if (level != 1 || this.getMaxLevel() != 1) {
            mutablecomponent.append(CommonComponents.SPACE);

            if (level <= 10) {
                 mutablecomponent.append(Component.translatable("enchantment.level." + level));
            } else {
                 mutablecomponent.append(Component.literal(intToRoman(level)));
            }
        }

        cir.setReturnValue(mutablecomponent);
    }
    
    private String intToRoman(int num) {
        if (num < 1 || num > 3999) return String.valueOf(num);
        
        int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] romanLiterals = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        
        StringBuilder roman = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            while (num >= values[i]) {
                num -= values[i];
                roman.append(romanLiterals[i]);
            }
        }
        return roman.toString();
    }
}
