package net.sennettlau.zooedenmod.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.ShoulderRidingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.scores.Team;
import net.minecraftforge.event.ForgeEventFactory;
import net.sennettlau.zooedenmod.entity.ModEntities;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;

public class BeaverEntity extends ShoulderRidingEntity implements GeoEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private Goal landOnOwnersShoulderGoal;

    public BeaverEntity(EntityType<? extends ShoulderRidingEntity> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.moveControl = new BeaverMoveControl(this, 10, 10, 0.5F);
        this.lookControl = new SmoothSwimmingLookControl(this, 20);
    }

    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(BeaverEntity.class, EntityDataSerializers.BOOLEAN);

    public Level getEntityLevel() {
        return level();
    }

    public static AttributeSupplier setAttributes() {
        return Animal.createMobAttributes().add(Attributes.MAX_HEALTH, 16D).add(Attributes.MOVEMENT_SPEED, 0.3f).add(Attributes.FOLLOW_RANGE, 16D).add(Attributes.ATTACK_DAMAGE, 1D).build();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new BreathAirGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.25D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(3, new FollowOwnerGoal(this, 1.0D, 3.0F, 2.0F, false));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1.0D, 10));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return ModEntities.BEAVER.get().create(serverLevel);
    }

    @Override
    public boolean isFood(ItemStack pStack) {
        return pStack.getItem() == Items.APPLE || pStack.getItem() == Items.CARROT;
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, 0.15F, 1.0F);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.FOX_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.DOLPHIN_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.DOLPHIN_DEATH;
    }

    protected float getSoundVolume() {
        return 0.2F;
    }

    /* TAMEABLE */
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        Item item = itemstack.getItem();

        Item itemForTaming = Items.GOLDEN_CARROT;

        if (isFood(itemstack)) {
            return super.mobInteract(player, hand);
        }

        if (item == itemForTaming && !isTame()) {
            if (this.getEntityLevel().isClientSide) {
                return InteractionResult.CONSUME;
            } else {
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                if (!ForgeEventFactory.onAnimalTame(this, player)) {
                    if (!this.getEntityLevel().isClientSide) {
                        super.tame(player);
                        this.navigation.recomputePath();
                        this.setTarget(null);
                        this.getEntityLevel().broadcastEntityEvent(this, (byte) 7);
                        if (this.isBaby()) {
                            this.landOnOwnersShoulderGoal = new LandOnOwnersShoulderGoal(this);
                            this.goalSelector.addGoal(4, this.landOnOwnersShoulderGoal);
                        }
                    }
                }

                return InteractionResult.SUCCESS;
            }
        }

        if (isTame() && !this.getEntityLevel().isClientSide && hand == InteractionHand.MAIN_HAND) {
            setSitting(!isSitting());
            return InteractionResult.SUCCESS;
        }

        if (itemstack.getItem() == itemForTaming) {
            return InteractionResult.PASS;
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setSitting(tag.getBoolean("isSitting"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("isSitting", this.isSitting());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SITTING, false);
    }

    public void setSitting(boolean sitting) {
        this.entityData.set(SITTING, sitting);
        this.setOrderedToSit(sitting);
    }

    public boolean isSitting() {
        return this.entityData.get(SITTING);
    }

    @Override
    public Team getTeam() {
        return super.getTeam();
    }

    public boolean canBeLeashed(Player player) {
        return false;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        if (tAnimationState.isMoving()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.beaver.walk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }
        if (this.isSitting()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.beaver.sit", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }


        tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.beaver.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public void ageUp(int growthSeconds) {
        super.ageUp(growthSeconds);
        if (!this.isBaby() && this.landOnOwnersShoulderGoal != null) {
            this.goalSelector.removeGoal(this.landOnOwnersShoulderGoal);
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    static class BeaverMoveControl extends MoveControl {
        private final BeaverEntity beaver;
        private static final float FULL_SPEED_TURN_THRESHOLD = 10.0F;
        private static final float STOP_TURN_THRESHOLD = 60.0F;
        private final int maxTurnX;
        private final int maxTurnY;
        private final float inWaterSpeedModifier;

        BeaverMoveControl(BeaverEntity beaver, int maxTurnX, int maxTurnY, float inWaterSpeedModifier) {
            super(beaver);
            this.beaver = beaver;
            this.maxTurnX = maxTurnX;
            this.maxTurnY = maxTurnY;
            this.inWaterSpeedModifier = inWaterSpeedModifier;
        }

        private void updateSpeed() {
            if (this.beaver.isInWater()) {
                double yDelta = this.beaver.getAirSupply() < 70 ? 0.02D : 0.005D;
                this.beaver.setDeltaMovement(this.beaver.getDeltaMovement().add(0.0D, yDelta, 0.0D));
            } else if (this.beaver.onGround()) {
                if (this.beaver.isBaby()) {
                    this.beaver.setSpeed(0.2f);
                } else {
                    this.beaver.setSpeed(0.4f);
                }
            }
        }

        public void tick() {
            this.updateSpeed();

            if (this.beaver.isInWater()) {
                this.inWaterTick();
            } else {
                super.tick();
            }
        }

        private void inWaterTick() {
            double d0 = this.wantedX - this.mob.getX();
            double d1 = this.wantedY - this.mob.getY();
            double d2 = this.wantedZ - this.mob.getZ();
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;
            if (d3 < (double) 2.5000003E-7F) {
                this.mob.setZza(0.0F);
            } else {
                float f = (float) (Mth.atan2(d2, d0) * (double) (180F / (float) Math.PI)) - 90.0F;
                this.mob.setYRot(this.rotlerp(this.mob.getYRot(), f, (float) this.maxTurnY));
                this.mob.yBodyRot = this.mob.getYRot();
                this.mob.yHeadRot = this.mob.getYRot();
                float f1 = (float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
                this.mob.setSpeed(f1 * this.inWaterSpeedModifier);
                double d4 = Math.sqrt(d0 * d0 + d2 * d2);
                if (Math.abs(d1) > (double) 1.0E-5F || Math.abs(d4) > (double) 1.0E-5F) {
                    float f3 = -((float) (Mth.atan2(d1, d4) * (double) (180F / (float) Math.PI)));
                    f3 = Mth.clamp(Mth.wrapDegrees(f3), (float) (-this.maxTurnX), (float) this.maxTurnX);
                    this.mob.setXRot(this.rotlerp(this.mob.getXRot(), f3, 5.0F));
                }

                float f6 = Mth.cos(this.mob.getXRot() * ((float) Math.PI / 180F));
                float f4 = Mth.sin(this.mob.getXRot() * ((float) Math.PI / 180F));
                this.mob.zza = f6 * f1;
                this.mob.yya = -f4 * f1;
            }
        }
    }
}
