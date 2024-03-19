package frc.robot.subsystems;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.DoublePredicate;

public class ChannelCurrentEvent {
    private final int channel;
    private final DoublePredicate currentThreshold;
    private final BiFunction<Integer, Double, Boolean> callback;

    public ChannelCurrentEvent(int channel, DoublePredicate currentThreshold, BiFunction<Integer, Double, Boolean> callback) {
        this.channel = channel;
        this.currentThreshold = currentThreshold;
        this.callback = callback;
    }

    public int getChannel() {
        return channel;
    }

    public DoublePredicate getCurrentThreshold() {
        return currentThreshold;
    }

    public BiFunction<Integer, Double, Boolean> getCallback() {
        return callback;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChannelCurrentEvent that = (ChannelCurrentEvent) o;
        return channel == that.channel && Objects.equals(currentThreshold, that.currentThreshold) && Objects.equals(callback, that.callback);
    }

    @Override
    public int hashCode() {
        return Objects.hash(channel, currentThreshold, callback);
    }
}
