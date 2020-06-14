/*
 * Created by David Luedtke (MrKinau)
 * 2019/5/5
 */

package systems.kinau.fishingbot.network.protocol.login;

import com.google.common.io.ByteArrayDataOutput;
import lombok.Getter;
import systems.kinau.fishingbot.Stresser;
import systems.kinau.fishingbot.network.protocol.NetworkHandler;
import systems.kinau.fishingbot.network.protocol.Packet;
import systems.kinau.fishingbot.network.utils.ByteArrayDataInputWrapper;
import systems.kinau.fishingbot.network.utils.CryptManager;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.PublicKey;

public class PacketOutEncryptionResponse extends Packet {

    @Getter private String serverId;
    @Getter private PublicKey publicKey;
    @Getter private byte[] verifyToken;
    @Getter private SecretKey secretKey;

    public PacketOutEncryptionResponse(Stresser stresser, String serverId, PublicKey publicKey, byte[] verifyToken, SecretKey secretKey) {
        super(stresser);
        this.serverId = serverId;
        this.publicKey = publicKey;
        this.verifyToken = verifyToken;
        this.secretKey = secretKey;
    }

    @Override
    public void write(ByteArrayDataOutput out, int protocolId) throws IOException {
        byte[] sharedSecret = CryptManager.encryptData(getPublicKey(), getSecretKey().getEncoded());
        byte[] verifyToken = CryptManager.encryptData(getPublicKey(), getVerifyToken());
        writeVarInt(sharedSecret.length, out);
        out.write(sharedSecret);
        writeVarInt(verifyToken.length, out);
        out.write(verifyToken);
    }

    @Override
    public void read(ByteArrayDataInputWrapper in, NetworkHandler networkHandler, int length, int protocolId) throws IOException { }
}
