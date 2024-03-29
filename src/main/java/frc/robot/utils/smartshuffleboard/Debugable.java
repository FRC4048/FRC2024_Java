package frc.robot.utils.smartshuffleboard;

import edu.wpi.first.networktables.*;
import frc.robot.constants.Constants;
import frc.robot.utils.CachedCallback;
import frc.robot.utils.logging.Logger;

import java.time.Instant;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public abstract class Debugable<T> {
    private static final NetworkTableInstance instance = NetworkTableInstance.getDefault();
    private static final ConcurrentHashMap<Topic, List<Consumer<?>>> callbacks = new ConcurrentHashMap<>();
    private static final ConcurrentLinkedQueue<CachedCallback<?>> callbackCache = new ConcurrentLinkedQueue<>();
    private final NetworkTableEntry entry;
    private final T defaultValue;

    public Debugable(String tab, String fieldName, T defaultValue) {
        NetworkTable table = instance.getTable(tab);
        this.setValue(defaultValue);
        this.setLastValue(defaultValue);
        this.entry = table.getEntry(fieldName);
        this.entry.setDefaultValue(defaultValue);
        this.defaultValue = defaultValue;
        if (Constants.ENABLE_DEVELOPMENT) {
            instance.addListener(entry, EnumSet.of(NetworkTableEvent.Kind.kPublish), (event -> addToCache(getUpdate(event))));
        }
    }

    public static void flushCache() {
        Instant start = Instant.now();
        CachedCallback<?> poll = callbackCache.poll();
        while (poll != null) {
            poll.call();
            poll = callbackCache.poll();
        }
        int diff = (int) TimeUnit.MICROSECONDS.convert(Instant.now().getNano() - start.getNano(), TimeUnit.NANOSECONDS);
        Logger.logInteger("robot/debugableCacheTime", diff, Constants.ENABLE_LOGGING);
    }

    public void addListener(Consumer<T> consumer) {
        List<Consumer<?>> list = callbacks.getOrDefault(entry.getTopic(), new ArrayList<>());
        list.add(consumer);
        callbacks.put(entry.getTopic(), list);
    }

    protected List<Consumer<?>> getCallback(Topic topic) {
        return callbacks.get(topic);
    }

    public void addCallback(Topic topic, Consumer<T> consumer) {
        List<Consumer<?>> list = callbacks.getOrDefault(topic, new ArrayList<>());
        list.add(consumer);
        callbacks.put(topic, list);
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public void resetValue() {
        entry.setValue(defaultValue);
    }

    protected void addToCache(CachedCallback<T> callback) {
        if (callback != null) {
            callbackCache.add(callback);
        }
    }

    protected abstract CachedCallback<T> getUpdate(NetworkTableEvent event);

    protected abstract void setLastValue(T defaultValue);

    public abstract T getValue();

    protected abstract void setValue(T defaultValue);
}
