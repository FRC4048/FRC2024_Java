package frc.robot.commands;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.SwerveDrivetrain;
import frc.robot.subsystems.Vision;

public class MoveToGamepiece extends Command {
    private SwerveDrivetrain drivetrain;
    private Vision vision;
    private double startTime;
    private final ProfiledPIDController turningPIDController;
    private final ProfiledPIDController movingPIDController;
    private ChassisSpeeds driveStates;
    private double ychange;


    public MoveToGamepiece(SwerveDrivetrain drivetrain, Vision vision) {
        this.drivetrain = drivetrain;
        this.vision = vision;
        addRequirements(drivetrain);
        TrapezoidProfile.Constraints constraints = new TrapezoidProfile.Constraints(Constants.GAMEPIECE_MAX_VELOCITY, Constants.GAMEPIECE_MAX_ACCELERATION);
        double tP = 0.015;
        double tD = 0.0015;
        double mP = 0.05;
        turningPIDController = new ProfiledPIDController(tP, 0, tD, constraints);
        movingPIDController = new ProfiledPIDController(mP, 0, 0, constraints);

    }

    @Override
    public void initialize() {
        startTime = Timer.getFPGATimestamp();
    }

    @Override
    public void execute() {
        ychange = vision.getPieceOffestAngleY() - Constants.LIMELIGHT_TURN_TO_PIECE_DESIRED_Y;
        if (vision.isPieceSeen() && (Math.abs(ychange) > Constants.MOVE_TO_GAMEPIECE_THRESHOLD)) {
        driveStates = new ChassisSpeeds(movingPIDController.calculate(ychange), 0, turningPIDController.calculate(vision.getPieceOffestAngleX()+Constants.LIMELIGHT_TURN_TO_PIECE_DESIRED_X));
        drivetrain.drive(driveStates);
        }
    }

    @Override
    public boolean isFinished() {
        return (Timer.getFPGATimestamp() - startTime > Constants.MOVE_TO_GAMEPIECE_TIMEOUT) || (!vision.isPieceSeen() || (Math.abs(ychange) < Constants.MOVE_TO_GAMEPIECE_THRESHOLD));
    }

    @Override
    public void end(boolean interrupted) {
        drivetrain.drive(new ChassisSpeeds(0, 0, 0));
    }
}
