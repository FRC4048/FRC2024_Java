package frc.robot.utils;

import edu.wpi.first.networktables.*;
import frc.robot.constants.Constants;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public abstract class Debugable<T> {
    private static final NetworkTableInstance instance = NetworkTableInstance.getDefault();
    private static final ConcurrentHashMap<Topic, List<Consumer<?>>> callbacks = new ConcurrentHashMap<>();
    private final NetworkTableEntry entry;

    public Debugable(String tab, String fieldName, T defaultValue) {
        NetworkTable table = instance.getTable(tab);
        this.setCurrentValue(defaultValue);
        this.setLastValue(defaultValue);
        this.entry = table.getEntry(fieldName);
        this.entry.setDefaultValue(defaultValue);
        if (Constants.ENABLE_DEVELOPMENT) {
            instance.addListener(entry, EnumSet.of(NetworkTableEvent.Kind.kPublish), this::sendUpdates);
        }
    }
    public void addListener(Consumer<T> consumer) {
        List<Consumer<?>> list = callbacks.getOrDefault(entry.getTopic(), new ArrayList<>());
        list.add(consumer);
        callbacks.put(entry.getTopic(), list);
    }

    protected List<Consumer<?>> getCallback(Topic topic){
        return callbacks.get(topic);
    }
    protected List<Consumer<?>> addCallback(Topic topic){
        return callbacks.get(topic);
    }

    protected abstract void sendUpdates(NetworkTableEvent event);

    protected abstract void setLastValue(T defaultValue);

    protected abstract void setCurrentValue(T defaultValue);
    public abstract void getCurrentValue();
}
