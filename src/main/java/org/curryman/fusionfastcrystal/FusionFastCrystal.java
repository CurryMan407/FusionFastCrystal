package org.curryman.fusionfastcrystal;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.curryman.fusionfastcrystal.listeners.bukkit.BukkitEventManager;
import org.curryman.fusionfastcrystal.listeners.packet.PacketEventManager;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Location;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.TabCompleter;


public final class FusionFastCrystal extends JavaPlugin {

    private static FusionFastCrystal INSTANCE;
    private static final Map<Integer, EnderCrystal> CRYSTALS = new ConcurrentHashMap<>();
    private static final Set<Location> CRYSTAL_LOCATIONS = ConcurrentHashMap.newKeySet();
    private static int lastEntityId;
    private FileConfiguration config;
    private Set<UUID> disabledPlayers = ConcurrentHashMap.newKeySet();

    public static FusionFastCrystal getInstance() {
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        Objects.requireNonNull(getCommand("ffc")).setExecutor(new FFCCommand());
        Objects.requireNonNull(getCommand("ffc")).setTabCompleter(new FFCTabCompleter());

        saveDefaultConfig();
        config = getConfig();

        BukkitEventManager.init();
        PacketEventManager.init();
        printStartupMessage();
    }
    @Override
    public void onDisable(){
        printDisableMessage();
    }
    private void printStartupMessage() {
        String darkPurple = "\033[0;35m";
        String lightPurple = "\033[1;35m";
        String reset = "\033[0m";

        getLogger().info(darkPurple + "------------------------------------" + reset);
        getLogger().info(lightPurple + "FusionFastCrystal" + reset);
        getLogger().info(lightPurple + "Plugin Enabled Successfully" + reset);
        getLogger().info(lightPurple + "Configuration loaded successfully" + reset);
        getLogger().info(lightPurple + "Developed by CurryMan" + reset);
        getLogger().info(lightPurple + "Made for Fusion Network" + reset);
        getLogger().info(darkPurple + "------------------------------------" + reset);
    }
    private void printDisableMessage() {
        String darkPurple = "\033[0;35m";
        String lightPurple = "\033[1;35m";
        String reset = "\033[0m";

        getLogger().info(darkPurple + "------------------------------------" + reset);
        getLogger().info(lightPurple + "FusionFastCrystal" + reset);
        getLogger().info(lightPurple + "Plugin Disabled safely" + reset);
        getLogger().info(lightPurple + "Configuration saved successfully" + reset);
        getLogger().info(lightPurple + "Developed by CurryMan" + reset);
        getLogger().info(lightPurple + "Made for Fusion Network" + reset);
        getLogger().info(darkPurple + "------------------------------------" + reset);
    }
    public static int getLastEntityId() {
        return lastEntityId;
    }

    public static void setLastEntityId(int entityId) {
        lastEntityId = entityId;
    }

    public static boolean containsCrystal(Location loc) {
        return CRYSTAL_LOCATIONS.contains(loc);
    }

    public static EnderCrystal getCrystal(int entityId) {
        return CRYSTALS.get(entityId);
    }

    public static void addCrystal(int entityId, EnderCrystal crystal) {
        CRYSTALS.put(entityId, crystal);
        CRYSTAL_LOCATIONS.add(crystal.getLocation());
    }

    public static void removeCrystal(int entityId) {
        CRYSTAL_LOCATIONS.remove(CRYSTALS.remove(entityId).getLocation());
    }
    public boolean isPlayerDisabled(UUID playerId) {
        return disabledPlayers.contains(playerId);
    }
    private String getMessage(String key) {
        return config.getString("messages." + key).replace("&", "§");
    }
    public void togglePlayer(UUID playerId) {
        Player sender = getServer().getPlayer(playerId);
        if (isPlayerDisabled(playerId)) {
            disabledPlayers.remove(playerId);
            sender.sendMessage(getMessage("toggleOn"));
        } else {
            disabledPlayers.add(playerId);
            sender.sendMessage(getMessage("toggleOff"));
        }
    }
    public void reloadPlugin() {
        reloadConfig();
        config = getConfig();
        getServer().getConsoleSender().sendMessage(getMessage("reload"));
    }
    private class FFCCommand implements CommandExecutor {

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.DARK_PURPLE + "-----------------------------");
                sender.sendMessage(ChatColor.LIGHT_PURPLE + " ");
                sender.sendMessage(ChatColor.LIGHT_PURPLE + "Fusion Fast Crystals - Made by CurryMan");
                sender.sendMessage(ChatColor.LIGHT_PURPLE + " ");
                sender.sendMessage(ChatColor.LIGHT_PURPLE + "Valid Commands:");
                sender.sendMessage(ChatColor.LIGHT_PURPLE + "/ffc toggle » Toggle fastcrystals on or off for yourself");
                sender.sendMessage(ChatColor.LIGHT_PURPLE + "/ffc reload » Reload plugin configuration");
                sender.sendMessage(ChatColor.LIGHT_PURPLE + " ");
                sender.sendMessage(ChatColor.DARK_PURPLE + "-----------------------------");
                return true;
            }

            if (args[0].equalsIgnoreCase("toggle")) {
                if (sender instanceof Player) {
                    togglePlayer(((Player) sender).getUniqueId());
                } else {
                    sender.sendMessage(ChatColor.RED + "This command can only be run by a player.");
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("fusionfastcrystal.reload")) {
                    reloadPlugin();
                } else {
                    sender.sendMessage(getMessage("noPerms"));
                }
                return true;
            }

            sender.sendMessage(getMessage("unknownCommand"));
            return true;
        }
    }
    private class FFCTabCompleter implements TabCompleter {

        @Override
        public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
            if (args.length == 1) {
                List<String> options = Arrays.asList("toggle", "reload");
                return options;
            }
            return Collections.emptyList();
        }
    }
}