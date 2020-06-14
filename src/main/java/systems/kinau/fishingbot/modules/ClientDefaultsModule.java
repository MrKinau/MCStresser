/*
 * Created by David Luedtke (MrKinau)
 * 2019/10/18
 */

package systems.kinau.fishingbot.modules;

import lombok.Getter;
import systems.kinau.fishingbot.Stresser;
import systems.kinau.fishingbot.bot.Player;
import systems.kinau.fishingbot.event.EventHandler;
import systems.kinau.fishingbot.event.Listener;
import systems.kinau.fishingbot.event.play.DifficultySetEvent;
import systems.kinau.fishingbot.event.play.DisconnectEvent;
import systems.kinau.fishingbot.event.play.JoinGameEvent;
import systems.kinau.fishingbot.event.play.KeepAliveEvent;
import systems.kinau.fishingbot.network.protocol.NetworkHandler;
import systems.kinau.fishingbot.network.protocol.play.PacketOutChat;
import systems.kinau.fishingbot.network.protocol.play.PacketOutClientSettings;
import systems.kinau.fishingbot.network.protocol.play.PacketOutKeepAlive;
import systems.kinau.fishingbot.network.protocol.play.PacketOutPosition;

import java.util.Arrays;

public class ClientDefaultsModule extends Module implements Listener {

    @Getter private Thread positionThread;
    @Getter private boolean joined;

    public ClientDefaultsModule(Stresser stresser) {
        super(stresser);
    }

    @Override
    public void onEnable() {
        getStresser().getInstance().getEventManager().registerListener(this);
    }

    @Override
    public void onDisable() {
        if(getPositionThread() != null)
            getPositionThread().interrupt();
        getStresser().getInstance().getEventManager().unregisterListener(this);
    }

    @EventHandler
    public void onSetDifficulty(DifficultySetEvent event) {
        if (isJoined())
            return;
        this.joined = true;
        new Thread(() -> {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //Send start texts
            if(getStresser().getInstance().getConfig().isStartTextEnabled()) {
                Arrays.asList(getStresser().getInstance().getConfig().getStartText().split(";")).forEach(s -> {
                    getStresser().getInstance().getNet().sendPacket(new PacketOutChat(getStresser(), s.replace("%prefix%", Stresser.PREFIX)));
                });
            }

            //Start position updates
            startPositionUpdate(getStresser().getInstance().getNet());
        }).start();
    }

    @EventHandler
    public void onDisconnect(DisconnectEvent event) {
        getStresser().getLog().info("Disconnected: " + event.getDisconnectMessage());
        getStresser().getInstance().setRunning(false);
    }

    @EventHandler
    public void onJoinGame(JoinGameEvent event) {
        getStresser().getInstance().getNet().sendPacket(new PacketOutClientSettings(getStresser()));
    }

    @EventHandler
    public void onKeepAlive(KeepAliveEvent event) {
        getStresser().getInstance().getNet().sendPacket(new PacketOutKeepAlive(getStresser(), event.getId()));
    }

    private void startPositionUpdate(NetworkHandler networkHandler) {
        if(positionThread != null)
            positionThread.interrupt();
        positionThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                Player player = getStresser().getInstance().getPlayer();
                networkHandler.sendPacket(new PacketOutPosition(getStresser(), player.getX(), player.getY(), player.getZ(), true));
                try { Thread.sleep(1000); } catch (InterruptedException e) { break; }
            }
        });
        positionThread.start();
    }
}
