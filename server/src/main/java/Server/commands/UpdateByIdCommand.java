package Server.commands;

import Common.entity.City;
import Common.exception.DatabaseException;
import Common.util.Request;
import Common.util.Response;
import Server.db.DBManager;
import Server.util.CollectionManager;

import java.sql.SQLException;

/**
 * Команда для обновления значения элемента коллекции, id которого равен заданному.
 */
public class UpdateByIdCommand extends AbstractCommand {

    /**
     * Создает новый объект команды.
     *
     * @param collectionManager менеджер коллекции
     */
    public UpdateByIdCommand(CollectionManager collectionManager) {
        super("update", "обновить значение элемента коллекции, id которого равен заданному", 1, collectionManager);
    }

    /**
     * Метод, который выполняет команду.
     *
     * @param request объект запроса
     * @return объект ответа
     */
    @Override
    public Response execute(Request request) {
        try {
            if (DBManager.validateUser(request.getLogin(), request.getPassword())) {
                Long id = request.getNumericArgument();
                if (DBManager.checkOrganizationExistence(id)) {
                    if (DBManager.updateById(request.getCity(), id, request.getLogin())) {
                        City updatedOrganization = request.getCity();
                        updatedOrganization.setId(id);
                        collectionManager.removeById(id);
                        collectionManager.addToCollection(request.getCity());
                        return new Response("Элемент с ИД " + id + " был успешно обновлен");
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
