package frc.robot.commands.sequences;

import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.deployer.LowerDeployer;
import frc.robot.commands.deployer.RaiseDeployer;
import frc.robot.commands.feeder.FeederBackDrive;
import frc.robot.commands.feeder.StartFeeder;
import frc.robot.commands.intake.StartIntake;
import frc.robot.commands.ramp.RampMoveAndWait;
import frc.robot.constants.GameConstants;
import frc.robot.subsystems.Deployer;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.Ramp;
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

    public StartIntakeAndFeeder(Feeder feeder, IntakeSubsystem intake, Deployer deployer, Ramp ramp) {
        super("StartIntakeAndFeeder",
                CommandUtil.parallel("First",
                        new LowerDeployer(deployer),
                        new RampMoveAndWait(ramp, () -> GameConstants.RAMP_POS_STOW)
                ),
                CommandUtil.race("second",
                        new StartIntake(intake, 10), //intake stops by ParallelRaceGroup when note in feeder
                        new StartFeeder(feeder)
                ),
                CommandUtil.parallel("third",
                        new RaiseDeployer(deployer),
                        CommandUtil.sequence("Fourth",
                                new WaitCommand(GameConstants.FEEDER_WAIT_TIME_BEFORE_BACKDRIVE),
                                new FeederBackDrive(feeder)
                        )
                )
        );
    }
}
