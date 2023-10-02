package Server.commands;

import Common.util.Request;
import Common.util.Response;
import Server.util.CollectionManager;


public class PrintFieldDescendingAreaCommand extends AbstractCommand {
    /**
     Менеджер коллекции.
     */
    private final CollectionManager collectionManager;

    /**
     * Создает новый объект команды.
     * @param collectionManager менеджер коллекции
     */
    public PrintFieldDescendingAreaCommand(CollectionManager collectionManager) {
        super("print_field_descending_area", "вывести значения поля area всех элементов в порядке убывания", 0);
        this.collectionManager = collectionManager;
    }


    @Override
    public Response execute(Request request) {
        if (collectionManager.getCollection().isEmpty())
            return new Response("Коллекция пуста");
        else {
            return new Response("Все значения поля area в порядке убывания: " + collectionManager.getListArea());
        }
    }
}
