package frc.robot.commands.sequences;

import frc.robot.commands.SpoolIntake;
import frc.robot.commands.deployer.LowerDeployer;
import frc.robot.commands.deployer.RaiseDeployer;
import frc.robot.commands.intake.CurrentBasedIntakeFeeder;
import frc.robot.commands.ramp.RampMoveAndWait;
import frc.robot.constants.Constants;
import frc.robot.constants.GameConstants;
import frc.robot.subsystems.deployer.Deployer;
import frc.robot.subsystems.feeder.Feeder;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.lightstrip.LightStrip;
import frc.robot.subsystems.ramp.Ramp;
import frc.robot.utils.loggingv2.LoggableParallelCommandGroup;
import frc.robot.utils.loggingv2.LoggableSequentialCommandGroup;

/**
 * Sequence to start intaking, this takes care of the lowering/raising the deployer,
 * as well as starting/stopping the intake and feeder.
 */
public class StartIntakeAndFeeder extends LoggableSequentialCommandGroup {

    public StartIntakeAndFeeder(Feeder feeder, Intake intake, Deployer deployer, Ramp ramp, LightStrip lightStrip) {
        super(new LoggableParallelCommandGroup(
                        new SpoolIntake(intake, Constants.INTAKE_SPOOL_TIME),
                        new LowerDeployer(deployer, lightStrip),
                        new RampMoveAndWait(ramp,lightStrip ,() -> GameConstants.RAMP_POS_STOW)
                ), new CurrentBasedIntakeFeeder(intake, feeder, lightStrip),
                new RaiseDeployer(deployer, lightStrip)
        );
    }
}
