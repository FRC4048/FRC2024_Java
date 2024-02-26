package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Shooter;

public class StopShooter extends Command {
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
        shooter.setShooterMotorLeftRPM(0.0);
        shooter.setShooterMotorRightRPM(0.0);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}