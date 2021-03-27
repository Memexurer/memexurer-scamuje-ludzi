package pl.memexurer.kguild5.bukkit.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Dependency;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Optional;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.memexurer.kguild5.bukkit.system.data.user.UserDataModel;
import pl.memexurer.kguild5.bukkit.system.data.user.UserHandler;

@CommandAlias("sethome")
@Description("{@@cmd.sethome.description}")
public class SetHomeCommand extends BaseCommand {

  @Dependency
  private UserHandler handler;

  @Default
  public void onCommand(CommandSender sender, @Optional String name) {
    if (name == null) {
      name = "home";
    }

    Player player = (Player) sender;
    int homeCount = sender.hasPermission("core.homesvip") ? 2 : 1;

    UserDataModel model = handler.getPlayer(player);
    if(model.getHomes().size() >= homeCount && !model.getHomes().containsKey(name)) {
      player.sendMessage("Posiadasz za duzo domkow!");
      return;
    }

    model.setHome(name, player.getLocation());
    player.sendMessage("ustawiono domek.");
  }
}
