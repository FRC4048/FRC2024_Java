package frc.robot.commands.pathplanning;

import edu.wpi.first.wpilibj2.command.*;
import frc.robot.commands.MoveToGamepiece;
import frc.robot.commands.drivetrain.Drive;
import frc.robot.commands.feeder.FeederBackDrive;
import frc.robot.commands.feeder.StartFeeder;
import frc.robot.commands.intake.StartIntake;
import frc.robot.constants.Constants;
import frc.robot.constants.GameConstants;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.SwerveDrivetrain;
import frc.robot.subsystems.Vision;

public class DevourerPiece extends ParallelDeadlineGroup {
    public DevourerPiece(SwerveDrivetrain drivetrain, Vision vision, IntakeSubsystem intake, Feeder feeder) {
        super(new SequentialCommandGroup(
                new ParallelRaceGroup(
                        new StartIntake(intake, Constants.INTAKE_LIME_TIMEOUT),
                new StartFeeder(feeder)),
                new ParallelCommandGroup(
                        new SequentialCommandGroup(
                                new WaitCommand(GameConstants.FEEDER_WAIT_TIME_BEFORE_BACKDRIVE),
                                new FeederBackDrive(feeder)
                        )
                )
        ),new SequentialCommandGroup(
                new MoveToGamepiece(drivetrain, vision),
//                new MoveDistance(drivetrain,-0.15,0,1,false))
                new Drive(drivetrain, () -> -0.15, () -> 0, () -> 0, false)// I know this will work not sure about commented command above
            )
        );
    }
}
