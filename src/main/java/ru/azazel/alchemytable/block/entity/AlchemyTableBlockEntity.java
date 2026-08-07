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
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.PotionContents;


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
                "container.azazels-alchemy-table.alchemy_table"
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
        if (
            this.level != null && !this.level.isClientSide()
        ) {
            tryCraftPotion();
        }
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
    private void tryCraftPotion() {

    // Получаем предметы из четырёх слотов.
    ItemStack potion1 = POTIONSLOT0
            this.items.get(
                    
            );

    ItemStack potion2 = POTIONSLOT1
            this.items.get(
                    
            );

    ItemStack bottle = POTIONSLOT2
            this.items.get(
                   
            );

    ItemStack fuel =
            this.items.get(
                    
            );


    // Проверяем первое зелье.
    if (!potion1.is(Items.)) {
        return;
    }


    // Проверяем второе зелье.
    if (!potion2.is(Items.)) {
        return;
    }


    // В третьем слоте должна быть
    /
    if (!bottle.is(Items.)) {
        return;
    }


    // В четвёртом слоте должен быть
    if (!fuel.is(Items.)) {
        return;
    }


    // Получаем содержимое первого зелья.
    PotionContents contents1 =
            potion1.getOrDefault(
                    DataComponents.POTION_CONTENTS,
                    PotionContents.EMPTY
            );


    // Получаем содержимое второго зелья.
    PotionContents contents2 =
            potion2.getOrDefault(
                    DataComponents.POTION_CONTENTS,
                    PotionContents.EMPTY
            );


    // Защита:
    // если у первого зелья нет эффектов,
    // рецепт не запускаем.
    if (!contents1.hasEffects()) {
        return;
    }


    // То же самое для второго.
    if (!contents2.hasEffects()) {
        return;
    }


    // Создаём новое обычное питьевое зелье.
    ItemStack result =
            new ItemStack(
                    Items.POTION
            );


    // Пока его содержимое пустое.
    PotionContents resultContents =
            PotionContents.EMPTY;


    // Копируем все эффекты первого зелья.
    for (
            MobEffectInstance effect :
            contents1.getAllEffects()
    ) {

        resultContents =
                resultContents.withEffectAdded(
                        new MobEffectInstance(
                                effect
                        )
                );
    }


    // Копируем все эффекты второго зелья.
    for (
            MobEffectInstance effect :
            contents2.getAllEffects()
    ) {

        resultContents =
                resultContents.withEffectAdded(
                        new MobEffectInstance(
                                effect
                        )
                );
    }


    // Записываем получившиеся эффекты
    // в готовое зелье.
    result.set(
            DataComponents.POTION_CONTENTS,
            resultContents
    );


    // Даём результату понятное имя.
    result.set(
            DataComponents.CUSTOM_NAME,
            Component.literal(
                    "Двойное зелье Азазеля"
            )
    );


    // Расходуем первое зелье.
    potion1.shrink(
            1
    );


    // Расходуем второе зелье.
    potion2.shrink(
            1
    );


    // Расходуем один порошок ифрита.
    fuel.shrink(
            1
    );


    // Заменяем пустую бутылочку
    // готовым двойным зельем.
    this.items.set(
            POTIONSLOT3,
            result
    );


    // Если первое зелье закончилось,
    // очищаем слот.
    if (potion1.isEmpty()) {

        this.items.set(
                POTIONSLOT1,
                ItemStack.EMPTY
        );
    }


    // Если второе зелье закончилось,
    // очищаем слот.
    if (potion2.isEmpty()) {

        this.items.set(
                POTIONSLOT2,
                ItemStack.EMPTY
        );
    }


    // Если порошок закончился,
    // очищаем топливный слот.
    if (fuel.isEmpty()) {

        this.items.set(
                POTIONSLOT4,
                ItemStack.EMPTY
        );
    }


    // Сообщаем Minecraft,
    // что Block Entity изменилась
    // и её нужно сохранить.
    this.setChanged();
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


