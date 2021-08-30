package dev.ftb.mods.ftbjarmod.temperature;

import dev.ftb.mods.ftbjarmod.FTBJarMod;
import dev.ftb.mods.ftbjarmod.recipe.FTBJarModRecipeSerializers;
import dev.ftb.mods.ftbjarmod.recipe.NoInventory;
import dev.ftb.mods.ftbjarmod.recipe.TemperatureSourceRecipe;
import dev.ftb.mods.ftblibrary.icon.Icon;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
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
	NONE("none", new DustParticleOptions(0.9F, 0.9F, 0.9F, 1F), 0.1F),
	LOW("low", ParticleTypes.FLAME, 0.1F),
	HIGH("high", ParticleTypes.SOUL_FIRE_FLAME, 0.1F),
	SUBZERO("subzero", ParticleTypes.END_ROD, 0.3F);

	public static final Temperature[] VALUES = values();
	public static final Map<String, Temperature> MAP = Arrays.stream(VALUES).collect(Collectors.toMap(t -> t.id, t -> t));

	private final String id;
	private final Component name;
	private final ResourceLocation texture;
	private final Icon icon;
	public final ParticleOptions particleOptions;
	public final float particleOffset;

	Temperature(String n, ParticleOptions p, float po) {
		id = n;
		name = new TranslatableComponent("ftbjarmod.temperature." + id);
		texture = new ResourceLocation(FTBJarMod.MOD_ID, "textures/gui/temperature/" + id + ".png");
		icon = Icon.getIcon(texture);
		particleOptions = p;
		particleOffset = po;
	}

	@Override
	public String getSerializedName() {
		return id;
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

	public static Temperature byName(String name) {
		return MAP.getOrDefault(name.toLowerCase(), NONE);
	}

	public static TemperaturePair fromLevel(Level level, BlockPos pos) {
		BlockState state = level.getBlockState(pos);

		for (TemperatureSourceRecipe recipe : level.getRecipeManager().getRecipesFor(FTBJarModRecipeSerializers.TEMPERATURE_SOURCE_TYPE, NoInventory.INSTANCE, level)) {
			if (recipe.test(state)) {
				return recipe.temperaturePair;
			}
		}

		return TemperaturePair.DEFAULT;
	}
}