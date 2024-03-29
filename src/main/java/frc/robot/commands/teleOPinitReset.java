package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.commands.climber.ResetClimber;
import frc.robot.commands.ramp.ResetRamp;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.LightStrip;
import frc.robot.subsystems.Ramp;

public class teleOPinitReset extends ParallelCommandGroup{
    public teleOPinitReset(Ramp ramp, Climber climber, LightStrip lightStrip) {
        addCommands(
            new ResetRamp(ramp, lightStrip),
            new ResetClimber(climber, lightStrip)
        );
    }
}
