package dev.ftb.mods.ftbjarmod;

import dev.ftb.mods.ftbjarmod.block.FTBJarModBlocks;
import dev.ftb.mods.ftbjarmod.block.entity.FTBJarModBlockEntities;
import dev.ftb.mods.ftbjarmod.client.FTBJarModClient;
import dev.ftb.mods.ftbjarmod.gui.FTBJarModMenus;
import dev.ftb.mods.ftbjarmod.item.FTBJarModItems;
import dev.ftb.mods.ftbjarmod.net.FTBJarModNet;
import dev.ftb.mods.ftbjarmod.recipe.FTBJarModRecipeSerializers;
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
@Mod(FTBJarMod.MOD_ID)
@Mod.EventBusSubscriber(modid = FTBJarMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FTBJarMod {
	public static final String MOD_ID = "ftbjarmod";

	public static FTBJarMod instance;
	public static FTBJarModCommon PROXY;

	public static CreativeModeTab group;

	public FTBJarMod() {
		instance = this;
		PROXY = DistExecutor.safeRunForDist(() -> FTBJarModClient::new, () -> FTBJarModCommon::new);

		group = new CreativeModeTab(MOD_ID) {
			@Override
			@OnlyIn(Dist.CLIENT)
			public ItemStack makeIcon() {
				return new ItemStack(FTBJarModItems.TEMPERED_JAR.get());
			}
		};

		FTBJarModBlocks.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
		FTBJarModItems.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
		FTBJarModBlockEntities.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
		FTBJarModRecipeSerializers.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
		FTBJarModMenus.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());

		FTBJarModNet.init();
		PROXY.init();
	}
}