package frc.robot.commands.sequences;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.SpoolIntake;
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

/**
 * Sequence to start intaking, this takes care of the lowering/raising the deployer,
 * as well as starting/stopping the intake and feeder.
 */
public class StartIntakeAndFeeder extends SequentialCommandGroup {

    public StartIntakeAndFeeder(Feeder feeder, Intake intake, Deployer deployer, Ramp ramp, LightStrip lightStrip) {
        super(new ParallelCommandGroup(
                        new SpoolIntake(intake, Constants.INTAKE_SPOOL_TIME),
                        new LowerDeployer(deployer, lightStrip),
                        new RampMoveAndWait(ramp,lightStrip ,() -> GameConstants.RAMP_POS_STOW)
                ), new CurrentBasedIntakeFeeder(intake, feeder, lightStrip),
                new RaiseDeployer(deployer, lightStrip)
        );
    }
}
