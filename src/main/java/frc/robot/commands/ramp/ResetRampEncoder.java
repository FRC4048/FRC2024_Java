package frc.robot.commands.ramp;
import frc.robot.subsystems.Ramp;
import edu.wpi.first.wpilibj2.command.Command;

public class ResetRampEncoder extends Command{
    private Ramp ramp;
    public ResetRampEncoder(Ramp ramp) {
        this.ramp = ramp;
    }
    @Override
    public void execute() {
        ramp.resetEncoder();
    }
    @Override
    public boolean isFinished() {
        return true;
    }
}
