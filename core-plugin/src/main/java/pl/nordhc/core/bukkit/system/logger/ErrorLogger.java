package pl.nordhc.core.bukkit.system.logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ErrorLogger {
    private static final ErrorLogger INSTANCE = new ErrorLogger();

    private final List<ErrorLogEntry> logEntryList = new ArrayList<>();

    private ErrorLogger() {
    }

    public static ErrorLogger getInstance() {
        return INSTANCE;
    }

    public void handleException(String player, Throwable throwable) {
        logEntryList.add(new ErrorLogEntry(throwable, System.currentTimeMillis(), player));
    }

    public List<ErrorLogEntry> getLogEntryListByPlayer(String name) {
        return logEntryList.stream()
                .filter(e -> e.getPlayer().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

    public void clearLogs(String name) {
        logEntryList.removeIf(e -> e.getPlayer().equalsIgnoreCase(name));
    }
}
