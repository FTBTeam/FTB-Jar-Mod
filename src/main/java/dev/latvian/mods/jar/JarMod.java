package dev.latvian.mods.jar;

import dev.latvian.mods.jar.block.JarModBlocks;
import dev.latvian.mods.jar.block.entity.JarModBlockEntities;
import dev.latvian.mods.jar.client.JarModClient;
import dev.latvian.mods.jar.fluid.JarModFluids;
import dev.latvian.mods.jar.item.JarModItems;
import dev.latvian.mods.jar.recipe.JarModRecipeSerializers;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * @author LatvianModder
 */
@Mod(JarMod.MOD_ID)
@Mod.EventBusSubscriber(modid = JarMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class JarMod
{
	public static final String MOD_ID = "jarmod";

	public static JarMod instance;
	public static JarModCommon proxy;

	public static ItemGroup group;

	public JarMod()
	{
		instance = this;
		//noinspection Convert2MethodRef
		proxy = DistExecutor.runForDist(() -> () -> new JarModClient(), () -> () -> new JarModCommon());

		group = new ItemGroup(MOD_ID)
		{
			@Override
			@OnlyIn(Dist.CLIENT)
			public ItemStack createIcon()
			{
				return new ItemStack(JarModItems.TEMPERED_JAR.get());
			}
		};

		JarModBlocks.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
		JarModItems.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
		JarModBlockEntities.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
		JarModRecipeSerializers.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
		JarModFluids.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
		proxy.init();
	}
}