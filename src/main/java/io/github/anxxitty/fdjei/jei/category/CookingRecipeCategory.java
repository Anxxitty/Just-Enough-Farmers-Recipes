package io.github.anxxitty.fdjei.jei.category;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;
import net.minecraft.text.Text;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import com.nhoryzon.mc.farmersdelight.FarmersDelightMod;
import com.nhoryzon.mc.farmersdelight.recipe.CookingPotRecipe;
import com.nhoryzon.mc.farmersdelight.registry.ItemsRegistry;

import io.github.anxxitty.fdjei.jei.FDRecipeTypes;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.github.anxxitty.fdjei.jei.JEIPlugin.getTranslation;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CookingRecipeCategory implements IRecipeCategory<CookingPotRecipe>
{
    public static final Identifier UID = new Identifier(FarmersDelightMod.MOD_ID, "cooking");
    protected final IDrawable heatIndicator;
    protected final IDrawableAnimated arrow;
    private final Text title;
    private final IDrawable background;
    private final IDrawable icon;

    public CookingRecipeCategory(IGuiHelper helper) {
        title = getTranslation("rei.cooking");
        Identifier backgroundImage = new Identifier(FarmersDelightMod.MOD_ID, "textures/gui/cooking_pot.png");
        background = helper.createDrawable(backgroundImage, 29, 16, 117, 57);
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ItemsRegistry.COOKING_POT.get()));
        heatIndicator = helper.createDrawable(backgroundImage, 176, 0, 17, 15);
        arrow = helper.drawableBuilder(backgroundImage, 176, 15, 24, 17)
                .buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public Identifier getUid() {
        return this.getRecipeType().getUid();
    }

    @Override
    public Class<? extends CookingPotRecipe> getRecipeClass() {
        return this.getRecipeType().getRecipeClass();
    }

    @Override
    public RecipeType<CookingPotRecipe> getRecipeType() {
        return FDRecipeTypes.COOKING;
    }

    @Override
    public Text getTitle() {
        return this.title;
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setIngredients(CookingPotRecipe cookingPotRecipe, IIngredients ingredients) {
        List<Ingredient> inputAndContainer = new ArrayList<>(cookingPotRecipe.getIngredients());
        inputAndContainer.add(Ingredient.ofStacks(cookingPotRecipe.getContainer()));

        ingredients.setInputIngredients(inputAndContainer);
        ingredients.setOutput(VanillaTypes.ITEM_STACK, cookingPotRecipe.getOutput());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, CookingPotRecipe recipe, IIngredients ingredients) {
        final int MEAL_DISPLAY = 6;
        final int CONTAINER_INPUT = 7;
        final int OUTPUT = 8;
        IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
        DefaultedList<Ingredient> recipeIngredients = recipe.getIngredients();

        int borderSlotSize = 18;
        for (int row = 0; row < 2; ++row) {
            for (int column = 0; column < 3; ++column) {
                int inputIndex = row * 3 + column;
                if (inputIndex < recipeIngredients.size()) {
                    itemStacks.init(inputIndex, true, column * borderSlotSize, row * borderSlotSize);
                    itemStacks.set(inputIndex, Arrays.asList(recipeIngredients.get(inputIndex).getMatchingStacks()));
                }
            }
        }

        itemStacks.init(MEAL_DISPLAY, false, 94, 9);
        itemStacks.set(MEAL_DISPLAY, recipe.getOutput());

        if (!recipe.getContainer().isEmpty()) {
            itemStacks.init(CONTAINER_INPUT, false, 62, 38);
            itemStacks.set(CONTAINER_INPUT, recipe.getContainer());
        }

        itemStacks.init(OUTPUT, false, 94, 38);
        itemStacks.set(OUTPUT, recipe.getOutput());
    }

    @Override
    public void draw(CookingPotRecipe recipe, IRecipeSlotsView recipeSlotsView, MatrixStack matrixStack, double mouseX, double mouseY) {
        arrow.draw(matrixStack, 60, 9);
        heatIndicator.draw(matrixStack, 18, 39);
    }
}
