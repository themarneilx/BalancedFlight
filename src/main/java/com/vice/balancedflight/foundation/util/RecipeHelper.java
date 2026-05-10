package com.vice.balancedflight.foundation.util;
 

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.level.ItemLike;

public class RecipeHelper
{
    public static BlockRecipe Block(RecipeOutput consumer, ItemLike BlockItem)
    {
        return new BlockRecipe(consumer, BlockItem);
    }

    public static CustomRecipeBuilder Shaped(RecipeOutput consumer, ItemLike BlockItem)
    {
        return new CustomRecipeBuilder(consumer, BlockItem);
    }
}

