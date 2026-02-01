package com.clayfactoria.path.builders;

import com.clayfactoria.models.WorldWaypointDefinition;
import com.clayfactoria.path.WorldPathDefinition;
import com.google.gson.JsonElement;
import com.hypixel.hytale.server.npc.asset.builder.*;
import com.hypixel.hytale.server.npc.util.expression.ExecutionContext;
import com.hypixel.hytale.server.npc.util.expression.Scope;
import com.hypixel.hytale.server.npc.validators.NPCLoadTimeValidationHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BuilderWorldPathDefinition extends BuilderBase<WorldPathDefinition> {
    protected final BuilderObjectListHelper<WorldWaypointDefinition> waypoints = new BuilderObjectListHelper<>(WorldWaypointDefinition.class, this);


    @Nonnull
    @Override
    public String getShortDescription() {
        return "List of transient path points";
    }

    @Nonnull
    @Override
    public String getLongDescription() {
        return this.getShortDescription();
    }

    @Nonnull
    public WorldPathDefinition build(@Nonnull BuilderSupport builderSupport) {
        return new WorldPathDefinition(this.getWaypoints(builderSupport));
    }

    @Nonnull
    @Override
    public Class<WorldPathDefinition> category() {
        return WorldPathDefinition.class;
    }

    @Override
    public final boolean isEnabled(ExecutionContext context) {
        return true;
    }

    @Nonnull
    @Override
    public Builder<WorldPathDefinition> readConfig(@Nonnull JsonElement data) {
        this.requireArray(
                data,
                "Waypoints",
                this.waypoints,
                null,
                BuilderDescriptorState.Stable,
                "List of world path points",
                null,
                new BuilderValidationHelper(this.fileName, null, this.internalReferenceResolver, null, null, this.extraInfo, null, this.readErrors)
        );
        return this;
    }

    @Nonnull
    @Override
    public BuilderDescriptorState getBuilderDescriptorState() {
        return BuilderDescriptorState.Stable;
    }

    @Override
    public boolean validate(
            String configName,
            @Nonnull NPCLoadTimeValidationHelper validationHelper,
            @Nonnull ExecutionContext context,
            Scope globalScope,
            @Nonnull List<String> errors
    ) {
        return super.validate(configName, validationHelper, context, globalScope, errors)
                & this.waypoints.validate(configName, validationHelper, this.builderManager, context, globalScope, errors);
    }

    @Nullable
    public List<WorldWaypointDefinition> getWaypoints(@Nonnull BuilderSupport support) {
        return this.waypoints.build(support);
    }
}
