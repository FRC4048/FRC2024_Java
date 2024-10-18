package frc.robot.commands.pathplanning;

import frc.robot.subsystems.lightstrip.LightStrip;
import frc.robot.subsystems.ramp.Ramp;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.utils.loggingv2.LoggableParallelCommandGroup;

public class RampShootCombo extends LoggableParallelCommandGroup {
    public RampShootCombo(Ramp ramp, Shooter shooter, LightStrip lightStrip, double target) {
        super(new MoveRamp(ramp,target), new BasicShoot(shooter, lightStrip,5));
    }
}
