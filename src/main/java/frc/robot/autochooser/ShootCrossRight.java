package frc.robot.autochooser;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.RobotContainer;
import frc.robot.commands.drivetrain.MoveDistance;
import frc.robot.commands.ramp.RampMove;
import frc.robot.commands.ramp.ResetRamp;
import frc.robot.commands.sequences.SpoolExitAndShoot;
import frc.robot.commands.shooter.ShootSpeaker;
import frc.robot.constants.GameConstants;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.Ramp;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.SwerveDrivetrain;

public class ShootCrossRight extends SequentialCommandGroup{
    public ShootCrossRight(SwerveDrivetrain drivetrain, Shooter shooter, Ramp ramp, IntakeSubsystem intake, Feeder feeder) {
        double direction = 1.0; 
        if (RobotContainer.isRedAlliance() == true) {
            direction = -1.0;
        }
        addCommands(
            new ResetRamp(ramp),
            new RampMove(ramp, ()->GameConstants.RAMP_POS_SHOOT_SPEAKER_CLOSE), //change later
            new WaitCommand(0.5), //change later
            new SpoolExitAndShoot(shooter, feeder, drivetrain),
            new WaitCommand(0.5),
            new MoveDistance(drivetrain, 0.0,  -2.74, 0.5, true),
            new WaitCommand(0.5),
            new MoveDistance(drivetrain, direction * 2.2, 0.0, 0.5, true)
        );
    }
}
