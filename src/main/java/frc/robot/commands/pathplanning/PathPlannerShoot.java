package frc.robot.commands.pathplanning;

import frc.robot.SpoolIntake;
import frc.robot.commands.feeder.TimedFeeder;
import frc.robot.commands.intake.CurrentBasedIntakeFeeder;
import frc.robot.commands.ramp.ResetRamp;
import frc.robot.constants.Constants;
import frc.robot.subsystems.feeder.Feeder;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.lightstrip.LightStrip;
import frc.robot.subsystems.ramp.Ramp;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.utils.loggingv2.LoggableParallelCommandGroup;
import frc.robot.utils.loggingv2.LoggableSequentialCommandGroup;
import frc.robot.utils.loggingv2.LoggableWaitCommand;

public class PathPlannerShoot extends LoggableParallelCommandGroup {
    public PathPlannerShoot(Shooter shooter, Feeder feeder, Ramp ramp, Intake intake, LightStrip lightStrip) {
        super(
                new BasicShoot(shooter, lightStrip,1),
                new LoggableSequentialCommandGroup(
                        new LoggableWaitCommand(0.05),
                        new TimedFeeder(feeder, lightStrip, Constants.TIMED_FEEDER_EXIT),
                        new SpoolIntake(intake, Constants.INTAKE_SPOOL_TIME),
                        new LoggableParallelCommandGroup(
                                new CurrentBasedIntakeFeeder(intake, feeder, lightStrip),
                                new ResetRamp(ramp, lightStrip)
                        ), new MoveRamp(ramp, 1.5)
                )
        );
    }
}
