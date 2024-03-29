package frc.robot.utils;

import edu.wpi.first.networktables.*;
import frc.robot.constants.Constants;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public abstract class DebugableNumber<T extends Number> {
    private static final NetworkTableInstance instance = NetworkTableInstance.getDefault();
    private static final ConcurrentHashMap<Topic, List<Consumer<? extends Number>>> callbacks = new ConcurrentHashMap<>();
    private final AtomicReference<T> value = new AtomicReference<>();
    private final AtomicReference<T> lastValue = new AtomicReference<>(null);
    private final NetworkTableEntry entry;
    private final Class<T> classZ;

    public DebugableNumber(String tab, String fieldName, T defaultValue, Class<T> classZ) {
        NetworkTable table = instance.getTable(tab);
        this.value.set(defaultValue);
        this.lastValue.set(defaultValue);
        this.entry = table.getEntry(fieldName);
        this.entry.setDefaultValue(defaultValue);
        this.classZ = classZ;
        if (Constants.ENABLE_DEVELOPMENT){
            instance.addListener(entry, EnumSet.of(NetworkTableEvent.Kind.kPublish), this::sendUpdates);
        }
    }
    public DebugableNumber(String tab, String fieldName, T defaultValue, Consumer<T> callback ,Class<T> classZ) {
        NetworkTable table = instance.getTable(tab);
        this.value.set(defaultValue);
        this.lastValue.set(defaultValue);
        this.entry = table.getEntry(fieldName);
        this.entry.setDefaultValue(defaultValue);
        this.classZ = classZ;
        if (Constants.ENABLE_DEVELOPMENT){
            instance.addListener(entry, EnumSet.of(NetworkTableEvent.Kind.kPublish), this::sendUpdates);
            addListener(callback);
        }
    }

    private void sendUpdates(NetworkTableEvent event) {
        if (!event.valueData.value.getValue().equals(lastValue.get())) {
            Object value = event.valueData.value.getValue();
            if (classZ.isInstance(value)) {
                T cast = classZ.cast(event.valueData.value.getValue());
                List<Consumer<? extends Number>> consumers = callbacks.get(event.topicInfo.getTopic());
                Class<Consumer<T>> tConsumer = getTConsumerCLass();
                for (Consumer<? extends Number> consumer : consumers) {
                    if (consumer.getClass().isAssignableFrom(tConsumer)) {
                        Consumer<T> c2 = tConsumer.cast(consumer);
                        c2.accept(cast);
                    }
                }
            }
        }
    }

    public void addListener(Consumer<T> consumer) {
        List<Consumer<? extends Number>> list = callbacks.getOrDefault(entry.getTopic(), new ArrayList<>());
        list.add(consumer);
        callbacks.put(entry.getTopic(), list);
    }

    public T getValue() {
        return value.get();
    }

    @SuppressWarnings("unchecked")
    private Class<Consumer<T>> getTConsumerCLass() {
        Consumer<T> tConsumer = t -> {};
        return (Class<Consumer<T>>) tConsumer.getClass();
    }
}
