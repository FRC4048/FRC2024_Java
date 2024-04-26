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
import edu.wpi.first.wpilibj2.command.*;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.autochooser.chooser.AutoChooser;
import frc.robot.autochooser.chooser.AutoChooser2024;
import frc.robot.commands.CancelAll;
import frc.robot.commands.MoveToGamepiece;
import frc.robot.commands.SetAlignable;
import frc.robot.commands.climber.ManualControlClimber;
import frc.robot.commands.deployer.LowerDeployer;
import frc.robot.commands.deployer.RaiseDeployer;
import frc.robot.commands.drivetrain.Drive;
import frc.robot.commands.drivetrain.MoveDistance;
import frc.robot.commands.drivetrain.SetInitOdom;
import frc.robot.commands.drivetrain.SetRobotDriveMode;
import frc.robot.commands.feeder.FeederBackDrive;
import frc.robot.commands.feeder.StartFeeder;
import frc.robot.commands.feeder.StopFeeder;
import frc.robot.commands.feeder.TimedFeeder;
import frc.robot.commands.intake.CurrentBasedIntakeFeeder;
import frc.robot.commands.intake.StartIntake;
import frc.robot.commands.intake.StopIntake;
import frc.robot.commands.pathplanning.*;
import frc.robot.commands.ramp.RampFollow;
import frc.robot.commands.ramp.RampMove;
import frc.robot.commands.ramp.RampMoveAndWait;
import frc.robot.commands.ramp.ResetRamp;
import frc.robot.commands.sequences.SpoolExitAndShootAtSpeed;
import frc.robot.commands.shooter.AdvancedSpinningShot;
import frc.robot.commands.shooter.SetShooterSpeed;
import frc.robot.commands.shooter.ShootSpeaker;
import frc.robot.commands.shooter.StopShooter;
import frc.robot.constants.Constants;
import frc.robot.constants.GameConstants;
import frc.robot.subsystems.SwerveDrivetrain;
import frc.robot.subsystems.climber.Climber;
import frc.robot.subsystems.climber.MockClimberIO;
import frc.robot.subsystems.climber.RealCimberIO;
import frc.robot.subsystems.deployer.Deployer;
import frc.robot.subsystems.deployer.MockDeployerIO;
import frc.robot.subsystems.deployer.RealDeployerIO;
import frc.robot.subsystems.feeder.Feeder;
import frc.robot.subsystems.feeder.MockFeederIO;
import frc.robot.subsystems.feeder.RealFeederIO;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.intake.MockIntakeIO;
import frc.robot.subsystems.intake.RealIntakeIO;
import frc.robot.subsystems.lightstrip.LightStrip;
import frc.robot.subsystems.lightstrip.MockLightStripIO;
import frc.robot.subsystems.lightstrip.RealLightStripIO;
import frc.robot.subsystems.ramp.MockRampIO;
import frc.robot.subsystems.ramp.Ramp;
import frc.robot.subsystems.ramp.RealRampIO;
import frc.robot.subsystems.shooter.MockShooterIO;
import frc.robot.subsystems.shooter.RealShooterIO;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.vision.MockVisionIO;
import frc.robot.subsystems.vision.RealVisionIO;
import frc.robot.subsystems.vision.Vision;
import frc.robot.swervev2.KinematicsConversionConfig;
import frc.robot.swervev2.SwerveIdConfig;
import frc.robot.swervev2.SwervePidConfig;
import frc.robot.utils.*;
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
    private final JoystickButton joyLeftButton2 = new JoystickButton(joyleft, 2);
    private final JoystickButton joyRightButton3 = new JoystickButton(joyright, 3);
    private final JoystickButton joyLeftButton3 = new JoystickButton(joyleft, 3);
    private final Shooter shooter;
    private final Deployer deployer;
    private final Feeder feeder;
    private final Ramp ramp;
    private final Climber climber;
    private final Vision vision;
    private final Intake intake;
    private final LightStrip lightStrip;
    private final CommandXboxController controller = new CommandXboxController(Constants.XBOX_CONTROLLER_ID);
    private SwerveDrivetrain drivetrain;
    private AutoChooser2024 autoChooser;

    /**f
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        if (Robot.isReal()){
            shooter = new Shooter(new RealShooterIO());
            deployer = new Deployer(new RealDeployerIO());
            feeder = new Feeder(new RealFeederIO());
            ramp = new Ramp(new RealRampIO());
            climber = new Climber(new RealCimberIO());
            vision = new Vision(new RealVisionIO());
            intake = new Intake(new RealIntakeIO());
            lightStrip = new LightStrip(new RealLightStripIO());
        }else{
            shooter = new Shooter(new MockShooterIO());
            deployer = new Deployer(new MockDeployerIO());
            feeder = new Feeder(new MockFeederIO());
            ramp = new Ramp(new MockRampIO());
            climber = new Climber(new MockClimberIO());
            vision = new Vision(new MockVisionIO());
            intake = new Intake(new MockIntakeIO());
            lightStrip = new LightStrip(new MockLightStripIO());
        }
        setupDriveTrain();
        registerPathPlanableCommands();
        setupPathPlanning();
        setupAutoChooser();
        configureBindings();
        putShuffleboardCommands();
    }

    private void setupAutoChooser() {
        autoChooser = new AutoChooser2024(intake, shooter, feeder, deployer, ramp, lightStrip);
        autoChooser.addOnValidationCommand(() -> new SetInitOdom(drivetrain, autoChooser));
        autoChooser.forceRefresh();
    }

    /**
     * NamedCommands
     */
    private void registerPathPlanableCommands() {
        NamedCommands.registerCommand("SlurpWithRamp", new ParallelRaceGroup(
                        new CurrentBasedIntakeFeeder(intake, feeder, lightStrip),
                        new SequentialCommandGroup(
                                new ResetRamp(ramp, lightStrip),
                                new WaitCommand(2)).withName("ResetRampPostIntake")
        ).withName("SlurpWithRamp"));
        NamedCommands.registerCommand("PathPlannerShoot", new PathPlannerShoot(shooter, feeder, ramp, intake, lightStrip));
        NamedCommands.registerCommand("ComboShot", new ComboShot(shooter, feeder, lightStrip));
        NamedCommands.registerCommand("FeederGamepieceUntilLeave", new TimedFeeder(feeder, lightStrip ,Constants.TIMED_FEEDER_EXIT));
        NamedCommands.registerCommand("ShootAndDrop", new ShootAndDrop(shooter, feeder, deployer, lightStrip));
        NamedCommands.registerCommand("FeederBackDrive", new FeederBackDrive(feeder, lightStrip));
        NamedCommands.registerCommand("ResetRamp", new ResetRamp(ramp, lightStrip));
        NamedCommands.registerCommand("RampShootComboCenter", new RampShootCombo(ramp, shooter, lightStrip, Constants.RAMP_CENTER_AUTO_SHOOT));// second piece
        NamedCommands.registerCommand("RampShootComboSide", new RampShootCombo(ramp, shooter, lightStrip, Constants.RAMP_SIDE_AUTO_SHOOT)); // first and third
        NamedCommands.registerCommand("RampShootComboSide2", new RampShootCombo(ramp, shooter, lightStrip, Constants.RAMP_DIP_AUTO_SHOOT)); // first and third
        NamedCommands.registerCommand("MoveToGamePiece", new DevourerPiece(drivetrain, vision, intake, feeder, deployer, lightStrip));
    }

    /**
     * <a href=https://github.com/mjansen4857/pathplanner/wiki/PathPlannerLib-Changelog>Change log</a>
     */
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
        ThreadedGyro navxGyro = new ThreadedGyro(new AHRS());
        navxGyro.start();
        this.drivetrain = new SwerveDrivetrain(frontLeftIdConf, frontRightIdConf, backLeftIdConf, backRightIdConf, kinematicsConversionConfig, pidConfig, navxGyro);
    }

    public void putShuffleboardCommands() {

        if (Constants.DEPLOYER_DEBUG) {
            SmartShuffleboard.putCommand("Deployer", "DeployerLower", new RaiseDeployer(deployer, lightStrip));
            SmartShuffleboard.putCommand("Deployer", "DeployerRaise", new LowerDeployer(deployer, lightStrip));
        }
        if (Constants.RAMP_DEBUG) {
            SmartShuffleboard.put("Ramp", "myTargetPos", 0);
            SmartShuffleboard.putCommand("Ramp", "SetRamp", new RampMove(ramp, () -> SmartShuffleboard.getDouble("Ramp", "myTargetPos", 0)));
            SmartShuffleboard.putCommand("Ramp", "ResetRamp", new ResetRamp(ramp, lightStrip));
        }
        if (Constants.SHOOTER_DEBUG) {
            SmartShuffleboard.putCommand("Shooter", "Spool Exit and shoot", new SpoolExitAndShootAtSpeed(shooter, feeder, lightStrip));
            SmartShuffleboard.putCommand("Shooter", "Set Shooter Speed", new SetShooterSpeed(shooter, lightStrip));
        }
        if (Constants.FEEDER_DEBUG) {
            SmartShuffleboard.putCommand("Feeder", "Feed", new StartFeeder(feeder, lightStrip));
        }
        if (Constants.INTAKE_DEBUG) {
            SmartShuffleboard.putCommand("Intake", "Start Intake", new StartIntake(intake, 5));
            SmartShuffleboard.putCommand("Intake", "IntakeFeederCombo", new SequentialCommandGroup(
                        new SpoolIntake(intake, Constants.INTAKE_SPOOL_TIME),
                        new CurrentBasedIntakeFeeder(intake, feeder, lightStrip)
                    ).withName("IntakeFeederCurrentCombo")
            );
        }
        if (Constants.SWERVE_DEBUG) {
            SmartShuffleboard.putCommand("Drivetrain", "Move Forward 1ft", new MoveDistance(drivetrain, lightStrip,0.3048, 0, 0.4));
            SmartShuffleboard.putCommand("Drivetrain", "Move Backward 1ft", new MoveDistance(drivetrain, lightStrip, -0.3048, 0, 0.4));
            SmartShuffleboard.putCommand("Drivetrain", "Move Left 1ft", new MoveDistance(drivetrain, lightStrip, 0, 0.3048, 0.4));
            SmartShuffleboard.putCommand("Drivetrain", "Move Right 1ft", new MoveDistance(drivetrain, lightStrip, 0, -0.3048, 0.4));
            SmartShuffleboard.putCommand("Drivetrain", "Move Left + Forward 1ft", new MoveDistance(drivetrain, lightStrip, 0.3048, 0.3048, 0.4));
            SmartShuffleboard.putCommand("Test", "Gamepiece", new MoveToGamepiece(drivetrain, vision));
            SmartShuffleboard.putCommand("Test", "DEVOUR", new DevourerPiece(drivetrain, vision, intake, feeder, deployer, lightStrip));
        }
        if (Constants.VISION_DEBUG) {
            SmartShuffleboard.putCommand("Vision", "Gamepiece", new MoveToGamepiece(drivetrain, vision));
            SmartShuffleboard.putCommand("Vision", "DEVOUR", new DevourerPiece(drivetrain, vision, intake, feeder, deployer, lightStrip));
        }
    }

    private void configureBindings() {
        drivetrain.setDefaultCommand(new Drive(drivetrain, joyleft::getY, joyleft::getX, joyright::getX, drivetrain::getDriveMode));
        Command rampMoveAndSpin = new ParallelRaceGroup(
                new RampFollow(ramp, drivetrain, lightStrip),
                new AdvancedSpinningShot(shooter, lightStrip, () -> drivetrain.getPose(), () -> drivetrain.getAlignable())
        ).withName("AdvancedAutoShoot");
        joyLeftButton1.onTrue(new SetAlignable(drivetrain, Alignable.SPEAKER)).onFalse(new SetAlignable(drivetrain, null));
        joyLeftButton3.onTrue(rampMoveAndSpin);
        joyRightButton1.onTrue(new SetAlignable(drivetrain, Alignable.AMP)).onFalse(new SetAlignable(drivetrain, null));
        joyLeftButton2.whileTrue(new SetRobotDriveMode(drivetrain, DriveMode.ROBOT_CENTRIC)).whileFalse(new SetRobotDriveMode(drivetrain, DriveMode.FIELD_CENTRIC));
        ManualControlClimber leftClimbCmd = new ManualControlClimber(climber, () -> -controller.getLeftY()); // negative because Y "up" is negative

        climber.setDefaultCommand(leftClimbCmd);

        // Set up to shoot Speaker CLOSE - Y
        controller.y().onTrue(new ParallelCommandGroup(
                new RampMove(ramp, () -> GameConstants.RAMP_POS_SHOOT_SPEAKER_CLOSE),
                new ShootSpeaker(shooter, drivetrain, lightStrip)
        ).withName("Setup Speaker Shot (CLOSE)"));

        // Set up to shoot Speaker AWAY - X
        controller.x().onTrue(new ParallelCommandGroup(
                new RampMove(ramp, () -> GameConstants.RAMP_POS_SHOOT_SPEAKER_AWAY),
                new ShootSpeaker(shooter, drivetrain, lightStrip)
        ).withName("Setup Speaker Shot (AWAY)"));

        // Cancell all - B
        controller.b().onTrue(new CancelAll(ramp, shooter, lightStrip));

        // Shoot - Right Trigger
        controller.rightTrigger(0.5).onTrue(new SequentialCommandGroup(
                new TimedFeeder(feeder, lightStrip, Constants.TIMED_FEEDER_EXIT),
                new WaitCommand(GameConstants.SHOOTER_TIME_BEFORE_STOPPING),
                new StopShooter(shooter),
                new RampMove(ramp, () -> GameConstants.RAMP_POS_STOW)
        ).withName("Operator Shoot"));

        //Driver Shoot
        joyRightButton2.onTrue(new SequentialCommandGroup(new TimedFeeder(feeder,lightStrip, Constants.TIMED_FEEDER_EXIT),
                new WaitCommand(GameConstants.SHOOTER_TIME_BEFORE_STOPPING),
                new StopShooter(shooter),
                new RampMove(ramp, () -> GameConstants.RAMP_POS_STOW)
        ).withName("Driver Shoot"));

        // start intaking a note
        Command lowerIntake = new ParallelCommandGroup(
                new SpoolIntake(intake, Constants.INTAKE_SPOOL_TIME),
                new LowerDeployer(deployer, lightStrip),
                new RampMoveAndWait(ramp, lightStrip ,() -> GameConstants.RAMP_POS_STOW)
        ).withName("lowerIntake");
        Command endIntake = new RaiseDeployer(deployer, lightStrip);

        controller.povDown().onTrue(new SequentialCommandGroup(
                lowerIntake,
                new CurrentBasedIntakeFeeder(intake, feeder, lightStrip),
                endIntake
        ).withName("Intake a Note"));

        controller.leftTrigger(.5).onTrue(new FeederBackDrive(feeder, lightStrip));

        // stop intake
        controller.povUp().onTrue(new ParallelCommandGroup(
                new RaiseDeployer(deployer, lightStrip),
                new StopFeeder(feeder),
                new StopIntake(intake)
        ).withName("stop intake"));

        joyRightButton3.onTrue(new TimedFeeder(feeder, lightStrip, Constants.TIMED_FEEDER_EXIT));
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

    public Feeder getFeeder() {
        return feeder;
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

    public Intake getIntake() {
        return intake;
    }

    public LightStrip getLEDStrip() {
        return lightStrip;
    }
}