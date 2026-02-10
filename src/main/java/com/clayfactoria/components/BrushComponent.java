package com.clayfactoria.components;

import com.clayfactoria.ClayFactoria;
import com.clayfactoria.codecs.PathType;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BrushComponent implements Component<EntityStore> {
  @Nonnull
  public static final BuilderCodec<BrushComponent> CODEC =
      BuilderCodec.builder(BrushComponent.class, BrushComponent::new)
          .append(
              new KeyedCodec<>("Path", new ArrayCodec<>(Vector3d.CODEC, Vector3d[]::new)),
              (comp, position) -> comp.path = new ArrayList<>(Arrays.asList(position)),
              (comp) -> comp.path.toArray(new Vector3d[0]))
          .documentation("The Vector3d positions for pathing")
          .add()
          .append(
              new KeyedCodec<>("PathType", PathType.CODEC),
              (comp, value) -> comp.pathType = value,
              (comp) -> comp.pathType)
          .documentation("Path type (LOOP or ONCE)")
          .add()
          .build();

  @Getter @Setter private List<Vector3d> path = new ArrayList<>();
  @Getter @Setter private PathType pathType = PathType.LOOP;

  public void addPath(Vector3d path) {
    this.path.add(path);
  }

  public PathType togglePathType() {
    if (pathType == PathType.ONCE) {
      pathType = PathType.LOOP;
    } else {
      pathType = PathType.ONCE;
    }

    return pathType;
  }

  public Component<EntityStore> clone() {
    BrushComponent brushComponent = new BrushComponent();
    brushComponent.path = this.path;
    brushComponent.pathType = this.pathType;
    return brushComponent;
  }

  public static ComponentType<EntityStore, BrushComponent> getComponentType() {
    return ClayFactoria.brushComponentType;
  }
}
