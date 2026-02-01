package com.clayfactoria.path;

import com.clayfactoria.models.WorldWaypointDefinition;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.server.core.universe.world.path.IPath;
import com.hypixel.hytale.server.core.universe.world.path.SimplePathWaypoint;

import javax.annotation.Nonnull;
import java.util.ArrayDeque;
import java.util.List;

public class WorldPathDefinition {
    protected final List<WorldWaypointDefinition> waypointDefinitions;

    public WorldPathDefinition(List<WorldWaypointDefinition> waypointDefinitions) {
        this.waypointDefinitions = waypointDefinitions;
    }

    @Nonnull
    public IPath<SimplePathWaypoint> buildPath(@Nonnull Vector3d position, @Nonnull Vector3f rotation) {
        ArrayDeque<WorldWaypointDefinition> queue = new ArrayDeque<>(this.waypointDefinitions);
        return WorldPath.buildPath(position, rotation, queue);
    }
}
