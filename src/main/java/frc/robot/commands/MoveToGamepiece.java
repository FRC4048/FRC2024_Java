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
    private final SwerveDrivetrain drivetrain;
    private final Vision vision;
    private double startTime;
    private final PIDController turningPIDController;
    private final PIDController movingPIDController;
    private double yChange;
    private double pieceNotSeenCounter = 0;
    private double gamePieceX;
    private double gamePieceY;


    public MoveToGamepiece(SwerveDrivetrain drivetrain, Vision vision) {
        this.drivetrain = drivetrain;
        this.vision = vision;
        turningPIDController = new PIDController(Constants.MOVE_TO_GAMEPIECE_TURNING_P, 0, Constants.MOVE_TO_GAMEPIECE_TURNING_D);
        movingPIDController = new PIDController(Constants.MOVE_TO_GAMEPIECE_MOVING_P, 0, 0);
        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        startTime = Timer.getFPGATimestamp();
        pieceNotSeenCounter = 0;
    }

    @Override
    public void execute() {
        if (!vision.isPieceSeen()){
            pieceNotSeenCounter++;
        } else {
            pieceNotSeenCounter = 0;
            gamePieceX = vision.getPieceOffestAngleX();
            gamePieceY = vision.getPieceOffestAngleY();
        }
        yChange = gamePieceY - Constants.LIMELIGHT_MOVE_TO_PIECE_DESIRED_Y;
        double xChange = gamePieceX - Constants.LIMELIGHT_MOVE_TO_PIECE_DESIRED_X;
        Logger.logDouble("/Vision/YError", yChange, Constants.ENABLE_LOGGING);
        Logger.logDouble("/Vision/XError", xChange, Constants.ENABLE_LOGGING);
        if (pieceNotSeenCounter < Constants.LIMELIGHT_PIECE_NOT_SEEN_COUNT && yChange > Constants.MOVE_TO_GAMEPIECE_THRESHOLD){
            double forwardSpeed = movingPIDController.calculate(yChange);
            double turningSpeed = movingPIDController.calculate(xChange);
            Logger.logDouble("/Vision/" + getName() + "/ForwardSpeed", forwardSpeed, Constants.ENABLE_LOGGING);
            Logger.logDouble("/Vision/" + getName() + "/TurningSpeed", turningSpeed, Constants.ENABLE_LOGGING);
            ChassisSpeeds driveStates = new ChassisSpeeds(forwardSpeed, 0, turningSpeed);
            drivetrain.drive(driveStates);
        }
        else {
            Logger.logDouble("/Vision/" + getName() + "/ForwardSpeed", 0.0, Constants.ENABLE_LOGGING);
            Logger.logDouble("/Vision/" + getName() + "/TurningSpeed", 0.0, Constants.ENABLE_LOGGING);
        }
    }

    @Override
    public boolean isFinished() {
        return ((Timer.getFPGATimestamp() - startTime > Constants.MOVE_TO_GAMEPIECE_TIMEOUT) || (yChange <= Constants.MOVE_TO_GAMEPIECE_THRESHOLD) || pieceNotSeenCounter >= Constants.LIMELIGHT_PIECE_NOT_SEEN_COUNT);
    }

    @Override
    public void end(boolean interrupted) {
        Logger.logDouble("/MoveToGamePiece/ForwardSpeed", 0, Constants.ENABLE_LOGGING);
        Logger.logDouble("/Vision/turningSpeed", 0, Constants.ENABLE_LOGGING);
        drivetrain.drive(new ChassisSpeeds(0, 0, 0));
    }
}
