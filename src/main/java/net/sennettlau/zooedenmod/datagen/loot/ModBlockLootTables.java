package net.sennettlau.zooedenmod.datagen.loot;

import net.sennettlau.zooedenmod.block.ModBlocks;
import net.sennettlau.zooedenmod.block.custom.TangerineCropBlock;
import net.sennettlau.zooedenmod.item.ModItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {

        LootItemCondition.Builder lootitemcondition$builder = LootItemBlockStatePropertyCondition
                .hasBlockStateProperties(ModBlocks.TANGERINE_CROP.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(TangerineCropBlock.AGE, 5));

        this.add(ModBlocks.TANGERINE_CROP.get(), createCropDrops(ModBlocks.TANGERINE_CROP.get(), ModItems.TANGERINE.get(),
                ModItems.TANGERINE.get(), lootitemcondition$builder));

    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}