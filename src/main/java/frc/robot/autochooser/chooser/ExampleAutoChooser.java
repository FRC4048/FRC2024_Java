package frc.robot.autochooser.chooser;

import com.ctre.phoenix6.mechanisms.swerve.SwerveDrivetrain;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.GoalEndState;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerPath;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.autochooser.AutoAction;
import frc.robot.autochooser.FieldLocation;
import frc.robot.autochooser.PlaceHolderCommand;
import frc.robot.autochooser.event.AutoEvent;

import java.util.List;
import java.util.Map;

public class ExampleAutoChooser extends Nt4AutoValidationChooser {

// Prevent the path from being flipped if the coordinates are already correct
    private final Map<AutoEvent, Command> commandMap = Map.of(
            new AutoEvent(AutoAction.DoNothing, FieldLocation.SpeakFront), new PlaceHolderCommand(),
            new AutoEvent(AutoAction.DoNothing, FieldLocation.SpeakerRight), new PlaceHolderCommand(),
            new AutoEvent(AutoAction.DoNothing, FieldLocation.SpeakerLeft), new PlaceHolderCommand(),
            new AutoEvent(AutoAction.DoNothing, FieldLocation.ZERO), new PlaceHolderCommand(),
            new AutoEvent(AutoAction.ShootAndCross, FieldLocation.SpeakerRight), AutoBuilder.followPath(null),
            new AutoEvent(AutoAction.ShootAndCross, FieldLocation.SpeakerLeft), AutoBuilder.followPath(PathPlannerPath.fromPathFile("ShootAndCrossLeft")),
            new AutoEvent(AutoAction.ShootAndCross, FieldLocation.SpeakFront), AutoBuilder.followPath(PathPlannerPath.fromPathFile("ShootAndCrossMid")),
            new AutoEvent(AutoAction.FigureEight, FieldLocation.ZERO), AutoBuilder.followPath(PathPlannerPath.fromPathFile("Figure8")),
            new AutoEvent(AutoAction.TwoPieceMoveLeft, FieldLocation.SpeakerRight), new PlaceHolderCommand()
            new AutoEvent(AutoAction.ShootAndCross, FieldLocation.SpeakFront), AutoBuilder.followPath(PathPlannerPath.fromPathFile("ShootAndCrossMid")),
            new AutoEvent(AutoAction.ShootFour, FieldLocation.SpeakerLeft), AutoBuilder.followPath(PathPlannerPath.fromPathFile("Shoot4Left")),
            new AutoEvent(AutoAction.ShootTwo, FieldLocation.SpeakerLeft), AutoBuilder.followPath(PathPlannerPath.fromPathFile("Shoot2Left"))
    );

    public ExampleAutoChooser() {
        super(AutoAction.DoNothing, FieldLocation.SpeakFront);
    }


    @Override
    public Command getAutoCommand() {
        return commandMap.get(new AutoEvent(getProvider().getSelectedAction(), getProvider().getSelectedLocation()));
    }

    @Override
    protected boolean isValid(AutoAction action, FieldLocation location) {
        return commandMap.containsKey(new AutoEvent(action, location));
    }
}
