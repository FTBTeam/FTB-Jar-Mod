package dev.ftb.mods.ftbjarmod.net;

import dev.ftb.mods.ftbjarmod.FTBJarMod;
import dev.ftb.mods.ftblibrary.net.snm.PacketID;
import dev.ftb.mods.ftblibrary.net.snm.SimpleNetworkManager;

/**
 * @author LatvianModder
 */
public interface FTBJarModNet {
	SimpleNetworkManager NET = SimpleNetworkManager.create(FTBJarMod.MOD_ID);

	PacketID OPEN_SELECT_JAR_RECIPE_SCREEN = NET.registerS2C("open_select_jar_recipe_screen", OpenSelectJarRecipeScreenPacket::new);
	PacketID OPEN_JAR_SCREEN = NET.registerS2C("open_jar_screen", OpenJarScreenPacket::new);
	PacketID SELECT_JAR_RECIPE = NET.registerC2S("select_jar_recipe", SelectJarRecipePacket::new);
	PacketID SELECT_JAR_RECIPE_RESPONSE = NET.registerS2C("select_jar_recipe_response", SelectJarRecipeResponsePacket::new);
	PacketID START_JAR = NET.registerC2S("start_jar", StartJarPacket::new);

	static void init() {
	}
}
