package frc.robot.subsystems;

import java.util.Objects;

public class ChannelCurrentEvent {
    private final int channel;
    private final double currentThreshold;
    private final Runnable action;

    public ChannelCurrentEvent(int channel, double currentThreshold, Runnable action) {
        this.channel = channel;
        this.currentThreshold = currentThreshold;
        this.action = action;
    }

    public int getChannel() {
        return channel;
    }

    public double getCurrentThreshold() {
        return currentThreshold;
    }

    public Runnable getAction() {
        return action;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChannelCurrentEvent that = (ChannelCurrentEvent) o;
        return channel == that.channel && currentThreshold == that.currentThreshold && Objects.equals(action, that.action);
    }

    @Override
    public int hashCode() {
        return Objects.hash(channel, currentThreshold, action);
    }
}
