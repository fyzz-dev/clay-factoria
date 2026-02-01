package com.clayfactoria.path;


import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.server.core.universe.world.path.IPath;
import com.hypixel.hytale.server.core.universe.world.path.SimplePathWaypoint;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class WorldPath implements IPath<SimplePathWaypoint> {
    protected final List<SimplePathWaypoint> waypoints = new ObjectArrayList<>();

    public void addWaypoint(@Nonnull Vector3d position, @Nonnull Vector3f rotation) {
        this.waypoints.add(new SimplePathWaypoint(
                this.waypoints.size(),
                new Transform(position.x, position.y, position.z, rotation.getPitch(), rotation.getYaw(), rotation.getRoll())
        ));
    }

    @Nullable
    @Override
    public UUID getId() {
        return null;
    }

    @Nullable
    @Override
    public String getName() {
        return null;
    }

    @Nonnull
    @Override
    public List<SimplePathWaypoint> getPathWaypoints() {
        return Collections.unmodifiableList(this.waypoints);
    }

    @Override
    public int length() {
        return this.waypoints.size();
    }

    public SimplePathWaypoint get(int index) {
        return this.waypoints.get(index);
    }

    @Nonnull
    public static IPath<SimplePathWaypoint> buildPath(@Nonnull List<Transform> transforms) {

        WorldPath path = new WorldPath();

        for (Transform transform : transforms) {
            Vector3d position = transform.getPosition();
            Vector3f rotation = transform.getRotation();

            path.addWaypoint(position, rotation);
        }

        return path;
    }
}
