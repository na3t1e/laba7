package Server.db;

import Common.exception.DatabaseException;
import Common.util.Request;
import Common.util.Response;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Менеджер пользователей, отвечающий за регистрацию новых пользователей и авторизацию уже зарегистрированных.
 */
public class UsersManager {

    /**
     * Менеджер базы данных, с которым взаимодействует данный менеджер пользователей.
     */
    private static final DBManager dbManager = new DBManager();


    public UsersManager() {

    }

    /**
     * Регистрирует нового пользователя в базе данных.
     *
     * @param request запрос, содержащий данные пользователя для регистрации (логин и пароль)
     * @return ответ с результатом операции (успешно или неуспешно), а также данными зарегистрированного пользователя (логин)
     */
    public static Response registerUser(Request request) {
        try {
            if (!dbManager.checkUsersExistence(request.getLogin())) {
                dbManager.addUser(request.getLogin(), request.getPassword());
                ArrayList<String> userData = new ArrayList<>();
                userData.add(request.getLogin());
                userData.add(request.getPassword());
                return new Response("Регистрация успешно завершена", userData);
            } else {
                ArrayList<String> userData = new ArrayList<>();
                userData.add(request.getLogin());
                return new Response("Такое имя пользователя уже существует", userData);
            }
        } catch (SQLException | DatabaseException e) {
            ArrayList<String> userData = new ArrayList<>();
            userData.add(request.getLogin());
            return new Response(e.getMessage(), userData);
        }
    }

    /**
     * Выполняет авторизацию пользователя в системе.
     *
     * @param request запрос, содержащий данные пользователя для авторизации (логин и пароль)
     * @return ответ с результатом операции (успешно или неуспешно), а также данными авторизованного пользователя (логин)
     */
    public static Response logInUser(Request request) {
        try {
            if (dbManager.checkUsersExistence(request.getLogin())) {
                if (dbManager.validateUser(request.getLogin(), request.getPassword())) {
                    ArrayList<String> userData = new ArrayList<>();
                    userData.add(request.getLogin());
                    userData.add(request.getPassword());
                    return new Response("Вход успешно выполнен\nВведите команду ниже\nДля просмотра всех команд, введите 'help'", userData);

                } else {
                    ArrayList<String> userData = new ArrayList<>();
                    userData.add(request.getLogin());
                    return new Response("Неправильный пароль", userData);
                }
            } else {
                ArrayList<String> userData = new ArrayList<>();
                userData.add(request.getLogin());
                return new Response("Этого имени пользователя не существует", userData);
            }
        } catch (SQLException | DatabaseException e) {
            ArrayList<String> userData = new ArrayList<>();
            userData.add(request.getLogin());
            return new Response(e.getMessage(), userData);
        }
    }
}
