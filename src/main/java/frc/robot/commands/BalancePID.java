package frc.robot.commands;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.utils.Constants;
import frc.robot.subsystems.Climber;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.utils.SimponsMethod;
//import frc.robot.utils.Logger;
//import frc.robot.utils.SmartShuffleboard;
//import frc.robot.utils.logging.wrappers.LoggedCommand;

public class BalancePID extends Command{
    private Climber climber;
    private int counter;
    private double startTime;

    public BalancePID(Climber climber) {
        this.climber = climber;
        addRequirements(climber);
    }
    @Override
    public void initialize() {
        super.initialize();
        counter=0;
        startTime=Timer.getFPGATimestamp();
    }
    @Override
    public void execute() {
        double pitch = climber.getNavxGyroValue();
        double dir = Math.signum(pitch);
        /*double speed = Math.abs(pitch) > Constants.BALANCE_THRESH ? 
         MathUtil.clamp(Constants.BALANCE_kP*(climber.getArmUnderExtend()+(SimponsMethod.integrate(0.00,Timer.getFPGATimestamp()))/Constants.BALANCE_kTi+Constants.Balance_KTd*climber.getinstatenousDeravative()), Constants.BALANCE_LOW_SPEED, Constants.BALANCE_HIGH_SPEED) : 0;*/
        double speed = Math.abs(pitch) > Constants.BALANCE_THRESH ? dir*Constants.BALANCE_LOW_SPEED : 0;
        climber.balance(speed);
    }
    
}
