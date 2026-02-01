package com.clayfactoria.actions.builders;

import com.clayfactoria.actions.ActionSetPath;
import com.google.gson.JsonElement;
import com.hypixel.hytale.builtin.path.path.TransientPathDefinition;
import com.hypixel.hytale.server.npc.asset.builder.BuilderDescriptorState;
import com.hypixel.hytale.server.npc.asset.builder.BuilderObjectReferenceHelper;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.asset.builder.BuilderValidationHelper;
import com.hypixel.hytale.server.npc.corecomponents.builders.BuilderActionBase;
import com.hypixel.hytale.server.npc.corecomponents.world.builders.BuilderActionMakePath;
import com.hypixel.hytale.server.npc.instructions.Action;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

public class BuilderActionSetPath extends BuilderActionBase {

    protected final BuilderObjectReferenceHelper<TransientPathDefinition> transientPath = new BuilderObjectReferenceHelper<>(TransientPathDefinition.class, this);

    @Override
    @Nonnull
    public Action build(@Nonnull BuilderSupport builderSupport) {
        return new ActionSetPath(this, builderSupport);
    }

    @Nonnull
    public BuilderActionSetPath readConfig(@Nonnull JsonElement data) {
        this.requireObject(
                data,
                "Path",
                this.transientPath,
                BuilderDescriptorState.Stable,
                "A transient path definition",
                null,
                new BuilderValidationHelper(this.fileName, null, this.internalReferenceResolver, null, null, this.extraInfo, null, this.readErrors)
        );
        return this;
    }

    @Override
    public void registerTags(@Nonnull Set<String> tags) {
        super.registerTags(tags);
        tags.add("path");
    }

    @Override
    @Nonnull
    public BuilderDescriptorState getBuilderDescriptorState() {
        return BuilderDescriptorState.Stable;
    }

    @Override
    @Nonnull
    public String getShortDescription() {
        return "Command break block";
    }

    @Override
    @Nonnull
    public String getLongDescription() {
        return this.getShortDescription();
    }

    @Nullable
    public TransientPathDefinition getPath(@Nonnull BuilderSupport support) {
        return this.transientPath.build(support);
    }
}
