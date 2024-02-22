package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
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
    private double startX;
    private double startY;
    ChassisSpeeds driveStates;
    double first = 0;
    double cycle = 0;
    private boolean finished = false;
    double startPose;
    private final ProfiledPIDController turningPIDController;


    public TurnToGamepiece(SwerveDrivetrain drivetrain) {
        this.drivetrain = drivetrain;
        addRequirements(drivetrain);
        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        NetworkTable table = inst.getTable("Luxonis");
        xSub = table.getDoubleTopic("x").subscribe(-1);
        ySub = table.getDoubleTopic("y").subscribe(-1);
        turningPIDController = new ProfiledPIDController(.1, .1, .1);

    }

    @Override
    public void initialize() {
        super.initialize();
        first = 0;
        finished = false;
        cycle = 0;
        startPose = drivetrain.getPose().getRotation().getDegrees();
        startX = xSub.get();
        startY = ySub.get();


        

    }

    @Override
    public void execute() {

        x = xSub.get();
        y = ySub.get();
            
        if ((drivetrain.getPose().getRotation().getDegrees() - startPose > Math.atan(startY/startX) && first == 0)) {
            

            if (x < 0) {
                driveStates = drivetrain.createChassisSpeeds(0, 0, .1, false);
                drivetrain.drive(driveStates);
            } else if (x > 0) {
                driveStates = drivetrain.createChassisSpeeds(0, 0, -.1, false);
                drivetrain.drive(driveStates);
            }
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
