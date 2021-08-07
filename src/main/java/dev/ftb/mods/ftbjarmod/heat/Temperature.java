package dev.ftb.mods.ftbjarmod.heat;

import dev.ftb.mods.ftbjarmod.FTBJarMod;
import dev.ftb.mods.ftbjarmod.block.CreativeTemperatureSourceBlock;
import dev.ftb.mods.ftbjarmod.recipe.FTBJarModRecipeSerializers;
import dev.ftb.mods.ftbjarmod.recipe.NoInventory;
import dev.ftb.mods.ftbjarmod.recipe.TemperatureSourceRecipe;
import dev.ftb.mods.ftblibrary.icon.Icon;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author LatvianModder
 */
public enum Temperature implements StringRepresentable {
	NONE("none", 0),
	LOW("low", 500),
	HIGH("high", 1000),
	SUBZERO("subzero", -10);

	public static final Temperature[] VALUES = values();
	public static final Map<String, Temperature> MAP = Arrays.stream(VALUES).collect(Collectors.toMap(t -> t.id, t -> t));

	private final String id;
	private final int value;
	private final Component name;
	private final ResourceLocation texture;
	private final Icon icon;

	Temperature(String n, int v) {
		id = n;
		value = v;
		name = new TranslatableComponent("ftbjarmod.temperature." + id);
		texture = new ResourceLocation(FTBJarMod.MOD_ID, "textures/gui/temperature/" + id + ".png");
		icon = Icon.getIcon(texture);
	}

	@Override
	public String getSerializedName() {
		return id;
	}

	public int getValue() {
		return value;
	}

	public Component getName() {
		return name;
	}

	public ResourceLocation getTexture() {
		return texture;
	}

	public Icon getIcon() {
		return icon;
	}

	public boolean isNone() {
		return getValue() == 0;
	}

	public boolean isCold() {
		return getValue() < 0;
	}

	public boolean isHot() {
		return getValue() > 0;
	}

	public static Temperature byName(String name) {
		return MAP.getOrDefault(name.toLowerCase(), NONE);
	}

	public static Temperature fromWorld(Level level, BlockPos pos) {
		BlockState state = level.getBlockState(pos);

		if (state.getBlock() instanceof CreativeTemperatureSourceBlock) {
			return ((CreativeTemperatureSourceBlock) state.getBlock()).temperature;
		}

		for (TemperatureSourceRecipe recipe : level.getRecipeManager().getRecipesFor(FTBJarModRecipeSerializers.TEMPERATURE_SOURCE_TYPE, NoInventory.INSTANCE, level)) {
			if (recipe.test(state)) {
				return recipe.temperature;
			}
		}

		return Temperature.NONE;
	}
}