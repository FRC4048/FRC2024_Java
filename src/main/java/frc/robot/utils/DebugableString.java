package frc.robot.utils;

import edu.wpi.first.networktables.NetworkTableEvent;
import frc.robot.constants.Constants;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class DebugableString extends Debugable<String> {
    private final AtomicReference<String> value = new AtomicReference<>();
    private final AtomicReference<String> lastValue = new AtomicReference<>();

    public DebugableString(String tab, String fieldName, String defaultValue) {
        super(tab, fieldName, defaultValue);
        this.value.set(defaultValue);
        this.lastValue.set(defaultValue);
    }

    public DebugableString(String tab, String fieldName, String defaultValue, Consumer<String> callback) {
        super(tab, fieldName, defaultValue);
        this.value.set(defaultValue);
        this.lastValue.set(defaultValue);
        callback.accept(defaultValue);
        if (Constants.ENABLE_DEVELOPMENT) {
            addListener(callback);
        }
    }

    @Override
    protected void setLastValue(String defaultValue) {
        lastValue.set(defaultValue);
    }

    @Override
    protected void setCurrentValue(String defaultValue) {
        value.set(defaultValue);
    }

    @Override
    public void getCurrentValue() {
        value.get();
    }

    @Override
    protected void sendUpdates(NetworkTableEvent event) {
        String data = event.valueData.value.getString();
        List<Consumer<?>> callback = getCallback(event.topicInfo.getTopic());
        Class<Consumer<String>> tConsumerCLass = getTConsumerCLass();
        for (Consumer<?> consumer : callback) {
            if (consumer.getClass().isAssignableFrom(tConsumerCLass)) {
                Consumer<String> c2 = (tConsumerCLass).cast(consumer);
                c2.accept(data);
            }
        }
    }
    @SuppressWarnings("unchecked")
    private Class<Consumer<String>> getTConsumerCLass() {
        Consumer<String> sConsumer = s -> {};
        return (Class<Consumer<String>>) sConsumer.getClass();
    }
}
