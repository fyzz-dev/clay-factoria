package com.clayfactoria.components;

import com.clayfactoria.ClayFactoria;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class TaskComponent implements Component<EntityStore> {
  @Nonnull
  public static final BuilderCodec<TaskComponent> CODEC =
      BuilderCodec.builder(TaskComponent.class, TaskComponent::new)
          .append(
              new KeyedCodec<>("PlayerId", Codec.UUID_BINARY),
              (comp, id) -> comp.playerId = id,
              comp -> comp.playerId)
          .documentation("The player id")
          .add()
          .append(
              new KeyedCodec<>("Path", new ArrayCodec<>(Vector3d.CODEC, Vector3d[]::new)),
              (comp, position) -> comp.path = new ArrayList<>(Arrays.asList(position)),
              (comp) -> comp.path.toArray(new Vector3d[0]))
          .documentation("The Vector3d positions for pathing")
          .add()
          .append(
              new KeyedCodec<>("CurrentTarget", Vector3d.CODEC),
              (comp, position) -> comp.currentTarget = position,
              (comp) -> comp.currentTarget)
          .documentation("The Vector3d positions for current target")
          .add()
          .append(
              new KeyedCodec<>("CurrentTargetIndex", Codec.INTEGER),
              (comp, index) -> comp.currentTargetIndex = index,
              (comp) -> comp.currentTargetIndex)
          .documentation("The index for current target")
          .add()
          .build();

  @Getter @Setter @Nullable private UUID playerId;
  @Getter @Setter private List<Vector3d> path = new ArrayList<>();
  @Getter @Setter @Nullable private Vector3d currentTarget;
  @Getter @Setter private int currentTargetIndex;

  @Nullable
  public Vector3d nextTarget() {
    int nextTargetIndex = currentTargetIndex + 1;
    if (nextTargetIndex < path.size()) {
      currentTargetIndex++;
      currentTarget = path.get(nextTargetIndex);
      return currentTarget;
    } else {
      currentTarget = path.getFirst();
      currentTargetIndex = 0;
      return null;
    }
  }

  @Override
  public Component<EntityStore> clone() {
    TaskComponent taskComponent = new TaskComponent();
    taskComponent.playerId = this.playerId;
    taskComponent.path = this.path;
    taskComponent.currentTarget = this.currentTarget;
    taskComponent.currentTargetIndex = this.currentTargetIndex;
    return taskComponent;
  }

  public static ComponentType<EntityStore, TaskComponent> getComponentType() {
    return ClayFactoria.ownerComponentType;
  }
}
