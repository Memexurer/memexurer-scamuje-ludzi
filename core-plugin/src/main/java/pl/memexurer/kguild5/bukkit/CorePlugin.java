package pl.memexurer.kguild5.bukkit;

import co.aikar.commands.PaperCommandManager;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoDatabase;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.diorite.cfg.Configuration;
import pl.memexurer.kguild5.bukkit.commands.BackupCommand;
import pl.memexurer.kguild5.bukkit.listener.InventoryListener;
import pl.memexurer.kguild5.bukkit.listener.UserDataListener;
import pl.memexurer.kguild5.bukkit.system.data.codec.converter.ItemConverter;
import pl.memexurer.kguild5.bukkit.commands.HomeCommand;
import pl.memexurer.kguild5.bukkit.commands.QuickReloadCommand;
import pl.memexurer.kguild5.bukkit.commands.SetHomeCommand;
import pl.memexurer.kguild5.bukkit.system.data.codec.ConverterProvider;
import pl.memexurer.kguild5.bukkit.system.data.codec.converter.LocationConverter;
import pl.memexurer.kguild5.bukkit.system.data.flat.PluginConfiguration;
import pl.memexurer.kguild5.bukkit.system.data.user.UserBackup.BackupConverter;
import pl.memexurer.kguild5.bukkit.system.data.user.UserDataModel.ModelConverter;
import pl.memexurer.kguild5.bukkit.system.data.user.UserHandler;

public final class CorePlugin extends JavaPlugin {

  private static CorePlugin instance;
  private MongoClient databaseConnection;
  private UserHandler userHandler;

  private PluginConfiguration pluginConfiguration;

  public static CorePlugin getInstance() {
    return instance;
  }

  public void onEnable() {
    instance = this;

    if (!getDataFolder().exists()) {
      getDataFolder().mkdirs();
    }

    CodecRegistry codecRegistry = CodecRegistries
        .fromRegistries(CodecRegistries
                .fromProviders(new ConverterProvider(
                    new LocationConverter(), new ItemConverter(),
                    new ModelConverter(), new BackupConverter())),
            MongoClient.getDefaultCodecRegistry());

    MongoDatabase database = (databaseConnection = new MongoClient(
        "localhost",
        new MongoClientOptions.Builder()
            .codecRegistry(codecRegistry)
            .build()
    )).getDatabase("database");
    (this.userHandler = new UserHandler(
        this,
        database
    )).initialize();

    Arrays.stream(new Listener[]{
        new UserDataListener(userHandler),
        new InventoryListener()
    }).forEach(x -> getServer().getPluginManager().registerEvents(x, this));

    PaperCommandManager commandManager = new PaperCommandManager(this);
    commandManager.registerDependency(UserHandler.class, userHandler);
    commandManager.registerCommand(new QuickReloadCommand());
    commandManager.registerCommand(new HomeCommand());
    commandManager.registerCommand(new SetHomeCommand());
    commandManager.registerCommand(new BackupCommand());

    try {
      pluginConfiguration = Configuration.loadConfigFile(
          new File(getDataFolder(), "config.yml"),
          PluginConfiguration.class, new PluginConfiguration()
      );
    } catch (IOException e) {
      die("cos sie zjebalo przy ladowaniu configu", e);
    }
  }

  private void die(String message, Throwable throwable) {
    getLogger().severe(message);
    throwable.printStackTrace();
    getServer().getPluginManager().disablePlugin(this);
  }

  public UserHandler getUserHandler() {
    return userHandler;
  }

  @Override
  public void onDisable() {
    userHandler.update();
    if (databaseConnection != null) {
      databaseConnection.close();
    }
  }
}
