package net.sennettlau.zooedenmod.datagen;

import net.sennettlau.zooedenmod.ZooEdenMod;
import net.sennettlau.zooedenmod.block.ModBlocks;
import net.sennettlau.zooedenmod.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.List;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.TANGERINE_SEEDS.get(), 3)
                .requires(ModItems.TANGERINE.get())
                .unlockedBy(getHasName(ModItems.TANGERINE.get()), has(ModItems.TANGERINE.get()))
                .save(pWriter);
    }
}