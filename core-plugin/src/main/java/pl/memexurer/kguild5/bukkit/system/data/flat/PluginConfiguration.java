package pl.memexurer.kguild5.bukkit.system.data.flat;

import org.diorite.cfg.annotations.CfgComment;
import org.diorite.cfg.annotations.CfgName;
import org.diorite.cfg.annotations.CfgStringStyle;
import org.diorite.cfg.annotations.CfgStringStyle.StringStyle;

public final class PluginConfiguration {
  @CfgComment("yung adisz")
  @CfgName("yung-adisz")
  @CfgStringStyle(StringStyle.ALWAYS_QUOTED)
  public String yungAdisz = "yung adisz";
}
