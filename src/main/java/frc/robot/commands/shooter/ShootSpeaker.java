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
    private SwerveDrivetrain drivetrain;
    private Timer timer = new Timer();
    private double startTime;
    private boolean leftStarted;
    private boolean rightStarted;
    public ShootSpeaker(Shooter shooter, SwerveDrivetrain drivetrain) {
        this.shooter = shooter;
        this.drivetrain = drivetrain;
        addRequirements(shooter);
    }

    @Override
    public void initialize() {
        startTime = Timer.getFPGATimestamp();
        leftStarted = false;
        rightStarted = false;
    }

    @Override 
    public boolean isFinished() {
        return (leftStarted && rightStarted && Timer.getFPGATimestamp()-startTime>5);
    }

    @Override
    public void execute() {

       double gyro = ((((drivetrain.getGyroAngle().getDegrees()) % 360) + 360) % 360); //Gets the gyro value 0-360
        if (((RobotContainer.isRedAlliance() == true) && (gyro > 180)) ||
            ((RobotContainer.isRedAlliance() == false) && (gyro < 180))) {
            if (!leftStarted) {
                shooter.setShooterMotorLeftRPM(Constants.SHOOTER_MOTOR_HIGH_SPEED);
                leftStarted = true;
            }
            if (/*timer.getFPGATimestamp() - startTime > 0.5 &&*/ !rightStarted) {
                shooter.setShooterMotorRightRPM(Constants.SHOOTER_MOTOR_LOW_SPEED);
                rightStarted = true;
            }
        }
        else {
            if (!rightStarted) {
                shooter.setShooterMotorRightRPM(Constants.SHOOTER_MOTOR_HIGH_SPEED);
                rightStarted = true;
            }
            if (/*timer.getFPGATimestamp() - startTime > 0.5 &&*/ !leftStarted) {
                shooter.setShooterMotorLeftRPM(Constants.SHOOTER_MOTOR_LOW_SPEED);
                leftStarted=true;
            }
        }
    }

    /**
     * @param interrupted if command was interrupted
     */
    @Override
    public void end(boolean interrupted) {
        shooter.setShooterMotorLeftRPM(0);
        shooter.setShooterMotorRightRPM(0);
    }
}