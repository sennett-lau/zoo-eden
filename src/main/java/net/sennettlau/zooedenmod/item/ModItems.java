package net.sennettlau.zooedenmod.item;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sennettlau.zooedenmod.ZooEdenMod;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ZooEdenMod.MOD_ID);

    public static final RegistryObject<Item> TANGERINE = ITEMS.register(
            "tangerine",
            () -> new Item(
                    new Item.Properties().stacksTo(64).food(
                            new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).build()
                    )
            )
    );

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}