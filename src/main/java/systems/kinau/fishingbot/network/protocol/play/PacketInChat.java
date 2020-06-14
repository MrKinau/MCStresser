/*
 * Created by David Luedtke (MrKinau)
 * 2019/5/5
 */

/*
 * Created by Summerfeeling on May, 5th 2019
 */

package systems.kinau.fishingbot.network.protocol.play;

import com.google.common.io.ByteArrayDataOutput;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import systems.kinau.fishingbot.Stresser;
import systems.kinau.fishingbot.event.play.ChatEvent;
import systems.kinau.fishingbot.network.protocol.NetworkHandler;
import systems.kinau.fishingbot.network.protocol.Packet;
import systems.kinau.fishingbot.network.protocol.ProtocolConstants;
import systems.kinau.fishingbot.network.utils.ByteArrayDataInputWrapper;
import systems.kinau.fishingbot.network.utils.TextComponent;

import java.util.UUID;

public class PacketInChat extends Packet {

	@Getter private String text;
	@Getter private UUID sender;
	private final JsonParser PARSER = new JsonParser();

	public PacketInChat(Stresser stresser) {
		super(stresser);
	}

	@Override
	public void write(ByteArrayDataOutput out, int protocolId) {
		//Only incoming packet
	}
	
	@Override
	public void read(ByteArrayDataInputWrapper in, NetworkHandler networkHandler, int length, int protocolId) {
		this.text = readString(in);
		try {
			JsonObject object = PARSER.parse(text).getAsJsonObject();

			try {
				this.text = TextComponent.toPlainText(object);
			} catch (Exception ignored) {
				//Ignored
			}

			if (getStresser().getServerProtocol() >= ProtocolConstants.MINECRAFT_1_16_PRE_2)
				this.sender = readUUID(in);

			getStresser().getEventManager().callEvent(new ChatEvent(getText(), getSender()));
		} catch (Exception ignored) {
			//Ignored
		}
	}
}
