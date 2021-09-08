package dev.ftb.mods.ftbjarmod.net;

import dev.ftb.mods.ftbjarmod.FTBJarMod;
import me.shedaniel.architectury.networking.simple.MessageType;
import me.shedaniel.architectury.networking.simple.SimpleNetworkManager;

/**
 * @author LatvianModder
 */
public interface FTBJarModNet {
	SimpleNetworkManager NET = SimpleNetworkManager.create(FTBJarMod.MOD_ID);

	MessageType SELECT_JAR_RECIPE = NET.registerC2S("select_jar_recipe", SelectJarRecipePacket::new);
	MessageType SELECT_JAR_RECIPE_RESPONSE = NET.registerS2C("select_jar_recipe_response", SelectJarRecipeResponsePacket::new);
	MessageType DISPLAY_ERROR = NET.registerS2C("display_error", DisplayErrorPacket::new);

	static void init() {
	}
}
