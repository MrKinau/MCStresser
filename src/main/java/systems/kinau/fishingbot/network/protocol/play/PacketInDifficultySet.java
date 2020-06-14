/*
 * Created by David Luedtke (MrKinau)
 * 2019/5/5
 */

package systems.kinau.fishingbot.network.protocol.play;

import com.google.common.io.ByteArrayDataOutput;
import lombok.Getter;
import systems.kinau.fishingbot.Stresser;
import systems.kinau.fishingbot.event.play.DifficultySetEvent;
import systems.kinau.fishingbot.network.protocol.NetworkHandler;
import systems.kinau.fishingbot.network.protocol.Packet;
import systems.kinau.fishingbot.network.utils.ByteArrayDataInputWrapper;

public class PacketInDifficultySet extends Packet {

    @Getter private int difficulty;

    public PacketInDifficultySet(Stresser stresser) {
        super(stresser);
    }

    @Override
    public void write(ByteArrayDataOutput out, int protocolId) {
        //Only incoming packet
    }

    @Override
    public void read(ByteArrayDataInputWrapper in, NetworkHandler networkHandler, int length, int protocolId) {
        this.difficulty = in.readUnsignedByte();

        getStresser().getEventManager().callEvent(new DifficultySetEvent(getDifficulty()));
    }
}
