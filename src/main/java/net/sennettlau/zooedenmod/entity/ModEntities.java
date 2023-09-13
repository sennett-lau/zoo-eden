package net.sennettlau.zooedenmod.entity;

import net.sennettlau.zooedenmod.ZooEdenMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sennettlau.zooedenmod.entity.custom.BeaverEntity;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ZooEdenMod.MOD_ID);

    public static final RegistryObject<EntityType<BeaverEntity>> BEAVER =
            ENTITY_TYPES.register("beaver",
                    () -> EntityType.Builder.of(BeaverEntity::new, MobCategory.CREATURE)
                            .sized(0.9f, 0.9f)
                            .build(new ResourceLocation(ZooEdenMod.MOD_ID, "beaver").toString()));


    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}