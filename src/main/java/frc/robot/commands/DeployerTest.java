package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.Deployer;
import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.Timer;

public class DeployerTest extends Command {
    
    private Deployer deployer;
    private Timer timer = new Timer();
    private final double MOTOR_RUN_TIME = 10;

    public DeployerTest(Deployer deployer) {
        this.deployer = deployer;
        addRequirements(deployer);
    }

    @Override
    public void initialize() {
        //Reset and start timers
        deployer.setDeployerMotorSpeed(50);
        timer.reset();
        timer.start();
    }

    @Override 
    public boolean isFinished() {
        //Check is timer has passed 2 seconds
        if (timer.advanceIfElapsed(MOTOR_RUN_TIME)) {
            deployer.setDeployerMotorSpeed(0);
            return true;
        }

        else {
            return false;
        }
    }

}
