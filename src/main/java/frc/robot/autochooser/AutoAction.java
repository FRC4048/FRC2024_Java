package frc.robot.autochooser;

public enum AutoAction {
    DoNothing("Do Nothing"),
    ShootFour("Shoot Four"),
    ShootTwo("Shoot Two"),
    ShootTwoDip("Shoot Two & Dip"),
    ShootCross("Shoot & Cross"),
    Fork("Fork");
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
