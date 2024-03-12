package frc.robot.subsystems;

import edu.wpi.first.hal.PowerDistributionFaults;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;
import frc.robot.utils.logging.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PowerMonitor extends SubsystemBase {
    private final String loggingBase = "/robot/pdh/";
    private final PowerDistribution pdh;
    private final Set<Integer> monitoringChannels = new HashSet<>();
    private final List<ChannelCurrentEvent> maxCurrentEvents = new ArrayList<>();
    private final List<ChannelCurrentEvent> minCurrentEvents = new ArrayList<>();

    public PowerMonitor() {
        this.pdh = new PowerDistribution(1, PowerDistribution.ModuleType.kRev);
    }

    /**
     * @param channel to monitor
     * @return boolean representing if addition to monitoring set was successful
     */
    public boolean logChannel(int channel) {
        if (channel < pdh.getNumChannels()) {
            return monitoringChannels.add(channel);
        }
        return false;
    }

    /**
     * @param channel to unmonitor
     * @return boolean representing if removal from monitoring set was successful
     */
    public boolean unlogChannel(int channel) {
        return monitoringChannels.remove(channel);
    }

    /**
     * Runs action when channel current draw reaches a threshold. <br>
     * Note, when channel reaches current draw you will have to re-register the event
     *
     * @param channel          to monitor
     * @param currentThreshold current threshold before runnable is called
     * @param runnable         action to preform when current is reached
     * @return if addition to currentEvent list was successful
     */
    public boolean onCurrentAboveThreshold(int channel, double currentThreshold, Runnable runnable) {
        if (channel < pdh.getNumChannels()) {
            return maxCurrentEvents.add(new ChannelCurrentEvent(channel, currentThreshold, runnable));
        }
        return false;
    }

    /**
     * Runs action when channel current draw goes below a threshold. <br>
     * Note, when channel goes under min current draw you will have to re-register the event
     *
     * @param channel          to monitor
     * @param currentThreshold current threshold before runnable is called
     * @param runnable         action to preform when current is reached
     * @return if addition to currentEvent list was successful
     */
    public boolean onCurrentBelowThreshold(int channel, double currentThreshold, Runnable runnable) {
        if (channel < pdh.getNumChannels()) {
            return minCurrentEvents.add(new ChannelCurrentEvent(channel, currentThreshold, runnable));
        }
        return false;
    }

    @Override
    public void periodic() {
        callCurrentMaxEvents();
        callCurrentMinEvents();
        monitoringChannels.forEach(c -> {
            Logger.logDouble(loggingBase + "channel" + c + "Current", getChannelCurrent(c), Constants.ENABLE_LOGGING);
        });
        Logger.logDouble(loggingBase + "totalCurrent", getTotalCurrent(), Constants.ENABLE_LOGGING);
        Logger.logDouble(loggingBase + "totalPower", getTotalPower(), Constants.ENABLE_LOGGING);
        Logger.logDouble(loggingBase + "totalEnergy", getTotalEnergy(), Constants.ENABLE_LOGGING);
        Logger.logBoolean(loggingBase + "haveBrownedOut", getFaults().Brownout, Constants.ENABLE_LOGGING);
    }

    private void callCurrentMaxEvents() {
        List<ChannelCurrentEvent> events = maxCurrentEvents.stream().filter(event -> {
            int channel = event.getChannel();
            return pdh.getCurrent(channel) > event.getCurrentThreshold();
        }).toList();
        events.forEach(e -> e.getAction().run());
        maxCurrentEvents.removeAll(events);
    }

    private void callCurrentMinEvents() {
        List<ChannelCurrentEvent> events = minCurrentEvents.stream().filter(event -> {
            int channel = event.getChannel();
            return pdh.getCurrent(channel) < event.getCurrentThreshold();
        }).toList();
        events.forEach(e -> e.getAction().run());
        minCurrentEvents.removeAll(events);
    }

    /**
     * @param channel to check
     * @return current value from channel or {@link Double#NaN} if channel is invalid
     */
    public double getChannelCurrent(int channel){
        if (channel < pdh.getNumChannels()) {
            return pdh.getCurrent(channel);
        }
        return Double.NaN;
    }

    /**
     * @return total Current draw from pdh
     */
    public double getTotalCurrent(){
        return pdh.getTotalCurrent();
    }
    public PowerDistributionFaults getFaults(){
        return pdh.getFaults();
    }

    /**
     * Power is the bus voltage multiplied by the current with the units Watts.
     * @return Total power drawn from the pdh.
     */
    public double getTotalPower(){
        return pdh.getTotalPower();
    }

    /**
     * Energy is the power summed over time with units Joules.
     * @return Total Energy drawn from the pdh
     */
    public double getTotalEnergy(){
        return pdh.getTotalEnergy();
    }

    /**
     * @return Temperature in degrees celsius
     */
    public double getTemp(){
        return pdh.getTemperature();
    }
}
