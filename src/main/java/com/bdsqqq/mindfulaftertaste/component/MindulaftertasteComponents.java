package com.bdsqqq.mindfulaftertaste.component;

import com.bdsqqq.mindfulaftertaste.Mindfulaftertaste;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;


public class MindulaftertasteComponents {
    public static ComponentType<Integer> MEAL_USES;

    public static void initialize() {
        MEAL_USES = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(Mindfulaftertaste.MOD_ID, "meal_uses"),
            ComponentType.<Integer>builder().codec(Codec.INT).build()
        );
    }
}
