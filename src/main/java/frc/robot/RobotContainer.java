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
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.autochooser.chooser.AutoChooser;
import frc.robot.autochooser.chooser.AutoChooser2024;
import frc.robot.commands.MoveToGamepiece;
import frc.robot.commands.deployer.LowerDeployer;
import frc.robot.commands.deployer.RaiseDeployer;
import frc.robot.commands.drivetrain.MoveDistance;
import frc.robot.commands.feeder.StartFeeder;
import frc.robot.commands.intake.StartIntake;
import frc.robot.commands.ramp.RampMove;
import frc.robot.commands.ramp.ResetRamp;
import frc.robot.commands.sequences.SpoolExitAndShootAtSpeed;
import frc.robot.commands.shooter.SetShooterSpeed;
import frc.robot.constants.Constants;
import frc.robot.subsystems.*;
import frc.robot.swervev2.KinematicsConversionConfig;
import frc.robot.swervev2.SwerveIdConfig;
import frc.robot.swervev2.SwervePidConfig;
import frc.robot.utils.Gain;
import frc.robot.utils.PID;
import frc.robot.utils.logging.CommandUtil;
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
    private final JoystickButton joyLeftButton1 = new JoystickButton(joyleft, 1);
    private final JoystickButton joyRightButton1 = new JoystickButton(joyright, 1);
    private final JoystickButton joyRightButton2 = new JoystickButton(joyright, 2);
    private SwerveDrivetrain drivetrain;
    private final AutoChooser2024 autoChooser;
    private final Amp amp = new Amp();
    private final Shooter shooter = new Shooter();
    private final Deployer deployer = new Deployer();
    private final Feeder feeder = new Feeder();
    private final Ramp ramp = new Ramp();
    private Climber climber;
    private final Vision vision = new Vision();
    private final IntakeSubsystem intake = new IntakeSubsystem();
    private final CommandXboxController controller = new CommandXboxController(Constants.XBOX_CONTROLLER_ID);
    private final Commands commands;


    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        setupDriveTrain();
        registerPathPlanableCommands();
        setupPathPlanning();
        autoChooser = new AutoChooser2024(intake, shooter, feeder, deployer, ramp);
        commands = new Commands(drivetrain, intake, ramp, feeder, deployer, climber, shooter,amp ,joyleft, joyright, controller, autoChooser);
        autoChooser.addOnValidationCommand(commands::getSetInitalOdom);
        autoChooser.forceRefresh();
        configureBindings();
        putShuffleboardCommands();
    }

    /**
     * NamedCommands
     */
    private void registerPathPlanableCommands() {
        NamedCommands.registerCommand("SlurpWithRamp", commands.getSlurpWithRamp());
        NamedCommands.registerCommand("PathPlannerShoot", commands.getPathplannerShoot());
        NamedCommands.registerCommand("ComboShot", commands.getComboShot());
        NamedCommands.registerCommand("FeederGamepieceUntilLeave", commands.getFeederUntilLeave());
        NamedCommands.registerCommand("ShootAndDrop", commands.getShootAndDrop());
        NamedCommands.registerCommand("FeederBackDrive", commands.getFeederBackDrive());
        NamedCommands.registerCommand("ResetRamp", commands.getResetRamp());
        NamedCommands.registerCommand("RampShootComboCenter", commands.getRampComboShootCenter());// second piece
        NamedCommands.registerCommand("RampShootComboSide", commands.getRampComboShootSide()); // first and third
        NamedCommands.registerCommand("RampShootComboSide2", commands.getRampComboShootSide2()); // first and third
    }

    private void setupPathPlanning() {
        AutoBuilder.configureHolonomic(drivetrain::getPose,
                drivetrain::resetOdometry,
                drivetrain::speedsFromStates,
                drivetrain::drive,
                new HolonomicPathFollowerConfig(
                        new PIDConstants(Constants.PATH_PLANNER_TRANSLATION_PID_P, Constants.PATH_PLANNER_TRANSLATION_PID_I, Constants.PATH_PLANNER_TRANSLATION_PID_D), // Translation PID constants
                        new PIDConstants(Constants.PATH_PLANNER_ROTATION_PID_P, Constants.PATH_PLANNER_ROTATION_PID_I, Constants.PATH_PLANNER_ROTATION_PID_D), // Rotation PID constants
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
        climber = new Climber();
        this.drivetrain = new SwerveDrivetrain(frontLeftIdConf, frontRightIdConf, backLeftIdConf, backRightIdConf, kinematicsConversionConfig, pidConfig, navxGyro);
    }

    public void putShuffleboardCommands() {
        if (Constants.AMP_DEBUG) {
//            SmartShuffleboard.putCommand("Amp", "Deploy AMP", CommandUtil.logged(new DeployAmpSequence(ramp, amp)));
//            SmartShuffleboard.putCommand("Amp", "Retract AMP", CommandUtil.logged(new RetractAmpSequence(ramp, amp)));
            SmartShuffleboard.put("Amp", "isDeployed", amp.isAmpDeployed());
        }
        SmartShuffleboard.putCommand("Test", "Gamepiece", new MoveToGamepiece(drivetrain, vision));
        if (Constants.DEPLOYER_DEBUG) {
            SmartShuffleboard.putCommand("Deployer", "DeployerLower", CommandUtil.logged(new RaiseDeployer(deployer)));
            SmartShuffleboard.putCommand("Deployer", "DeployerRaise", CommandUtil.logged(new LowerDeployer(deployer)));
        }
        if (Constants.RAMP_DEBUG) {
            SmartShuffleboard.put("Ramp", "myTargetPos", 0);
            SmartShuffleboard.putCommand("Ramp", "SetRamp", CommandUtil.logged(new RampMove(ramp, () -> SmartShuffleboard.getDouble("Ramp", "myTargetPos", 0))));
//            SmartShuffleboard.putCommand("Ramp", "SetArmPID400", CommandUtil.logged(new RampMove(ramp, 15 )));
//            SmartShuffleboard.putCommand("Ramp", "SetArmPID500", CommandUtil.logged(new RampMove(ramp, 500)));
            SmartShuffleboard.putCommand("Ramp", "ResetRamp", CommandUtil.logged(new ResetRamp(ramp)));
        }
        if (Constants.SHOOTER_DEBUG) {
            SmartShuffleboard.putCommand("Shooter", "Spool Exit and shoot", new SpoolExitAndShootAtSpeed(shooter, feeder, ramp));
            SmartShuffleboard.putCommand("Shooter", "Set Shooter Speed", new SetShooterSpeed(shooter));
//            SmartShuffleboard.putCommand("Shooter", "Shoot", new Shoot(shooter));
//            SmartShuffleboard.putCommand("Shooter", "Shoot", CommandUtil.logged(new Shoot(shooter)));

        }
        if (Constants.FEEDER_DEBUG) {
            SmartShuffleboard.putCommand("Feeder", "Feed", CommandUtil.logged(new StartFeeder(feeder)));
        }
        if (Constants.INTAKE_DEBUG) {
            SmartShuffleboard.putCommand("Intake", "Start Intake", CommandUtil.logged(new StartIntake(intake, 5)));
        }
        if (Constants.SWERVE_DEBUG) {
            SmartShuffleboard.putCommand("Drivetrain", "Move Forward 1ft", CommandUtil.logged(new MoveDistance(drivetrain, 0.3048, 0, 0.4)));
            SmartShuffleboard.putCommand("Drivetrain", "Move Backward 1ft", CommandUtil.logged(new MoveDistance(drivetrain, -0.3048, 0, 0.4)));
            SmartShuffleboard.putCommand("Drivetrain", "Move Left 1ft", CommandUtil.logged(new MoveDistance(drivetrain, 0, 0.3048, 0.4)));
            SmartShuffleboard.putCommand("Drivetrain", "Move Right 1ft", CommandUtil.logged(new MoveDistance(drivetrain, 0, -0.3048, 0.4)));
            SmartShuffleboard.putCommand("Drivetrain", "Move Left + Forward 1ft", CommandUtil.logged(new MoveDistance(drivetrain, 0.3048, 0.3048, 0.4)));
        }
    }

    private void configureBindings() {
        drivetrain.setDefaultCommand(commands.getDrive());
        joyLeftButton1.onTrue(commands.getAlignSpeaker()).onFalse(commands.getClearAlignable());
        joyRightButton1.onTrue(commands.getSetAlignableAmp()).onFalse(commands.getClearAlignable());
        climber.setDefaultCommand(commands.getManualClimb());
        // Disengage
        controller.leftBumper().onTrue(commands.getDisengageRatchet());

        // Engage
        controller.rightBumper().onTrue(commands.getEngateRatchet());

        // Set up to shoot Speaker CLOSE - Y
        controller.y().onTrue(commands.getShootSpeakerClose());

        // Set up to shoot Speaker AWAY - X
        controller.x().onTrue(commands.getShootSpeakerFar());

        // Set up to shoot AMP - A
        controller.a().onTrue(commands.getSetupAmpShoot());

        // Cancell all - B
        controller.b().onTrue(commands.getCalnsellAll());

        // Shoot - Right Trigger
        controller.rightTrigger(0.5).onTrue(commands.getOperatorShoot());

        //Driver Shoot
        joyRightButton2.onTrue(commands.getDriverShoot());

        // amp up and down
        controller.povLeft().onTrue(commands.getToggleAmp());

        controller.povDown().onTrue(commands.getIntakeNote());

        controller.leftTrigger(.5).onTrue(commands.getFeederBackDrive());

        // stop intake
        controller.povUp().onTrue(commands.getStopIntake());

        controller.rightTrigger().onTrue(commands.getAdvancedShot());
    }

    public SwerveDrivetrain getDrivetrain() {
        return drivetrain;
    }

    public Ramp getRamp() {
        return ramp;
    }

    public Climber getClimber() {
        return climber;
    }

    public Deployer getDeployer() {
        return deployer;
    }

    public Command getAutoCommand() {
        return autoChooser.getAutoCommand();
    }

    /**
     * Returns a boolean based on the current alliance color assigned by the FMS.
     *
     * @return true if red, false if blue
     */
    public static boolean isRedAlliance() {
        Optional<DriverStation.Alliance> alliance = DriverStation.getAlliance();
        return alliance.filter(value -> value == DriverStation.Alliance.Red).isPresent();
    }

    public AutoChooser getAutoChooser() {
        return autoChooser;
    }

    public Feeder getFeeder() {
        return feeder;
    }
}