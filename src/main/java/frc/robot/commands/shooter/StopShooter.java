package frc.robot.commands.shooter;

import frc.robot.subsystems.shooter.Shooter;
import frc.robot.utils.loggingv2.LoggableCommand;

public class StopShooter extends LoggableCommand {
    private final Shooter shooter;

    public StopShooter(Shooter shooter) {
        addRequirements(shooter);
        this.shooter = shooter;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
    }

    @Override
    public void end(boolean interrupted) {
        shooter.stopShooter();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}