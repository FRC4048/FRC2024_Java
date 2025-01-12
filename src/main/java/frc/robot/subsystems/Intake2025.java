package frc.robot.subsystems;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;

public class Intake2025 extends SubsystemBase {
    private final WPI_TalonSRX intakeMotor_1;
    private final WPI_TalonSRX intakeMotor_2;
    private double desiredSpeed1 = 0;
    private double desiredSpeed2 = 0;
    public Intake2025() {
        this.intakeMotor_1 = new WPI_TalonSRX(Constants.INTAKE_MOTOR_TESTING_1_ID);
        this.intakeMotor_2 = new WPI_TalonSRX(Constants.INTAKE_MOTOR_TESTING_2_ID);
    }
    public void setIntakeMotor1Speed(double speed) {
        intakeMotor_1.set(speed);
    }
    public void setIntakeMotor2Speed(double speed) {
        intakeMotor_2.set(speed);
    }
    public void stopIntakeMotors() {
        intakeMotor_1.set(0);
        intakeMotor_2.set(0);
    }
    @Override
    public void periodic() {
        // TODO Auto-generated method stub
        super.periodic();
        if (Constants.INTAKE_TESTING_DEBUG) {
            SmartShuffleboard.put("IntakeTesting", "Desired speed 1", desiredSpeed1);
            SmartShuffleboard.put("IntakeTesting", "Desired Speed 2", desiredSpeed2);
        }
    }
}
