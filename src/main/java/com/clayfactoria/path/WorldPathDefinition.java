package com.clayfactoria.path;

import com.clayfactoria.models.WorldWaypointDefinition;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.server.core.universe.world.path.IPath;
import com.hypixel.hytale.server.core.universe.world.path.SimplePathWaypoint;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class WorldPathDefinition {
    protected final List<WorldWaypointDefinition> waypointDefinitions;

    public WorldPathDefinition(List<WorldWaypointDefinition> waypointDefinitions) {
        this.waypointDefinitions = waypointDefinitions;
    }

    @Nonnull
    public IPath<SimplePathWaypoint> buildPath(@Nonnull Vector3d startPosition, @Nonnull Vector3f startRotation, @Nullable Vector3d endPosition, @Nullable Vector3f endRotation) {
        return WorldPath.buildPath(startPosition, startRotation, endPosition, endRotation);
    }
}
