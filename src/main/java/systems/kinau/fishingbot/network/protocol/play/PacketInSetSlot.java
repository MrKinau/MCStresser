/*
 * Created by David Luedtke (MrKinau)
 * 2019/5/5
 */

package systems.kinau.fishingbot.network.protocol.play;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.Getter;
import systems.kinau.fishingbot.Stresser;
import systems.kinau.fishingbot.event.play.UpdateSlotEvent;
import systems.kinau.fishingbot.network.protocol.NetworkHandler;
import systems.kinau.fishingbot.network.protocol.Packet;
import systems.kinau.fishingbot.network.utils.ByteArrayDataInputWrapper;

public class PacketInSetSlot extends Packet {

    @Getter private int windowId;
    @Getter private short slotId;
    @Getter private ByteArrayDataOutput slotData;

    public PacketInSetSlot(Stresser stresser) {
        super(stresser);
    }

    @Override
    public void write(ByteArrayDataOutput out, int protocolId) { }

    @Override
    public void read(ByteArrayDataInputWrapper in, NetworkHandler networkHandler, int length, int protocolId) {
        this.windowId = in.readByte();
        this.slotId = in.readShort();

        byte[] bytes = new byte[in.getAvailable()];
        in.readBytes(bytes);
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.write(bytes.clone());
        this.slotData = out;

        getStresser().getEventManager().callEvent(new UpdateSlotEvent(windowId, slotId, slotData));
    }
}
