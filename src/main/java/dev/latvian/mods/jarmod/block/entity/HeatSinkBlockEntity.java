package dev.latvian.mods.jarmod.block.entity;

import dev.latvian.mods.jarmod.block.HeatSinkBlock;
import dev.latvian.mods.jarmod.recipe.HeatSourceRecipe;
import dev.latvian.mods.jarmod.recipe.JarModRecipeSerializers;
import dev.latvian.mods.jarmod.recipe.NoInventory;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;

/**
 * @author LatvianModder
 */
public class HeatSinkBlockEntity extends TileEntity implements ITickableTileEntity
{
	public int temperature;
	public int heatSourceTick;
	public HeatSourceRecipe heatSource = HeatSourceRecipe.NONE;

	public HeatSinkBlockEntity()
	{
		super(JarModBlockEntities.HEAT_SINK.get());
	}

	@Override
	public void tick()
	{
		heatSource = HeatSourceRecipe.NONE;

		BlockPos down = pos.down();
		BlockState downState = world.getBlockState(down);

		if (downState != Blocks.AIR.getDefaultState())
		{
			for (HeatSourceRecipe recipe : world.getRecipeManager().getRecipes(JarModRecipeSerializers.HEAT_SOURCE_TYPE, NoInventory.INSTANCE, world))
			{
				if (recipe.test(downState))
				{
					heatSource = recipe;
					break;
				}
			}
		}

		if (temperature > heatSource.temperature)
		{
			temperature--;

			if (temperature == 0 && getBlockState().get(HeatSinkBlock.HOT))
			{
				world.setBlockState(pos, getBlockState().with(HeatSinkBlock.HOT, false), Constants.BlockFlags.DEFAULT);
				markDirty();
			}
		}
		else if (temperature < heatSource.temperature)
		{
			if (temperature == 0 && !getBlockState().get(HeatSinkBlock.HOT))
			{
				world.setBlockState(pos, getBlockState().with(HeatSinkBlock.HOT, true), Constants.BlockFlags.DEFAULT);
				markDirty();
			}

			temperature++;
		}

		if (heatSource.temperature == 0)
		{
			heatSourceTick = 0;
		}
		else
		{
			heatSourceTick++;

			if (heatSourceTick >= heatSource.burnTime)
			{
				heatSourceTick = 0;
				markDirty();
				world.setBlockState(pos.down(), heatSource.resultBlock, Constants.BlockFlags.DEFAULT);
			}
		}
	}

	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		compound.putInt("Temperature", temperature);
		compound.putInt("HeatSourceTick", heatSourceTick);
		return super.write(compound);
	}

	@Override
	public void func_230337_a_(BlockState state, CompoundNBT compound)
	{
		super.func_230337_a_(state, compound);
		temperature = compound.getInt("Temperature");
		heatSourceTick = compound.getInt("HeatSourceTick");
	}
}
