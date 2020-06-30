package dev.latvian.mods.jarmod.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class JarBlockEntity extends TileEntity
{
	public final FluidTank tank;
	public final LazyOptional<IFluidHandler> tankOptional;

	public JarBlockEntity()
	{
		super(JarModBlockEntities.JAR.get());
		tank = new FluidTank(8000);
		tankOptional = LazyOptional.of(() -> tank);
	}

	public void rightClick(PlayerEntity player, Hand hand, ItemStack stack)
	{
		if (FluidUtil.interactWithFluidHandler(player, hand, tank))
		{
			// return;
		}

		if (!world.isRemote())
		{
			if (tank.isEmpty())
			{
				player.sendStatusMessage(new TranslationTextComponent("block.jarmod.jar.empty"), true);
			}
			else
			{
				player.sendStatusMessage(new TranslationTextComponent("block.jarmod.jar.mb", tank.getFluidAmount(), tank.getFluid().getDisplayName()), true);
			}
		}
	}

	@Override
	public CompoundNBT getUpdateTag()
	{
		return write(new CompoundNBT());
	}

	@Override
	public void handleUpdateTag(BlockState state, CompoundNBT tag)
	{
		func_230337_a_(state, tag);
	}

	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(pos, 0, tank.writeToNBT(new CompoundNBT()));
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		tank.readFromNBT(pkt.getNbtCompound());
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
	{
		return cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && (side == null || side == Direction.UP) ? tankOptional.cast() : super.getCapability(cap, side);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		compound.put("Tank", tank.writeToNBT(new CompoundNBT()));
		return super.write(compound);
	}

	@Override
	public void func_230337_a_(BlockState state, CompoundNBT compound)
	{
		super.func_230337_a_(state, compound);
		tank.readFromNBT(compound.getCompound("Tank"));
	}
}
