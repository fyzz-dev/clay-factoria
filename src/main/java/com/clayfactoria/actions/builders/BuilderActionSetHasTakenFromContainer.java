package com.clayfactoria.actions.builders;

import com.clayfactoria.actions.ActionSetHasTakenFromContainer;
import com.google.gson.JsonElement;
import com.hypixel.hytale.server.npc.asset.builder.Builder;
import com.hypixel.hytale.server.npc.asset.builder.BuilderDescriptorState;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.asset.builder.holder.BooleanHolder;
import com.hypixel.hytale.server.npc.corecomponents.builders.BuilderActionBase;
import com.hypixel.hytale.server.npc.instructions.Action;
import javax.annotation.Nonnull;

public class BuilderActionSetHasTakenFromContainer extends BuilderActionBase {
  private final BooleanHolder newValue = new BooleanHolder();

  public boolean getNewValue(@Nonnull BuilderSupport builderSupport) {
    return newValue.get(builderSupport.getExecutionContext());
  }

  @Override
  @Nonnull
  public String getShortDescription() {
    return "Take items from a chest adjacent to the entity.";
  }

  @Override
  @Nonnull
  public String getLongDescription() {
    return getShortDescription();
  }

  @Override
  @Nonnull
  public Action build(BuilderSupport builderSupport) {
    return new ActionSetHasTakenFromContainer(this, builderSupport);
  }

  @Override
  @Nonnull
  public BuilderDescriptorState getBuilderDescriptorState() {
    return BuilderDescriptorState.Stable;
  }

  @Nonnull
  public Builder<Action> readConfig(@Nonnull JsonElement data) {
    this.getBoolean(
        data,
        "Value",
        this.newValue,
        false,
        getBuilderDescriptorState(),
        "The new value for SetHasTakenFromContainer",
        null
    );
    return this;
  }
}
