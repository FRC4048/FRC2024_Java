package frc.robot.commands.drive;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.swervev2.SwerveDrivetrain;

public class WheelAlign extends Command {
    private SwerveDrivetrain drivetrain;
    private int delay = 2;
    private double startTime;

    public WheelAlign(SwerveDrivetrain drivetrain){
        this.drivetrain = drivetrain;
        addRequirements(drivetrain);
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
    }

    @Override
    public void execute() {
        drivetrain.setSteerOffset(Constants.FRONT_LEFT_ABS_ENCODER_ZERO,Constants.FRONT_RIGHT_ABS_ENCODER_ZERO,Constants.BACK_LEFT_ABS_ENCODER_ZERO,Constants.BACK_RIGHT_ABS_ENCODER_ZERO);
    }

    @Override
    public void initialize() {
        super.initialize();
        startTime = Timer.getFPGATimestamp();
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
