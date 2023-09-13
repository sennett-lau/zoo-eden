package net.sennettlau.zooedenmod.entity.client;

import net.sennettlau.zooedenmod.ZooEdenMod;
import net.sennettlau.zooedenmod.entity.custom.BeaverEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class BeaverModel extends GeoModel<BeaverEntity> {
    @Override
    public ResourceLocation getModelResource(BeaverEntity animatable) {
        return new ResourceLocation(ZooEdenMod.MOD_ID, "geo/beaver.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BeaverEntity animatable) {
        return new ResourceLocation(ZooEdenMod.MOD_ID, "textures/entity/beaver.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BeaverEntity animatable) {
        return new ResourceLocation(ZooEdenMod.MOD_ID, "animations/beaver.animation.json");
    }

    @Override
    public void setCustomAnimations(BeaverEntity animatable, long instanceId, AnimationState<BeaverEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}