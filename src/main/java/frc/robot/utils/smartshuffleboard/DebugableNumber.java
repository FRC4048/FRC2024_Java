package frc.robot.utils.smartshuffleboard;

import edu.wpi.first.networktables.NetworkTableEvent;
import frc.robot.constants.Constants;
import frc.robot.utils.CachedCallback;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class DebugableNumber<T extends Number> extends Debugable<T> {
    private final AtomicReference<T> value = new AtomicReference<>();
    private final AtomicReference<T> lastValue = new AtomicReference<>();
    public DebugableNumber(String tab, String fieldName, T defaultValue) {
        super(tab,fieldName,defaultValue);
        this.value.set(defaultValue);
        this.lastValue.set(defaultValue);
    }
    public DebugableNumber(String tab, String fieldName, T defaultValue, Consumer<T> callback) {
        super(tab,fieldName,defaultValue);
        this.value.set(defaultValue);
        this.lastValue.set(defaultValue);
        callback.accept(defaultValue);
        if (Constants.ENABLE_DEVELOPMENT){
            addListener(callback);
        }
    }

    @Override
    protected void setLastValue(T defaultValue) {
        lastValue.set(defaultValue);
    }

    @Override
    protected void setValue(T defaultValue) {
        value.set(defaultValue);
    }

    @Override
    public T getValue() {
        return value.get();
    }
    @Override
    protected CachedCallback<T> getUpdate(NetworkTableEvent event) {
        if (!event.valueData.value.getValue().equals(lastValue.get())) {
            Object v = event.valueData.value.getValue();
            Class<T> typeClass = getTypeClass();
            if (typeClass.isInstance(v)) {
                T cast = typeClass.cast(event.valueData.value.getValue());
                lastValue.set(value.get());
                value.set(cast);
                List<Consumer<?>> consumers = getCallback(event.topicInfo.getTopic());
                Class<Consumer<T>> tConsumer = getTConsumerCLass();
                for (Consumer<?> consumer : consumers) {
                    if (consumer.getClass().isAssignableFrom(tConsumer)) {
                        Consumer<T> c2 = tConsumer.cast(consumer);
                        return new CachedCallback<>(c2,cast);
                    }
                }
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private Class<Consumer<T>> getTConsumerCLass() {
        Consumer<T> tConsumer = t -> {};
        return (Class<Consumer<T>>) tConsumer.getClass();
    }
    @SuppressWarnings("unchecked")
    private Class<T> getTypeClass() {
        return (Class<T>) value.get().getClass();
    }
}
