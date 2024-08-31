package org.curryman.fusionfastcrystal.listeners.packet;

import org.curryman.fusionfastcrystal.enums.AnimationType;
import org.curryman.fusionfastcrystal.FusionFastCrystal;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType.Play.Client;
import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import org.curryman.fusionfastcrystal.player.CrystalPlayer;

public class LeftClickPacketListener implements PacketListener {

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getUser().getUUID() == null) {
            return;
        }

        CrystalPlayer player = CrystalPlayer.getPlayer(event.getUser().getUUID());

        if (player == null) {
            return;
        }

        if (event.getPacketType() != Client.PLAYER_DIGGING) {
            player.setLastAnimation(AnimationType.OTHER);
            return;
        }

        WrapperPlayClientPlayerDigging wrapper = new WrapperPlayClientPlayerDigging(event);

        if (wrapper.getAction() == DiggingAction.DROP_ITEM
                || wrapper.getAction() == DiggingAction.DROP_ITEM_STACK) {
            player.setLastAnimation(AnimationType.IGNORE);
        } else if (wrapper.getAction() == DiggingAction.START_DIGGING) {
            player.setLastAnimation(AnimationType.START_DIGGING);
        }
    }
}
