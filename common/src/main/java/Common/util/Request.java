package Common.util;

import Common.entity.City;
import Common.interfaces.Data;

import java.io.Serializable;

/**
 * Класс, представляющий объект-запрос.
 * Реализует интерфейс Serializable для возможности сериализации объектов.
 * Реализует интерфейс Data для отправки данных по сети.
 * Содержит информацию о команде и параметрах команды.
 */
public class Request implements Serializable, Data {

    /**
     * Имя команды.
     */
    private String commandName;

    /**
     * Числовой аргумент команды.
     */
    private Long numericArgument;


    private City city;
    private String login;

    private String password;

    private final RequestType type;

    /**
     * Конструктор класса, принимающий имя команды.
     * @param commandName имя команды
     */
    public Request(String commandName, RequestType type) {
        this.commandName = commandName;
        this.type = type;
    }

    public Request(String commandName, Long numericArgument, RequestType type) {
        this.commandName = commandName;
        this.numericArgument = numericArgument;
        this.type = type;
    }

    public Request(String commandName, City city, RequestType type) {
        this.commandName = commandName;
        this.city = city;
        this.type = type;
    }

    public Request(String commandName, Long numericArgument, City city, RequestType type) {
        this.commandName = commandName;
        this.numericArgument = numericArgument;
        this.city = city;
        this.type = type;
    }

    public Request(String login, String password, RequestType type) {
        this.login = login;
        this.password = password;
        this.type = type;
    }


    /**
     * Получает имя команды.
     * @return имя команды
     */
    public String getCommandName() {
        return commandName;
    }

    /**
     * Получает числовой аргумент.
     * @return числовой аргумент команды
     */
    public Long getNumericArgument() {
        return numericArgument;
    }


    public City getCity() {
        return city;
    }

    public RequestType getType() {
        return type;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

/*    @Override
    public String toString() {
        return "Request{" +
                "commandName='" + commandName + '\'' +
                ", numericArgument=" + numericArgument +
                ", city=" + city +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", type=" + type +
                '}';
    }*/

    /**
     * Метод getData() возвращает строковое представление данных объекта в виде имени команды и соответствующих аргументов.
     */


    @Override
    public String getData(){
        return "Имя команды для отправки: " + commandName
                + (city == null ? "" : ("\nИнформация о городе для отправки:\n " + city))
                + (numericArgument == null ? "" : ("\nЧисловой аргумент для отправки:\n " + numericArgument));
    }

    /**
     * Возвращает строковое представление объекта в формате "Ответ[имя команды]".
     */
    @Override
    public String toString() {
        return "Ответ[" + commandName + "]" ;
    }
}
