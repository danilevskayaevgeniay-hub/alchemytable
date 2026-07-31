package ru.azazel.alchemytable.menu;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import ru.azazel.alchemytable.AzazelSAlchemyTable;

/**
 * Этот класс регистрирует типы меню нашего мода.
 *
 * Minecraft должен заранее знать, что существует
 * интерфейс алхимического стола.
 */
public final class ModMenuTypes {

    /**
     * Зарегистрированный тип меню алхимического стола.
     *
     * AlchemyTableMenu - класс, который описывает слоты.
     * "alchemy_table" - техническое имя меню.
     */
    public static final MenuType<AlchemyTableMenu>
            ALCHEMY_TABLE_MENU =
            register(
                    "alchemy_table",
                    AlchemyTableMenu::new
            );


    /**
     * Универсальный метод регистрации меню.
     *
     * name - техническое имя.
     * constructor - ссылка на конструктор меню.
     */
    private static <
            T extends AbstractContainerMenu
            > MenuType<T> register(
                    String name,
                    MenuType.MenuSupplier<T> constructor
    ) {

        return Registry.register(

                // Реестр, в котором Minecraft хранит типы меню.
                BuiltInRegistries.MENU,

                // Полный технический адрес:
                // azazels-alchemy-table:alchemy_table
                ResourceLocation.fromNamespaceAndPath(
                        AzazelSAlchemyTable.MOD_ID,
                        name
                ),

                // Создаём новый тип меню.
                new MenuType<>(
                        constructor,
                        FeatureFlagSet.of()
                )
        );
    }


    /**
     * Этот метод вызывается из главного класса мода.
     *
     * Сам код регистрации выполняется при загрузке
     * статического поля ALCHEMY_TABLE_MENU.
     */
    public static void registerModMenuTypes() {

        AzazelSAlchemyTable.LOGGER.info(
                "Registering menu types for {}",
                AzazelSAlchemyTable.MOD_ID
        );
    }


    /**
     * Запрещаем случайно создавать объект этого класса.
     */
    private ModMenuTypes() {
    }
}


