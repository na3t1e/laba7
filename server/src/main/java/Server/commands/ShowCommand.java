package Server.commands;

import Common.exception.DatabaseException;
import Common.util.Request;
import Common.util.Response;
import Server.db.DBManager;
import Server.util.CollectionManager;
import Server.util.Encryptor;

import java.sql.SQLException;
import java.util.List;

/**
 * Команда, которая выводит все элементы коллекции.
 */
public class ShowCommand extends AbstractCommand {

    public ShowCommand(CollectionManager collectionManager) {
        super("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении", 0, collectionManager);
    }

    /**
     * Метод, который выполняет команду.
     *
     * @param request объект запроса
     * @return строку, содержащую все элементы коллекции.
     */
    @Override
    public Response execute(Request request) {
        if (collectionManager.getCollection().isEmpty())
            return new Response("Коллекция пуста");
        try {
            if (DBManager.validateUser(request.getLogin(), request.getPassword())) {
                List<Long> ids = DBManager.getIdsOfUsersElements(request.getLogin());
                return new Response("Элементы коллекции:",
                        collectionManager.getUsersElements(ids),
                        collectionManager.getAlienElements(ids));
            } else {
                return new Response("Несоответствие логина и пароля");
            }

        } catch (DatabaseException | SQLException e) {
            return new Response(e.getMessage());
        }
    }
}

