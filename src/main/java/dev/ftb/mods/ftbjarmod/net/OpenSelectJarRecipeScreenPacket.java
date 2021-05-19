package dev.ftb.mods.ftbjarmod.net;


import dev.ftb.mods.ftbjarmod.FTBJarMod;
import dev.ftb.mods.ftblibrary.net.snm.BaseS2CPacket;
import dev.ftb.mods.ftblibrary.net.snm.PacketID;
import me.shedaniel.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

/**
 * @author LatvianModder
 */
public class OpenSelectJarRecipeScreenPacket extends BaseS2CPacket {
	public final BlockPos pos;

	public OpenSelectJarRecipeScreenPacket(BlockPos p) {
		pos = p;
	}

	public OpenSelectJarRecipeScreenPacket(FriendlyByteBuf buf) {
		pos = buf.readBlockPos();
	}

	@Override
	public PacketID getId() {
		return FTBJarModNet.OPEN_SELECT_JAR_RECIPE_SCREEN;
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeBlockPos(pos);
	}

	@Override
	public void handle(NetworkManager.PacketContext context) {
		FTBJarMod.PROXY.openJarRecipeScreen(this);
	}
}