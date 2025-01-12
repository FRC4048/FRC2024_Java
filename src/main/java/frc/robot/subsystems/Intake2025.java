package frc.robot.subsystems;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
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
    private double desiredSpeed3 = 0;
    private double desiredSpeed4 = 0;
    public Intake2025() {
        this.intakeMotor_1 = new WPI_TalonSRX(Constants.INTAKE_MOTOR_TESTING_1_ID);
        this.intakeMotor_2 = new WPI_TalonSRX(Constants.INTAKE_MOTOR_TESTING_2_ID);
        this.intakeMotor_1.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);
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
    public boolean getForwardSwitchTripped() {
        return intakeMotor_1.isFwdLimitSwitchClosed() == 1;
    }
    @Override
    public void periodic() {
        // TODO Auto-generated method stub
        super.periodic();
        if (Constants.INTAKE_TESTING_DEBUG) {
            SmartShuffleboard.put("IntakeTesting", "Desired intake speed 1", desiredSpeed1);
            SmartShuffleboard.put("IntakeTesting", "Desired intake Speed 2", desiredSpeed2);
            SmartShuffleboard.put("IntakeTesting", "Shooter motor 1 speed", desiredSpeed3);
            SmartShuffleboard.put("IntakeTesting", "Shooter motor 2 speed", desiredSpeed4);
        }
    }
}
