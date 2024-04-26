package frc.robot.subsystems.deployer;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import frc.robot.Robot;
import frc.robot.constants.Constants;
import frc.robot.utils.diag.DiagTalonSrxSwitch;

public class RealDeployer implements DeployerIO {
    private final WPI_TalonSRX motor;

    public RealDeployer() {
        motor = new WPI_TalonSRX(Constants.DEPLOYER_MOTOR_ID);
        configureMotor();
        Robot.getDiagnostics().addDiagnosable(new DiagTalonSrxSwitch("Deployer", "Forward switch", motor, DiagTalonSrxSwitch.Direction.FORWARD));
        Robot.getDiagnostics().addDiagnosable(new DiagTalonSrxSwitch("Deployer", "Reverse switch", motor, DiagTalonSrxSwitch.Direction.REVERSE));
    }

    private void configureMotor() {
        int TIMEOUT = 100;
        motor.configNominalOutputForward(0, TIMEOUT);
        motor.configNominalOutputReverse(0, TIMEOUT);
        motor.configPeakOutputForward(1, TIMEOUT);
        motor.configPeakOutputReverse(-1, TIMEOUT);
        motor.setNeutralMode(NeutralMode.Brake);
        motor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, TIMEOUT);
        motor.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);
        motor.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);
        motor.setSelectedSensorPosition(0);
        motor.setStatusFramePeriod(1,20);
        motor.setStatusFramePeriod(2,100);
        motor.setStatusFramePeriod(3,100);
    }

    @Override
    public void stop() {
        motor.stopMotor();
    }

    @Override
    public void setSpeed(double spd) {
        motor.set(spd);
    }

    @Override
    public void updateInputs(DeployerInputs inputs) {
        inputs.deployerSpeed = motor.get();
        inputs.fwdLimit = motor.getSensorCollection().isFwdLimitSwitchClosed();
        inputs.revLimit = motor.getSensorCollection().isRevLimitSwitchClosed();
    }
}
