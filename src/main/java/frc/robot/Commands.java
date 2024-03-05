package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.autochooser.chooser.AutoChooser;
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
import frc.robot.commands.drivetrain.SetInitOdom;
import frc.robot.commands.feeder.FeederBackDrive;
import frc.robot.commands.feeder.FeederGamepieceUntilLeave;
import frc.robot.commands.feeder.StartFeeder;
import frc.robot.commands.feeder.StopFeeder;
import frc.robot.commands.intake.StartIntake;
import frc.robot.commands.intake.StopIntake;
import frc.robot.commands.pathplanning.*;
import frc.robot.commands.ramp.RampFollow;
import frc.robot.commands.ramp.RampMove;
import frc.robot.commands.ramp.RampMoveAndWait;
import frc.robot.commands.ramp.ResetRamp;
import frc.robot.commands.sequences.CancelAllSequence;
import frc.robot.commands.shooter.AdvancedSpinningShot;
import frc.robot.commands.shooter.ShootAmp;
import frc.robot.commands.shooter.ShootSpeaker;
import frc.robot.commands.shooter.StopShooter;
import frc.robot.constants.Constants;
import frc.robot.constants.GameConstants;
import frc.robot.subsystems.*;
import frc.robot.utils.Alignable;
import frc.robot.utils.logging.CommandUtil;

public class Commands {
    private final Command drive;
    private final Command alignSpeaker;
    private final Command clearAlignable;
    private final Command setAlignableAmp;
    private final Command manualClimb;
    private final Command disengageRatchet;
    private final Command setInitalOdom;
    private final Command engateRatchet;
    private final Command shootSpeakerClose;
    private final Command shootSpeakerFar;
    private final Command setupAmpShoot;
    private final Command calnsellAll;
    private final Command operatorShoot;
    private final Command driverShoot;
    private final Command toggleAmp;
    private final Command lowerIntake;
    private final Command startSpinning;
    private final Command backDrive;
    private final Command endIntake;
    private final Command intakeNote;
    private final Command feederBackDrive;
    private final Command stopIntake;
    private final Command advancedShot;
    private final Command pathplannerShoot;
    private final Command slurpWithRamp;
    private final Command comboShot;
    private final Command feederUntilLeave;
    private final Command shootAndDrop;
    private final Command resetRamp;
    private final Command rampComboShootSide;
    private final Command rampComboShootSide2;
    private final Command rampComboShootCenter;

