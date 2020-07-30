package dev.latvian.mods.jarmod.block;

import dev.latvian.mods.jarmod.block.entity.StoneFactoryBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockReader;

import java.util.function.Supplier;

/**
 * @author LatvianModder
 */
public class StoneFactoryBlock extends Block
{
	public enum Mode implements IStringSerializable
	{
		COBBLESTONE("cobblestone", 0, 0, () -> Items.COBBLESTONE),
		STONE("stone", 125, 0, () -> Items.STONE),
		OBSIDIAN("obsidian", 1000, 1000, () -> Items.OBSIDIAN);

		public final String name;
		public final int water;
		public final int lava;
		public final Supplier<Item> item;

		Mode(String n, int w, int l, Supplier<Item> i)
		{
			name = n;
			water = w;
			lava = l;
			item = i;
		}

		@Override
		public String getString()
		{
			return name;
		}
	}

	public static final EnumProperty<Mode> MODE = EnumProperty.create("mode", Mode.class);

	public StoneFactoryBlock()
	{
		super(Properties.create(Material.ROCK).hardnessAndResistance(5F, 6F).sound(SoundType.STONE));
		setDefaultState(stateContainer.getBaseState().with(MODE, Mode.COBBLESTONE));
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new StoneFactoryBlockEntity();
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(MODE);
	}
}