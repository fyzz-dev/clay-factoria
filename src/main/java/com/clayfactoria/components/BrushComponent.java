package com.clayfactoria.components;

import com.clayfactoria.ClayFactoria;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class BrushComponent implements Component<EntityStore> {
    @Nonnull
    public static final BuilderCodec<BrushComponent> CODEC = BuilderCodec.builder(BrushComponent.class, BrushComponent::new)
            .append(
                    new KeyedCodec<>("Path", new ArrayCodec<>(Transform.CODEC, Transform[]::new)),
                    (comp, transform) -> new ArrayList<>(Arrays.asList(transform)),
                    (o) -> o.paths.toArray(new Transform[0]))
            .documentation("The Transform positions for pathing").add()
            .build();

    @Getter
    @Setter
    private List<Transform> paths =  new ArrayList<>();

    public void addPath(Transform path) {
        paths.add(path);
    }

    public Component<EntityStore> clone() {
        BrushComponent brushComponent = new BrushComponent();
        brushComponent.paths = this.paths;
        return brushComponent;
    }

    public static ComponentType<EntityStore, BrushComponent> getComponentType() {
        return ClayFactoria.brushComponentType;
    }
}
