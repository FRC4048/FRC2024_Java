package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.networktables.DoubleSubscriber;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.SwerveDrivetrain;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;

public class TurnToGamepiece extends Command {
    private SwerveDrivetrain drivetrain;
    // private DoubleSupplier x_position;
    // private DoubleSupplier y_position;
    private DoubleSubscriber xSub;
    private DoubleSubscriber ySub;
    private double x;
    private double y;
    ChassisSpeeds driveStates;
    private boolean finished = false;
    private final ProfiledPIDController PIDController;
    private double cycle;


    public TurnToGamepiece(SwerveDrivetrain drivetrain) {
        this.drivetrain = drivetrain;
        addRequirements(drivetrain);
        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        NetworkTable table = inst.getTable("Luxonis");
        xSub = table.getDoubleTopic("x").subscribe(-1);
        ySub = table.getDoubleTopic("y").subscribe(-1);

        TrapezoidProfile.Constraints constraints = new TrapezoidProfile.Constraints(Constants.MAX_ANGULAR_SPEED * 150, 2 * Math.PI * 150);
        PIDController = new ProfiledPIDController(.0001, 0, .00, constraints);

    }

    @Override
    public void initialize() {
        finished = false;
    }

    @Override
    public void execute() {
        x = xSub.get();
        y = ySub.get();
        if (Math.abs(x) >.1) {
            driveStates = new ChassisSpeeds(0, 0, PIDController.calculate(x));
            drivetrain.drive(driveStates);
        } else {
            finished = true;
        }
    }
    //     } else if (y > -.4) {
    //         driveStates = new ChassisSpeeds(.5, 0, 0);
    //         drivetrain.drive(driveStates);
    //     } else {
    //         if (cycle > 30) {
    //             driveStates = new ChassisSpeeds(0, 0, 0);
    //             drivetrain.drive(driveStates);
    //             finished = true;
    //         }
    //     }        
    // }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public void end(boolean interrupted) {
        
    }
        
    }
