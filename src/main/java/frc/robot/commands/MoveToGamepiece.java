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
    private double timeSincePieceLoss;
    private final ProfiledPIDController turningPIDController;
    private final ProfiledPIDController movingPIDController;
    private boolean shouldMove;
    private final TimeoutCounter timeoutcounter;

    public MoveToGamepiece(SwerveDrivetrain drivetrain, Vision vision) {
        timeoutcounter = new TimeoutCounter(getName());
        this.drivetrain = drivetrain;
        this.vision = vision;
        addRequirements(drivetrain);
        TrapezoidProfile.Constraints constraints = new TrapezoidProfile.Constraints(Constants.GAMEPIECE_MAX_VELOCITY, Constants.GAMEPIECE_MAX_ACCELERATION);
        double tP = 0.04;
        double mP = 0.05;
        turningPIDController = new ProfiledPIDController(tP, 0, 0, constraints);
        movingPIDController = new ProfiledPIDController(mP, 0, 0, constraints);

    }

    @Override
    public void initialize() {
        startTime = Timer.getFPGATimestamp();
        timeSincePieceLoss = Timer.getFPGATimestamp();
        shouldMove = !(vision.isPieceSeen());
    }

    @Override
    public void execute() {
        if (shouldMove) {
            ChassisSpeeds driveStates;
            double ychange = vision.getPieceOffestAngleY() - Constants.LIMELIGHT_TURN_TO_PIECE_DESIRED_Y;
            if (vision.isPieceSeen() && (Math.abs(ychange) > Constants.MOVE_TO_GAMEPIECE_THRESHOLD)) {
                timeSincePieceLoss = Timer.getFPGATimestamp();
                driveStates = new ChassisSpeeds(movingPIDController.calculate(ychange), 0, turningPIDController.calculate(vision.getPieceOffestAngleX()));
            }
            else driveStates = drivetrain.createChassisSpeeds(-0.6, 0.0, 0.0, false);
            drivetrain.drive(driveStates);
        }
    }

    @Override
    public boolean isFinished() {
        if ((Timer.getFPGATimestamp() - startTime > Constants.MOVE_TO_GAMEPIECE_TIMEOUT)) {
            timeoutcounter.increaseTimeoutCount();
            return true;
        }
        return ((Timer.getFPGATimestamp() - timeSincePieceLoss >= Constants.TIMEOUT_AFTER_PIECE_NOT_SEEN) || shouldMove);
    }

    @Override
    public void end(boolean interrupted) {
        drivetrain.drive(new ChassisSpeeds(0, 0, 0));
    }

}
