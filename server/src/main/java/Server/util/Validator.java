package Server.util;

import Common.entity.City;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Класс Validator предназначен для валидации списка городов
 */
public class Validator {


    List<City> cities;


    public Validator(List<City> cities) {
        this.cities = cities;
    }

    public List<City> validate() {
        Set<Long> idSet = new HashSet<>();
        Iterator<City> iterator = cities.iterator();
        while (iterator.hasNext()) {
            City city = iterator.next();
            if (city.getId() == null || city.getId() <= 0 || !idSet.add(city.getId())) iterator.remove();
            if (city.getName() == null || city.getName().equals("")) iterator.remove();
            if (city.getCoordinates() == null) iterator.remove();
            else {
                if (city.getCoordinates().getX() > 741) iterator.remove();
                if (city.getCoordinates().getY() == null) iterator.remove();
            }
            if (city.getCreationDate() == null) iterator.remove();
            if (city.getArea() <= 0) iterator.remove();
            if (city.getPopulation() <= 0 || city.getPopulation() == null) iterator.remove();
            if (city.getTelephoneCode() <= 0 || city.getTelephoneCode() >= 100000) iterator.remove();
            if (city.getCarCode() <= 0 || city.getCarCode() >= 1000) iterator.remove();
            if (city.getClimate() == null) iterator.remove();
        }
        return cities;
    }
}