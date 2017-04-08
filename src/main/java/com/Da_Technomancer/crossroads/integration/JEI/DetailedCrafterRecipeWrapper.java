package com.Da_Technomancer.crossroads.integration.JEI;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class DetailedCrafterRecipeWrapper implements IRecipeWrapper{

	private IRecipe recipe;
	private int type;

	protected DetailedCrafterRecipeWrapper(@Nonnull DetailedCrafterRecipe recipe){
		this.recipe = recipe.getRecipe();
		type = recipe.getType();
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY){
		if(recipe instanceof ShapelessOreRecipe){
			minecraft.fontRendererObj.drawString("Shapeless", 60, 5, 4210752);
		}
	}

	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY){
		return ImmutableList.of();
	}

	@Override
	public boolean handleClick(Minecraft minecraft, int mouseX, int mouseY, int mouseButton){
		return false;
	}

	protected int getType(){
		return type;
	}
	
	@Override
	public void getIngredients(IIngredients ingredients){
		if(recipe instanceof ShapedOreRecipe){
			Object[] input = ((ShapedOreRecipe) recipe).getInput();
			int width = ((ShapedOreRecipe) recipe).getWidth();
			int height = ((ShapedOreRecipe) recipe).getHeight();
			ingredients.setInputLists(ItemStack.class, ImmutableList.of(format(input[0]), format(width < 2 ? ImmutableList.of() : input[1]), format(width < 3 ? ImmutableList.of() : input[2]), format(height < 2 ? ImmutableList.of() : input[width]), format(height < 2 || width < 2 ? ImmutableList.of() : input[width + 1]), format(height < 2 || width < 3 ? ImmutableList.of() : input[width + 2]), format(height < 3 ? ImmutableList.of() : input[width * 2]), format(height < 3 || width < 2 ? ImmutableList.of() : input[(width * 2) + 1]), format(height < 3 || width < 3 ? ImmutableList.of() : input[(width * 2) + 2])));
		}else if(recipe instanceof ShapelessOreRecipe){
			NonNullList<Object> input = ((ShapelessOreRecipe) recipe).getInput();
			ingredients.setInputLists(ItemStack.class, ImmutableList.of(format(input.get(0)), format(input.size() < 2 ? ImmutableList.of() : input.get(1)), format(input.size() < 3 ? ImmutableList.of() : input.get(2)), format(input.size() < 4 ? ImmutableList.of() : input.get(3)), format(input.size() < 5 ? ImmutableList.of() : input.get(4)), format(input.size() < 6 ? ImmutableList.of() : input.get(5)), format(input.size() < 7 ? ImmutableList.of() : input.get(6)), format(input.size() < 8 ? ImmutableList.of() : input.get(7)), format(input.size() < 9 ? ImmutableList.of() : input.get(8))));
		}else{
			throw new IllegalArgumentException("INVALID RECIPE TYPE passed to JEI for Detailed Crafter!");
		}
		ingredients.setOutputs(ItemStack.class, ImmutableList.of(recipe.getRecipeOutput()));
	}

	@SuppressWarnings("unchecked")
	private static List<ItemStack> format(Object ingr){
		if(ingr instanceof NonNullList<?>){
			return (NonNullList<ItemStack>) ingr;
		}
		if(ingr instanceof ItemStack){
			return ImmutableList.of((ItemStack) ingr);
		}
		return ImmutableList.of();
	}
}