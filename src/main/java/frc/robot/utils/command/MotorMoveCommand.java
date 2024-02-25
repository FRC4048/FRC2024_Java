package frc.robot.utils.command;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import java.util.function.DoubleConsumer;

public class MotorMoveCommand<T extends SubsystemBase> extends TimedSubsystemCommand<T>{
    private final DoubleConsumer setMotorSpeed;
    private final double speed;
    public MotorMoveCommand(T subsystem, double timeout, DoubleConsumer setMotorSpeed, double speed) {
        super(subsystem, timeout);
        this.setMotorSpeed = setMotorSpeed;
        this.speed = speed;
    }

    @Override
    public void execute() {
        setMotorSpeed.accept(speed);
    }

    @Override
    public void end(boolean interrupted) {
        setMotorSpeed.accept(0);
    }
}
