package io.github.anxxitty.fdjei.jei.category;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.block.Block;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;
import net.minecraft.text.Text;
import net.minecraft.text.MutableText;
import net.minecraft.item.ItemStack;
import com.nhoryzon.mc.farmersdelight.FarmersDelightMod;
import com.nhoryzon.mc.farmersdelight.registry.BlocksRegistry;
import com.nhoryzon.mc.farmersdelight.registry.ItemsRegistry;
import com.nhoryzon.mc.farmersdelight.registry.TagsRegistry;

import io.github.anxxitty.fdjei.jei.FDRecipeTypes;
import io.github.anxxitty.fdjei.jei.resources.DecompositionDummy;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntryList;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.github.anxxitty.fdjei.jei.JEIPlugin.getTranslation;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class DecompositionRecipeCategory implements IRecipeCategory<DecompositionDummy>
{
    private static final int slotSize = 22;

    private final Text title;
    private final IDrawable background;
    private final IDrawable slotIcon;
    private final IDrawable icon;
    private final ItemStack organicCompost;
    private final ItemStack richSoil;

    public DecompositionRecipeCategory(IGuiHelper helper) {
        title = getTranslation("rei.decomposition");
        Identifier backgroundImage = new Identifier(FarmersDelightMod.MOD_ID, "textures/gui/rei/decomposition.png");
        background = helper.createDrawable(backgroundImage, 0, 0, 118, 80);
        organicCompost = new ItemStack(BlocksRegistry.ORGANIC_COMPOST.get());
        richSoil = new ItemStack(ItemsRegistry.RICH_SOIL.get());
        icon = helper.createDrawableItemStack(richSoil);
        slotIcon = helper.createDrawable(backgroundImage, 119, 0, slotSize, slotSize);
    }

    @Override
    public RecipeType<DecompositionDummy> getRecipeType() {
        return FDRecipeTypes.DECOMPOSITION;
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
    public void setRecipe(IRecipeLayoutBuilder recipeLayout, DecompositionDummy decompositionRecipe, IFocusGroup iIngredients) {
        List<RegistryEntryList.Named<Block>> acceleratorsEntryList = Registries.BLOCK.getEntryList(TagsRegistry.COMPOST_ACTIVATORS).stream().toList();
        List<ItemStack> accelerators = new ArrayList<>();

        for (RegistryEntryList.Named<Block> NamedBlock: acceleratorsEntryList) {
            accelerators.add(NamedBlock.get(0).value().asItem().getDefaultStack());
        }


        recipeLayout.addSlot(RecipeIngredientRole.INPUT, 9, 26).addItemStack(organicCompost);
        recipeLayout.addSlot(RecipeIngredientRole.OUTPUT, 93, 26).addItemStack(richSoil);
        recipeLayout.addSlot(RecipeIngredientRole.RENDER_ONLY, 64, 54).addItemStacks(accelerators);
    }

    @Override
    public void draw(DecompositionDummy recipe, IRecipeSlotsView recipeSlotsView, DrawContext ms, double mouseX, double mouseY) {
        this.slotIcon.draw(ms, 63, 53);
    }

    @Override
    public List<Text> getTooltipStrings(DecompositionDummy recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (inIconAt(40, 38, mouseX, mouseY)) {
            return ImmutableList.of(translateKey(".light"));
        }
        if (inIconAt(53, 38, mouseX, mouseY)) {
            return ImmutableList.of(translateKey(".fluid"));
        }
        if (inIconAt(67, 38, mouseX, mouseY)) {
            return ImmutableList.of(translateKey(".accelerators"));
        }
        return Collections.emptyList();
    }

    private static boolean inIconAt(int iconX, int iconY, double mouseX, double mouseY) {
        final int icon_size = 11;
        return iconX <= mouseX && mouseX < iconX + icon_size && iconY <= mouseY && mouseY < iconY + icon_size;
    }

    private static MutableText translateKey(@Nonnull String suffix) {
        return Text.translatable(FarmersDelightMod.MOD_ID + ".rei.decomposition" + suffix);
    }
}
