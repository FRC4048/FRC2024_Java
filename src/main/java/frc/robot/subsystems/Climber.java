package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;
import frc.robot.utils.diag.DiagClimber;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;

public class Climber extends SubsystemBase {
    private final CANSparkMax SparkMax1;
    private final CANSparkMax SparkMax2;
    private final Servo leftServo;
    private final Servo rightServo;
    private final AHRS navxGyro;
    private final DiagClimber diagClimber;
    private boolean raisedTrue;
    private boolean loweredTrue;
    private double climberPosition;

    public Climber(AHRS navxGyro) {
        this.SparkMax1 = new CANSparkMax(Constants.CLIMBER_MOTOR1_ID, CANSparkMax.MotorType.kBrushless);
        this.SparkMax2 = new CANSparkMax(Constants.CLIMBER_MOTOR2_ID, CANSparkMax.MotorType.kBrushless);
        this.leftServo = new Servo(Constants.LEFT_SERVO_ID);
        this.rightServo = new Servo(Constants.RIGHT_SERVO_ID);
        this.navxGyro = navxGyro;
        this.diagClimber = new DiagClimber("Climber", "Climber", Constants.REQUIRED_RAISE, SparkMax1, Constants.REQUIRED_LOWER);
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
    private boolean raisedTrue(){
        return diagClimber.getDiagResultRaised();
    }
    private boolean loweredTrue(){
        return diagClimber.getDiagResultLowered();
    }
    private double getClimberPosition(){
        return diagClimber.getCurrentValue();
    }
    @Override
    public void periodic() {
        raisedTrue = raisedTrue();
        loweredTrue = loweredTrue();
        climberPosition = getClimberPosition();
        if (Constants.CLIMBER_DEBUG) {
            SmartShuffleboard.put("Climber", "Climber State", "Pitch", getGyroPitch());
            SmartShuffleboard.put("Climber", "Climber State", "Yaw", getGyroYaw());
            SmartShuffleboard.put("Climber", "Climber State", "Roll", getGyroRoll());
            SmartShuffleboard.put("Diagnostics", "Climber State", "Raised", raisedTrue);
            SmartShuffleboard.put("Diagnostics", "Climber State", "Lowered", loweredTrue);
            SmartShuffleboard.put("Diagnostics", "Climber State", "Position", climberPosition);
        }
    } 
}
