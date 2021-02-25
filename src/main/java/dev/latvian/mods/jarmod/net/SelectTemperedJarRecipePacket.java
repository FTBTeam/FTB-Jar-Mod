package dev.latvian.mods.jarmod.net;


import dev.latvian.mods.jarmod.block.entity.TemperedJarBlockEntity;
import dev.latvian.mods.jarmod.recipe.JarRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * @author LatvianModder
 */
public class SelectTemperedJarRecipePacket {
	private final BlockPos pos;
	private final ResourceLocation id;

	public SelectTemperedJarRecipePacket(BlockPos p, ResourceLocation r) {
		pos = p;
		id = r;
	}

	public SelectTemperedJarRecipePacket(FriendlyByteBuf buf) {
		pos = buf.readBlockPos();
		id = buf.readResourceLocation();
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeBlockPos(pos);
		buf.writeResourceLocation(id);
	}

	public void handle(Supplier<NetworkEvent.Context> context) {
		final ServerPlayer player = context.get().getSender();

		context.get().enqueueWork(() -> {
			BlockEntity entity = player.level.getBlockEntity(pos);

			if (entity instanceof TemperedJarBlockEntity) {
				player.level.getRecipeManager().byKey(id).ifPresent(r -> {
					if (r instanceof JarRecipe && ((JarRecipe) r).isAvailableFor(player)) {
						((TemperedJarBlockEntity) entity).setRecipe(player, (JarRecipe) r);
						entity.setChanged();
					}
				});
			}
		});

		context.get().setPacketHandled(true);
	}
}