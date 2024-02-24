package frc.robot.commands.sequences;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.amp.DeployAmp;
import frc.robot.commands.amp.RetractAmp;
import frc.robot.commands.ramp.RampMove;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Amp;
import frc.robot.subsystems.Ramp;

public class RetractAmpSequence extends ParallelCommandGroup {
    public RetractAmpSequence(Ramp ramp, Amp amp) {
        addCommands(
            new SequentialCommandGroup(
                new RampMove(ramp, ()->Constants.RAMP_ANGLE),
                new WaitCommand(3.5),
                new RetractAmp(amp),
                new RampMove(ramp, ()->0)
            )

        );
    }
}