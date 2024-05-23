package frc.robot.subsystems.swervev3;

import frc.robot.Robot;
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
            double time = Logger.getRealTimestamp() - startTime;
            Robot.runInMainThread(()-> Logger.recordOutput("OdomThreadLockTime", time));
            CountDownLatch latch = new CountDownLatch(odometryRunnables.size());
            boolean overrun;
            try {
                for (Consumer<Double> odometryRunnable : odometryRunnables) {
                    odometryRunnable.accept(startTime/1.0e6);
                    latch.countDown();
                }
                overrun = !latch.await(5, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                overrun = true;
            } finally {
                lock.unlock();
            }
            double endTime = Logger.getRealTimestamp();
            double cycleTime = (endTime - startTime) / 1000;
            Robot.runInMainThread(()-> Logger.recordOutput("OdomUpdateCycleTime", cycleTime));
            boolean finalOverrun = overrun;
            Robot.runInMainThread(()-> Logger.recordOutput("OdomOverrun", finalOverrun));
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
