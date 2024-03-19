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
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.autochooser.chooser.AutoChooser;
import frc.robot.autochooser.chooser.AutoChooser2024;
import frc.robot.commands.MoveToGamepiece;
import frc.robot.commands.SetAlignable;
import frc.robot.commands.amp.DeployAmp;
import frc.robot.commands.amp.RetractAmp;
import frc.robot.commands.amp.ToggleAmp;
import frc.robot.commands.climber.ManualControlClimber;
import frc.robot.commands.deployer.LowerDeployer;
import frc.robot.commands.deployer.RaiseDeployer;
import frc.robot.commands.drivetrain.Drive;
import frc.robot.commands.drivetrain.MoveDistance;
import frc.robot.commands.drivetrain.SetInitOdom;
import frc.robot.commands.drivetrain.ToggleDrivingMode;
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
import frc.robot.commands.sequences.CancelAllSequence;
import frc.robot.commands.sequences.SpoolExitAndShootAtSpeed;
import frc.robot.commands.shooter.*;
import frc.robot.constants.Constants;
import frc.robot.constants.GameConstants;
import frc.robot.subsystems.*;
import frc.robot.swervev2.KinematicsConversionConfig;
import frc.robot.swervev2.SwerveIdConfig;
import frc.robot.swervev2.SwervePidConfig;
import frc.robot.utils.Alignable;
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
    private final JoystickButton joyLeftButton2 = new JoystickButton(joyleft, 2);
    private final JoystickButton joyRightButton3 = new JoystickButton(joyright, 3);
    private final JoystickButton joyLeftButton3 = new JoystickButton(joyleft, 3);
    private final Amp amp = new Amp();
    private final Shooter shooter = new Shooter();
    private final Deployer deployer = new Deployer();
    private final Feeder feeder = new Feeder();
    private final Ramp ramp = new Ramp();
    private final Climber climber = new Climber();
    private final Vision vision = new Vision();
    private final IntakeSubsystem intake = new IntakeSubsystem();
    private final LightStrip lightStrip = new LightStrip(Constants.LIGHTSTRIP_PORT);

    private final CommandXboxController controller = new CommandXboxController(Constants.XBOX_CONTROLLER_ID);
    private SwerveDrivetrain drivetrain;
    private AutoChooser2024 autoChooser;

    /**f
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        setupDriveTrain();
        registerPathPlanableCommands();
        setupPathPlanning();
        setupAutoChooser();
        configureBindings();
        putShuffleboardCommands();
    }

    private void setupAutoChooser() {
        autoChooser = new AutoChooser2024(intake, shooter, feeder, deployer, ramp, lightStrip);
        autoChooser.addOnValidationCommand(() -> CommandUtil.logged(new SetInitOdom(drivetrain, autoChooser)));
        autoChooser.forceRefresh();
    }

    /**
     * NamedCommands
     */
    private void registerPathPlanableCommands() {
        NamedCommands.registerCommand("SlurpWithRamp", new ParallelDeadlineGroup(
                new CurrentBasedIntakeFeeder(intake, feeder, lightStrip),
                new ResetRamp(ramp, lightStrip))
        );
        NamedCommands.registerCommand("PathPlannerShoot", CommandUtil.logged(new PathPlannerShoot(shooter, feeder, ramp, intake, lightStrip)));
        NamedCommands.registerCommand("ComboShot", CommandUtil.logged(new ComboShot(shooter, feeder, lightStrip)));
        NamedCommands.registerCommand("FeederGamepieceUntilLeave", CommandUtil.logged(new TimedFeeder(feeder, lightStrip ,Constants.TIMED_FEEDER_EXIT)));
        NamedCommands.registerCommand("ShootAndDrop", CommandUtil.logged(new ShootAndDrop(shooter, feeder, deployer, lightStrip)));
        NamedCommands.registerCommand("FeederBackDrive", CommandUtil.logged(new FeederBackDrive(feeder, lightStrip)));
        NamedCommands.registerCommand("ResetRamp", CommandUtil.logged(new ResetRamp(ramp, lightStrip)));
        NamedCommands.registerCommand("RampShootComboCenter", CommandUtil.logged(new RampShootCombo(ramp, shooter, lightStrip, Constants.RAMP_CENTER_AUTO_SHOOT)));// second piece
        NamedCommands.registerCommand("RampShootComboSide", CommandUtil.logged(new RampShootCombo(ramp, shooter, lightStrip, Constants.RAMP_SIDE_AUTO_SHOOT))); // first and third
        NamedCommands.registerCommand("RampShootComboSide2", CommandUtil.logged(new RampShootCombo(ramp, shooter, lightStrip, Constants.RAMP_DIP_AUTO_SHOOT))); // first and third
        NamedCommands.registerCommand("MoveToGamePiece", CommandUtil.logged(new DevourerPiece(drivetrain, vision, intake, feeder, deployer, lightStrip)));
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
        AHRS navxGyro = new AHRS();
        this.drivetrain = new SwerveDrivetrain(frontLeftIdConf, frontRightIdConf, backLeftIdConf, backRightIdConf, kinematicsConversionConfig, pidConfig, navxGyro);
    }

    public void putShuffleboardCommands() {

        if (Constants.AMP_DEBUG) {
//            SmartShuffleboard.putCommand("Amp", "Deploy AMP", CommandUtil.logged(new DeployAmpSequence(ramp, amp)));
//            SmartShuffleboard.putCommand("Amp", "Retract AMP", CommandUtil.logged(new RetractAmpSequence(ramp, amp)));
            SmartShuffleboard.put("Amp", "isDeployed", amp.isAmpDeployed());
        }
        if (Constants.DEPLOYER_DEBUG) {
            SmartShuffleboard.putCommand("Deployer", "DeployerLower", CommandUtil.logged(new RaiseDeployer(deployer, lightStrip)));
            SmartShuffleboard.putCommand("Deployer", "DeployerRaise", CommandUtil.logged(new LowerDeployer(deployer, lightStrip)));
        }
        if (Constants.RAMP_DEBUG) {
            SmartShuffleboard.put("Ramp", "myTargetPos", 0);
            SmartShuffleboard.putCommand("Ramp", "SetRamp", CommandUtil.logged(new RampMove(ramp, () -> SmartShuffleboard.getDouble("Ramp", "myTargetPos", 0))));
//            SmartShuffleboard.putCommand("Ramp", "SetArmPID400", CommandUtil.logged(new RampMove(ramp, 15 )));
//            SmartShuffleboard.putCommand("Ramp", "SetArmPID500", CommandUtil.logged(new RampMove(ramp, 500)));
            SmartShuffleboard.putCommand("Ramp", "ResetRamp", CommandUtil.logged(new ResetRamp(ramp, lightStrip)));
        }
        if (Constants.SHOOTER_DEBUG) {
            SmartShuffleboard.putCommand("Shooter", "Spool Exit and shoot", new SpoolExitAndShootAtSpeed(shooter, feeder, lightStrip));
            SmartShuffleboard.putCommand("Shooter", "Set Shooter Speed", new SetShooterSpeed(shooter, lightStrip));
//            SmartShuffleboard.putCommand("Shooter", "Shoot", new Shoot(shooter));
//            SmartShuffleboard.putCommand("Shooter", "Shoot", CommandUtil.logged(new Shoot(shooter)));

        }
        if (Constants.FEEDER_DEBUG) {
            SmartShuffleboard.putCommand("Feeder", "Feed", CommandUtil.logged(new StartFeeder(feeder, lightStrip)));
        }
        if (Constants.INTAKE_DEBUG) {
            SmartShuffleboard.putCommand("Intake", "Start Intake", CommandUtil.logged(new StartIntake(intake, 5)));
            SmartShuffleboard.putCommand("Intake", "IntakeFeederCombo", CommandUtil.sequence(
                    "IntakeFeederCurrentCombo",
                    new SpoolIntake(intake, Constants.INTAKE_SPOOL_TIME),
                    new CurrentBasedIntakeFeeder(intake, feeder, lightStrip))
            );
        }
        if (Constants.SWERVE_DEBUG) {
            SmartShuffleboard.putCommand("Drivetrain", "Move Forward 1ft", CommandUtil.logged(new MoveDistance(drivetrain, lightStrip,0.3048, 0, 0.4)));
            SmartShuffleboard.putCommand("Drivetrain", "Move Backward 1ft", CommandUtil.logged(new MoveDistance(drivetrain, lightStrip, -0.3048, 0, 0.4)));
            SmartShuffleboard.putCommand("Drivetrain", "Move Left 1ft", CommandUtil.logged(new MoveDistance(drivetrain, lightStrip, 0, 0.3048, 0.4)));
            SmartShuffleboard.putCommand("Drivetrain", "Move Right 1ft", CommandUtil.logged(new MoveDistance(drivetrain, lightStrip, 0, -0.3048, 0.4)));
            SmartShuffleboard.putCommand("Drivetrain", "Move Left + Forward 1ft", CommandUtil.logged(new MoveDistance(drivetrain, lightStrip, 0.3048, 0.3048, 0.4)));
            SmartShuffleboard.putCommand("Test", "Gamepiece", new MoveToGamepiece(drivetrain, vision));
            SmartShuffleboard.putCommand("Test", "DEVOUR", new DevourerPiece(drivetrain, vision, intake, feeder, deployer, lightStrip));
        }
    }

    private void configureBindings() {
        drivetrain.setDefaultCommand(new Drive(drivetrain, joyleft::getY, joyleft::getX, joyright::getX, drivetrain::getDriveMode));
        Command rampMoveAndSpin = CommandUtil.race(
                "AdvancedAutoShoot",
                new RampFollow(ramp, ()-> drivetrain.getAlignable(), ()-> drivetrain.getPose()),
                new AdvancedSpinningShot(shooter, lightStrip, () -> drivetrain.getPose(), () -> drivetrain.getAlignable())
        );
        joyLeftButton1.onTrue(CommandUtil.logged(new SetAlignable(drivetrain, Alignable.SPEAKER))).onFalse(CommandUtil.logged(new SetAlignable(drivetrain, null)));
        joyLeftButton3.onTrue(rampMoveAndSpin);
        joyRightButton1.onTrue(CommandUtil.logged(new SetAlignable(drivetrain, Alignable.AMP))).onFalse(CommandUtil.logged(new SetAlignable(drivetrain, null)));
        joyLeftButton2.onTrue(CommandUtil.logged(new ToggleDrivingMode(drivetrain)));
        ManualControlClimber leftClimbCmd = new ManualControlClimber(climber, () -> -controller.getLeftY()); // negative because Y "up" is negative

        climber.setDefaultCommand(leftClimbCmd);

        // Set up to shoot Speaker CLOSE - Y
        controller.y().onTrue(CommandUtil.parallel("Setup Speaker Shot (CLOSE)",
                new RampMove(ramp, () -> GameConstants.RAMP_POS_SHOOT_SPEAKER_CLOSE),
                new ShootSpeaker(shooter, drivetrain, lightStrip)));

        // Set up to shoot Speaker AWAY - X
        controller.x().onTrue(CommandUtil.parallel("Setup Speaker Shot (AWAY)",
                new RampMove(ramp, () -> GameConstants.RAMP_POS_SHOOT_SPEAKER_AWAY),
                new ShootSpeaker(shooter, drivetrain, lightStrip)));

        // Set up to shoot AMP - A
        controller.a().onTrue(CommandUtil.parallel("Setup Amp shot",
                new DeployAmp(amp, lightStrip),
                new RampMove(ramp, () -> GameConstants.RAMP_POS_SHOOT_AMP),
                new ShootAmp(shooter, lightStrip)));

        // Cancell all - B
        controller.b().onTrue(CommandUtil.logged(new CancelAllSequence(ramp, shooter, amp, lightStrip)));

        // Shoot - Right Trigger
        controller.rightTrigger(0.5).onTrue(CommandUtil.sequence("Operator Shoot",
                new TimedFeeder(feeder, lightStrip, Constants.TIMED_FEEDER_EXIT),
                new WaitCommand(GameConstants.SHOOTER_TIME_BEFORE_STOPPING),
                new StopShooter(shooter),
                new RetractAmp(amp, lightStrip),
                new RampMove(ramp, () -> GameConstants.RAMP_POS_STOW)));

        //Driver Shoot
        joyRightButton2.onTrue(CommandUtil.sequence("Driver Shoot",
                new TimedFeeder(feeder,lightStrip, Constants.TIMED_FEEDER_EXIT),
                new WaitCommand(GameConstants.SHOOTER_TIME_BEFORE_STOPPING),
                new StopShooter(shooter),
                new RetractAmp(amp, lightStrip),
                new RampMove(ramp, () -> GameConstants.RAMP_POS_STOW))
        );

        // amp up and down
        controller.povLeft().onTrue(CommandUtil.logged(new ToggleAmp(amp, lightStrip)));

        // start intaking a note
        Command lowerIntake = CommandUtil.parallel("lowerIntake",
                new SpoolIntake(intake, Constants.INTAKE_SPOOL_TIME),
                new LowerDeployer(deployer, lightStrip),
                new RampMoveAndWait(ramp, lightStrip ,() -> GameConstants.RAMP_POS_STOW));
        Command endIntake = CommandUtil.parallel("endIntake",
                new RaiseDeployer(deployer, lightStrip));
        controller.povDown().onTrue(CommandUtil.sequence("Intake a Note",
                lowerIntake, new CurrentBasedIntakeFeeder(intake, feeder, lightStrip), endIntake));

        controller.leftTrigger(.5).onTrue(new FeederBackDrive(feeder, lightStrip));

        // stop intake
        controller.povUp().onTrue(CommandUtil.parallel("stop intake",
                CommandUtil.logged(new RaiseDeployer(deployer, lightStrip)),
                CommandUtil.logged(new StopIntake(intake)),
                CommandUtil.logged(new StopFeeder(feeder))));
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

    public IntakeSubsystem getIntake() {
        return intake;
    }

    public LightStrip getLEDStrip() {
        return lightStrip;
    }
}