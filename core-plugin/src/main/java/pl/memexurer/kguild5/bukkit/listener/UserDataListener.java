package pl.memexurer.kguild5.bukkit.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pl.memexurer.kguild5.bukkit.system.data.user.UserDataModel;
import pl.memexurer.kguild5.bukkit.system.data.user.UserHandler;

public class UserDataListener implements Listener {

  private final UserHandler handler;

  public UserDataListener(UserHandler handler) {
    this.handler = handler;
  }

  @EventHandler
  public void onJoin(PlayerJoinEvent e) {
    UserDataModel dataModel = handler.getPlayer(e.getPlayer());
    if (!dataModel.getName().equals(e.getPlayer().getName())) {
      dataModel.setName(e.getPlayer().getName());
    }
  }
}
