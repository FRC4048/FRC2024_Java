package frc.robot.commands.sequences;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.shooter.ShootSpeaker;
import frc.robot.constants.Constants;
import frc.robot.commands.feeder.FeederGamepieceUntilLeave;
import frc.robot.commands.ramp.RampMove;
import frc.robot.commands.ramp.RampMoveWithoutSupplier;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.Ramp;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.SwerveDrivetrain;

public class ExitAndShootFromSpeaker extends SequentialCommandGroup{
    public ExitAndShootFromSpeaker(Shooter shooter, Feeder feeder, SwerveDrivetrain drivetrain, Ramp ramp) {
        addCommands(
            //Small wait needed since waiting for ramp to move to position
            new ParallelCommandGroup(
                new ShootSpeaker(shooter, drivetrain),
                new RampMoveWithoutSupplier(ramp, Constants.SPEAKER_RAMP_ENC_VALUE) 
            ),
                new WaitCommand(Constants.SHOOTING_WAIT),
                new FeederGamepieceUntilLeave(feeder)
        );
    }  
}
