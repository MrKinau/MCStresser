/*
 * Created by David Luedtke (MrKinau)
 * 2019/5/3
 */

package systems.kinau.fishingbot.network.protocol.handshake;

import com.google.common.io.ByteArrayDataOutput;
import systems.kinau.fishingbot.Stresser;
import systems.kinau.fishingbot.network.protocol.NetworkHandler;
import systems.kinau.fishingbot.network.protocol.Packet;
import systems.kinau.fishingbot.network.utils.ByteArrayDataInputWrapper;

public class PacketOutHandshake extends Packet {

    private String serverName;
    private int serverPort;

    public PacketOutHandshake(Stresser stresser, String serverName, int serverPort) {
        super(stresser);
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    @Override
    public void write(ByteArrayDataOutput out, int protocolId) {
        writeVarInt(getStresser().getInstance().getServerProtocol(), out);
        writeString(serverName + "\0FML\0", out);
        out.writeShort(serverPort);
        writeVarInt(2, out); //next State = 2 -> LOGIN
    }

    @Override
    public void read(ByteArrayDataInputWrapper in, NetworkHandler networkHandler, int length, int protocolId) { }
}
