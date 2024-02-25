package frc.robot.commands.climber;

import frc.robot.subsystems.Climber;
import frc.robot.utils.command.SubsystemCommandBase;

public class SetServoAngle extends SubsystemCommandBase<Climber> {
    private final double leftTargetAngle;
    private final double rightTargetAngle;

    public SetServoAngle(Climber climber, double leftTargetAngle, double rightTargetAngle){
        super(climber);
        this.leftTargetAngle = leftTargetAngle;
        this.rightTargetAngle = rightTargetAngle;
    }

    @Override
    public void execute() {
        getSystem().setLeftServoAngle(leftTargetAngle);
        getSystem().setRightServoAngle(rightTargetAngle);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
