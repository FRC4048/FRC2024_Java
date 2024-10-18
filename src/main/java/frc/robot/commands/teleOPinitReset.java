package frc.robot.commands;

import frc.robot.commands.climber.ResetClimber;
import frc.robot.commands.ramp.ResetRamp;
import frc.robot.subsystems.climber.Climber;
import frc.robot.subsystems.lightstrip.LightStrip;
import frc.robot.subsystems.ramp.Ramp;
import frc.robot.utils.loggingv2.LoggableParallelCommandGroup;

public class teleOPinitReset extends LoggableParallelCommandGroup {
    public teleOPinitReset(Ramp ramp, Climber climber, LightStrip lightStrip) {
        addCommands(
            new ResetRamp(ramp, lightStrip),
            new ResetClimber(climber, lightStrip)
        );
    }
}
