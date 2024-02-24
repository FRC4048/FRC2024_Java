package frc.robot.commands.amp;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Amp;
import java.util.function.DoubleSupplier;

public class AmpJoystick extends Command {
    private Amp amp;
    private double JoystickPose;
    private DoubleSupplier supplier;

    public AmpJoystick(Amp amp, DoubleSupplier supplier) {
        this.amp = amp;
        this.supplier= supplier;
        addRequirements(amp);
    }


    @Override
    public void execute() {
        double value = supplier.getAsDouble();
        if (Math.abs(value)>0.1) {
            amp.setAmpMotorSpeed(value);
        } else {
            amp.setAmpMotorSpeed(0);
        }
    }

    @Override
    public boolean isFinished() {
        return (amp.isForwardLimitSwitchPressed() || amp.isReverseLimitSwitchPressed());
    }

    @Override
    public void end(boolean interrupted) {
        amp.setAmpMotorSpeed(0);
    }
}
