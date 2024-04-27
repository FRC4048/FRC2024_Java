package frc.robot.commands.feeder;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.constants.Constants;
import frc.robot.subsystems.feeder.Feeder;
import frc.robot.subsystems.lightstrip.LightStrip;
import frc.robot.utils.BlinkinPattern;
import frc.robot.utils.loggingv2.Loggable;
import frc.robot.utils.loggingv2.LoggableCommand;

public class TimedFeeder extends LoggableCommand {
    private final Feeder feeder;
    private final Timer timer = new Timer();
    private final LightStrip lightStrip;
    private final double motorRunTime; // temporary until  done testing

    public TimedFeeder(Feeder feeder, LightStrip lightStrip, double motorRunTime) {
        this.feeder = feeder;
        this.lightStrip = lightStrip;
        this.motorRunTime = motorRunTime;
        addRequirements(feeder);
    }
    public TimedFeeder(Loggable parent, Feeder feeder, LightStrip lightStrip, double motorRunTime) {
        super(parent);
        this.feeder = feeder;
        this.lightStrip = lightStrip;
        this.motorRunTime = motorRunTime;
        addRequirements(feeder);
    }

    @Override
    public void initialize() {
        feeder.switchFeederBeamState(false);
        timer.reset();
        timer.start();
        if (feeder.pieceSeen(false)){
            lightStrip.setPattern(BlinkinPattern.BLACK);
        }
    }

    @Override
    public void execute() {
        feeder.setFeederMotorSpeed(Constants.FEEDER_MOTOR_SPEAKER_SPEED);
    }

    @Override
    public void end(boolean interrupted) {
        feeder.stopFeederMotor();
    }

    @Override
    public boolean isFinished() {
        return timer.hasElapsed(motorRunTime);
    }
}
