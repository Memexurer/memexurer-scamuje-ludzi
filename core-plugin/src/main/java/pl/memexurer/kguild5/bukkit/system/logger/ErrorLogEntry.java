package pl.memexurer.kguild5.bukkit.system.logger;

public final class ErrorLogEntry {
    private final Throwable throwable;
    private final long time;
    private final String player;

    public ErrorLogEntry(Throwable throwable, long time, String player) {
        this.throwable = throwable;
        this.time = time;
        this.player = player;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public long getTime() {
        return time;
    }

    public String getPlayer() {
        return player;
    }
}
