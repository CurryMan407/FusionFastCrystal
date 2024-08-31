package org.curryman.fusionfastcrystal.listeners.packet;

import org.curryman.fusionfastcrystal.FusionFastCrystal;
import org.curryman.fusionfastcrystal.player.CrystalPlayer;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType.Play.Client;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity.InteractAction;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.scheduler.BukkitRunnable;

public class EntityAttackListener implements PacketListener {

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() != Client.INTERACT_ENTITY) {
            return;
        }

        WrapperPlayClientInteractEntity wrapper = new WrapperPlayClientInteractEntity(event);

        if (wrapper.getAction() != InteractAction.ATTACK) {
            return;
        }

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

        EnderCrystal crystal = FusionFastCrystal.getCrystal(wrapper.getEntityId());

        WrapperPlayServerDestroyEntities crystalDestroy = new WrapperPlayServerDestroyEntities(
                crystal.getEntityId());

        event.getUser().sendPacket(crystalDestroy);

        new BukkitRunnable() {
            @Override
            public void run() {
                player.attack(crystal);
            }
        }.runTask(FusionFastCrystal.getInstance());
    }
}