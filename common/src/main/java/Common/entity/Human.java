package Common.entity;

import java.io.Serializable;
import java.util.Objects;

public class Human implements Serializable {
    private Float height; //Значение поля должно быть больше 0

    public Human(Float height) {
        this.height = height;
    }

    public Human() {
    }

    public Float getHeight() {

        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }
    @Override
    public String toString() {
        return "рост = " + height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Human human = (Human) o;
        return height.equals(human.height);
    }

    @Override
    public int hashCode() {
        return Objects.hash(height);
    }
}
