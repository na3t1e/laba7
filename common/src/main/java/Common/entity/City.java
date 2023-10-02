package Common.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

public class City implements Comparable<City>, Serializable {

    @NotNull
    @Positive(message = "ID должен быть больше нуля!")
    private Long id;

    @NotNull(message = "Имя не может быть null")
    @NotBlank(message = "Имя должно содержать хотя бы 1 символ")
    private String name;

    @NotNull(message = "Координаты не могут быть null")
    private Coordinates coordinates;

    @NotNull(message = "Дата не может быть null")
    @PastOrPresent
    private ZonedDateTime creationDate;

    @Positive(message = "Площадь должна быть больше нуля!")
    private double area;

    @NotNull(message = "Численность населения не может быть null")
    @Positive(message = "Численность населения должна быть больше нуля!")
    private Long population;

    private Float metersAboveSeaLevel;

    @Positive(message = "Телефонный код должен быть больше нуля!")
    private int telephoneCode;


    @Positive(message = "Код номера машины должен быть больше нуля!")
    private Integer carCode;

    @NotNull(message = "Климат не может быть null")
    private Climate climate;

    private Human governor;

    public City(Long id, String name, Coordinates coordinates, ZonedDateTime creationDate, double area, Long population, Float metersAboveSeaLevel, int telephoneCode, Integer carCode, Climate climate, Human governor) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.area = area;
        this.population = population;
        this.metersAboveSeaLevel = metersAboveSeaLevel;
        this.telephoneCode = telephoneCode;
        this.carCode = carCode;
        this.climate = climate;
        this.governor = governor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public double getArea() {
        return area;
    }

    public Long getPopulation() {
        return population;
    }


    public Float getMetersAboveSeaLevel() {
        return metersAboveSeaLevel;
    }

    public int getTelephoneCode() {
        return telephoneCode;
    }

    public Integer getCarCode() {
        return carCode;
    }

    public Climate getClimate() {
        return climate;
    }


    public Human getGovernor() {
        return governor;
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates: " + coordinates +
                ", creationDate=" + creationDate +
                ", area=" + area +
                ", population=" + population +
                ", metersAboveSeaLevel=" + metersAboveSeaLevel +
                ", telephoneCode=" + telephoneCode +
                ", carCode=" + carCode +
                ", climate=" + climate +
                ", " + governor +
                '}';
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, area, population, metersAboveSeaLevel, telephoneCode, carCode, climate, governor);
    }

    @Override
    public int compareTo(City o) {
        return this.getName().compareTo(o.getName());
    }
}

