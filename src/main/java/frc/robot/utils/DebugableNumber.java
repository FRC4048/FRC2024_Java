package frc.robot.utils;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableEvent;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;

import java.util.*;
import java.util.function.Consumer;

public abstract class DebugableNumber<T extends Number> {
    private static final Map<DebugableNumber<?>, List<Consumer<NetworkTableEvent>>> callbacks = new HashMap<>();
    private static final NetworkTableInstance instance = NetworkTableInstance.getDefault();
    protected T value;

    public DebugableNumber(String tag, String fieldName, T defaultValue) {
        this.value = defaultValue;
        NetworkTableEntry networkTableEntry = new NetworkTableEntry(instance, SmartShuffleboard.put(tag, fieldName, defaultValue).getEntry().getHandle());
        instance.addListener(networkTableEntry, EnumSet.of(NetworkTableEvent.Kind.kPublish), networkTableEvent -> callbacks.get(this).forEach(c -> c.accept(networkTableEvent)));
    }

    public void onChange(Consumer<NetworkTableEvent> consumer){
        List<Consumer<NetworkTableEvent>> list = callbacks.getOrDefault(this, new ArrayList<>());
        list.add(consumer);
        callbacks.put(this, list);
    }
    public T getValue(){
        return value;
    }
}
