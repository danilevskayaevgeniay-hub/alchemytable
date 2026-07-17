package ru.azazel.alchemytable.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import ru.azazel.alchemytable.block.ModBlocks;

public class AzazelSAlchemyTableClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Настраиваем отображение прозрачных участков модели.
        BlockRenderLayerMap.INSTANCE.putBlock(
                ModBlocks.ALCHEMY_TABLE,
                RenderType.cutout()
        );
    }
}
