package Server.db;

import Common.entity.*;
import Common.exception.DatabaseException;
import Server.interfaces.DBConnectable;
import Server.util.Encryptor;

import java.sql.*;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;


/**
 * DBManager - класс, предоставляющий методы для работы с базой данных.
 */
public class DBManager {
    /**
     * Класс, предоставляющий подключение к базе данных.
     */

    private static final DBConnectable dbConnector = new DBConnector();

//    private final DBConnectable dbConnector;

    //    /**
//     * Конструктор класса DBManager.
//     *
//     * @param dbConnector объект, предоставляющий подключение к базе данных.
//*/
    public DBManager() {
    }

    /**
     * Метод для загрузки коллекции организаций из базы данных.
     *
     * @return объект ConcurrentLinkedDeque с загруженной коллекцией.
     * @throws DatabaseException если произошла ошибка при обращении к базе данных.
     */
    public static ConcurrentLinkedDeque<City> loadCollection() throws DatabaseException, SQLException {
        try (Connection connection = DBConnector.getConnection()) {
            String selectCollectionQuery = "SELECT * FROM cities";
            Statement statement = connection.createStatement();
            ResultSet collectionSet = statement.executeQuery(selectCollectionQuery);
            ConcurrentLinkedDeque<City> resultDeque = new ConcurrentLinkedDeque<>();
            while (collectionSet.next()) {
                Coordinates coordinates = new Coordinates(
                        collectionSet.getFloat("x"),
                        collectionSet.getDouble("y")
                );
                Climate climate = Climate.valueOf(collectionSet.getString("climate"));
                Human governor = new Human(collectionSet.getFloat("height"));
                City city = new City(
                        collectionSet.getLong("id"),
                        collectionSet.getString("name"),
                        coordinates,
                        collectionSet.getDate("creation_date").toLocalDate().atStartOfDay(ZoneId.of("Europe/Moscow")),
                        collectionSet.getDouble("area"),
                        collectionSet.getLong("population"),
                        collectionSet.getFloat("meters_above_sea_level"),
                        collectionSet.getInt("telephone_code"),
                        collectionSet.getInt("car_code"),
                        climate,
                        governor
                );
                resultDeque.add(city);
            }
            return resultDeque;
        }
    }


    public static Long addElement(City city, String username) throws DatabaseException, SQLException {
        try (Connection connection = DBConnector.getConnection()) {
            String addElementQuery = "INSERT INTO cities "
                    + "(name, x, y, creation_date, area, population, meters_above_sea_level, telephone_code, car_code, climate, height, owner_id) "
                    + "SELECT ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?, id "
                    + "FROM users "
                    + "WHERE users.login = ?;";

            PreparedStatement preparedStatement = connection.prepareStatement(addElementQuery,
                    Statement.RETURN_GENERATED_KEYS);
            Human governor = city.getGovernor();
            preparedStatement.setString(1, city.getName());
            preparedStatement.setFloat(2, city.getCoordinates().getX());
            preparedStatement.setDouble(3, city.getCoordinates().getY());
            preparedStatement.setDate(4, Date.valueOf(city.getCreationDate().toLocalDate()));
            preparedStatement.setDouble(5, city.getArea());
            preparedStatement.setFloat(6, city.getPopulation());
            preparedStatement.setFloat(7, city.getMetersAboveSeaLevel());
            preparedStatement.setInt(8, city.getTelephoneCode());
            preparedStatement.setInt(9, city.getCarCode());
            preparedStatement.setString(10, city.getClimate().toString());
            preparedStatement.setFloat(11, governor != null ? governor.getHeight() : null);
            preparedStatement.setString(12, username);
            preparedStatement.executeUpdate();
            ResultSet result = preparedStatement.getGeneratedKeys();
            result.next();

            return result.getLong(1);
        }
    }

    /**
     * Метод для проверки существования города с заданным идентификатором в базе данных.
     *
     * @param id идентификатор города
     * @return true, если организация существует в базе данных, иначе - false
     * @throws DatabaseException если возникла ошибка при обращении к базе данных
     */
    public static boolean checkOrganizationExistence(Long id) throws DatabaseException, SQLException {
        try (Connection connection = DBConnector.getConnection()) {
            String existenceQuery = "SELECT COUNT (*) "
                    + "FROM cities "
                    + "WHERE cities.id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(existenceQuery);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            return resultSet.getInt("count") > 0;
        }
    }

    /**
     * Метод для удаления города с заданным идентификатором, принадлежащей заданному пользователю, из базы данных.
     *
     * @param id       идентификатор удаляемой организации
     * @param username логин пользователя, которому принадлежит удаляемый город
     * @return true, если город была успешно удален, иначе - false
     * @throws DatabaseException если возникла ошибка при обращении к базе данных
     */
    public static boolean removeById(Long id, String username) throws DatabaseException, SQLException {
        return dbConnector.handleQuery((Connection connection) -> {
            String removeQuery = "DELETE FROM cities "
                    + "USING users "
                    + "WHERE cities.id = ? "
                    + "AND cities.owner_id = users.id AND users.login = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(removeQuery);
            preparedStatement.setLong(1, id);
            preparedStatement.setString(2, username);

            int deletedBands = preparedStatement.executeUpdate();
            return deletedBands > 0;
        });
    }

