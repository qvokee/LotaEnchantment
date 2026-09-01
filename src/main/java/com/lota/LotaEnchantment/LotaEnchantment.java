package com.lota.lotaenchantment;

import com.lota.lotaenchantment.config.ModConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(LotaEnchantment.MODID)
public class LotaEnchantment {
    public static final String MODID = "lotaenchantment";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public LotaEnchantment() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(Type.SERVER, ModConfig.SERVER_SPEC);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(com.lota.lotaenchantment.event.EnchantmentEventHandler.class);
        
        com.lota.lotaenchantment.registry.ModEnchantments.register(modEventBus);
    }
}
