/*
 * Created by David Luedtke (MrKinau)
 * 2019/5/5
 */

package systems.kinau.fishingbot.network.protocol.login;

import com.google.common.io.ByteArrayDataOutput;
import systems.kinau.fishingbot.Stresser;
import systems.kinau.fishingbot.network.protocol.NetworkHandler;
import systems.kinau.fishingbot.network.protocol.Packet;
import systems.kinau.fishingbot.network.utils.ByteArrayDataInputWrapper;

public class PacketOutLoginStart extends Packet {

    private String userName;

    public PacketOutLoginStart(Stresser stresser, String userName) {
        super(stresser);
        this.userName = userName;
    }

    @Override
    public void write(ByteArrayDataOutput out, int protocolId) {
        writeString(userName, out);
    }

    @Override
    public void read(ByteArrayDataInputWrapper in, NetworkHandler networkHandler, int length, int protocolId) { }
}
