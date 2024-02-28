package frc.robot.commands.sequences;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.feeder.FeederGamepieceUntilLeave;
import frc.robot.commands.ramp.RampMove;
import frc.robot.commands.ramp.RampMoveWithoutSupplier;
import frc.robot.commands.shooter.ShootAmp;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.Ramp;
import frc.robot.subsystems.Shooter;

public class ExitAndShootFromAmp extends SequentialCommandGroup{
    public ExitAndShootFromAmp(Shooter shooter, Feeder feeder, Ramp ramp) {
        addCommands(
            //Small wait needed since waiting for ramp to move to position
            new ParallelCommandGroup(
                new ShootAmp(shooter),
                new RampMoveWithoutSupplier(ramp, Constants.AMP_RAMP_ENC_VALUE) 
            ),
                new WaitCommand(Constants.SHOOTING_WAIT),
                new FeederGamepieceUntilLeave(feeder)
        );
    }  
}