package com.lota.lotaenchantment;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(LotaEnchantment.MODID)
public class LotaEnchantment {
    public static final String MODID = "lotaenchantment";
    private static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public LotaEnchantment() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register event handlers
        MinecraftForge.EVENT_BUS.register(this);
    }
}
