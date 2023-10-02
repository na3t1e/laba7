package Server;

import Common.exception.DatabaseException;
import Server.db.DBConnector;
import Server.db.DBManager;

import java.io.*;
import java.sql.SQLException;
import java.util.NoSuchElementException;

/**
 * Главный класс, запускающий приложение сервера.
 * Содержит метод main, отвечающий за запуск приложения.
 */
public final class Server {
    /**
     * Порт по умолчанию
     */
    private static int PORT;

    /**
     * Максимально возможный номер порта.
     */
    private static final int maxPort = 65535;
    /**
     * Объект, который используется для прослушивания входящих клиентских подключений на сервере.
     */

    public static DBConnector dbConnector;

    static {
        dbConnector = new DBConnector();
    }


    public static void main(String[] args) {
        try {
            if (args.length > 1) System.out.println("Введены лишние аргументы");
            Config config = new Config();
            checkPort(args[0]);
            ServerApp.collectionManager.setCityCollection(DBManager.loadCollection());
            config.consoleThread().start();
            config.app().start();
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.err.println("Не введен порт или введен некорректно");
            System.exit(1);
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (SQLException | DatabaseException e) {
            System.err.println("Ошибка загрузки данных коллекции: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }


    /**
     * Метод checkPort пытается установить соединение
     */

    private static void checkPort(String port) {
        try {
            int portInt = Integer.parseInt(port);
            if (portInt > 0 && portInt <= maxPort) {
                PORT = portInt;
            } else {
                System.err.println("Порт не входит в возможный диапазон");
                System.exit(1);
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Порт введен некорректно");
            System.exit(1);
        } catch (NoSuchElementException e) {
            System.err.println("Принудительное завершение работы");
            System.exit(1);
        }
    }

    public static int getPORT() {
        return PORT;
    }
}
