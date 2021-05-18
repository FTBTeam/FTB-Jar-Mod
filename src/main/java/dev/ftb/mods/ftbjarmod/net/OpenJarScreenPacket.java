package dev.ftb.mods.ftbjarmod.net;


import dev.ftb.mods.ftbjarmod.FTBJarMod;
import dev.ftb.mods.ftbjarmod.block.entity.TemperedJarBlockEntity;
import dev.ftb.mods.ftblibrary.net.snm.BaseS2CPacket;
import dev.ftb.mods.ftblibrary.net.snm.PacketID;
import me.shedaniel.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * @author LatvianModder
 */
public class OpenJarScreenPacket extends BaseS2CPacket {
	private final BlockPos pos;
	private final boolean[] ingredients;

	public OpenJarScreenPacket(BlockPos p, boolean[] in) {
		pos = p;
		ingredients = in;
	}

	public OpenJarScreenPacket(FriendlyByteBuf buf) {
		pos = buf.readBlockPos();
		ingredients = new boolean[buf.readVarInt()];
	}

	@Override
	public PacketID getId() {
		return FTBJarModNet.OPEN_JAR_SCREEN;
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeBlockPos(pos);
		buf.writeVarInt(ingredients.length);
	}

	@Override
	public void handle(NetworkManager.PacketContext context) {
		BlockEntity entity = context.getPlayer().level.getBlockEntity(pos);

		if (entity instanceof TemperedJarBlockEntity) {
			FTBJarMod.PROXY.openJarScreen((TemperedJarBlockEntity) entity, ingredients);
		}
	}
}