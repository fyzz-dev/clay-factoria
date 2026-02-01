package com.clayfactoria.actions;

import com.clayfactoria.actions.builders.BuilderActionSetPath;
import com.clayfactoria.actions.builders.BuilderActionSetWorldPath;
import com.clayfactoria.components.BrushComponent;
import com.hypixel.hytale.builtin.path.WorldPathBuilder;
import com.hypixel.hytale.builtin.path.WorldPathData;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.entity.UUIDComponent;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.path.IPath;
import com.hypixel.hytale.server.core.universe.world.path.SimplePathWaypoint;
import com.hypixel.hytale.server.core.universe.world.path.WorldPath;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.corecomponents.ActionBase;
import com.hypixel.hytale.server.npc.entities.NPCEntity;
import com.hypixel.hytale.server.npc.role.Role;
import com.hypixel.hytale.server.npc.sensorinfo.InfoProvider;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ActionSetWorldPath extends ActionBase {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public ActionSetWorldPath(@Nonnull BuilderActionSetWorldPath builder, @Nonnull BuilderSupport support) {
        super(builder);
    }

    @Override
    public boolean canExecute(@Nonnull Ref<EntityStore> ref, @Nonnull Role role, InfoProvider sensorInfo, double dt, @Nonnull Store<EntityStore> store) {
        return super.canExecute(ref, role, sensorInfo, dt, store);
    }

    public boolean execute(@Nonnull Ref<EntityStore> ref, @Nonnull Role role, InfoProvider sensorInfo, double dt, @Nonnull Store<EntityStore> store) {
        super.execute(ref, role, sensorInfo, dt, store);
        Ref<EntityStore> playerRef = role.getStateSupport().getInteractionIterationTarget();
        if (playerRef == null) {
            LOGGER.atSevere().log("Action Set World Path: execute -> playerRef was null");
            return false;
        }

        Player player = store.getComponent(playerRef, Player.getComponentType());
        if (player == null) {
            LOGGER.atSevere().log("Action Set World Path: execute -> player was null");
            return false;
        }

        World world = player.getWorld();
        if (world == null) {
            LOGGER.atSevere().log("Action Set World Path: execute -> world was null");
            return false;
        }

        BrushComponent brushComponent = store.getComponent(playerRef, BrushComponent.getComponentType());
        if (brushComponent == null) {
            LOGGER.atSevere().log("Action Set World Path: execute -> brushComponent was null");
            return false;
        }

        NPCEntity npcComponent = store.getComponent(ref, NPCEntity.getComponentType());
        if (npcComponent == null) {
            LOGGER.atSevere().log("Action Set World Path: execute -> npcComponent was null");
            return false;
        }

        Vector3d pathStartPosition = brushComponent.getPathStartPosition();
        Vector3f pathStartRotation = brushComponent.getPathStartRotation();

        Vector3d pathEndPosition = brushComponent.getPathEndPosition();
        Vector3f pathEndRotation = brushComponent.getPathEndRotation();

        TransformComponent transformComponent = store.getComponent(ref, TransformComponent.getComponentType());
        if (transformComponent == null) {
            LOGGER.atSevere().log("Action Set World Path: execute -> transformComponent was null");
            return false;
        }

        Transform startTransform = transformComponent.getTransform().clone();
        startTransform.setPosition(pathStartPosition);
        startTransform.setRotation(pathStartRotation);

        Transform endTransform = transformComponent.getTransform().clone();
        endTransform.setPosition(pathEndPosition);
        endTransform.setRotation(pathEndRotation);

        WorldPathBuilder worldPathBuilder = getOrCreateBuilder(ref, store);
        WorldPath worldPath = worldPathBuilder.getPath();
        ObjectArrayList<Transform> waypoints = new ObjectArrayList<>(worldPath.getWaypoints());
        waypoints.add(startTransform);
        waypoints.add(endTransform);

        CompletableFuture<Void> future = new CompletableFuture<>();
        ScheduledFuture<?>[] scheduledFuture = new ScheduledFuture[1];
        scheduledFuture[0] = HytaleServer.SCHEDULED_EXECUTOR.scheduleWithFixedDelay(() -> {
            Transform transform = waypoints.removeFirst();
            if (transform == null) {
                future.complete(null);
                scheduledFuture[0].cancel(false);
            } else {
                world.execute(() -> {
                    Teleport teleportComponent = Teleport.createExact(transform.getPosition(), transform.getRotation());
                    store.addComponent(ref, Teleport.getComponentType(), teleportComponent);
                });
            }
        }, 1L, 1L, TimeUnit.SECONDS);

        String message = String.format("Action Set World Path: execute -> Successfully set Start Path %s, and End Path %s", brushComponent.getPathStart().toString(), brushComponent.getPathEnd().toString());
        LOGGER.atInfo().log(message);
        return true;
    }

    @Nonnull
    private static WorldPathBuilder getOrCreateBuilder(@Nonnull Ref<EntityStore> ref, @Nonnull Store<EntityStore> store) {
        WorldPathBuilder builder = store.getComponent(ref, WorldPathBuilder.getComponentType());
        return builder != null ? builder : putBuilder(ref, store, createBuilder(ref, store, null));
    }

    @Nonnull
    private static WorldPathBuilder createBuilder(@Nonnull Ref<EntityStore> ref, @Nonnull Store<EntityStore> store, @Nullable WorldPath existing) {
        UUIDComponent uuidComponent = store.getComponent(ref, UUIDComponent.getComponentType());

        assert uuidComponent != null;

        String name = "Builder-" + uuidComponent.getUuid();
        WorldPathBuilder builder = new WorldPathBuilder();
        if (existing == null) {
            builder.setPath(new WorldPath(name, new ObjectArrayList<>()));
        } else {
            builder.setPath(new WorldPath(name, new ObjectArrayList<>(existing.getWaypoints())));
        }

        return builder;
    }

    @Nonnull
    private static WorldPathBuilder putBuilder(@Nonnull Ref<EntityStore> ref, @Nonnull Store<EntityStore> store, @Nonnull WorldPathBuilder builder) {
        store.putComponent(ref, WorldPathBuilder.getComponentType(), builder);
        return builder;
    }
}
