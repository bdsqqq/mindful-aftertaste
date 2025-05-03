package com.bdsqqq.mindfulaftertaste.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.component.DataComponentTypes;

import com.bdsqqq.mindfulaftertaste.component.MindfulAftertasteComponents;
import com.bdsqqq.mindfulaftertaste.config.MealConfig;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    private static final MealConfig MEAL_CONFIG = new MealConfig();
    
    @Inject(method = "<init>(Lnet/minecraft/item/Item;I)V", at = @At("RETURN"))
    private void mindfulaftertaste$addMealUses(CallbackInfo ci) {
        ItemStack stack = (ItemStack)(Object)this;
        Item item = stack.getItem();
        
        if (stack.contains(DataComponentTypes.FOOD) && MEAL_CONFIG.isMeal(item)) {
            int uses = MEAL_CONFIG.getMealUses(item);
            stack.set(MindfulAftertasteComponents.MEAL_USES, uses);
        }
    }
    
    @Inject(method = "isItemBarVisible", at = @At("HEAD"), cancellable = true)
    private void isItemBarVisible(CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = (ItemStack)(Object)this;
        if (stack.contains(MindfulAftertasteComponents.MEAL_USES)) {
            cir.setReturnValue(true);
        }
    }
    
    @Inject(method = "getItemBarStep", at = @At("HEAD"), cancellable = true)
    private void getItemBarStep(CallbackInfoReturnable<Integer> cir) {
        ItemStack stack = (ItemStack)(Object)this;
        if (stack.contains(MindfulAftertasteComponents.MEAL_USES)) {
            int usesLeft = stack.get(MindfulAftertasteComponents.MEAL_USES);
            int maxUses = MEAL_CONFIG.getMealUses(stack.getItem());
            int barStep = Math.round((usesLeft / (float)maxUses) * 13);
            cir.setReturnValue(barStep);
        }
    }
    
    @Inject(method = "getItemBarColor", at = @At("HEAD"), cancellable = true)
    private void getItemBarColor(CallbackInfoReturnable<Integer> cir) {
        ItemStack stack = (ItemStack)(Object)this;
        if (stack.contains(MindfulAftertasteComponents.MEAL_USES)) {
            cir.setReturnValue(0xFF5555);
        }
    }
} 