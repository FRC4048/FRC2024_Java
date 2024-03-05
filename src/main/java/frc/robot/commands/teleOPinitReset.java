package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.commands.ramp.ResetRamp;
import frc.robot.commands.climber.ResetClimber;
import frc.robot.subsystems.Ramp;
import frc.robot.subsystems.Climber;

public class teleOPinitReset extends ParallelCommandGroup{
    public teleOPinitReset(Ramp ramp, Climber climber) {
        addCommands(
            new ResetRamp(ramp),
            new ResetClimber(climber)
        );
    }
}
