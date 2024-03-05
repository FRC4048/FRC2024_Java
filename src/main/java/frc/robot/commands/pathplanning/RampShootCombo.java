package frc.robot.commands.pathplanning;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.subsystems.Ramp;
import frc.robot.subsystems.Shooter;

public class RampShootCombo extends ParallelCommandGroup {
    public RampShootCombo(Ramp ramp, Shooter shooter, double target) {
        super(new MoveRamp(ramp,target), new BasicShoot(shooter,5));
    }
}
