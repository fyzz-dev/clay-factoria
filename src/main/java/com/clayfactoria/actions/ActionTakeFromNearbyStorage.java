package com.clayfactoria.actions;

import com.clayfactoria.actions.builders.BuilderActionTakeFromNearbyStorage;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.EntityChunk;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.corecomponents.ActionBase;
import com.hypixel.hytale.server.npc.entities.NPCEntity;
import com.hypixel.hytale.server.npc.role.Role;
import com.hypixel.hytale.server.npc.sensorinfo.InfoProvider;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jetbrains.annotations.NotNull;

public class ActionTakeFromNearbyStorage extends ActionBase {
  private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
  protected final int quantity;

  public ActionTakeFromNearbyStorage(@NotNull BuilderActionTakeFromNearbyStorage builder, @Nonnull
  BuilderSupport builderSupport) {
    super(builder);
    this.quantity = builder.getQuantity(builderSupport);
  }

  public boolean execute(
      @Nonnull Ref<EntityStore> ref,
      @Nonnull Role role,
      InfoProvider sensorInfo,
      double dt,
      @Nonnull Store<EntityStore> store
  ) {
    LOGGER.atInfo().log("ActionTakeFromNearbyStorage running");
    super.execute(ref, role, sensorInfo, dt, store);
    ComponentType<EntityStore, NPCEntity> component = NPCEntity.getComponentType();
    assert component != null;
    NPCEntity npcEntity = store.getComponent(ref, component);
    assert npcEntity != null;

    World world = npcEntity.getWorld();
    assert world != null;

    // Check surrounding blocks
    Vector3i containerPos = findNearbyContainer(npcEntity);
    if (containerPos == null) {
      LOGGER.atInfo().log("Found no container for ActionTakeFromNearbyStorage");
      return false;
    }

    Vector3i npcPos = npcEntity.getOldPosition().toVector3i();
    long chunkIndex = ChunkUtil.indexChunkFromBlock(containerPos.x, containerPos.z);
    WorldChunk worldChunk = world.getChunk(chunkIndex);
    assert worldChunk != null;
    EntityChunk entityChunk = worldChunk.getEntityChunk();
    assert entityChunk != null;
    Set<Ref<EntityStore>> entityStoreRefs = entityChunk.getEntityReferences();
    for (Ref<EntityStore> entityStoreRef : entityStoreRefs) {
      NPCEntity entity = store.getComponent(entityStoreRef, NPCEntity.getComponentType());
      assert entity != null;
      Universe.get().sendMessage(Message.raw("npcEntity at " + entity.getOldPosition().toString()));
    }

    return false;
  }

  private @Nullable Vector3i findNearbyContainer(NPCEntity npcEntity) {
    World world = npcEntity.getWorld();
    assert world != null;
    Vector3i pos = npcEntity.getOldPosition().toVector3i();
    LOGGER.atInfo().log("NPCEntity Position is " + pos);

    // Check surrounding blocks
    Vector3i[] directions = {
        new Vector3i(0,0,-1),
        new Vector3i(1,0,0),
        new Vector3i(0, 0, -1),
        new Vector3i(-1, 0, 0)
    };

    // Shuffle order to prevent order of check being predictable
    List<Vector3i> shuffled = Arrays.asList(directions);
    Collections.shuffle(shuffled);
    for (Vector3i dir : shuffled) {
      BlockType type = world.getBlockType(pos.add(dir));
      LOGGER.atInfo().log("Block type at " + pos.add(dir) + " was " + type);
      if (type == null) {continue;}
      String blockID = type.getId();
      if (blockID == null) {continue;}
      if (blockID.equals("container")) {
        return pos.add(dir);
      }
    }
    return null;
  }
}
