package Server.commands;

import Common.exception.DatabaseException;
import Common.util.Request;
import Common.util.Response;
import Server.db.DBManager;
import Server.util.CollectionManager;

import java.sql.SQLException;

/**
 * Команда очистки коллекции.
 */
public class ClearCommand extends AbstractCommand {
    /**
     * Менеджер коллекции.
     */
    private final CollectionManager collectionManager;

    /**
     * Создает новый объект команды.
     *
     * @param collectionManager менеджер коллекции
     */
    public ClearCommand(CollectionManager collectionManager) {
        super("clear", "очистить коллекцию", 0);
        this.collectionManager = collectionManager;
    }

    /**
     * Очищает коллекцию, если она не пуста.
     *
     * @param request объект запроса
     * @return объект Response с сообщением об успешном выполнении или ошибке, если коллекция пуста
     */
    @Override
    public Response execute(Request request) {
        if (collectionManager.getCollection().isEmpty())
            return new Response("Коллекция пуста");
        else {
            try {
                DBManager.clear(request.getLogin());
                collectionManager.clearCollection();
                collectionManager.setCityCollection(DBManager.loadCollection());
            } catch (DatabaseException | SQLException e) {
                throw new RuntimeException(e);
            }
            return new Response("Коллекция очищена");
        }
    }
}
