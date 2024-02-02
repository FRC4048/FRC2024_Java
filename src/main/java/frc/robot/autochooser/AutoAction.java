package frc.robot.autochooser;

public enum AutoAction {
    DoNothing("Do Nothing"),
    ShootAndCross("Shoot and Cross"),
    ShootFourLeft("Shoot 4 Left");
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
