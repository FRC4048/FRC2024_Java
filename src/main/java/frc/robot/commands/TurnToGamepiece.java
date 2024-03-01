package frc.robot.commands;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.networktables.DoubleSubscriber;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.SwerveDrivetrain;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;

public class TurnToGamepiece extends Command {
    private SwerveDrivetrain drivetrain;
    private double startTime;
    private DoubleSubscriber xSub;
    private DoubleSubscriber ySub;
    private DoubleSubscriber det;
    ChassisSpeeds driveStates;
    private final ProfiledPIDController TurningPIDController;
    private final ProfiledPIDController MovingPIDController;

    private double timeOfLastPieceLoss;

    public TurnToGamepiece(SwerveDrivetrain drivetrain) {
        this.drivetrain = drivetrain;
        addRequirements(drivetrain);
        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        NetworkTable table = inst.getTable("limelight");
        xSub = table.getDoubleTopic("tx").subscribe(-1000);
        ySub = table.getDoubleTopic("ty").subscribe(-1000);
        det = table.getDoubleTopic("tv").subscribe(-1);

        TrapezoidProfile.Constraints constraints = new TrapezoidProfile.Constraints(Constants.GAMEPIECE_MAX_VELOCITY,
                Constants.GAMEPIECE_MAX_ACCELERATION);
        double tP = 0.04;
        double mP = 0.05;
        TurningPIDController = new ProfiledPIDController(tP, 0, 0, constraints);
        MovingPIDController = new ProfiledPIDController(mP, 0, 0, constraints);

    }

    @Override
    public void initialize() {
        timeOfLastPieceLoss = 0;
        startTime = Timer.getFPGATimestamp();
    }

    @Override
    public void execute() {
        if (ySub.get() != 0 && det.get() == 1) {
            driveStates = new ChassisSpeeds(-MovingPIDController.calculate(ySub.get() + Constants.LIMELIGHT_TURN_TO_PIECE_DESIRED_Y), 0, TurningPIDController.calculate(xSub.get()));
            if (Constants.VISION_DEBUG) SmartShuffleboard.put("Test", "Speed", TurningPIDController.calculate(xSub.get()));
            drivetrain.drive(driveStates);
            timeOfLastPieceLoss = 0;
        } else {
            if (timeOfLastPieceLoss == 0) timeOfLastPieceLoss = Timer.getFPGATimestamp();
        }

    }

    @Override
    public boolean isFinished() {
        return (ySub.get() < Constants.MAX_Y_ANGLE || (Timer.getFPGATimestamp() - timeOfLastPieceLoss) == Constants.PIECE_LOST_TIME_THRESHOLD || (Timer.getFPGATimestamp() - startTime > Constants.TURNTOGAMEPIECE_TIMEOUT));
    }

    @Override
    public void end(boolean interrupted) {
        drivetrain.drive(new ChassisSpeeds(0, 0, 0));
    }

}