    public Commands(SwerveDrivetrain drivetrain, IntakeSubsystem intake, Ramp ramp, Feeder feeder, Deployer deployer, Climber climber,Shooter shooter ,Amp amp,Joystick joyLeft, Joystick joyRight, CommandXboxController controller, AutoChooser autoChooser) {
        this.drive = CommandUtil.logged(new Drive(drivetrain, joyLeft::getY, joyLeft::getX, joyRight::getX));
        this.clearAlignable = CommandUtil.logged("Clear Alignable",new SetAlignable(drivetrain, null));
        this.setAlignableAmp = CommandUtil.logged("Set Alignable Amp",new SetAlignable(drivetrain, Alignable.AMP));
        this.manualClimb = CommandUtil.logged("Manual Climb",new ManualControlClimber(climber, () -> -controller.getLeftY()));
        this.disengageRatchet = CommandUtil.logged("Manual Climb",CommandUtil.logged(new DisengageRatchet(climber)));
        this.setInitalOdom = CommandUtil.logged(new SetInitOdom(drivetrain, autoChooser));
        this.engateRatchet = CommandUtil.logged(new EngageRatchet(climber));
        this.calnsellAll = CommandUtil.logged(new CancelAllSequence(ramp, shooter, amp));
        this.toggleAmp = CommandUtil.logged(new ToggleAmp(amp));
        this.feederBackDrive = CommandUtil.logged("Manual Feeder Back Drive",new FeederBackDrive(feeder));
        this.pathplannerShoot = CommandUtil.logged(new PathPlannerShoot(shooter, feeder, ramp, intake));
        this.comboShot = CommandUtil.logged(new ComboShot(shooter, feeder, ramp));
        this.feederUntilLeave = CommandUtil.logged(new FeederGamepieceUntilLeave(feeder, ramp));
        this.shootAndDrop = CommandUtil.logged(new ShootAndDrop(shooter, feeder, deployer, ramp));
        this.resetRamp = CommandUtil.logged(new ResetRamp(ramp));
        this.rampComboShootSide = CommandUtil.logged(new RampShootCombo(ramp, shooter, Constants.RAMP_SIDE_AUTO_SHOOT));
        this.rampComboShootSide2 = CommandUtil.logged(new RampShootCombo(ramp, shooter, Constants.RAMP_DIP_AUTO_SHOOT));
        this.rampComboShootCenter = CommandUtil.logged(new RampShootCombo(ramp, shooter, Constants.RAMP_CENTER_AUTO_SHOOT));
        this.alignSpeaker = CommandUtil.sequence(
                "Shoot&AlignSpeaker",
                new SetAlignable(drivetrain, Alignable.SPEAKER),
                new RampFollow(ramp, drivetrain::getAlignable, drivetrain::getPose)
        );
        this.shootSpeakerClose = CommandUtil.parallel("Setup Speaker Shot (CLOSE)",
                new RampMove(ramp, () -> GameConstants.RAMP_POS_SHOOT_SPEAKER_CLOSE),
                new ShootSpeaker(shooter, drivetrain)
        );
        this.shootSpeakerFar = CommandUtil.parallel("Setup Speaker Shot (AWAY)",
                new RampMove(ramp, () -> GameConstants.RAMP_POS_SHOOT_SPEAKER_AWAY),
                new ShootSpeaker(shooter, drivetrain)
        );
        this.setupAmpShoot = CommandUtil.parallel("Setup Amp shot",
                new DeployAmp(amp),
                new RampMove(ramp, () -> GameConstants.RAMP_POS_SHOOT_AMP),
                new ShootAmp(shooter));
        this.operatorShoot = CommandUtil.sequence("Operator Shoot",
                new FeederGamepieceUntilLeave(feeder, ramp),
                new WaitCommand(GameConstants.SHOOTER_TIME_BEFORE_STOPPING),
                new StopShooter(shooter),
                new RetractAmp(amp),
                new RampMove(ramp, () -> GameConstants.RAMP_POS_STOW)
        );
        this.driverShoot = CommandUtil.sequence("Driver Shoot",
                new FeederGamepieceUntilLeave(feeder, ramp),
                new StopShooter(shooter),
                new RetractAmp(amp)
        );
        this.lowerIntake = CommandUtil.parallel("lowerIntake",
                new LowerDeployer(deployer),
                new RampMoveAndWait(ramp, () -> GameConstants.RAMP_POS_STOW)
        );
        this.startSpinning = CommandUtil.race("startSpinning",
                new StartIntake(intake, 10),
                new StartFeeder(feeder)
        );
        this.backDrive = CommandUtil.sequence("backDrive",
                new WaitCommand(GameConstants.FEEDER_WAIT_TIME_BEFORE_BACKDRIVE),
                new FeederBackDrive(feeder)
        );
        this.endIntake = CommandUtil.parallel("endIntake",
                new RaiseDeployer(deployer), backDrive
        );
        this.intakeNote = CommandUtil.sequence("Intake a Note",
                lowerIntake, startSpinning, endIntake
        );
        this.stopIntake = CommandUtil.parallel("stop intake",
                CommandUtil.logged(new RaiseDeployer(deployer)),
                CommandUtil.logged(new StopIntake(intake)),
                CommandUtil.logged(new StopFeeder(feeder))
        );
        this.advancedShot = new ParallelDeadlineGroup(
                new SequentialCommandGroup(
                        new WaitCommand(0.5),
                        new FeederGamepieceUntilLeave(feeder, ramp),
                        new WaitCommand(GameConstants.SHOOTER_TIME_BEFORE_STOPPING),
                        new RampMove(ramp, () -> GameConstants.RAMP_POS_STOW)
                ),
                new AdvancedSpinningShot(shooter, drivetrain::getPose, drivetrain::getAlignable)
        );
        this.slurpWithRamp = new ParallelDeadlineGroup(
                new StartFeeder(feeder),
                new TimedIntake(intake, Constants.TIMED_INTAKE_AUTO_TIMEOUT),
                new ResetRamp(ramp)
        );
    }

    public Command getDrive() {
        return drive;
    }

    public Command getAlignSpeaker() {
        return alignSpeaker;
    }

    public Command getClearAlignable() {
        return clearAlignable;
    }

    public Command getSetAlignableAmp() {
        return setAlignableAmp;
    }

    public Command getManualClimb() {
        return manualClimb;
    }

    public Command getDisengageRatchet() {
        return disengageRatchet;
    }

    public Command getSetInitalOdom() {
        return setInitalOdom;
    }

    public Command getEngateRatchet() {
        return engateRatchet;
    }

    public Command getShootSpeakerClose() {
        return shootSpeakerClose;
    }

    public Command getShootSpeakerFar() {
        return shootSpeakerFar;
    }

    public Command getSetupAmpShoot() {
        return setupAmpShoot;
    }

    public Command getCalnsellAll() {
        return calnsellAll;
    }

    public Command getOperatorShoot() {
        return operatorShoot;
    }

    public Command getDriverShoot() {
        return driverShoot;
    }

    public Command getToggleAmp() {
        return toggleAmp;
    }

    public Command getLowerIntake() {
        return lowerIntake;
    }

    public Command getStartSpinning() {
        return startSpinning;
    }

    public Command getBackDrive() {
        return backDrive;
    }

    public Command getEndIntake() {
        return endIntake;
    }

    public Command getIntakeNote() {
        return intakeNote;
    }

    public Command getFeederBackDrive() {
        return feederBackDrive;
    }

    public Command getStopIntake() {
        return stopIntake;
    }

    public Command getAdvancedShot() {
        return advancedShot;
    }

    public Command getPathplannerShoot() {
        return pathplannerShoot;
    }

    public Command getSlurpWithRamp() {
        return slurpWithRamp;
    }

    public Command getComboShot() {
        return comboShot;
    }

    public Command getFeederUntilLeave() {
        return feederUntilLeave;
    }

    public Command getShootAndDrop() {
        return shootAndDrop;
    }

    public Command getResetRamp() {
        return resetRamp;
    }

    public Command getRampComboShootSide() {
        return rampComboShootSide;
    }

    public Command getRampComboShootSide2() {
        return rampComboShootSide2;
    }

    public Command getRampComboShootCenter() {
        return rampComboShootCenter;
    }
}
