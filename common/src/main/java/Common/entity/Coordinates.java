package Common.entity;

import java.io.Serializable;

/**
 * Этот класс представляет точку в двумерном пространстве
 */
public class Coordinates implements Serializable {

    /**
     * Координата x
     * Максимальное значение поля: 741
     */
    private final float x;

    /**
     * Координата y
     * Поле не может быть null
     */
    private final Double y;

    /**
     * Конструктор, задающий параметры координат
     * @param x - координата x
     * @param y - координата y
     */
    public Coordinates(float x, Double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Возвращает координату точки X
     *
     * @return X
     */
    public float getX() {
        return x;
    }


    /**
     * Возвращает координату точки Y
     *
     * @return Y
     */
    public Double getY() {
        return y;
    }
}