package net.sennettlau.zooedenmod.item;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sennettlau.zooedenmod.ZooEdenMod;
import net.sennettlau.zooedenmod.block.ModBlocks;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ZooEdenMod.MOD_ID);

    public static final RegistryObject<Item> TANGERINE = ITEMS.register("tangerine",
            () -> new Item(new Item.Properties().food(ModFoods.TANGERINE)));

    public static final RegistryObject<Item> TANGERINE_SEEDS = ITEMS.register("tangerine_seeds",
            () -> new ItemNameBlockItem(ModBlocks.TANGERINE_CROP.get(), new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
