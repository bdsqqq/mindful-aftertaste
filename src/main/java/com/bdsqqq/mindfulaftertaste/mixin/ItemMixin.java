package com.bdsqqq.mindfulaftertaste.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registries;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.registry.tag.TagKey;
import com.bdsqqq.mindfulaftertaste.LastEatenFoodComponent;
import com.bdsqqq.mindfulaftertaste.Mindfulaftertaste;

@Mixin(Item.class)
public class ItemMixin {
    private static final Map<String, StatusEffect> FOOD_GROUP_EFFECTS = new HashMap<>();
    static {
        FOOD_GROUP_EFFECTS.put("fruits", Registries.STATUS_EFFECT.get(new Identifier("minecraft", "dolphins_grace")));
        FOOD_GROUP_EFFECTS.put("vegetables", Registries.STATUS_EFFECT.get(new Identifier("minecraft", "jump_boost")));
        FOOD_GROUP_EFFECTS.put("grains", Registries.STATUS_EFFECT.get(new Identifier("minecraft", "haste")));
        FOOD_GROUP_EFFECTS.put("proteins", Registries.STATUS_EFFECT.get(new Identifier("minecraft", "strength")));
        FOOD_GROUP_EFFECTS.put("sugars", Registries.STATUS_EFFECT.get(new Identifier("minecraft", "regeneration")));
    }

    @Inject(method = "finishUsing", at = @At("HEAD"))
    protected void finishUsing(ItemStack stack, World world, LivingEntity entity, CallbackInfoReturnable<ItemStack> cir) {
        if (!stack.isFood()) return;

        TagKey<Item>[] foodGroups = new TagKey[] {
            TagKey.of(Registries.ITEM.getKey(), new Identifier(Mindfulaftertaste.MOD_ID, "foods/fruits")),
            TagKey.of(Registries.ITEM.getKey(), new Identifier(Mindfulaftertaste.MOD_ID, "foods/vegetables")),
            TagKey.of(Registries.ITEM.getKey(), new Identifier(Mindfulaftertaste.MOD_ID, "foods/grains")),
            TagKey.of(Registries.ITEM.getKey(), new Identifier(Mindfulaftertaste.MOD_ID, "foods/proteins")),
            TagKey.of(Registries.ITEM.getKey(), new Identifier(Mindfulaftertaste.MOD_ID, "foods/sugars"))
        };
        String[] groupNames = {"fruits", "vegetables", "grains", "proteins", "sugars"};

        Item item = stack.getItem();
        Identifier foundGroup = null;
        String foundGroupName = null;
        for (int i = 0; i < foodGroups.length; i++) {
            if (item.getRegistryEntry().isIn(foodGroups[i])) {
                foundGroup = new Identifier(Mindfulaftertaste.MOD_ID, "foods/" + groupNames[i]);
                foundGroupName = groupNames[i];
                break;
            }
        }
        if (foundGroup != null) {
            LastEatenFoodComponent comp = Mindfulaftertaste.LAST_EATEN_FOOD_COMPONENT_KEY.get(entity);
            Identifier previousGroup = comp.getLastFoodGroup();
            comp.setLastFoodGroup(foundGroup);

            // Remove previous effect if present
            if (previousGroup != null) {
                String prevGroupName = previousGroup.getPath().replace("foods/", "");
                StatusEffect prevEffect = FOOD_GROUP_EFFECTS.get(prevGroupName);
                if (prevEffect != null) {
                    entity.removeStatusEffect(prevEffect);
                }
            }

            // Apply new effect
            StatusEffect effect = FOOD_GROUP_EFFECTS.get(foundGroupName);
            if (effect != null) {
                entity.addStatusEffect(new StatusEffectInstance(effect, 20 * 60, 0)); // 60 seconds, amplifier 0
            }

            Mindfulaftertaste.LOGGER.info("Entity {} ate food in group: {}", entity.getName().getString(), foundGroup);
        }
    }
}