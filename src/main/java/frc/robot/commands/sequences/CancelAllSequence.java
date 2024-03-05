package frc.robot.commands.sequences;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.CancelAll;
import frc.robot.commands.amp.RetractAmp;
import frc.robot.subsystems.Amp;
import frc.robot.subsystems.Ramp;
import frc.robot.subsystems.Shooter;

public class CancelAllSequence extends SequentialCommandGroup {
    public CancelAllSequence(Ramp ramp, Shooter shooter, Amp amp) {
        addCommands(
            new RetractAmp(amp),
            new CancelAll(ramp, shooter)
        );
    }
}
