package Server.commands;

import Common.entity.City;
import Common.exception.DatabaseException;
import Common.util.Request;
import Common.util.Response;
import Server.db.DBManager;
import Server.util.CollectionManager;

import java.sql.SQLException;


public class AddCommand extends AbstractCommand {
    /**
     * Менеджер коллекции.
     */
    private final CollectionManager collectionManager;

    /**
     * Создает новый объект команды.
     *
     * @param collectionManager менеджер коллекции
     */
    public AddCommand(CollectionManager collectionManager) {
        super("add", "добавить новый элемент в коллекцию", 0);
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду добавления элемента в коллекцию.
     *
     * @param request объект запроса
     * @return объект ответа
     */
    @Override
    public Response execute(Request request) {
        try {
            if (DBManager.validateUser(request.getLogin(), request.getPassword())) {
                City cityToAdd = request.getCity();
                Long id = DBManager.addElement(cityToAdd, request.getLogin());
                cityToAdd.setId(id);
                collectionManager.addToCollection(cityToAdd);
                return new Response("Элемент был успешно добавлен с ИД: " + id);
            } else {
                return new Response("Несоответствие логина и пароля");
            }
        } catch (DatabaseException | SQLException e) {
            return new Response(e.getMessage());
        }
    }
}
