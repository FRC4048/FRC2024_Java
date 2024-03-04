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
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.autochooser.chooser.AutoChooser;
import frc.robot.autochooser.chooser.AutoChooser2024;
import frc.robot.commands.sequences.CancelAllSequence;
import frc.robot.commands.MoveToGamepiece;
import frc.robot.commands.SetAlignable;
import frc.robot.commands.amp.DeployAmp;
import frc.robot.commands.amp.RetractAmp;
import frc.robot.commands.amp.ToggleAmp;
import frc.robot.commands.climber.DisengageRatchet;
import frc.robot.commands.climber.EngageRatchet;
import frc.robot.commands.climber.ManualControlClimber;
import frc.robot.commands.deployer.LowerDeployer;
import frc.robot.commands.deployer.RaiseDeployer;
import frc.robot.commands.drivetrain.Drive;
import frc.robot.commands.drivetrain.MoveDistance;
import frc.robot.commands.drivetrain.SetInitOdom;
import frc.robot.commands.feeder.FeederBackDrive;
import frc.robot.commands.feeder.FeederGamepieceUntilLeave;
import frc.robot.commands.feeder.StartFeeder;
import frc.robot.commands.feeder.StopFeeder;
import frc.robot.commands.intake.StartIntake;
import frc.robot.commands.intake.StopIntake;
import frc.robot.commands.pathplanning.*;
import frc.robot.commands.ramp.RampMove;
import frc.robot.commands.ramp.RampMoveAndWait;
import frc.robot.commands.ramp.ResetRamp;
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

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        setupDriveTrain();
        registerPathPlanableCommands();
        setupPathPlanning();
        autoChooser = new AutoChooser2024(intake, shooter, feeder, deployer, ramp);
        autoChooser.addOnValidationCommand(() -> CommandUtil.logged(new SetInitOdom(drivetrain, autoChooser)));
        autoChooser.forceRefresh();
        configureBindings();
        putShuffleboardCommands();
    }

    /**
     * NamedCommands
     */
    private void registerPathPlanableCommands() {
        NamedCommands.registerCommand("StartIntakeAndFeeder", CommandUtil.race("StartIntakeAndFeeder",
                new StartFeeder(feeder),
                new TimedIntake(intake, Constants.TIMED_INTAKE_AUTO_TIMEOUT))
        );
        NamedCommands.registerCommand("PathPlannerShoot", CommandUtil.logged(new PathPlannerShoot(shooter, feeder, ramp, intake)));
        NamedCommands.registerCommand("ComboShot", CommandUtil.logged(new ComboShot(shooter, feeder,ramp)));
        NamedCommands.registerCommand("FeederGamepieceUntilLeave", CommandUtil.logged(new FeederGamepieceUntilLeave(feeder)));
        NamedCommands.registerCommand("ShootAndDrop", CommandUtil.logged(new ShootAndDrop(shooter,feeder,deployer,ramp)));
        NamedCommands.registerCommand("FeederBackDrive", CommandUtil.logged(new FeederBackDrive(feeder)));
        NamedCommands.registerCommand("ResetRamp", CommandUtil.logged(new ResetRamp(ramp)));
        NamedCommands.registerCommand("RampShootComboCenter", CommandUtil.logged(new RampShootCombo(ramp,shooter, Constants.RAMP_CENTER_AUTO_SHOOT)));// second piece
        NamedCommands.registerCommand("RampShootComboSide", CommandUtil.logged(new RampShootCombo(ramp,shooter, Constants.RAMP_SIDE_AUTO_SHOOT))); // first and third
        NamedCommands.registerCommand("RampShootComboSide2", CommandUtil.logged(new RampShootCombo(ramp,shooter,Constants.RAMP_DIP_AUTO_SHOOT))); // first and third
    }

    private void setupPathPlanning() {
        AutoBuilder.configureHolonomic(drivetrain::getPose,
                drivetrain::resetOdometry,
                drivetrain::speedsFromStates,
                drivetrain::drive,
                new HolonomicPathFollowerConfig(
                        new PIDConstants(Constants.PATH_PLANNER_TRANSLATION_PID_P, Constants.PATH_PLANNER_TRANSLATION_PID_I, Constants.PATH_PLANNER_TRANSLATION_PID_D), // Translation PID constants
                        new PIDConstants(Constants.PATH_PLANNER_ROTATION_PID_P, Constants.PATH_PLANNER_ROTATION_PID_I,Constants.PATH_PLANNER_ROTATION_PID_D), // Rotation PID constants
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
        drivetrain.setDefaultCommand(new Drive(drivetrain, joyleft::getY, joyleft::getX, joyright::getX));
        Command alignSpeaker = CommandUtil.parallel(
                "Shoot&AlignSpeaker",
                new SetAlignable(drivetrain, Alignable.SPEAKER),
                new AdvancedSpinningShot(shooter, () -> drivetrain.getPose(), () -> drivetrain.getAlignable())
        );
        joyLeftButton1.onTrue(alignSpeaker).onFalse(CommandUtil.logged(new SetAlignable(drivetrain, null)));
        joyRightButton1.onTrue(CommandUtil.logged(new SetAlignable(drivetrain, Alignable.AMP))).onFalse(CommandUtil.logged(new SetAlignable(drivetrain, null)));
        ManualControlClimber leftClimbCmd = new ManualControlClimber(climber, () -> -controller.getLeftY()); // negative because Y "up" is negative

        climber.setDefaultCommand(leftClimbCmd);

        // Disengage
        controller.leftBumper().onTrue(CommandUtil.logged(new DisengageRatchet(climber)));

        // Engage
        controller.rightBumper().onTrue(CommandUtil.logged(new EngageRatchet(climber)));

        // Set up to shoot Speaker CLOSE - Y
        controller.y().onTrue(CommandUtil.parallel("Setup Speaker Shot (CLOSE)",
                new RampMove(ramp, () -> GameConstants.RAMP_POS_SHOOT_SPEAKER_CLOSE),
                new ShootSpeaker(shooter, drivetrain)));

        // Set up to shoot Speaker AWAY - X
        controller.x().onTrue(CommandUtil.parallel("Setup Speaker Shot (AWAY)",
                new RampMove(ramp, () -> GameConstants.RAMP_POS_SHOOT_SPEAKER_AWAY),
                new ShootSpeaker(shooter, drivetrain)));

        // Set up to shoot AMP - A
        controller.a().onTrue(CommandUtil.parallel("Setup Amp shot",
                new DeployAmp(amp),
                new RampMove(ramp, () -> GameConstants.RAMP_POS_SHOOT_AMP),
                new ShootAmp(shooter)));

        // Shoot the note - B
        controller.b().onTrue(CommandUtil.sequence("Operator Shoot",
                new FeederGamepieceUntilLeave(feeder,ramp),
                new WaitCommand(GameConstants.SHOOTER_TIME_BEFORE_STOPPING),
                new StopShooter(shooter),
                new RetractAmp(amp),
                new RampMove(ramp, () -> GameConstants.RAMP_POS_STOW)));

        joyRightButton2.onTrue(CommandUtil.sequence("Driver Shoot",
                new FeederGamepieceUntilLeave(feeder,ramp),
                new StopShooter(shooter),
                new RetractAmp(amp)));

        // amp up and down
        controller.povLeft().onTrue(CommandUtil.logged(new ToggleAmp(amp)));

        // start intaking a note
        Command lowerIntake = CommandUtil.parallel("lowerIntake",
                new LowerDeployer(deployer),
                new RampMoveAndWait(ramp, () -> GameConstants.RAMP_POS_STOW));
        Command startSpinning = CommandUtil.race("startSpinning",
                new StartIntake(intake, 10),
                new StartFeeder(feeder));
        Command backDrive = CommandUtil.sequence("backDrive",
                new WaitCommand(GameConstants.FEEDER_WAIT_TIME_BEFORE_BACKDRIVE),
                new FeederBackDrive(feeder));
        Command endIntake = CommandUtil.parallel("endIntake",
                new RaiseDeployer(deployer),
                backDrive);
        controller.povDown().onTrue(CommandUtil.sequence("Intake a Note",
                lowerIntake, startSpinning, endIntake));

        controller.leftTrigger(.5).onTrue(new FeederBackDrive(feeder));

        // stop intake
        controller.povUp().onTrue(CommandUtil.parallel("stop intake",
                CommandUtil.logged(new RaiseDeployer(deployer)),
                CommandUtil.logged(new StopIntake(intake)),
                CommandUtil.logged(new StopFeeder(feeder))));

        controller.povRight().onTrue(CommandUtil.sequence("Cancel All",new CancelAllSequence(ramp, shooter,amp)));
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
}