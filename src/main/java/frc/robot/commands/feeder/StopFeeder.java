package frc.robot.commands.feeder;

import frc.robot.subsystems.feeder.Feeder;
import frc.robot.utils.loggingv2.LoggableCommand;

public class StopFeeder extends LoggableCommand {
    private final Feeder feeder;

    public StopFeeder(Feeder feeder) {
        addRequirements(feeder);
        this.feeder = feeder;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        feeder.stopFeederMotor();
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}