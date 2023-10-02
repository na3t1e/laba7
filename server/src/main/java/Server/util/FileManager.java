package Server.util;

import Common.entity.*;
import Server.ServerApp;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.StreamException;
import com.thoughtworks.xstream.security.NoTypePermission;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Класс FileManager, который отвечает за запись коллекции в файл и чтение из файла.
 */
public class FileManager {


    /**
     * Объект XStream для преобразования коллекции в XML и обратно
     */
    private final XStream xstream;

    /**
     * Создает новый экземпляр класса FileManager с указанным именем файла и настройками XStream.
     * Задает псевдонимы для классов, устанавливает настройки без ссылок и безопасности и
     * разрешает использование типа List и String.
     *
     * @param fileName имя файла
     */
    public FileManager(String fileName) {
        this.path = fileName;
        xstream = new XStream();

        xstream.alias("human", Human.class); // используется для установки псевдонимов для классов, которые будут использоваться при преобразовании объекта в поток байтов для сохранения в файл XML
        xstream.alias("coordinates", Coordinates.class);
        xstream.alias("climate", Climate.class);
        xstream.alias("city", City.class);
        xstream.addImplicitCollection(CollectionManager.class, "cityCollection"); // используется для определения, какая коллекция будет использоваться для хранения списка организаций в классе CollectionManager

        xstream.setMode(XStream.NO_REFERENCES); // Устанавливает режим NO_REFERENCES, который гарантирует, что при сериализации не будут использоваться ссылки на другие объекты, т.е. каждый объект будет сериализован в своем собственном контексте
        xstream.addPermission(NoTypePermission.NONE); // запрещает сериализацию или десериализацию объектов, не имеющих типа
        xstream.addPermission(NullPermission.NULL); // позволяет сериализовать и десериализовать null значения
        xstream.addPermission(PrimitiveTypePermission.PRIMITIVES); // разрешены только примитивные типы данных и списки
        xstream.allowTypeHierarchy(List.class); // разрешает использование иерархии типов для объектов, которые реализуют интерфейс List
        xstream.allowTypeHierarchy(String.class); // разрешает использование иерархии типов для объектов типа String
        xstream.ignoreUnknownElements(); // используется для игнорирования неизвестных элементов при десериализации
    }


    /**
     * Записывает коллекцию в файл в формате XML.
     *
     * @param collection коллекция для записи в файл
     */
    public void writeCollection(LinkedList<City> collection) {
        if (!path.equals("")) {
            try {
                FileOutputStream file = new FileOutputStream(path);
                BufferedOutputStream collectionFileWriter = new BufferedOutputStream(file);
                String xml = xstream.toXML(new ArrayList<>(collection)); // сериализуется список элементов коллекции в формат XML и сохраняется в строковую переменную xml
                byte[] buffer = xml.getBytes(); // создается массив байтов buffer из строки xml
                collectionFileWriter.write(buffer, 0, buffer.length);
                collectionFileWriter.flush(); // все накопленные данные записываются в файл
                System.out.println("Коллекция сохранена");
            } catch (IOException exception) {
                System.err.println("Файл не может быть открыт или является директорией");
            }
        } else System.err.println("Файл поврежден или ошибка в названии");
    }

    /**
     * Метод для чтения коллекции организаций из файла.
     */
    public void readCollection() {
        if (!path.isEmpty()) {
            try (Scanner collectionFileScanner = new Scanner(new File(path))) {
                xstream.setMode(XStream.NO_REFERENCES);
                xstream.addPermission(NoTypePermission.NONE);
                xstream.addPermission(NullPermission.NULL);
                xstream.addPermission(PrimitiveTypePermission.PRIMITIVES);
                xstream.allowTypeHierarchy(List.class);
                xstream.allowTypeHierarchy(String.class);
                xstream.ignoreUnknownElements();
                xstream.allowTypes(new Class[]{ArrayList.class, City.class});
                StringBuilder xml = new StringBuilder();
                while (collectionFileScanner.hasNext()) xml.append(collectionFileScanner.nextLine());
                List<City> list = (List<City>) xstream.fromXML(xml.toString());
                Validator validator = new Validator(list);
                ConcurrentLinkedDeque<City> cityLinkedList = new ConcurrentLinkedDeque<>(validator.validate());
//                cityLinkedList.sort(Comparator.comparing(City::getName, String.CASE_INSENSITIVE_ORDER));
                ServerApp.collectionManager.setCityCollection(cityLinkedList);
            } catch (StreamException exception) {
                System.out.println("EOF ошибка, но файл обработан.\n");
            } catch (FileNotFoundException exception) {
                System.err.println("Файл не найден или доступ запрещен.Работа сервера завершена");
                System.exit(1);
            } catch (NoSuchElementException exception) {
                System.err.println("Файл пуст. Работа сервера завершена");
                System.exit(1);
            } catch (NullPointerException exception) {
                System.err.println("Неверный формат коллекции в файле. Работа сервера завершена");
                System.exit(1);
            } catch (IllegalStateException exception) {
                System.err.println("Непредвиденная ошибка. Работа сервера завершена");
                System.exit(1);
            }
        } else {
            System.err.println("Файл поврежден или ошибка в названии. Работа сервера завершена");
            System.exit(1);
        }
    }

    /**
     * Переменная, в которой хранится путь к папке с файлами.
     */
    public static String path;

    /**
     * Метод для получения имени файла из переменной окружения.
     *
     * @return имя файла
     */
    public static String checkFileName(String fileName) {
        try {
            path = fileName;
            String[] checkPaths = path.split(";");
            if (checkPaths.length > 1) {
                System.err.println("В этой переменной содержится более одного пути к файлам. Работа сервера завершена");
                System.exit(1);
            }
        } catch (NullPointerException e) {
            System.err.println("Введено некорректное название файла. Работа сервера завершена");
            System.exit(1);
        }
        File name = new File(path);
        return name.getName();
    }
}
