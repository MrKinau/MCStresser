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

public class PacketOutPosLook extends Packet {

    @Getter private double x;
    @Getter private double y;
    @Getter private double z;
    @Getter private float yaw;
    @Getter private float pitch;
    @Getter private boolean onGround;

    public PacketOutPosLook(Stresser stresser, double x, double y, double z, float yaw, float pitch, boolean onGround) {
        super(stresser);
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }

    @Override
    public void write(ByteArrayDataOutput out, int protocolId) throws IOException {
        out.writeDouble(getX());
        out.writeDouble(getY());
        out.writeDouble(getZ());
        out.writeFloat(getYaw());
        out.writeFloat(getPitch());
        out.writeBoolean(isOnGround());
    }

    @Override
    public void read(ByteArrayDataInputWrapper in, NetworkHandler networkHandler, int length, int protocolId) throws IOException {

    }
}
