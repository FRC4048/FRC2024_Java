package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import frc.robot.Robot;
import frc.robot.Constants;
import frc.robot.utils.SmartShuffleboard;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class IntakeSubsystem extends SubsystemBase{
    private final WPI_TalonSRX intakeMotor;
    private final WPI_TalonSRX intakeRaiseMotor;
    private final DigitalInput intakeSensor1;
    private final DigitalInput intakeSensor2;


    public IntakeSubsystem() {
        this.intakeMotor = new WPI_TalonSRX(45);
        this.intakeRaiseMotor = new WPI_TalonSRX(Constants.INTAKE_RAISE_MOTOR_ID);
        this.intakeSensor1 = new DigitalInput(Constants.INTAKE_SENSOR_ID_1);
        this.intakeSensor2 = new DigitalInput(Constants.INTAKE_SENSOR_ID_2);
        
        intakeMotor.configFactoryDefault();
        intakeRaiseMotor.configFactoryDefault();

        intakeMotor.configContinuousCurrentLimit(20);
    }
    
    public void spinMotor(double speed) {
        intakeMotor.set(speed);
    }

    public void stopMotor() {
        intakeMotor.set(0);
    }

    public void raiseIntake(double speed) {
        intakeRaiseMotor.set(speed);
    }

    public boolean isRingInIntake() {
        return !intakeSensor1.get() || !intakeSensor2.get();
    }

    public boolean getIntakeSensor1() {
        return intakeSensor1.get();
    }
    
    public boolean getIntakeSensor2() {
        return intakeSensor2.get();
    }
}
