package frc.robot.utils;

import java.util.Arrays;
import java.util.HashMap;

import edu.wpi.first.wpilibj.util.Color;

public enum ColorValue {
    Piece("Piece",new Color(0.559326171875,0.364013671875,0.0771484375));
//    Plastic("Plastic",new Color(0.288330078125,0.478759765625,0.2333984375));

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