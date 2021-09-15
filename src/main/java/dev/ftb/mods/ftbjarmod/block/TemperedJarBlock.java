package dev.ftb.mods.ftbjarmod.block;


import dev.ftb.mods.ftbjarmod.block.entity.TemperedJarBlockEntity;
import dev.ftb.mods.ftbjarmod.gui.JarMenu;
import dev.ftb.mods.ftbjarmod.item.FTBJarModItems;
import dev.ftb.mods.ftbjarmod.temperature.Temperature;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.Random;

/**
 * @author LatvianModder
 */
public class TemperedJarBlock extends JarBlock {
	public static final EnumProperty<Temperature> TEMPERATURE = EnumProperty.create("temperature", Temperature.class);
	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

	public TemperedJarBlock() {
		registerDefaultState(getStateDefinition().any().setValue(TEMPERATURE, Temperature.NONE).setValue(ACTIVE, false));
	}

	@Override
	public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
		return new TemperedJarBlockEntity();
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(TEMPERATURE, ACTIVE);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return defaultBlockState().setValue(TEMPERATURE, Temperature.fromLevel(context.getLevel(), context.getClickedPos().below()).temperature);
	}

	@Override
	@Deprecated
	public BlockState updateShape(BlockState state, Direction direction, BlockState facingState, LevelAccessor level, BlockPos pos, BlockPos facingPos) {
		if (direction == Direction.DOWN) {
			BlockEntity entity = level.getBlockEntity(pos);

			if (entity instanceof TemperedJarBlockEntity) {
				entity.clearCache();
			}
		}

		return direction == Direction.DOWN && level instanceof Level ? state.setValue(TEMPERATURE, Temperature.fromLevel((Level) level, facingPos).temperature) : state;
	}

	@Override
	@Deprecated
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		ItemStack item = player.getItemInHand(hand);

		if (hit.getDirection() == Direction.UP && item.getItem() == FTBJarModItems.TUBE.get()) {
			return InteractionResult.PASS;
		}

		if (!level.isClientSide()) {
			BlockEntity entity = level.getBlockEntity(pos);

			if (entity instanceof TemperedJarBlockEntity) {
				NetworkHooks.openGui((ServerPlayer) player, new MenuProvider() {
					@Override
					public Component getDisplayName() {
						return new TranslatableComponent("block.ftbjarmod.tempered_jar");
					}

					@Override
					public AbstractContainerMenu createMenu(int id, Inventory playerInv, Player player1) {
						return new JarMenu(id, playerInv, (TemperedJarBlockEntity) entity, (TemperedJarBlockEntity) entity);
					}
				}, buf -> ((TemperedJarBlockEntity) entity).writeMenu(player, buf));
			}
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, Level level, BlockPos pos, Random rand) {
		Temperature temperature = state.getValue(TEMPERATURE);

		if (state.getValue(ACTIVE)) {
			for (int i = 0; i < 12; i++) {
				double angle = (i / 12F) * Math.PI * 2D;
				double x = pos.getX() + 0.5D + Math.cos(angle) * 0.4D;
				double y = pos.getY() + temperature.particleOffset;
				double z = pos.getZ() + 0.5D + Math.sin(angle) * 0.4D;
				level.addParticle(temperature.particleOptions, x, y, z, 0D, 0D, 0D);
			}
		}
	}
}
