package frc.robot.subsystems;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.motorcontrol.PWMTalonSRX;
import frc.robot.utils.ClimberState;
import frc.robot.utils.Constants;

public class Climber extends SubsystemBase {
    private final PWMTalonSRX talonSRX1;
    private final PWMTalonSRX talonSRX2;
    private final AHRS navxGyro;
    private double navxGyroValue;
    
    

    public Climber(){
        this.talonSRX1 = new PWMTalonSRX(Constants.CLIMBER_MOTOR1_ID);
        this.talonSRX2 = new PWMTalonSRX(Constants.CLIMBER_MOTOR2_ID);
        navxGyro = new AHRS();

        navxGyroValue = -1.00;
    }
    public double getNavxGyroValue() {
        return navxGyroValue;
    }
    private double getGyro() {
        return (navxGyro.getAngle() % 360)*-1; //ccw should be positive
    }
    private ClimberState currentState = ClimberState.STOPPED;

    private double ArmUnderExtend=Constants.ArmSeperationDistance*java.lang.Math.tan(navxGyroValue);
    public double BalanceSpeedFunc() {
        return 1.00;
    }

    /**
     * sets the speed of both motors to the IntakeSpeed defined in the {@link Constants} file
     * @param forward if true the motors will spin in the intake directions,
     *                if false the motors will spin in the outtake direction
     */
    public void raise(boolean upward){
        double speed = upward ? Constants.CLIMBER_SPEED : Constants.CLIMBER_SPEED*-1;
        talonSRX1.set(speed);
        talonSRX2.set(speed);
        currentState = upward ? ClimberState.NORMALUP: ClimberState.NORMALDOWN;
    }  
    public void balance() {
        double speed = BalanceSpeedFunc();
        talonSRX1.set(speed);
        talonSRX2.set(-speed);
    }
    public void stop(){
        talonSRX1.set(0);
        talonSRX2.set(0);
        currentState = ClimberState.STOPPED;
    }

    public ClimberState getState() {
        return currentState;
    }
    public void periodic() {
        navxGyroValue = getGyro(); 
        if (currentState==ClimberState.BALANCE && navxGyroValue<1 && navxGyroValue>-1) {
            currentState=ClimberState.STOPPED;
        } else if (navxGyroValue>1 || navxGyroValue<-1) {
            currentState=ClimberState.BALANCE;
        }
    } 
}
