package dev.ftb.mods.ftbjarmod.gui;

import dev.ftb.mods.ftbjarmod.block.entity.TemperedJarBlockEntity;
import dev.ftb.mods.ftbjarmod.recipe.JarRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;

public class JarMenu extends AbstractContainerMenu {
	public final TemperedJarBlockEntity jar;
	public final Player player;
	public final ContainerData containerData;
	public JarRecipe recipe;

	public JarMenu(int id, Inventory playerInv, TemperedJarBlockEntity r, ContainerData d) {
		super(FTBJarModMenus.JAR.get(), id);
		jar = r;
		containerData = d;
		player = playerInv.player;
		addDataSlots(containerData);
		recipe = TemperedJarBlockEntity.getRecipe(playerInv.player.level, r.recipe);
	}

	public JarMenu(int id, Inventory playerInv, FriendlyByteBuf buf) {
		this(id, playerInv, (TemperedJarBlockEntity) playerInv.player.level.getBlockEntity(buf.readBlockPos()), new SimpleContainerData(5));
		String r = buf.readUtf(Short.MAX_VALUE);
		recipe = TemperedJarBlockEntity.getRecipe(playerInv.player.level, r.isEmpty() ? null : new ResourceLocation(r));
	}

	@Override
	public ItemStack quickMoveStack(Player player, int slotId) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean stillValid(Player player) {
		return !jar.isRemoved();
	}

	@Override
	public void broadcastChanges() {
		super.broadcastChanges();
	}

	@Override
	public boolean clickMenuButton(Player player, int button) {
		if (button == 0) {
			if (getRecipeTime() > 0) {
				jar.stop(jar.getBlockState());
			} else {
				jar.start(jar.getBlockState(), (ServerPlayer) player);
			}
		}

		return true;
	}

	public int getAvailableResource(int index) {
		return index < 0 || index >= 3 ? 0 : containerData.get(index);
	}

	public boolean hasAnyResources() {
		return getAvailableResource(0) > 0 || getAvailableResource(1) > 0 || getAvailableResource(2) > 0;
	}

	public boolean isRightTemperature() {
		return recipe != null && jar.getTemperature().temperature == recipe.temperature;
	}

	public int getRecipeTime() {
		return containerData.get(3);
	}

	public int getMaxRecipeTime() {
		return containerData.get(4);
	}
}
