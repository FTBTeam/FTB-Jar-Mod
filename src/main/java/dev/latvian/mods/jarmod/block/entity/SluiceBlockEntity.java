package dev.latvian.mods.jarmod.block.entity;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

/**
 * @author LatvianModder
 */
public class SluiceBlockEntity extends TileEntity implements ITickableTileEntity
{
	public SluiceBlockEntity()
	{
		super(JarModBlockEntities.SLUICE.get());
	}

	@Override
	public void tick()
	{
	}
}
