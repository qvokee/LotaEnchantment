package com.lota.lotaenchantment.event;

import com.lota.lotaenchantment.LotaEnchantment;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.api.distmarker.Dist;

@Mod.EventBusSubscriber(modid = LotaEnchantment.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class TooltipEvents {

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.isEnchantable() || stack.isEnchanted()) {
            int count = EnchantmentHelper.getEnchantments(stack).size();
            event.getToolTip().add(Component.translatable("tooltip.lotaenchantment.enchantments_count", count).withStyle(ChatFormatting.BLUE));
        }
    }
}
