package dev.latvian.mods.jarmod.recipe;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class JarRecipe implements IRecipe<NoInventory>
{
	private final ResourceLocation id;
	private final String group;
	public int temperature;
	public int time;
	public final List<IngredientPair<Ingredient>> inputItems;
	public final List<IngredientPair<FluidIngredient>> inputFluids;
	public final List<ItemStack> outputItems;
	public final List<FluidStack> outputFluids;

	public JarRecipe(ResourceLocation i, String g)
	{
		id = i;
		group = g;
		temperature = 0;
		time = 200;
		inputItems = new ArrayList<>();
		inputFluids = new ArrayList<>();
		outputItems = new ArrayList<>();
		outputFluids = new ArrayList<>();
	}

	@Override
	public boolean matches(NoInventory inv, World world)
	{
		return true;
	}

	@Override
	public ItemStack getCraftingResult(NoInventory inv)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canFit(int width, int height)
	{
		return true;
	}

	@Override
	public ItemStack getRecipeOutput()
	{
		return ItemStack.EMPTY;
	}

	@Override
	public ResourceLocation getId()
	{
		return id;
	}

	@Override
	public String getGroup()
	{
		return group;
	}

	@Override
	public IRecipeSerializer<?> getSerializer()
	{
		return JarModRecipeSerializers.JAR.get();
	}

	@Override
	public IRecipeType<?> getType()
	{
		return JarModRecipeSerializers.JAR_TYPE;
	}

	public boolean isAvailableFor(PlayerEntity player)
	{
		return true;
	}
}