package org.curryman.fusionfastcrystal.listeners.packet;

import org.curryman.fusionfastcrystal.FusionFastCrystal;
import org.curryman.fusionfastcrystal.enums.AnimationType;
import org.curryman.fusionfastcrystal.player.CrystalPlayer;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType.Play.Client;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import java.util.concurrent.CompletableFuture;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class AnimationPacketListener implements PacketListener {

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getUser().getUUID() == null) {
            return;
        }

        if (event.getPacketType() != Client.ANIMATION) {
            return;
        }

        CrystalPlayer player = CrystalPlayer.getPlayer(event.getUser().getUUID());

        if (player == null) {
            return;
        }

        if (player.getLastAnimation() == AnimationType.IGNORE) {
            return;
        }

        if (player.hasPotionEffect(PotionEffectType.WEAKNESS)) {
            return;
        }

        Location eyeLoc = player.getEyeLocation();

        CompletableFuture<RayTraceResult> future = CompletableFuture.supplyAsync(() -> player.getWorld()
                .rayTraceEntities(eyeLoc, player.getLocation().getDirection(), 3.0, 0.0,
                        entity -> {
                            if (entity.getType() != EntityType.PLAYER) {
                                return true;
                            }

                            Player p = (Player) entity;

                            return !player.getUniqueId().equals(p.getUniqueId())
                                    && p.getGameMode() != GameMode.SPECTATOR && player.canSee(p);
                        }), Bukkit.getScheduler().getMainThreadExecutor(FusionFastCrystal.getInstance()));

        future.thenAcceptAsync(result -> {
            if (result == null) {
                return;
            }

            Entity crystal = result.getHitEntity();

            if (crystal == null || crystal.getType() != EntityType.ENDER_CRYSTAL) {
                return;
            }

            if (crystal.getTicksLived() == 0) {
                return;
            }

            if (!crystal.getBoundingBox().contains(eyeLoc.toVector())) {
                RayTraceResult traceBlocks = player.getPlayer().rayTraceBlocks(
                        player.getGameMode() == GameMode.CREATIVE ? 5.0 : 4.5);

                if (traceBlocks != null) {
                    Block block = traceBlocks.getHitBlock();
                    Vector eyeLocV = eyeLoc.toVector();
                    if (block != null) {
                        if (eyeLocV.distanceSquared(traceBlocks.getHitPosition()) <= eyeLocV.distanceSquared(
                                result.getHitPosition())) {
                            return;
                        }

                        if (player.getLastAnimation() == AnimationType.OTHER) {
                            return;
                        }
                    }
                }
            }

            WrapperPlayServerDestroyEntities crystalDestroy = new WrapperPlayServerDestroyEntities(
                    crystal.getEntityId());

            event.getUser().sendPacket(crystalDestroy);

            new BukkitRunnable() {
                @Override
                public void run() {
                    player.attack(crystal);
                }
            }.runTask(FusionFastCrystal.getInstance());
        });
    }
}
