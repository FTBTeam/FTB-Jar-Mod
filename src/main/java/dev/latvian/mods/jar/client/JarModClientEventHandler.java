package dev.latvian.mods.jar.client;

import dev.latvian.mods.jar.JarMod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

/**
 * @author LatvianModder
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = JarMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class JarModClientEventHandler
{
}
