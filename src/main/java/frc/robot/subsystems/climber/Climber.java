package frc.robot.subsystems.climber;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;
import frc.robot.subsystems.LoggableSystem;

public class Climber extends SubsystemBase {
    private final LoggableSystem<ClimberIO, ClimberInputs> system;

    public Climber(ClimberIO io) {
        this.system = new LoggableSystem<>(io, new ClimberInputs());
    }

    /**
     * Right motor - Positive is down, Negative is up
     * Left motor - Positive is up, Negative is down
     * @return true if setting speed was successful
     */
    public boolean setSpeed(double spd) {
        if (spd > 0 && (servosEngaged())){
            return false;
        }
        if (spd > 0) {
            if (system.getInputs().rightClimberEnc < -78) {
                system.getIO().setRightSpeed(0);
            } else {
                system.getIO().setRightSpeed(-spd);
            }
            if (system.getInputs().leftClimberEnc > 78) {
                system.getIO().setLeftSpeed(0);
            } else {
                system.getIO().setLeftSpeed(spd);
            }
        } else {
            system.getIO().setRightSpeed(-spd);
            system.getIO().setLeftSpeed(spd);
        }
        return true;
    }

    private boolean servosEngaged() {
        return system.getInputs().leftServoSetpoint == Constants.LEFT_SERVO_ENGAGED ||
                system.getInputs().rightServoSetpoint == Constants.RIGHT_SERVO_ENGAGED;
    }

    public void resetEncoders() {
        system.getIO().resetEncoder();
    }

    public void engageRatchet() {
        system.getIO().setRatchetPos(Constants.LEFT_SERVO_ENGAGED, Constants.RIGHT_SERVO_ENGAGED);
    }

    public void disengageRatchet() {
        system.getIO().setRatchetPos(Constants.LEFT_SERVO_DISENGAGED, Constants.RIGHT_SERVO_DISENGAGED);
    }

    public boolean isLeftReverseLimitSwitchPressed() {
        return system.getInputs().atLeftClimberLimit;
    }

    public boolean isRightReverseLimitSwitchPressed() {
        return system.getInputs().atRightClimberLimit;
    }

    @Override
    public void periodic() {
        system.updateInputs();
    }
}
