package com.clayfactoria.sensors.builders;

import com.clayfactoria.sensors.SensorPathComplete;
import com.google.gson.JsonElement;
import com.hypixel.hytale.server.npc.asset.builder.Builder;
import com.hypixel.hytale.server.npc.asset.builder.BuilderDescriptorState;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.asset.builder.holder.BooleanHolder;
import com.hypixel.hytale.server.npc.corecomponents.builders.BuilderSensorBase;
import com.hypixel.hytale.server.npc.instructions.Sensor;
import org.jspecify.annotations.Nullable;

import javax.annotation.Nonnull;

public class BuilderSensorPathComplete extends BuilderSensorBase {
    protected final BooleanHolder isComplete = new BooleanHolder();

    public boolean getIsComplete(@Nonnull BuilderSupport builderSupport) {
        return this.isComplete.get(builderSupport.getExecutionContext());
    }

    @Override
    public @Nullable String getShortDescription() {
        return "Sensor to check if entity pathing is complete.";
    }

    @Override
    public @Nullable String getLongDescription() {
        return this.getShortDescription();
    }

    @Override
    public @Nullable Sensor build(BuilderSupport builderSupport) {
        return new SensorPathComplete(this, builderSupport);
    }

    @Override
    public @Nullable BuilderDescriptorState getBuilderDescriptorState() {
        return BuilderDescriptorState.Stable;
    }

    @Nonnull
    public Builder<Sensor> readConfig(@Nonnull JsonElement data) {
        this.getBoolean(
                data,
                "IsComplete",
                this.isComplete,
                true,
                BuilderDescriptorState.Stable,
                "Is entity done pathing or not",
                null
        );
        return this;
    }
}
