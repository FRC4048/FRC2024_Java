package frc.robot.commands.feeder;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Feeder;

public class FeederLimitSwitchEnable extends Command {
  private Feeder feeder;

  /** Creates a new FeederLimitSwitchEnable. */
  public FeederLimitSwitchEnable(Feeder feeder) {
    this.feeder = feeder;
    addRequirements(feeder);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    feeder.enableLimitSwitch();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
