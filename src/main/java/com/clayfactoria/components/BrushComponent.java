package com.clayfactoria.components;

import com.clayfactoria.ClayFactoria;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nonnull;
import java.util.UUID;

public class BrushComponent implements Component<EntityStore> {
    @Nonnull
    public static final BuilderCodec<BrushComponent> CODEC = BuilderCodec.builder(BrushComponent.class, BrushComponent::new)
            .append(
                    new KeyedCodec<>("TargetBlockId", Codec.STRING),
                    (component, id) -> component.targetBlockId = id,
                    component -> component.targetBlockId
            )
            .documentation("The target block id for a command").add()
            .append(
                    new KeyedCodec<>("TargetEntityId", Codec.UUID_STRING),
                    (component, id) -> component.targetEntityId = id,
                    component -> component.targetEntityId
            )
            .documentation("The target entity id for a command").add()
            .build();

    @Setter
    private String targetBlockId;

    @Setter
    @Getter
    private UUID targetEntityId;

    public Component<EntityStore> clone() {
        BrushComponent brushComponent = new BrushComponent();
        brushComponent.targetBlockId = this.targetBlockId;
        brushComponent.targetEntityId = this.targetEntityId;
        return brushComponent;
    }

    public static ComponentType<EntityStore, BrushComponent> getComponentType() {
        return ClayFactoria.brushComponentType;
    }
}
