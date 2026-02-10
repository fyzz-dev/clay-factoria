package com.clayfactoria.sensors.builders;

import com.clayfactoria.sensors.SensorBlockUnder;
import com.google.gson.JsonElement;
import com.hypixel.hytale.server.npc.asset.builder.Builder;
import com.hypixel.hytale.server.npc.asset.builder.BuilderDescriptorState;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.asset.builder.holder.StringHolder;
import com.hypixel.hytale.server.npc.corecomponents.builders.BuilderSensorBase;
import com.hypixel.hytale.server.npc.instructions.Sensor;
import javax.annotation.Nonnull;
import org.jetbrains.annotations.Nullable;

public class BuilderSensorBlockUnder extends BuilderSensorBase {
  private final StringHolder block = new StringHolder();

  public String getBlock(@Nonnull BuilderSupport builderSupport) {
    return this.block.get(builderSupport.getExecutionContext());
  }

  @Override
  public @Nullable String getShortDescription() {
    return "Sensor to check the block underneath the entity";
  }

  @Override
  public @Nullable String getLongDescription() {
    return this.getShortDescription();
  }

  @Override
  public @Nullable Sensor build(BuilderSupport builderSupport) {
    return new SensorBlockUnder(this, builderSupport);
  }

  @Override
  public @Nullable BuilderDescriptorState getBuilderDescriptorState() {
    return BuilderDescriptorState.Stable;
  }

  @Nonnull
  public Builder<Sensor> readConfig(@Nonnull JsonElement data) {
    this.getString(
        data,
        "Block",
        this.block,
        "air",
        null,
        BuilderDescriptorState.Stable,
        "Block underneath the entity",
        null
    );
    return this;
  }
}
