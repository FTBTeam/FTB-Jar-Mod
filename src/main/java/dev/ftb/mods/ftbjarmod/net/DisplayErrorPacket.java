package dev.ftb.mods.ftbjarmod.net;


import dev.ftb.mods.ftbjarmod.FTBJarMod;
import dev.ftb.mods.ftblibrary.net.snm.BaseS2CPacket;
import dev.ftb.mods.ftblibrary.net.snm.PacketID;
import me.shedaniel.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

/**
 * @author LatvianModder
 */
public class DisplayErrorPacket extends BaseS2CPacket {
	private final Component message;

	public DisplayErrorPacket(Component m) {
		message = m;
	}

	public DisplayErrorPacket(FriendlyByteBuf buf) {
		message = buf.readComponent();
	}

	@Override
	public PacketID getId() {
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