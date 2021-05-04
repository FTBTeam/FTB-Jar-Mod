package dev.ftb.mods.ftbjarmod.block.entity;

import dev.ftb.mods.ftbjarmod.recipe.JarRecipe;
import net.minecraft.world.item.ItemStack;

/**
 * @author LatvianModder
 */
public class JarItemHandlerInput extends JarItemHandler {
	public JarItemHandlerInput(TemperedJarBlockEntity e, int s) {
		super(e, s);
	}

	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		JarRecipe r = entity.getRecipe();
		return r != null && slot < r.inputItems.size() && r.inputItems.get(slot).ingredient.test(stack);
	}

	@Override
	public int getSlotLimit(int slot) {
		JarRecipe r = entity.getRecipe();
		return r != null && slot < r.inputItems.size() ? r.inputItems.get(slot).amount : 0;
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		return ItemStack.EMPTY;
	}
}
