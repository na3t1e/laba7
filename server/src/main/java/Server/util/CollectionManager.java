package Server.util;

import Common.entity.City;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

/**
 * Класс-менеджер коллекции.
 * Содержит методы для работы с коллекцией, такие как добавление, удаление,
 * поиск по id, замена по id, сортировка, перемешивание, очистка и т.д.
 * Также возвращает информацию о коллекции.
 */
public class CollectionManager {

    private ConcurrentLinkedDeque<City> cityCollection;
    private LocalDateTime lastInitTime;

    public ConcurrentLinkedDeque<City> sort(ConcurrentLinkedDeque<City> collection) {
        return collection.stream().sorted().collect(Collectors.toCollection(ConcurrentLinkedDeque<City>::new));
    }
    public ConcurrentLinkedDeque<City> getCollection() {
        return cityCollection;
    }

    public LocalDateTime getLastInitTime() {
        return lastInitTime;
    }

    public void removeById(Long id) {
        cityCollection.removeIf(p -> p.getId().equals(id));
    }


    public long getFirstId(){
        Optional<Long> minId = cityCollection.stream()
                .map(City::getId)
                .min(Comparator.naturalOrder());
        return minId.orElse(0L);
    }

    public void addToCollection(City city) {
        cityCollection.add(city);
        cityCollection = new ConcurrentLinkedDeque<>(sort(cityCollection));
        setLastInitTime(LocalDateTime.now());
    }

    public void setLastInitTime(LocalDateTime lastInitTime) {
        this.lastInitTime = lastInitTime;
    }

    public void clearCollection() {
        cityCollection.clear();
    }

    public void setCityCollection(ConcurrentLinkedDeque<City> cityCollection) {
        this.cityCollection = cityCollection;
    }

    public ConcurrentLinkedDeque<City> shuffleCollection() {
        List<City> list = new ArrayList<>(cityCollection);
        Collections.shuffle(list);
        return new ConcurrentLinkedDeque<>(list);
    }

    public ConcurrentLinkedDeque<City> sortCollection(){
        List<City> list = new ArrayList<>(cityCollection);
        Collections.sort(list);
        return new ConcurrentLinkedDeque<>(list);
    }

    public Set<Integer> getCarCodes() {
        return cityCollection.stream()
                .map(City::getCarCode)
                .collect(Collectors.toSet());
    }

    public List<Double> getListArea() {
        return cityCollection.stream() // создание потока данных
                .map(City::getArea)
                .sorted(Collections.reverseOrder()) // сортировка в обратном порядке
                .collect(Collectors.toList()); // собирает все значения в List
    }


    public ConcurrentLinkedDeque<City> getDescending() {
        return cityCollection.stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toCollection(ConcurrentLinkedDeque<City>::new));
    }

    /**
     * Возвращает коллекцию городов с указанными идентификаторами.
     * @param ids список идентификаторов городов
     * @return коллекция организаций с указанными идентификаторами
     */
    public ConcurrentLinkedDeque<City> getUsersElements(List<Long> ids) {
        return cityCollection.stream().filter(p -> ids.contains(p.getId())).collect(Collectors.toCollection(ConcurrentLinkedDeque::new));
    }

    /**
     * Возвращает коллекцию городов, которые не содержатся в указанном списке идентификаторов.
     * @param ids список идентификаторов городов
     * @return коллекция организаций, которые не содержатся в указанном списке идентификаторов
     */
    public ConcurrentLinkedDeque<City> getAlienElements(List<Long> ids) {
        return cityCollection.stream().filter(p -> !ids.contains(p.getId())).collect(Collectors.toCollection(ConcurrentLinkedDeque::new));
    }

    public String getInfo() {
        return "Тип коллекции: " + ConcurrentLinkedDeque.class + ", тип элементов: "
                + City.class
                + (getLastInitTime() == null ? "" : (", дата инициализации: "
                + getLastInitTime().format(DateTimeFormatter.ofPattern("dd.MM.y H:mm:ss"))))
                + ", количество элементов: " + cityCollection.size();

    }

    @Override
    public String toString() {
        if (cityCollection.isEmpty()) return "Коллекция пуста.";
        StringBuilder info = new StringBuilder();
        for (City city : cityCollection) {
            info.append(city);
            if (city != cityCollection.getLast()) info.append("\n\n");
        }
        return info.toString();
    }
}
