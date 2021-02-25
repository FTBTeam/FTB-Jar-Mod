package dev.latvian.mods.jarmod.block.entity;

import dev.latvian.mods.jarmod.heat.Temperature;
import dev.latvian.mods.jarmod.recipe.JarModRecipeSerializers;
import dev.latvian.mods.jarmod.recipe.NoInventory;
import dev.latvian.mods.jarmod.recipe.TemperatureSourceRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author LatvianModder
 */
public class HeatSinkBlockEntity extends BlockEntity implements TickableBlockEntity {
	public Temperature temperature;
	public int temperatureTick;
	public TemperatureSourceRecipe temperatureSource = TemperatureSourceRecipe.NONE;

	public HeatSinkBlockEntity() {
		super(JarModBlockEntities.HEAT_SINK.get());
	}

	@Override
	public void tick() {
		temperatureSource = TemperatureSourceRecipe.NONE;

		BlockPos down = worldPosition.below();
		BlockState downState = level.getBlockState(down);

		if (downState != Blocks.AIR.defaultBlockState()) {
			for (TemperatureSourceRecipe recipe : level.getRecipeManager().getRecipesFor(JarModRecipeSerializers.TEMPERATURE_SOURCE_TYPE, NoInventory.INSTANCE, level)) {
				if (recipe.test(downState)) {
					temperatureSource = recipe;
					break;
				}
			}
		}

		/*
		if (temperature.getValue() > temperatureSource.temperature.getValue()) {
			temperature--;

			if (temperature.isNone() && getBlockState().getValue(HeatSinkBlock.HOT)) {
				level.setBlock(worldPosition, getBlockState().setValue(HeatSinkBlock.HOT, false), Constants.BlockFlags.DEFAULT);
				setChanged();
			}
		} else if (temperature.getValue() < temperatureSource.temperature.getValue()) {
			if (temperature.isNone() && !getBlockState().getValue(HeatSinkBlock.HOT)) {
				level.setBlock(worldPosition, getBlockState().setValue(HeatSinkBlock.HOT, true), Constants.BlockFlags.DEFAULT);
				setChanged();
			}

			temperature++;
		}

		if (temperatureSource.temperature.isNone()) {
			temperatureTick = 0;
		} else {
			temperatureTick++;

			if (temperatureTick >= temperatureSource.burnTime) {
				temperatureTick = 0;
				setChanged();
				level.setBlock(worldPosition.below(), temperatureSource.resultBlock, Constants.BlockFlags.DEFAULT);
			}
		}
		 */
	}

	@Override
	public CompoundTag save(CompoundTag compound) {
		compound.putString("Temperature", temperature.getSerializedName());
		compound.putInt("TemperatureTick", temperatureTick);
		return super.save(compound);
	}

	@Override
	public void load(BlockState state, CompoundTag compound) {
		super.load(state, compound);
		temperature = Temperature.byName(compound.getString("Temperature"));
		temperatureTick = compound.getInt("TemperatureTick");
	}
}
