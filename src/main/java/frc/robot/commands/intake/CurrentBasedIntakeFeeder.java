package frc.robot.commands.intake;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.IntakeSubsystem;

public class CurrentBasedIntakeFeeder extends Command {
    private final Feeder feeder;
    private final IntakeSubsystem intake;
    private final Timer timer = new Timer();
    private boolean slowState = false;
    private double intakeSpikeCount = 0;

    public CurrentBasedIntakeFeeder(IntakeSubsystem intake, Feeder feeder) {
        this.intake = intake;
        this.feeder = feeder;
        addRequirements(intake, feeder);
    }

    @Override
    public void initialize() {
        timer.reset();
        timer.start();
    }

    @Override
    public void execute() {
        intake.setMotorSpeed(Constants.INTAKE_MOTOR_1_SPEED, Constants.INTAKE_MOTOR_2_SPEED);
        if(intake.getMotor1StatorCurrent() > Constants.INTAKE_PIECE_THESHOLD){
            intakeSpikeCount++;
        }
        slowState = (intakeSpikeCount > 2);
        double speed = slowState ? (Constants.RELY_COLOR_SENSOR ? Constants.FEEDER_SUPER_LOW_FEEDER_SPEED :Constants.LOW_FEEDER_SPEED) : Constants.FEEDER_MOTOR_ENTER_SPEED;
        feeder.setFeederMotorSpeed(speed);
    }

    @Override
    public void end(boolean interrupted) {
        intake.stopMotors();
        feeder.stopFeederMotor();
        intakeSpikeCount = 0;
        slowState = false;
        timer.stop();
    }

    @Override
    public boolean isFinished() {
        return timer.hasElapsed(10) || feeder.pieceSeen(true);
    }
}
