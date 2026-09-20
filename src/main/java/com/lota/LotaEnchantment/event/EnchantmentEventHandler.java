package com.lota.lotaenchantment.event;

import com.lota.lotaenchantment.LotaEnchantment;
import com.lota.lotaenchantment.registry.ModEnchantments;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

public class EnchantmentEventHandler {

    private static final Map<EquipmentSlot, UUID> MAGIC_PROTECTION_UUIDS = createSlotMap(UUID.fromString("c0e86b20-918d-4e92-965a-076135891341"));
    private static final Map<EquipmentSlot, UUID> LIGHTNESS_UUIDS = createSlotMap(UUID.fromString("a1b2c3d4-e5f6-7890-1234-567890abcdef"));
    private static final Map<EquipmentSlot, UUID> ROGUE_FISHING_LUCK_UUIDS = createSlotMap(UUID.fromString("b2c3d4e5-f678-9012-3456-7890abcdef12"));
    private static final Map<EquipmentSlot, UUID> ROGUE_FISHING_LURE_UUIDS = createSlotMap(UUID.fromString("c3d4e5f6-7890-1234-5678-90abcdef1234"));
    private static final Map<EquipmentSlot, UUID> ROGUE_HARVEST_UUIDS = createSlotMap(UUID.fromString("d4e5f678-9012-3456-7890-abcdef123456"));
    
    private static final Map<EquipmentSlot, UUID> MAGIC_DAMAGE_UUIDS = createSlotMap(UUID.fromString("e5f67890-1234-5678-90ab-cdef12345678"));
    private static final Map<EquipmentSlot, UUID> ARMOR_PEN_UUIDS = createSlotMap(UUID.fromString("f6789012-3456-7890-abcd-ef1234567890"));
    private static final Map<EquipmentSlot, UUID> SWEEP_UUIDS = createSlotMap(UUID.fromString("01234567-890a-bcde-f012-34567890abcd"));

    private static class EffectState {
        int originalDuration;
        long startTime;
        EffectState(int duration, long time) { this.originalDuration = duration; this.startTime = time; }
    }

    private static final WeakHashMap<LivingEntity, Map<MobEffect, EffectState>> TRACKED_EFFECTS = new WeakHashMap<>();

