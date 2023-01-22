package io.github.anxxitty.fdjei.jei;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.recipe.RecipeManager;
import com.nhoryzon.mc.farmersdelight.recipe.CookingPotRecipe;
import com.nhoryzon.mc.farmersdelight.recipe.CuttingBoardRecipe;
import com.nhoryzon.mc.farmersdelight.registry.RecipeTypesRegistry;

import java.util.List;

public class FDRecipes
{
    private final RecipeManager recipeManager;

    public FDRecipes() {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        ClientWorld level = minecraft.world;

        if (level != null) {
            this.recipeManager = level.getRecipeManager();
        } else {
            throw new NullPointerException("minecraft world must not be null.");
        }
    }

    public List<CookingPotRecipe> getCookingPotRecipes() {
        return recipeManager.listAllOfType(RecipeTypesRegistry.COOKING_RECIPE_SERIALIZER.type());
    }

    public List<CuttingBoardRecipe> getCuttingBoardRecipes() {
        return recipeManager.listAllOfType(RecipeTypesRegistry.CUTTING_RECIPE_SERIALIZER.type());
    }
}
