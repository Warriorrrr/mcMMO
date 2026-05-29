package com.gmail.nossr50.util;

import static com.gmail.nossr50.util.MobMetadataUtils.removeTransientMobFlags;

import com.gmail.nossr50.mcMMO;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.TNTPrimed;
import org.jetbrains.annotations.NotNull;

public class TransientMetadataTools {
    private final mcMMO pluginRef;

    public TransientMetadataTools(@NotNull mcMMO pluginRef) {
        this.pluginRef = pluginRef;
    }

    public void cleanLivingEntityMetadata(@NotNull LivingEntity entity) {
        // Restore mob name from healthbar snapshot if one is present. This ensures the entity
        // leaves the world with its correct name, not a stale healthbar string.
        MobHealthbarUtils.restoreNameFromSnapshot(entity);

        //Gets assigned to endermen, potentially doesn't get cleared before this point
        if (entity instanceof Enderman) {
            entity.removeMetadata(MetadataConstants.METADATA_KEY_TRAVELING_BLOCK, pluginRef);
        }

        //Cleanup mob metadata
        removeTransientMobFlags(entity);

        for (String key : MetadataConstants.MOB_METADATA_KEYS) {
            entity.removeMetadata(key, pluginRef);
        }
    }

    public void cleanEntityMetadata(final @NotNull Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            this.cleanLivingEntityMetadata(livingEntity);
        }

        if (entity instanceof TNTPrimed) {
            entity.removeMetadata(MetadataConstants.METADATA_KEY_TRACKED_TNT, pluginRef);
        }
    }
}
