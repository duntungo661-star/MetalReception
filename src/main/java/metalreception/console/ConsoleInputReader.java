package metalreception.console;

import java.math.BigDecimal;
import java.util.Scanner;

public class ConsoleInputReader {

    private final Scanner scanner;

    public ConsoleInputReader() {
        this.scanner = new Scanner(System.in);
    }

    public int readInt() {
        while (true) {
            String input = scanner.nextLine().strip();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(
                        "Некорректный формат. Введите целое число:"
                );
            }
        }
    }

    public BigDecimal readBigDecimal() {
        while (true) {
            String input = scanner.nextLine().strip();
            input = input.replace(',', '.');

            try {
                BigDecimal value = new BigDecimal(input);

                if (value.compareTo(BigDecimal.ZERO) > 0) {
                    return value;
                }

                System.out.println(
                        "Число должно быть больше нуля. Попробуйте снова:"
                );
            } catch (NumberFormatException e) {
                System.out.println(
                        "Некорректный формат числа. Например: 32.3"
                );
            }
        }
    }

    public String readLine() {
        return scanner.nextLine().strip();
    }

    public boolean readYesNo() {
        while (true) {
            String input = readLine();

            if (input.equalsIgnoreCase("да")) {
                return true;
            }

            if (input.equalsIgnoreCase("нет")) {
                return false;
            }

            System.out.println(
                    "Пожалуйста, введите «да» или «нет»:"
            );
        }
    }

    public void close() {
        scanner.close();
    }
}