package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Feeder;

public class IntakeStart extends SequentialCommandGroup {
    public IntakeStart(Feeder intake) {

    addCommands(
        System.out.println("Deploying"),
        new ParallelCommandGroup(new StartFeeder(intake), new StartFeeder(intake)),
        System.out.println("undeploying")
    );
    }
}