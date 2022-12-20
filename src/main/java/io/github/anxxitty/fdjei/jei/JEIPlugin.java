package io.github.anxxitty.fdjei.jei;

import com.google.common.collect.ImmutableList;
import com.nhoryzon.mc.farmersdelight.FarmersDelightMod;
import com.nhoryzon.mc.farmersdelight.client.screen.CookingPotScreen;
import com.nhoryzon.mc.farmersdelight.entity.block.screen.CookingPotScreenHandler;
import com.nhoryzon.mc.farmersdelight.registry.BlocksRegistry;
import io.github.anxxitty.fdjei.FDJEI;
import io.github.anxxitty.fdjei.jei.category.CookingRecipeCategory;
import io.github.anxxitty.fdjei.jei.category.CuttingRecipeCategory;
import io.github.anxxitty.fdjei.jei.category.DecompositionRecipeCategory;
import io.github.anxxitty.fdjei.jei.resources.DecompositionDummy;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.*;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.recipe.Recipe;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;
import com.nhoryzon.mc.farmersdelight.registry.RecipeTypesRegistry;
import com.nhoryzon.mc.farmersdelight.registry.ItemsRegistry;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.stream.Collectors;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@JeiPlugin
public class JEIPlugin implements IModPlugin {

    private static final Identifier ID = new Identifier(FDJEI.MODID, "jei_mod_plugin");
    private static final MinecraftClient MC = MinecraftClient.getInstance();


    private static List<Recipe<?>> findRecipesByType(RecipeType<?> type) {
        return MC.world
                .getRecipeManager()
                .values()
                .stream()
                .filter(r -> r.getType() == type)
                .collect(Collectors.toList());
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new CookingRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new CuttingRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new DecompositionRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(findRecipesByType(RecipeTypesRegistry.COOKING_RECIPE_SERIALIZER.type()), CookingRecipeCategory.UID);
        registration.addRecipes(findRecipesByType(RecipeTypesRegistry.CUTTING_RECIPE_SERIALIZER.type()), CuttingRecipeCategory.UID);
        registration.addRecipes(FDRecipeTypes.DECOMPOSITION, ImmutableList.of(new DecompositionDummy()));
        registration.addIngredientInfo(new ItemStack(ItemsRegistry.STRAW.get()), VanillaTypes.ITEM_STACK, getTranslation("rei.info.straw"));
        registration.addIngredientInfo(new ItemStack(ItemsRegistry.HAM.get()), VanillaTypes.ITEM_STACK, getTranslation("rei.info.ham"));
        registration.addIngredientInfo(new ItemStack(ItemsRegistry.SMOKED_HAM.get()), VanillaTypes.ITEM_STACK, getTranslation("rei.info.ham"));
        registration.addIngredientInfo(new ItemStack(ItemsRegistry.FLINT_KNIFE.get()), VanillaTypes.ITEM_STACK, getTranslation("rei.info.knife"));
        registration.addIngredientInfo(new ItemStack(ItemsRegistry.IRON_KNIFE.get()), VanillaTypes.ITEM_STACK, getTranslation("rei.info.knife"));
        registration.addIngredientInfo(new ItemStack(ItemsRegistry.DIAMOND_KNIFE.get()), VanillaTypes.ITEM_STACK, getTranslation("rei.info.knife"));
        registration.addIngredientInfo(new ItemStack(ItemsRegistry.NETHERITE_KNIFE.get()), VanillaTypes.ITEM_STACK, getTranslation("rei.info.knife"));
        registration.addIngredientInfo(new ItemStack(ItemsRegistry.GOLDEN_KNIFE.get()), VanillaTypes.ITEM_STACK, getTranslation("rei.info.knife"));

    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ItemsRegistry.COOKING_POT.get()), FDRecipeTypes.COOKING);
        registration.addRecipeCatalyst(new ItemStack(ItemsRegistry.CUTTING_BOARD.get()), FDRecipeTypes.CUTTING);
        registration.addRecipeCatalyst(new ItemStack(ItemsRegistry.STOVE.get()), RecipeTypes.CAMPFIRE_COOKING);
        registration.addRecipeCatalyst(new ItemStack(ItemsRegistry.SKILLET.get()), RecipeTypes.CAMPFIRE_COOKING);
        registration.addRecipeCatalyst(new ItemStack(BlocksRegistry.ORGANIC_COMPOST.get()), FDRecipeTypes.DECOMPOSITION);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(CookingPotScreen.class, 89, 25, 24, 17, FDRecipeTypes.COOKING);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(CookingPotScreenHandler.class, FDRecipeTypes.COOKING, 0, 6, 9, 36);
    }

    @Override
    public Identifier getPluginUid() {
        return ID;
    }

    public static MutableText getTranslation(String key, Object... args) {
        return new TranslatableText(FarmersDelightMod.MOD_ID + "." + key, args);
    }
}
