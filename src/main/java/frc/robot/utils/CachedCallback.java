package frc.robot.utils;

import java.util.function.Consumer;

public record CachedCallback<T>(Consumer<T> consumer, T value) {
    public void call(){
        consumer.accept(value);
    }
}
