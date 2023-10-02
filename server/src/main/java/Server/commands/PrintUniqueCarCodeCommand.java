package Server.commands;

import Common.util.Request;
import Common.util.Response;
import Server.util.CollectionManager;


public class PrintUniqueCarCodeCommand extends AbstractCommand {
    /**
     Менеджер коллекции.
     */
    private final CollectionManager collectionManager;

    /**
     * Создает новый объект команды.
     * @param collectionManager менеджер коллекции
     */
    public PrintUniqueCarCodeCommand(CollectionManager collectionManager) {
        super("print_unique_car_code", "вывести уникальные значения поля carCode всех элементов в коллекции", 0);
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        if (collectionManager.getCollection().isEmpty())
            return new Response("Коллекция пуста.");
        else {
            return new Response("Уникальные значения поля carCode: " + collectionManager.getCarCodes());
        }
    }
}
