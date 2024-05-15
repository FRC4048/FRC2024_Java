package frc.robot.subsystems.apriltags;

import edu.wpi.first.networktables.*;

public class NtApriltag implements ApriltagIO {
    private final DoubleArraySubscriber visionMeasurementSubscriber;
    private final IntegerArraySubscriber apriltagIdSubscriber;

    public NtApriltag() {
        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        NetworkTable table = inst.getTable("ROS");
        visionMeasurementSubscriber = table.getDoubleArrayTopic("Pos").subscribe(new double[]{-1,-1,-1,-1}, PubSubOption.pollStorage(100), PubSubOption.sendAll(true));
        apriltagIdSubscriber = table.getIntegerArrayTopic("apriltag_id").subscribe(new long[]{-1,-1});
    }

    @Override
    public void updateInputs(ApriltagInputs inputs) {
        long[] tagData = apriltagIdSubscriber.get();
        TimestampedDoubleArray[] queue = visionMeasurementSubscriber.readQueue();
        inputs.posX = new double[queue.length];
        inputs.posY = new double[queue.length];
        inputs.rotationDeg = new double[queue.length];
        inputs.serverTime = new double[queue.length];
        inputs.timestamp = new double[queue.length];
        for (int i = 0; i < queue.length; i++) {
            TimestampedDoubleArray measurement = queue[i];
            if (queue.length < 4 || tagData.length < 1) continue;
            inputs.apriltagNumber[i] = (int) tagData[0];
            inputs.posX[i] = measurement.value[0];
            inputs.posY[i] = measurement.value[1];
            inputs.rotationDeg[i] = measurement.value[2];
            inputs.timestamp[i] = measurement.value[3];
            inputs.serverTime[i] = measurement.serverTime;
        }
    }

}
