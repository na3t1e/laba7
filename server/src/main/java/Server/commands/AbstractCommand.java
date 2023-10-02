package Server.commands;

import Common.util.Request;
import Common.util.Response;
import Server.db.DBManager;
import Server.util.CollectionManager;

/**
 * Абстрактный класс для команд
 */
public abstract class AbstractCommand {

    /**
     * Название команды
     */
    private final String name;

    /**
     * Описание команды
     */
    private final String description;

    /**
     * Количество аргументов у команды
     */
    private final int amountOfArgs;
    /**
     * Менеджер коллекции
     */
    protected CollectionManager collectionManager;


    /**
     * Конструктор абстрактного класса команды
     *
     * @param name         название команды
     * @param description  описание команды
     * @param amountOfArgs количество аргументов у команды
     */
    public AbstractCommand(String name, String description, int amountOfArgs) {
        this.name = name;
        this.description = description;
        this.amountOfArgs = amountOfArgs;
    }

    /**
     * Конструктор абстрактного класса команды
     *
     * @param name              название команды
     * @param description       описание команды
     * @param amountOfArgs      количество аргументов у команды
     * @param collectionManager менеджер коллекции
     */
    public AbstractCommand(String name, String description, int amountOfArgs, CollectionManager collectionManager) {
        this.name = name;
        this.description = description;
        this.amountOfArgs = amountOfArgs;
        this.collectionManager = collectionManager;

    }

    /**
     * Метод для выполнения команды
     *
     * @param request запрос пользователя
     * @return ответ сервера
     */
    public abstract Response execute(Request request);


    /**
     * Получает название команды
     *
     * @return название команды
     */
    public String getName() {
        return name;
    }

    public CollectionManager getCollectionManager() {
        return collectionManager;
    }



    /**
     * Переопределенный метод toString() для класса AbstractCommand
     *
     * @return строковое представление объекта AbstractCommand
     */
    @Override
    public String toString() {
        return name + ": " + description;
    }
}
