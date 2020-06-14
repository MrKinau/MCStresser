/*
 * Created by David Luedtke (MrKinau)
 * 2019/10/18
 */

package systems.kinau.fishingbot.modules;

import systems.kinau.fishingbot.Stresser;
import systems.kinau.fishingbot.event.EventHandler;
import systems.kinau.fishingbot.event.Listener;
import systems.kinau.fishingbot.event.play.ChatEvent;
import systems.kinau.fishingbot.network.protocol.play.PacketOutChat;

public class ChatCommandModule extends Module implements Listener {

    public ChatCommandModule(Stresser stresser) {
        super(stresser);
    }

    @Override
    public void onEnable() {
        getStresser().getInstance().getEventManager().registerListener(this);
    }

    @Override
    public void onDisable() {
        getStresser().getInstance().getEventManager().unregisterListener(this);
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        if (isEnabled() && event.getText().contains(getStresser().getInstance().getUserName() + ", Level?")) {
            getStresser().getInstance().getNet().sendPacket(new PacketOutChat(getStresser(), getStresser().getInstance().getPlayer().getLevels() + " Level, Sir!"));
        }
    }
}
