package frc.robot.subsystems;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.kauailabs.navx.frc.AHRS;
import java.util.*; 
import java.awt.geom.Path2D;

import edu.wpi.first.wpilibj.Timer;
import com.revrobotics.CANSparkMax;

import frc.robot.commands.BalancePID;
import frc.robot.utils.ClimberState;
import frc.robot.utils.Constants;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;

public class Climber extends SubsystemBase {
    private final CANSparkMax SparkMax1;
    private final CANSparkMax SparkMax2;
    private final AHRS navxGyro;
    private double navxGyroValue;
    public Path2D.Double ArmUnderExtendLog;
    private double currentTime = Timer.getFPGATimestamp();
    private double instatenousDeravative;

    public Climber(){
        this.SparkMax1 = new CANSparkMax(Constants.CLIMBER_MOTOR1_ID, CANSparkMax.MotorType.kBrushless);
        this.SparkMax2 = new CANSparkMax(Constants.CLIMBER_MOTOR2_ID, CANSparkMax.MotorType.kBrushless);
        navxGyro = new AHRS();

        navxGyroValue = -1.00;
    }
    public double getinstatenousDeravative() {
        return instatenousDeravative;
    }
    private double getcurrentTime() {
        return currentTime;
    }
    public double getNavxGyroValue() {
        return navxGyroValue;
    }
    public double getGyroPitch() { // change to private later
        return (navxGyro.getPitch() % 360)*-1; //ccw should be positive
    }
    public double getGyroYaw() { // change to private later
        return (navxGyro.getYaw() % 360)*-1;
    }
    public double getGyroRoll() { // change to private later
        return (navxGyro.getRoll() % 360)*-1;
    }
    private ClimberState currentState = ClimberState.STOPPED;
    public ClimberState setClimberState(ClimberState newState){
        currentState = newState;
        return currentState;
    }

    private double ArmUnderExtend=Constants.ArmSeperationDistance*java.lang.Math.tan(navxGyroValue);
    
    public double getArmUnderExtend() {
        return ArmUnderExtend;
    }

    /**
     * sets the speed of both motors to the IntakeSpeed defined in the {@link Constants} file
     * @param forward if true the motors will spin in the intake directions,
     *                if false the motors will spin in the outtake direction
     */
    public void raise(boolean upward){
        double speed = upward ? Constants.CLIMBER_SPEED : Constants.CLIMBER_SPEED*-1;
        SparkMax1.set(speed);
        SparkMax2.set(speed);
        currentState = upward ? ClimberState.NORMALUP: ClimberState.NORMALDOWN;
    }  
    public void balance(double speed) {
        SparkMax1.set(speed);
        SparkMax2.set(-speed);
    }
    public void stop(){
        SparkMax1.set(0);
        SparkMax2.set(0);
        currentState = ClimberState.STOPPED;
    }

    public ClimberState getState() {
        return currentState;
    }
    @Override
    public void periodic() {
        instatenousDeravative = (getGyroPitch()-navxGyroValue)/(Timer.getFPGATimestamp()-currentTime);
        currentTime=Timer.getFPGATimestamp();
        ArmUnderExtend=Constants.ArmSeperationDistance*java.lang.Math.tan(navxGyroValue);
        navxGyroValue = getGyroPitch(); 
        //ArmUnderExtendLog.lineTo(Timer.getFPGATimestamp(),navxGyroValue);
        if (currentState==ClimberState.BALANCE && navxGyroValue<1 && navxGyroValue>-1) {
            currentState=ClimberState.STOPPED;
        } else if (navxGyroValue>1 || navxGyroValue<-1) {
            currentState=ClimberState.BALANCE;
        }
        SmartShuffleboard.put("Climber", "Climber State", "State", currentState);
        SmartShuffleboard.put("Climber", "Climber State", "ArmUnderExtend", ArmUnderExtend);
        SmartShuffleboard.put("Climber", "Climber State", "Pitch", getGyroPitch());
        SmartShuffleboard.put("Climber", "Climber State", "Yaw", getGyroYaw());
        SmartShuffleboard.put("Climber", "Climber State", "Roll", getGyroRoll());
    } 
}
