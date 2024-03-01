package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Shooter;

public class ShootAmp extends Command {
    private final Shooter shooter;

    public ShootAmp(Shooter shooter) {
        this.shooter = shooter;
        addRequirements(shooter);
    }

    @Override
    public void initialize() {
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void execute() {
        shooter.setShooterMotorRightRPM(Constants.SHOOTER_MOTOR_AMP_SPEED);
        shooter.setShooterMotorLeftRPM(Constants.SHOOTER_MOTOR_AMP_SPEED);
        shooter.setLastMotorRightSpeed(Constants.SHOOTER_MOTOR_AMP_SPEED);
        shooter.setLastMotorLeftSpeed(Constants.SHOOTER_MOTOR_AMP_SPEED);

    }

    /**
     * @param interrupted if command was interrupted
     */
    @Override
    public void end(boolean interrupted) {
    }
}