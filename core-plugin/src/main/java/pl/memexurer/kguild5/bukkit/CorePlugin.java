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
import pl.memexurer.kguild5.bukkit.listener.InventoryListener;
import pl.memexurer.kguild5.bukkit.listener.UserDataListener;
import pl.memexurer.kguild5.bukkit.system.data.flat.FlatConfiguration;
import pl.memexurer.kguild5.bukkit.commands.HomeCommand;
import pl.memexurer.kguild5.bukkit.commands.QuickReloadCommand;
import pl.memexurer.kguild5.bukkit.commands.SetHomeCommand;
import pl.memexurer.kguild5.bukkit.system.data.codec.ConverterProvider;
import pl.memexurer.kguild5.bukkit.system.data.codec.LocationConverter;
import pl.memexurer.kguild5.bukkit.system.data.flat.FlatConfigurationUtils;
import pl.memexurer.kguild5.bukkit.system.data.user.UserDataModel.ModelConverter;
import pl.memexurer.kguild5.bukkit.system.data.user.UserHandler;

public final class CorePlugin extends JavaPlugin {

  private static CorePlugin instance;
  private MongoClient databaseConnection;
  private UserHandler userHandler;

  public static CorePlugin getInstance() {
    return instance;
  }

  @Override
  public void onEnable() {
    instance = this;

    if (!getDataFolder().exists()) {
      getDataFolder().mkdirs();
    }

    CodecRegistry codecRegistry = CodecRegistries
        .fromRegistries(CodecRegistries
                .fromProviders(new ConverterProvider(new LocationConverter(), new ModelConverter())),
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

    File flatConfigurationFile = new File(getDataFolder(), "styl_zycia.adisz");
    if (flatConfigurationFile.exists()) {
      try {
        FlatConfigurationUtils.load(FlatConfiguration.class, flatConfigurationFile);
      } catch (IOException e) {
        getLogger().severe("styl zycia adisz");
        e.printStackTrace();
      }
    }
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

    try {
      File outFile = new File(getDataFolder(), "config.json");
      FlatConfigurationUtils.save(FlatConfiguration.class, outFile);
    } catch (IOException e) {
      getLogger().severe("styl zycia adisz");
      e.printStackTrace();
    }
  }
}
