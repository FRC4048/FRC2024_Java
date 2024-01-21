package frc.robot.commands.Intake;

import frc.robot.Constants;
import frc.robot.subsystems.IntakeSubsystem;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

public class IntakeRingCommand extends Command {
    private IntakeSubsystem intakeSubsystem;
    private double initTime;
    private int ringDetections;
    private int timeOut = 20;

    public IntakeRingCommand(IntakeSubsystem intakeSubsystem, int timeOut) {
        addRequirements(intakeSubsystem);
        this.intakeSubsystem = intakeSubsystem;
        this.timeOut = timeOut;
    }

    @Override
    public void initialize() {
        initTime = Timer.getFPGATimestamp();
    }
  
    @Override
    public void execute() {
        intakeSubsystem.spinMotor(0.3);
        if (intakeSubsystem.isRingInIntake()) {
            ringDetections++;
        } else {
            ringDetections = 0;
        }
        SmartDashboard.putNumber("Ring Detections", ringDetections);
    }

    @Override
    public void end(boolean interrupted) {
        intakeSubsystem.spinMotor(0);
    }

    @Override
    public boolean isFinished() {
        return((ringDetections>=3) || (Timer.getFPGATimestamp() - initTime) >= timeOut);
    }
}