    /**
     * Метод для обновления информации о городе с заданным идентификатором, принадлежащего заданному пользователю в базе данных.
     *
     * @param city     новые данные о городе
     * @param id       идентификатор организации
     * @param username логин пользователя, которому принадлежит обновляемая организация
     * @return true, если информация об организации была успешно обновлена, иначе - false
     * @throws DatabaseException если возникла ошибка при обращении к базе данных
     */
    public static boolean updateById(City city, Long id, String username) throws DatabaseException, SQLException {
        return dbConnector.handleQuery((Connection connection) -> {
            connection.createStatement().execute("BEGIN TRANSACTION;");
            String updateQuery = "UPDATE cities "
                    + "SET "
                    + "name = ?, "
                    + "x = ?, "
                    + "y = ?, "
                    + "creation_date = ?, "
                    + "area = ?, "
                    + "population = ?, "
                    + "meters_above_sea_level = ?, "
                    + "telephone_code = ?, "
                    + "car_code = ?, "
                    + "climate = ?, "
                    + "height = ? "
                    + "FROM users "
                    + "WHERE cities.id = ? "
                    + "AND cities.owner_id = users.id AND users.login = ?;";

            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);

            Human governor = city.getGovernor();
            preparedStatement.setString(1, city.getName());
            preparedStatement.setFloat(2, city.getCoordinates().getX());
            preparedStatement.setDouble(3, city.getCoordinates().getY());
            preparedStatement.setDate(4, Date.valueOf(city.getCreationDate().toLocalDate()));
            preparedStatement.setDouble(5, city.getArea());
            preparedStatement.setFloat(6, city.getPopulation());
            preparedStatement.setFloat(7, city.getMetersAboveSeaLevel());
            preparedStatement.setInt(8, city.getTelephoneCode());
            preparedStatement.setInt(9, city.getCarCode());
            preparedStatement.setString(10, city.getClimate().toString());
            preparedStatement.setFloat(11, governor != null ? governor.getHeight() : null);
            preparedStatement.setLong(12, id);
            preparedStatement.setString(13, username);

            int updatedRows = preparedStatement.executeUpdate();
            connection.createStatement().execute("COMMIT;");

            return updatedRows > 0;
        });
    }

    /**
     * Очищает базу данных городов, которые принадлежат указанному пользователю.
     *
     * @param username логин пользователя, города, который нужно удалить.
     * @return список идентификаторов удаленных городов.
     * @throws DatabaseException если произошла ошибка при работе с базой данных.
     */
    public static List<Long> clear(String username) throws DatabaseException, SQLException {
        return dbConnector.handleQuery((Connection connection) -> {
            String clearQuery = "DELETE FROM cities "
                    + "USING users "
                    + "WHERE cities.owner_id = users.id AND users.login = ? "
                    + "RETURNING cities.id;";
            PreparedStatement preparedStatement = connection.prepareStatement(clearQuery);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Long> resultingList = new ArrayList<>();
            while (resultSet.next()) {
                resultingList.add(resultSet.getLong("id"));
            }
            return resultingList;
        });
    }

    /**
     * Добавляет нового пользователя в базу данных.
     *
     * @param username логин пользователя.
     * @param password пароль пользователя.
     * @throws DatabaseException если произошла ошибка при работе с базой данных.
     */
    public static void addUser(String username, String password) throws DatabaseException, SQLException {
        dbConnector.handleQuery((Connection connection) -> {
            String addUserQuery = "INSERT INTO users (login, password) "
                    + "VALUES (?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(addUserQuery,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, Encryptor.encryptString(password));

            preparedStatement.executeUpdate();
        });
    }

    /**
     * Получает пароль пользователя с указанным логином.
     *
     * @param username логин пользователя.
     * @return пароль пользователя или null, если пользователь не найден.
     * @throws DatabaseException если произошла ошибка при работе с базой данных.
     */
    public static String getPassword(String username) throws SQLException {
        try (Connection connection = DBConnector.getConnection()) {
            String getPasswordQuery = "SELECT (password) "
                    + "FROM users "
                    + "WHERE users.login = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(getPasswordQuery);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("password");
            }
            return null;
        }
    }

    /**
     * Проверяет существование пользователя с указанным логином.
     *
     * @param username логин пользователя.
     * @return true, если пользователь существует, иначе false.
     * @throws DatabaseException если произошла ошибка при работе с базой данных.
     */
    public static boolean checkUsersExistence(String username) throws DatabaseException, SQLException {
        try (Connection connection = DBConnector.getConnection()) {
//        return dbConnector.handleQuery((Connection connection) -> {
            String existenceQuery = "SELECT COUNT (*) "
                    + "FROM users "
                    + "WHERE users.login = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(existenceQuery);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt("count") > 0;
        }
    }

    /**
     * Метод для получения списка id организаций пользователя
     *
     * @param username логин пользователя
     * @return список id организаций пользователя
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public static List<Long> getIdsOfUsersElements(String username) throws DatabaseException, SQLException {
        return dbConnector.handleQuery((Connection connection) -> {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT cities.id "
                    + "FROM cities, users "
                    + "WHERE cities.owner_id = users.id AND users.login = ?;");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Long> resultingList = new ArrayList<>();
            while (resultSet.next()) {
                resultingList.add(resultSet.getLong("id"));
            }

            return resultingList;
        });
    }

    /**
     * Метод для проверки правильности введенных пользователем данных
     *
     * @param username логин пользователя
     * @param password пароль пользователя
     * @return true, если данные верны, false в противном случае
     * @throws DatabaseException если произошла ошибка при работе с базой данных
     */
    public static boolean validateUser(String username, String password) throws DatabaseException, SQLException {
        return Encryptor.encryptString(password).equals(getPassword(username));
    }
}
