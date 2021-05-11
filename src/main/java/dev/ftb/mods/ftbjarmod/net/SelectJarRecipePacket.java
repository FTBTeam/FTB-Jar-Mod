package dev.ftb.mods.ftbjarmod.net;


import dev.ftb.mods.ftbjarmod.block.entity.TemperedJarBlockEntity;
import dev.ftb.mods.ftbjarmod.recipe.JarRecipe;
import dev.ftb.mods.ftblibrary.net.snm.BaseC2SPacket;
import dev.ftb.mods.ftblibrary.net.snm.PacketID;
import me.shedaniel.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * @author LatvianModder
 */
public class SelectJarRecipePacket extends BaseC2SPacket {
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
	public PacketID getId() {
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
			player.level.getRecipeManager().byKey(id).ifPresent(r -> {
				if (r instanceof JarRecipe && ((JarRecipe) r).isAvailableFor(player)) {
					((TemperedJarBlockEntity) entity).setRecipe(player, (JarRecipe) r);
					entity.setChanged();
				}
			});
		}
	}
}