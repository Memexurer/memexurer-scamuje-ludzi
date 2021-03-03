package pl.nordhc.core.bukkit.system.data.user;

import java.util.UUID;
import org.bukkit.entity.Player;
import pl.nordhc.core.bukkit.CorePlugin;

public final class UserUtils {

  private UserUtils() {
  }

  public static UserDataModel getPlayer(Player player) {
    return CorePlugin.getInstance().getUserHandler().getPlayer(player);
  }
  public static UserDataModel getPlayer(UUID uuid) {
    return CorePlugin.getInstance().getUserHandler().getPlayer(uuid);
  }

  public static UserDataModel getPlayer(String name) {
    return CorePlugin.getInstance().getUserHandler().getPlayer(name);
  }
}
