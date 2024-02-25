package frc.robot.commands.sequences;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.amp.DeployAmp;
import frc.robot.commands.ramp.RampMove;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Amp;
import frc.robot.subsystems.Ramp;

public class DeployAmpSequence extends ParallelCommandGroup {
    public DeployAmpSequence(Ramp ramp, Amp amp) {
        addCommands(
            new RampMove(ramp, ()->Constants.RAMP_ANGLE),
            new SequentialCommandGroup(
                new WaitCommand(3.5),
                new DeployAmp(amp)
            )

        );
    }
}
