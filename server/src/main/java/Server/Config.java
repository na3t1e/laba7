package Server;

import Server.util.ConsoleThread;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Config {

    public static ServerSocketChannel serverSocketChannel() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        return serverSocketChannel;
    }

    public ServerApp app() throws IOException {
        return new ServerApp(serverSocketChannel(), executorService1(), executorService2(), executorService3());
    }

    /**
     * Создает экземпляр ExecutorService для выполнения задач на сервере.
     *
     * @return экземпляр ExecutorService
     */

    public static ExecutorService executorService1() {
        return Executors.newFixedThreadPool(10);
    }

    /**
     * Создает экземпляр ExecutorService для выполнения задач на сервере.
     *
     * @return экземпляр ExecutorService
     */

    public static ExecutorService executorService2() {
        return Executors.newFixedThreadPool(10);
    }

    /**
     * Создает экземпляр ExecutorService для выполнения задач на сервере.
     *
     * @return экземпляр ExecutorService
     */

    public static ExecutorService executorService3() {
        return Executors.newFixedThreadPool(10);
    }

    /**
     * Создает экземпляр ConsoleThread.
     *
     * @return экземпляр ConsoleThread
     */

    public ConsoleThread consoleThread() {
        return new ConsoleThread();
    }

}
