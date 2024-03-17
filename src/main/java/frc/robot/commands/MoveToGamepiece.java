package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.SwerveDrivetrain;
import frc.robot.subsystems.Vision;

public class MoveToGamepiece extends Command {
    private SwerveDrivetrain drivetrain;
    private Vision vision;
    private double startTime;
    private final PIDController turningPIDController;
    private final PIDController movingPIDController;
    private ChassisSpeeds driveStates;
    private double ychange;
    private double xchange;
    private double cycle;



    public MoveToGamepiece(SwerveDrivetrain drivetrain, Vision vision) {
        this.drivetrain = drivetrain;
        this.vision = vision;
        addRequirements(drivetrain);
        turningPIDController = new PIDController(Constants.MOVE_TO_GAMEPIECE_TURNING_P, 0, Constants.MOVE_TO_GAMEPIECE_TURNING_D);
        movingPIDController = new PIDController(Constants.MOVE_TO_GAMEPIECE_MOVING_P, 0, 0);

    }

    @Override
    public void initialize() {
        startTime = Timer.getFPGATimestamp();
        cycle = 0;
    }

    @Override
    public void execute() {
        ychange = vision.getPieceOffestAngleY() - Constants.LIMELIGHT_TURN_TO_PIECE_DESIRED_Y;
        xchange = vision.getPieceOffestAngleX() - Constants.LIMELIGHT_TURN_TO_PIECE_DESIRED_X;
        if (vision.isPieceSeen() && (Math.abs(ychange) > Constants.MOVE_TO_GAMEPIECE_THRESHOLD)) {
        driveStates = new ChassisSpeeds(movingPIDController.calculate(ychange), 0, turningPIDController.calculate(xchange));
        drivetrain.drive(driveStates);
        }
        if (vision.isPieceSeen()) {
            cycle = 0;
        } else {
            cycle++;
        }
    }

    @Override
    public boolean isFinished() {
        return ((Timer.getFPGATimestamp() - startTime > Constants.MOVE_TO_GAMEPIECE_TIMEOUT) || (Math.abs(ychange) < Constants.MOVE_TO_GAMEPIECE_THRESHOLD) || (cycle > 5));
    }

    @Override
    public void end(boolean interrupted) {
        drivetrain.drive(new ChassisSpeeds(0, 0, 0));
    }
}
