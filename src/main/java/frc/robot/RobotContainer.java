// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.kauailabs.navx.frc.AHRS;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.pathplanner.lib.util.PIDConstants;
import com.pathplanner.lib.util.ReplanningConfig;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.autochooser.chooser.AutoChooser;
import frc.robot.commands.Shoot;
import frc.robot.commands.ShootTest;
import frc.robot.subsystems.Shooter;
import frc.robot.commands.RampMove;
import frc.robot.commands.ReportErrorCommand;
import frc.robot.autochooser.chooser.AutoChooser2024;
import frc.robot.commands.SetInitOdom;
import frc.robot.subsystems.Ramp;
import frc.robot.subsystems.swervev2.KinematicsConversionConfig;
import frc.robot.subsystems.swervev2.SwerveDrivetrain;
import frc.robot.subsystems.swervev2.SwerveIdConfig;
import frc.robot.subsystems.swervev2.SwervePidConfig;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;

import java.util.Optional;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
      private final Joystick joyleft = new Joystick(Constants.LEFT_JOYSICK_ID);
      private final Joystick joyright = new Joystick(Constants.RIGHT_JOYSTICK_ID);
      private SwerveDrivetrain drivetrain;
      private final Ramp ramp;
      private final AutoChooser2024 autoChooser;
      private final Shooter shooter = new Shooter();

    /** The container for the robot. Contains subsystems, OI devices, and commands. */
    public RobotContainer() {
        setupDriveTrain();
        registerPathPlanableCommands();
        setupPathPlaning();
        autoChooser = new AutoChooser2024();
        autoChooser.addOnValidationCommand(()->new SetInitOdom(drivetrain,autoChooser));
        autoChooser.forceRefresh();
        ramp = new Ramp();
        configureBindings();
        putShuffleboardCommands();
    }

    /**
     * NamedCommands
     */
    private void registerPathPlanableCommands() {
        NamedCommands.registerCommand(ReportErrorCommand.class.getName(), new ReportErrorCommand()); //place holder
    }

    private void setupPathPlaning() {
        AutoBuilder.configureHolonomic(drivetrain::getPose,
                drivetrain::resetOdometry,
                drivetrain::speedsFromStates,
                drivetrain::drive,
                new HolonomicPathFollowerConfig(
                        new PIDConstants(5, 0.0, 0), // Translation PID constants
                        new PIDConstants(5, 0.0, 0), // Rotation PID constants
                        3, // Max module speed, in m/s
                        0.5, // Drive base radius in meters. Distance from robot center to the furthest module.
                        new ReplanningConfig()
                ), RobotContainer::shouldFlip, drivetrain);
    }

    private void setupDriveTrain() {
    
        SwerveIdConfig frontLeftIdConf = new SwerveIdConfig(Constants.DRIVE_FRONT_LEFT_D, Constants.DRIVE_FRONT_LEFT_S, Constants.DRIVE_CANCODER_FRONT_LEFT);
            SwerveIdConfig frontRightIdConf = new SwerveIdConfig(Constants.DRIVE_FRONT_RIGHT_D, Constants.DRIVE_FRONT_RIGHT_S, Constants.DRIVE_CANCODER_FRONT_RIGHT);
            SwerveIdConfig backLeftIdConf = new SwerveIdConfig(Constants.DRIVE_BACK_LEFT_D, Constants.DRIVE_BACK_LEFT_S, Constants.DRIVE_CANCODER_BACK_LEFT);
            SwerveIdConfig backRightIdConf = new SwerveIdConfig(Constants.DRIVE_BACK_RIGHT_D, Constants.DRIVE_BACK_RIGHT_S, Constants.DRIVE_CANCODER_BACK_RIGHT);

        TrapezoidProfile.Constraints constraints = new TrapezoidProfile.Constraints(Constants.MAX_ANGULAR_SPEED * 150, 2 * Math.PI * 150);
        PID drivePid = PID.of(Constants.DRIVE_PID_P, Constants.DRIVE_PID_I, Constants.DRIVE_PID_D);
        PID steerPid = PID.of(Constants.STEER_PID_P, Constants.STEER_PID_I, Constants.STEER_PID_D);
        Gain driveGain = Gain.of(Constants.DRIVE_PID_FF_V, Constants.DRIVE_PID_FF_S);
        Gain steerGain = Gain.of(Constants.STEER_PID_FF_V, Constants.STEER_PID_FF_S);

        KinematicsConversionConfig kinematicsConversionConfig = new KinematicsConversionConfig(Constants.WHEEL_RADIUS, Constants.CHASSIS_DRIVE_GEAR_RATIO, Constants.CHASSIS_STEER_GEAR_RATIO);
        SwervePidConfig pidConfig = new SwervePidConfig(drivePid, steerPid, driveGain, steerGain, constraints);
        AHRS navxGyro = new AHRS();
        this.drivetrain = new SwerveDrivetrain(frontLeftIdConf, frontRightIdConf, backLeftIdConf, backRightIdConf, kinematicsConversionConfig, pidConfig, navxGyro);
    }
    public void putShuffleboardCommands() {
        SmartShuffleboard.putCommand("Ramp", "SetArmPID400", new RampMove(ramp, 400));
        SmartShuffleboard.putCommand("Ramp", "SetArmPID500", new RampMove(ramp, 500));
        SmartShuffleboard.putCommand("Shooter", "Shoot", new Shoot(shooter));
        SmartShuffleboard.putCommand("Shooter", "ShootTest", new ShootTest(shooter));
    }

    private void configureBindings() {
        drivetrain.setDefaultCommand(new Drive(drivetrain, joyleft::getY, joyleft::getX, joyright::getX));
    }

    public SwerveDrivetrain getDrivetrain() {
        return drivetrain;
    }

    public Command getAutoCommand() {
        return autoChooser.getAutoCommand();
    }
    public static boolean shouldFlip(){
        Optional<DriverStation.Alliance> alliance = DriverStation.getAlliance();
        return alliance.filter(value -> value == DriverStation.Alliance.Red).isPresent();
    }

    public AutoChooser getAutoChooser() {
        return autoChooser;
    }
}