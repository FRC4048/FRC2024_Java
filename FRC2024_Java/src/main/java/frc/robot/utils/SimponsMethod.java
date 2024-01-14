package frc.robot.utils;
import frc.robot.subsystems.Climber;

public class SimponsMethod {
    static int n=100;
    /* 
    public static double integrate(double a, double b) {
        double h = (b-a)/(n-1);
        double sum = 0;
        for (int i=1; i<n-1; i+=2) {
            sum += 4*Climber.ArmUnderExtendLog(a+i*h);
        }
        for (int i=2; i<n-1; i+=2) {
            sum += 2*Climber.ArmUnderExtendLog(a+i*h);
        }
        sum += Climber.ArmUnderExtendLog(a) + Climber.ArmUnderExtendLog(b);
        return sum*h/3;
    }
    */
}
