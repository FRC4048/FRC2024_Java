package frc.robot.commands.deployer;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Deployer;
import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.Timer;

public class DeployerLower extends Command {
    
    private Deployer deployer;
    private Timer timer = new Timer();
    private final double MOTOR_RUN_TIME = Constants.DEPLOYER_LOWER_TIMEOUT;

    public DeployerLower(Deployer deployer) {
        this.deployer = deployer;
        addRequirements(deployer);
    }

    @Override
    public void initialize() {
        //Reset and start timers
        deployer.setDeployerMotorSpeed(Constants.DEPLOYER_LOWER_SPEED);
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
