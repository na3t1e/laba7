package Server.requestHandlers;

import Common.util.Request;
import Common.util.RequestType;
import Common.util.Response;
import Server.Server;
import Server.ServerApp;
import Server.db.UsersManager;

import java.util.function.Function;

/**
 * Класс-обработчик запросов клиентов, реализующий интерфейс Function Request, Response.
 */
public class RequestExecutor implements Function<Request, Response> {

    /**
     * Обработчик запросов.
     *
     * @param request запрос клиента.
     * @return ответ сервера на запрос клиента.
     */
    @Override
    public Response apply(Request request) {
        if (request.getLogin() != null && request.getPassword() != null) {
            if (request.getType().equals(RequestType.REGISTER)) {
                return UsersManager.registerUser(request);
            } else if (request.getType().equals(RequestType.LOGIN)) return UsersManager.logInUser(request);
        }

        if (request.getType().equals(RequestType.COMMAND)) {
            return ServerApp.commandManager.initCommand(request).execute(request);
        } else return null;
    }
}
