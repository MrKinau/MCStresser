/*
 * Created by David Luedtke (MrKinau)
 * 2019/5/5
 */

package systems.kinau.fishingbot.network.protocol.play;

import com.google.common.io.ByteArrayDataOutput;
import lombok.Getter;
import systems.kinau.fishingbot.Stresser;
import systems.kinau.fishingbot.network.protocol.NetworkHandler;
import systems.kinau.fishingbot.network.protocol.Packet;
import systems.kinau.fishingbot.network.utils.ByteArrayDataInputWrapper;

public class PacketOutPosition extends Packet {

    @Getter private double x;
    @Getter private double y;
    @Getter private double z;
    @Getter private boolean onGround;

    public PacketOutPosition(Stresser stresser, double x, double y, double z, boolean onGround) {
        super(stresser);
        this.x = x;
        this.y = y;
        this.z = z;
        this.onGround = onGround;
    }

    @Override
    public void write(ByteArrayDataOutput out, int protocolId) {
        out.writeDouble(getX());
        out.writeDouble(getY());
        out.writeDouble(getZ());
        out.writeBoolean(isOnGround());
    }

    @Override
    public void read(ByteArrayDataInputWrapper in, NetworkHandler networkHandler, int length, int protocolId) { }
}
