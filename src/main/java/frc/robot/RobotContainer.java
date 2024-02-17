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
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.autochooser.chooser.AutoChooser;
import frc.robot.autochooser.chooser.AutoChooser2024;
import frc.robot.commands.ReportErrorCommand;
import frc.robot.commands.cannon.Shoot;
import frc.robot.commands.cannon.StartFeeder;
import frc.robot.commands.cannon.StartIntake;
import frc.robot.commands.climber.StaticClimb;
import frc.robot.commands.drivetrain.Drive;
import frc.robot.commands.drivetrain.SetInitOdom;
import frc.robot.commands.ramp.RampMove;
import frc.robot.constants.Constants;
import frc.robot.subsystems.*;
import frc.robot.swervev2.KinematicsConversionConfig;
import frc.robot.swervev2.SwerveIdConfig;
import frc.robot.swervev2.SwervePidConfig;
import frc.robot.utils.Alignable;
import frc.robot.utils.Gain;
import frc.robot.utils.PID;
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
      private final JoystickButton joyLeftButton1 = new JoystickButton(joyleft,1);
      private final JoystickButton joyRightButton1 = new JoystickButton(joyright,1);
      private SwerveDrivetrain drivetrain;
      private final Ramp ramp;
      private final AutoChooser2024 autoChooser;
      private final Shooter shooter = new Shooter();
      private final Feeder feeder = new Feeder();
      private Climber climber;
      private final IntakeSubsystem intakeSubsystem = new IntakeSubsystem();

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
                        Constants.MAX_VELOCITY, // Max module speed, in m/s
                        Constants.ROBOT_RADIUS, // Drive base radius in meters. Distance from robot center to the furthest module.
                        new ReplanningConfig()
                ), RobotContainer::isRedAlliance, drivetrain);
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

        KinematicsConversionConfig kinematicsConversionConfig = new KinematicsConversionConfig(Constants.WHEEL_RADIUS, Constants.SWERVE_MODULE_PROFILE.getDriveRatio(), Constants.SWERVE_MODULE_PROFILE.getSteerRatio());
        SwervePidConfig pidConfig = new SwervePidConfig(drivePid, steerPid, driveGain, steerGain, constraints);
        AHRS navxGyro = new AHRS();
        climber = new Climber(navxGyro);
        this.drivetrain = new SwerveDrivetrain(frontLeftIdConf, frontRightIdConf, backLeftIdConf, backRightIdConf, kinematicsConversionConfig, pidConfig, navxGyro);
    }

    public void putShuffleboardCommands() {
        if (Constants.RAMP_DEBUG){
            SmartShuffleboard.putCommand("Ramp", "SetArmPID400", new RampMove(ramp, 400));
            SmartShuffleboard.putCommand("Ramp", "SetArmPID500", new RampMove(ramp, 500));
        }
        if (Constants.SHOOTER_DEBUG){
            SmartShuffleboard.putCommand("Shooter", "Shoot", new Shoot(shooter));

        }
        if (Constants.FEEDER_DEBUG){
            SmartShuffleboard.putCommand("Feeder", "Feed", new StartFeeder(feeder));
            SmartShuffleboard.putCommand("Feeder", "StartFeeder", new FeederColorMatcher(feeder));
        }
        if (Constants.CLIMBER_DEBUG) {
            SmartShuffleboard.putCommand("Climber", "Climb", new StaticClimb(climber));
        }
        if (Constants.INTAKE_DEBUG){
        SmartShuffleboard.putCommand("Intake", "Start Intake", new StartIntake(intakeSubsystem));
        }
    }

    private void configureBindings() {
        drivetrain.setDefaultCommand(new Drive(drivetrain, joyleft::getY, joyleft::getX, joyright::getX));
        joyLeftButton1.onTrue(new InstantCommand(() -> drivetrain.setAlignable(Alignable.SPEAKER))).onFalse(new InstantCommand(()-> drivetrain.setAlignable(null)));
        joyRightButton1.onTrue(new InstantCommand(() -> drivetrain.setAlignable(Alignable.AMP))).onFalse(new InstantCommand(()-> drivetrain.setAlignable(null)));
    }

    public SwerveDrivetrain getDrivetrain() {
        return drivetrain;
    }

    public Command getAutoCommand() {
        return autoChooser.getAutoCommand();
    }

    /**
     * Returns a boolean based on the current alliance color assigned by the FMS.
     * @return true if red, false if blue
     */
    public static boolean isRedAlliance(){
        Optional<DriverStation.Alliance> alliance = DriverStation.getAlliance();
        return alliance.filter(value -> value == DriverStation.Alliance.Red).isPresent();
    }

    public AutoChooser getAutoChooser() {
        return autoChooser;
    }
}