package frc.robot.commands.sequences;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.ramp.ResetRamp;
import frc.robot.commands.ramp.ResetRampEncoder;
import frc.robot.subsystems.Ramp;

public class ResetRampThenEncoder extends SequentialCommandGroup{

    public ResetRampThenEncoder(Ramp ramp) {
        addCommands(
          new ResetRamp(ramp),
          new ResetRampEncoder(ramp)
        );
        addRequirements(ramp);
    }
}
