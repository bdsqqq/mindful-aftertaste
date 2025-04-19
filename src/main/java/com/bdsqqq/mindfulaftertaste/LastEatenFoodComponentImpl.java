package com.bdsqqq.mindfulaftertaste;

import net.minecraft.util.Identifier;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.ComponentV3;

public class LastEatenFoodComponentImpl implements LastEatenFoodComponent {
    private Identifier lastFoodGroup;

    public LastEatenFoodComponentImpl() {
        this.lastFoodGroup = null;
    }

    @Override
    public Identifier getLastFoodGroup() {
        return lastFoodGroup;
    }

    @Override
    public void setLastFoodGroup(Identifier group) {
        this.lastFoodGroup = group;
    }
} 