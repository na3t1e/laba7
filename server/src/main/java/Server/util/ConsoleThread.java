package Server.util;

import Common.entity.City;
import Common.exception.DatabaseException;
import Server.ServerApp;
import Server.db.DBManager;

import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Класс, представляющий поток консоли сервера.
 */
public class ConsoleThread extends Thread {

    /**
     * Сканнер, считывающий ввод пользователя.
     */
    private static final Scanner scanner = ServerApp.scanner;

    /**
     * Состояние запущенного потока.
     */
    public volatile boolean running = true;

    /**
     * Запускает поток консоли сервера.
     */
    @Override
    public void run() {
        try {
            while (running) {
                String line = scanner.nextLine();
                if ("exit".equalsIgnoreCase(line)) {
                    System.out.println("Работа сервера завершена");
                    System.out.println("До свидания!");
                    System.exit(0);
                } else {
                    System.err.println("Введенной команды не существует. Доступна только 'exit'");
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("Принудительное завершение работы");
            System.out.println("Работа сервера завершена");
            System.exit(1);
        } catch (IndexOutOfBoundsException e) {
            System.err.println(e.getMessage());
        }
    }
}
