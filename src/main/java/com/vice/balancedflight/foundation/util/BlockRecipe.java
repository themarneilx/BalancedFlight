package com.vice.balancedflight.foundation.util;


import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.level.ItemLike;

public class BlockRecipe
{
    RecipeOutput consumer;
    ItemLike BlockItem;

    public BlockRecipe(RecipeOutput consumer, ItemLike BlockItem)
    {
        this.consumer = consumer;
        this.BlockItem = BlockItem;
    }

    public void MadeFrom(ItemLike IngotItem)
    {
        // block recipe
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockItem)
                .pattern("xxx")
                .pattern("xxx")
                .pattern("xxx")
                .define('x', IngotItem)
                .unlockedBy(IngotItem.toString(), InventoryChangeTrigger.TriggerInstance.hasItems(IngotItem))
                .save(consumer);

        // ingot recipe
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, IngotItem, 9)
                .requires(BlockItem, 1)
                .unlockedBy(IngotItem.toString(), InventoryChangeTrigger.TriggerInstance.hasItems(BlockItem))
                .save(consumer);
    }
}
