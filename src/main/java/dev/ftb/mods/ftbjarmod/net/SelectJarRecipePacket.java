package dev.ftb.mods.ftbjarmod.net;


import dev.ftb.mods.ftbjarmod.block.entity.TemperedJarBlockEntity;
import dev.ftb.mods.ftbjarmod.recipe.JarRecipe;
import me.shedaniel.architectury.networking.NetworkManager;
import me.shedaniel.architectury.networking.simple.BaseC2SMessage;
import me.shedaniel.architectury.networking.simple.MessageType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * @author LatvianModder
 */
public class SelectJarRecipePacket extends BaseC2SMessage {
	private final BlockPos pos;
	private final ResourceLocation id;

	public SelectJarRecipePacket(BlockPos p, ResourceLocation r) {
		pos = p;
		id = r;
	}

	public SelectJarRecipePacket(FriendlyByteBuf buf) {
		pos = buf.readBlockPos();
		id = buf.readResourceLocation();
	}

	@Override
	public MessageType getType() {
		return FTBJarModNet.SELECT_JAR_RECIPE;
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeBlockPos(pos);
		buf.writeResourceLocation(id);
	}

	@Override
	public void handle(NetworkManager.PacketContext context) {
		ServerPlayer player = (ServerPlayer) context.getPlayer();
		BlockEntity entity = player.level.getBlockEntity(pos);

		if (entity instanceof TemperedJarBlockEntity) {
			TemperedJarBlockEntity jar = (TemperedJarBlockEntity) entity;
			player.level.getRecipeManager().byKey(id).ifPresent(r -> {
				if (r instanceof JarRecipe && ((JarRecipe) r).isAvailableFor(player)) {
					jar.setRecipe(player, (JarRecipe) r);
					entity.setChanged();
					new SelectJarRecipeResponsePacket(pos, id).sendTo(player);
				}
			});
		}
	}
}