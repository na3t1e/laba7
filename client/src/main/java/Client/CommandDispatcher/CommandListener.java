package Client.CommandDispatcher;

import Client.util.ScannerManager;

import java.util.Arrays;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Класс для чтения команд из консоли или файла скрипта и создания объекта CommandToSend с информацией о команде.
 */
public class CommandListener {
    /**
     * Считывает команду из консоли или файла скрипта, создает объект CommandToSend с информацией о команде.
     *
     * @param sc         сканнер для чтения из консоли или файла скрипта
     * @param scriptMode режим считывания команд, в котором находится приложение
     * @return объект CommandToSend, содержащий имя команды и ее аргументы
     */
    public static CommandToSend readCommand(Scanner sc, boolean scriptMode) {
        try {
            String[] splitedInput = sc.nextLine().trim().split("\\s+");
            if (splitedInput.length == 0 || splitedInput[0].equals("")) {
                return readCommand(sc, scriptMode);
            }
            if (splitedInput[0].equals("EOF")) return null;
            String commandName = splitedInput[0].toLowerCase(Locale.ROOT);
            String[] commandsArgs = Arrays.copyOfRange(splitedInput, 1, splitedInput.length);
            return new CommandToSend(commandName, commandsArgs);

        } catch (NoSuchElementException e) {
            if (!scriptMode) {
                System.err.println("Принудительное завершение работы");
                System.exit(1);
            }
            return null;
        }
    }
}
