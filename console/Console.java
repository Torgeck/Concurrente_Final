package console;

import java.util.Map;

public class Console {

    private static final Map<String, String> COLOR_NAMES = Map.of(
            "CYAN", "\u001B[36m",
            "BLACK", "\u001B[30m",
            "RED", "\u001B[31m",
            "GREEN", "\u001B[32m",
            "YELLOW", "\u001B[33m",
            "BLUE", "\u001B[34m",
            "PURPLE", "\u001B[35m",
            "WHITE", "\u001B[37m",
            "RESET", "\u001B[0m");

    public static String colorString(String colorName, String string) {
        StringBuilder formattedString = new StringBuilder();
        String stringColor = COLOR_NAMES.get(colorName);

        if (stringColor != null) {
            formattedString.append(stringColor);
            formattedString.append(string);
            formattedString.append(COLOR_NAMES.get("RESET"));
        }

        return formattedString.toString();
    }
}