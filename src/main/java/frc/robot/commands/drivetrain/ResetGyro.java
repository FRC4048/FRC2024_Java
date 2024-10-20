package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.subsystems.swervev3.SwerveDrivetrain;
import frc.robot.utils.loggingv2.LoggableCommand;

public class ResetGyro extends LoggableCommand {
    private final SwerveDrivetrain drivetrain;
    private final int delay;
    private double startTime;

    public ResetGyro(SwerveDrivetrain drivetrain, int delay){
        this.drivetrain = drivetrain;
        this.delay = delay;
        addRequirements(drivetrain);
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
        drivetrain.resetGyro();
    }

    @Override
    public void initialize() {
        super.initialize();
        startTime = Timer.getFPGATimestamp();
    }

    @Override
    public boolean isFinished() {
        if (Timer.getFPGATimestamp() - startTime >= delay) {
            return true;
        }
        return false;
        //TODO: make it return true after testing
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }  

    
}
