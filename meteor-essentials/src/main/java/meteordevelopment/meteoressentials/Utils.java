package meteordevelopment.meteoressentials;

public class Utils {
    public static double clamp(double value, double min, double max) {
        if (value < min) return min;
        return Math.min(value, max);
    }

    public static boolean isDigits(String str) {
        if (str.isEmpty()) return false;

        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }
}
