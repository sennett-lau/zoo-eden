package net.sennettlau.zooedenmod.datagen;

import net.sennettlau.zooedenmod.ZooEdenMod;
import net.sennettlau.zooedenmod.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ZooEdenMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModItems.TANGERINE);
        simpleItem(ModItems.TANGERINE_SEEDS);

        withExistingParent(ModItems.BEAVER_SPAWN_EGG.getId().getPath(),
                mcLoc("item/template_spawn_egg"));
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(ZooEdenMod.MOD_ID,"item/" + item.getId().getPath()));
    }
}