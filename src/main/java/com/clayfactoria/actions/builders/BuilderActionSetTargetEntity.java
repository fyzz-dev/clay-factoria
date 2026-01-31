package com.clayfactoria.actions.builders;

import com.clayfactoria.actions.ActionSetTargetEntity;
import com.google.gson.JsonElement;
import com.hypixel.hytale.server.npc.asset.builder.BuilderDescriptorState;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.corecomponents.builders.BuilderActionBase;
import com.hypixel.hytale.server.npc.instructions.Action;

import javax.annotation.Nonnull;

public class BuilderActionSetTargetEntity extends BuilderActionBase {
    @Override
    @Nonnull
    public Action build(@Nonnull BuilderSupport builderSupport) {
        return new ActionSetTargetEntity(this, builderSupport);
    }

    public BuilderActionSetTargetEntity readConfig(@Nonnull JsonElement data) {
        return this;
    }

    @Override
    @Nonnull
    public BuilderDescriptorState getBuilderDescriptorState() {
        return BuilderDescriptorState.Stable;
    }

    @Override
    @Nonnull
    public String getShortDescription() {
        return "Command break block";
    }

    @Override
    @Nonnull
    public String getLongDescription() {
        return this.getShortDescription();
    }
}
