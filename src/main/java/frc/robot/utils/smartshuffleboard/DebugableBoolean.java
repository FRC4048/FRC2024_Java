package frc.robot.utils.smartshuffleboard;

import edu.wpi.first.networktables.NetworkTableEvent;
import frc.robot.constants.Constants;
import frc.robot.utils.CachedCallback;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class DebugableBoolean extends Debugable<Boolean> {
    private final AtomicBoolean value = new AtomicBoolean();
    private final AtomicBoolean lastValue = new AtomicBoolean();

    public DebugableBoolean(String tab, String fieldName, boolean defaultValue) {
        super(tab, fieldName, defaultValue);
    }

    public DebugableBoolean(String tab, String fieldName, Boolean defaultValue, Consumer<Boolean> callback) {
        super(tab, fieldName, defaultValue);
        callback.accept(defaultValue);
        if (Constants.ENABLE_DEVELOPMENT) {
            addListener(callback);
        }
    }

    @Override
    protected void setLastValue(Boolean defaultValue) {
        lastValue.set(defaultValue);
    }

    @Override
    protected void setValue(Boolean defaultValue) {
        value.set(defaultValue);
    }

    @Override
    public Boolean getValue() {
        return value.get();
    }

    @Override
    protected CachedCallback<Boolean> getUpdate(NetworkTableEvent event) {
        if (!event.valueData.value.getValue().equals(lastValue.get())) {
            Boolean data = event.valueData.value.getBoolean();
            lastValue.set(value.get());
            value.set(data);
            List<Consumer<?>> callback = getCallback(event.topicInfo.getTopic());
            Class<Consumer<Boolean>> bConsumerCLass = getTConsumerCLass();
            for (Consumer<?> consumer : callback) {
                if (consumer.getClass().isAssignableFrom(bConsumerCLass)) {
                    Consumer<Boolean> c2 = (bConsumerCLass).cast(consumer);
                    return new CachedCallback<>(c2, data);
                }
            }
        }
        return null;
    }
    @SuppressWarnings("unchecked")
    private Class<Consumer<Boolean>> getTConsumerCLass() {
        Consumer<Boolean> bConsumer = s -> {};
        return (Class<Consumer<Boolean>>) bConsumer.getClass();
    }
}
