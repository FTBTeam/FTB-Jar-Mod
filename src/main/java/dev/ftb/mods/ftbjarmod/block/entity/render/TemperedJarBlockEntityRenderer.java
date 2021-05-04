package dev.ftb.mods.ftbjarmod.block.entity.render;


import com.mojang.blaze3d.vertex.PoseStack;
import dev.ftb.mods.ftbjarmod.block.entity.TemperedJarBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;

/**
 * @author LatvianModder
 */
public class TemperedJarBlockEntityRenderer extends BlockEntityRenderer<TemperedJarBlockEntity> {
	public TemperedJarBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	@SuppressWarnings("deprecation")
	public void render(TemperedJarBlockEntity entity, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
		/*
		if (entity.itemHandler.getSlots() <= 0) {
			return;
		}

		ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

		for (int i = 0; i < entity.itemHandler.items.length; i++) {
			ItemStack stack = entity.itemHandler.items[i];

			if (stack.isEmpty()) {
				continue;
			}

			matrixStack.pushPose();
			BakedModel bakedModel = itemRenderer.getModel(stack, entity.getLevel(), null);
			double f2 = bakedModel.getTransforms().getTransform(ItemTransforms.TransformType.GROUND).scale.y();
			double f3 = (entity.tick + partialTicks) / 20D + 7.7 * i;
			matrixStack.translate(0.5D, 0.1D + 0.25D * f2, 0.5D);
			matrixStack.mulPose(Vector3f.YP.rotation((float) (f3 * 0.3D)));
			itemRenderer.render(stack, ItemTransforms.TransformType.GROUND, false, matrixStack, buffer, combinedLight, OverlayTexture.NO_OVERLAY, bakedModel);
			matrixStack.popPose();
		}
		*/
	}
}
