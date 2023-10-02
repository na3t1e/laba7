package Client.util;

import Common.exception.*;
import Common.entity.*;

import java.time.ZonedDateTime;
import java.util.*;


/**
 * Менеджер ввода информации об организации.
 */
public class ScannerManager {

    /**
     * Константа строки приглашения для ввода в интерактивном режиме.
     */


    /**
     * Константа строки приглашения для ввода команд в интерактивном режиме.
     */


    /**
     * Сканер для ввода.
     */
    private Scanner userScanner;

    /**
     * Режим скрипта.
     */
    private boolean scriptMode;

    /**
     * Шаблон для проверки числа.
     */
    private final String numberPattern = "-?\\d+(\\.\\d+)?";

    /**
     * Создает менеджер для ввода с помощью заданного сканера.
     *
     * @param scanner    сканер для ввода.
     * @param scriptMode режим чтение со скрипта
     */
    public ScannerManager(Scanner scanner, boolean scriptMode) {
        this.userScanner = scanner;
        this.scriptMode = scriptMode;
    }

    public City askCity() throws IncorrectInputInScriptException {
        return new City(
                null,
                askName(),
                askCoordinates(),
                ZonedDateTime.now(),
                askArea(),
                askPopulation(),
                askMetersAboveSeaLevel(),
                askTelephoneCode(),
                askCarCode(),
                askClimate(),
                askHuman()
        );
    }

