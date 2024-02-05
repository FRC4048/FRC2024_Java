package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import frc.robot.Robot;
import frc.robot.Constants;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.IdleMode;

public class IntakeSubsystem extends SubsystemBase{
    private final CANSparkMax intakeMotor;
    private final CANSparkMax intakeRaiseMotor;
    private final DigitalInput intakeSensor1;
    private final DigitalInput intakeSensor2;


    public IntakeSubsystem() {
        this.intakeMotor = new CANSparkMax(Constants.INTAKE_MOTOR_ID, CANSparkLowLevel.MotorType.kBrushless);
        this.intakeRaiseMotor = new CANSparkMax(Constants.INTAKE_RAISE_MOTOR_ID, CANSparkLowLevel.MotorType.kBrushless);
        this.intakeSensor1 = new DigitalInput(Constants.INTAKE_SENSOR_ID_1);
        this.intakeSensor2 = new DigitalInput(Constants.INTAKE_SENSOR_ID_2);
        
        intakeMotor.restoreFactoryDefaults();

        this.intakeMotor.setIdleMode(IdleMode.kCoast);
    }
    
    public void spinMotor(double speed) {
        intakeMotor.set(speed);
    }

    public void stopMotor() {
        intakeMotor.set(0);
    }

    public void moveRaiseIntake(double speed) {
        intakeRaiseMotor.set(speed);
    }

    public void stopRaiseIntake() {
        intakeRaiseMotor.set(0);
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
