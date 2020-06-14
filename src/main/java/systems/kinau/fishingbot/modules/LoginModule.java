/*
 * Created by David Luedtke (MrKinau)
 * 2019/10/18
 */

package systems.kinau.fishingbot.modules;

import lombok.Getter;
import systems.kinau.fishingbot.Stresser;
import systems.kinau.fishingbot.event.EventHandler;
import systems.kinau.fishingbot.event.Listener;
import systems.kinau.fishingbot.event.login.*;
import systems.kinau.fishingbot.network.protocol.NetworkHandler;
import systems.kinau.fishingbot.network.protocol.State;
import systems.kinau.fishingbot.network.protocol.login.PacketOutEncryptionResponse;
import systems.kinau.fishingbot.network.protocol.login.PacketOutLoginPluginResponse;
import systems.kinau.fishingbot.network.protocol.login.PacketOutLoginStart;
import systems.kinau.fishingbot.network.utils.CryptManager;

import javax.crypto.SecretKey;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLEncoder;

public class LoginModule extends Module implements Listener {

    @Getter private String userName;

    public LoginModule(Stresser stresser, String userName) {
        super(stresser);
        this.userName = userName;
        stresser.getInstance().getEventManager().registerListener(this);
    }

    @Override
    public void onEnable() {
        getStresser().getInstance().getNet().sendPacket(new PacketOutLoginStart(getStresser(), getUserName()));
    }

    @Override
    public void onDisable() {
        getStresser().getInstance().getEventManager().unregisterListener(this);
    }

    @EventHandler
    public void onEncryptionRequest(EncryptionRequestEvent event) {
        NetworkHandler networkHandler = getStresser().getInstance().getNet();

        //Set public key
        networkHandler.setPublicKey(event.getPublicKey());

        //Generate & Set secret key
        SecretKey secretKey = CryptManager.createNewSharedKey();
        networkHandler.setSecretKey(secretKey);

        byte[] serverIdHash = CryptManager.getServerIdHash(event.getServerId().trim(), event.getPublicKey(), secretKey);
        if(serverIdHash == null) {
            getStresser().getLog().severe("Cannot hash server id: exiting!");
            getStresser().getInstance().setRunning(false);
            return;
        }

        String var5 = (new BigInteger(serverIdHash)).toString(16);
        String var6 = sendSessionRequest(getStresser().getInstance().getUserName(), "token:" + null + ":" + null, var5);

        networkHandler.sendPacket(new PacketOutEncryptionResponse(getStresser(), event.getServerId(), event.getPublicKey(), event.getVerifyToken(), secretKey));
        networkHandler.activateEncryption();
        networkHandler.decryptInputStream();
    }

    @EventHandler
    public void onLoginDisconnect(LoginDisconnectEvent event) {
        getStresser().getLog().severe("Login failed: " + event.getErrorMessage());
        getStresser().getInstance().setRunning(false);
    }

    @EventHandler
    public void onSetCompression(SetCompressionEvent event) {
        getStresser().getInstance().getNet().setThreshold(event.getThreshold());
    }

    @EventHandler
    public void onLoginPluginRequest(LoginPluginRequestEvent event) {
        System.out.println(event.getMsgId() + " >> " + event.getChannel());
        getStresser().getInstance().getNet().sendPacket(new PacketOutLoginPluginResponse(getStresser(), event.getMsgId(), false, null));
    }

    @EventHandler
    public void onLoginSuccess(LoginSuccessEvent event) {
        getStresser().getLog().info("Login successful!");
        getStresser().getLog().info("Name: " + event.getUserName());
        getStresser().getLog().info("UUID: " + event.getUuid());
        getStresser().getInstance().getNet().setState(State.PLAY);
    }

    private String sendSessionRequest(String user, String session, String serverid) {
        try {
            return sendGetRequest("http://session.minecraft.net/game/joinserver.jsp"
                    + "?user=" + urlEncode(user)
                    + "&sessionId=" + urlEncode(session)
                    + "&serverId=" + urlEncode(serverid));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private String sendGetRequest(String url) {
        try {
            URL var4 = new URL(url);
            BufferedReader var5 = new BufferedReader(new InputStreamReader(var4.openStream()));
            String var6 = var5.readLine();
            var5.close();
            return var6;
        } catch (IOException var7) {
            return var7.toString();
        }
    }

    private String urlEncode(String par0Str) throws IOException {
        return URLEncoder.encode(par0Str, "UTF-8");
    }
}
