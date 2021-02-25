package dev.latvian.mods.jarmod;

import dev.latvian.mods.jarmod.block.JarModBlocks;
import dev.latvian.mods.jarmod.block.entity.JarModBlockEntities;
import dev.latvian.mods.jarmod.client.JarModClient;
import dev.latvian.mods.jarmod.client.gui.JarModContainers;
import dev.latvian.mods.jarmod.item.JarModItems;
import dev.latvian.mods.jarmod.net.JarModNet;
import dev.latvian.mods.jarmod.recipe.JarModRecipeSerializers;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
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
public class JarMod {
	public static final String MOD_ID = "jarmod";

	public static JarMod instance;
	public static JarModCommon proxy;

	public static CreativeModeTab group;

	public JarMod() {
		instance = this;
		proxy = DistExecutor.safeRunForDist(() -> JarModClient::new, () -> JarModCommon::new);

		group = new CreativeModeTab(MOD_ID) {
			@Override
			@OnlyIn(Dist.CLIENT)
			public ItemStack makeIcon() {
				return new ItemStack(JarModItems.TEMPERED_JAR.get());
			}
		};

		JarModBlocks.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
		JarModItems.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
		JarModBlockEntities.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
		JarModRecipeSerializers.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
		JarModContainers.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());

		JarModNet.init();
		proxy.init();
	}
}