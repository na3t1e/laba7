package Server.commands;

import Common.util.Request;
import Common.util.Response;
import Server.db.DBManager;
import Server.util.CollectionManager;

/**
 * Класс SortCommand случайным образом перемешивает элементы коллекции
 */
public class SortCommand extends AbstractCommand {


    /**c
     * Создает новый объект команды.
     *
     * @param collectionManager менеджер коллекции
     */
    public SortCommand(CollectionManager collectionManager) {
        super("sort", "отсортировать коллекцию в естественном порядке", 0,  collectionManager);
    }

    /**
     * Сортирует элементы коллекции
     *
     * @param request объект запроса
     * @return объект ответа
     */
    @Override
    public Response execute(Request request) {
        if (collectionManager.getCollection().isEmpty())
            return new Response("Коллекция пуста.");
        else {
            collectionManager.sortCollection();
            return new Response("Коллекция отсортирована.");
        }
    }
}
