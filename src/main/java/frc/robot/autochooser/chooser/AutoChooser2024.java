package frc.robot.autochooser.chooser;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathPlannerPath;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.autochooser.AutoAction;
import frc.robot.autochooser.FieldLocation;
import frc.robot.autochooser.PlaceHolderCommand;
import frc.robot.autochooser.ShootCrossRight;
import frc.robot.autochooser.event.AutoEvent;
import frc.robot.commands.pathplanning.FourPieceLeft;
import frc.robot.commands.pathplanning.ShootAndDrop;
import frc.robot.subsystems.*;

import java.util.Map;

public class AutoChooser2024 extends Nt4AutoValidationChooser {
    private final Map<AutoEvent, Command> commandMap;

    public AutoChooser2024(IntakeSubsystem intakeSubsystem, SwerveDrivetrain drivetrain, Shooter shooter, Feeder feeder, Deployer deployer, Ramp ramp) {
        super(AutoAction.DoNothing, FieldLocation.SpeakFront);
        commandMap = Map.ofEntries(
                Map.entry(new AutoEvent(AutoAction.DoNothing, FieldLocation.SpeakFront), new PlaceHolderCommand()),
                Map.entry(new AutoEvent(AutoAction.DoNothing, FieldLocation.SpeakerRight), new PlaceHolderCommand()),
                Map.entry(new AutoEvent(AutoAction.DoNothing, FieldLocation.SpeakerLeft), new PlaceHolderCommand()),
                Map.entry(new AutoEvent(AutoAction.DoNothing, FieldLocation.ZERO), new PlaceHolderCommand()),
                Map.entry(new AutoEvent(AutoAction.ShootTwoDip, FieldLocation.SpeakerLeft), AutoBuilder.followPath(PathPlannerPath.fromPathFile("Shoot2AndDip")).beforeStarting(new ShootAndDrop(shooter,feeder,deployer))),
                Map.entry(new AutoEvent(AutoAction.ShootAndCross, FieldLocation.SpeakerRight), new ShootCrossRight(drivetrain, shooter, ramp, intakeSubsystem, feeder)),
                Map.entry(new AutoEvent(AutoAction.ShootAndCross, FieldLocation.SpeakerLeft), new ShootCrossRight(drivetrain, shooter, ramp, intakeSubsystem, feeder)),
                Map.entry(new AutoEvent(AutoAction.ShootAndCross, FieldLocation.SpeakFront), new ShootCrossRight(drivetrain, shooter, ramp, intakeSubsystem, feeder)),
                Map.entry(new AutoEvent(AutoAction.ShootFour, FieldLocation.SpeakerLeft), new FourPieceLeft(shooter,feeder,deployer)),
                Map.entry(new AutoEvent(AutoAction.ShootFour, FieldLocation.SpeakerRight), AutoBuilder.followPath(PathPlannerPath.fromPathFile("Shoot4Right"))),
                Map.entry(new AutoEvent(AutoAction.ShootTwo, FieldLocation.SpeakerLeft), AutoBuilder.followPath(PathPlannerPath.fromPathFile("Shoot2Left")))
        );
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
