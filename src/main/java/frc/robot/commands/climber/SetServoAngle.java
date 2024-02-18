package frc.robot.commands.climber;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Climber;

public class SetServoAngle extends Command {
    private final Climber climber;
    private final double targetAngle;

    public SetServoAngle(Climber climber, double targetAngle){
        this.climber = climber;
        this.targetAngle = targetAngle;
        addRequirements(climber);
    }

    @Override
    public void execute() {
        climber.setServoAngle(targetAngle);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
