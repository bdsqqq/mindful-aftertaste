package com.bdsqqq.mindfulaftertaste.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;

import com.bdsqqq.mindfulaftertaste.component.MindfulAftertasteComponents;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Inject(method = "finishUsing", at = @At("HEAD"), cancellable = true)
    private void mindfulaftertaste$handleMealUses(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        // Check if the item is a meal with uses left
        if (stack.contains(MindfulAftertasteComponents.MEAL_USES)) {
            int usesLeft = stack.get(MindfulAftertasteComponents.MEAL_USES);

            // Get the food component from the item
            FoodComponent foodComponent = stack.get(DataComponentTypes.FOOD);
            
            if (foodComponent != null) {
                // Apply hunger/saturation effects
                if (user instanceof PlayerEntity player) {
                    player.getHungerManager().eat(foodComponent);
                }
                // Note: Status effects are automatically applied by HungerManager.eat
            }

            if (usesLeft > 1) {
                // Decrement uses
                stack.set(MindfulAftertasteComponents.MEAL_USES, usesLeft - 1);
                // Prevent the item stack from being consumed by returning the modified stack
                cir.setReturnValue(stack);
            } else if (usesLeft == 1) {
                // If it's the last use, set to 0 and let vanilla handle consumption
                stack.set(MindfulAftertasteComponents.MEAL_USES, 0);
                // Don't cancel, let vanilla handle the item consumption
            }
            // If usesLeft is 0 or negative, let vanilla handle it
        }
        // If not a meal, let the original method run
    }
}