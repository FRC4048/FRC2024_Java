package frc.robot.commands.intake;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.constants.Constants;
import frc.robot.subsystems.feeder.Feeder;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.lightstrip.LightStrip;
import frc.robot.utils.BlinkinPattern;
import frc.robot.utils.loggingv2.LoggableCommand;

public class CurrentBasedIntakeFeeder extends LoggableCommand {
    private final Feeder feeder;
    private final LightStrip lightStrip;
    private final Intake intake;
    private final Timer timer = new Timer();
    private boolean slowState = false;
    private double intakeSpikeCount = 0;

    public CurrentBasedIntakeFeeder(Intake intake, Feeder feeder, LightStrip lightStrip) {
        this.intake = intake;
        this.feeder = feeder;
        this.lightStrip = lightStrip;
        addRequirements(intake, feeder);
    }

    @Override
    public void initialize() {
        feeder.switchFeederBeamState(true);
        lightStrip.setPattern(BlinkinPattern.ORANGE);
        timer.reset();
        timer.start();
    }

    @Override
    public void execute() {
        intake.setMotorSpeed(Constants.INTAKE_MOTOR_1_SPEED, Constants.INTAKE_MOTOR_2_SPEED);
        if(intake.getMotor1StatorCurrent() > Constants.INTAKE_PIECE_THESHOLD){
            intakeSpikeCount++;
        }
        slowState = (intakeSpikeCount > Constants.INTAKE_SPIKE_THESHOLD);
        double speed = slowState ? Constants.FEEDER_SLOW_SPEED : Constants.FEEDER_MOTOR_ENTER_SPEED;
        feeder.setFeederMotorSpeed(speed);
    }

    @Override
    public void end(boolean interrupted) {
        intake.stopMotors();
        feeder.stopFeederMotor();
        intakeSpikeCount = 0;
        slowState = false;
        timer.stop();
        if (feeder.pieceSeen(true)){
            lightStrip.setPattern(BlinkinPattern.GREEN);
        }
    }

    @Override
    public boolean isFinished() {
        return timer.hasElapsed(Constants.CURRENT_INTAKE_TIMEOUT) || feeder.pieceSeen(true);
    }
}
