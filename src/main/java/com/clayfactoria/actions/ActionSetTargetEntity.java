package com.clayfactoria.actions;

import com.clayfactoria.actions.builders.BuilderActionSetTargetEntity;
import com.clayfactoria.components.BrushComponent;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.entity.UUIDComponent;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.Inventory;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.corecomponents.ActionBase;
import com.hypixel.hytale.server.npc.role.Role;
import com.hypixel.hytale.server.npc.sensorinfo.InfoProvider;

import javax.annotation.Nonnull;
import java.util.UUID;

public class ActionSetTargetEntity extends ActionBase {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public ActionSetTargetEntity(@Nonnull BuilderActionSetTargetEntity builderAction, @Nonnull BuilderSupport builderSupport) {
        super(builderAction);
    }

    public boolean execute(@Nonnull Ref<EntityStore> ref, @Nonnull Role role, InfoProvider sensorInfo, double dt, @Nonnull Store<EntityStore> store) {
        super.execute(ref, role, sensorInfo, dt, store);
        Ref<EntityStore> playerRef = role.getStateSupport().getInteractionIterationTarget();
        if (playerRef == null) {
            return false;
        }

        Player player = store.getComponent(playerRef, Player.getComponentType());
        if (player == null) {
            return false;
        }

        UUIDComponent entityIdComp = store.getComponent(ref, UUIDComponent.getComponentType());
        if (entityIdComp == null) {
            LOGGER.atSevere().log("ActionSetTargetEntity: execute -> entityIdComp was null");
            return false;
        }

        BrushComponent brushComponent = store.getComponent(playerRef, BrushComponent.getComponentType());
        if (brushComponent == null) {
            LOGGER.atSevere().log("ActionSetTargetEntity: execute -> brushComponent was null");
            return false;
        }
        UUID entityId = entityIdComp.getUuid();
        brushComponent.setTargetEntityId(entityId);
        LOGGER.atInfo().log("ActionSetTargetEntity: execute -> Brush Component -> Set Target Entity Id: " + entityId);
        return true;
    }
}
