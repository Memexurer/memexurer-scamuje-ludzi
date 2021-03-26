package pl.memexurer.kguild5.bukkit.system.disco.effects;

import org.bukkit.Color;

public class GrayscaleDiscoEffect implements DiscoEffect {
    private int grayscale;

    @Override
    public Color getColor() {
        if (grayscale > 250) {
            grayscale = 0;
        } else grayscale += 3;

        return Color.fromRGB(grayscale, grayscale, grayscale);
    }
}
