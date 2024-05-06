package frc.robot.commands.sequences;

import frc.robot.commands.SpoolIntake;
import frc.robot.commands.deployer.LowerDeployer;
import frc.robot.commands.ramp.RampMoveAndWait;
import frc.robot.constants.Constants;
import frc.robot.constants.GameConstants;
import frc.robot.subsystems.deployer.Deployer;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.lightstrip.LightStrip;
import frc.robot.subsystems.ramp.Ramp;
import frc.robot.utils.loggingv2.LoggableParallelCommandGroup;

public class PrepIntake extends LoggableParallelCommandGroup {
    public PrepIntake(Intake intake, Deployer deployer, Ramp ramp, LightStrip lightStrip){
        super(
                new SpoolIntake(intake, Constants.INTAKE_SPOOL_TIME),
                new LowerDeployer(deployer, lightStrip),
                new RampMoveAndWait(ramp, lightStrip ,() -> GameConstants.RAMP_POS_STOW)
        );
    }
}
