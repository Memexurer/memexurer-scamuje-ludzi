package pl.nordhc.core.bukkit.system.disco.effects;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Color;

public class UltraDiscoEffect implements DiscoEffect{
    @Override
    public Color getColor() {
        Random random = ThreadLocalRandom.current();
        return Color.fromRGB(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    }
}
