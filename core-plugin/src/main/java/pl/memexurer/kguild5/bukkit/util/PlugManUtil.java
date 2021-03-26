package pl.memexurer.kguild5.bukkit.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.Event;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredListener;

public final class PlugManUtil {

  /**
   * Reload a plugin.
   *
   * @param plugin the plugin to reload
   */
  public static void reload(Plugin plugin)
      throws InvalidDescriptionException, InvalidPluginException {
    if (plugin != null) {
      unload(plugin);

      File file;
      try {
        file = new File(URLDecoder.decode(plugin.getClass().getProtectionDomain()
            .getCodeSource()
            .getLocation()
            .getPath(), StandardCharsets.UTF_8.name()));
      } catch (UnsupportedEncodingException e) {
        throw new RuntimeException(e);
      }

      Plugin pl = Bukkit.getPluginManager().loadPlugin(file);
      pl.onLoad();
      Bukkit.getPluginManager().enablePlugin(pl);
    }
  }

  /**
   * Unload a plugin.
   *
   * @param plugin the plugin to unload
   */
  private static void unload(Plugin plugin) {

    String name = plugin.getName();

    PluginManager pluginManager = Bukkit.getPluginManager();

    SimpleCommandMap commandMap = null;

    List<Plugin> plugins = null;

    Map<String, Plugin> names = null;
    Map<String, Command> commands = null;
    Map<Event, SortedSet<RegisteredListener>> listeners = null;

    boolean reloadlisteners = true;

    if (pluginManager != null) {

      pluginManager.disablePlugin(plugin);

      try {

        Field pluginsField = Bukkit.getPluginManager().getClass().getDeclaredField("plugins");
        pluginsField.setAccessible(true);
        plugins = (List<Plugin>) pluginsField.get(pluginManager);

        Field lookupNamesField = Bukkit.getPluginManager().getClass()
            .getDeclaredField("lookupNames");
        lookupNamesField.setAccessible(true);
        names = (Map<String, Plugin>) lookupNamesField.get(pluginManager);

        try {
          Field listenersField = Bukkit.getPluginManager().getClass().getDeclaredField("listeners");
          listenersField.setAccessible(true);
          listeners = (Map<Event, SortedSet<RegisteredListener>>) listenersField.get(pluginManager);
        } catch (Exception e) {
          reloadlisteners = false;
        }

        Field commandMapField = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
        commandMapField.setAccessible(true);
        commandMap = (SimpleCommandMap) commandMapField.get(pluginManager);

        Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
        knownCommandsField.setAccessible(true);
        commands = (Map<String, Command>) knownCommandsField.get(commandMap);

      } catch (ReflectiveOperationException e) {
        throw new RuntimeException(e);
      }

    }

    pluginManager.disablePlugin(plugin);

    if (plugins != null) {
      plugins.remove(plugin);
    }

    if (names != null) {
      names.remove(name);
    }

    if (listeners != null && reloadlisteners) {
      for (SortedSet<RegisteredListener> set : listeners.values()) {
        set.removeIf(value -> value.getPlugin() == plugin);
      }
    }

    if (commandMap != null) {
      for (Iterator<Map.Entry<String, Command>> it = commands.entrySet().iterator();
          it.hasNext(); ) {
        Map.Entry<String, Command> entry = it.next();
        if (entry.getValue() instanceof PluginCommand) {
          PluginCommand c = (PluginCommand) entry.getValue();
          if (c.getPlugin() == plugin) {
            c.unregister(commandMap);
            it.remove();
          }
        }
      }
    }

    // Attempt to close the classloader to unlock any handles on the plugin's jar file.
    ClassLoader cl = plugin.getClass().getClassLoader();

    if (cl instanceof URLClassLoader) {

      try {

        Field pluginField = cl.getClass().getDeclaredField("plugin");
        pluginField.setAccessible(true);
        pluginField.set(cl, null);

        Field pluginInitField = cl.getClass().getDeclaredField("pluginInit");
        pluginInitField.setAccessible(true);
        pluginInitField.set(cl, null);

      } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
        throw new RuntimeException(ex);
      }

      try {

        ((URLClassLoader) cl).close();
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }

    }

    // Will not work on processes started with the -XX:+DisableExplicitGC flag, but lets try it anyway.
    // This tries to get around the issue where Windows refuses to unlock jar files that were previously loaded into the JVM.
    System.gc();
  }
}
