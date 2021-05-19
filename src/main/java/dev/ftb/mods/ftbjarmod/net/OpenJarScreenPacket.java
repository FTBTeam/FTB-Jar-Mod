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
public class OpenJarScreenPacket extends BaseS2CPacket {
	public final BlockPos pos;
	public final boolean[] ingredients;
	public final boolean force;
	public final int recipeTime;

	public OpenJarScreenPacket(BlockPos p, boolean[] in, boolean f, int pr) {
		pos = p;
		ingredients = in;
		force = f;
		recipeTime = pr;
	}

	public OpenJarScreenPacket(FriendlyByteBuf buf) {
		pos = buf.readBlockPos();
		ingredients = new boolean[buf.readVarInt()];

		for (int i = 0; i < ingredients.length; i++) {
			ingredients[i] = buf.readBoolean();
		}

		force = buf.readBoolean();
		recipeTime = buf.readVarInt();
	}

	@Override
	public PacketID getId() {
		return FTBJarModNet.OPEN_JAR_SCREEN;
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeBlockPos(pos);
		buf.writeVarInt(ingredients.length);

		for (boolean b : ingredients) {
			buf.writeBoolean(b);
		}

		buf.writeBoolean(force);
		buf.writeVarInt(recipeTime);
	}

	@Override
	public void handle(NetworkManager.PacketContext context) {
		FTBJarMod.PROXY.openJarScreen(this);
	}
}