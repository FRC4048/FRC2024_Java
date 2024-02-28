package frc.robot.commands.feeder;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Feeder;

public class FeederBackEject extends Command {
    private final Feeder feeder;
    private final Timer timer = new Timer();
    private final double time;

    public FeederBackEject(Feeder feeder, double time) {
        this.feeder = feeder;
        this.time = time;
        addRequirements(feeder);
    }
    @Override
    public void initialize() {
        timer.reset();
        timer.start();
    }
    @Override
    public void execute() {
        feeder.setFeederMotorSpeed(Constants.FEEDER_BACK_EJECT_SPEED);
    }
    @Override
    public void end(boolean interrupted) {
        feeder.stopFeederMotor();
        timer.stop();
    }
    @Override
    public boolean isFinished() {
        return timer.hasElapsed(time);
    }
}
