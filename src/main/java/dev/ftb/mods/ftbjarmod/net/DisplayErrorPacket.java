package dev.ftb.mods.ftbjarmod.net;


import dev.ftb.mods.ftbjarmod.FTBJarMod;
import me.shedaniel.architectury.networking.NetworkManager;
import me.shedaniel.architectury.networking.simple.BaseS2CMessage;
import me.shedaniel.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

/**
 * @author LatvianModder
 */
public class DisplayErrorPacket extends BaseS2CMessage {
	private final Component message;

	public DisplayErrorPacket(Component m) {
		message = m;
	}

	public DisplayErrorPacket(FriendlyByteBuf buf) {
		message = buf.readComponent();
	}

	@Override
	public MessageType getType() {
		return FTBJarModNet.DISPLAY_ERROR;
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeComponent(message);
	}

	@Override
	public void handle(NetworkManager.PacketContext context) {
		FTBJarMod.PROXY.displayError(message);
	}
}