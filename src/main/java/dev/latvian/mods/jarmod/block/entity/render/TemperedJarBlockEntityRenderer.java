package dev.latvian.mods.jarmod.block.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.latvian.mods.jarmod.block.entity.TemperedJarBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;

/**
 * @author LatvianModder
 */
public class TemperedJarBlockEntityRenderer extends TileEntityRenderer<TemperedJarBlockEntity>
{
	public TemperedJarBlockEntityRenderer(TileEntityRendererDispatcher dispatcher)
	{
		super(dispatcher);
	}

	@Override
	@SuppressWarnings("deprecation")
	public void render(TemperedJarBlockEntity entity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay)
	{
		Minecraft mc = Minecraft.getInstance();
		Matrix4f m = matrixStack.getLast().getMatrix();

		if (entity.itemHandler.getSlots() <= 0)
		{
			return;
		}

		ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

		for (int i = 0; i < entity.itemHandler.items.length; i++)
		{
			ItemStack stack = entity.itemHandler.items[i];

			if (stack.isEmpty())
			{
				continue;
			}

			matrixStack.push();
			IBakedModel bakedModel = itemRenderer.getItemModelWithOverrides(stack, entity.getWorld(), null);
			double f2 = bakedModel.getItemCameraTransforms().getTransform(ItemCameraTransforms.TransformType.GROUND).scale.getY();
			double f3 = (entity.tick + partialTicks) / 20D + 7.7 * i;
			matrixStack.translate(0.5D, 0.1D + 0.25D * f2, 0.5D);
			matrixStack.rotate(Vector3f.YP.rotation((float) (f3 * 0.3D)));
			itemRenderer.renderItem(stack, ItemCameraTransforms.TransformType.GROUND, false, matrixStack, buffer, combinedLight, OverlayTexture.NO_OVERLAY, bakedModel);
			matrixStack.pop();
		}
	}
}