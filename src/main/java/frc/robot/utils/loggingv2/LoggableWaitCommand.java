package frc.robot.utils.loggingv2;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class LoggableWaitCommand extends WaitCommand implements Loggable {
    private String basicName = getClass().getSimpleName();
    private Command parent = new BlankCommand();


    public LoggableWaitCommand(double seconds) {
        super(seconds);
    }

    @Override
    public String getBasicName() {
        return basicName;
    }

    @Override
    public String getName() {
        if (parent == null) {
            parent = new BlankCommand();
            DriverStation.reportWarning("Wait Command parrent is null",false);
        }
        return parent.getName() +"/"+ getBasicName();
    }

    @Override
    public void setParent(Command loggable) {
        if (loggable == null){
            DriverStation.reportError("Parent can not be null", true);
        }else{
            DriverStation.reportWarning("Parent Name" + loggable.getName(), false);
        }
        parent = loggable;
    }

    public LoggableWaitCommand withBasicName(String name){
        this.basicName = name;
        return this;
    }
}
