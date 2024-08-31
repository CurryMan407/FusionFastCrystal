package org.curryman.fusionfastcrystal.listeners.bukkit;

import org.curryman.fusionfastcrystal.player.CrystalPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEventListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        new CrystalPlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        CrystalPlayer.getPlayer(event.getPlayer().getUniqueId()).remove();
    }
}