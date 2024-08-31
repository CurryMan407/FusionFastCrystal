package org.curryman.fusionfastcrystal.listeners.bukkit;

import org.curryman.fusionfastcrystal.FusionFastCrystal;
import org.bukkit.Bukkit;

public class BukkitEventManager {

    public static void init() {
        Bukkit.getPluginManager().registerEvents(new EntityEventListener(), FusionFastCrystal.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerEventListener(), FusionFastCrystal.getInstance());
    }
}