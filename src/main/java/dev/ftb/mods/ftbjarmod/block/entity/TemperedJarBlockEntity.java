package dev.ftb.mods.ftbjarmod.block.entity;

import dev.ftb.mods.ftbjarmod.FTBJarMod;
import dev.ftb.mods.ftbjarmod.block.FTBJarModBlocks;
import dev.ftb.mods.ftbjarmod.recipe.JarRecipe;
import dev.ftb.mods.ftblibrary.util.TimeUtils;
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
	public int recipeTime;
	public int temperatureTime;
	public ResourceLocation recipe;
	public boolean redstonePowered;

	public TemperedJarBlockEntity() {
		super(FTBJarModBlockEntities.TEMPERED_JAR.get());
		recipeTime = 0;
		temperatureTime = 0;
		recipe = null;
		redstonePowered = false;
	}

	@Override
	public void tick() {
		if (recipeTime > 0) {
			recipeTime--;

			if (recipeTime == 0) {
				JarRecipe r = getRecipe();
			}
		}
	}

	public void rightClick(Player player, InteractionHand hand, ItemStack item) {
		if (level == null) {
			return;
		}

		if (recipeTime > 0) {
			if (!level.isClientSide()) {
				player.displayClientMessage(new TextComponent(TimeUtils.prettyTimeString(recipeTime / 20L) + " left"), true);
			}

			return;
		}

		if (player.isCrouching() || recipe == null) {
			if (level.isClientSide()) {
				FTBJarMod.PROXY.openTemperedJarScreen(this);
			}

			return;
		}

		startProgress();

		if (!level.isClientSide()) {
			player.displayClientMessage(new TextComponent(TimeUtils.prettyTimeString(recipeTime / 20L) + " left"), true);
		}
	}

	public void startProgress() {
		if (level.isClientSide() || recipeTime > 0) {
			return;
		}

		JarRecipe r = getRecipe();

		if (r == null) {
			return;
		}

		recipeTime = r.time;
		setChangedAndSend();
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

		recipeTime = 0;

		if (r == null) {
			recipe = null;
		} else {
			recipe = r.getId();
		}

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
	public CompoundTag save(CompoundTag tag) {
		tag.putInt("RecipeTime", recipeTime);
		tag.putInt("TemperatureTime", temperatureTime);
		tag.putString("Recipe", recipe == null ? "" : recipe.toString());
		return super.save(tag);
	}

	@Override
	public void load(BlockState state, CompoundTag tag) {
		super.load(state, tag);
		recipeTime = tag.getInt("RecipeTime");
		temperatureTime = tag.getInt("TemperatureTime");
		String r = tag.getString("Recipe");
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
		handleUpdateTag(FTBJarModBlocks.TEMPERED_JAR.get().defaultBlockState(), pkt.getTag());
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