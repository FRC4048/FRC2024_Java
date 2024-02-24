package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.RobotContainer;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.SwerveDrivetrain;
import frc.robot.utils.logging.Logger;

public class ShootSpeaker extends Command {

    private final Shooter shooter;
    private final Timer timer = new Timer();
    private boolean activated = false;
    private SwerveDrivetrain drivetrain;
    public ShootSpeaker(Shooter shooter, SwerveDrivetrain drivetrain) {
        this.shooter = shooter;
        this.drivetrain = drivetrain;
        addRequirements(shooter);
    }

    @Override
    public void initialize() {
        timer.reset();
        activated = true;
        timer.start();

    }

    @Override 
    public boolean isFinished() {
        return (timer.hasElapsed(Constants.SHOOTER_TIME_AFTER_TRIGGER)) && (activated);
    }

    @Override
    public void execute() {
        double gyro = ((((drivetrain.getGyroAngle().getDegrees()) % 360) + 360) % 360); //Gets the gyro value 0-360
        if (((RobotContainer.isRedAlliance() == true) && (gyro > 180)) || 
            ((RobotContainer.isRedAlliance() == false) && (gyro < 180))) {
            shooter.setShooterMotorRightSpeed(Constants.SHOOTER_MOTOR_LOW_SPEED);
            shooter.setShooterMotorLeftSpeed(Constants.SHOOTER_MOTOR_HIGH_SPEED);
        }
        else if (((RobotContainer.isRedAlliance() == false) && (gyro >= 180)) || 
                ((RobotContainer.isRedAlliance() == true) && (gyro <= 180))) {
            shooter.setShooterMotorRightSpeed(Constants.SHOOTER_MOTOR_HIGH_SPEED);
            shooter.setShooterMotorLeftSpeed(Constants.SHOOTER_MOTOR_LOW_SPEED);
          }
        else {
            Logger.logDouble("Shooting Condition not working", gyro, true);
        }
        
    }

    /**
     * @param interrupted if command was interrupted
     *  stop shooter and timer. Set activated to false
     */
    @Override
    public void end(boolean interrupted) {
        shooter.stopShooter();
        timer.stop();
        activated = false;
    }
}
