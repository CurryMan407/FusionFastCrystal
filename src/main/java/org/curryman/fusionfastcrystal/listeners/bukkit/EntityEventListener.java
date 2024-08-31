package org.curryman.fusionfastcrystal.listeners.bukkit;

import org.curryman.fusionfastcrystal.FusionFastCrystal;
import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class EntityEventListener implements Listener {
    @EventHandler
            (priority = EventPriority.MONITOR)
    public void onEntityRemoveFromWorld(EntityRemoveFromWorldEvent event) {
        if (event.getEntityType() != EntityType.ENDER_CRYSTAL) {
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                FusionFastCrystal.removeCrystal(event.getEntity().getEntityId());
            }
        }.runTaskLaterAsynchronously(FusionFastCrystal.getInstance(), 40L);
    }

    @EventHandler
            (priority = EventPriority.MONITOR)
    public void onEntitySpawn(EntitySpawnEvent event) {
        FusionFastCrystal.setLastEntityId(event.getEntity().getEntityId());

        if (event.getEntityType() != EntityType.ENDER_CRYSTAL) {
            return;
        }

        FusionFastCrystal.addCrystal(event.getEntity().getEntityId(), (EnderCrystal) event.getEntity());
    }
}
