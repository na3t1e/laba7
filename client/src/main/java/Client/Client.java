package Client;

import Client.util.AuthorizationModule;
import Client.util.ScriptReader;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.*;

/**
 * Главный класс, запускающий приложение клиента.
 * Содержит метод main, отвечающий за запуск приложения.
 */
public final class Client {

    /**
     * Порт по умолчанию
     */
    private static int PORT;

    /**
     * Хост, к которому пытается подключиться клиент.
     */
    private static String HOST = "localhost";

    /**
     * Максимально возможный номер порта.
     */
    private static final int maxPort = 65535;

    /**
     * Сканнер для считывания ввода с консоли.
     */
    private static final Scanner SCANNER = new Scanner(System.in);

    /**
     * Селектор, используемый для отслеживания событий в сокете.
     */
    private static Selector selector;
    static final AuthorizationModule authorizationModule = new AuthorizationModule(SCANNER);
    private static SocketChannel clientChannel;

    /**
     * Режим переподключения, установленный в режим ожидания.
     */
    private static boolean reconnectionMode = false;

    /**
     * Количество неудачных попыток подключения к серверу.
     */
    private static int attempts = 0;

    public static void main(String[] args) {
        try {
            if (!reconnectionMode) {
                try {
                    checkPort(args[0]);
                } catch (ArrayIndexOutOfBoundsException ex) {
                    System.err.println("Не введен порт");
                    System.exit(1);
                }
            } else {
                Thread.sleep(5000); // 5 секунд на переподключение
                clientChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
            }
            attempts = 0;
            clientChannel.configureBlocking(false);
            selector = Selector.open();
            clientChannel.register(selector, SelectionKey.OP_WRITE);
            System.out.println("Успешное подключение");
            ClientApp.startSelectorLoop(selector, clientChannel, SCANNER, false);
        } catch (ClassNotFoundException e) {
            System.err.println("Не удалось сериализовать данные");
        } catch (InterruptedException e) {
            System.err.println("Соединение было прервано из-за бездействия");
        } catch (IOException e) {
            System.err.println("Сервер недоступен. Переподключение, попытка №" + (attempts + 1));
            reconnectionMode = true;
            if (attempts == 4) {
                System.err.println("Не удалось подключиться. Попробуйте позднее");
                System.exit(1);
            }
            attempts++;
            ScriptReader.callStack.clear();
            main(args);
        } catch (NoSuchElementException e) {
            System.err.println("Принудительное завершение работы.");
            System.exit(1);
        }
    }

    /**
     * Метод checkPort проверяет корректность введенного порта
     */
    private static void checkPort(String port) {
        try {
            int portInt = Integer.parseInt(port);
            if (portInt > 0 && portInt <= maxPort) {
                PORT = portInt;
                clientChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
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
        } catch (IOException e) {
            System.out.println("Сервер недоступен");
            System.exit(1);
//            throw new RuntimeException(e);
        }
    }
}