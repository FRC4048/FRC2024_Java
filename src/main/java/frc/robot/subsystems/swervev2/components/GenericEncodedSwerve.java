package frc.robot.subsystems.swervev2.components;

import com.ctre.phoenix.sensors.WPI_CANCoder;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;

/**
 * Generic Class for Swerve Modules with Encoders
 */
public class GenericEncodedSwerve implements SwerveMotor, SwerveMotorEncoder {
    private final MotorController driveMotor;
    private final MotorController steerMotor;

    private final RelativeEncoder driveEncoder;
    private final RelativeEncoder steerEncoder;
    private double steerOffset = 0;
    private final WPI_CANCoder absEncoder;

    public GenericEncodedSwerve(MotorController driveMotor, MotorController steerMotor, WPI_CANCoder absEncoder, RelativeEncoder driveEncoder, RelativeEncoder steerEncoder,
                                double driveVelFactor, double drivePosFactor, double steerPosFactor) {
        this.driveMotor = driveMotor;
        this.steerMotor = steerMotor;
        this.absEncoder = absEncoder;
        this.driveEncoder = driveEncoder;
        this.steerEncoder = steerEncoder;
        configureEncoders(driveVelFactor,drivePosFactor, steerPosFactor);
    }

    /**
     * resets the relative encoders and sets the encoder conversion factors
     * @param driveVelFactor (2 * PI * wheelRadius) / (gearRatio * 60);
     * @param drivePosFactor (2 * PI * wheelRadius) / gearRatio;
     * @param steerPosFactor (2 * PI) / gearRatio
     */
    public void configureEncoders(double driveVelFactor, double drivePosFactor, double steerPosFactor){
        resetRelEnc();
        driveEncoder.setVelocityConversionFactor(driveVelFactor);
        driveEncoder.setPositionConversionFactor(drivePosFactor);
        steerEncoder.setPositionConversionFactor(steerPosFactor);
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
        return normalizeAngle(steerEncoder.getPosition() - getSteerOffset());
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
        return steerOffset;
    }

    /**
     * @param zeroAbs the absolute position of the steer encoder when the robot is zeroed
     */
    @Override
    public void setSteerOffset(double zeroAbs) {
        steerEncoder.setPosition(0);
        steerOffset = Math.toRadians(zeroAbs - absEncoder.getAbsolutePosition());
        steerOffset = normalizeAngle(steerOffset);
    }
    public SwerveModulePosition getPosition(){
        return new SwerveModulePosition(getDriveEncPosition(),new Rotation2d(getSteerEncPosition()));
    }

    /**
     * @param angleInRad angle in radians
     * @return the angle between 0 and (2 * PI)
     */
    private double normalizeAngle(double angleInRad){
        angleInRad %= 2 * Math.PI;
        if (angleInRad < 0) {
            angleInRad += 2 * Math.PI;
        }
        return angleInRad;
    }

}
