package com.bdsqqq.mindfulaftertaste.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import com.bdsqqq.mindfulaftertaste.component.MindulaftertasteComponents;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    
    @Inject(method = "isItemBarVisible", at = @At("HEAD"), cancellable = true)
    private void isItemBarVisible(CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = (ItemStack)(Object)this;
        if (stack.getItem() == Items.COOKED_BEEF && stack.contains(MindulaftertasteComponents.MEAL_USES)) {
            cir.setReturnValue(true);
        }
    }
    
    @Inject(method = "getItemBarStep", at = @At("HEAD"), cancellable = true)
    private void getItemBarStep(CallbackInfoReturnable<Integer> cir) {
        ItemStack stack = (ItemStack)(Object)this;
        if (stack.getItem() == Items.COOKED_BEEF && stack.contains(MindulaftertasteComponents.MEAL_USES)) {
            int usesLeft = stack.get(MindulaftertasteComponents.MEAL_USES);
            int maxUses = 3; // Set this to your intended max uses
            int barStep = Math.round((usesLeft / (float)maxUses) * 13);
            cir.setReturnValue(barStep);
        }
    }
    
    @Inject(method = "getItemBarColor", at = @At("HEAD"), cancellable = true)
    private void getItemBarColor(CallbackInfoReturnable<Integer> cir) {
        ItemStack stack = (ItemStack)(Object)this;
        if (stack.getItem() == Items.COOKED_BEEF && stack.contains(MindulaftertasteComponents.MEAL_USES)) {
            // Use a reddish color for the durability bar
            cir.setReturnValue(0xFF5555);
        }
    }
} 