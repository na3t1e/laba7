package Server.commands;

import Common.entity.City;
import Common.exception.DatabaseException;
import Common.util.Request;
import Common.util.Response;
import Server.db.DBManager;
import Server.util.CollectionManager;

import java.sql.SQLException;

/**
 * Команда для удаления элемента коллекции по его id.
 */
public class RemoveByIdCommand extends AbstractCommand {

    /**
     * Создает новый объект команды.
     *
     * @param collectionManager менеджер коллекции
     */
    public RemoveByIdCommand(CollectionManager collectionManager) {
        super("remove_by_id", "удалить элемент из коллекции по его id", 1, collectionManager);

    }

    /**
     * Выполняет удаление элемента коллекции по его id.
     * @param request запрос, содержащий id элемента для удаления
     * @return ответ с информацией об успешности выполнения операции
     */
    @Override
    public Response execute(Request request) {
        try {
            if (DBManager.validateUser(request.getLogin(), request.getPassword())) {
                if (DBManager.checkOrganizationExistence(request.getNumericArgument())) {
                    if (DBManager.removeById(request.getNumericArgument(), request.getLogin())) {
                        collectionManager.removeById(request.getNumericArgument());
                        return new Response("Элемент с ИД " + request.getNumericArgument() + " был удален из коллекции");
                    } else {
                        return new Response("Элемент был создан другим пользователем. У вас нет прав для его обновления");
                    }
                } else {
                    return new Response("Нет элемента с таким идентификатором");
                }
            } else {
                return new Response("Несоответствие логина и пароля");
            }
        } catch (DatabaseException | SQLException e) {
            return new Response(e.getMessage());
        }
    }
}