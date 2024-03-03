package frc.robot.commands;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.SwerveDrivetrain;
import frc.robot.subsystems.Vision;
import frc.robot.utils.TimeoutCounter;

public class MoveToGamepiece extends Command {
    private SwerveDrivetrain drivetrain;
    private Vision vision;
    private double startTime;
    private final ProfiledPIDController turningPIDController;
    private final ProfiledPIDController movingPIDController;
    private ChassisSpeeds driveStates;
    private double ychange;
    private final TimeoutCounter timeoutCounter = new TimeoutCounter(getName());



    public MoveToGamepiece(SwerveDrivetrain drivetrain, Vision vision) {
        this.drivetrain = drivetrain;
        this.vision = vision;
        addRequirements(drivetrain);
        TrapezoidProfile.Constraints constraints = new TrapezoidProfile.Constraints(Constants.GAMEPIECE_MAX_VELOCITY, Constants.GAMEPIECE_MAX_ACCELERATION);
        turningPIDController = new ProfiledPIDController(Constants.TURN_TO_GAMEPIECE_TURNING_P, 0, Constants.TURN_TO_GAMEPIECE_TURNING_D, constraints);
        movingPIDController = new ProfiledPIDController(Constants.TURN_TO_GAMEPIECE_MOVING_P, 0, 0, constraints);

    }

    @Override
    public void initialize() {
        startTime = Timer.getFPGATimestamp();
    }

    @Override
    public void execute() {
        ychange = vision.getPieceOffestAngleY() - Constants.LIMELIGHT_TURN_TO_PIECE_DESIRED_Y;
        if (vision.isPieceSeen() && (Math.abs(ychange) > Constants.MOVE_TO_GAMEPIECE_THRESHOLD)) {
        driveStates = new ChassisSpeeds(movingPIDController.calculate(ychange), 0, turningPIDController.calculate(vision.getPieceOffestAngleX() - Constants.LIMELIGHT_TURN_TO_PIECE_DESIRED_X));
        drivetrain.drive(driveStates);
        }
    }

    @Override
    public boolean isFinished() {
        if ((Timer.getFPGATimestamp() - startTime > Constants.MOVE_TO_GAMEPIECE_TIMEOUT) || (!vision.isPieceSeen() || (Math.abs(ychange) < Constants.MOVE_TO_GAMEPIECE_THRESHOLD))) {
            timeoutCounter.increaseTimeoutCount();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void end(boolean interrupted) {
        drivetrain.drive(new ChassisSpeeds(0, 0, 0));
    }
}
