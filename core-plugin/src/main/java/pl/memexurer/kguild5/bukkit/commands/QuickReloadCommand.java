package pl.memexurer.kguild5.bukkit.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Dependency;
import co.aikar.commands.annotation.Description;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import pl.memexurer.kguild5.bukkit.util.PlugManUtil;

@CommandAlias("quickreload")
@CommandPermission("core.quickreload")
@Description("{@@cmd.quickreload.description}")
public class QuickReloadCommand extends BaseCommand {

  @Dependency
  private Plugin plugin;

  @Default
  public void onCommand(CommandSender sender) {
    try {
      PlugManUtil.reload(plugin);
    } catch (Throwable throwable) {
      sender.sendMessage(throwable.getMessage());
    }
    sender.sendMessage(ChatColor.GREEN + "Reload complete.");
  }
}
