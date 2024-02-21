package frc.robot.utils.math;

public class RangeUtils {
    public static double map(double value, double startMin, double startMax, double endMin, double endMax){
        double endRange = endMax - endMin;
        double startRange = startMax - startMin;
        return ((value - startMin) * endRange / startRange) + endMin;

    }
}
