package io.github.anxxitty.fdjei.jei.resources;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.platform.GlStateManager;
import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.client.MinecraftClient;

import java.util.function.Supplier;

public class CuttingBoardDrawable implements IDrawable {

    private final Supplier<ItemStack> supplier;
    private ItemStack stack;

    public CuttingBoardDrawable(Supplier<ItemStack> supplier) {
        this.supplier = supplier;
    }

    @Override
    public int getWidth() {
        return 48;
    }

    @Override
    public int getHeight() {
        return 48;
    }

    @Override
    public void draw(MatrixStack matrixStack, int xOffset, int yOffset) {
        if (stack == null) {
            stack = supplier.get();
        }

        MinecraftClient minecraft = MinecraftClient.getInstance();
        ItemRenderer itemRenderer = minecraft.getItemRenderer();
        itemRenderer.zOffset += 50.0F;
        BakedModel bakedmodel = itemRenderer.getModel(stack, null, null, 0);
        TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
        renderItemIntoGUIScalable(stack, 48.0F, 48.0F, bakedmodel, itemRenderer, textureManager);
        itemRenderer.zOffset -= 50.0F;

    }

    public static void renderItemIntoGUIScalable(ItemStack stack, float width, float height, BakedModel bakedmodel, ItemRenderer renderer, TextureManager textureManager) {
        textureManager.getTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).setFilter(false, false);
        RenderSystem.setShaderTexture(0, SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);

        MatrixStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.push();
        modelViewStack.translate((float) 0, (float) 0, 100.0F + renderer.zOffset);
        modelViewStack.translate(8.0F, 8.0F, 0.0F);
        modelViewStack.scale(1.0F, -1.0F, 1.0F);
        modelViewStack.scale(width, height, 48.0F);
        RenderSystem.applyModelViewMatrix();

        MatrixStack poseStack = new MatrixStack();
        VertexConsumerProvider.Immediate vertexConsumerProvider$Immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        boolean usesBlockLight = !bakedmodel.isSideLit();
        if (usesBlockLight) {
            DiffuseLighting.disableGuiDepthLighting();
        }

        renderer.renderItem(stack, ModelTransformation.Mode.GUI, false, poseStack, vertexConsumerProvider$Immediate, 15728880, OverlayTexture.DEFAULT_UV, bakedmodel);
        vertexConsumerProvider$Immediate.draw();
        RenderSystem.enableDepthTest();
        if (usesBlockLight) {
            DiffuseLighting.enableGuiDepthLighting();
        }

        poseStack.pop();
        RenderSystem.applyModelViewMatrix();
    }

}
