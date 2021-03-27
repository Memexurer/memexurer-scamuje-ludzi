package pl.memexurer.kguild5.bukkit.system.data.user;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bson.BsonBinary;
import org.bson.Document;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import pl.memexurer.kguild5.bukkit.system.data.codec.CodecHelper;
import pl.memexurer.kguild5.bukkit.system.data.codec.Converter;

public class UserDataModel {

  private final UUID uuid;
  private final Map<String, Location> homes;
  private final List<UserBackup> backupList;
  private String name;
  private boolean hasChanged;

  public UserDataModel(String name, UUID uuid, Map<String, Location> homes,
      List<UserBackup> backupList) {
    this.name = name;
    this.uuid = uuid;
    this.homes = homes;
    this.backupList = backupList;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public UUID getUuid() {
    return uuid;
  }

  public Map<String, Location> getHomes() {
    return homes;
  }

  public void setHome(String name, Location location) {
    this.homes.put(name, location);
    this.hasChanged = true;
  }

  public List<UserBackup> getBackupList() {
    return backupList;
  }

  public void createBackup(Player player) {
    backupList.add(UserBackup.createBackup(player));
    this.hasChanged = true;
  }

  void update() {
    this.hasChanged = false;
  }

  public static class ModelConverter implements Converter<UserDataModel> {

    public Document encode(UserDataModel dataModel) {
      Document document = new Document();
      document.put("name", dataModel.name);
      document.put("_id", new BsonBinary(dataModel.uuid));
      document.put("homes", dataModel.homes);
      document.put("backups", dataModel.backupList);
      return document;
    }

    @Override
    public Class<UserDataModel> getConvertedClass() {
      return UserDataModel.class;
    }

    public UserDataModel decode(Document dataModel, CodecHelper helper) {
      return new UserDataModel(
          dataModel.get("name", String.class),
          dataModel.get("_id", UUID.class),
          helper.reinterpretMap((Document) dataModel.get("homes"), Location.class),
          helper.reinterpretList(dataModel.getList("backups", Document.class), UserBackup.class)
      );
    }
  }
}
