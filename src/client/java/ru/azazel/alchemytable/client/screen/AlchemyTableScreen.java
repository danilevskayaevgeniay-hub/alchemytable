package ru.azazel.alchemytable.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import ru.azazel.alchemytable.AzazelSAlchemyTable;
import ru.azazel.alchemytable.menu.AlchemyTableMenu;

/**
 * Видимый экран алхимического стола.
 *
 * Этот класс отвечает только за визуальную часть:
 *
 * 1. Рисует фон окна.
 * 2. Показывает название стола.
 * 3. Показывает название инвентаря игрока.
 * 4. Позволяет Minecraft нарисовать предметы в слотах.
 *
 * Сами предметы хранятся не здесь.
 * Они хранятся в AlchemyTableBlockEntity.
 *
 * Координаты слотов задаются не здесь.
 * Они задаются в AlchemyTableMenu.
 */
public class AlchemyTableScreen
        extends AbstractContainerScreen<AlchemyTableMenu> {

    // -----------------------------------------
    // ТЕКСТУРА ИНТЕРФЕЙСА
    // -----------------------------------------

    /**
     * Полный технический адрес нашей текстуры:
     *
     * azazels-alchemy-table:
     * textures/gui/alchemy_table.png
     */
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    AzazelSAlchemyTable.MOD_ID,
                    "textures/gui/alchemy_table.png"
            );


    // -----------------------------------------
    // РАЗМЕР ТЕКСТУРЫ
    // -----------------------------------------

    /**
     * Наша текстура имеет точный размер:
     *
     * 176 пикселей в ширину
     * 166 пикселей в высоту
     */
    private static final int TEXTURE_WIDTH = 176;
    private static final int TEXTURE_HEIGHT = 166;


    // -----------------------------------------
    // КОНСТРУКТОР
    // -----------------------------------------

    /**
     * Minecraft вызывает этот конструктор,
     * когда нужно открыть интерфейс.
     *
     * menu
     * содержит слоты и логику перемещения предметов.
     *
     * playerInventory
     * содержит инвентарь игрока.
     *
     * title
     * содержит название окна.
     */
    public AlchemyTableScreen(
            AlchemyTableMenu menu,
            Inventory playerInventory,
            Component title
    ) {

        // Передаём данные базовому экрану Minecraft.
        super(
                menu,
                playerInventory,
                title
        );


        /**
         * Указываем размер самого окна.
         *
         * Он должен совпадать с размером
         * подготовленного PNG-файла.
         */
        this.imageWidth = TEXTURE_WIDTH;
        this.imageHeight = TEXTURE_HEIGHT;


        /**
         * Положение названия инвентаря игрока.
         *
         * Основной инвентарь начинается с Y = 84,
         * поэтому подпись помещаем немного выше.
         */
        this.inventoryLabelX = 8;
        this.inventoryLabelY = 72;
    }


    // -----------------------------------------
    // ОСНОВНАЯ ОТРИСОВКА
    // -----------------------------------------

    /**
     * Этот метод вызывается каждый кадр,
     * пока интерфейс открыт.
     */
    @Override
    public void render(
            GuiGraphics guiGraphics,
            int mouseX,
            int mouseY,
            float partialTick
    ) {

        /**
         * Затемняем игровой мир
         * за открытым интерфейсом.
         */
        this.renderBackground(
                guiGraphics,
                mouseX,
                mouseY,
                partialTick
        );


        /**
         * Базовая отрисовка Minecraft.
         *
         * Она показывает:
         *
         * - фон, который рисует renderBg();
         * - предметы в слотах;
         * - переносимый курсором ItemStack;
         * - подсветку слота;
         * - названия.
         */
        super.render(
                guiGraphics,
                mouseX,
                mouseY,
                partialTick
        );


        /**
         * Показываем подсказку предмета,
         * когда курсор находится над слотом.
         */
        this.renderTooltip(
                guiGraphics,
                mouseX,
                mouseY
        );
    }


    // -----------------------------------------
    // ФОН ОКНА
    // -----------------------------------------

    /**
     * Здесь рисуется PNG-текстура интерфейса.
     *
     * leftPos и topPos автоматически вычисляются
     * Minecraft, чтобы окно было по центру экрана.
     */
    @Override
    protected void renderBg(
            GuiGraphics guiGraphics,
            float partialTick,
            int mouseX,
            int mouseY
    ) {

        guiGraphics.blit(

                // Какая текстура используется.
                TEXTURE,

                // Координата X окна на экране.
                this.leftPos,

                // Координата Y окна на экране.
                this.topPos,

                // Начало области внутри PNG по X.
                0.0F,

                // Начало области внутри PNG по Y.
                0.0F,

                // Сколько пикселей рисуем по ширине.
                this.imageWidth,

                // Сколько пикселей рисуем по высоте.
                this.imageHeight,

                // Полная ширина PNG.
                TEXTURE_WIDTH,

                // Полная высота PNG.
                TEXTURE_HEIGHT
        );
    }


    // -----------------------------------------
    // НАЗВАНИЯ
    // -----------------------------------------

    /**
     * Рисуем название стола
     * и название инвентаря игрока.
     */
    @Override
    protected void renderLabels(
            GuiGraphics guiGraphics,
            int mouseX,
            int mouseY
    ) {

        /**
         * Название алхимического стола.
         *
         * Рисуем его по центру окна.
         */
        guiGraphics.drawCenteredString(

                // Minecraft-шрифт.
                this.font,

                // Название, полученное от MenuProvider.
                this.title,

                // Центр окна по X.
                this.imageWidth / 2,

                // Положение по Y.
                6,

                // Светлый цвет текста.
                0xE6D5C5
        );


        /**
         * Название инвентаря игрока.
         */
        guiGraphics.drawString(

                this.font,

                this.playerInventoryTitle,

                this.inventoryLabelX,

                this.inventoryLabelY,

                0xD8C7B6,

                // false означает:
                // не добавлять чёрную тень.
                false
        );
    }
}


