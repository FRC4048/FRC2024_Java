package frc.robot.subsystems;
import edu.wpi.first.wpilibj.motorcontrol.PWMTalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.utils.OuttakeState; 

public class Outtake extends SubsystemBase {
    private final PWMTalonSRX talonSRX1;
    private final PWMTalonSRX talonSRX2;
    private OuttakeState currentState = OuttakeState.STOPPED;

    public Outtake(){
        this.talonSRX1 = new PWMTalonSRX(Constants.OUTTAKE_MOTOR1_ID);
        this.talonSRX2 = new PWMTalonSRX(Constants.OUTTAKE_MOTOR2_ID);
    }

    /**
     * sets the speed of both motors to the OuttakeSpeed defined in the {@link Constants} file
     * @param forward if true the motors will spin in the outtake directions,
     *                if false the motors will spin in the outtake direction
     */
    public void spin(boolean forward){
        double speed = forward ? Constants.OUTTAKE_SPEED : Constants.OUTTAKE_SPEED*-1;
        talonSRX1.set(speed);
        talonSRX2.set(-speed);
        currentState = forward ? OuttakeState.FORWARD: OuttakeState.BACKWARD;
    }
    public void stop(){
        talonSRX1.set(0);
        talonSRX2.set(0);
        currentState = OuttakeState.STOPPED;
    }

    public OuttakeState getState() {
        return currentState;
    }
}
