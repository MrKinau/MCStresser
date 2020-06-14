/*
 * Created by David Luedtke (MrKinau)
 * 2019/5/5
 */

package systems.kinau.fishingbot.network.protocol.login;

import com.google.common.io.ByteArrayDataOutput;
import lombok.Getter;
import systems.kinau.fishingbot.Stresser;
import systems.kinau.fishingbot.event.login.EncryptionRequestEvent;
import systems.kinau.fishingbot.network.protocol.NetworkHandler;
import systems.kinau.fishingbot.network.protocol.Packet;
import systems.kinau.fishingbot.network.utils.ByteArrayDataInputWrapper;
import systems.kinau.fishingbot.network.utils.CryptManager;

import java.io.IOException;
import java.security.PublicKey;

public class PacketInEncryptionRequest extends Packet {

    @Getter private String serverId;
    @Getter private PublicKey publicKey;
    @Getter private byte[] verifyToken;

    public PacketInEncryptionRequest(Stresser stresser) {
        super(stresser);
    }

    @Override
    public void write(ByteArrayDataOutput out, int protocolId) throws IOException { }

    @Override
    public void read(ByteArrayDataInputWrapper in, NetworkHandler networkHandler, int length, int protocolId) throws IOException {
        this.serverId = readString(in);
        this.publicKey = CryptManager.decodePublicKey(readBytesFromStream(in));
        this.verifyToken = readBytesFromStream(in);

        getStresser().getInstance().getEventManager().callEvent(new EncryptionRequestEvent(serverId, publicKey, verifyToken));
    }
}
