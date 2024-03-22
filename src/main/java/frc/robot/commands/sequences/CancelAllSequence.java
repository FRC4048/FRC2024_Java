package frc.robot.commands.sequences;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.CancelAll;
import frc.robot.subsystems.LightStrip;
import frc.robot.subsystems.Ramp;
import frc.robot.subsystems.Shooter;

public class CancelAllSequence extends SequentialCommandGroup {
    public CancelAllSequence(Ramp ramp, Shooter shooter, LightStrip lightStrip) {
        addCommands(
            new CancelAll(ramp, shooter, lightStrip)
        );
    }
}
