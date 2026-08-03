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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.item.ItemStack;

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
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(
                        FACING,
                        context.getHorizontalDirection().getOpposite()
                );
    }
        // -----------------------------------------
    // ОБЩИЙ МЕТОД ОТКРЫТИЯ МЕНЮ
    // -----------------------------------------

    /**
     * Открывает интерфейс конкретного
     * алхимического стола.
     */
    private void openAlchemyTableMenu(
            Level level,
            BlockPos pos,
            Player player
    ) {

        /**
         * Настоящее меню открывает сервер.
         *
         * Клиент только получает команду
         * показать соответствующий Screen.
         */
        if (!level.isClientSide()) {

            BlockEntity blockEntity =
                    level.getBlockEntity(
                            pos
                    );


            /**
             * Проверяем, что в этой позиции
             * находится именно Block Entity
             * алхимического стола.
             */
            if (
                    blockEntity
                            instanceof AlchemyTableBlockEntity
                            alchemyTable
            ) {

                player.openMenu(
                        alchemyTable
                );
            }
        }
    }


    // -----------------------------------------
    // ПКМ ПУСТОЙ РУКОЙ
    // -----------------------------------------

    /**
     * Вызывается, когда в руке нет предмета.
     */
    @Override
    protected InteractionResult useWithoutItem(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            BlockHitResult hit
    ) {

        openAlchemyTableMenu(
                level,
                pos,
                player
        );


        /**
         * Сообщаем Minecraft,
         * что действие обработано успешно.
         */
        return InteractionResult.sidedSuccess(
                level.isClientSide()
        );
    }


    // -----------------------------------------
    // ПКМ С ПРЕДМЕТОМ В РУКЕ
    // -----------------------------------------

    /**
     * Вызывается, когда игрок держит предмет.
     *
     * Благодаря этому интерфейс откроется,
     * даже когда в руке находится зелье.
     */
    @Override
    protected ItemInteractionResult useItemOn(
            ItemStack stack,
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hit
    ) {

        /**
         * Обрабатываем только основную руку.
         *
         * Это защищает от двойного вызова
         * для двух рук игрока.
         */
        if (hand == InteractionHand.MAIN_HAND) {

            openAlchemyTableMenu(
                    level,
                    pos,
                    player
            );
        }


        return ItemInteractionResult.sidedSuccess(
                level.isClientSide()
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
