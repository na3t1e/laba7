package Server.commands;

import Common.entity.City;
import Common.exception.DatabaseException;
import Common.util.Request;
import Common.util.Response;
import Server.db.DBManager;
import Server.util.CollectionManager;

import java.sql.SQLException;

/**
 * Удаление первой организации из коллекции
 */
public class RemoveFirstCommand extends AbstractCommand {
    /**
     * Создает новый объект команды.
     *
     * @param collectionManager менеджер коллекции
     */
    public RemoveFirstCommand(CollectionManager collectionManager) {
        super("remove_first", "удалить первый элемент из коллекции", 0, collectionManager);

    }

    /**
     * Выполняет удаление первого элемента коллекции.
     *
     * @param request объект запроса
     * @return объект ответа
     */
    @Override
    public Response execute(Request request) {
        try {
            if (DBManager.validateUser(request.getLogin(), request.getPassword())) {
                if (collectionManager.getCollection().isEmpty())
                    return new Response("Коллекция пуста.");
                else {
                    Long firstOrganizationId = collectionManager.getCollection().getFirst().getId();
                    if (DBManager.checkOrganizationExistence(firstOrganizationId)) {
                        if (DBManager.removeById(collectionManager.getFirstId(), request.getLogin())) {
                            collectionManager.removeById(request.getNumericArgument());
                            return new Response("Элемент был удален.");
                        } else {
                            return new Response("Элемент был создан другим пользователем. У вас нет прав для его обновления.");
                        }
                    } else {
                        return new Response("Нет элемента с таким идентификатором.");
                    }
                }
            } else {
                return new Response("Несоответствие логина и пароля.");
            }
        } catch (DatabaseException | SQLException e) {
            return new Response(e.getMessage());
        }
    }
}
