package dev.ftb.mods.ftbjarmod.item;

import dev.ftb.mods.ftbjarmod.FTBJarMod;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class FluidItem extends Item {
	public static final IForgeRegistry<Fluid> FLUID_REGISTRY = ForgeRegistries.FLUIDS; // Registries.get(FTBJarMod.MOD_ID).get(net.minecraft.core.Registry.FLUID_REGISTRY);

	public FluidItem() {
		super(new Item.Properties().stacksTo(1).tab(FTBJarMod.group));
	}

	public static FluidStack getFluidStack(ItemStack item) {
		return item.hasTag() && item.getTag().contains("Fluid") ? FluidStack.loadFluidStackFromNBT(item.getTag().getCompound("Fluid")) : FluidStack.EMPTY;
	}

	public static String getFluidStackHash(ItemStack item) {
		if (item.hasTag() && item.getTag().contains("Fluid")) {
			CompoundTag tag = item.getTag().getCompound("Fluid");
			return String.format("%s:%08X", tag.getString("FluidName"), Objects.hashCode(tag.get("Tag")));
		}

		return "";
	}

	public static void setFluid(ItemStack item, FluidStack fluidStack) {
		if (!fluidStack.isEmpty()) {
			item.addTagElement("Fluid", fluidStack.writeToNBT(new CompoundTag()));
		} else if (item.hasTag()) {
			item.removeTagKey("Fluid");
		}
	}

	public static ItemStack of(FluidStack fluidStack) {
		ItemStack stack = new ItemStack(FTBJarModItems.FLUID.get());
		setFluid(stack, fluidStack);
		return stack;
	}

	@Override
	public ItemStack getDefaultInstance() {
		return of(new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		FluidStack fluidStack = getFluidStack(stack);

		if (!fluidStack.isEmpty()) {
			tooltip.add(new TextComponent("< ").append(new TranslatableComponent("block.ftbjarmod.jar.mb", fluidStack.getAmount(), fluidStack.getDisplayName())).append(" >").withStyle(ChatFormatting.GRAY));
		}

		tooltip.add(new TranslatableComponent("item.ftbjarmod.fluid.use").withStyle(ChatFormatting.DARK_GRAY));
	}

	@Override
	public void fillItemCategory(CreativeModeTab tag, NonNullList<ItemStack> list) {
		if (allowdedIn(tag)) {
			for (Fluid fluid : FLUID_REGISTRY) {
				if (fluid != Fluids.EMPTY && fluid.isSource(fluid.defaultFluidState())) {
					list.add(of(new FluidStack(fluid, FluidAttributes.BUCKET_VOLUME)));
				}
			}
		}
	}
}
