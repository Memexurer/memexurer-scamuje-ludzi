package pl.memexurer.kguild5.bukkit.system.data.codec;

import java.util.Arrays;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationConverter implements Converter<Location> {

  public Document encode(Location location) {
    Document document = new Document();
    document.put("world", location.getWorld().getName());
    document.put("x", location.getX());
    document.put("y", location.getY());
    document.put("z", location.getZ());
    document.put("yaw", location.getYaw());
    document.put("pitch", location.getPitch());
    return document;
  }

  public Location decode(Document document) {
    System.out.println(Arrays.toString(new Throwable().getStackTrace()));
    return new Location(
        Bukkit.getWorld(document.getString("world")),
        document.getDouble("x"),
        document.getDouble("y"),
        document.getDouble("z"),
        document.get("yaw", float.class),
        document.get("pitch", float.class)
    );
  }

  @Override
  public Class<Location> getConvertedClass() {
    return Location.class;
  }
}
