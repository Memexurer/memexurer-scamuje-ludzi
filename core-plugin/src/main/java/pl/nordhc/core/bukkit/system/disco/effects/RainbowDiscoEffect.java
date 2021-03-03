package pl.nordhc.core.bukkit.system.disco.effects;

import org.bukkit.Color;

public class RainbowDiscoEffect implements DiscoEffect {
    private int r, g, b;

    public RainbowDiscoEffect() {
        this.r = 255;
        this.g = 0;
        this.b = 0;
    }

    @Override
    public Color getColor() {
        if (r > 3 && b < 4) {
            r -= 3;
            g += 3;
        }
        if (g > 3 && r < 4) {
            g -= 3;
            b += 3;
        }
        if (b > 3 && g < 4) {
            r += 3;
            b -= 3;
        }

        return Color.fromRGB(r, g, b);
    }
}
