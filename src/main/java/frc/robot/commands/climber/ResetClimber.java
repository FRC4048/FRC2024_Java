package frc.robot.commands.climber;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.LightStrip;
import frc.robot.utils.TimeoutCounter;

public class ResetClimber extends Command {
  /** Creates a new ResetRamp. */
  private final Climber climber;
  private double startTime;
  private final TimeoutCounter timeoutCounter;

  public ResetClimber(Climber climber, LightStrip lightStrip) {
    this.climber = climber;
    timeoutCounter = new TimeoutCounter("Reset Climber", lightStrip);
    addRequirements(climber);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    startTime = Timer.getFPGATimestamp();
    climber.engageRatchet();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    climber.setSpeed(-Constants.CLIMBER_RAISING_SPEED); //assuming positive is forward with a random speed
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    climber.setSpeed(0);
    climber.resetEncoders();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (climber.isLeftReverseLimitSwitchPressed() && climber.isRightReverseLimitSwitchPressed()) {
      return true;
    }
    else if ((Timer.getFPGATimestamp() - startTime) >= Constants.RESET_CLIMBER_TIMEOUT) {
      timeoutCounter.increaseTimeoutCount();
      return true;
    }
    return false;
  }
}