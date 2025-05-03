package com.bdsqqq.mindfulaftertaste.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.item.Items;

import com.bdsqqq.mindfulaftertaste.component.MindulaftertasteComponents;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "finishUsing", at = @At("HEAD"), cancellable = true)
    protected void finishUsing(ItemStack stack, World world, LivingEntity entity, CallbackInfoReturnable<ItemStack> cir) {
        if (stack.getItem() == Items.COOKED_BEEF) {
            // Ensure unstackable
            stack.setCount(1);
            // Get or initialize uses
            int uses = stack.getOrDefault(MindulaftertasteComponents.MEAL_USES, 3);
            uses--;
            if (uses > 0) {
                stack.set(MindulaftertasteComponents.MEAL_USES, uses);
                cir.setReturnValue(stack);
            } else {
                cir.setReturnValue(ItemStack.EMPTY);
            }
        }
    }
    
    @Inject(method = "getMaxCount", at = @At("HEAD"), cancellable = true)
    protected void getMaxCount(CallbackInfoReturnable<Integer> cir) {
        Item item = (Item)(Object)this;
        if (item == Items.COOKED_BEEF) {
            cir.setReturnValue(1); // Make steak unstackable
        }
    }
}