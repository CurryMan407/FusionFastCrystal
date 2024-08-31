package org.curryman.fusionfastcrystal.listeners.packet;

import org.curryman.fusionfastcrystal.FusionFastCrystal;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;

public class PacketEventManager {
    public static void init() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(FusionFastCrystal.getInstance()));
        PacketEvents.getAPI().getSettings().bStats(true).checkForUpdates(false).debug(false);
        PacketEvents.getAPI().load();

        PacketEvents.getAPI().getEventManager().registerListener(new LeftClickPacketListener(), PacketListenerPriority.LOWEST);
        PacketEvents.getAPI().getEventManager().registerListener(new InteractPacketListener(), PacketListenerPriority.LOWEST);
        PacketEvents.getAPI().getEventManager().registerListener(new AnimationPacketListener(), PacketListenerPriority.LOWEST);
        PacketEvents.getAPI().getEventManager().registerListener(new EntityAttackListener(), PacketListenerPriority.LOWEST);
    }
}