    private static Map<EquipmentSlot, UUID> createSlotMap(UUID base) {
        Map<EquipmentSlot, UUID> map = new EnumMap<>(EquipmentSlot.class);
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            map.put(slot, new UUID(base.getMostSignificantBits(), base.getLeastSignificantBits() + slot.ordinal()));
        }
        return map;
    }

    @SubscribeEvent
    public static void onItemAttributeModifier(ItemAttributeModifierEvent event) {
        ItemStack stack = event.getItemStack();
        EquipmentSlot slot = event.getSlotType();
        
        if (slot.getType() == EquipmentSlot.Type.ARMOR
                && slot == LivingEntity.getEquipmentSlotForItem(stack)) {
            int magicProtectionLevel = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.MAGIC_PROTECTION.get(), stack);
            if (magicProtectionLevel > 0) {
                Attribute spellResist = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation("irons_spellbooks", "spell_resist"));
                if (spellResist != null) event.addModifier(spellResist, new AttributeModifier(MAGIC_PROTECTION_UUIDS.get(slot), "Magic Protection", magicProtectionLevel * 0.2, AttributeModifier.Operation.ADDITION));
            }

            int lightnessLevel = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.LIGHTNESS.get(), stack);
            if (lightnessLevel > 0) {
                Attribute weight = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation("epicfight", "weight"));
                if (weight != null) event.addModifier(weight, new AttributeModifier(LIGHTNESS_UUIDS.get(slot), "Lightness", lightnessLevel * -0.04, AttributeModifier.Operation.ADDITION));
            }

            int rogueLevel = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.ROGUE.get(), stack);
            if (rogueLevel > 0) {
                Attribute fishingLuck = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation("additional_attributes", "fishing_luck"));
                if (fishingLuck != null) event.addModifier(fishingLuck, new AttributeModifier(ROGUE_FISHING_LUCK_UUIDS.get(slot), "Rogue Luck", rogueLevel * 0.2, AttributeModifier.Operation.ADDITION));
                Attribute fishingLure = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation("additional_attributes", "fishing_lure"));
                if (fishingLure != null) event.addModifier(fishingLure, new AttributeModifier(ROGUE_FISHING_LURE_UUIDS.get(slot), "Rogue Lure", rogueLevel * 0.2, AttributeModifier.Operation.ADDITION));
                Attribute harvest = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation("additional_attributes", "harvest"));
                if (harvest != null) event.addModifier(harvest, new AttributeModifier(ROGUE_HARVEST_UUIDS.get(slot), "Rogue Harvest", rogueLevel * 0.2, AttributeModifier.Operation.ADDITION));
            }
        }

        if (slot == EquipmentSlot.MAINHAND) {
            int magicDamageLevel = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.MAGIC_DAMAGE.get(), stack);
            if (magicDamageLevel > 0) {
                Attribute magicDmg = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation("irons_spellbooks", "spell_power"));
                if (magicDmg == null) magicDmg = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation("irons_spellbooks", "magic_damage"));
                if (magicDmg != null) event.addModifier(magicDmg, new AttributeModifier(MAGIC_DAMAGE_UUIDS.get(slot), "Magic Damage Enchant", magicDamageLevel * 0.2, AttributeModifier.Operation.ADDITION));
            }

            int armorPenLevel = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.ARMOR_PENETRATION.get(), stack);
            if (armorPenLevel > 0) {
                Attribute armorNegation = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation("epicfight", "armor_negation"));
                if (armorNegation != null) event.addModifier(armorNegation, new AttributeModifier(ARMOR_PEN_UUIDS.get(slot), "Armor Penetration Bonus", armorPenLevel * 1.0, AttributeModifier.Operation.ADDITION));
            }

            int sweepLevel = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.SWEEP.get(), stack);
            if (sweepLevel > 0) {
                Attribute maxStrikes = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation("epicfight", "max_strikes"));
                if (maxStrikes != null) event.addModifier(maxStrikes, new AttributeModifier(SWEEP_UUIDS.get(slot), "Sweep Strikes Bonus", sweepLevel * 1.0, AttributeModifier.Operation.ADDITION));
            }
        }
    }

    @SubscribeEvent
    public static void onMobEffectAdded(MobEffectEvent.Added event) {
        LivingEntity entity = event.getEntity();
        if (entity == null || entity.level().isClientSide) return;
        
        MobEffectInstance instance = event.getEffectInstance();
        MobEffect effect = instance.getEffect();
        ResourceLocation id = ForgeRegistries.MOB_EFFECTS.getKey(effect);
        
        if (id != null && (id.getPath().contains("poison") || id.getPath().contains("toxin"))) {
            Map<MobEffect, EffectState> entityMap = TRACKED_EFFECTS.computeIfAbsent(entity, k -> new HashMap<>());
            long now = entity.level().getGameTime();
            EffectState state = entityMap.get(effect);
            int incoming = instance.getDuration();
            
            if (state == null) {
                entityMap.put(effect, new EffectState(incoming, now));
            } else {
                int remainingOriginal = (int) Math.max(0, state.originalDuration - (now - state.startTime));
                if (incoming > remainingOriginal + 5) {
                    state.originalDuration = incoming;
                    state.startTime = now;
                }
            }
            applyPoisonProtection(entity, instance);
        }
    }

    @SubscribeEvent
    public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide) return;
        for (MobEffectInstance instance : entity.getActiveEffects()) {
            ResourceLocation id = ForgeRegistries.MOB_EFFECTS.getKey(instance.getEffect());
            if (id != null && (id.getPath().contains("poison") || id.getPath().contains("toxin"))) applyPoisonProtection(entity, instance);
        }
    }

    private static void applyPoisonProtection(LivingEntity entity, MobEffectInstance instance) {
        Map<MobEffect, EffectState> entityMap = TRACKED_EFFECTS.get(entity);
        if (entityMap == null) return;
        EffectState state = entityMap.get(instance.getEffect());
        if (state == null) return;

        long now = entity.level().getGameTime();
        int remainingOriginal = (int) Math.max(0, state.originalDuration - (now - state.startTime));
        if (remainingOriginal <= 1) return;

        int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.POISON_PROTECTION.get(), entity);
        int targetDuration = (level > 0) ? (int)(remainingOriginal * 0.4) : remainingOriginal;

        if (Math.abs(instance.getDuration() - targetDuration) > 1) {
            try {
                java.lang.reflect.Field field;
                try { field = MobEffectInstance.class.getDeclaredField("f_19503_"); } 
                catch (NoSuchFieldException e) { field = MobEffectInstance.class.getDeclaredField("duration"); }
                field.setAccessible(true);
                field.setInt(instance, targetDuration);
                if (entity instanceof ServerPlayer player) player.connection.send(new ClientboundUpdateMobEffectPacket(entity.getId(), instance));
            } catch (Exception e) { LotaEnchantment.LOGGER.error("[SERVER] Poison Protection Error: {}", e.getMessage()); }
        }
    }

    @SubscribeEvent
    public static void onLivingHeal(LivingHealEvent event) {
        if (event.getEntity() == null || event.getEntity().level().isClientSide) return;
        int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.LIFE_ARMOR.get(), event.getEntity());
        if (level > 0) event.setAmount(event.getAmount() * (1.0f + (level * 0.04f)));
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {}
}
