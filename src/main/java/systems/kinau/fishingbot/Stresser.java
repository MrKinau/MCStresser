/*
 * Created by David Luedtke (MrKinau)
 * 2019/5/3
 */

package systems.kinau.fishingbot;

import lombok.Getter;
import lombok.Setter;
import systems.kinau.fishingbot.bot.Player;
import systems.kinau.fishingbot.event.EventManager;
import systems.kinau.fishingbot.io.LogFormatter;
import systems.kinau.fishingbot.io.SettingsConfig;
import systems.kinau.fishingbot.io.discord.DiscordMessageDispatcher;
import systems.kinau.fishingbot.modules.*;
import systems.kinau.fishingbot.network.ping.ServerPinger;
import systems.kinau.fishingbot.network.protocol.NetworkHandler;
import systems.kinau.fishingbot.network.protocol.ProtocolConstants;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Stresser {

    public static String PREFIX;
    @Getter private Stresser instance;
    @Getter public Logger log = Logger.getLogger(Stresser.class.getSimpleName());

    @Getter @Setter private boolean running;
    @Getter private SettingsConfig config;
    @Getter private DiscordMessageDispatcher discord;
    @Getter @Setter private int serverProtocol = ProtocolConstants.MINECRAFT_1_8; //default 1.8
    @Getter @Setter private String serverHost;
    @Getter @Setter private int serverPort;
    @Getter @Setter private boolean wontConnect = false;
    @Getter         private EventManager eventManager;
    @Getter         private Player player;
    @Getter         private ClientDefaultsModule clientModule;

    @Getter         private Socket socket;
    @Getter         private NetworkHandler net;
    @Getter         private String userName;

    private File logsFolder = new File("logs");

    public Stresser(String userName) {
        instance = this;

        this.userName = userName;

        try {
            final Properties properties = new Properties();
            properties.load(this.getClass().getClassLoader().getResourceAsStream("stresser.properties"));
            PREFIX = properties.getProperty("name") + " v" + properties.getProperty("version") + " - ";
        } catch (Exception ex) {
            PREFIX = "Stresser vUnknown - ";
            ex.printStackTrace();
        }

        //Initialize Logger
        log.setLevel(Level.ALL);
        ConsoleHandler ch;
        log.addHandler(ch = new ConsoleHandler());
        log.setUseParentHandlers(false);
        LogFormatter formatter = new LogFormatter();
        ch.setFormatter(formatter);

        //Generate/Load config
        this.config = new SettingsConfig();

        //Set logger file handler
        try {
            FileHandler fh;
            if(!logsFolder.exists() && !logsFolder.mkdir() && logsFolder.isDirectory())
                throw new IOException("Could not create logs folder!");
            log.addHandler(fh = new FileHandler("logs/log%g.log", 0 /* 0 = infinity */, getConfig().getLogCount()));
            fh.setFormatter(new LogFormatter());
        } catch (IOException e) {
            System.err.println("Could not create log!");
            return;
        }

        getLog().info("Starting in offline-mode with username: " + getUserName());

        String ip = getConfig().getServerIP();
        int port = getConfig().getServerPort();

        //Ping server
        getLog().info("Pinging " + ip + ":" + port + " with protocol of MC-" + getConfig().getDefaultProtocol());
        ServerPinger sp = new ServerPinger(ip, port, this);
        sp.ping();

        //Activate Discord webHook
        if(!getConfig().getWebHook().equalsIgnoreCase("false"))
            this.discord = new DiscordMessageDispatcher(getConfig().getWebHook());
    }

    public void start() {
        if(isRunning())
            return;
        connect();
    }

    private void connect() {
        String serverName = getServerHost();
        int port = getServerPort();

        do {
            try {
                setRunning(true);
                if(isWontConnect()) {
                    setWontConnect(false);
                    ServerPinger sp = new ServerPinger(getServerHost(), getServerPort(), this);
                    sp.ping();
                    if(isWontConnect()) {
                        if(!getConfig().isAutoReconnect())
                            return;
                        try {
                            Thread.sleep(getConfig().getAutoReconnectTime() * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }
                }
                this.socket = new Socket(serverName, port);

                this.net = new NetworkHandler(this);

                //Load EventManager
                this.eventManager = new EventManager();

                new HandshakeModule(this, serverName, port).enable();
                new LoginModule(this, getUserName()).enable();
                if (getConfig().isProxyChat())
                    new ChatProxyModule(this).enable();
                if(getConfig().isStartTextEnabled())
                    new ChatCommandModule(this).enable();
                this.clientModule = new ClientDefaultsModule(this);
                getClientModule().enable();
                this.player = new Player(this);

                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }));

                while (running) {
                    try {
                        net.readData();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        getLog().warning("Could not receive packet! Shutting down!");
                        break;
                    }
                }
            } catch (IOException e) {
                getLog().severe("Could not start bot: " + e.getMessage());
            } finally {
                try {
                    if (socket != null)
                        this.socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (getClientModule() != null)
                    this.getClientModule().disable();
                this.socket = null;
                this.net = null;
            }
            if (getConfig().isAutoReconnect()) {
                getLog().info("FishingBot restarts in " + getConfig().getAutoReconnectTime() + " seconds...");
                try {
                    Thread.sleep(getConfig().getAutoReconnectTime() * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } while (getConfig().isAutoReconnect());
    }
}
