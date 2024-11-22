package frc.robot.swervev2.components;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import frc.robot.swervev2.encoder.SwerveAbsEncoder;
import frc.robot.swervev2.encoder.SwerveEncoder;
import frc.robot.utils.math.AngleUtils;

/**
 * Generic Class for Swerve Modules with Encoders
 */
public class GenericEncodedSwerve implements SwerveMotor, SwerveMotorEncoder {
    private final MotorController driveMotor;
    private final MotorController steerMotor;

    private final SwerveEncoder driveEncoder;
    private final SwerveEncoder steerEncoder;
    private final SwerveAbsEncoder absEncoder;

    public GenericEncodedSwerve(MotorController driveMotor, MotorController steerMotor, SwerveAbsEncoder absEncoder, SwerveEncoder driveEncoder, SwerveEncoder steerEncoder) {
        this.driveMotor = driveMotor;
        this.steerMotor = steerMotor;
        this.absEncoder = absEncoder;
        this.driveEncoder = driveEncoder;
        this.steerEncoder = steerEncoder;
    }

    /**
     * resets the relative encoders and sets the encoder conversion factors
     * @param driveVelFactor (2 * PI * wheelRadius) / (gearRatio * 60);
     * @param drivePosFactor (2 * PI * wheelRadius) / gearRatio;
     * @param steerPosFactor (2 * PI) / gearRatio
     */
    public void configureEncoders(){
        resetRelEnc();
    }

    @Override
    public MotorController getSteerMotor() {
        return steerMotor;
    }

    @Override
    public MotorController getDriveMotor() {
        return driveMotor;
    }

    /**
     * @return the steer encoder position in radians between 0 and (2 * PI)
     */
    @Override
    public double getSteerEncPosition() {
        return AngleUtils.normalizeAngle2(steerEncoder.getPosition() - getSteerOffset());
    }

    @Override
    public double getDriveEncPosition() {
        return driveEncoder.getPosition();
    }

    @Override
    public void resetRelEnc() {
        steerEncoder.setPosition(0);
        driveEncoder.setPosition(0);
    }

    @Override
    public double getDriveEncVel() {
        return driveEncoder.getVelocity();
    }

    @Override
    public double getSteerOffset() {
        return absEncoder.getSteerOffset();
    }

    /**
     * @param zeroAbs the absolute position of the steer encoder when the robot is zeroed
     */
    @Override
    public void setSteerOffset(double zeroAbs) {
        steerEncoder.setPosition(0);
        absEncoder.zero(zeroAbs);
    }
    public SwerveModulePosition getPosition(){
        return new SwerveModulePosition(getDriveEncPosition(),new Rotation2d(getSteerEncPosition()));
    }



    public SwerveAbsEncoder getAbsEnc() {
        return absEncoder;
    }

    public double getSteerEncoderRawPos() {
        return steerEncoder.getPosition();
    }
}