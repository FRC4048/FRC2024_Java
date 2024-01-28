// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.Autos;
import frc.robot.commands.ExampleCommand;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.swervev2.KinematicsConversionConfig;
import frc.robot.subsystems.swervev2.SwerveDrivetrain;
import frc.robot.subsystems.swervev2.SwerveIdConfig;
import frc.robot.subsystems.swervev2.SwervePidConfig;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.commands.StaticClimb;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  /*private Joystick joyleft = new Joystick(Constants.LEFT_JOYSICK_ID);
  private Joystick joyright = new Joystick(Constants.RIGHT_JOYSTICK_ID);*/
  private CommandXboxController controller = new CommandXboxController(Constants.CONTROLLER_ID);
  private final SwerveDrivetrain drivetrain;
  private Climber climber = null;
  // The robot's subsystems and commands are defined here...
  private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();

  // Replace with CommandPS4Controller or CommandJoystick if needed
  /*private final CommandXboxController m_driverController =
      new CommandXboxController(OperatorConstants.kDriverControllerPort);*/

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {

    SwerveIdConfig frontLeftIdConf = new SwerveIdConfig(Constants.DRIVE_FRONT_LEFT_D, Constants.DRIVE_FRONT_LEFT_S, Constants.DRIVE_CANCODER_FRONT_LEFT);
    SwerveIdConfig frontRightIdConf = new SwerveIdConfig(Constants.DRIVE_FRONT_RIGHT_D, Constants.DRIVE_FRONT_RIGHT_S, Constants.DRIVE_CANCODER_FRONT_RIGHT);
    SwerveIdConfig backLeftIdConf = new SwerveIdConfig(Constants.DRIVE_BACK_LEFT_D, Constants.DRIVE_BACK_LEFT_S, Constants.DRIVE_CANCODER_BACK_LEFT);
    SwerveIdConfig backRightIdConf = new SwerveIdConfig(Constants.DRIVE_BACK_RIGHT_D, Constants.DRIVE_BACK_RIGHT_S, Constants.DRIVE_CANCODER_BACK_RIGHT);

    TrapezoidProfile.Constraints constraints = new TrapezoidProfile.Constraints(Constants.MAX_ANGULAR_SPEED * 4, 2 * Math.PI * 10);
    PID drivePid = PID.of(Constants.DRIVE_PID_P,Constants.DRIVE_PID_I,Constants.DRIVE_PID_D);
    PID steerPid = PID.of(Constants.STEER_PID_P,Constants.STEER_PID_I,Constants.STEER_PID_D);
    Gain driveGain = Gain.of(Constants.DRIVE_PID_FF_V,Constants.DRIVE_PID_FF_S);
    Gain steerGain = Gain.of(Constants.STEER_PID_FF_V,Constants.STEER_PID_FF_S);

    KinematicsConversionConfig kinematicsConversionConfig = new KinematicsConversionConfig(Constants.WHEEL_RADIUS, Constants.CHASSIS_DRIVE_GEAR_RATIO, Constants.CHASSIS_STEER_GEAR_RATIO);
    SwervePidConfig pidConfig = new SwervePidConfig(drivePid,steerPid,driveGain,steerGain,constraints);
    AHRS navxGyro = new AHRS();
    navxGyro.setAngleAdjustment(0);
    this.drivetrain = new SwerveDrivetrain(frontLeftIdConf, frontRightIdConf, backLeftIdConf, backRightIdConf, kinematicsConversionConfig, pidConfig, navxGyro);
    drivetrain.resetOdometry(new Pose2d(0,0,new Rotation2d(Math.toRadians(0))));

    climber = new Climber(navxGyro);
    
    // Configure the trigger bindings
    configureBindings();
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    //drivetrain.setDefaultCommand(new Drive(drivetrain, ()-> joyleft.getY(), ()-> joyleft.getX(), ()-> joyright.getX()));
    //controller.button(XboxController.Button.kA.value).onTrue(new StaticClimb(climber).andThen(new BalancePID(climber)).repeatedly());
    controller.button(XboxController.Button.kB.value).onTrue(new StaticClimb(climber));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return Autos.exampleAuto(m_exampleSubsystem);
  }

  public SwerveDrivetrain getDrivetrain() {
    return drivetrain;
  }
}