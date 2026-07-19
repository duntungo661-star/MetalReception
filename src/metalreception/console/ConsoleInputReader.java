package metalreception.console;

import java.math.BigDecimal;
import java.util.Scanner;

public class ConsoleInputReader {
    private final Scanner scanner = new Scanner(System.in);

    public int readInt() {
        while (!scanner.hasNextInt()) {
            System.out.println("Введите число: ");
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }

    public BigDecimal readBigDecimal() {
        while (true) {
            String input = scanner.nextLine().strip();
            try {
                BigDecimal value = new BigDecimal(input);
                if (value.compareTo(BigDecimal.ZERO) > 0) {
                    return value;
                }
                System.out.println("Число должно быть положительным. Попробуйте снова: ");
            } catch (NumberFormatException e) {
                System.out.println("Некорректный формат числа. Введите число (например 32.3): ");
            }
        }
    }

    public String readLine() {
        return scanner.nextLine().strip();
    }

    public boolean readYesNo() {
        while(true) {
            String input = readLine().toLowerCase();
            if (input.equals("да")) {
                return true;
            }
            if (input.equals("нет")) {
                return false;
            }
            System.out.println("Пожалуйста, введите 'да' или 'нет': ");
        }
    }
}
