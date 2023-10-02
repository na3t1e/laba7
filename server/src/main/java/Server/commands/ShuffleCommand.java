package Server.commands;

import Common.util.Request;
import Common.util.Response;
import Server.util.CollectionManager;

/**
 * Класс ShuffleCommand случайным образом перемешивает элементы коллекции
 */
public class ShuffleCommand extends AbstractCommand {


    /**
     * Создает новый объект команды.
     *
     * @param collectionManager менеджер коллекции
     */
    public ShuffleCommand(CollectionManager collectionManager) {
        super("shuffle", "перемешать элементы коллекции в случайном порядке", 0, collectionManager);
    }

    /**
     * Перемешивает элементы коллекции
     *
     * @param request объект запроса
     * @return объект ответа
     */
    @Override
    public Response execute(Request request) {
        if (collectionManager.getCollection().isEmpty())
            return new Response("Коллекция пуста");
        else {
            collectionManager.shuffleCollection();
            return new Response("Коллекция перемешана");
        }
    }
}