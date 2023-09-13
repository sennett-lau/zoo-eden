package net.sennettlau.zooedenmod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.sennettlau.zooedenmod.ZooEdenMod;
import net.sennettlau.zooedenmod.entity.custom.BeaverEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BeaverRenderer extends GeoEntityRenderer<BeaverEntity> {
    public BeaverRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BeaverModel());
    }

    @Override
    public ResourceLocation getTextureLocation(BeaverEntity animatable) {
        return new ResourceLocation(ZooEdenMod.MOD_ID, "textures/entity/beaver.png");
    }

    @Override
    public void render(BeaverEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        if(entity.isBaby()) {
            poseStack.scale(0.4f, 0.4f, 0.4f);
        }

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}