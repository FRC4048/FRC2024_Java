package frc.robot;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.swervev2.SwerveDrivetrain;

import java.util.function.DoubleSupplier;

public class Drive extends Command {
    private SwerveDrivetrain drivetrain;

    private DoubleSupplier fwdSupplier, strSupplier, rtSupplier;


    public Drive(
            SwerveDrivetrain drivetrain,
            DoubleSupplier fwdSupplier,
            DoubleSupplier strSupplier,
            DoubleSupplier rtSupplier) {
        addRequirements(drivetrain);

        this.drivetrain = drivetrain;
        this.fwdSupplier = fwdSupplier;
        this.strSupplier = strSupplier;
        this.rtSupplier = rtSupplier;
    }


    @Override
    public void execute() {
        double fwd = MathUtil.applyDeadband(fwdSupplier.getAsDouble()*Constants.MAX_VELOCITY,0.1);
        double str = MathUtil.applyDeadband(strSupplier.getAsDouble()*Constants.MAX_VELOCITY, 0.1);
        double rcw = MathUtil.applyDeadband(rtSupplier.getAsDouble()*Constants.MAX_VELOCITY, 0.1);

        drivetrain.drive(-fwd, -str, -rcw, true);
    }


    @Override
    public boolean isFinished() {
        return false;
    }
}