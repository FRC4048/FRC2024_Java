package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SwerveDrivetrain;
import frc.robot.utils.TimeoutCounter;

public class ResetGyro extends Command {
    private final SwerveDrivetrain drivetrain;
    private final int delay;
    private double startTime;
    private final TimeoutCounter timeoutCounter = new TimeoutCounter("Reset Gyro");

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
            timeoutCounter.increaseTimeoutCount();
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
