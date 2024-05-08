package frc.robot.utils.math;

public class ArrayUtils {
    public static <T extends Number> boolean contains(T[] array, T value){
        for (T t : array) {
            if (t.equals(value)) {
                return true;
            }
        }
        return false;
    }
    public static boolean contains(double[] array, double value){
        for (double d : array){
            if (d == value){
                return true;
            }
        }
        return false;
    }
    public static boolean contains(int[] array, int value){
        for (int d : array){
            if (d == value){
                return true;
            }
        }
        return false;
    }
    public static boolean allMatch(double[] array, double value){
        for (double d : array){
            if (d != value){
                return false;
            }
        }
        return true;
    }
    public static boolean allMatch(int[] array, int value){
        for (int d : array){
            if (d != value){
                return false;
            }
        }
        return true;
    }
    public static <T extends Number> boolean allMatch(T[] array, T value){
        for (T t : array) {
            if (!t.equals(value)) {
                return false;
            }
        }
        return true;
    }
}
