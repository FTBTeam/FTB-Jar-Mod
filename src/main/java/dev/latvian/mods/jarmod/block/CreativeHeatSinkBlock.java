package dev.latvian.mods.jarmod.block;

import dev.latvian.mods.jarmod.block.entity.CreativeHeatSinkBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * @author LatvianModder
 */
public class CreativeHeatSinkBlock extends Block {
	public CreativeHeatSinkBlock() {
		super(Properties.of(Material.METAL).noDrops().strength(-1.0F, 3600000.0F).noDrops().sound(SoundType.METAL));
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
		return new CreativeHeatSinkBlockEntity();
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return HeatSinkBlock.SHAPE;
	}
}
