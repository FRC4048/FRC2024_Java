package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.SwerveDrivetrain;
import frc.robot.subsystems.Vision;
import frc.robot.utils.logging.Logger;

public class MoveToGamepiece extends Command {
    private SwerveDrivetrain drivetrain;
    private Vision vision;
    private double startTime;
    private final PIDController turningPIDController;
    private final PIDController movingPIDController;
    private ChassisSpeeds driveStates;
    private double ychange;
    private double xchange;



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
    }

    @Override
    public void execute() {
        Logger.logDouble("/Vision/XAngle", vision.getPieceOffestAngleX(), Constants.ENABLE_LOGGING);
        Logger.logDouble("/Vision/YAngle", vision.getPieceOffestAngleY(), Constants.ENABLE_LOGGING);
        ychange = vision.getPieceOffestAngleY() - Constants.LIMELIGHT_MOVE_TO_PIECE_DESIRED_Y;
        xchange = vision.getPieceOffestAngleX() - Constants.LIMELIGHT_MOVE_TO_PIECE_DESIRED_X;
        if (vision.isPieceSeen() && (ychange > Constants.MOVE_TO_GAMEPIECE_THRESHOLD)) {
            double forwardSpeed = movingPIDController.calculate(ychange);
            double turningSpeed = turningPIDController.calculate(xchange);
            Logger.logDouble("/Vision/" + getName() + "/ForwardSpeed", forwardSpeed, Constants.ENABLE_LOGGING);
            Logger.logDouble("/Vision/" + getName() + "/turningSpeed", turningSpeed, Constants.ENABLE_LOGGING);
            driveStates = new ChassisSpeeds(forwardSpeed, 0, turningSpeed);
            drivetrain.drive(driveStates);
        }
    }

    @Override
    public boolean isFinished() {
        return ((Timer.getFPGATimestamp() - startTime > Constants.MOVE_TO_GAMEPIECE_TIMEOUT) || (ychange <= Constants.MOVE_TO_GAMEPIECE_THRESHOLD) || !vision.isPieceSeen());
    }

    @Override
    public void end(boolean interrupted) {
        Logger.logDouble("/MoveToGamePiece/ForwardSpeed", 0, Constants.ENABLE_LOGGING);
        Logger.logDouble("/Vision/turningSpeed", 0, Constants.ENABLE_LOGGING);
        drivetrain.drive(new ChassisSpeeds(0, 0, 0));
    }
}
