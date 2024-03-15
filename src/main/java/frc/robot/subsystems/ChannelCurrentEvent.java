package frc.robot.subsystems;

import java.util.Objects;
import java.util.function.DoublePredicate;

public class ChannelCurrentEvent {
    private final int channel;
    private final DoublePredicate currentThreshold;
    private final Runnable action;
    private final boolean persistent;

    public ChannelCurrentEvent(int channel, DoublePredicate currentThreshold, Runnable action, boolean persistent) {
        this.channel = channel;
        this.currentThreshold = currentThreshold;
        this.action = action;
        this.persistent = persistent;
    }

    public int getChannel() {
        return channel;
    }

    public DoublePredicate getCurrentThreshold() {
        return currentThreshold;
    }

    public Runnable getAction() {
        return action;
    }
    public boolean shouldPersist(){
        return persistent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChannelCurrentEvent that = (ChannelCurrentEvent) o;
        return channel == that.channel && persistent == that.persistent && Objects.equals(currentThreshold, that.currentThreshold) && Objects.equals(action, that.action);
    }

    @Override
    public int hashCode() {
        return Objects.hash(channel, currentThreshold, action, persistent);
    }
}
