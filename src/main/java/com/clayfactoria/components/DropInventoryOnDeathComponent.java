package com.clayfactoria.components;

import com.clayfactoria.ClayFactoria;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import javax.annotation.Nonnull;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

public class DropInventoryOnDeathComponent implements Component<EntityStore> {
  @Nonnull
  public static final BuilderCodec<DropInventoryOnDeathComponent>
      CODEC = BuilderCodec.builder(DropInventoryOnDeathComponent.class, DropInventoryOnDeathComponent::new)
      .append(
          new KeyedCodec<>("DropInventoryOnDeath", Codec.BOOLEAN),
          (comp, hasTakenFromStorage) -> comp.dropInventoryOnDeath = hasTakenFromStorage,
          comp -> comp.dropInventoryOnDeath
      )
      .documentation("Whether this NPC should drop their inventory when they die").add().build();

  @Getter boolean dropInventoryOnDeath = false;

  @Override
  public @Nullable Component<EntityStore> clone() {
    DropInventoryOnDeathComponent duplicateComponent = new DropInventoryOnDeathComponent();
    duplicateComponent.dropInventoryOnDeath = this.dropInventoryOnDeath;
    return duplicateComponent;
  }

  public static ComponentType<EntityStore, DropInventoryOnDeathComponent> getComponentType() {
    return ClayFactoria.dropInventoryOnDeathComponentType;
  }
}
