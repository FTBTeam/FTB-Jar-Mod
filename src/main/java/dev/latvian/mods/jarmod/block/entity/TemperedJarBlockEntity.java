package dev.latvian.mods.jarmod.block.entity;

import dev.latvian.mods.jarmod.JarMod;
import dev.latvian.mods.jarmod.block.JarModBlocks;
import dev.latvian.mods.jarmod.recipe.JarRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class TemperedJarBlockEntity extends BlockEntity implements TickableBlockEntity {
	public long recipeTime;
	public long maxRecipeTime;
	public long temperatureTime;
	public ResourceLocation recipe;
	public boolean redstonePowered;

	public TemperedJarBlockEntity() {
		super(JarModBlockEntities.TEMPERED_JAR.get());
		recipeTime = 0L;
		maxRecipeTime = 0L;
		temperatureTime = 0L;
		recipe = null;
		redstonePowered = false;
	}

	@Override
	public void tick() {
		if (maxRecipeTime == 0L) {
			return;
		}

		recipeTime++;

		if (recipeTime >= maxRecipeTime) {
			recipeTime = 0L;
			maxRecipeTime = 0L;

			JarRecipe r = getRecipe();
		}
	}

	public void rightClick(Player player, InteractionHand hand, ItemStack item) {
		if (level == null) {
			return;
		}

		if (maxRecipeTime > 0L) {
			if (!level.isClientSide()) {
				player.displayClientMessage(new TextComponent((int) (recipeTime * 100D / (double) maxRecipeTime) + "%"), true);
			}

			return;
		}

		if (player.isCrouching() || recipe == null) {
			if (level.isClientSide()) {
				JarMod.proxy.openTemperedJarScreen(this);
			}

			return;
		}

		startProgress();
	}

	public void startProgress() {
		if (maxRecipeTime > 0L || level.isClientSide()) {
			return;
		}

		JarRecipe r = getRecipe();

		if (r == null) {
			return;
		}

		maxRecipeTime = r.time;
	}

	public void setRecipe(Player player, @Nullable JarRecipe r) {
		if (level.isClientSide()) {
			return;
		}

		JarRecipe prev = getRecipe();

		if (prev == r) {
			return;
		}

		// verify if player can set recipe //

		recipeTime = 0L;
		maxRecipeTime = 0L;
		setChangedAndSend();
	}

	public void setChangedAndSend() {
		setChanged();
		level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Constants.BlockFlags.DEFAULT_AND_RERENDER);
	}

	@Nullable
	public JarRecipe getRecipe() {
		Recipe<?> r = level.getRecipeManager().byKey(recipe).orElse(null);
		return r instanceof JarRecipe ? (JarRecipe) r : null;
	}

	@Override
	public CompoundTag save(CompoundTag compound) {
		compound.putLong("RecipeTime", recipeTime);
		compound.putLong("MaxRecipeTime", maxRecipeTime);
		compound.putLong("TemperatureTime", temperatureTime);
		compound.putString("Recipe", recipe == null ? "" : recipe.toString());
		return super.save(compound);
	}

	@Override
	public void load(BlockState state, CompoundTag compound) {
		super.load(state, compound);
		recipeTime = compound.getLong("RecipeTime");
		maxRecipeTime = compound.getLong("MaxRecipeTime");
		String r = compound.getString("Recipe");
		recipe = r.isEmpty() ? null : new ResourceLocation(r);
	}

	@Override
	public CompoundTag getUpdateTag() {
		return save(new CompoundTag());
	}

	@Override
	public void handleUpdateTag(BlockState state, CompoundTag tag) {
		load(state, tag);
	}

	@Nullable
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return new ClientboundBlockEntityDataPacket(worldPosition, 0, getUpdateTag());
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		handleUpdateTag(JarModBlocks.TEMPERED_JAR.get().defaultBlockState(), pkt.getTag());
	}

	public void neighborChanged() {
		boolean p = level.hasNeighborSignal(worldPosition);

		if (redstonePowered != p) {
			redstonePowered = p;

			if (p) {
				startProgress();
			}
		}
	}
}