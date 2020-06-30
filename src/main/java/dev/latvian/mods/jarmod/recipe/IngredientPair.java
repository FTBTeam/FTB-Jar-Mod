package dev.latvian.mods.jarmod.recipe;

/**
 * @author LatvianModder
 */
public class IngredientPair<T>
{
	public final T ingredient;
	public final int amount;
	public final boolean consume;

	public IngredientPair(T i, int a, boolean c)
	{
		ingredient = i;
		amount = a;
		consume = c;
	}
}