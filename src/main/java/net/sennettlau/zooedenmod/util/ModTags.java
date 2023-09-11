package net.sennettlau.zooedenmod.util;

import net.sennettlau.zooedenmod.ZooEdenMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {
        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(ZooEdenMod.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> TANGERINE_SEEDS = tag("tangerine_seeds");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(ZooEdenMod.MOD_ID, name));
        }
    }
}