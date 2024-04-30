package frc.robot.utils.loggingv2;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;


//cant use extends WaitCommand because if nameChange SendableRegistry
public class LoggableWaitCommand extends Command implements Loggable {
    private final Timer timer;
    private final double seconds;
    private String basicName = getClass().getSimpleName();
    private Command parent = new BlankCommand();


    public LoggableWaitCommand(double seconds) {
        this.seconds = seconds;
        timer = new Timer();
    }

    @Override
    public String getBasicName() {
        return basicName;
    }

    @Override
    public String getName() {
        String prefix = parent.getName();
        if (!prefix.isBlank()){
            prefix += "/";
        }
        return prefix + getBasicName();
    }

    @Override
    public void setParent(Command loggable) {
        this.parent = loggable == null ? new BlankCommand() : loggable;
    }

    public LoggableWaitCommand withBasicName(String name){
        this.basicName = name;
        return this;
    }
    @Override
    public void initialize() {
        timer.restart();
    }

    @Override
    public void end(boolean interrupted) {
        timer.stop();
    }

    @Override
    public boolean isFinished() {
        return timer.hasElapsed(seconds);
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }

}
