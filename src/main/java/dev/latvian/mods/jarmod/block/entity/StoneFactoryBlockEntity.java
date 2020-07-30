package dev.latvian.mods.jarmod.block.entity;

import dev.latvian.mods.jarmod.block.JarModBlocks;
import dev.latvian.mods.jarmod.block.StoneFactoryBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;

/**
 * @author LatvianModder
 */
public class StoneFactoryBlockEntity extends TileEntity implements ITickableTileEntity
{
	public int tick;
	public int water;
	public int lava;
	public ItemStackHandler inventory;

	public StoneFactoryBlockEntity()
	{
		super(JarModBlockEntities.STONE_FACTORY.get());
		water = 0;
		lava = 0;
		inventory = new ItemStackHandler(1);
	}

	@Override
	public void tick()
	{
		if (++tick >= 5)
		{
			BlockState state = getBlockState();

			if (state.getBlock() == JarModBlocks.STONE_FACTORY.get())
			{
				StoneFactoryBlock.Mode m = getBlockState().get(StoneFactoryBlock.MODE);

				if (water >= m.water && lava >= m.lava && inventory.insertItem(0, new ItemStack(m.item.get()), world.isRemote()).isEmpty())
				{
					water -= m.water;
					lava -= m.lava;
					markDirty();
				}
			}

			tick = 0;
		}
	}

	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		compound.putByte("Tick", (byte) tick);
		compound.putInt("Water", water);
		compound.putInt("Lava", lava);
		compound.put("Inventory", inventory.serializeNBT());
		return super.write(compound);
	}

	@Override
	public void read(BlockState state, CompoundNBT compound)
	{
		tick = compound.getByte("Tick");
		water = compound.getInt("Water");
		lava = compound.getInt("Lava");
		inventory.deserializeNBT(compound.getCompound("Inventory"));
		super.read(state, compound);
	}
}
