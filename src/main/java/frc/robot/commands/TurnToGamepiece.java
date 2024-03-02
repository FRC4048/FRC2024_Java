package frc.robot.commands;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.SwerveDrivetrain;
import frc.robot.subsystems.Vision;

public class TurnToGamepiece extends Command {
    private SwerveDrivetrain drivetrain;
    private Vision vision;
    private double startTime;
    private double timeSincePieceLoss;
    private final ProfiledPIDController turningPIDController;
    private final ProfiledPIDController movingPIDController;

    public TurnToGamepiece(SwerveDrivetrain drivetrain, Vision vision) {
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
    }

    @Override
    public void execute() {
        ChassisSpeeds driveStates;
        double ychange = vision.getPieceOffestAngleY() - Constants.LIMELIGHT_TURN_TO_PIECE_DESIRED_Y;
        if (vision.isPieceSeen() && (Math.abs(ychange) > Constants.TURN_TO_GAME_PIECE_THRESHOLD)) {
            timeSincePieceLoss = Timer.getFPGATimestamp();
            driveStates = drivetrain.createChassisSpeeds(0.6, 0.0, 0.0, false);
        }
        else driveStates = new ChassisSpeeds(-movingPIDController.calculate(ychange), 0, turningPIDController.calculate(vision.getPieceOffestAngleX()));
        drivetrain.drive(driveStates);
    }

    @Override
    public boolean isFinished() {
        return ((Timer.getFPGATimestamp() - timeSincePieceLoss >= Constants.TIMEOUT_AFTER_PIECE_NOT_SEEN) || (Timer.getFPGATimestamp() - startTime > Constants.TURN_TO_GAMEPIECE_TIMEOUT || !vision.isPieceSeen()));
    }

    @Override
    public void end(boolean interrupted) {
        drivetrain.drive(new ChassisSpeeds(0, 0, 0));
    }

}
