package pl.nordhc.core.bukkit.system.data.user;

import com.mongodb.client.MongoDatabase;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import pl.nordhc.core.bukkit.CorePlugin;

public class UserHandler {

  private static final long SAVE_TIME = 20L * 60L * 3L;

  private final Map<UUID, UserDataModel> dataModelMap = new HashMap<>();

  private final Plugin parentPlugin;
  private final UserRepository repository;

  private boolean initialized;

  public UserHandler(CorePlugin plugin, MongoDatabase database) {
    this.parentPlugin = plugin;
    this.repository = new UserRepository(
        database
    );
  }

  public void initialize() {
    if (initialized) {
      throw new IllegalStateException("UserHandler already initialized!");
    }

    this.loadData();
    this.parentPlugin.getServer().getScheduler()
        .runTaskTimerAsynchronously(parentPlugin, this::update,
            SAVE_TIME, SAVE_TIME);

    initialized = true;
  }

  public void update() {
    if (dataModelMap.size() != 0) {
      repository.update(dataModelMap.values());
    }

    for (UserDataModel dataModel : dataModelMap.values()) {
      dataModel.update();
    }
  }


  private void loadData() {
    Iterator<UserDataModel> dataModelIterator = repository.fetchUsers();
    while (dataModelIterator.hasNext()) {
      UserDataModel dataModel = dataModelIterator.next();
      dataModelMap.put(dataModel.getUuid(), dataModel);
    }
  }

  public UserDataModel getPlayer(Player player) {
    UserDataModel dataModel = dataModelMap.get(player.getUniqueId());
    if (dataModel != null) {
      return dataModel;
    }

    dataModel = new UserDataModel(player.getName(), player.getUniqueId(), null);
    dataModelMap.put(dataModel.getUuid(), dataModel);
    return dataModel;
  }

  public UserDataModel getPlayer(UUID uuid) {
    return dataModelMap.get(uuid);
  }

  public UserDataModel getPlayer(String name) {
    for (UserDataModel dataModel : dataModelMap.values()) {
      if (dataModel.getName().equalsIgnoreCase(name)) {
        return dataModel;
      }
    }
    return null;
  }
}
