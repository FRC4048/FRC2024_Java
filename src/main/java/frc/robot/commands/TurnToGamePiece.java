package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.Drive;
import frc.robot.commands.drive.WheelAlign;
import frc.robot.subsystems.swervev2.SwerveDrivetrain;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;

public class TurnToGamePiece extends Command{
    private final SwerveDrivetrain drivetrain;
    //private DoubleSupplier x_position;
    //private DoubleSupplier y_position;
    private double x_position;
    private double y_position;
    private double rcw;
    private double fwd;
    private double str;
    private DoubleSupplier rtSupplier;


    public TurnToGamePiece(SwerveDrivetrain drivetrain, double x_position, double y_position) {
        this.x_position = x_position;
        this.y_position = y_position;
        this.drivetrain = drivetrain;
        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        super.initialize();
        double fwd = 0;
        double str = 0;
    }

    @Override
    public void execute() {
        if (y_position < -.1) {
                double rcw = MathUtil.applyDeadband(rtSupplier.getAsDouble()*Constants.MAX_VELOCITY, 0.3);
                ChassisSpeeds driveStates = drivetrain.createChassisSpeeds(-fwd, -str, -rcw, true);
                drivetrain.drive(driveStates);
            }
         else if (y_position > .1) {
                double rcw = MathUtil.applyDeadband(rtSupplier.getAsDouble()*Constants.MAX_VELOCITY, -0.3);
                ChassisSpeeds driveStates = drivetrain.createChassisSpeeds(-fwd, -str, -rcw, true);
                drivetrain.drive(driveStates);
        } 
    }

    @Override
    public boolean isFinished() {
        if (-.1 < y_position || .1 > y_position) {
            return true;
        }
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
    }

        
        
} 

