package dev.ftb.mods.ftbjarmod.net;


import dev.ftb.mods.ftbjarmod.block.entity.TemperedJarBlockEntity;
import dev.ftb.mods.ftblibrary.net.snm.BaseC2SPacket;
import dev.ftb.mods.ftblibrary.net.snm.PacketID;
import me.shedaniel.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * @author LatvianModder
 */
public class StartJarPacket extends BaseC2SPacket {
	private final BlockPos pos;

	public StartJarPacket(BlockPos p) {
		pos = p;
	}

	public StartJarPacket(FriendlyByteBuf buf) {
		pos = buf.readBlockPos();
	}

	@Override
	public PacketID getId() {
		return FTBJarModNet.START_JAR;
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeBlockPos(pos);
	}

	@Override
	public void handle(NetworkManager.PacketContext context) {
		ServerPlayer player = (ServerPlayer) context.getPlayer();
		BlockEntity entity = player.level.getBlockEntity(pos);

		if (entity instanceof TemperedJarBlockEntity) {
			((TemperedJarBlockEntity) entity).startProgress();
		}
	}
}