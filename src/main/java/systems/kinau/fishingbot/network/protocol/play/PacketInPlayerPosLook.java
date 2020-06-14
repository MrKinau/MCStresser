/*
 * Created by David Luedtke (MrKinau)
 * 2019/5/5
 */

package systems.kinau.fishingbot.network.protocol.play;

import com.google.common.io.ByteArrayDataOutput;
import lombok.Getter;
import systems.kinau.fishingbot.Stresser;
import systems.kinau.fishingbot.event.play.PosLookChangeEvent;
import systems.kinau.fishingbot.network.protocol.NetworkHandler;
import systems.kinau.fishingbot.network.protocol.Packet;
import systems.kinau.fishingbot.network.protocol.ProtocolConstants;
import systems.kinau.fishingbot.network.utils.ByteArrayDataInputWrapper;

public class PacketInPlayerPosLook extends Packet {

    @Getter private double x;
    @Getter private double y;
    @Getter private double z;
    @Getter private float yaw;
    @Getter private float pitch;
    @Getter private  int teleportId;

    public PacketInPlayerPosLook(Stresser stresser) {
        super(stresser);
    }

    @Override
    public void write(ByteArrayDataOutput out, int protocolId) {
        //Only incoming packet
    }

    @Override
    public void read(ByteArrayDataInputWrapper in, NetworkHandler networkHandler, int length, int protocolId) {
        double x = in.readDouble();
        double y = in.readDouble();
        double z = in.readDouble();
        float yaw = in.readFloat();
        float pitch = in.readFloat();
        if(in.readByte() == 0) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.yaw = yaw;
            this.pitch = pitch;
            if(protocolId >= ProtocolConstants.MINECRAFT_1_9) {
                this.teleportId = readVarInt(in); //tID
            }
            getStresser().getEventManager().callEvent(new PosLookChangeEvent(x, y, z, yaw, pitch, teleportId));
        }
    }
}
