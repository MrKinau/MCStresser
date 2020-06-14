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

import java.util.Scanner;

public class ChatProxyModule extends Module implements Listener {

    private Thread chatThread;

    public ChatProxyModule(Stresser stresser) {
        super(stresser);
    }

    @Override
    public void onEnable() {
        getStresser().getInstance().getEventManager().registerListener(this);
        chatThread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while(!chatThread.isInterrupted()){
                String line = scanner.nextLine();
                getStresser().getInstance().getNet().sendPacket(new PacketOutChat(getStresser(), line));
            }
        });
        chatThread.start();
    }

    @Override
    public void onDisable() {
        getStresser().getInstance().getEventManager().unregisterListener(this);
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        if (isEnabled() && !"".equals(event.getText()))
            getStresser().getLog().info("[CHAT] " + event.getText());
    }
}
