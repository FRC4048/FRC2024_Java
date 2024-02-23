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
    private DoubleSubscriber zSub;
    private double x;
    private double y;
    private double startX;
    private double startY;
    ChassisSpeeds driveStates;
    double first = 0;
    double cycle = 0;
    private boolean finished = false;
    double startPose;
    private final ProfiledPIDController PIDController;
    private double rwd;


    public TurnToGamepiece(SwerveDrivetrain drivetrain) {
        this.drivetrain = drivetrain;
        addRequirements(drivetrain);
        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        NetworkTable table = inst.getTable("Luxonis");
        xSub = table.getDoubleTopic("x").subscribe(-1);
        ySub = table.getDoubleTopic("y").subscribe(-1);
        zSub = table.getDoubleTopic("z").subscribe(-1);
        TrapezoidProfile.Constraints constraints = new TrapezoidProfile.Constraints(Constants.MAX_ANGULAR_SPEED * 150, 2 * Math.PI * 150);
        PIDController = new ProfiledPIDController(.01, 0, .00, constraints);

    }

    @Override
    public void initialize() {
        super.initialize();
        first = 0;
        finished = false;
        cycle = 0;
        startPose = drivetrain.getGyroAngle().getDegrees();
        startX = xSub.get();
        startY = ySub.get();
        rwd = 0;


        

    }

    @Override
    public void execute() {
        rwd = PIDController.calculate(Math.toDegrees(Math.atan(y/x)));
        x = xSub.get();
        y = ySub.get();
        System.out.println(Math.abs(Math.toDegrees(Math.atan(startY/startX))));

        
            
        if ((Math.abs(drivetrain.getGyroAngle().getDegrees() - startPose) < Math.abs(Math.toDegrees(Math.atan(startY/startX))))) {
            driveStates = drivetrain.createChassisSpeeds(0, 0, -rwd, false);
            drivetrain.drive(driveStates);
        }
        else {
            driveStates = drivetrain.createChassisSpeeds(1, 0, 0, false);
            drivetrain.drive(driveStates);
            if (y == 0) {
                
                if (cycle == 30) {
                    driveStates = drivetrain.createChassisSpeeds(0, 0, 0, false);
                    drivetrain.drive(driveStates);
                    finished = true;
                }
                cycle++;
            }
            first++;
        }
    }

    @Override
    public boolean isFinished() {
        
        return finished;
        
    }

    @Override
    public void end(boolean interrupted) {
        
    }
        
    }
