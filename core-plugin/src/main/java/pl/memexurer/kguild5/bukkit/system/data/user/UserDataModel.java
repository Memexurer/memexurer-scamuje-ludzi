package pl.memexurer.kguild5.bukkit.system.data.user;

import java.util.Map;
import java.util.UUID;
import org.bson.BsonBinary;
import org.bson.Document;
import org.bukkit.Location;
import pl.memexurer.kguild5.bukkit.system.data.codec.Converter;

public class UserDataModel {

  private final UUID uuid;
  private final Map<String, Location> homes;
  private String name;
  private boolean hasChanged;

  public UserDataModel(String name, UUID uuid, Map<String, Location> homes) {
    this.name = name;
    this.uuid = uuid;
    this.homes = homes;
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

  void update() {
    this.hasChanged = false;
  }

  public static class ModelConverter implements Converter<UserDataModel> {

    public Document encode(UserDataModel dataModel) {
      Document document = new Document();
      document.put("name", dataModel.name);
      document.put("_id", new BsonBinary(dataModel.uuid));
      document.put("homes", dataModel.homes);
      return document;
    }

    public UserDataModel decode(Document dataModel) {
      return new UserDataModel(
          dataModel.get("name", String.class),
          dataModel.get("_id", UUID.class),
          dataModel.get("homes", Map.class)
      );
    }

    @Override
    public Class<UserDataModel> getConvertedClass() {
      return UserDataModel.class;
    }
  }
}
