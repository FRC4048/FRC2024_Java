package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.commands.cannon.Shoot;
import frc.robot.commands.cannon.StartFeeder;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.Shooter;

public class ShootAmpTestSequence extends ParallelCommandGroup {
    public ShootAmpTestSequence(Feeder feeder, Shooter shooter) {
        super(new StartFeeder(feeder), new Shoot(shooter));
        addRequirements(feeder,shooter);
    }
}
