package com.clayfactoria.actions;

import com.clayfactoria.actions.builders.BuilderActionSetPath;
import com.clayfactoria.components.BrushComponent;
import com.clayfactoria.path.WorldPathDefinition;
import com.hypixel.hytale.builtin.path.path.TransientPathDefinition;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.server.core.universe.world.path.IPath;
import com.hypixel.hytale.server.core.universe.world.path.SimplePathWaypoint;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.corecomponents.ActionBase;
import com.hypixel.hytale.server.npc.entities.NPCEntity;
import com.hypixel.hytale.server.npc.role.Role;
import com.hypixel.hytale.server.npc.sensorinfo.InfoProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ActionSetPath  extends ActionBase {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    @Nullable
    protected final WorldPathDefinition pathDefinition;

    public ActionSetPath(@Nonnull BuilderActionSetPath builder, @Nonnull BuilderSupport support) {
        super(builder);
        this.pathDefinition = builder.getPath(support);
    }

    @Override
    public boolean canExecute(@Nonnull Ref<EntityStore> ref, @Nonnull Role role, InfoProvider sensorInfo, double dt, @Nonnull Store<EntityStore> store) {
        return super.canExecute(ref, role, sensorInfo, dt, store);
    }

    public boolean execute(@Nonnull Ref<EntityStore> ref, @Nonnull Role role, InfoProvider sensorInfo, double dt, @Nonnull Store<EntityStore> store) {
        super.execute(ref, role, sensorInfo, dt, store);
        Ref<EntityStore> playerRef = role.getStateSupport().getInteractionIterationTarget();
        if (playerRef == null) {
            LOGGER.atSevere().log("Action Set Path: execute -> playerRef was null");
            return false;
        }

        BrushComponent brushComponent = store.getComponent(playerRef, BrushComponent.getComponentType());
        if (brushComponent == null) {
            LOGGER.atSevere().log("Action Set Path: execute -> brushComponent was null");
            return false;
        }

        NPCEntity npcComponent = store.getComponent(ref, NPCEntity.getComponentType());
        if (npcComponent == null) {
            LOGGER.atSevere().log("Action Set Path: execute -> npcComponent was null");
            return false;
        }

        Vector3d pathStartPosition = brushComponent.getPathStartPosition();
        Vector3f pathStartRotation = brushComponent.getPathStartRotation();

        Vector3d pathEndPosition = brushComponent.getPathEndPosition();
        Vector3f pathEndRotation = brushComponent.getPathEndRotation();

        IPath<SimplePathWaypoint> path = this.pathDefinition.buildPath(pathStartPosition, pathStartRotation, pathEndPosition, pathEndRotation);
        npcComponent.getPathManager().setTransientPath(path);

        String message = String.format("Action Set Path: execute -> Successfully set Start Path %s, and End Path %s", brushComponent.getPathStart().toString(), brushComponent.getPathEnd().toString());
        LOGGER.atInfo().log(message);
        return true;
    }
}
