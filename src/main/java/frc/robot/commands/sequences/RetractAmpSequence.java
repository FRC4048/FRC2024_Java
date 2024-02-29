package frc.robot.commands.sequences;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.amp.DeployAmp;
import frc.robot.commands.amp.RetractAmp;
import frc.robot.commands.ramp.RampMove;
import frc.robot.commands.ramp.RampMoveAndWait;
import frc.robot.constants.Constants;
import frc.robot.constants.GameConstants;
import frc.robot.subsystems.Amp;
import frc.robot.subsystems.Ramp;

public class RetractAmpSequence extends SequentialCommandGroup {
    public RetractAmpSequence(Ramp ramp, Amp amp) {
        addCommands(
                new RampMoveAndWait(ramp, ()-> GameConstants.RAMP_POS_SAFE_AMP_DEPLOY),
                new RetractAmp(amp),
                new RampMove(ramp, ()-> GameConstants.RAMP_POS_STOW)
        );
    }
}