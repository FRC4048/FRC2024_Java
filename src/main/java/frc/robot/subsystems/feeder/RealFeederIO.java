package frc.robot.subsystems.feeder;

import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.I2C;
import frc.robot.Robot;
import frc.robot.constants.Constants;
import frc.robot.utils.diag.DiagColorSensor;
import frc.robot.utils.diag.DiagTalonSrxSwitch;

public class RealFeederIO implements FeederIO {
    private final WPI_TalonSRX feederMotor;
    private final ColorSensor colorSensor;
    private boolean areBeamsEnabled;

    public RealFeederIO() {
        this.feederMotor = new WPI_TalonSRX(Constants.FEEDER_MOTOR_ID);
        configureMotor();
        I2C.Port i2cPort = I2C.Port.kMXP;
        colorSensor = new ColorSensor(i2cPort);
        Robot.getDiagnostics().addDiagnosable(new DiagColorSensor("Feeder", "Color Sensor", colorSensor));
        Robot.getDiagnostics().addDiagnosable(new DiagTalonSrxSwitch("Feeder", "Beam Limit Switch", feederMotor, DiagTalonSrxSwitch.Direction.FORWARD));
    }

    private void configureMotor() {
        this.feederMotor.setNeutralMode(NeutralMode.Brake);
        this.feederMotor.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);
        feederMotor.setStatusFramePeriod(2,100);
        feederMotor.setStatusFramePeriod(3,100);
    }

    @Override
    public void setSpeed(double spd) {
        feederMotor.set(spd);
    }

    @Override
    public void stop() {
        feederMotor.stopMotor();
    }

    @Override
    public void switchFeederBeamState(boolean enable) {
        areBeamsEnabled = enable;
        if (enable){
            this.feederMotor.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);
        }else {
            this.feederMotor.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.Disabled);
        }
    }

    @Override
    public void updateInputs(FeederInputs inputs) {
        inputs.feederSpeed = feederMotor.get();
        inputs.isFwdTripped = feederMotor.isFwdLimitSwitchClosed() == 1;
        inputs.colorMatchResult = colorSensor.getMatchedColor();
        inputs.areBeamsEnabled = areBeamsEnabled;
    }
}
