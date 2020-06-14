/*
 * Created by David Luedtke (MrKinau)
 * 2019/5/5
 */

package systems.kinau.fishingbot.network.protocol.play;

import com.google.common.io.ByteArrayDataOutput;
import lombok.Getter;
import systems.kinau.fishingbot.Stresser;
import systems.kinau.fishingbot.io.discord.DiscordDetails;
import systems.kinau.fishingbot.network.protocol.NetworkHandler;
import systems.kinau.fishingbot.network.protocol.Packet;
import systems.kinau.fishingbot.network.utils.ByteArrayDataInputWrapper;

public class PacketOutChat extends Packet {

    @Getter private String message;

    public PacketOutChat(Stresser stresser, String message) {
        super(stresser);
        this.message = message;
    }

    @Override
    public void write(ByteArrayDataOutput out, int protocolId) {
        writeString(getMessage(), out);
        if(!getStresser().getConfig().getWebHook().equalsIgnoreCase("false"))
            getStresser().getDiscord().dispatchMessage("`" + getMessage() + "`",
                    new DiscordDetails("FishingBot", "https://vignette.wikia.nocookie.net/mcmmo/images/2/2f/FishingRod.png/revision/latest?cb=20110822134546"));

    }

    @Override
    public void read(ByteArrayDataInputWrapper in, NetworkHandler networkHandler, int length, int protocolId) { }
}
