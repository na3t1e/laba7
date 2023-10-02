package Common.entity;

import java.io.Serializable;

public enum Climate implements Serializable {
    RAIN_FOREST,
    HUMIDCONTINENTAL,
    OCEANIC,
    MEDITERRANIAN,
    STEPPE;

    public static String names() {
        StringBuilder nameList = new StringBuilder();
        for (Climate climate : values()) {
            nameList.append(climate.name()).append("\n");
        }
        return nameList.substring(0, nameList.length()-1);
    }
}
