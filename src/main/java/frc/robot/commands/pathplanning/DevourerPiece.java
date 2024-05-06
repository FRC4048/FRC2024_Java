package frc.robot.commands.pathplanning;

import frc.robot.commands.MoveToGamepiece;
import frc.robot.commands.SpoolIntake;
import frc.robot.commands.deployer.LowerDeployer;
import frc.robot.commands.drivetrain.Drive;
import frc.robot.commands.intake.CurrentBasedIntakeFeeder;
import frc.robot.constants.Constants;
import frc.robot.subsystems.deployer.Deployer;
import frc.robot.subsystems.feeder.Feeder;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.lightstrip.LightStrip;
import frc.robot.subsystems.limelight.Vision;
import frc.robot.subsystems.swervev3.SwerveDrivetrain;
import frc.robot.utils.DriveMode;
import frc.robot.utils.loggingv2.LoggableParallelCommandGroup;
import frc.robot.utils.loggingv2.LoggableRaceCommandGroup;
import frc.robot.utils.loggingv2.LoggableSequentialCommandGroup;
import frc.robot.utils.loggingv2.LoggableWaitCommand;

public class DevourerPiece extends LoggableSequentialCommandGroup {
    public DevourerPiece(SwerveDrivetrain drivetrain, Vision vision, Intake intake, Feeder feeder, Deployer deployer, LightStrip lightStrip) {
        super(
                new LoggableRaceCommandGroup(
                        new LoggableSequentialCommandGroup(
                                new LoggableParallelCommandGroup(
                                    new LowerDeployer(deployer, lightStrip),
                                    new SpoolIntake(intake, Constants.INTAKE_SPOOL_TIME)
                                ),
                                new LoggableRaceCommandGroup(
                                        new CurrentBasedIntakeFeeder(intake, feeder, lightStrip),
                                        new LoggableWaitCommand(7)
                                )
                        ),
                        new LoggableSequentialCommandGroup(
                                new MoveToGamepiece(drivetrain, vision),
                                new LoggableRaceCommandGroup(
                                        new Drive(drivetrain, () -> -0.4, () -> 0, () -> 0, ()->DriveMode.ROBOT_CENTRIC),
                                        new LoggableWaitCommand(0.7)
                                ), new LoggableWaitCommand(5)
                        )
                )
        );
    }
}
