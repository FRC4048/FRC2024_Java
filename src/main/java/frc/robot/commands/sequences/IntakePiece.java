package frc.robot.commands.sequences;

import frc.robot.commands.deployer.RaiseDeployer;
import frc.robot.commands.intake.CurrentBasedIntakeFeeder;
import frc.robot.subsystems.deployer.Deployer;
import frc.robot.subsystems.feeder.Feeder;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.lightstrip.LightStrip;
import frc.robot.subsystems.ramp.Ramp;
import frc.robot.utils.loggingv2.LoggableSequentialCommandGroup;

public class IntakePiece extends LoggableSequentialCommandGroup {
    public IntakePiece(Intake intake, Deployer deployer, Ramp ramp, Feeder feeder, LightStrip lightStrip) {
        super(
                new PrepIntake(intake, deployer, ramp, lightStrip),
                new CurrentBasedIntakeFeeder(intake, feeder, lightStrip),
                new RaiseDeployer(deployer, lightStrip)
        );
    }
}
