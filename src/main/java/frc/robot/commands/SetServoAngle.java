package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ServoSubsystem;

public class SetServoAngle extends Command {
    private final ServoSubsystem servo;
    private final double targetAngle;

    public SetServoAngle(ServoSubsystem servo, double targetAngle){
        this.servo = servo;
        this.targetAngle = targetAngle;
        addRequirements(servo);
    }

    @Override
    public void execute() {
        servo.setServoAngle(targetAngle);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
