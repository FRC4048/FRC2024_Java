// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.commands.Shoot;
import frc.robot.commands.ShootTest;
import frc.robot.subsystems.Shooter;
import frc.robot.commands.drive.WheelAlign;
import frc.robot.subsystems.swervev2.KinematicsConversionConfig;
import frc.robot.subsystems.swervev2.SwerveDrivetrain;
import frc.robot.subsystems.swervev2.SwerveIdConfig;
import frc.robot.subsystems.swervev2.SwervePidConfig;
import frc.robot.commands.RampMove;
import frc.robot.subsystems.Ramp;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;

public class RobotContainer {
    private final Joystick joyleft = new Joystick(Constants.LEFT_JOYSICK_ID);
    private final Joystick joyright = new Joystick(Constants.RIGHT_JOYSTICK_ID);
    private final SwerveDrivetrain drivetrain;
    private Ramp ramp;
    private final Shooter shooter = new Shooter();

    /** The container for the robot. Contains subsystems, OI devices, and commands. */
    public RobotContainer() {

        SwerveIdConfig frontLeftIdConf = new SwerveIdConfig(Constants.DRIVE_FRONT_LEFT_D, Constants.DRIVE_FRONT_LEFT_S, Constants.DRIVE_CANCODER_FRONT_LEFT);
        SwerveIdConfig frontRightIdConf = new SwerveIdConfig(Constants.DRIVE_FRONT_RIGHT_D, Constants.DRIVE_FRONT_RIGHT_S, Constants.DRIVE_CANCODER_FRONT_RIGHT);
        SwerveIdConfig backLeftIdConf = new SwerveIdConfig(Constants.DRIVE_BACK_LEFT_D, Constants.DRIVE_BACK_LEFT_S, Constants.DRIVE_CANCODER_BACK_LEFT);
        SwerveIdConfig backRightIdConf = new SwerveIdConfig(Constants.DRIVE_BACK_RIGHT_D, Constants.DRIVE_BACK_RIGHT_S, Constants.DRIVE_CANCODER_BACK_RIGHT);

        TrapezoidProfile.Constraints constraints = new TrapezoidProfile.Constraints(Constants.MAX_ANGULAR_SPEED * 4, 2 * Math.PI * 10);
        PID drivePid = PID.of(Constants.DRIVE_PID_P, Constants.DRIVE_PID_I, Constants.DRIVE_PID_D);
        PID steerPid = PID.of(Constants.STEER_PID_P, Constants.STEER_PID_I, Constants.STEER_PID_D);
        Gain driveGain = Gain.of(Constants.DRIVE_PID_FF_V, Constants.DRIVE_PID_FF_S);
        Gain steerGain = Gain.of(Constants.STEER_PID_FF_V, Constants.STEER_PID_FF_S);

        KinematicsConversionConfig kinematicsConversionConfig = new KinematicsConversionConfig(Constants.WHEEL_RADIUS, Constants.CHASSIS_DRIVE_GEAR_RATIO, Constants.CHASSIS_STEER_GEAR_RATIO);
        SwervePidConfig pidConfig = new SwervePidConfig(drivePid,steerPid,driveGain,steerGain,constraints);
        AHRS navxGyro = new AHRS();
        navxGyro.setAngleAdjustment(0);
        this.drivetrain = new SwerveDrivetrain(frontLeftIdConf, frontRightIdConf, backLeftIdConf, backRightIdConf, kinematicsConversionConfig, pidConfig, navxGyro);
        drivetrain.resetOdometry(new Pose2d(0,0,new Rotation2d(Math.toRadians(0))));
    
        // Configure the trigger bindings
        ramp = new Ramp();
        configureBindings();
        putShuffleboardCommands();

  }
  public void putShuffleboardCommands() {
    SmartShuffleboard.putCommand("Ramp", "SetArmPID400", new RampMove(ramp, 400));
    SmartShuffleboard.putCommand("Ramp", "SetArmPID500", new RampMove(ramp, 500));

  }


    private void configureBindings() {

    drivetrain.setDefaultCommand(new Drive(drivetrain, joyleft::getY, joyleft::getX, joyright::getX));

    SmartShuffleboard.putCommand("TEST","WheelAlign",new WheelAlign(drivetrain));
    SmartShuffleboard.putCommand("Shooter", "Shoot", new Shoot(shooter));
    SmartShuffleboard.putCommand("Shooter", "ShootTest", new ShootTest(shooter));

  }

    public SwerveDrivetrain getDrivetrain() {
        return drivetrain;
    }
}