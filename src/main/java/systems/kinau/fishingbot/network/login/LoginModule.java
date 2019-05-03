/*
 * Created by David Luedtke (MrKinau)
 * 2019/5/3
 */

package systems.kinau.fishingbot.network.login;

import lombok.Getter;
import systems.kinau.fishingbot.network.Module;
import systems.kinau.fishingbot.network.NetworkHandler;

import java.io.IOException;

public class LoginModule extends Module {

    @Getter private String userName;

    public LoginModule(String userName, NetworkHandler networkHandler) {
        super(networkHandler);
        this.userName = userName;
    }

    @Override
    public void perform() {
        getNetworkHandler().sendPacket(new PacketOutLoginStart(getUserName()));
        //TODO: Is this needed?
        try {
            getNetworkHandler().getOut().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
