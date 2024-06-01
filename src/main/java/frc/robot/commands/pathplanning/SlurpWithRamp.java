package frc.robot.commands.pathplanning;

import frc.robot.commands.intake.CurrentBasedIntakeFeeder;
import frc.robot.commands.ramp.ResetRamp;
import frc.robot.subsystems.feeder.Feeder;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.lightstrip.LightStrip;
import frc.robot.subsystems.ramp.Ramp;
import frc.robot.utils.loggingv2.LoggableRaceCommandGroup;
import frc.robot.utils.loggingv2.LoggableSequentialCommandGroup;
import frc.robot.utils.loggingv2.LoggableWaitCommand;

public class SlurpWithRamp extends LoggableRaceCommandGroup {
    public SlurpWithRamp(Intake intake, Feeder feeder, LightStrip lightStrip, Ramp ramp) {
        super(
                new CurrentBasedIntakeFeeder(intake, feeder, lightStrip),
                new LoggableSequentialCommandGroup(
                        new ResetRamp(ramp, lightStrip),
                        new LoggableWaitCommand(2)
                ).withBasicName("ResetRampPostIntake")
        );
    }
}
