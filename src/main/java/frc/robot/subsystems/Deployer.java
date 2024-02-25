package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;
import frc.robot.Robot;
import frc.robot.utils.diag.DiagTalonSrxSwitch;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

///This class is meant to manage the motor that "deploys" the intake, by rotating in order to raise and lower the intake area.
public class Deployer extends SubsystemBase{
    private WPI_TalonSRX deployerMotor;

    // The following line references the "protectionMechanism" subsystem, which does not currently exist in this year's code, so I am leaving it commented out. For more detail, see the last comment on this file - currently line 82, as I am writing this
    /* private ProtectionMechanism protectionMechanism; */
    

    public Deployer() {
        int TIMEOUT=100;

        deployerMotor = new WPI_TalonSRX(Constants.DEPLOYER_MOTOR_ID);
        deployerMotor.configNominalOutputForward(0, TIMEOUT);
        deployerMotor.configNominalOutputReverse(0, TIMEOUT);
        deployerMotor.configPeakOutputForward(1, TIMEOUT);
        deployerMotor.configPeakOutputReverse(-1, TIMEOUT);
        deployerMotor.setNeutralMode(NeutralMode.Brake);
        deployerMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, TIMEOUT);
        deployerMotor.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);
        deployerMotor.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);
        deployerMotor.setSelectedSensorPosition(0);
        deployerMotor.configPeakCurrentLimit(Constants.DEPLOYER_MOTOR_PEAK_CURRENT_LIMIT);
        deployerMotor.configPeakCurrentDuration(Constants.DEPLOYER_MOTOR_PEAK_CURRENT_DURATION);
        deployerMotor.configContinuousCurrentLimit(Constants.DEPLOYER_MOTOR_CONTINUOUS_CURRENT_LIMIT);
        deployerMotor.enableCurrentLimit(Constants.DEPLOYER_CURRENT_LIMIT_ENABLED);

        Robot.getDiagnostics().addDiagnosable(new DiagTalonSrxSwitch("Deployer", "Forward switch", deployerMotor, DiagTalonSrxSwitch.Direction.FORWARD));
        Robot.getDiagnostics().addDiagnosable(new DiagTalonSrxSwitch("Deployer", "Reverse switch", deployerMotor, DiagTalonSrxSwitch.Direction.REVERSE));
    }

    public void resetEncoder() {
        deployerMotor.setSelectedSensorPosition(0);
    }
    
    // The following function references another function which in turn references the "protectionMechanism" subsystem, which does not currently exist in this year's code, so I am leaving it commented out. For more detail, see the last comment on this file - currently line 82, as I am writing this
    /*
    public void move(double speed) {
        deployerMotor.set(validateExtenderVolt(speed));
    }
    */

    public void stop() {
        deployerMotor.set(0);
    }

    public double getEncoder() {
        return deployerMotor.getSelectedSensorPosition();
    }

    @Override
    public void periodic() {
        if (Constants.DEPLOYER_DEBUG) {
            SmartShuffleboard.put("Deployer", "encoder", getEncoder());
            SmartShuffleboard.put("Deployer", "Fwd Limt", isDeployerForwardLimitSwitchClosed());
            SmartShuffleboard.put("Deployer", "Rev Limit", isDeployerReverseLimitSwitchClosed());
        }
        SmartShuffleboard.put("Driver", "Deployer Raised", isDeployerForwardLimitSwitchClosed());
        SmartShuffleboard.put("Driver", "Deployer Down", isDeployerReverseLimitSwitchClosed());
        //Another place with logging code in last year's extender class
    }

    //Spin deployer motor
    public void setDeployerMotorSpeed(double speed) {
        deployerMotor.set(speed);
    }

    //Get deployer motor speed
    public double getDeployerMotorSpeed() {
        return deployerMotor.get();
    }

    public boolean isDeployerForwardLimitSwitchClosed() {
        return deployerMotor.getSensorCollection().isFwdLimitSwitchClosed();
    }

    public boolean isDeployerReverseLimitSwitchClosed() {
        return deployerMotor.getSensorCollection().isRevLimitSwitchClosed();
    }

    //

    // I'm not completely sure what the following code does (again, it is based on last year's "extender" subsystem), but it uses the "ProtectionMechanism" susbsystem, which is currently not implemented in this year's code, so I am leaving it commented out for now.
    /*
    public void setProtectionMechanism(ProtectionMechanism protectionMechanism) {
        this.protectionMechanism = protectionMechanism;
    }
    public double validateExtenderVolt(double volt){
        if ((volt > 0 && protectionMechanism.safeToExtend()) || volt < 0) return volt;
        return 0;
    }
    public boolean safeToExtend(){
        return protectionMechanism.safeToExtend();
    }
    */
}
