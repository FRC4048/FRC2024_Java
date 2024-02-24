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
            new RetractAmp(amp),
            new SequentialCommandGroup(
                new WaitCommand(0.5),
                new RampMove(ramp, -1*Constants.RAMP_ANGLE)
            )

        );
    }
}