package frc.robot.commands.pathplanning;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.SpoolIntake;
import frc.robot.commands.feeder.TimedFeeder;
import frc.robot.commands.intake.CurrentBasedIntakeFeeder;
import frc.robot.commands.ramp.ResetRamp;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.Ramp;
import frc.robot.subsystems.Shooter;

public class PathPlannerShoot extends ParallelCommandGroup {
    public PathPlannerShoot(Shooter shooter, Feeder feeder, Ramp ramp, IntakeSubsystem intake) {
        addCommands(
                new BasicShoot(shooter, 1),
                new SequentialCommandGroup(
                        new WaitCommand(0.05),
                        new TimedFeeder(feeder,1),
                        new SpoolIntake(intake, Constants.INTAKE_SPOOL_TIME),
                        new ParallelCommandGroup (
                                new CurrentBasedIntakeFeeder(intake, feeder),
                                new ResetRamp(ramp)
                        ), new MoveRamp(ramp,1.5)

                )
        );
    }
}
