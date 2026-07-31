package ru.azazel.alchemytable.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import ru.azazel.alchemytable.block.entity.AlchemyTableBlockEntity;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.whis.BlockHitResult;

public class AlchemyTableBlock extends Block implements EntityBlock{

    public static final DirectionProperty FACING =
            BlockStateProperties.HORIZONTAL_FACING;

    // Стол занимает две клетки и имеет высоту 25 пикселей.
    private static final VoxelShape SHAPE_NORTH =
            Block.box(-16, 0, 0, 16, 25, 16);

    private static final VoxelShape SHAPE_EAST =
            Block.box(0, 0, -16, 16, 25, 16);

    private static final VoxelShape SHAPE_SOUTH =
            Block.box(0, 0, 0, 32, 25, 16);

    private static final VoxelShape SHAPE_WEST =
            Block.box(0, 0, 0, 16, 25, 32);

    public AlchemyTableBlock(Properties properties) {
        super(properties);

        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(FACING, Direction.NORTH)
        );
    }

    @Override
    public BlockEntity newBlockEntity(
        BlockPos pos,
        BlockState state
    ) {
    return new AlchemyTableBlockEntity(
            pos,
            state
        );
    }
    // -----------------------------------------
    // ОТКРЫТИЕ МЕНЮ ПО ПКМ
    // -----------------------------------------

    /**
     * Этот метод вызывается,
     * когда игрок нажимает по столу
     * правой кнопкой мыши без использования
     * специального действия предмета.
     */
    @Override
    protected InteractionResult useWithoutItem(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            BlockHitResult hit
    ) {

        /**
         * Меню открываем только на серверной стороне.
         *
         * Сервер хранит настоящие предметы
         * и управляет содержимым контейнера.
         */
        if (!level.isClientSide()) {

            // Получаем Block Entity,
            // находящуюся в координатах стола.
            BlockEntity blockEntity =
                    level.getBlockEntity(
                            pos
                    );


            /**
             * Проверяем, что это действительно
             * наша AlchemyTableBlockEntity.
             */
            if (
                    blockEntity
                            instanceof AlchemyTableBlockEntity
                            alchemyTable
            ) {

                /**
                 * Передаём Block Entity как MenuProvider.
                 *
                 * Minecraft вызовет:
                 * getDisplayName()
                 * createMenu()
                 */
                player.openMenu(
                        alchemyTable
                );
            }
        }


        /**
         * SUCCESS сообщает Minecraft,
         * что взаимодействие обработано.
         */
        return InteractionResult.SUCCESS;
    }
    
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(
                        FACING,
                        context.getHorizontalDirection().getOpposite()
                );
    }

    @Override
    protected void createBlockStateDefinition(
            StateDefinition.Builder<Block, BlockState> builder
    ) {
        builder.add(FACING);
    }

    private VoxelShape getTableShape(BlockState state) {
        return switch (state.getValue(FACING)) {
            case NORTH -> SHAPE_NORTH;
            case EAST -> SHAPE_EAST;
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_WEST;
            default -> SHAPE_NORTH;
        };
    }

    @Override
    protected VoxelShape getShape(
            BlockState state,
            BlockGetter level,
            BlockPos pos,
            CollisionContext context
    ) {
        return getTableShape(state);
    }

    @Override
    protected VoxelShape getCollisionShape(
            BlockState state,
            BlockGetter level,
            BlockPos pos,
            CollisionContext context
    ) {
        return getTableShape(state);
    }

    @Override
    protected BlockState rotate(
            BlockState state,
            Rotation rotation
    ) {
        return state.setValue(
                FACING,
                rotation.rotate(state.getValue(FACING))
        );
    }

    @Override
    protected BlockState mirror(
            BlockState state,
            Mirror mirror
    ) {
        return state.rotate(
                mirror.getRotation(state.getValue(FACING))
        );
    }
}
