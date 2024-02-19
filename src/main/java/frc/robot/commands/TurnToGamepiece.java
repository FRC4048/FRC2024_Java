package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.networktables.DoubleSubscriber;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.SwerveDrivetrain;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;

public class TurnToGamepiece extends Command{
    private SwerveDrivetrain drivetrain;
    //private DoubleSupplier x_position;
    //private DoubleSupplier y_position;
    private double rcw;
    private double fwd;
    private double str;
    private DoubleSupplier rtSupplier;
    private DoubleSubscriber xSub;
    private DoubleSubscriber ySub;
    private DoubleSubscriber zSub;
    private DoubleSubscriber fpsSub;
    private DoubleSubscriber probSub;
    private double x;
    private double y;
    private double z;
    private double fps;
    private double prob;
    ChassisSpeeds driveStates;
    private boolean finished = false;


    public void TurnToGamepiece(SwerveDrivetrain drivetrain) {
        this.drivetrain = drivetrain;
        addRequirements(drivetrain);
        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        NetworkTable table = inst.getTable("Luxonis");
        xSub = table.getDoubleTopic("x").subscribe(-1);
        ySub = table.getDoubleTopic("y").subscribe(-1);
        zSub = table.getDoubleTopic("z").subscribe(-1);
        fpsSub = table.getDoubleTopic("fps").subscribe(-1);
        probSub = table.getDoubleTopic("prob").subscribe(-1);
    }

    @Override
    public void initialize() {
        super.initialize();
        double fwd = 0;
        double str = 0;
        double rcw = MathUtil.applyDeadband(rtSupplier.getAsDouble()*Constants.MAX_VELOCITY, 0.3);

    }

    @Override
    public void execute() {
        if (-.1 > x || .1 < x) {
        x = xSub.get();
        y = ySub.get();
        z = zSub.get();
        fps = fpsSub.get();
        prob = probSub.get();
        SmartShuffleboard.put("Test", "x", x);
        SmartShuffleboard.put("Test", "y", y);
        SmartShuffleboard.put("Test", "z", z);
        SmartShuffleboard.put("Test", "prob", prob);
        SmartShuffleboard.put("Test", "fps", fps);
        if (x < -.1) {
                
                driveStates = drivetrain.createChassisSpeeds(-fwd, -str, rcw, true);
                drivetrain.drive(driveStates);
            }
         else if (x > .1) {
                driveStates = drivetrain.createChassisSpeeds(-fwd, -str, -rcw, true);
                drivetrain.drive(driveStates);
        } 
        } if (-.1 < x || .1 > x) {
            str = MathUtil.applyDeadband(rtSupplier.getAsDouble()*Constants.MAX_VELOCITY, 0.3);
            rcw = 0;
            driveStates = drivetrain.createChassisSpeeds(-fwd, str, -rcw, true);
            drivetrain.drive(driveStates);
            if (y == -1) {
                driveStates = drivetrain.createChassisSpeeds(0, 0, 0, true);
                drivetrain.drive(driveStates);
                finished = true;
            }
        }
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
    }

        
        
} 