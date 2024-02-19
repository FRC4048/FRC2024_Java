package frc.robot.utils.diag;

package frc.robot.utils.diag;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DigitalInput;

/**
 * Diagnostic class for the Optical sensor; it is a DiagBoolean object.
 */
public class DiagGetGamePieceDetection extends DiagBoolean {

    private NetworkTableEntry probEntry;

    /**
     * Constructor
     * 
     * @param name            - The sensor's name, which will be shown on Shuffleboard
     * @param digitalInput    - The DigitalInput pin the sensor is connected to
     */
    public DiagGetGamePieceDetection(String title, String name, NetworkTableEntry probEntry) {
        super(title, name);
        this.probEntry = probEntry;
    }

    @Override
    protected boolean getValue() {
        return probEntry.getDouble(0) != 0;
    }
} 