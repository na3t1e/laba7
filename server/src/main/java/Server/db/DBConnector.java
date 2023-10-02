package Server.db;

import Common.exception.DatabaseException;
import Server.interfaces.DBConnectable;
import Server.interfaces.SQLConsumer;
import Server.interfaces.SQLFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;


/**
 * Класс для соединения с базой данных.
 */
public class DBConnector implements DBConnectable {


    /**
     * Конструктор класса DBConnector.
     */
    public DBConnector() {
        try {
            Class.forName("org.postgresql.Driver");
            initializeDB();
        } catch (ClassNotFoundException e) {
            System.err.println("Нет драйвера БД.");
            System.exit(1);
        } catch (SQLException | DatabaseException e) {
            e.printStackTrace();
//            System.err.println("Произошла ошибка при инициализации таблиц");
            System.exit(1);
        }
    }

    /**
     * Обрабатывает запросы без возвращаемого значения.
     *
     * @param queryBody тело запроса.
     * @throws DatabaseException в случае ошибки при работе с базой данных.
     */
    public void handleQuery(SQLConsumer<Connection> queryBody) throws DatabaseException {
        try (Connection connection = getConnection()) {
            queryBody.accept(connection);
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка при работе с БД: " + Arrays.toString(e.getStackTrace()));
        }
    }

    /**
     * Обрабатывает запросы с возвращаемым значением.
     *
     * @param queryBody тело запроса.
     * @param <T>       тип возвращаемого значения.
     * @return результат выполнения запроса.
     * @throws DatabaseException в случае ошибки при работе с базой данных.
     */
    public <T> T handleQuery(SQLFunction<Connection, T> queryBody) throws DatabaseException {
        try (Connection connection = getConnection()) {
            return queryBody.apply(connection);
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка при работе с БД: " + Arrays.toString(e.getStackTrace()));
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/studs", "s268727", "hgV9UNML5j2k6bLj");
    }

    /**
     * Создает таблицы в базе данных, если они не существуют.
     *
     * @throws SQLException в случае ошибки при создании таблиц.
     */
    private void initializeDB() throws SQLException, DatabaseException {
        try (Connection connection = getConnection()) {

            Statement statement = connection.createStatement();



            statement.execute("CREATE SEQUENCE IF NOT EXISTS cities_id_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1");

            statement.execute("CREATE SEQUENCE IF NOT EXISTS users_id_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1");

            statement.execute("CREATE TABLE IF NOT EXISTS users "
                    + "("
                    + "id bigint NOT NULL PRIMARY KEY DEFAULT nextval('users_id_seq'),"
                    + "login varchar(255) NOT NULL UNIQUE CHECK(login<>''),"
                    + "password varchar(255) NOT NULL CHECK(password<>'')"
                    + ");");

            statement.execute("CREATE TABLE IF NOT EXISTS cities "
                    + "("
                    + "id bigint NOT NULL PRIMARY KEY DEFAULT nextval('cities_id_seq'),"
                    + "name varchar(255) NOT NULL CHECK(name <> ''),"
                    + "x float NOT NULL,"
                    + "y double precision CHECK(y <= 672) NOT NULL,"
                    + "creation_date date NOT NULL,"
                    + "area double precision check (area > 0),"
                    + "population bigint NOT NULL CHECK(population > 0),"
                    + "meters_above_sea_level float,"
                    + "telephone_code int CHECK(telephone_code > 0 and telephone_code < 100000),"
                    + "car_code int CHECK(car_code > 0 and telephone_code < 1000),"
                    + "climate varchar(24) NOT NULL CHECK(climate = 'RAIN_FOREST' "
                    + "OR climate = 'HUMIDCONTINENTAL' "
                    + "OR climate = 'OCEANIC' "
                    + "OR climate = 'MEDITERRANIAN' "
                    + "OR climate = 'STEPPE'),"
                    + "height float check (height > 0),"
                    + "owner_id bigint NOT NULL REFERENCES users (id) ON DELETE CASCADE"
                    + ");");

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("Ошибка при работе с БД: " + Arrays.toString(e.getStackTrace()));
        }
    }

}

