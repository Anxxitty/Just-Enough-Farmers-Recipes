package io.github.anxxitty.fdjei.jei.category;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;
import net.minecraft.text.Text;
import net.minecraft.item.ItemStack;
import com.nhoryzon.mc.farmersdelight.FarmersDelightMod;
import com.nhoryzon.mc.farmersdelight.recipe.CuttingBoardRecipe;
import com.nhoryzon.mc.farmersdelight.recipe.ingredient.ChanceResult;
import com.nhoryzon.mc.farmersdelight.registry.ItemsRegistry;
import net.minecraft.util.collection.DefaultedList;
import io.github.anxxitty.fdjei.jei.FDRecipeTypes;

import javax.annotation.ParametersAreNonnullByDefault;

import static io.github.anxxitty.fdjei.jei.JEIPlugin.getTranslation;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CuttingRecipeCategory implements IRecipeCategory<CuttingBoardRecipe>
{
    public static final int OUTPUT_GRID_X = 76;
    public static final int OUTPUT_GRID_Y = 10;
    private final IDrawable slot;
    private final IDrawable slotChance;
    private final Text title;
    private final IDrawable background;
    private final IDrawable icon;

    public CuttingRecipeCategory(IGuiHelper helper) {
        title = getTranslation("rei.cutting");
        Identifier backgroundImage = new Identifier(FarmersDelightMod.MOD_ID, "textures/gui/rei/cutting_board.png");
        slot = helper.createDrawable(backgroundImage, 0, 58, 18, 18);
        slotChance = helper.createDrawable(backgroundImage, 18, 58, 18, 18);
        background = helper.createDrawable(backgroundImage, 0, 0, 117, 57);
        icon = helper.createDrawableItemStack(new ItemStack(ItemsRegistry.CUTTING_BOARD.get()));
    }

    @Override
    public RecipeType<CuttingBoardRecipe> getRecipeType() {
        return FDRecipeTypes.CUTTING;
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
    public void setRecipe(IRecipeLayoutBuilder builder, CuttingBoardRecipe recipe, IFocusGroup focusGroup) {
        builder.addSlot(RecipeIngredientRole.INPUT, 16, 8).addIngredients(recipe.getTool());
        builder.addSlot(RecipeIngredientRole.INPUT, 16, 27).addIngredients(recipe.getIngredients().get(0));

        DefaultedList<ChanceResult> recipeOutputs = recipe.getRollableResults();

        int size = recipeOutputs.size();
        int centerX = size > 1 ? 1 : 10;
        int centerY = size > 2 ? 1 : 10;

        for (int i = 0; i < size; i++) {
            int xOffset = centerX + (i % 2 == 0 ? 0 : 19);
            int yOffset = centerY + ((i / 2) * 19);

            int index = i;
            builder.addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_GRID_X + xOffset, OUTPUT_GRID_Y + yOffset)
                    .addItemStack(recipeOutputs.get(i).stack())
                    .addTooltipCallback((slotView, tooltip) -> {
                        ChanceResult output = recipeOutputs.get(index);
                        float chance = output.chance();
                        if (chance != 1)
                            tooltip.add(1, getTranslation("jei.chance", chance < 0.01 ? "<1" : (int) (chance * 100))
                                    .formatted(Formatting.GOLD));
                    });
        }
    }

    @Override
    public void draw(CuttingBoardRecipe recipe, IRecipeSlotsView recipeSlotsView, DrawContext matrixStack, double mouseX, double mouseY) {
        //cuttingBoard.draw(matrixStack, 15, 19);
        DefaultedList<ChanceResult> recipeOutputs = recipe.getRollableResults();

        int size = recipe.getResultList().size();
        int centerX = size > 1 ? 0 : 9;
        int centerY = size > 2 ? 0 : 9;

        for (int i = 0; i < size; i++) {
            int xOffset = centerX + (i % 2 == 0 ? 0 : 19);
            int yOffset = centerY + ((i / 2) * 19);

            if (recipeOutputs.get(i).chance() != 1) {
                slotChance.draw(matrixStack, OUTPUT_GRID_X + xOffset, OUTPUT_GRID_Y + yOffset);
            } else {
                slot.draw(matrixStack, OUTPUT_GRID_X + xOffset, OUTPUT_GRID_Y + yOffset);
            }
        }
    }
}
