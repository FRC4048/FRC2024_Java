package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.constants.Constants;
import frc.robot.utils.diag.DiagTalonSrxSwitch;

public class Amp extends SubsystemBase {
    private final WPI_TalonSRX ampMotor;

    public Amp() {
        ampMotor = new WPI_TalonSRX(Constants.AMP_ID);
        ampMotor.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);
        ampMotor.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);

            Robot.getDiagnostics().addDiagnosable(new DiagTalonSrxSwitch("Arm", "Forward Limit Switch", ampMotor, frc.robot.utils.diag.DiagTalonSrxSwitch.Direction.FORWARD));
            Robot.getDiagnostics().addDiagnosable(new DiagTalonSrxSwitch("Arm", "Reverse Limit Switch", ampMotor, frc.robot.utils.diag.DiagTalonSrxSwitch.Direction.REVERSE));
    }

    @Override
    public void periodic() {

    }
    public boolean isForwardLimitSwitchPressed() {
        return ampMotor.getSensorCollection().isFwdLimitSwitchClosed();
    }

    public boolean isReverseLimitSwitchPressed() {
        return ampMotor.getSensorCollection().isRevLimitSwitchClosed();
    }

    public void setAmpMotorSpeed(double motorSpeed) {
        ampMotor.set(motorSpeed);
    }

    public void getAmpMotorSpeed() {
        ampMotor.get();
    }

}
