package frc.robot.subsystems;

import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;
import frc.robot.utils.logging.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PowerMonitor extends SubsystemBase {
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
     * @param channel to monitor
     * @param currentThreshold current threshold before runnable is called
     * @param runnable action to preform when current is reached
     * @return if addition to currentEvent list was successful
     */
    public boolean onCurrentAboveThreshold(int channel, double currentThreshold, Runnable runnable){
        if (channel < pdh.getNumChannels()){
            return maxCurrentEvents.add(new ChannelCurrentEvent(channel,currentThreshold,runnable));
        }
        return false;
    }
    /**
     * Runs action when channel current draw goes below a threshold. <br>
     * Note, when channel goes under min current draw you will have to re-register the event
     * @param channel to monitor
     * @param currentThreshold current threshold before runnable is called
     * @param runnable action to preform when current is reached
     * @return if addition to currentEvent list was successful
     */
    public boolean onCurrentBelowThreshold(int channel, double currentThreshold, Runnable runnable){
        if (channel < pdh.getNumChannels()){
            return minCurrentEvents.add(new ChannelCurrentEvent(channel,currentThreshold,runnable));
        }
        return false;
    }

    @Override
    public void periodic() {
        monitoringChannels.forEach(c -> {
            Logger.logDouble("robot/pdh/channel" + c + "Current", pdh.getCurrent(c), Constants.ENABLE_LOGGING);
        });
        callCurrentMaxEvents();
        callCurrentMinEvents();

        Logger.logDouble("robot/pdh/totalCurrent", pdh.getTotalCurrent(), Constants.ENABLE_LOGGING);
        // Power is the bus voltage multiplied by the current with the units Watts.
        Logger.logDouble("robot/pdh/totalPower", pdh.getTotalPower(), Constants.ENABLE_LOGGING);
        // Energy is the power summed over time with units Joules.
        Logger.logDouble("robot/pdh/totalEnergy", pdh.getTotalEnergy(), Constants.ENABLE_LOGGING);
        Logger.logBoolean("robot/pdh/haveBrownedOut", pdh.getFaults().Brownout, Constants.ENABLE_LOGGING);
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
}
