/*
 * Created by David Luedtke (MrKinau)
 * 2019/10/19
 */

package systems.kinau.fishingbot.network.protocol.play;

import com.google.common.io.ByteArrayDataOutput;
import lombok.Getter;
import systems.kinau.fishingbot.Stresser;
import systems.kinau.fishingbot.network.protocol.NetworkHandler;
import systems.kinau.fishingbot.network.protocol.Packet;
import systems.kinau.fishingbot.network.utils.ByteArrayDataInputWrapper;

import java.io.IOException;

public class PacketOutPlayer extends Packet {

    @Getter private boolean onGround;

    public PacketOutPlayer(Stresser stresser, boolean onGround) {
        super(stresser);
        this.onGround = onGround;
    }

    @Override
    public void write(ByteArrayDataOutput out, int protocolId) throws IOException {
        out.writeBoolean(onGround);
    }

    @Override
    public void read(ByteArrayDataInputWrapper in, NetworkHandler networkHandler, int length, int protocolId) throws IOException { }
}
