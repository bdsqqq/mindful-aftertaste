package com.bdsqqq.mindfulaftertaste;

import net.fabricmc.api.ModInitializer;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mindfulaftertaste implements ModInitializer, EntityComponentInitializer {
	public static final String MOD_ID = "mindful-aftertaste";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final ComponentKey<LastEatenFoodComponent> LAST_EATEN_FOOD_COMPONENT_KEY =
		ComponentRegistry.getOrCreate(new Identifier(MOD_ID, "last_eaten_food"), LastEatenFoodComponent.class);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");
	}

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerFor(LivingEntity.class, LAST_EATEN_FOOD_COMPONENT_KEY, LastEatenFoodComponentImpl::new);
	}
}