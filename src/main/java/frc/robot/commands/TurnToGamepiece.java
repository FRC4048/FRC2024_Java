// package frc.robot.commands;

// import java.util.function.DoubleSupplier;

// import edu.wpi.first.math.MathUtil;
// import edu.wpi.first.math.kinematics.ChassisSpeeds;
// import edu.wpi.first.networktables.DoubleSubscriber;
// import edu.wpi.first.networktables.NetworkTable;
// import edu.wpi.first.networktables.NetworkTableInstance;
// import edu.wpi.first.wpilibj2.command.Command;
// import frc.robot.constants.Constants;
// import frc.robot.subsystems.SwerveDrivetrain;
// import frc.robot.utils.smartshuffleboard.SmartShuffleboard;

// public class TurnToGamepiece extends Command {
//     private SwerveDrivetrain drivetrain;
//     // private DoubleSupplier x_position;
//     // private DoubleSupplier y_position;
//     private double rcw;
//     private double fwd;
//     private double str;
//     private DoubleSupplier rtSupplier;
//     private DoubleSubscriber xSub;
//     private DoubleSubscriber ySub;
//     private DoubleSubscriber zSub;
//     private DoubleSubscriber fpsSub;
//     private DoubleSubscriber probSub;
//     private double x;
//     private double y;
//     private double startX;
//     private double startY;
//     private double z;
//     private double fps;
//     private double prob;
//     ChassisSpeeds driveStates;
//     double first = 0;
//     double cycle = 0;
//     private boolean finished = false;
//     double startPose;

//     public TurnToGamepiece(SwerveDrivetrain drivetrain) {
//         this.drivetrain = drivetrain;
//         addRequirements(drivetrain);
//         NetworkTableInstance inst = NetworkTableInstance.getDefault();
//         NetworkTable table = inst.getTable("Luxonis");
//         xSub = table.getDoubleTopic("x").subscribe(-1);
//         ySub = table.getDoubleTopic("y").subscribe(-1);
//         zSub = table.getDoubleTopic("z").subscribe(-1);
//         fpsSub = table.getDoubleTopic("fps").subscribe(-1);
//         probSub = table.getDoubleTopic("prob").subscribe(-1);
//     }

//     @Override
//     public void initialize() {
//         super.initialize();
//         double fwd = 0;
//         double str = 0;
//         // double rcw =
//         // MathUtil.applyDeadband(rtSupplier.getAsDouble()*Constants.MAX_VELOCITY, 0.3);
//         double rcw = 0.3;
//         first = 0;
//         finished = false;
//         cycle = 0;
//         startPose = drivetrain.getPose().getRotation().getDegrees();
//         startX = xSub.get();
//         startY = ySub.get();
//         z = zSub.get();
//         fps = fpsSub.get();
//         prob = probSub.get();

        

//     }

//     @Override
//     public void execute() {

//         x = xSub.get();
//         y = ySub.get();
            
//         if ((Math.abs(drivetrain.getPose().getRotation().getDegrees()) - Math.abs(startPose) > Math.abs(Math.atan(startY/startX)) && first == 0)) {
            

//             if (x < 0) {
//                 driveStates = drivetrain.createChassisSpeeds(0, 0, .1, false);
//                 drivetrain.drive(driveStates);
//             } else if (x > 0) {
//                 driveStates = drivetrain.createChassisSpeeds(0, 0, -.1, false);
//                 drivetrain.drive(driveStates);
//             }
//         }
//         else {
//             str = .3;
//             fwd = 0;
//             driveStates = drivetrain.createChassisSpeeds(1, 0, 0, false);
//             drivetrain.drive(driveStates);
//             if (y == 0) {
                
//                 if (cycle == 30) {
//                     driveStates = drivetrain.createChassisSpeeds(0, 0, 0, false);
//                     drivetrain.drive(driveStates);
//                     finished = true;
//                 }
//                 cycle++;
//             }
//             first++;
//         }
//     }

//     @Override
//     public boolean isFinished() {
        
//         return finished;
        
//     }

//     @Override
//     public void end(boolean interrupted) {
        
//         }
        
//     }
package frc.robot.commands;

import java.util.function.DoubleSupplier;

import com.pathplanner.lib.path.GoalEndState;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerPath;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.networktables.DoubleSubscriber;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.SwerveDrivetrain;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;
import java.util.List;

public class TurnToGamepiece extends Command {
    private SwerveDrivetrain drivetrain;
    // private DoubleSupplier x_position;
    // private DoubleSupplier y_position;
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
    private double startX;
    private double startY;
    private double z;
    private double fps;
    private double prob;
    ChassisSpeeds driveStates;
    double first = 0;
    double cycle = 0;
    private boolean finished = false;
    double startPose;
    private List<Translation2d> bezierPoints;
    private PathPlannerPath path;

    public TurnToGamepiece(SwerveDrivetrain drivetrain) {
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
        // double rcw =
        // MathUtil.applyDeadband(rtSupplier.getAsDouble()*Constants.MAX_VELOCITY, 0.3);
        double rcw = 0.3;
        
        first = 0;
        finished = false;
        cycle = 0;
        startPose = drivetrain.getPose().getRotation().getDegrees();
        startX = xSub.get();
        startY = ySub.get();
        double distance = startY+.5;
        double degrees = Math.abs(Math.atan(startY+.5/startX));
        z = zSub.get();
        fps = fpsSub.get();
        prob = probSub.get();
        bezierPoints = PathPlannerPath.bezierFromPoses(new Pose2d(0.333*distance*Math.cos(Math.toRadians(degrees)), 0.333*distance*Math.sin(Math.toRadians(degrees)), Rotation2d.fromDegrees(degrees)), new Pose2d(distance*Math.cos(Math.toRadians(degrees)), distance*Math.sin(Math.toRadians(degrees)), Rotation2d.fromDegrees(degrees)));
        path = new PathPlannerPath(bezierPoints, new PathConstraints(.25, .25, 2 * Math.PI, 4 * Math.PI),  new GoalEndState(0.0, Rotation2d.fromDegrees(degrees)));
        path.preventFlipping = true;

        

    }

    @Override
    public void execute() {

        x = xSub.get();
        y = ySub.get();
        
        
    }
            
    
    @Override
    public boolean isFinished() {
        
        return finished;
        
    }

    @Override
    public void end(boolean interrupted) {
        
        }
        
    }

