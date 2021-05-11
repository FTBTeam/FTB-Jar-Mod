package dev.ftb.mods.ftbjarmod.net;

import dev.ftb.mods.ftbjarmod.FTBJarMod;
import dev.ftb.mods.ftblibrary.net.snm.PacketID;
import dev.ftb.mods.ftblibrary.net.snm.SimpleNetworkManager;

/**
 * @author LatvianModder
 */
public interface FTBJarModNet {
	SimpleNetworkManager NET = SimpleNetworkManager.create(FTBJarMod.MOD_ID);

	PacketID SELECT_JAR_RECIPE = NET.registerC2S("select_jar_recipe", SelectJarRecipePacket::new);
	PacketID START_JAR = NET.registerC2S("start_jar", StartJarPacket::new);

	static void init() {
	}
}
