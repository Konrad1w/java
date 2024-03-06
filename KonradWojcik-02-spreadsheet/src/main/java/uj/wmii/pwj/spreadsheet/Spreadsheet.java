package uj.wmii.pwj.spreadsheet;

public class Spreadsheet {

    public static boolean isNumber(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static int calculatedValue(String operation, int number1, int number2) {
        return switch (operation) {
            case "ADD" -> number1 + number2;
            case "SUB" -> number1 - number2;
            case "MUL" -> number1 * number2;
            case "DIV" -> number1 / number2;
            case "MOD" -> number1 % number2;
            default -> 0;
        };
    }

    public static String calculateExpression(String expression, String[][] array) {
        String operation = expression.substring(1, 4);
        String firstParameter = expression.substring(5, expression.indexOf(','));
        String secondParameter = expression.substring(expression.indexOf(',') + 1, expression.length() - 1);

        while (!isNumber(firstParameter))
            firstParameter = calculateNotNumberFromCell(firstParameter, array);

        while (!isNumber(secondParameter))
            secondParameter = calculateNotNumberFromCell(secondParameter, array);

        return String.valueOf(calculatedValue(operation, Integer.parseInt(firstParameter), Integer.parseInt(secondParameter)));
    }

    public static String calculateNotNumberFromCell(String expression, String[][] array) {
        while (!isNumber(expression)) {
            if (expression.charAt(0) == '=')
                expression = calculateExpression(expression, array);
            else
                expression = valueFromAnotherCellInMatrix(expression, array);
        }
        return expression;
    }

    public static String valueFromAnotherCellInMatrix(String cellAddress, String[][] array) {
        char letter = cellAddress.charAt(1);
        int column = letter - 65;
        int row = Integer.parseInt(cellAddress.substring(2)) - 1;
        return array[row][column];
    }

    public String[][] calculate(String[][] input) {
        for (int i = 0; i < input.length; i++)
            for (int j = 0; j < input[i].length; j++) {
                if (!isNumber(input[i][j]))
                    input[i][j] = calculateNotNumberFromCell(input[i][j], input);
                System.out.println(input[i][j]);
            }
        return input;
    }
}
