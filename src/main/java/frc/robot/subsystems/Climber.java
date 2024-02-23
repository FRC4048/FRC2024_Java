package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkLimitSwitch;
import com.revrobotics.CANSparkBase.IdleMode;

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

        this.climberLeft.restoreFactoryDefaults();
        this.climberRight.restoreFactoryDefaults();

        this.climberLeft.setIdleMode(IdleMode.kBrake);
        this.climberRight.setIdleMode(IdleMode.kBrake);

        rightLimitSwitch = climberRight.getReverseLimitSwitch(SparkLimitSwitch.Type.kNormallyOpen);
        leftLimitSwitch = climberLeft.getForwardLimitSwitch(SparkLimitSwitch.Type.kNormallyOpen);
        
//        this.climberRight.setInverted(false);
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
     * Right motor - Forward is up, Reverse is down
     * Left motor - Forward is down, Reverse is up
     * @param left
     * @param spd
     */
    public void setSingleSpeed(boolean left, double spd) {

        climberRight.set(spd);
        climberLeft.set(-spd);



        // if (left) {
        //     climberLeft.set(spd);
        // } else {
        //     climberRight.set(spd);
        // }
    }

    public void resetEncoders() {
        this.climberLeft.getEncoder().setPosition(0);
        this.climberRight.getEncoder().setPosition(0);
    }    

    @Override
    public void periodic() {
        if (this.climberRight.getEncoder().getPosition() > Constants.MAX_CLIMBER_ENCODER) {
            climberLeft.set(0);
            climberRight.set(0);
        }
        if (Constants.CLIMBER_DEBUG) {
            SmartShuffleboard.put("Climber", "Left Encoder", climberLeft.getEncoder().getPosition());
            SmartShuffleboard.put("Climber", "Right Encoder", climberRight.getEncoder().getPosition());

            SmartShuffleboard.put("Climber", "Climber State", "Pitch", getGyroPitch());
            SmartShuffleboard.put("Climber", "Climber State", "Yaw", getGyroYaw());
            SmartShuffleboard.put("Climber", "Climber State", "Roll", getGyroRoll());
            SmartShuffleboard.put("Climber", "leftLimitSwitch", leftLimitSwitch.isPressed());
            SmartShuffleboard.put("Climber", "rightLimitSwitch", rightLimitSwitch.isPressed());
            SmartShuffleboard.put("Climber", "Climber State", "Roll", getGyroRoll());
        }
    }
    public boolean isLeftLimit(){
        return leftLimitSwitch.isPressed();
    }
    public boolean isRightLimit(){
        return rightLimitSwitch.isPressed();
    }
}
