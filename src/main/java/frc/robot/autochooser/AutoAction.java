package frc.robot.autochooser;

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
}
