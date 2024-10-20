package frc.robot.commands.feeder;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.constants.Constants;
import frc.robot.subsystems.feeder.Feeder;
import frc.robot.subsystems.lightstrip.LightStrip;
import frc.robot.utils.BlinkinPattern;
import frc.robot.utils.loggingv2.LoggableCommand;

public class StartFeeder extends LoggableCommand {

    private final Feeder feeder;
    private final LightStrip lightStrip;
    private double startTime;

    public StartFeeder(Feeder feeder, LightStrip lightStrip) {
        this.feeder = feeder;
        this.lightStrip = lightStrip;
        addRequirements(feeder);
    }

    @Override
    public void initialize() {
        startTime = Timer.getFPGATimestamp();
    }

    @Override
    public void execute() {
        feeder.setFeederMotorSpeed(Constants.FEEDER_MOTOR_ENTER_SPEED);
    }

    @Override
    public void end(boolean interrupted) {
        feeder.stopFeederMotor();
        lightStrip.setPattern(BlinkinPattern.ORANGE);
        
    }

    @Override
    public boolean isFinished() {
//        if (feeder.pieceSeen(true)) {
//            return true;
//        }
        if (Timer.getFPGATimestamp() - startTime > Constants.START_FEEDER_TIMEOUT) {
            return true;
        }
        return false;
    }
}