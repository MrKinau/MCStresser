/*
 * Created by David Luedtke (MrKinau)
 * 2019/10/25
 */

package systems.kinau.fishingbot.network.protocol.play;

import com.google.common.io.ByteArrayDataOutput;
import lombok.Getter;
import systems.kinau.fishingbot.Stresser;
import systems.kinau.fishingbot.event.login.SetCompressionEvent;
import systems.kinau.fishingbot.network.protocol.NetworkHandler;
import systems.kinau.fishingbot.network.protocol.Packet;
import systems.kinau.fishingbot.network.utils.ByteArrayDataInputWrapper;

import java.io.IOException;

public class PacketInSetCompressionLegacy extends Packet {

    @Getter
    private int threshold;

    public PacketInSetCompressionLegacy(Stresser stresser) {
        super(stresser);
    }

    @Override
    public void write(ByteArrayDataOutput out, int protocolId) throws IOException { }

    @Override
    public void read(ByteArrayDataInputWrapper in, NetworkHandler networkHandler, int length, int protocolId) throws IOException {
        threshold = readVarInt(in);
        getStresser().getEventManager().callEvent(new SetCompressionEvent(threshold));
    }
}
