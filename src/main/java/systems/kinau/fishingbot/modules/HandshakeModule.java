/*
 * Created by David Luedtke (MrKinau)
 * 2019/10/18
 */

package systems.kinau.fishingbot.modules;

import lombok.Getter;
import systems.kinau.fishingbot.Stresser;
import systems.kinau.fishingbot.network.protocol.State;
import systems.kinau.fishingbot.network.protocol.handshake.PacketOutHandshake;

public class HandshakeModule extends Module {

    @Getter private String serverName;
    @Getter private int serverPort;

    public HandshakeModule(Stresser stresser, String serverName, int serverPort) {
        super(stresser);
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    @Override
    public void onEnable() {
        getStresser().getInstance().getNet().sendPacket(new PacketOutHandshake(getStresser(), serverName, serverPort));
        getStresser().getInstance().getNet().setState(State.LOGIN);
    }

    @Override
    public void onDisable() {
        getStresser().getLog().warning("Tried to disable " + this.getClass().getSimpleName() + ", can not disable it!");
    }
}
