package io.github.anxxitty.fdjei.jei;

import com.nhoryzon.mc.farmersdelight.recipe.CookingPotRecipe;
import com.nhoryzon.mc.farmersdelight.recipe.CuttingBoardRecipe;
import com.nhoryzon.mc.farmersdelight.FarmersDelightMod;
import io.github.anxxitty.fdjei.jei.resources.DecompositionDummy;
import mezz.jei.api.recipe.RecipeType;

public class FDRecipeTypes {

    public static final RecipeType<CookingPotRecipe> COOKING = RecipeType.create(FarmersDelightMod.MOD_ID, "cooking", CookingPotRecipe.class);
    public static final RecipeType<CuttingBoardRecipe> CUTTING = RecipeType.create(FarmersDelightMod.MOD_ID, "cutting", CuttingBoardRecipe.class);
    public static final RecipeType<DecompositionDummy> DECOMPOSITION = RecipeType.create(FarmersDelightMod.MOD_ID, "decomposition", DecompositionDummy.class);

}
