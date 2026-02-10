package com.clayfactoria.sensors;

import com.clayfactoria.components.TaskComponent;
import com.clayfactoria.sensors.builders.BuilderSensorLeashTarget;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.corecomponents.SensorBase;
import com.hypixel.hytale.server.npc.role.Role;
import com.hypixel.hytale.server.npc.sensorinfo.InfoProvider;
import com.hypixel.hytale.server.npc.sensorinfo.PositionProvider;

import java.util.List;
import javax.annotation.Nonnull;

public class SensorLeashTarget extends SensorBase {
  private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
  protected final PositionProvider positionProvider = new PositionProvider();

  public SensorLeashTarget(
      @Nonnull BuilderSensorLeashTarget builderSensorLeash,
      @Nonnull BuilderSupport builderSupport) {
    super(builderSensorLeash);
  }

  @Override
  public boolean matches(
      @Nonnull Ref<EntityStore> ref,
      @Nonnull Role role,
      double dt,
      @Nonnull Store<EntityStore> store) {
    if (!super.matches(ref, role, dt, store)) {
      return false;
    } else {
      TransformComponent transformComponent =
          store.getComponent(ref, TransformComponent.getComponentType());
      if (transformComponent == null) {
        LOGGER.atSevere().log("Sensor Leash Target: Transform Component was null");
        return false;
      }

      TaskComponent taskComponent = store.getComponent(ref, TaskComponent.getComponentType());
      if (taskComponent == null) {
        LOGGER.atSevere().log("Sensor Leash Target: Task Component was null");
        return false;
      }

      List<Vector3d> path = taskComponent.getPath();
      if (path == null || path.isEmpty()) {
        LOGGER.atSevere().log("Sensor Leash Target: Task Component path was null or empty");
        return false;
      }

      Vector3d currentTarget = taskComponent.getCurrentTarget();
      if (currentTarget == null) {
        LOGGER.atInfo().log(
            "Sensor Leash Target: Current Target was null. Clearing Position Provider");
        this.positionProvider.clear();
        return false;
      }

      if (transformComponent.getPosition().distanceSquaredTo(currentTarget) > 0.2f) {
        this.positionProvider.setTarget(currentTarget);
        return true;
      } else {
        Vector3d nextTarget = taskComponent.nextTarget();
        if (nextTarget == null) {
          this.positionProvider.clear();
          LOGGER.atInfo().log(
              "Sensor Leash Target: nextTarget was null. Clearing Position Provider");
          return false;
        }
        this.positionProvider.setTarget(nextTarget);
        LOGGER.atInfo().log(
            String.format(
                "Sensor Leash Target: Setting Next Target from (%.0f, %.0f, %.0f) to (%.0f, %.0f, %.0f)",
                currentTarget.x,
                currentTarget.y,
                currentTarget.z,
                nextTarget.x,
                nextTarget.y,
                nextTarget.z));
        return true;
      }
    }
  }

  @Override
  public InfoProvider getSensorInfo() {
    return this.positionProvider;
  }
}
