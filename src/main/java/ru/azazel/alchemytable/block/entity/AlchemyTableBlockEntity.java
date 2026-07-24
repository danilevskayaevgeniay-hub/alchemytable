package ru.azazel.alchemytable.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class AlchemyTableBlockEntity extends BlockEntity {

    public AlchemyTableBlockEntity(
            BlockPos pos,
            BlockState state
    ) {
        super(
                ModBlockEntities.ALCHEMY_TABLE_BLOCK_ENTITY,
                pos,
                state
        );
    }
}


