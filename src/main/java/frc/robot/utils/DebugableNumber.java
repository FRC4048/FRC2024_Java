package frc.robot.utils;

import edu.wpi.first.networktables.NetworkTableEvent;
import frc.robot.constants.Constants;

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
    protected void setCurrentValue(T defaultValue) {
        value.set(defaultValue);
    }

    @Override
    public void getCurrentValue() {
        value.get();
    }
    @Override
    protected void sendUpdates(NetworkTableEvent event) {
        if (!event.valueData.value.getValue().equals(lastValue.get())) {
            Object v = event.valueData.value.getValue();
            Class<T> typeClass = getTypeClass();
            if (typeClass.isInstance(v)) {
                T cast = typeClass.cast(event.valueData.value.getValue());
                List<Consumer<?>> consumers = getCallback(event.topicInfo.getTopic());
                Class<Consumer<T>> tConsumer = getTConsumerCLass();
                for (Consumer<?> consumer : consumers) {
                    if (consumer.getClass().isAssignableFrom(tConsumer)) {
                        Consumer<T> c2 = tConsumer.cast(consumer);
                        c2.accept(cast);
                    }
                }
            }
        }
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
