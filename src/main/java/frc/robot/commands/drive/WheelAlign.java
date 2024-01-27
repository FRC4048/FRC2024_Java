package frc.robot.commands.drive;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.swervev2.SwerveDrivetrain;

public class WheelAlign extends Command {
    private final SwerveDrivetrain drivetrain;
    private final double degree;

    public WheelAlign(SwerveDrivetrain drivetrain){
        this.drivetrain = drivetrain;
        this.degree = 0;
        addRequirements(drivetrain);
    }
    public WheelAlign(SwerveDrivetrain drivetrain, double degree){
        this.degree = degree;
        this.drivetrain = drivetrain;
        addRequirements(drivetrain);
    }
    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
    }

    @Override
    public void initialize() {
        super.initialize();
        drivetrain.setSteerOffset(Constants.FRONT_LEFT_ABS_ENCODER_ZERO-degree,Constants.FRONT_RIGHT_ABS_ENCODER_ZERO,Constants.BACK_LEFT_ABS_ENCODER_ZERO-degree,Constants.BACK_RIGHT_ABS_ENCODER_ZERO-degree);
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }
}
