package frc.robot.subsystems.shooter;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;
import frc.robot.subsystems.LoggableSystem;
import org.littletonrobotics.junction.AutoLogOutput;

public class Shooter extends SubsystemBase {

    private final LoggableSystem<ShooterIO, ShooterInputs> system;

    public Shooter(ShooterIO shooterIO) {
        this.system = new LoggableSystem<>(shooterIO, new ShooterInputs());
    }
    /**
     * Set shooter motor 1 RPM with PID
     *
     * @param rpm of motor
     */
    public void setShooterMotorLeftRPM(double rpm) {
        system.getIO().setShooterLeftRPM(rpm);
    }

    /**
     * Set shooter motor 2 RPM with PID
     *
     * @param rpm of motor
     */
    public void setShooterMotorRightRPM(double rpm) {
        system.getIO().setShooterRightRPM(rpm);
    }

    /**
     * sets speed of motor1 and motor2 to 0
     */
    public void stopShooter() {
        system.getIO().stop();
    }

    public void slowStop() {
        system.getIO().slowStop();
    }

    @AutoLogOutput()
    public boolean upToSpeed(double leftSpeed, double rightSpeed) {
        return ((system.getInputs().shooterMotorLeftRPM / leftSpeed) * 100 > Constants.SHOOTER_UP_TO_SPEED_THRESHOLD) &&
                ((system.getInputs().shooterMotorRightRPM / rightSpeed) * 100 > Constants.SHOOTER_UP_TO_SPEED_THRESHOLD);
    }

    @Override
    public void periodic() {
        system.updateInputs();
    }
}
