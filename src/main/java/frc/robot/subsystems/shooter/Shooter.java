package frc.robot.subsystems.shooter;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;
import org.littletonrobotics.junction.AutoLogOutput;

public class Shooter extends SubsystemBase {
    private final ShooterIO shooterIO;
    private final ShooterInput input = new ShooterInput();

    public Shooter() {
        shooterIO = new RealShooter();
    }


    /**
     * Set shooter motor 1 RPM with PID
     *
     * @param rpm of motor
     */
    public void setShooterMotorLeftRPM(double rpm) {
        shooterIO.setShooterLeftRPM(rpm);
    }

    /**
     * Set shooter motor 2 RPM with PID
     *
     * @param rpm of motor
     */
    public void setShooterMotorRightRPM(double rpm) {
        shooterIO.setShooterRightRPM(rpm);
    }

    /**
     * sets speed of motor1 and motor2 to 0
     */
    public void stopShooter() {
        shooterIO.stop();
    }

    public void slowStop() {
        shooterIO.slowStop();
    }

    @Override
    public void periodic() {
        shooterIO.updateInputs(input);
        org.littletonrobotics.junction.Logger.processInputs("ShooterInputs", input);
    }

    @AutoLogOutput(key = "/robot/shooter")
    public boolean upToSpeed(double leftSpeed, double rightSpeed) {
        return ((input.shooterMotorLeftRPM / leftSpeed) * 100 > Constants.SHOOTER_UP_TO_SPEED_THRESHOLD) &&
                ((input.shooterMotorRightRPM / rightSpeed) * 100 > Constants.SHOOTER_UP_TO_SPEED_THRESHOLD);
    }
}
