package frc.robot.autochooser;

import java.util.Arrays;
import java.util.HashMap;

public enum AutoAction {
    DoNothing("Do Nothing"),
    ShootAndCross("Shoot & Cross"),
    ShootFour("Shoot Four"),
    ShootTwo("Shoot Two"),
    ShootTwoDip("Shoot Two & Dip"),
    Fork("Fork"),
    SmartFork("Smart Fork"),
    SHOOT("Shoot & Stop"),
    INVALID("INVALID");
    private final String name;
    private static final HashMap<String, AutoAction> nameMap = new HashMap<>();

    static{
        Arrays.stream(AutoAction.values()).forEach(v -> nameMap.put(v.getName(), v));
    }

    AutoAction(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    @Override
    public String toString() {
        return getName();
    }
    public static AutoAction fromName(String name){
        return nameMap.get(name);
    }
}
