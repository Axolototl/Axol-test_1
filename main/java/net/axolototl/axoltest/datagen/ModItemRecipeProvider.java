package net.axolototl.axoltest.datagen;

import net.axolototl.axoltest.AxolTest;
import net.axolototl.axoltest.block.ModBlocks;
import net.axolototl.axoltest.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModItemRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModItemRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        List<ItemLike> BLACK_OPAL_SMELTABLES = List.of(ModItems.RAW_BLACK_OPAL,
                ModBlocks.BLACK_OPAL_ORE, ModBlocks.BLACK_OPAL_DEEPSLATE_ORE, ModBlocks.BLACK_OPAL_NETHER_ORE, ModBlocks.BLACK_OPAL_END_ORE);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.BLACK_OPAL_BLOCK)
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', ModItems.BLACK_OPAL)
                .unlockedBy("has_black_opal", has(ModItems.BLACK_OPAL)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.BLACK_OPAL, 9)
                .requires(ModBlocks.BLACK_OPAL_BLOCK)
                .unlockedBy("has_black_opal_block", has(ModBlocks.BLACK_OPAL_BLOCK)).save(recipeOutput);

        oreSmelting(recipeOutput, BLACK_OPAL_SMELTABLES, RecipeCategory.MISC, ModItems.BLACK_OPAL.get(), 4.0f, 200, "black_opal");
        oreBlasting(recipeOutput, BLACK_OPAL_SMELTABLES, RecipeCategory.MISC, ModItems.BLACK_OPAL.get(), 4.0f, 100, "black_opal");

        stairBuilder(ModBlocks.BLACK_OPAL_STAIRS.get(), Ingredient.of(ModBlocks.BLACK_OPAL_BLOCK.get())).group("black_opal")
                .unlockedBy("has_black_opal_block", has(ModBlocks.BLACK_OPAL_BLOCK.get())).save(recipeOutput);
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.BLACK_OPAL_SLAB.get(), ModBlocks.BLACK_OPAL_BLOCK);

    }

    protected static void oreSmelting(RecipeOutput recipeOutput, java.util.List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float experience, int cookingTime, String group) {
        oreCooking(recipeOutput, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, ingredients, category, result,
                experience, cookingTime, group, "_from_smelting");
    }

    protected static void oreBlasting(RecipeOutput recipeOutput, List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float experience, int cookingTime, String group) {
        oreCooking(recipeOutput, RecipeSerializer.BLASTING_RECIPE, BlastingRecipe::new, ingredients, category, result,
                experience, cookingTime, group, "_from_blasting");
    }

    protected static <T extends AbstractCookingRecipe> void oreCooking(RecipeOutput recipeOutput, RecipeSerializer<T> serializer, AbstractCookingRecipe.Factory<T> recipeFactory, List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float experience, int cookingTime, String group, String suffix) {
        Iterator var10 = ingredients.iterator();

        while(var10.hasNext()) {
            ItemLike itemlike = (ItemLike)var10.next();
            SimpleCookingRecipeBuilder.generic(Ingredient.of(new ItemLike[]{itemlike}), category, result,
                    experience, cookingTime, serializer, recipeFactory).group(group).unlockedBy(getHasName(itemlike),
                    has(itemlike)).save(recipeOutput, AxolTest.MOD_ID + ":" + getItemName(result) + suffix + "_" + getItemName(itemlike));
        }

    }

}
