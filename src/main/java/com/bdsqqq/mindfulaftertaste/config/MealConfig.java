package com.bdsqqq.mindfulaftertaste.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class MealConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File("config/mindfulaftertaste/meals.json");
    
    private Map<String, Integer> mealUses = new HashMap<>();
    
    public MealConfig() {
        // Default configuration
        mealUses.put("minecraft:cooked_beef", 3);
        mealUses.put("minecraft:cooked_porkchop", 3);
        mealUses.put("minecraft:cooked_chicken", 3);
        mealUses.put("minecraft:cooked_mutton", 3);
        mealUses.put("minecraft:cooked_rabbit", 3);
    }
    
    public void load() {
        try {
            if (CONFIG_FILE.exists()) {
                try (FileReader reader = new FileReader(CONFIG_FILE)) {
                    MealConfig loaded = GSON.fromJson(reader, MealConfig.class);
                    this.mealUses = loaded.mealUses;
                }
            } else {
                save();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void save() {
        try {
            CONFIG_FILE.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
                GSON.toJson(this, writer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public int getMealUses(Item item) {
        Identifier id = Registries.ITEM.getId(item);
        return mealUses.getOrDefault(id.toString(), 0);
    }
    
    public boolean isMeal(Item item) {
        return getMealUses(item) > 0;
    }
} 