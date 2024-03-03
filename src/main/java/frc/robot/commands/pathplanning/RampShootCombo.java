package frc.robot.commands.pathplanning;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.commands.ramp.RampMove;
import frc.robot.subsystems.Ramp;
import frc.robot.subsystems.Shooter;

import java.util.function.DoubleSupplier;

public class RampShootCombo extends ParallelCommandGroup {
    public RampShootCombo(Ramp ramp, Shooter shooter, DoubleSupplier target) {
        super(new RampMove(ramp,target), new BasicShoot(shooter,5));
    }
}
