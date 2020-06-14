package systems.kinau.fishingbot.network.protocol.login;

import com.google.common.io.ByteArrayDataOutput;
import systems.kinau.fishingbot.Stresser;
import systems.kinau.fishingbot.network.protocol.NetworkHandler;
import systems.kinau.fishingbot.network.protocol.Packet;
import systems.kinau.fishingbot.network.utils.ByteArrayDataInputWrapper;

public class PacketOutLoginPluginResponse extends Packet {

    private int msgId;
    private boolean hasResponse;
    private byte[] data;

    public PacketOutLoginPluginResponse(Stresser stresser, int msgId, boolean hasResponse, byte[] data) {
        super(stresser);
        this.msgId = msgId;
        this.hasResponse = hasResponse;
        this.data = data;
    }

    @Override
    public void write(ByteArrayDataOutput out, int protocolId) {
        writeVarInt(msgId, out);
        out.writeBoolean(hasResponse);
        if (hasResponse)
            out.write(data);
    }

    @Override
    public void read(ByteArrayDataInputWrapper in, NetworkHandler networkHandler, int length, int protocolId) {
        // This packet is outgoing only
    }
}
