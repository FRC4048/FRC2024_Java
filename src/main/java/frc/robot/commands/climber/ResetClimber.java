package frc.robot.commands.climber;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Climber;
import frc.robot.utils.TimeoutCounter;

public class ResetClimber extends Command {
  /** Creates a new ResetRamp. */
  private final Climber climber;
  private double startTime;
  private boolean leftRetracted;
  private boolean rightRetracted;
  private final TimeoutCounter timeoutCounter = new TimeoutCounter("Reset Climber");

  /*
   *When we get the robot:
   *TODO: Check if the forward limit switch is the top limit switch otherwise, swap getForwardSwitchState() with getReversedSwitchState()
   *TODO: Check if the motor pulls the cannon up otherwise multiply the value by negative one
   *TODO: Check if the motor is at a reasonable speed
   */

  public ResetClimber(Climber climber) {
    this.climber = climber;
    addRequirements(climber);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    startTime = Timer.getFPGATimestamp();
    climber.disengageRatchet();
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
    climber.engageRatchet();
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

  @Override
  public boolean runsWhenDisabled() {
    return true;
  }
}