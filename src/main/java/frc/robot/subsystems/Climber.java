package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;

public class Climber extends SubsystemBase {
    private final CANSparkMax sparkMax1;
    private final CANSparkMax sparkMax2; //invert this motor
    private final Servo leftServo;
    private final Servo rightServo;
    private final AHRS navxGyro;

    public Climber(AHRS navxGyro) {
        this.sparkMax1 = new CANSparkMax(Constants.CLIMBER_LEFT, CANSparkMax.MotorType.kBrushless);
        this.sparkMax2 = new CANSparkMax(Constants.CLIMBER_RIGHT, CANSparkMax.MotorType.kBrushless);
        this.sparkMax2.setInverted(true);
        this.leftServo = new Servo(Constants.LEFT_SERVO_ID);
        this.rightServo = new Servo(Constants.RIGHT_SERVO_ID);
        this.navxGyro = navxGyro;
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
    public void setLeftServoAngle(double angle){
        this.leftServo.setAngle(angle);
    }
    public void setRightServoAngle(double angle){
        this.rightServo.setAngle(angle);
    }

    /**
     * sets the speed of both motors to the IntakeSpeed defined in the {@link Constants} file
     * @param upward if true the motors will spin in the intake directions,
     *                if false the motors will spin in the outtake direction
     */
    public void raise(boolean upward){
        double speed = upward ? Constants.CLIMBER_RAISING_SPEED : Constants.CLIMBER_SPEED * -1;
        sparkMax1.set(speed);
        sparkMax2.set(speed);
    }
    public void balanceRight(double speed) {
        sparkMax1.set(speed);
        sparkMax2.set(0);
    }
    public void balanceLeft(double speed) {
        sparkMax1.set(0);
        sparkMax2.set(speed);
    }
    public void stop(){
        sparkMax1.set(0);
        sparkMax2.set(0);
    }
    public void setSpeeds(double spd1, double spd2){
        sparkMax1.set(spd1);
        sparkMax2.set(spd2);
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
