package frc.robot;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.swervev2.SwerveDrivetrain;

public class Drive extends Command {
    private final SwerveDrivetrain drivetrain;

    private final DoubleSupplier fwdSupplier;
    private final DoubleSupplier strSupplier;
    private final DoubleSupplier rtSupplier;


    public Drive(SwerveDrivetrain drivetrain, DoubleSupplier fwdSupplier, DoubleSupplier strSupplier, DoubleSupplier rtSupplier) {
        addRequirements(drivetrain);
        this.drivetrain = drivetrain;
        this.fwdSupplier = fwdSupplier;
        this.strSupplier = strSupplier;
        this.rtSupplier = rtSupplier;
    }


    @Override
    public void execute() {
        double fwd = MathUtil.applyDeadband(fwdSupplier.getAsDouble()*Constants.MAX_VELOCITY,0.3);
        double str = MathUtil.applyDeadband(strSupplier.getAsDouble()*Constants.MAX_VELOCITY, 0.3);
        double rcw = MathUtil.applyDeadband(rtSupplier.getAsDouble()*Constants.MAX_VELOCITY, 0.3);

        ChassisSpeeds driveStates = drivetrain.createChassisSpeeds(-fwd, -str, -rcw, Constants.FIELD_RELATIVE);
        drivetrain.drive(driveStates);
    }


    @Override
    public boolean isFinished() {
        return false;
    }
}