package com.bdsqqq.mindfulaftertaste;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.util.Identifier;

public interface LastEatenFoodComponent extends Component {
    Identifier getLastFoodGroup();
    void setLastFoodGroup(Identifier group);
} 