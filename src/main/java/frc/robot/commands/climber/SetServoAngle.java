package frc.robot.commands.climber;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Climber;

public class SetServoAngle extends Command {
    private final Climber climber;
    private final double leftTargetAngle;
    private final double rightTargetAngle;

    public SetServoAngle(Climber climber, double leftTargetAngle, double rightTargetAngle){
        this.climber = climber;
        this.leftTargetAngle = leftTargetAngle;
        this.rightTargetAngle = rightTargetAngle;
    }

    @Override
    public void execute() {
        climber.setLeftServoAngle(leftTargetAngle);
        climber.setRightServoAngle(rightTargetAngle);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
