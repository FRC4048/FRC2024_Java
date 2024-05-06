package frc.robot.subsystems.swervev3;

import org.littletonrobotics.junction.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

public class OdometryThread {


    private static final OdometryThread inst = new OdometryThread();
    private final ScheduledExecutorService executor;
    private final List<Consumer<Double>> odometryRunnables = new ArrayList<>();
    private final ReentrantLock lock = new ReentrantLock();
    private boolean started = false;

    public static OdometryThread getInstance() {
        return inst;
    }

    public OdometryThread() {
        this.executor = Executors.newScheduledThreadPool(1, r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
    }

    public void start() {
        if (started) return;
        started = true;
        executor.scheduleAtFixedRate(() -> {
            double startTime = Logger.getRealTimestamp();
            lock.lock();
            CountDownLatch latch = new CountDownLatch(odometryRunnables.size());
            double time = Logger.getRealTimestamp();
            for (Consumer<Double> odometryRunnable : odometryRunnables) {
                Thread thread = new Thread(() -> {
                    odometryRunnable.accept(time);
                    latch.countDown();
                });
                thread.setDaemon(true);
                thread.start();
            }
            try {
                latch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            lock.unlock();
            double endTime = Logger.getRealTimestamp();
            double cycleTime = (endTime - startTime) / 1000;
            Logger.recordOutput("OdomUpdateCycleTime", cycleTime);
        }, 0, 10, TimeUnit.MILLISECONDS);
    }

    public void addRunnable(Consumer<Double> runnable) {
        lock.lock();
        odometryRunnables.add(runnable);
        lock.unlock();
    }

    public ReentrantLock getLock() {
        return lock;
    }
}
