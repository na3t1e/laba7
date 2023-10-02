package Server;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

import Common.util.Request;
import Common.util.Response;
import Server.commands.*;
import Server.requestHandlers.RequestExecutor;
import Server.requestHandlers.RequestReader;
import Server.requestHandlers.ResponseSender;
import Server.util.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Класс для установки соединения с клиентами.
 */
public class ServerApp {
    /**
     * Сканер для чтения ввода.
     */
    public static final Scanner scanner = new Scanner(System.in);

    /**
     * Менеджер коллекции.
     */
    public static CollectionManager collectionManager = new CollectionManager();

    /**
     * Состояние сервера.
     */
    public static boolean running = true;

    /**
     * Селектор для мониторинга каналов ввода-вывода.
     */
    private volatile Selector selector;
    /**
     * Пул потоков для чтения запросов от клиентов.
     */
    private final ExecutorService fixedService1;
    /**
     * Пул потоков для обработки запросов от клиентов.
     */
    private final ExecutorService fixedService2;
    /**
     * Пул потоков для отправки ответов клиентам.
     */
    private final ExecutorService fixedService3;
    /**
     * Множество ключей, которые в данный момент обрабатываются.
     */
    private final Set<SelectionKey> workingKeys =
            Collections.synchronizedSet(new HashSet<>());
    /**
     * Серверный канал для прослушивания входящих соединений.
     */
    private final ServerSocketChannel server;

    /**
     * Командный менеджер, содержащий список команд для обработки запросов клиентов.
     */
    public static CommandManager commandManager = new CommandManager(
            new AddCommand(collectionManager),
            new ClearCommand(collectionManager),
            new ExecuteScriptCommand(),
            new ExitCommand(),
            new HelpCommand(),
            new InfoCommand(collectionManager),
            new PrintDescendingCommand(collectionManager),
            new PrintFieldDescendingAreaCommand(collectionManager),
            new PrintUniqueCarCodeCommand(collectionManager),
            new RemoveByIdCommand(collectionManager),
            new RemoveFirstCommand(collectionManager),
            new ShowCommand(collectionManager),
            new ShuffleCommand(collectionManager),
            new SortCommand(collectionManager),
            new UpdateByIdCommand(collectionManager)
    );

    /**
     * Конструктор для создания объекта сервера
     *
     * @param server        серверный канал для прослушивания входящих соединений
     * @param fixedService1  пул потоков для чтения запросов от клиентов
     * @param fixedService2 пул потоков для обработки запросов от клиентов
     * @param fixedService3 пул потоков для отправки ответов клиентам
     */

    public ServerApp(ServerSocketChannel server,
                     ExecutorService fixedService1,
                     ExecutorService fixedService2,
                     ExecutorService fixedService3) {
        this.server = server;
        this.fixedService1 = fixedService1;
        this.fixedService2 = fixedService2;
        this.fixedService3 = fixedService3;
    }

    /**
     * Метод для запуска сервера и инициализации селектора.
     */
    private void startServer() {
        System.out.println("Сервер запущен");
        try {
            selector = Selector.open();
            ServerSocketChannel server = initChannel(selector);
            startSelectorLoop(server);
        } catch (BindException e) {
            e.printStackTrace();
            System.err.println("Проблемы с IO");
            toggleStatus();
        } catch (ClassNotFoundException e) {
            System.err.println("Попытка сериализовать несериализуемый объект");
            toggleStatus();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод для запуска сервера и обработки запросов от клиентов
     */
    public void start() {
        if (running) {
            startServer();
        }
        fixedService1.shutdown();
        fixedService2.shutdown();
        fixedService3.shutdown();
    }

    /**
     * Метод для запуска цикла селектора, который прослушивает подключения.
     *
     * @param channel канал серверного сокета, который будет слушать подключения.
     * @throws IOException            если произошла ошибка при работе с каналом.
     * @throws ClassNotFoundException если не найден класс.
     */
    private void startSelectorLoop(ServerSocketChannel channel) throws IOException, ClassNotFoundException {
        while (channel.isOpen() && running) {
            if (selector.select(1) != 0) {
                startIteratorLoop(channel);
            }
        }
    }

    /**
     * Метод для запуска цикла итератора, который обрабатывает готовые к обработке ключи селектора.
     *
     * @param channel канал серверного сокета, который будет слушать подключения.
     * @throws IOException если произошла ошибка при работе с каналом.
     */
    private void startIteratorLoop(ServerSocketChannel channel) throws IOException {
        Set<SelectionKey> readyKeys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = readyKeys.iterator();
        while (iterator.hasNext()) {
            SelectionKey key = iterator.next();
            iterator.remove();
            if (key.isValid() && !workingKeys.contains(key)) {
                if (key.isAcceptable()) {
                    accept(channel);
                } else if (key.isReadable()) {
                    workingKeys.add(key);
                    System.out.println("Клиент " + ((SocketChannel) key.channel()).getLocalAddress() + " прислал сообщение");
                    Supplier<Request> requestReader = new RequestReader(key);
                    Function<Request, Response> requestExecutor = new RequestExecutor();
                    Consumer<Response> responseSender = new ResponseSender(key, workingKeys);
                    CompletableFuture
                            .supplyAsync(requestReader, fixedService1)
                            .thenApplyAsync(requestExecutor, fixedService2)
                            .thenAcceptAsync(responseSender, fixedService3);
                }
            }
        }
    }

    /**
     * Метод для принятия входящих подключений.
     *
     * @param channel канал серверного сокета, который будет слушать подключения.
     * @throws IOException если произошла ошибка при работе с каналом.
     */
    private void accept(ServerSocketChannel channel) throws IOException {
        SocketChannel socketChannel = channel.accept();
        System.out.println("Сервер получил соединение от " + socketChannel.getLocalAddress());
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
    }

    /**
     * Метод для инициализации серверного канала.
     *
     * @param selector селектор, который будет использоваться для регистрации канала и слушания событий.
     * @return инициализированный серверный канал.
     * @throws IOException если произошла ошибка при работе с каналом.
     */
    private ServerSocketChannel initChannel(Selector selector) throws IOException {
        server.configureBlocking(false);
        server.register(selector, SelectionKey.OP_ACCEPT);
        server.socket().bind(new InetSocketAddress(Server.getPORT()));
        return server;
    }

    public static void toggleStatus() {
        running = !running;
    }
}
