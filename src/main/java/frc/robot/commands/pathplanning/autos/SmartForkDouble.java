package frc.robot.commands.pathplanning.autos;

import frc.robot.subsystems.SwerveDrivetrain;
import frc.robot.subsystems.deployer.Deployer;
import frc.robot.subsystems.feeder.Feeder;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.lightstrip.LightStrip;
import frc.robot.subsystems.ramp.Ramp;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.vision.Vision;
import frc.robot.utils.loggingv2.LoggableSequentialCommandGroup;

public class SmartForkDouble extends LoggableSequentialCommandGroup {
    public SmartForkDouble(SwerveDrivetrain drivetrain, Intake intake, Shooter shooter, Feeder feeder, Deployer deployer, Ramp ramp, LightStrip lightStrip, Vision vision) {
        super(
                new SmartForkSingle(drivetrain,intake,shooter,feeder,deployer,ramp,lightStrip,vision).withBasicName("SmartForkRun1"),
                new SmartForkSingle(drivetrain,intake,shooter,feeder,deployer,ramp,lightStrip,vision).withBasicName("SmartForkRun2")
        );
    }
}
