package frc.robot.commands.sequences;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.ramp.ResetRamp;
import frc.robot.subsystems.Ramp;

public class ResetRampThenEncoder extends SequentialCommandGroup{

    public ResetRampThenEncoder(Ramp ramp) {
        addCommands(
          new ResetRamp(ramp)
          //new WaitCommand(0.5),
          //new ResetRampEncoder(ramp)
        );
        addRequirements(ramp);
    }
}
