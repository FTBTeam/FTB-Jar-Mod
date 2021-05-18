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
public class OpenSelectJarRecipeScreenPacket extends BaseS2CPacket {
	private final BlockPos pos;

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
		BlockEntity entity = context.getPlayer().level.getBlockEntity(pos);

		if (entity instanceof TemperedJarBlockEntity) {
			FTBJarMod.PROXY.openJarRecipeScreen((TemperedJarBlockEntity) entity);
		}
	}
}