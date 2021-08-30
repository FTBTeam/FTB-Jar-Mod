package dev.ftb.mods.ftbjarmod.gui;

import dev.ftb.mods.ftbjarmod.block.TemperedJarBlock;
import dev.ftb.mods.ftbjarmod.block.entity.TemperedJarBlockEntity;
import dev.ftb.mods.ftbjarmod.recipe.JarRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class JarMenu extends AbstractContainerMenu {
	public final TemperedJarBlockEntity jar;
	public final Player player;
	public final List<ItemStack> availableItems;
	public final List<FluidStack> availableFluids;
	public final ContainerData containerData;
	public JarRecipe recipe;

	public JarMenu(int id, Inventory playerInv, TemperedJarBlockEntity r, ContainerData d) {
		super(FTBJarModMenus.JAR.get(), id);
		jar = r;
		containerData = d;
		player = playerInv.player;
		addDataSlots(containerData);
		availableItems = new ArrayList<>();
		availableFluids = new ArrayList<>();
		recipe = TemperedJarBlockEntity.getRecipe(playerInv.player.level, r.recipe);
	}

	public JarMenu(int id, Inventory playerInv, FriendlyByteBuf buf) {
		this(id, playerInv, (TemperedJarBlockEntity) playerInv.player.level.getBlockEntity(buf.readBlockPos()), new SimpleContainerData(5));
		String r = buf.readUtf(Short.MAX_VALUE);
		recipe = TemperedJarBlockEntity.getRecipe(playerInv.player.level, r.isEmpty() ? null : new ResourceLocation(r));

		int si = buf.readVarInt();

		for (int i = 0; i < si; i++) {
			ItemStack stack = buf.readItem();
			stack.setCount(buf.readVarInt());
			availableItems.add(stack);
		}

		int sf = buf.readVarInt();

		for (int i = 0; i < sf; i++) {
			availableFluids.add(FluidStack.readFromPacket(buf));
		}

		availableFluids.sort((o1, o2) -> Integer.compare(o2.getAmount(), o1.getAmount()));
		availableItems.sort((o1, o2) -> Integer.compare(o2.getCount(), o1.getCount()));
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
			if (jar.getBlockState().getValue(TemperedJarBlock.ACTIVE)) {
				jar.stop();
			} else {
				jar.start(jar.getBlockState(), player);
			}
		}

		return true;
	}

	public int getAvailableResource(int index) {
		return index < 0 || index >= 3 ? 0 : containerData.get(index);
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
