package com.lota.lotaenchantment.mixin;

import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin {

    @Shadow @Final private DataSlot cost;

    @Shadow private String itemName;

    // Inject at the end of createResult to ensure that if we have a valid result from our event,
    // the cost is set to 0 (if intended) and the result is kept.
    @Inject(method = "createResult", at = @At("RETURN"))
    private void lotaenchantment$modifyResult(CallbackInfo ci) {
        net.minecraft.world.inventory.AbstractContainerMenu menu = (net.minecraft.world.inventory.AbstractContainerMenu) (Object) this;
        ItemStack output = menu.getSlot(2).getItem();
        
        if (output.isEmpty()) return;

        // 1. Enforce Max 2 Enchantments Cap
        if (net.minecraft.world.item.enchantment.EnchantmentHelper.getEnchantments(output).size() > 2) {
             menu.getSlot(2).set(ItemStack.EMPTY);
             this.cost.set(0);
             return;
        }

        // 2. Logic for XP Cost
        ItemStack left = menu.getSlot(0).getItem();
        ItemStack right = menu.getSlot(1).getItem();
        
        boolean isBook = right.getItem() instanceof net.minecraft.world.item.EnchantedBookItem;
        
        // Check if renaming
        boolean isRenaming = false;
        if (this.itemName != null && !this.itemName.isBlank()) {
             if (!this.itemName.equals(left.getHoverName().getString())) {
                 isRenaming = true;
             }
        } else if (left.hasCustomHoverName()) {
             isRenaming = true;
        }

        if (!isBook && !isRenaming) {
            this.cost.set(0);
        }
    }

    @Inject(method = "mayPickup", at = @At("HEAD"), cancellable = true)
    private void lotaenchantment$allowPickup(net.minecraft.world.entity.player.Player pPlayer, boolean pHasStack, CallbackInfoReturnable<Boolean> cir) {
        net.minecraft.world.inventory.AbstractContainerMenu menu = (net.minecraft.world.inventory.AbstractContainerMenu) (Object) this;
        ItemStack output = menu.getSlot(2).getItem();
        if (!output.isEmpty()) {
            cir.setReturnValue(true);
        }
    }
}
