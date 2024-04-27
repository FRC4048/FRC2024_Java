package frc.robot.utils.loggingv2;

public interface Loggable {
    String getBasicName();
    void setParent(Loggable loggable);
}
