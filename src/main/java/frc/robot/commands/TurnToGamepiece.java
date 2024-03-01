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
import frc.robot.constants.GameConstants;
import frc.robot.subsystems.SwerveDrivetrain;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;

public class TurnToGamepiece extends Command {
    private SwerveDrivetrain drivetrain;
    // private DoubleSupplier x_position;
    // private DoubleSupplier y_position;
    private double startTime;
    private DoubleSubscriber xSub;
    private DoubleSubscriber ySub;
    private DoubleSubscriber det;
    private double x;
    private double y;
    private boolean valid;
    ChassisSpeeds driveStates;
    private boolean finished = false;
    private final ProfiledPIDController TurningPIDController;
    private final ProfiledPIDController MovingPIDController;

    private double cycle;

    public TurnToGamepiece(SwerveDrivetrain drivetrain) {
        this.drivetrain = drivetrain;
        addRequirements(drivetrain);
        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        NetworkTable table = inst.getTable("limelight");
        xSub = table.getDoubleTopic("tx").subscribe(-1);
        ySub = table.getDoubleTopic("ty").subscribe(-1);
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
        cycle = 0;
        startTime = Timer.getFPGATimestamp();
        finished = false;
    }

    @Override
    public void execute() {
        valid = det.get()  > 0;
        x = xSub.get();
        y = ySub.get();
        if (y != 0 && (valid || Timer.getFPGATimestamp() - startTime < GameConstants.TURNTOGAMEPIECE_TIMEOUT)) {
            driveStates = new ChassisSpeeds(-MovingPIDController.calculate(y + 18), 0,
                    TurningPIDController.calculate(x));
            SmartShuffleboard.put("Test", "Speed", TurningPIDController.calculate(x));
            drivetrain.drive(driveStates);
            cycle = 0;
        } else {
            cycle++;
        }

    }

    @Override
    public boolean isFinished() {
        return (y < GameConstants.MAX_Y_ANGLE || cycle > GameConstants.MAX_EXECUTE_CYCLE);
    }

    @Override
    public void end(boolean interrupted) {
        drivetrain.drive(new ChassisSpeeds(0, 0, 0));
    }

}
