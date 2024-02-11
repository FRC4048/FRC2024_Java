package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class IntakeSubsystem extends SubsystemBase{
    private final WPI_TalonSRX intakeMotor1;
    private final WPI_TalonSRX intakeMotor2;

    public IntakeSubsystem() {
        this.intakeMotor1 = new WPI_TalonSRX(Constants.INTAKE_MOTOR_1_ID);
        this.intakeMotor2 = new WPI_TalonSRX(Constants.INTAKE_MOTOR_2_ID);
        
        intakeMotor1.setInverted(false);
        intakeMotor2.setInverted(false);

        //Set current stall limit (All of these values are tests for now)
        intakeMotor1.configPeakCurrentLimit(30);
        intakeMotor1.configPeakCurrentDuration(100);
        intakeMotor1.configContinuousCurrentLimit(20);
        intakeMotor1.enableCurrentLimit(true);

        intakeMotor2.configPeakCurrentLimit(30);
        intakeMotor2.configPeakCurrentDuration(100);
        intakeMotor2.configContinuousCurrentLimit(20);
        intakeMotor2.enableCurrentLimit(true);
    }
    
    public void setMotorSpeed(double motor1Speed, double motor2Speed) {
        intakeMotor1.set(motor1Speed);
        intakeMotor2.set(motor2Speed);
    }

    public double getMotor1Speed() {
        return intakeMotor1.get();
    }

    public double getMotor2Speed() {
        return intakeMotor2.get();
    }

    public void stopMotors() {
        intakeMotor1.set(0);
        intakeMotor2.set(0);
    }

    @Override
    public void periodic() {
        SmartShuffleboard.put("Intake", "Intake Motor 1 Speed", getMotor1Speed());
        SmartShuffleboard.put("Intake", "Intake Motor 2 Speed", getMotor2Speed());

        SmartShuffleboard.put("Intake", "Intake Motor 1 current", intakeMotor1.getStatorCurrent());
        SmartShuffleboard.put("Intake", "Intake Motor 2 current", intakeMotor2.getStatorCurrent());
    }
}
