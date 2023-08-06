package io.github.anxxitty.fdjei.jei.category;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.DynamicRegistryManager;
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
import java.util.Arrays;

import static io.github.anxxitty.fdjei.jei.JEIPlugin.getTranslation;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CookingRecipeCategory implements IRecipeCategory<CookingPotRecipe>
{
    protected final IDrawable heatIndicator;
    protected final IDrawableAnimated arrow;
    private final Text title;
    private final IDrawable background;
    private final IDrawable icon;

    public CookingRecipeCategory(IGuiHelper helper) {
        title = getTranslation("rei.cooking");
        Identifier backgroundImage = new Identifier(FarmersDelightMod.MOD_ID, "textures/gui/cooking_pot.png");
        background = helper.createDrawable(backgroundImage, 29, 16, 117, 57);
        icon = helper.createDrawableItemStack(new ItemStack(ItemsRegistry.COOKING_POT.get()));
        heatIndicator = helper.createDrawable(backgroundImage, 176, 0, 17, 15);
        arrow = helper.drawableBuilder(backgroundImage, 176, 15, 24, 17)
                .buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
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
    public void setRecipe(IRecipeLayoutBuilder builder, CookingPotRecipe recipe, IFocusGroup focusGroup) {
        DefaultedList<Ingredient> recipeIngredients = recipe.getIngredients();
        ItemStack resultStack = recipe.getOutput(DynamicRegistryManager.EMPTY);
        ItemStack containerStack = recipe.getContainer();

        int borderSlotSize = 18;
        for (int row = 0; row < 2; ++row) {
            for (int column = 0; column < 3; ++column) {
                int inputIndex = row * 3 + column;
                if (inputIndex < recipeIngredients.size()) {
                    builder.addSlot(RecipeIngredientRole.INPUT, (column * borderSlotSize) + 1, (row * borderSlotSize) + 1)
                            .addItemStacks(Arrays.asList(recipeIngredients.get(inputIndex).getMatchingStacks()));
                }
            }
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 95, 10).addItemStack(resultStack);

        if (!containerStack.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 63, 39).addItemStack(containerStack);
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 95, 39).addItemStack(resultStack);
    }

    @Override
    public void draw(CookingPotRecipe recipe, IRecipeSlotsView recipeSlotsView, DrawContext matrixStack, double mouseX, double mouseY) {
        arrow.draw(matrixStack, 60, 9);
        heatIndicator.draw(matrixStack, 18, 39);
    }
}
