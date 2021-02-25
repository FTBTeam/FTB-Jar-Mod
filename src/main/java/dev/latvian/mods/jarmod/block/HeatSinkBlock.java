package dev.latvian.mods.jarmod.block;

import dev.latvian.mods.jarmod.block.entity.HeatSinkBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * @author LatvianModder
 */
public class HeatSinkBlock extends Block {
	public static final BooleanProperty HOT = BooleanProperty.create("hot");

	public static final VoxelShape SHAPE = Shapes.or(
			box(0, 0, 0, 16, 2, 16),
			box(0, 14, 0, 16, 16, 16),
			box(1, 2, 1, 15, 14, 15)
	);

	public HeatSinkBlock() {
		super(Properties.of(Material.METAL).strength(5F, 6F).sound(SoundType.METAL));
		registerDefaultState(stateDefinition.any().setValue(HOT, false));
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
		return new HeatSinkBlockEntity();
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(HOT);
	}
}
