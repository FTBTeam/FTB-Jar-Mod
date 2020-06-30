package dev.latvian.mods.jarmod.item;

import dev.latvian.mods.jarmod.JarMod;
import dev.latvian.mods.jarmod.block.SluiceBlock;
import net.minecraft.item.Item;

/**
 * @author LatvianModder
 */
public class MeshItem extends Item
{
	public final SluiceBlock.Mesh mesh;

	public MeshItem(SluiceBlock.Mesh m)
	{
		super(new Properties().group(JarMod.group).maxStackSize(16));
		mesh = m;
	}
}
