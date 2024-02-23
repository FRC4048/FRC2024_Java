package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkLimitSwitch;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;

public class Climber extends SubsystemBase {
    private final CANSparkMax climberLeft;
    private final CANSparkMax climberRight; //invert this motor
    private final Servo leftServo;
    private final Servo rightServo;
    private final AHRS navxGyro;
    private final SparkLimitSwitch rightLimitSwitch;
    private final SparkLimitSwitch leftLimitSwitch;

    public Climber(AHRS navxGyro) {
        this.climberLeft = new CANSparkMax(Constants.CLIMBER_LEFT, CANSparkMax.MotorType.kBrushless);
        this.climberRight = new CANSparkMax(Constants.CLIMBER_RIGHT, CANSparkMax.MotorType.kBrushless);
        rightLimitSwitch = climberRight.getReverseLimitSwitch(SparkLimitSwitch.Type.kNormallyOpen);
        leftLimitSwitch = climberLeft.getForwardLimitSwitch(SparkLimitSwitch.Type.kNormallyOpen);
        this.climberRight.setInverted(true);
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
     */
    public void raise(){
        climberLeft.set(-Constants.CLIMBER_RAISING_SPEED);
        climberRight.set(-Constants.CLIMBER_RAISING_SPEED);
    }
    public void lower(){
        climberLeft.set(isLeftLimit() ? 0 : Constants.CLIMBER_RAISING_SPEED);
        climberRight.set(isRightLimit() ? 0: Constants.CLIMBER_RAISING_SPEED);
    }
    public void balanceRight(double speed) {
        climberLeft.set(speed);
        climberRight.set(0);
    }
    public void balanceLeft(double speed) {
        climberLeft.set(0);
        climberRight.set(speed);
    }
    public void stop(){
        climberLeft.set(0);
        climberRight.set(0);
    }
    public void setSpeeds(double spd1, double spd2){
        climberLeft.set(spd1);
        climberRight.set(spd2);
    }
     /**
     * 
     * @return if left arm is raised
     */
    public boolean isLeftArmRaised() {
        return (climberLeft.getAlternateEncoder(42).getPosition() >= Constants.REQUIRED_RAISE);
    }
    /**
     * 
     * @return if right arm is raised
     */
    public boolean isRightArmRaised() {
        return (climberRight.getAlternateEncoder(42).getPosition() >= Constants.REQUIRED_RAISE);
    }
    /**
     * Delete this soon
     * @return if left arm is lowered
     */
    public boolean isLeftArmLowered() {
        return (climberLeft.getAlternateEncoder(42).getPosition() >= Constants.REQUIRED_LOWER);
    }
     /**
     * Delete this soon
     * @return if right arm is lowered
     */
    public boolean isRightArmLowered() {
        return (climberRight.getAlternateEncoder(42).getPosition() >= Constants.REQUIRED_LOWER);
    }
    @Override
    public void periodic() {
        if (Constants.CLIMBER_DEBUG) {
            SmartShuffleboard.put("Climber", "Climber State", "Pitch", getGyroPitch());
            SmartShuffleboard.put("Climber", "Climber State", "Yaw", getGyroYaw());
            SmartShuffleboard.put("Climber", "Climber State", "Roll", getGyroRoll());
            SmartShuffleboard.put("Climber", "leftLimitSwitch", leftLimitSwitch.isPressed());
            SmartShuffleboard.put("Climber", "rightLimitSwitch", rightLimitSwitch.isPressed());
            SmartShuffleboard.put("Climber", "Climber State", "Roll", getGyroRoll());
            SmartShuffleboard.put("Diagnostics", "Left Climber Lowered", isLeftArmLowered());
            SmartShuffleboard.put("Diagnostics", "Right Climber Lowered", isRightArmLowered());
            SmartShuffleboard.put("Diagnostics", "Left Climber Raised", isLeftArmRaised());
            SmartShuffleboard.put("Diagnostics", "Right Climber Raised", isRightArmRaised());
        }
    }
    public boolean isLeftLimit(){
        return leftLimitSwitch.isPressed();
    }
    public boolean isRightLimit(){
        return rightLimitSwitch.isPressed();
    }
}
