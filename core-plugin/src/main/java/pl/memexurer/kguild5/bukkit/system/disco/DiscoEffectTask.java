package pl.memexurer.kguild5.bukkit.system.disco;

public class DiscoEffectTask implements Runnable {
    @Override
    public void run() {
        DiscoEffectWrapper.GRAYSCALE.update();
        DiscoEffectWrapper.RAINBOW.update();
        DiscoEffectWrapper.ULTRA.update();
    }
}
