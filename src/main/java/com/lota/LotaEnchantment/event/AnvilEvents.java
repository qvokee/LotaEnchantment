package com.lota.lotaenchantment.event;

import com.lota.lotaenchantment.LotaEnchantment;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

@Mod.EventBusSubscriber(modid = LotaEnchantment.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AnvilEvents {

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        //Logic moved to AnvilMenuMixin to better handle vanilla fallbacks and constraints
    }
}
