package frc.robot.commands.drivetrain;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.subsystems.swervev3.SwerveDrivetrain;
import frc.robot.utils.loggingv2.LoggableCommand;

public class PointForwards extends LoggableCommand {
    private final SwerveDrivetrain drivetrain;
    private final Timer timer = new Timer();


    public PointForwards(SwerveDrivetrain drivetrain) {
        this.drivetrain = drivetrain;
        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        timer.reset();
    }

    @Override
    public void execute() {
        drivetrain.drive(ChassisSpeeds.fromRobotRelativeSpeeds(0,0,0, Rotation2d.fromDegrees(0)));
    }

    @Override
    public boolean isFinished() {
        return timer.hasElapsed(3);
    }
}
