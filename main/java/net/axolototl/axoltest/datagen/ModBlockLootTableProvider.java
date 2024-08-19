package net.axolototl.axoltest.datagen;

import net.axolototl.axoltest.block.ModBlocks;
import net.axolototl.axoltest.item.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    protected ModBlockLootTableProvider(HolderLookup.Provider provider) {
        super((Set.of()), FeatureFlags.REGISTRY.allFlags(), provider);
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.BLACK_OPAL_BLOCK.get());
        dropSelf(ModBlocks.RAW_BLACK_OPAL_BLOCK.get());
        dropSelf(ModBlocks.MAGIC_BLOCK.get());

        this.add(ModBlocks.BLACK_OPAL_ORE.get(),
                block -> createMultipleOreDrops(ModBlocks.BLACK_OPAL_ORE.get(), ModItems.RAW_BLACK_OPAL.get(), 2.0f, 5.0f));
        this.add(ModBlocks.BLACK_OPAL_DEEPSLATE_ORE.get(),
                block -> createMultipleOreDrops(ModBlocks.BLACK_OPAL_DEEPSLATE_ORE.get(), ModItems.RAW_BLACK_OPAL.get(), 2.0f, 5.0f));
        this.add(ModBlocks.BLACK_OPAL_END_ORE.get(),
                block -> createMultipleOreDrops(ModBlocks.BLACK_OPAL_END_ORE.get(), ModItems.RAW_BLACK_OPAL.get(), 2.0f, 5.0f));
        this.add(ModBlocks.BLACK_OPAL_NETHER_ORE.get(),
                block -> createMultipleOreDrops(ModBlocks.BLACK_OPAL_NETHER_ORE.get(), ModItems.RAW_BLACK_OPAL.get(), 2.0f, 5.0f));

        dropSelf(ModBlocks.BLACK_OPAL_STAIRS.get());
        this.add(ModBlocks.BLACK_OPAL_SLAB.get(),
                block -> createSlabItemTable(ModBlocks.BLACK_OPAL_SLAB.get()));


    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }

    protected LootTable.Builder createMultipleOreDrops(Block block, Item item, float minDrops, float maxDrops) {
        HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return this.createSilkTouchDispatchTable(block,
                (LootPoolEntryContainer.Builder)this.applyExplosionDecay(block, LootItem.lootTableItem(item)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(minDrops, maxDrops)))
                        .apply(ApplyBonusCount.addOreBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE)))));
    }
}