    public String askName() throws IncorrectInputInScriptException {
        String str;
        while (true) {
            try {
                System.out.println("Введите название:");
                str = userScanner.nextLine().trim();
                if (scriptMode) System.out.println(str);
                if (str.equals("")) throw new NotNullException();
                break;
            } catch (NoSuchElementException exception) {
                System.err.println("Значение поля не может быть использовано");
                System.exit(1);
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (NotNullException exception) {
                System.err.println("Значение поля не может быть пустым");
                if (scriptMode) throw new IncorrectInputInScriptException();
                if (!userScanner.hasNext()) {
                    System.err.println("Работа программы прекращена");
                    System.exit(1);
                }
            } catch (IllegalStateException exception) {
                System.err.println("Непредвиденная ошибка");
                System.exit(1);
            }
        }
        return str;
    }

    public float askX() throws IncorrectInputInScriptException {
        String str;
        float x;
        while (true) {
            try {
                System.out.println("Введите координату X:");
                str = userScanner.nextLine().trim();
                str = str.replace(",", ".");
                if (str.equals("")) throw new NotNullException();
                if (scriptMode) System.out.println(str);
                if (!str.matches(numberPattern)) throw new NumberFormatException();
                x = Float.parseFloat(str);
                break;
            } catch (NoSuchElementException exception) {
                System.err.println("Значение поля не может быть использовано");
                if (scriptMode) throw new IncorrectInputInScriptException();
                if (!userScanner.hasNext()) {
                    System.err.println("Работа программы прекращена");
                    System.exit(1);
                }
            } catch (NumberFormatException exception) {
                System.err.println("Значение поля должно быть типа float");
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                System.err.println("Непредвиденная ошибка");
                System.exit(1);
            } catch (NotNullException exception) {
                System.err.println("Значение поля не может быть пустым");
                if (scriptMode) throw new IncorrectInputInScriptException();
            }
        }
        return x;
    }

    public Double askY() throws IncorrectInputInScriptException {
        String str;
        Double y;
        while (true) {
            try {
                System.out.println("Введите координату Y:");
                str = userScanner.nextLine().trim();
                str = str.replace(",", ".");
                if (scriptMode) System.out.println(str);
                if (str.equals("")) throw new NotNullException();
                if (!str.matches(numberPattern)) throw new NumberFormatException();
                y = Double.parseDouble(str);
                if (y > 672) throw new NotInDeclaredLimitsException();
                break;
            } catch (NoSuchElementException exception) {
                System.err.println("Значение поля не может быть использовано");
                System.exit(1);
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (NotInDeclaredLimitsException exception) {
                System.err.println("Значение поля должно быть меньше 672");
                if (scriptMode) throw new IncorrectInputInScriptException();

            } catch (NumberFormatException exception) {
                System.err.println("Значение поля должно быть типа Double");
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                System.err.println("Непредвиденная ошибка");
                System.exit(1);
            } catch (NotNullException exception) {
                System.err.println("Значение поля не может быть пустым");
                if (scriptMode) throw new IncorrectInputInScriptException();
            }
        }
        return y;
    }

    public Coordinates askCoordinates() throws IncorrectInputInScriptException {
        float x = askX();
        Double y = askY();
        return new Coordinates(x, y);
    }


    public double askArea() throws IncorrectInputInScriptException {
        String str;
        double area;
        while (true) {
            try {
                System.out.println("Введите площадь:");
                str = userScanner.nextLine().trim();
                str = str.replace(",", ".");
                if (scriptMode) System.out.println(str);
                if (!str.matches("-?\\d+$")) throw new NumberFormatException();
                area = Double.parseDouble(str);
                if (area <= 0) throw new NotInDeclaredLimitsException();
                break;
            } catch (NoSuchElementException exception) {
                System.err.println("Значение поля не может быть использовано");
                System.exit(1);
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (NumberFormatException exception) {
                System.err.println("Значение поля должно быть double");
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                System.err.println("Непредвиденная ошибка.");
                System.exit(1);
            } catch (NotInDeclaredLimitsException e) {
                System.err.println("Значение поля должно быть больше 0");
                if (scriptMode) throw new IncorrectInputInScriptException();
            }
        }
        return area;
    }


    public Long askPopulation() throws IncorrectInputInScriptException {
        String str;
        Long population;
        while (true) {
            try {
                System.out.println("Введите население:");
                str = userScanner.nextLine().trim();
                str = str.replace(",", ".");
                if (scriptMode) System.out.println(str);
                if (!str.matches("-?\\d+$")) throw new NumberFormatException();
                if (str.equals("")) throw new NotNullException();
                population = Long.parseLong(str);
                if (population <= 0) throw new NotInDeclaredLimitsException();
                break;
            } catch (NoSuchElementException exception) {
                System.err.println("Значение поля не может быть использовано");
                System.exit(1);
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (NumberFormatException exception) {
                System.err.println("Значение поля должно быть Long");
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                System.err.println("Непредвиденная ошибка.");
                System.exit(1);
            } catch (NotInDeclaredLimitsException e) {
                System.err.println("Значение поля должно быть больше 0");
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (NotNullException exception) {
                System.err.println("Значение поля не может быть пустым");
                if (scriptMode) throw new IncorrectInputInScriptException();
            }
        }
        return population;
    }

    public Float askMetersAboveSeaLevel() throws IncorrectInputInScriptException {
        String str;
        Float metersAboveSeaLevel;
        while (true) {
            try {
                System.out.println("Введите количество метров над уровнем моря:");
                str = userScanner.nextLine().trim();
                if (str.equals("")) return null;
                str = str.replace(",", ".");
                if (scriptMode) System.out.println(str);
                if (str.equals("")) return null;
                if (!str.matches("-?\\d+$")) throw new NumberFormatException();
                metersAboveSeaLevel = Float.parseFloat(str);
                break;
            } catch (NoSuchElementException exception) {
                System.err.println("Значение поля не может быть использовано");
                System.exit(1);
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (NumberFormatException exception) {
                System.err.println("Значение поля должно быть Float");
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                System.err.println("Непредвиденная ошибка");
                System.exit(1);
            }
        }
        return metersAboveSeaLevel;
    }

    public int askTelephoneCode() throws IncorrectInputInScriptException {
        String str;
        int telephoneCode;
        while (true) {
            try {
                System.out.println("Введите телефонный код:");
                str = userScanner.nextLine().trim();
                str = str.replace(",", ".");
                if (scriptMode) System.out.println(str);
                if (!str.matches(numberPattern)) throw new NumberFormatException();
                telephoneCode = Integer.parseInt(str);
                if (telephoneCode < 0 || telephoneCode > 100000) throw new NotInDeclaredLimitsException();
                break;
            } catch (NoSuchElementException exception) {
                System.err.println("Значение поля не может быть использовано");
                System.exit(1);
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (NotInDeclaredLimitsException exception) {
                System.err.println("Значение поля должно быть больше 0 и меньше 100000");
                if (scriptMode) throw new IncorrectInputInScriptException();

            } catch (NumberFormatException exception) {
                System.err.println("Значение поля должно быть типа int");
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                System.err.println("Непредвиденная ошибка");
                System.exit(1);
            }
        }
        return telephoneCode;
    }

    public Integer askCarCode() throws IncorrectInputInScriptException {
        String str;
        Integer carCode;
        while (true) {
            try {
                System.out.println("Введите код машины:");
                str = userScanner.nextLine().trim();
                str = str.replace(",", ".");
                if (scriptMode) System.out.println(str);
                if (!str.matches(numberPattern)) throw new NumberFormatException();
                carCode = Integer.parseInt(str);
                if (carCode < 0 || carCode > 1000) throw new NotInDeclaredLimitsException();
                break;
            } catch (NoSuchElementException exception) {
                System.err.println("Значение поля не может быть использовано");
                System.exit(1);
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (NotInDeclaredLimitsException exception) {
                System.err.println("Значение поля должно быть больше 0 и меньше 1000");
                if (scriptMode) throw new IncorrectInputInScriptException();

            } catch (NumberFormatException exception) {
                System.err.println("Значение поля должно быть типа Integer");
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                System.err.println("Непредвиденная ошибка");
                System.exit(1);
            }
        }
        return carCode;
    }

    public Climate askClimate() throws IncorrectInputInScriptException {
        String str;
        Climate climate;
        while (true) {
            try {
                System.out.println("Список климата: " + Climate.names());
                System.out.println("Введите климат из списка:");
                str = userScanner.nextLine().trim();
                if (scriptMode) System.out.println(str);
                if (str.equals("")) throw new NotNullException();
                climate = Climate.valueOf(str.toUpperCase());
                break;
            } catch (NoSuchElementException exception) {
                System.err.println("Значение поля не может быть использовано");
                System.exit(1);
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (IllegalArgumentException exception) {
                System.err.println("Такого типа нет в списке");
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                System.err.println("Непредвиденная ошибка");
                System.exit(1);
            } catch (NotNullException exception) {
                System.err.println("Значение поля не может быть пустым");
                if (scriptMode) throw new IncorrectInputInScriptException();
            }
        }
        return climate;
    }

    public Float askHeight() throws IncorrectInputInScriptException {
        String str;
        Float height;
        while (true) {
            try {
                System.out.println("Введите рост губернатора:");
                str = userScanner.nextLine().trim();
                str = str.replace(",", ".");
                if (scriptMode) System.out.println(str);
                if (str.equals("")) return null;
                if (!str.matches("-?\\d+$")) throw new NumberFormatException();
                height = Float.parseFloat(str);
                if (height <= 0) throw new NotInDeclaredLimitsException();
                break;
            } catch (NoSuchElementException exception) {
                System.err.println("Значение поля не может быть использовано");
                System.exit(1);
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (NumberFormatException exception) {
                System.err.println("Значение поля должно быть Float");
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                System.err.println("Непредвиденная ошибка");
                System.exit(1);
            } catch (NotInDeclaredLimitsException e) {
                System.err.println("Значение поля должно быть больше 0");
                if (scriptMode) throw new IncorrectInputInScriptException();
            }
        }
        return height;
    }

    public Human askHuman() throws IncorrectInputInScriptException {
        Float height = askHeight();
        if (height == null) return null;
        return new Human(height);
    }
}