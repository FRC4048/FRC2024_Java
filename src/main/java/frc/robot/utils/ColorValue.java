package frc.robot.utils;

import java.util.Arrays;
import java.util.HashMap;

import edu.wpi.first.wpilibj.util.Color;

public enum ColorValue {
    Piece50("Piece50",new Color(0.586669921875,0.3491210937500,0.0646972656250)),
    PiecePointPoint1("PiecePointPoint1", new Color(0.312012268066,0.479247786621,0.209228226563)),
    PlasticPieceConcoction("PlasticPieceConcoction",new Color(0.312011718751,0.4792480468749,0.2092285156249)),
    
    Plastic("Plastic",new Color(0.312011718750,0.4792480468750,0.2092285156250));

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