package frc.robot.commands.feeder;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.feeder.Feeder;

public class StopFeeder extends Command {
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