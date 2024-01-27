package frc.robot.autochooser;

public enum AutoAction {
    DoNothing("Do Nothing"),
    DIAGROT("DIAG ROT"),

    FigureEight("Figure 8"),
    ShootAndCross("Shoot and Cross"),
    CrossLine("Cross The Line"),
    TwoPieceMoveLeft("Two Piece Move Left"),
    OnePieceMoveLeft("One Piece Move Left");

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
