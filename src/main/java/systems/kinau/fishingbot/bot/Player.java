/*
 * Created by David Luedtke (MrKinau)
 * 2019/10/18
 */

package systems.kinau.fishingbot.bot;

import com.google.common.io.ByteArrayDataOutput;
import lombok.Getter;
import lombok.Setter;
import systems.kinau.fishingbot.Stresser;
import systems.kinau.fishingbot.event.EventHandler;
import systems.kinau.fishingbot.event.Listener;
import systems.kinau.fishingbot.event.play.JoinGameEvent;
import systems.kinau.fishingbot.event.play.PosLookChangeEvent;
import systems.kinau.fishingbot.event.play.SetHeldItemEvent;
import systems.kinau.fishingbot.event.play.UpdateSlotEvent;
import systems.kinau.fishingbot.network.protocol.ProtocolConstants;
import systems.kinau.fishingbot.network.protocol.play.PacketOutTeleportConfirm;

public class Player implements Listener {

    @Getter @Setter private double x;
    @Getter @Setter private double y;
    @Getter @Setter private double z;
    @Getter @Setter private float yaw;
    @Getter @Setter private float pitch;

    @Getter @Setter private int experience;
    @Getter @Setter private int levels;

    @Getter @Setter private int heldSlot;
    @Getter @Setter private ByteArrayDataOutput slotData;

    @Getter @Setter private int entityID = -1;
    @Getter private Stresser stresser;

    public Player(Stresser stresser) {
        this.stresser = stresser;
        getStresser().getEventManager().registerListener(this);
    }

    @EventHandler
    public void onPosLookChange(PosLookChangeEvent event) {
        this.x = event.getX();
        this.y = event.getY();
        this.z = event.getZ();
        this.yaw = event.getYaw();
        this.pitch = event.getPitch();
        if (getStresser().getInstance().getServerProtocol() >= ProtocolConstants.MINECRAFT_1_9)
            getStresser().getInstance().getNet().sendPacket(new PacketOutTeleportConfirm(getStresser(), event.getTeleportId()));

    }

    @EventHandler
    public void onSetHeldItem(SetHeldItemEvent event) {
        this.heldSlot = event.getSlot();
    }

    @EventHandler
    public void onUpdateSlot(UpdateSlotEvent event) {
        if(event.getWindowId() != 0)
            return;
        if(event.getSlotId() != getHeldSlot())
            return;
        this.slotData = event.getSlotData();
    }

    @EventHandler
    public void onJoinGame(JoinGameEvent event) {
        setEntityID(event.getEid());
    }
}
