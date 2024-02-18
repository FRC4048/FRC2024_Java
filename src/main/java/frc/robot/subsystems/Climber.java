package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;

public class Climber extends SubsystemBase {
    private final CANSparkMax SparkMax1;
    private final CANSparkMax SparkMax2;
    private final Servo servo;
    private final AHRS navxGyro;

    public Climber(AHRS navxGyro, Servo servo) {
        this.SparkMax1 = new CANSparkMax(Constants.CLIMBER_MOTOR1_ID, CANSparkMax.MotorType.kBrushless);
        this.SparkMax2 = new CANSparkMax(Constants.CLIMBER_MOTOR2_ID, CANSparkMax.MotorType.kBrushless);
        this.navxGyro = navxGyro;
        this.servo = servo;
    }
    public double getGyroPitch() { // when the robot is mounted north south this is the right thingy to use
        return (navxGyro.getPitch() % 360); 
    }
    public double getGyroYaw() {
        return (navxGyro.getYaw() % 360);
    }
    public double getGyroRoll() {
        return (navxGyro.getRoll() % 360);
    }

    /**
     * @param angle in degrees
     */
    public void setServoAngle(double angle){
        this.servo.setAngle(angle);
    }

    /**
     * sets the speed of both motors to the IntakeSpeed defined in the {@link Constants} file
     * @param upward if true the motors will spin in the intake directions,
     *                if false the motors will spin in the outtake direction
     */
    public void raise(boolean upward){
        double speed = upward ? Constants.CLIMBER_RAISING_SPEED : Constants.CLIMBER_SPEED * -1;
        SparkMax1.set(speed);
        SparkMax2.set(speed);
    }
    public void balanceRight(double speed) {
        SparkMax1.set(speed);
        SparkMax2.set(0);
    }
    public void balanceLeft(double speed) {
        SparkMax1.set(0);
        SparkMax2.set(speed);
    }
    public void stop(){
        SparkMax1.set(0);
        SparkMax2.set(0);
    }
    @Override
    public void periodic() {
        if (Constants.CLIMBER_DEBUG) {
            SmartShuffleboard.put("Climber", "Climber State", "Pitch", getGyroPitch());
            SmartShuffleboard.put("Climber", "Climber State", "Yaw", getGyroYaw());
            SmartShuffleboard.put("Climber", "Climber State", "Roll", getGyroRoll());
        }
    } 
}
