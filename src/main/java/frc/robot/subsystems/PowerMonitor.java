package frc.robot.subsystems;

import edu.wpi.first.hal.PowerDistributionFaults;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;
import frc.robot.utils.logging.Logger;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.DoublePredicate;

public class PowerMonitor extends SubsystemBase {
    private final String loggingBase = "/robot/pdh/";
    private final PowerDistribution pdh;
    private final BitSet monitoringChannels = new BitSet();
    private final List<ChannelCurrentEvent> currentEvents = new ArrayList<>();

    public PowerMonitor() {
        this.pdh = new PowerDistribution(1, PowerDistribution.ModuleType.kRev);
    }

    /**
     * @param channel to monitor
     * @return boolean representing if addition to monitoring set was successful
     */
    public boolean logChannel(int channel) {
        if (channel < pdh.getNumChannels()) {
            monitoringChannels.set(channel, true);
            return true;
        }
        return false;
    }

    /**
     * @param channel to unmonitor
     */
    public void unlogChannel(int channel) {
        monitoringChannels.set(channel, false);
    }

    /**
     * Runs action when channel current draw goes below or above a threshold.
     *
     * @param channel       to monitor
     * @param shouldTrigger predicate that takes a current and returns true if you want event to be fired or false if you want the event to not be fired
     * @param callback      returns true if event should remain registered after being called
     * @return if addition to currentEvent list was successful
     */
    public boolean onCurrentChange(int channel, DoublePredicate shouldTrigger, BiFunction<Integer, Double, Boolean> callback) {
        if (channel < pdh.getNumChannels()) {
            return currentEvents.add(new ChannelCurrentEvent(channel, shouldTrigger, callback));
        }
        return false;
    }

    /**
     * Runs action when channel current draw goes below or above a threshold.
     *
     * @param event to add
     * @return if addition to currentEvent list was successful
     */
    public boolean onCurrentChange(ChannelCurrentEvent event) {
        if (event.getChannel() < pdh.getNumChannels()) {
            return currentEvents.add(event);
        }
        return false;
    }

    /**
     * Removes {@link ChannelCurrentEvent} from monitoring list
     *
     * @param channel       to monitor
     * @param shouldTrigger predicate that takes a current and returns true if you want event to be fired or false if you want the event to not be fired
     * @param callback      returns true if event should remain registered after being called
     * @return if removal to currentEvent list was successful
     */
    public boolean ignoreCurrentChange(int channel, DoublePredicate shouldTrigger, BiFunction<Integer, Double, Boolean> callback) {
        if (channel < pdh.getNumChannels()) {
            return currentEvents.remove(new ChannelCurrentEvent(channel, shouldTrigger, callback));
        }
        return false;
    }

    /**
     * Removes {@link ChannelCurrentEvent} from monitoring list
     *
     * @param event to remove
     * @return if removal to currentEvent list was successful
     */
    public boolean ignoreCurrentChange(ChannelCurrentEvent event) {
        if (event.getChannel() < pdh.getNumChannels()) {
            return currentEvents.remove(event);
        }
        return false;
    }

    @Override
    public void periodic() {
        callCurrentEvents();
        monitoringChannels.stream().forEach(c -> {
            Logger.logDouble(loggingBase + "channel" + c + "Current", getChannelCurrent(c), Constants.ENABLE_LOGGING);
        });
        Logger.logDouble(loggingBase + "totalCurrent", getTotalCurrent(), Constants.ENABLE_LOGGING);
        Logger.logDouble(loggingBase + "totalPower", getTotalPower(), Constants.ENABLE_LOGGING);
        Logger.logDouble(loggingBase + "totalEnergy", getTotalEnergy(), Constants.ENABLE_LOGGING);
        Logger.logDouble(loggingBase + "voltage", getVoltage(), Constants.ENABLE_LOGGING);
        Logger.logBoolean(loggingBase + "haveBrownedOut", getFaults().Brownout, Constants.ENABLE_LOGGING);
    }

    private double getVoltage() {
        return pdh.getVoltage();
    }

    private void callCurrentEvents() {
        HashMap<ChannelCurrentEvent, Double> channelCurrentMap = new HashMap<>();
        List<ChannelCurrentEvent> events = currentEvents.stream().filter(event -> {
            int channel = event.getChannel();
            double current = getChannelCurrent(channel);
            if (event.getCurrentThreshold().test(current)) {
                channelCurrentMap.put(event, current);
                return true;
            }
            return false;
        }).toList();
        currentEvents.removeAll(
                events.stream()
                        .filter(e -> !e.getCallback().apply(e.getChannel(), channelCurrentMap.get(e)))
                        .toList()
        );
    }

    /**
     * @param channel to check
     * @return current value from channel or {@link Double#NaN} if channel is invalid
     */
    public double getChannelCurrent(int channel) {
        if (channel < pdh.getNumChannels()) {
            return pdh.getCurrent(channel);
        }
        return Double.NaN;
    }

    /**
     * @return total Current draw from pdh
     */
    public double getTotalCurrent() {
        return pdh.getTotalCurrent();
    }

    public PowerDistributionFaults getFaults() {
        return pdh.getFaults();
    }

    /**
     * Power is the bus voltage multiplied by the current with the units Watts.
     *
     * @return Total power drawn from the pdh.
     */
    public double getTotalPower() {
        return pdh.getTotalPower();
    }

    /**
     * Energy is the power summed over time with units Joules.
     *
     * @return Total Energy drawn from the pdh
     */
    public double getTotalEnergy() {
        return pdh.getTotalEnergy();
    }

    /**
     * @return Temperature in degrees celsius
     */
    public double getTemp() {
        return pdh.getTemperature();
    }
}
