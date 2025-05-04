package com.bdsqqq.mindfulaftertaste.mixin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.bdsqqq.mindfulaftertaste.component.MindfulAftertasteComponents;
import com.bdsqqq.mindfulaftertaste.Mindfulaftertaste;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    private static final Logger LOGGER = LoggerFactory.getLogger(Mindfulaftertaste.MOD_ID + " ItemStackMixin");

    @Inject(method = "<init>(Lnet/minecraft/item/ItemConvertible;I)V", at = @At("RETURN"))
    private void addMyComponent(ItemConvertible itemConvertible, int count, CallbackInfo ci) {
        ItemStack stack = (ItemStack) (Object) this;
        Item stackItem = stack.getItem();
        boolean isFood = stackItem.getComponents().contains(DataComponentTypes.FOOD);

        LOGGER.info("ItemStackMixin for {} (from {}) fired.", stackItem, itemConvertible);

        if (isFood) {
            LOGGER.info("Item {} is food. Adding MEAL_USES component.", stackItem);
            stack.set(MindfulAftertasteComponents.MEAL_USES, 3);
            Integer uses = stack.get(MindfulAftertasteComponents.MEAL_USES);
            LOGGER.info("MEAL_USES component set to: {}. Check with /data get.", uses);
        } else {
            LOGGER.info("Item {} is not food. Skipping MEAL_USES component.", stackItem);
        }
    }
}
