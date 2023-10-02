package Common.util;

import Common.entity.City;
import Common.interfaces.Data;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Класс Response - класс, содержащий информацию для ответа на запрос.
 */
public class Response implements Serializable, Data {

    /**
     * Сообщение, отправляемое в ответ на запрос.
     */
    private String messageToResponse;

    private City cityToResponse;


    /**
     * Данные коллекции, отправляемые в ответ на запрос.
     */
    private ConcurrentLinkedDeque<City> collectionToResponse;

    /**
     * Коллекция элементов, не принадлежащих клиенту
     */
    private ConcurrentLinkedDeque<City> alienElements;

    /**
     * Список дополнительной информации, содержащейся в ответе сервера
     */
    private List<String> info;

    /**
     * Конструктор класса Response, принимающий сообщение для ответа.
     *
     * @param messageToResponse сообщение для ответа
     */
    public Response(String messageToResponse) {
        this.messageToResponse = messageToResponse;
    }


    public Response(String messageToResponse, City cityToResponse) {
        this.messageToResponse = messageToResponse;
        this.cityToResponse = cityToResponse;
    }

    public Response(String messageToResponse, ConcurrentLinkedDeque<City> collectionToResponse) {
        this.messageToResponse = messageToResponse;
        this.collectionToResponse = collectionToResponse;
    }

    public Response(String messageToResponse, List<String> info) {
        this.messageToResponse = messageToResponse;
        this.info = info;
    }
    public Response(String messageToResponse, ConcurrentLinkedDeque<City> usersElements, ConcurrentLinkedDeque<City> alienElements) {
        this.messageToResponse = messageToResponse;
        collectionToResponse = usersElements;
        this.alienElements = alienElements;
    }

    public Response(City cityToResponse) {
        this.cityToResponse = cityToResponse;
    }


    public Response(ConcurrentLinkedDeque<City> collectionToResponse) {
        this.collectionToResponse = collectionToResponse;
    }

    /**
     * Метод, возвращающий сообщение для ответа.
     *
     * @return сообщение для ответа
     */
    public String getMessageToResponse() {
        return messageToResponse;
    }


    public City getCityToResponse() {
        return cityToResponse;
    }

    public ConcurrentLinkedDeque<City> getCollectionToResponse() {
        return collectionToResponse;
    }

    public ConcurrentLinkedDeque<City> getAlienElements() {
        return alienElements;
    }

    public List<String> getInfo() {
        return info;
    }

    /**
     * Метод, возвращающий информацию для отправки.
     *
     * @return информация для отправки
     */
    @Override
    public String getData() {
        return (messageToResponse == null ? "" : (getMessageToResponse()))
                + (cityToResponse == null ? "" : ("\nДанные города:\n" + getCityToResponse().toString()))
                + (collectionToResponse == null ? "" : ("\nКоллекция:\n" + getCollectionToResponse()))
                + (alienElements == null ? "" :("\nКоллекции других пользователей:\n" +
                (getAlienElements().isEmpty() ? "В коллекциях других пользователей нет элементов" : getAlienElements())));
    }

    /**
     * Представляет ответ, полученный от сервера, в формате, удобном для чтения.
     */
    @Override
    public String toString() {
        return "Ответ[" + messageToResponse + "]";
    }
}