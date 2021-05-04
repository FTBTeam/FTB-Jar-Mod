package dev.ftb.mods.ftbjarmod.recipe;

import net.minecraft.world.item.crafting.Ingredient;

/**
 * @author LatvianModder
 */
public class ItemIngredientPair {
	public final Ingredient ingredient;
	public final int amount;

	public ItemIngredientPair(Ingredient i, int a) {
		ingredient = i;
		amount = a;
	}
}