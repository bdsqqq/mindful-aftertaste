package com.bdsqqq.mindfulaftertaste.component;

import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import com.mojang.serialization.Codec;

import com.bdsqqq.mindfulaftertaste.Mindfulaftertaste;

public class MindfulAftertasteComponents {
    public static final ComponentType<Integer> MEAL_USES = Registry.register(
        Registries.DATA_COMPONENT_TYPE,
        Identifier.of(Mindfulaftertaste.MOD_ID, "meal_uses"),
        ComponentType.<Integer>builder()
            .codec(Codec.INT)
            .packetCodec(PacketCodecs.VAR_INT)
            .build()
    );

    public static void initialize() {
        // This method is intentionally left empty as registration is handled by the static initializer
    }
}
