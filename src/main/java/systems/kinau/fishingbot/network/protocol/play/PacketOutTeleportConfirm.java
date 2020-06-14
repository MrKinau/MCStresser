/*
 * Created by David Luedtke (MrKinau)
 * 2019/10/18
 */

package systems.kinau.fishingbot.network.protocol.play;

import com.google.common.io.ByteArrayDataOutput;
import lombok.Getter;
import systems.kinau.fishingbot.Stresser;
import systems.kinau.fishingbot.network.protocol.NetworkHandler;
import systems.kinau.fishingbot.network.protocol.Packet;
import systems.kinau.fishingbot.network.utils.ByteArrayDataInputWrapper;

import java.io.IOException;

public class PacketOutTeleportConfirm extends Packet {

    @Getter private int teleportId;

    public PacketOutTeleportConfirm(Stresser stresser, int teleportId) {
        super(stresser);
        this.teleportId = teleportId;
    }

    @Override
    public void write(ByteArrayDataOutput out, int protocolId) throws IOException {
        writeVarInt(teleportId, out);
    }

    @Override
    public void read(ByteArrayDataInputWrapper in, NetworkHandler networkHandler, int length, int protocolId) throws IOException {

    }
}
