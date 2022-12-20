package io.github.anxxitty.fdjei.jei.category;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;
import net.minecraft.text.Text;
import net.minecraft.item.ItemStack;
import com.nhoryzon.mc.farmersdelight.FarmersDelightMod;
import com.nhoryzon.mc.farmersdelight.recipe.CuttingBoardRecipe;
import com.nhoryzon.mc.farmersdelight.recipe.ingredient.ChanceResult;
import com.nhoryzon.mc.farmersdelight.registry.BlocksRegistry;
import com.nhoryzon.mc.farmersdelight.registry.ItemsRegistry;
import net.minecraft.util.collection.DefaultedList;
import io.github.anxxitty.fdjei.jei.FDRecipeTypes;
import io.github.anxxitty.fdjei.jei.resources.CuttingBoardDrawable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;

import static io.github.anxxitty.fdjei.jei.JEIPlugin.getTranslation;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CuttingRecipeCategory implements IRecipeCategory<CuttingBoardRecipe>
{
    public static final Identifier UID = new Identifier(FarmersDelightMod.MOD_ID, "cutting");
    public static final int OUTPUT_GRID_X = 76;
    public static final int OUTPUT_GRID_Y = 10;
    public static final int SLOT_SPRITE_SIZE = 18;
    private final IDrawable slot;
    private final IDrawable slotChance;
    private final Text title;
    private final IDrawable background;
    private final IDrawable icon;
    private final CuttingBoardDrawable cuttingBoard;

    public CuttingRecipeCategory(IGuiHelper helper) {
        title = getTranslation("rei.cutting");
        Identifier backgroundImage = new Identifier(FarmersDelightMod.MOD_ID, "textures/gui/rei/cutting_board.png");
        slot = helper.createDrawable(backgroundImage, 0, 58, 18, 18);
        slotChance = helper.createDrawable(backgroundImage, 18, 58, 18, 18);
        background = helper.createDrawable(backgroundImage, 0, 0, 117, 57);
        icon = helper.createDrawableIngredient(new ItemStack(ItemsRegistry.CUTTING_BOARD.get()));
        cuttingBoard = new CuttingBoardDrawable(() -> new ItemStack(BlocksRegistry.CUTTING_BOARD.get()));
    }

    @Override
    public Identifier getUid() {
        return this.getRecipeType().getUid();
    }

    @Override
    public Class<? extends CuttingBoardRecipe> getRecipeClass() {
        return this.getRecipeType().getRecipeClass();
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
    public void setIngredients(CuttingBoardRecipe cuttingBoardRecipe, IIngredients ingredients) {
        ingredients.setInputIngredients(cuttingBoardRecipe.getIngredientsAndTool());
        ingredients.setOutputs(VanillaTypes.ITEM_STACK, cuttingBoardRecipe.getResultList());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, CuttingBoardRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
        DefaultedList<ChanceResult> recipeOutputs = recipe.getRollableResults();

        // Draw required tool
        itemStacks.init(0, true, 15, 7);
        itemStacks.set(0, Arrays.asList(recipe.getTool().getMatchingStacks()));

        // Draw input
        itemStacks.init(1, true, 15, 26);
        itemStacks.set(1, Arrays.asList(recipe.getIngredients().get(0).getMatchingStacks()));

        // Draw outputs
        int size = recipeOutputs.size();
        int centerX = size > 1 ? 0 : 9;
        int centerY = size > 2 ? 0 : 9;

        for (int i = 0; i < size; i++) {
            int xOffset = centerX + (i % 2 == 0 ? 0 : 19);
            int yOffset = centerY + ((i / 2) * 19);

            itemStacks.init(i + 2, false, OUTPUT_GRID_X + xOffset, OUTPUT_GRID_Y + yOffset);
            itemStacks.set(i + 2, recipeOutputs.get(i).stack());
        }

        itemStacks.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
            if (input || slotIndex < 2) {
                return;
            }
            ChanceResult output = recipeOutputs.get(slotIndex - 2);
            float chance = output.chance();
            if (chance != 1)
                tooltip.add(1, getTranslation("rei.chance", chance < 0.01 ? "<1" : (int) (chance * 100))
                        .formatted(Formatting.GOLD));
        });
    }

    @Override
    public void draw(CuttingBoardRecipe recipe, IRecipeSlotsView recipeSlotsView, MatrixStack matrixStack, double mouseX, double mouseY) {
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
