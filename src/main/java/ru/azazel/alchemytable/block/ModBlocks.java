package ru.azazel.alchemytable.block;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSoundGroup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import ru.azazel.alchemytable.AzazelsAlchemyTableMod;

public class ModBlocks {

    // Пока это декоративный блок без собственного интерфейса.
    public static final Block ALCHEMY_TABLE = registerBlock(
            "alchemy_table",
            new Block(
                    AbstractBlock.Settings.create()
                            .strength(2.5f, 6.0f)
                            .sounds(BlockSoundGroup.WOOD)
                            .nonOpaque()
            )
    );

    private static Block registerBlock(String name, Block block) {
        Identifier id = Identifier.of(AzazelsAlchemyTableMod.MOD_ID, name);

        // Регистрируем объект как блок, который можно поставить в мир.
        Block registeredBlock = Registry.register(Registries.BLOCK, id, block);

        // Регистрируем его предметную версию для инвентаря и руки игрока.
        Registry.register(
                Registries.ITEM,
                id,
                new BlockItem(registeredBlock, new Item.Settings())
        );

        return registeredBlock;
    }

    public static void registerModBlocks() {
        // Добавляем стол во вкладку функциональных блоков творческого режима.
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL)
                .register(entries -> entries.add(ALCHEMY_TABLE));
    }

    private ModBlocks() {
        // Служебный класс не должен создаваться как обычный объект.
    }
}
