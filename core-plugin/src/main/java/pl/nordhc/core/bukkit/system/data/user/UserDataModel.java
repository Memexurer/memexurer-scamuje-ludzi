package pl.nordhc.core.bukkit.system.data.user;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bson.BsonBinary;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import pl.nordhc.core.bukkit.system.data.codec.Converter;

public class UserDataModel {

  private final UUID uuid;
  private final Map<String, Location> locationMap;
  private String name;
  private boolean hasChanged;

  public UserDataModel(String name, UUID uuid, Location homes) {
    this.name = name;
    this.uuid = uuid;
    System.out.println(homes);
    this.locationMap = new HashMap<>();
    //  this.locationMap = homes;
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

  public Map<String, Location> getLocationMap() {
    return locationMap;
  }

  public void setHome(String name, Location location) {
    this.locationMap.put(name, location);
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
      document.put("homes", new Location(Bukkit.getWorld("world"), 0, 0, 0));
      return document;
    }

    public UserDataModel decode(Document dataModel) {
      return new UserDataModel(
          dataModel.get("name", String.class),
          dataModel.get("_id", UUID.class),
          dataModel.get("homes", Location.class)
      );
    }

    @Override
    public Class<UserDataModel> getConvertedClass() {
      return UserDataModel.class;
    }
  }
}
