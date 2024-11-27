package io.github.a1qs.vaultadditions.block.blockentity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import io.github.a1qs.vaultadditions.block.LootStatueBlock;
import io.github.a1qs.vaultadditions.block.blockentity.LootStatueBlockEntity;
import iskallia.vault.VaultMod;
import iskallia.vault.entity.model.StatuePlayerModel;
import iskallia.vault.util.SkinProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class LootStatueBlockRenderer implements BlockEntityRenderer<LootStatueBlockEntity> {
    protected static StatuePlayerModel PLAYER_MODEL;
    private final Minecraft mc = Minecraft.getInstance();
    private final ItemRenderer itemRenderer;
    private static final ResourceLocation STONE_SKIN = VaultMod.id("textures/entity/stoneskin.png");

    public LootStatueBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = this.mc.getItemRenderer();
        PLAYER_MODEL = new StatuePlayerModel(context);
    }

    @Override
    public void render(LootStatueBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int combinedLight, int combinedOverlay) {
        BlockState blockState = blockEntity.getBlockState();
        Direction direction = blockState.getValue(LootStatueBlock.FACING);
        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.mulPose(Vector3f.YN.rotationDegrees(direction.toYRot() + 180.0F));

        ItemStack renderItem = blockEntity.getLootItem();
        if (!renderItem.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.0, 0.1, 0.05);
            BakedModel bakedmodel = this.itemRenderer.getModel(renderItem, null, null, 0);
            float scale = bakedmodel.isGui3d() ? 0.5F : 0.333333F;
            poseStack.scale(scale, scale, scale);
            this.itemRenderer.render(renderItem, ItemTransforms.TransformType.FIXED, false, poseStack, multiBufferSource, combinedLight, combinedOverlay, bakedmodel);
            poseStack.popPose();
        }

        this.drawPlayerModel(poseStack, multiBufferSource, blockEntity, combinedLight, combinedOverlay);
    }

    private void drawPlayerModel(PoseStack matrixStack, MultiBufferSource buffer, LootStatueBlockEntity blockEntity, int combinedLight, int combinedOverlay) {
        SkinProfile skin = blockEntity.getSkin();
        ResourceLocation skinLocation = StringUtil.isNullOrEmpty(skin.getLatestNickname()) ? STONE_SKIN : skin.getLocationSkin();
        RenderType renderType = PLAYER_MODEL.renderType(skinLocation);
        PLAYER_MODEL.young = false;
        PLAYER_MODEL.leftArm.xRot = -120.0F;
        PLAYER_MODEL.leftSleeve.xRot = -120.0F;
        PLAYER_MODEL.rightArm.xRot = -120.0F;
        PLAYER_MODEL.rightSleeve.xRot = -120.0F;
        PLAYER_MODEL.head.zRot = 0.0F;
        PLAYER_MODEL.hat.zRot = 0.0F;
        PLAYER_MODEL.setSlim(skin.isSlim());

        VertexConsumer vertexBuilder = buffer.getBuffer(renderType);
        float scale = 0.5F;
        float statueOffset = -0.15F;
        matrixStack.pushPose();
        matrixStack.translate(0.0, 0.0, statueOffset);
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(180.0F));
        matrixStack.scale(scale, scale, scale);
        matrixStack.translate(0.0, -0.75, 0.0); // -1.5
        PLAYER_MODEL.renderToBuffer(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.popPose();
        if (buffer instanceof MultiBufferSource.BufferSource) {
            ((MultiBufferSource.BufferSource)buffer).endBatch(renderType);
        }
    }
}
