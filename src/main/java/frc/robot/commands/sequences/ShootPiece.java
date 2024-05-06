package frc.robot.commands.sequences;

import frc.robot.commands.feeder.TimedFeeder;
import frc.robot.commands.ramp.RampMove;
import frc.robot.commands.shooter.StopShooter;
import frc.robot.constants.Constants;
import frc.robot.constants.GameConstants;
import frc.robot.subsystems.feeder.Feeder;
import frc.robot.subsystems.lightstrip.LightStrip;
import frc.robot.subsystems.ramp.Ramp;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.utils.loggingv2.LoggableSequentialCommandGroup;
import frc.robot.utils.loggingv2.LoggableWaitCommand;

public class ShootPiece extends LoggableSequentialCommandGroup {
    public ShootPiece(Shooter shooter, Feeder feeder, Ramp ramp, LightStrip lightStrip) {
        super(
                new TimedFeeder(feeder, lightStrip, Constants.TIMED_FEEDER_EXIT),
                new LoggableWaitCommand(GameConstants.SHOOTER_TIME_BEFORE_STOPPING),
                new StopShooter(shooter),
                new RampMove(ramp, () -> GameConstants.RAMP_POS_STOW)
        );
    }
}
