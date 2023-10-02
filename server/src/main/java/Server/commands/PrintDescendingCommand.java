package Server.commands;

import Common.exception.DatabaseException;
import Common.util.Request;
import Common.util.Response;
import Server.db.DBManager;
import Server.util.CollectionManager;

import java.sql.SQLException;

/**
 * Команда, выводящая элементы коллекции в порядке убывания.
 */
public class PrintDescendingCommand extends AbstractCommand {

    /**
     * Создает новый объект команды.
     *
     * @param collectionManager менеджер коллекции
     */
    public PrintDescendingCommand(CollectionManager collectionManager ) {
        super("print_descending", "вывести элементы коллекции в порядке убывания", 0, collectionManager);
    }

    /**
     * Выполнение команды вывода элементов в порядке убывания.
     *
     * @param request объект запроса
     * @return объект типа Response с результатом выполнения команды
     */
    @Override
    public Response execute(Request request) {
        try {
            if (DBManager.validateUser(request.getLogin(), request.getPassword())) {
                if (collectionManager.getCollection().isEmpty()) {
                    return new Response("Коллекция пуста");
                } else {
                    return new Response("Коллекция была отсортирована в обратном порядке",
                            collectionManager.getDescending());
                }
            } else {
                return new Response("Несоответствие логина и пароля");
            }

        } catch (DatabaseException | SQLException e) {
            return new Response(e.getMessage());
        }
    }
}
