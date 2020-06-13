package dev.latvian.mods.jar.recipe;

/**
 * @author LatvianModder
 */
public class IngredientPair<T>
{
	public final T ingredient;
	public final int amount;

	public IngredientPair(T i, int a)
	{
		ingredient = i;
		amount = a;
	}
}