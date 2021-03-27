package pl.memexurer.kguild5.bukkit.system.data.codec.converter;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import pl.memexurer.kguild5.bukkit.system.data.codec.CodecHelper;
import pl.memexurer.kguild5.bukkit.system.data.codec.Converter;

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

  public Location decode(Document document, CodecHelper helper) {
    return new Location(
        Bukkit.getWorld(document.getString("world")),
        document.getDouble("x"),
        document.getDouble("y"),
        document.getDouble("z"),
        document.getDouble("yaw").floatValue(),  // nwm czemu ale float nie dziala
        document.getDouble("pitch").floatValue() // nwm czemu ale float nie dziala
    );
  }

  @Override
  public Class<Location> getConvertedClass() {
    return Location.class;
  }
}
