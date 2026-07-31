package ru.azazel.alchemytable.menu;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import ru.azazel.alchemytable.block.entity.AlchemyTableBlockEntity;

/**
 * Логика меню алхимического стола.
 *
 * Этот класс:
 * 1. Создаёт четыре слота алхимического стола.
 * 2. Добавляет инвентарь игрока.
 * 3. Добавляет панель быстрого доступа.
 * 4. Ограничивает типы предметов в слотах.
 * 5. Обрабатывает Shift + клик.
 *
 * Важно:
 * этот класс не рисует интерфейс.
 * За внешний вид позже будет отвечать AlchemyTableScreen.
 */
public class AlchemyTableMenu
        extends AbstractContainerMenu {

    // -----------------------------------------
    // КОЛИЧЕСТВО СЛОТОВ АЛХИМИЧЕСКОГО СТОЛА
    // -----------------------------------------

    /**
     * У самого стола четыре слота.
     *
     * Значение берём из Block Entity,
     * чтобы не писать число 4 повторно.
     */
    private static final int TABLE_SLOT_COUNT =
            AlchemyTableBlockEntity.CONTAINER_SIZE;


    // -----------------------------------------
    // ГРАНИЦЫ СЛОТОВ ВНУТРИ MENU
    // -----------------------------------------

    /**
     * Слоты стола в общем списке Menu:
     *
     * 0 - первое зелье
     * 1 - второе зелье
     * 2 - бутылочка / результат
     * 3 - порошок ифрита
     */
    private static final int TABLE_START = 0;
    private static final int TABLE_END =
            TABLE_START + TABLE_SLOT_COUNT;


    /**
     * Основной инвентарь игрока содержит 27 ячеек.
     *
     * Он начинается сразу после четырёх слотов стола.
     */
    private static final int PLAYER_INVENTORY_START =
            TABLE_END;

    private static final int PLAYER_INVENTORY_END =
            PLAYER_INVENTORY_START + 27;


    /**
     * Хотбар содержит 9 ячеек.
     */
    private static final int HOTBAR_START =
            PLAYER_INVENTORY_END;

    private static final int HOTBAR_END =
            HOTBAR_START + 9;


    // -----------------------------------------
    // КООРДИНАТЫ СЛОТОВ
    // -----------------------------------------

    /**
     * Координаты измеряются в пикселях
     * относительно левого верхнего угла будущего окна.
     *
     * Эти же координаты мы учтём при создании Screen.
     */

    // Первое зелье.
    private static final int POTION_1_X = 56;
    private static final int POTION_1_Y = 35;

    // Второе зелье.
    private static final int POTION_2_X = 102;
    private static final int POTION_2_Y = 35;

    // Пустая бутылочка, а затем результат.
    private static final int BOTTLE_RESULT_X = 79;
    private static final int BOTTLE_RESULT_Y = 58;

    // Порошок ифрита.
    private static final int FUEL_X = 17;
    private static final int FUEL_Y = 17;

    // Начальная точка основного инвентаря игрока.
    private static final int PLAYER_INVENTORY_X = 8;
    private static final int PLAYER_INVENTORY_Y = 84;

    // Положение хотбара.
    private static final int HOTBAR_X = 8;
    private static final int HOTBAR_Y = 142;

    // Расстояние между слотами Minecraft.
    private static final int SLOT_DISTANCE = 18;


    // -----------------------------------------
    // КОНТЕЙНЕР АЛХИМИЧЕСКОГО СТОЛА
    // -----------------------------------------

    /**
     * Здесь хранится ссылка на настоящий контейнер.
     *
     * На сервере это будет AlchemyTableBlockEntity.
     * На клиенте сначала используется SimpleContainer.
     */
    private final Container container;


    // -----------------------------------------
    // КЛИЕНТСКИЙ КОНСТРУКТОР
    // -----------------------------------------

    /**
     * Этот конструктор Minecraft вызывает на клиенте.
     *
     * Клиент пока не получает прямую ссылку
     * на серверную Block Entity.
     *
     * Поэтому создаём временный контейнер
     * на четыре ячейки.
     */
    public AlchemyTableMenu(
            int containerId,
            Inventory playerInventory
    ) {

        this(
                containerId,
                playerInventory,

                new SimpleContainer(
                        AlchemyTableBlockEntity.CONTAINER_SIZE
                )
        );
    }


    // -----------------------------------------
    // ОСНОВНОЙ КОНСТРУКТОР
    // -----------------------------------------

    /**
     * Этот конструктор используется на сервере.
     *
     * В параметре container сюда будет передана
     * настоящая AlchemyTableBlockEntity.
     */
    public AlchemyTableMenu(
            int containerId,
            Inventory playerInventory,
            Container container
    ) {

        // Передаём родительскому классу:
        // 1. Тип нашего меню.
        // 2. ID открытого контейнера.
        super(
                ModMenuTypes.ALCHEMY_TABLE_MENU,
                containerId
        );


        // Проверяем, что контейнер действительно
        // содержит четыре слота.
        checkContainerSize(
                container,
                AlchemyTableBlockEntity.CONTAINER_SIZE
        );


        // Запоминаем настоящий контейнер.
        this.container = container;


        // Сообщаем контейнеру,
        // что игрок начал им пользоваться.
        this.container.startOpen(
                playerInventory.player
        );


        // Добавляем четыре слота алхимического стола.
        addAlchemyTableSlots();


        // Добавляем 27 основных ячеек игрока.
        addPlayerInventory(
                playerInventory
        );


        // Добавляем 9 ячеек хотбара.
        addPlayerHotbar(
                playerInventory
        );
    }


    // -----------------------------------------
    // ЧЕТЫРЕ СЛОТА АЛХИМИЧЕСКОГО СТОЛА
    // -----------------------------------------

    /**
     * Создаём четыре видимых логических Slot.
     *
     * Каждый Slot связывается с определённым
     * индексом внутри AlchemyTableBlockEntity.
     */
    private void addAlchemyTableSlots() {

        // Слот 1 - первое питьевое зелье.
        addPotionSlot(
                AlchemyTableBlockEntity.POTIONSLOT1,
                POTION_1_X,
                POTION_1_Y
        );


        // Слот 2 - второе питьевое зелье.
        addPotionSlot(
                AlchemyTableBlockEntity.POTIONSLOT2,
                POTION_2_X,
                POTION_2_Y
        );


        // Слот 3 - пустая бутылочка,
        // которая позже станет результатом.
        addBottleResultSlot(
                AlchemyTableBlockEntity.POTIONSLOT3,
                BOTTLE_RESULT_X,
                BOTTLE_RESULT_Y
        );


        // Слот 4 - порошок ифрита.
        addFuelSlot(
                AlchemyTableBlockEntity.POTIONSLOT4,
                FUEL_X,
                FUEL_Y
        );
    }


    // -----------------------------------------
    // СЛОТ ЗЕЛЬЯ
    // -----------------------------------------

    /**
     * Создаёт слот, принимающий только
     * обычное питьевое зелье Items.POTION.
     */
    private void addPotionSlot(
            int containerSlot,
            int x,
            int y
    ) {

        this.addSlot(

                new Slot(
                        this.container,
                        containerSlot,
                        x,
                        y
                ) {

                    /**
                     * Проверяем, можно ли положить
                     * предмет в этот слот.
                     */
                    @Override
                    public boolean mayPlace(
                            ItemStack stack
                    ) {

                        return stack.is(
                                Items.POTION
                        );
                    }


                    /**
                     * В один слот помещается
                     * только одна бутылочка.
                     */
                    @Override
                    public int getMaxStackSize() {
                        return 1;
                    }
                }
        );
    }


    // -----------------------------------------
    // СЛОТ БУТЫЛОЧКИ И РЕЗУЛЬТАТА
    // -----------------------------------------

    /**
     * Игрок может положить сюда только
     * пустую стеклянную бутылочку.
     *
     * Позже код смешивания сам заменит её
     * на готовое зелье с двумя эффектами.
     */
    private void addBottleResultSlot(
            int containerSlot,
            int x,
            int y
    ) {

        this.addSlot(

                new Slot(
                        this.container,
                        containerSlot,
                        x,
                        y
                ) {

                    @Override
                    public boolean mayPlace(
                            ItemStack stack
                    ) {

                        return stack.is(
                                Items.GLASS_BOTTLE
                        );
                    }


                    /**
                     * За одну операцию используем
                     * одну пустую бутылочку.
                     */
                    @Override
                    public int getMaxStackSize() {
                        return 1;
                    }
                }
        );
    }


    // -----------------------------------------
    // СЛОТ ТОПЛИВА
    // -----------------------------------------

    /**
     * В топливный слот разрешён
     * только порошок ифрита.
     */
    private void addFuelSlot(
            int containerSlot,
            int x,
            int y
    ) {

        this.addSlot(

                new Slot(
                        this.container,
                        containerSlot,
                        x,
                        y
                ) {

                    @Override
                    public boolean mayPlace(
                            ItemStack stack
                    ) {

                        return stack.is(
                                Items.BLAZE_POWDER
                        );
                    }
                }
        );
    }


    // -----------------------------------------
    // ОСНОВНОЙ ИНВЕНТАРЬ ИГРОКА
    // -----------------------------------------

    /**
     * Основной инвентарь состоит из:
     *
     * 3 рядов
     * по 9 ячеек
     *
     * Всего 27 ячеек.
     */
    private void addPlayerInventory(
            Inventory playerInventory
    ) {

        // Перебираем три ряда.
        for (
                int row = 0;
                row < 3;
                row++
        ) {

            // В каждом ряду девять столбцов.
            for (
                    int column = 0;
                    column < 9;
                    column++
            ) {

                /**
                 * Индексы 0-8 в Inventory
                 * принадлежат хотбару.
                 *
                 * Поэтому основной инвентарь
                 * начинается с индекса 9.
                 */
                int inventorySlot =
                        column
                                + row * 9
                                + 9;


                int x =
                        PLAYER_INVENTORY_X
                                + column * SLOT_DISTANCE;


                int y =
                        PLAYER_INVENTORY_Y
                                + row * SLOT_DISTANCE;


                this.addSlot(

                        new Slot(
                                playerInventory,
                                inventorySlot,
                                x,
                                y
                        )
                );
            }
        }
    }


    // -----------------------------------------
    // ХОТБАР ИГРОКА
    // -----------------------------------------

    /**
     * Хотбар - нижняя панель из девяти ячеек.
     *
     * В Inventory он использует индексы 0-8.
     */
    private void addPlayerHotbar(
            Inventory playerInventory
    ) {

        for (
                int column = 0;
                column < 9;
                column++
        ) {

            int x =
                    HOTBAR_X
                            + column * SLOT_DISTANCE;


            this.addSlot(

                    new Slot(
                            playerInventory,
                            column,
                            x,
                            HOTBAR_Y
                    )
            );
        }
    }


    // -----------------------------------------
    // ПРОВЕРКА ДОСТУПА К СТОЛУ
    // -----------------------------------------

    /**
     * Minecraft регулярно проверяет,
     * может ли игрок продолжать пользоваться столом.
     *
     * Например, стол могли сломать
     * или игрок мог отойти слишком далеко.
     */
    @Override
    public boolean stillValid(
            Player player
    ) {

        return this.container.stillValid(
                player
        );
    }


    // -----------------------------------------
    // ЗАКРЫТИЕ МЕНЮ
    // -----------------------------------------

    /**
     * Сообщаем контейнеру,
     * что игрок перестал им пользоваться.
     */
    @Override
    public void removed(
            Player player
    ) {

        super.removed(
                player
        );

        this.container.stopOpen(
                player
        );
    }


    // -----------------------------------------
    // SHIFT + КЛИК
    // -----------------------------------------

    /**
     * Этот метод отвечает за быстрое перемещение
     * предметов через Shift + клик.
     *
     * index - индекс Slot внутри открытого Menu,
     * а не индекс внутри Block Entity.
     */
    @Override
    public ItemStack quickMoveStack(
            Player player,
            int index
    ) {

        // Получаем слот, по которому нажал игрок.
        Slot sourceSlot =
                this.slots.get(
                        index
                );


        // В пустом слоте перемещать нечего.
        if (!sourceSlot.hasItem()) {

            return ItemStack.EMPTY;
        }


        // Получаем предмет из исходного слота.
        ItemStack sourceStack =
                sourceSlot.getItem();


        // Сохраняем копию для результата метода.
        ItemStack originalStack =
                sourceStack.copy();


        // -------------------------------------
        // ИЗ СТОЛА В ИНВЕНТАРЬ ИГРОКА
        // -------------------------------------

        if (index < TABLE_END) {

            boolean moved =
                    this.moveItemStackTo(
                            sourceStack,
                            PLAYER_INVENTORY_START,
                            HOTBAR_END,

                            // Перебираем слоты с конца.
                            true
                    );


            if (!moved) {

                return ItemStack.EMPTY;
            }
        }


        // -------------------------------------
        // ИЗ ИНВЕНТАРЯ ИГРОКА В СТОЛ
        // -------------------------------------

        else {

            // Обычные питьевые зелья
            // отправляем в слоты 0 и 1.
            if (
                    sourceStack.is(
                            Items.POTION
                    )
            ) {

                boolean moved =
                        this.moveItemStackTo(
                                sourceStack,
                                AlchemyTableBlockEntity.POTIONSLOT1,

                                // Конечный индекс не включается.
                                // Поэтому диапазон 0-2 означает
                                // только слоты 0 и 1.
                                AlchemyTableBlockEntity.POTIONSLOT3,

                                false
                        );


                if (!moved) {

                    return ItemStack.EMPTY;
                }
            }


            // Пустую бутылочку
            // отправляем только в слот 2.
            else if (
                    sourceStack.is(
                            Items.GLASS_BOTTLE
                    )
            ) {

                boolean moved =
                        this.moveItemStackTo(
                                sourceStack,

                                // Начало диапазона - индекс 2.
                                AlchemyTableBlockEntity.POTIONSLOT3,

                                // Конец не включается.
                                // 2-3 означает только слот 2.
                                AlchemyTableBlockEntity.POTIONSLOT4,

                                false
                        );


                if (!moved) {

                    return ItemStack.EMPTY;
                }
            }


            // Порошок ифрита
            // отправляем только в слот 3.
            else if (
                    sourceStack.is(
                            Items.BLAZE_POWDER
                    )
            ) {

                boolean moved =
                        this.moveItemStackTo(
                                sourceStack,

                                // Начало - индекс 3.
                                AlchemyTableBlockEntity.POTIONSLOT4,

                                // Конец - индекс 4,
                                // который уже не включается.
                                TABLE_END,

                                false
                        );


                if (!moved) {

                    return ItemStack.EMPTY;
                }
            }


            // ---------------------------------
            // ОБЫЧНЫЕ ПРЕДМЕТЫ
            // ---------------------------------

            // Предмет из основного инвентаря
            // переносим в хотбар.
            else if (
                    index >= PLAYER_INVENTORY_START
                            && index < PLAYER_INVENTORY_END
            ) {

                boolean moved =
                        this.moveItemStackTo(
                                sourceStack,
                                HOTBAR_START,
                                HOTBAR_END,
                                false
                        );


                if (!moved) {

                    return ItemStack.EMPTY;
                }
            }


            // Предмет из хотбара
            // переносим в основной инвентарь.
            else if (
                    index >= HOTBAR_START
                            && index < HOTBAR_END
            ) {

                boolean moved =
                        this.moveItemStackTo(
                                sourceStack,
                                PLAYER_INVENTORY_START,
                                PLAYER_INVENTORY_END,
                                false
                        );


                if (!moved) {

                    return ItemStack.EMPTY;
                }
            }


            // Неизвестный диапазон слотов.
            else {

                return ItemStack.EMPTY;
            }
        }


        // -------------------------------------
        // ОБНОВЛЯЕМ ИСХОДНЫЙ СЛОТ
        // -------------------------------------

        // Если исходная стопка полностью переместилась,
        // делаем слот пустым.
        if (sourceStack.isEmpty()) {

            sourceSlot.setByPlayer(
                    ItemStack.EMPTY
            );
        }

        // Если часть стопки осталась,
        // сообщаем Minecraft об изменении.
        else {

            sourceSlot.setChanged();
        }


        /**
         * Защита:
         * если количество не изменилось,
         * значит реального перемещения не произошло.
         */
        if (
                sourceStack.getCount()
                        == originalStack.getCount()
        ) {

            return ItemStack.EMPTY;
        }


        // Сообщаем слоту,
        // что игрок забрал из него предмет.
        sourceSlot.onTake(
                player,
                sourceStack
        );


        return originalStack;
    }
}


