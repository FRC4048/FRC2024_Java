package frc.robot.utils.command;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleConsumer;

/**
 * Fun class for quick testing
 * example way to do the same functionality as startFeeder <br>
 * <b>new MotorMoveCommand<>(feeder, 0, feeder::setFeederMotorSpeed, 0.5, feeder::pieceSeen)</b>
 *
 */
public class TimedMotorMoveCommand<T extends SubsystemBase> extends TimedSubsystemCommand<T>{
    private final DoubleConsumer setMotorSpeed;
    private final double speed;
    private final BooleanSupplier finishedCondition;
    public TimedMotorMoveCommand(T subsystem, double timeout, DoubleConsumer setMotorSpeed, double speed, BooleanSupplier finishedCondition) {
        super(subsystem, timeout);
        this.setMotorSpeed = setMotorSpeed;
        this.speed = speed;
        this.finishedCondition = finishedCondition;
    }

    @Override
    public void execute() {
        setMotorSpeed.accept(speed);
    }

    @Override
    public void end(boolean interrupted) {
        setMotorSpeed.accept(0);
    }

    @Override
    public boolean isFinished() {
        return finishedCondition.getAsBoolean() || super.isFinished();
    }
}
