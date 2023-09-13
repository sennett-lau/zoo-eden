package net.sennettlau.zooedenmod.event;

import net.sennettlau.zooedenmod.ZooEdenMod;
import net.sennettlau.zooedenmod.entity.ModEntities;
import net.sennettlau.zooedenmod.entity.custom.BeaverEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ZooEdenMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents {
    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(ModEntities.BEAVER.get(), BeaverEntity.setAttributes());
    }
}