package com.clayfactoria.models.builders;

import com.clayfactoria.models.WorldWaypointDefinition;
import com.google.gson.JsonElement;
import com.hypixel.hytale.server.npc.asset.builder.Builder;
import com.hypixel.hytale.server.npc.asset.builder.BuilderBase;
import com.hypixel.hytale.server.npc.asset.builder.BuilderDescriptorState;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.asset.builder.validators.DoubleSingleValidator;
import com.hypixel.hytale.server.npc.util.expression.ExecutionContext;

import javax.annotation.Nonnull;

public class BuilderWorldWaypointDefinition extends BuilderBase<WorldWaypointDefinition> {

    protected double positionX;
    protected double positionY;
    protected double positionZ;

    protected float rotationX;
    protected float rotationY;
    protected  float rotationZ;


    @Nonnull
    @Override
    public String getShortDescription() {
        return "A simple world waypoint definition where each waypoint is a world position";
    }

    @Nonnull
    @Override
    public String getLongDescription() {
        return this.getShortDescription();
    }


    @Nonnull
    public WorldWaypointDefinition build(BuilderSupport builderSupport) {
        return new WorldWaypointDefinition(this.positionX, this.positionY, this.positionZ, this.rotationX, this.rotationY, this.rotationZ);
    }

    @Nonnull
    @Override
    public Class<WorldWaypointDefinition> category() {
        return WorldWaypointDefinition.class;
    }

    @Nonnull
    @Override
    public BuilderDescriptorState getBuilderDescriptorState() {
        return BuilderDescriptorState.Stable;
    }

    @Nonnull
    @Override
    public Builder<WorldWaypointDefinition> readConfig(@Nonnull JsonElement data) {
        this.requireDouble(
                data,
                "PositionX",
                d -> this.positionX = d,
                DoubleSingleValidator.greater0(),
                BuilderDescriptorState.Stable,
                "Position X",
                null
        );
        this.requireDouble(
                data,
                "PositionY",
                d -> this.positionY = d,
                DoubleSingleValidator.greater0(),
                BuilderDescriptorState.Stable,
                "Position Y",
                null
        );
        this.requireDouble(
                data,
                "PositionZ",
                d -> this.positionZ = d,
                DoubleSingleValidator.greater0(),
                BuilderDescriptorState.Stable,
                "Position Z",
                null
        );
        this.getFloat(
                data,
                "RotationX",
                f -> this.rotationX = f,
                0.0F,
                DoubleSingleValidator.greaterEqual0(),
                BuilderDescriptorState.Stable,
                "Rotation X",
                null
        );
        this.getFloat(
                data,
                "RotationY",
                f -> this.rotationY = f,
                0.0F,
                DoubleSingleValidator.greaterEqual0(),
                BuilderDescriptorState.Stable,
                "Rotation Y",
                null
        );
        this.getFloat(
                data,
                "RotationZ",
                f -> this.rotationZ = f,
                0.0F,
                DoubleSingleValidator.greaterEqual0(),
                BuilderDescriptorState.Stable,
                "Rotation Z",
                null
        );
        return this;
    }

    @Override
    public final boolean isEnabled(ExecutionContext context) {
        return true;
    }
}
