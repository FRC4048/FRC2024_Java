package frc.robot.commands.shooter;

import frc.robot.RobotContainer;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.SwerveDrivetrain;
import frc.robot.utils.command.TimedCommand;
import frc.robot.utils.logging.Logger;

public class ShootSpeaker extends TimedCommand {

    private final Shooter shooter;
    private final SwerveDrivetrain drivetrain;
    public ShootSpeaker(Shooter shooter, SwerveDrivetrain drivetrain) {
        super(Constants.SHOOTER_TIMEOUT);
        this.shooter = shooter;
        this.drivetrain = drivetrain;
        addRequirements(shooter);
    }

    @Override
    public void execute() {
        double gyro = ((((drivetrain.getGyroAngle().getDegrees()) % 360) + 360) % 360); //Gets the gyro value 0-360
        if (((RobotContainer.isRedAlliance() == true) && (gyro > 180)) ||
            ((RobotContainer.isRedAlliance() == false) && (gyro < 180))) {
            shooter.setShooterMotorRightRPM(Constants.SHOOTER_MOTOR_LOW_SPEED);
            shooter.setShooterMotorLeftRPM(Constants.SHOOTER_MOTOR_HIGH_SPEED);
        }
        else if (((RobotContainer.isRedAlliance() == false) && (gyro >= 180)) ||
                ((RobotContainer.isRedAlliance() == true) && (gyro <= 180))) {
            shooter.setShooterMotorRightRPM(Constants.SHOOTER_MOTOR_HIGH_SPEED);
            shooter.setShooterMotorLeftRPM(Constants.SHOOTER_MOTOR_LOW_SPEED);
          }
        else {
            Logger.logDouble("Shooting Condition not working", gyro, true);
        }

    }
    @Override
    public void end(boolean interrupted) {
        shooter.stopShooter();
    }
}