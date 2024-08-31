package org.curryman.fusionfastcrystal.player;

import org.curryman.fusionfastcrystal.enums.AnimationType;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

public class CrystalPlayer {
    private static final Map<UUID, CrystalPlayer> PLAYERS = new ConcurrentHashMap<>();
    private final Player player;
    private AnimationType lastAnimation;

    public CrystalPlayer(Player player) {
        this.player = player;

        PLAYERS.put(player.getUniqueId(), this);
    }

    public void remove() {
        PLAYERS.remove(player.getUniqueId());
    }

    public AnimationType getLastAnimation() {
        return lastAnimation;
    }

    public ItemStack getMainHand() {
        return player.getInventory().getItemInMainHand();
    }

    public ItemStack getOffHand() {
        return player.getInventory().getItemInOffHand();
    }

    public GameMode getGameMode() {
        return player.getGameMode();
    }

    public boolean hasPotionEffect(PotionEffectType type) {
        return player.hasPotionEffect(type);
    }

    public Location getLocation() {
        return player.getLocation();
    }

    public Location getEyeLocation() {
        return player.getEyeLocation();
    }

    public void attack(Entity target) {
        player.attack(target);
    }

    public World getWorld() {
        return player.getWorld();
    }

    public boolean canSee(Player player) {
        return player.canSee(player);
    }

    public UUID getUniqueId() {
        return player.getUniqueId();
    }

    public Player getPlayer() {
        return player;
    }

    public void setLastAnimation(AnimationType type) {
        lastAnimation = type;
    }

    public static CrystalPlayer getPlayer(UUID uuid) {
        return PLAYERS.get(uuid);
    }
}