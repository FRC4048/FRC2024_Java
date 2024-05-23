package frc.robot.subsystems.apriltags;

import java.io.DataInputStream;
import java.io.IOException;

public class TCPAriltagServer extends TCPServer<ApriltagReading> {

    public TCPAriltagServer(int port) {
        super(port);
    }

    @Override
    protected ApriltagReading extractFromStream(DataInputStream stream) throws IOException {
        double posX = stream.readDouble();
        double posY = stream.readDouble();
        double rotationDeg = stream.readDouble();
        double timestamp = stream.readDouble();
        double now = System.currentTimeMillis();
        return new ApriltagReading(posX, posY, rotationDeg, timestamp, now);
    }
}
