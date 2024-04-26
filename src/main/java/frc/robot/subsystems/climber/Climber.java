package frc.robot.subsystems.climber;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;

public class Climber extends SubsystemBase {
    private final ClimberIO climberIO;
    private final ClimberInputs inputs = new ClimberInputs();

    public Climber(ClimberIO io) {
        this.climberIO = io;
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
            if (inputs.rightClimberEnc < -78) {
                climberIO.setRightSpeed(0);
            } else {
                climberIO.setRightSpeed(-spd);
            }
            if (inputs.leftClimberEnc > 78) {
                climberIO.setLeftSpeed(0);
            } else {
                climberIO.setLeftSpeed(spd);
            }
        } else {
            climberIO.setRightSpeed(-spd);
            climberIO.setLeftSpeed(spd);
        }
        return true;
    }

    private boolean servosEngaged() {
        return inputs.leftServoSetpoint == Constants.LEFT_SERVO_ENGAGED ||
                inputs.rightServoSetpoint == Constants.RIGHT_SERVO_ENGAGED;
    }

    public void resetEncoders() {
        climberIO.resetEncoder();
    }

    public void engageRatchet() {
        climberIO.setRatchetPos(Constants.LEFT_SERVO_ENGAGED, Constants.RIGHT_SERVO_ENGAGED);
    }

    public void disengageRatchet() {
        climberIO.setRatchetPos(Constants.LEFT_SERVO_DISENGAGED, Constants.RIGHT_SERVO_DISENGAGED);
    }

    public boolean isLeftReverseLimitSwitchPressed() {
        return inputs.atLeftClimberLimit;
    }

    public boolean isRightReverseLimitSwitchPressed() {
        return inputs.atRightClimberLimit;
    }

    @Override
    public void periodic() {
        climberIO.updateInputs(inputs);
        org.littletonrobotics.junction.Logger.processInputs("climberInputs", inputs);
    }
}
