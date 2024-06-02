package frc.robot.subsystems.swervev3.vision;

import edu.wpi.first.math.Vector;
import edu.wpi.first.math.numbers.N3;

public class PoseDivation {
    private final Vector<N3> wheelStd;
    private final Vector<N3> visionStd;

    public PoseDivation(Vector<N3> wheelStd, Vector<N3> visionStd) {
        this.wheelStd = wheelStd;
        this.visionStd = visionStd;
    }

    public Vector<N3> getWheelStd() {
        return wheelStd;
    }

    public Vector<N3> getVisionStd() {
        return visionStd;
    }
}
