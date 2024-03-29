package frc.robot.utils.smartshuffleboard;

import edu.wpi.first.networktables.NetworkTableEvent;
import frc.robot.constants.Constants;
import frc.robot.utils.CachedCallback;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class DebugableString extends Debugable<String> {
    private final AtomicReference<String> value = new AtomicReference<>();
    private final AtomicReference<String> lastValue = new AtomicReference<>();

    public DebugableString(String tab, String fieldName, String defaultValue) {
        super(tab, fieldName, defaultValue);
    }

    public DebugableString(String tab, String fieldName, String defaultValue, Consumer<String> callback) {
        super(tab, fieldName, defaultValue);
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
    protected void setValue(String defaultValue) {
        value.set(defaultValue);
    }

    @Override
    public String getValue() {
        return value.get();
    }

    @Override
    protected CachedCallback<String> getUpdate(NetworkTableEvent event) {
        if (!event.valueData.value.getValue().equals(lastValue.get())) {
            String data = event.valueData.value.getString();
            setLastValue(value.get());
            setValue(data);
            List<Consumer<?>> callback = getCallback(event.topicInfo.getTopic());
            for (Consumer<?> consumer : callback) {
                Consumer<String> c2 = narrowConsumer(consumer);
                return new CachedCallback<>(c2, data);
            }
        }
        return null;
    }
}
