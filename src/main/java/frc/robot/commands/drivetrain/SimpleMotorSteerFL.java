package frc.robot.commands.drivetrain;
import frc.robot.SwerveV2Drivetrain;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj.Timer;
public class SimpleMotorSteerFL extends Command {
    private final SwerveV2Drivetrain drivetrain;
    private Timer timer = new Timer();
    public SimpleMotorSteerFL(SwerveV2Drivetrain drivetrain) {
        addRequirements(drivetrain);
        this.drivetrain = drivetrain;
    }
    @Override
    public void initialize() {
        timer.reset();
        timer.start();
    }
    @Override
    public void execute() {
        drivetrain.getFrontLeft().getSwerveMotor().getSteerMotor().set(0.1);
    }
    @Override
    public boolean isFinished() {
        if (timer.get() > 0.5) {
            return true;
        }
        return false;
    }
    @Override
    public void end(boolean isFinished) {
        drivetrain.getFrontLeft().getSwerveMotor().getSteerMotor().set(0);
    }
}