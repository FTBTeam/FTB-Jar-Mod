package dev.ftb.mods.ftbjarmod.net;


import dev.ftb.mods.ftbjarmod.block.entity.TemperedJarBlockEntity;
import me.shedaniel.architectury.networking.NetworkManager;
import me.shedaniel.architectury.networking.simple.BaseS2CMessage;
import me.shedaniel.architectury.networking.simple.MessageType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * @author LatvianModder
 */
public class SelectJarRecipeResponsePacket extends BaseS2CMessage {
	private final BlockPos pos;
	private final ResourceLocation id;

	public SelectJarRecipeResponsePacket(BlockPos p, ResourceLocation r) {
		pos = p;
		id = r;
	}

	public SelectJarRecipeResponsePacket(FriendlyByteBuf buf) {
		pos = buf.readBlockPos();
		id = buf.readResourceLocation();
	}

	@Override
	public MessageType getType() {
		return FTBJarModNet.SELECT_JAR_RECIPE_RESPONSE;
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeBlockPos(pos);
		buf.writeResourceLocation(id);
	}

	@Override
	public void handle(NetworkManager.PacketContext context) {
		BlockEntity entity = context.getPlayer().level.getBlockEntity(pos);

		if (entity instanceof TemperedJarBlockEntity) {
			TemperedJarBlockEntity jar = (TemperedJarBlockEntity) entity;
			jar.recipe = id;
		}
	}
}