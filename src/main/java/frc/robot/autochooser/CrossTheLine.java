package frc.robot.autochooser;



import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.commands.drivetrain.MoveDistance;
import frc.robot.commands.ramp.ResetRamp;
import frc.robot.subsystems.Ramp;
import frc.robot.subsystems.SwerveDrivetrain;

public class CrossTheLine extends ParallelCommandGroup {
    double direction;
    public CrossTheLine(SwerveDrivetrain drivetrain, Ramp ramp) {
        if (RobotContainer.isRedAlliance() == true) {
            direction = -1.715;
        }
        else if (RobotContainer.isRedAlliance() == false) {
            direction = 1.715;
        }
        addCommands(
            new MoveDistance(drivetrain, direction, 0.0, 0.3), 
            new ResetRamp(ramp));
  }
    }
