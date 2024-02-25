package frc.robot.utils;

import java.util.Arrays;
import java.util.HashMap;

import edu.wpi.first.wpilibj.util.Color;

public enum ColorValue {
    Piece50("Piece50",new Color(0.559326171875,0.364013671875,0.0771484375)),
    Piece25("Piece25",new Color(0.4238281250,0.4213867188,0.1552734375)),
    Piece10("Piece10", new Color(0.3425292969,0.4558105469,0.2021484375)),
    Piece5("Piece5", new Color(0.3154296875,0.4672851563,0.2177734375)),
    Plastic("Plastic",new Color(0.288330078125,0.478759765625,0.2333984375));

    private final String name;
    private final Color color;
    private static HashMap<Color, ColorValue> nameMap = new HashMap<>();

    static {
        Arrays.stream(ColorValue.values()).forEach(c -> nameMap.put(c.getColor(), c));
    }

    private ColorValue(String name, Color color){
        this.name = name;
        this.color = color;
    }
    public String getName(){
        return this.name;
    }

    public Color getColor(){
        return this.color;
    }

    public static ColorValue getFromColor(Color color){
        return nameMap.get(color);
    }
}