package dev.latvian.mods.jarmod.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.latvian.mods.jarmod.JarMod;
import dev.latvian.mods.jarmod.block.entity.TemperedJarBlockEntity;
import dev.latvian.mods.jarmod.net.JarModNet;
import dev.latvian.mods.jarmod.net.SelectTemperedJarRecipePacket;
import dev.latvian.mods.jarmod.recipe.JarModRecipeSerializers;
import dev.latvian.mods.jarmod.recipe.JarRecipe;
import dev.latvian.mods.jarmod.recipe.NoInventory;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class TemperedJarScreen extends Screen
{
	private static final ResourceLocation TEXTURE = new ResourceLocation(JarMod.MOD_ID + ":textures/gui/tempered_jar.png");

	public final TemperedJarBlockEntity jar;
	public final List<JarRecipe> availableRecipes;
	public int xSize, ySize;

	public TemperedJarScreen(TemperedJarBlockEntity j)
	{
		super(new TranslationTextComponent("block." + JarMod.MOD_ID + ".tempered_jar"));
		jar = j;
		availableRecipes = new ArrayList<>();
		xSize = 160;
		ySize = 212;
	}

	@Override
	protected void func_231160_c_()
	{
		availableRecipes.clear();

		for (JarRecipe recipe : field_230706_i_.world.getRecipeManager().getRecipes(JarModRecipeSerializers.JAR_TYPE, NoInventory.INSTANCE, field_230706_i_.world))
		{
			if (recipe.isAvailableFor(field_230706_i_.player))
			{
				availableRecipes.add(recipe);
			}
		}

		int x = (field_230708_k_ - xSize) / 2;
		int y = (field_230709_l_ - ySize) / 2;

		for (int i = 0; i < availableRecipes.size(); i++)
		{
			JarRecipe recipe = availableRecipes.get(i);
			String s = recipe.getId().toString();

			if (s.startsWith("jarmod:"))
			{
				s = s.substring(7);
			}

			if (s.startsWith("jar/"))
			{
				s = s.substring(4);
			}

			ExtendedButton button = new ExtendedButton(x + 8, y + 21 + i * 12, 128, 12, new StringTextComponent(s), p_onPress_1_ -> {
				System.out.println(recipe);
				JarModNet.MAIN.sendToServer(new SelectTemperedJarRecipePacket(jar.getPos(), recipe.getId()));
				field_230706_i_.displayGuiScreen(null);
			});

			if (recipe.getId().equals(jar.recipe))
			{
				setFocusedDefault(button);
			}

			func_230480_a_(button);
		}

		super.func_231160_c_();
	}

	@Override
	public void func_230430_a_(MatrixStack matrixStack, int mouseX, int mouseY, float pt)
	{
		int x = (field_230708_k_ - xSize) / 2;
		int y = (field_230709_l_ - ySize) / 2;

		func_230446_a_(matrixStack); //render background
		field_230706_i_.getTextureManager().bindTexture(TEXTURE);
		GuiUtils.drawTexturedModalRect(x, y, 0, 0, xSize, ySize, 0F);
		super.func_230430_a_(matrixStack, mouseX, mouseY, pt);
	}

	@Override
	public boolean func_231177_au__() // pause
	{
		return false;
	}

	@Override
	public boolean func_231046_a_(int key, int scanCode, int state)
	{
		if (super.func_231046_a_(key, scanCode, state))
		{
			return true;
		}

		InputMappings.Input mouseKey = InputMappings.getInputByCode(key, scanCode);

		if (key == 256 || field_230706_i_.gameSettings.keyBindInventory.isActiveAndMatches(mouseKey))
		{
			field_230706_i_.player.closeScreen();
			return true;
		}

		return false;
	}
}
