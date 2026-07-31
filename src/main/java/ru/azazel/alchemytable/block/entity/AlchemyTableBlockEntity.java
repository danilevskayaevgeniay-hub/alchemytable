package ru.azazel.alchemytable.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import ru.azazel.alchemytable.menu.AlchemyTableMenu;


public class AlchemyTableBlockEntity extends BlockEntity implements Container, MenuProvider {
    public static final int POTIONSLOT1 = 0;
    public static final int POTIONSLOT2 = 1;
    public static final int POTIONSLOT3 = 2;
    public static final int POTIONSLOT4 = 3;
    public static final int CONTAINER_SIZE = 4;

    private final NonNullList<ItemStack> items =
            NonNullList.withSize(
                    CONTAINER_SIZE,
                    ItemStack.EMPTY
            );
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
    @Override
    public Component getDisplayName() {

        return Component.translatable(
                "block.azazels-alchemy-table.alchemy_table"
        );
    }
    @Override
    public AbstractContainerMenu createMenu(
            int containerId,
            Inventory playerInventory,
            Player player
    ) {

        return new AlchemyTableMenu(
                containerId,
                playerInventory,
                this
        );
    }

    @Override
    public int getContainerSize() {
        return CONTAINER_SIZE;
    }
    @Override
    public boolean isEmpty() {

        for (ItemStack stack : this.items) {

            if (!stack.isEmpty()) {
                return false;
            }
        }

        return true;
    }
    @Override
    public ItemStack removeItem(
            int slot,
            int amount
    ) {

        ItemStack result =
                ContainerHelper.removeItem(
                        this.items,
                        slot,
                        amount
                );

        if (!result.isEmpty()) {
            this.setChanged();
        }

        return result;
    }
    @Override
    public ItemStack removeItemNoUpdate(int slot) {

        return ContainerHelper.takeItem(
                this.items,
                slot
        );
    }
    @Override
    public ItemStack getItem(int slot) {
        return this.items.get(slot);
    }
    @Override
    public void setItem(
            int slot,
            ItemStack stack
    ) {

        this.items.set(
                slot,
                stack
        );

        stack.limitSize(
                this.getMaxStackSize(stack)
        );

        this.setChanged();
    }
    @Override
    public boolean stillValid(Player player) {

        return Container.stillValidBlockEntity(
                this,
                player
        );
    }
    @Override
    public void clearContent() {

        this.items.clear();

        this.setChanged();
    }


    // -----------------------------
    // ЧТО МОЖНО КЛАСТЬ В СЛОТЫ
    // -----------------------------

    @Override
    public boolean canPlaceItem(
            int slot,
            ItemStack stack
    ) {

        return switch (slot) {

            // В первые два слота кладём
            // только обычные питьевые зелья.
            case POTIONSLOT1,
                 POTIONSLOT2 ->
                    stack.is(Items.POTION);

            // В топливный слот -
            // только огненный порошок.
            case POTIONSLOT4 ->
                    stack.is(Items.BLAZE_POWDER);

            // В слот результата игрок
            // ничего положить не может.
            case POTIONSLOT3 ->
                    stack.is(Items.GLASS_BOTTLE);

            // Любого неизвестного слота
            // тоже быть не должно.
            default ->
                    false;
        };
    }
    // -----------------------------
    // СОХРАНЕНИЕ ПРЕДМЕТОВ
    // -----------------------------

    @Override
    protected void saveAdditional(
            CompoundTag tag,
            HolderLookup.Provider registries
    ) {

        super.saveAdditional(
                tag,
                registries
        );

        ContainerHelper.saveAllItems(
                tag,
                this.items,
                registries
        );
    }


    // -----------------------------
    // ЗАГРУЗКА ПРЕДМЕТОВ
    // -----------------------------

    @Override
    protected void loadAdditional(
            CompoundTag tag,
            HolderLookup.Provider registries
    ) {

        super.loadAdditional(
                tag,
                registries
        );

        this.items.clear();

        ContainerHelper.loadAllItems(
                tag,
                this.items,
                registries
        );
    }
}


