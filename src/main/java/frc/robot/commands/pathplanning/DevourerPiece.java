package frc.robot.commands.pathplanning;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.SpoolIntake;
import frc.robot.commands.MoveToGamepiece;
import frc.robot.commands.deployer.LowerDeployer;
import frc.robot.commands.drivetrain.Drive;
import frc.robot.commands.intake.CurrentBasedIntakeFeeder;
import frc.robot.constants.Constants;
import frc.robot.subsystems.SwerveDrivetrain;
import frc.robot.subsystems.deployer.Deployer;
import frc.robot.subsystems.feeder.Feeder;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.lightstrip.LightStrip;
import frc.robot.subsystems.vision.Vision;
import frc.robot.utils.DriveMode;

public class DevourerPiece extends SequentialCommandGroup {
    public DevourerPiece(SwerveDrivetrain drivetrain, Vision vision, Intake intake, Feeder feeder, Deployer deployer, LightStrip lightStrip) {
        super(
                new ParallelRaceGroup(
                        new SequentialCommandGroup(
                                new ParallelCommandGroup(
                                    new LowerDeployer(deployer, lightStrip),
                                    new SpoolIntake(intake, Constants.INTAKE_SPOOL_TIME)
                                ),
                                new ParallelRaceGroup(
                                        new CurrentBasedIntakeFeeder(intake, feeder, lightStrip),
                                        new WaitCommand(7)
                                )
                        ),
                        new SequentialCommandGroup(
                                new MoveToGamepiece(drivetrain, vision),
                                new ParallelRaceGroup(
                                        new Drive(drivetrain, () -> -0.4, () -> 0, () -> 0, ()->DriveMode.ROBOT_CENTRIC),
                                        new WaitCommand(0.7)
                                ), new WaitCommand(5)
                        )
                )
        );
    }
}
