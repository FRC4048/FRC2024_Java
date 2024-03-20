package frc.robot.commands.sequences;

import frc.robot.SpoolIntake;
import frc.robot.commands.deployer.LowerDeployer;
import frc.robot.commands.deployer.RaiseDeployer;
import frc.robot.commands.intake.CurrentBasedIntakeFeeder;
import frc.robot.commands.ramp.RampMoveAndWait;
import frc.robot.constants.Constants;
import frc.robot.constants.GameConstants;
import frc.robot.subsystems.*;
import frc.robot.utils.logging.CommandUtil;
import frc.robot.utils.logging.SequentialLoggingCommand;

/**
 * Sequence to start intaking, this takes care of the lowering/raising the deployer,
 * as well as starting/stopping the intake and feeder.
 */
public class StartIntakeAndFeeder extends SequentialLoggingCommand {
    @Override
    public void execute() {
        super.execute();
    }

    public StartIntakeAndFeeder(Feeder feeder, IntakeSubsystem intake, Deployer deployer, Ramp ramp, LightStrip lightStrip) {
        super("StartIntakeAndFeeder",
                CommandUtil.parallel("First",
                        new SpoolIntake(intake, Constants.INTAKE_SPOOL_TIME),
                        new LowerDeployer(deployer, lightStrip),
                        new RampMoveAndWait(ramp,lightStrip ,() -> GameConstants.RAMP_POS_STOW)
                ), new CurrentBasedIntakeFeeder(intake, feeder, lightStrip),
                new RaiseDeployer(deployer, lightStrip)
        );
    }
}
